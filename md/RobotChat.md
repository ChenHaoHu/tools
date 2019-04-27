### 智能陪聊



> 要求 

图灵API接口  <http://www.turingapi.com/>

示例代码 (Python)

```python
import requests
import io
import sys
import json 

sys.stdout = io.TextIOWrapper(sys.stdout.buffer,encoding='utf-8')

def chat(str):
    params = {
    "perception": {
        "inputText": {
            "text": str
        }
    },
    "userInfo": {
        "apiKey": "****************",
        "userId": "123456"
    }
    }
    res = requests.post("http://openapi.tuling123.com/openapi/api/v2",json=params);
    res=res.json();
    print(res["results"][0]["values"]["text"]+"\n")


while 1:
    str = input("input:");
    chat(str)
     
```



运行结果:

```shell
# hcy @ hcy in ~/Desktop [23:22:35] C:127
$ python3.7 Main.py 
input:你好?
默默飘过来～

input:嗯啦
嗯呢，我在听呢。

input:你我唠唠把
唠几块钱的。

input:
```



***java代码看仓库***



