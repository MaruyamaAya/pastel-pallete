from urllib import request
import json
import time

URL = 'http://openapi.tuling123.com/openapi/api/v2'
API_KEY = "f2cfb0951b3e40aa9426fa64e20ce0f3"
USER_ID = "429885"


def chat_robot(ques):
    t1 = time.time()
    # 请求体数据
    request_data = {
        "reqType": 0,
        "perception": {
            "inputText": {
                "text": ques
            }
        },
        "userInfo": {
            "apiKey": API_KEY,
            "userId": USER_ID
        }
    }

    headers = {
        "content-type": "application/json"
    }

    req = request.Request(url=URL,
                          headers=headers,
                          data=json.dumps(request_data).encode("utf-8"))

    response = request.urlopen(req).read().decode("utf-8")
    d = json.loads(response)
    t2 = time.time()
    print('chatbot:', t2 - t1)
    return d['results'][0]['values']["text"]


if __name__ == '__main__':
    print(chat_robot("你是谁？"))
