### 语音识别(一句话识别) 



> 要求

* 阿里云智能语音 <https://help.aliyun.com/product/30413.html>



> 技术处理

* 音频转码处理

  1. 音频要求

     - 支持音频编码格式：pcm(无压缩的pcm文件或wav文件)、opus，16bit采样位数的单声道(mono)；
     - 支持音频采样率：8000Hz、16000Hz；
     - 支持对返回结果进行设置：是否返回中间识别结果，是否在后处理中添加标点，是否将中文数字转为阿拉伯数字输出。

  2. 转码方案

      首先想到的是神器 [ffmpeg](<https://baike.baidu.com/item/ffmpeg/2665727?fr=aladdin>) ,然而这哥们异常的不好用，操作不方便，在服务器上安装的时候贼麻烦。后来重新明确了需求，我们主要是需要对音频进行转换格式即转换音频格式到```wav```格式、转换音频的采样率和转换音频的声道。故我们使用了比较合适的轻量级工具 **SOX (Sound eXchange)**,贼好用，教程在下方

     * 学习资料 <https://www.jianshu.com/p/be8977de4a6b>


转换代码：
```java
     		String cmd1 = "lame "+ file + " " + name+".wav --decode";
            Process exec1 = Runtime.getRuntime().exec(cmd1);
            exec1.waitFor();
            String cmd2 = "sox " + name +".wav -r 16000 -c 1 "+name+"_tras.wav";
            Process exec2 = Runtime.getRuntime().exec(cmd2);
            exec2.waitFor();

```


* 阿里云接口处理

  1. 这里阿里云接口的API的交互流程图

     ![阿里云接口的API的交互流程图](http://docs-aliyun.cn-hangzhou.oss.aliyun-inc.com/assets/pic/84442/cn_zh/1530077684486/SpeechRecognizer.png)

	2. . API接口代码

```java
/**
 * SpeechRecognizerDemo class
 *
 * 一句话识别Demo
 */
public class SpeechRecognizerDemo {
    private String appKey;
    private String accessToken;
    NlsClient client;
    /**
     * @param appKey
     * @param token
     */
    public SpeechRecognizerDemo(String appKey, String token) {
        this.appKey = appKey;
        this.accessToken = token;
        // Step0 创建NlsClient实例,应用全局创建一个即可,默认服务地址为阿里云线上服务地址
        client = new NlsClient(accessToken);
    }
    private static SpeechRecognizerListener getRecognizerListener() {
        SpeechRecognizerListener listener = new SpeechRecognizerListener() {
            // 识别出中间结果.服务端识别出一个字或词时会返回此消息.仅当setEnableIntermediateResult(true)时,才会有此类消息返回
            @Override
            public void onRecognitionResultChanged(SpeechRecognizerResponse response) {
                // 事件名称 RecognitionResultChanged
                System.out.println("name: " + response.getName() +
                        // 状态码 20000000 表示识别成功
                        ", status: " + response.getStatus() +
                        // 一句话识别的中间结果
                        ", result: " + response.getRecognizedText());
            }
            // 识别完毕
            @Override
            public void onRecognitionCompleted(SpeechRecognizerResponse response) {
                // 事件名称 RecognitionCompleted
                System.out.println("name: " + response.getName() +
                        // 状态码 20000000 表示识别成功
                        ", status: " + response.getStatus() +
                        // 一句话识别的完整结果
                        ", result: " + response.getRecognizedText());
            }
        };
        return listener;
    }
    public void process(InputStream ins) {
        SpeechRecognizer recognizer = null;
        try {
            // Step1 创建实例,建立连接
            recognizer = new SpeechRecognizer(client, getRecognizerListener());
            recognizer.setAppKey(appKey);
            // 设置音频编码格式
            recognizer.setFormat(InputFormatEnum.PCM);
            // 设置音频采样率
            recognizer.setSampleRate(SampleRateEnum.SAMPLE_RATE_16K);
            // 设置是否返回中间识别结果
            recognizer.setEnableIntermediateResult(true);
            // Step2 此方法将以上参数设置序列化为json发送给服务端,并等待服务端确认
            recognizer.start();
            // Step3 语音数据来自声音文件用此方法,控制发送速率;若语音来自实时录音,不需控制发送速率直接调用 recognizer.send(ins)即可
            recognizer.send(ins, 3200, 100);
            // Step4 通知服务端语音数据发送完毕,等待服务端处理完成
            recognizer.stop();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            // Step5 关闭连接
            if (null != recognizer) {
                recognizer.close();
            }
        }
    }
    public void shutdown() {
        client.shutdown();
    }
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("SpeechRecognizerDemo need params: <app-key> <token>");
            System.exit(-1);
        }
        String appKey = args[0];
        String token = args[1];
        SpeechRecognizerDemo demo = new SpeechRecognizerDemo(appKey, token);
        InputStream ins = SpeechRecognizerDemo.class.getResourceAsStream("/nls-sample-16k.wav");
        if (null == ins) {
            System.err.println("open the audio file failed!");
            System.exit(-1);
        }
        demo.process(ins);
        demo.shutdown();
    }
}

```

这里可以发现 **Step4 通知服务端语音数据发送完毕,等待服务端处理完成**说明客户端发送请求过后是等待识别完毕然后结束process方法，因为监听的**SpeechRecognizerListener**是个**abstract**类，必须在new得时候实现他的监听方法，所以监听里获得的数据无法通过局部变量传给调用者，如果直接使用一个成员变量接收，会出现线程安全问题。所以在service里实现一个成员变量**ConcurrentHashMap**以**taskid**为key，接收到的内容为value。

修改过后的代码如下

```java
ConcurrentHashMap<String,String> data = new ConcurrentHashMap<>();

    /**
     * @param appKey
     * @param token
     */
    {
        // Step0 创建NlsClient实例,应用全局创建一个即可,默认服务地址为阿里云线上服务地址
        client = new NlsClient(accessToken);

        logger = LoggerFactory.getLogger("SpeechRecognizerDemo.class");
    }



    public  String process(InputStream ins) {
        SpeechRecognizer recognizer = null;
        String taskId = "";

        SpeechRecognizerListener listener = new SpeechRecognizerListener() {
            @Override
            public void onRecognitionCompleted(SpeechRecognizerResponse response) {
                // 事件名称 RecognitionCompleted
                System.out.println("name: " + response.getName() +
                        // 状态码 20000000 表示识别成功
                        ", status: " + response.getStatus() +
                        // 一句话识别的完整结果
                        ", result: " + response.getRecognizedText());
         data.put(response.getTaskId(),response.getRecognizedText());
            }
        };

        try {
            // Step1 创建实例,建立连接
            recognizer = new SpeechRecognizer(client, listener);
            recognizer.setAppKey(appKey);
            // 设置音频编码格式
            recognizer.setFormat(InputFormatEnum.PCM);
            // 设置音频采样率
            recognizer.setSampleRate(SampleRateEnum.SAMPLE_RATE_16K);
            // 设置是否返回中间识别结果
            recognizer.setEnableIntermediateResult(false);
            // Step2 此方法将以上参数设置序列化为json发送给服务端,并等待服务端确认
            recognizer.start();
            // Step3 语音数据来自声音文件用此方法,控制发送速率;若语音来自实时录音,不需控制发送速率直接调用 recognizer.send(ins)即可
            recognizer.send(ins);
            // Step4 通知服务端语音数据发送完毕,等待服务端处理完成
            recognizer.stop();
            taskId = recognizer.getTaskId();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            // Step5 关闭连接
            if (null != recognizer) {
                recognizer.close();
            }

            if (null!=ins){
                try {
                    ins.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return taskId;

    }
```







> 效果演示

暂无



