### 文字转语音



这里没有采用什么阿里云的API接口

耍了一点小聪明



这里我们来使用百度翻译的API，不是正规渠道提供的那种，下面url是通过检查分析得到

```html
https://fanyi.baidu.com/gettts?lan=zh&text=%E4%BD%A0%E5%A5%BD%E5%90%97&spd=5&source=web

param:
		lan: zh
		text: 你好吗
		spd: 5
		source: web

```



访问这个url即可传出一个名为**tts.mp3**文件



