# coding=utf-8
# github: https://github.com/Baidu-AIP/speech-demo/tree/master/rest-api-asr/python

import sys
import json
import time

IS_PY3 = sys.version_info.major == 3

if IS_PY3:
    from urllib.request import urlopen
    from urllib.request import Request
    from urllib.error import URLError
    from urllib.parse import urlencode

    timer = time.perf_counter
else:
    import urllib2
    from urllib2 import urlopen
    from urllib2 import Request
    from urllib2 import URLError
    from urllib import urlencode

    if sys.platform == "win32":
        timer = time.clock
    else:
        # On most other platforms the best timer is time.time()
        timer = time.time

API_KEY = 'iReHkSNHqeVxE0NH3aiVKKAO'
SECRET_KEY = 'yvtPvFr4uk6YiF4vH1B79OACo0uEBWFU'

# 需要识别的文件
# AUDIO_FILE = 'result.wav'  # 只支持 pcm/wav/amr
# 文件格式
# FORMAT = AUDIO_FILE[-3:]  # 文件后缀只支持 pcm/wav/amr

CUID = '123456PYTHON'
# 采样率
RATE = 16000  # 固定值

# 免费版

DEV_PID = 1536  # 1537 表示识别普通话，使用输入法模型。1536表示识别普通话，使用搜索模型。根据文档填写PID，选择语言及识别模型
ASR_URL = 'http://vop.baidu.com/server_api'
SCOPE = 'audio_voice_assistant_get'  # 有此scope表示有asr能力，没有请在网页里勾选，非常旧的应用可能没有


# ASR_URL = 'https://vop.baidu.com/pro_api'
# SCOPE = 'brain_enhanced_asr'  # 有此scope表示有收费极速版能力，没有请在网页里开通极速版


# 收费极速版 打开注释的话请填写自己申请的appkey appSecret ，并在网页中开通极速版

# DEV_PID = 80001
# ASR_URL = 'https://vop.baidu.com/pro_api'
# SCOPE = 'brain_enhanced_asr'  # 有此scope表示有asr能力，没有请在网页里开通极速版

# 忽略scope检查，非常旧的应用可能没有
# SCOPE = False


# 收费极速版

class DemoError(Exception):
    pass


"""  TOKEN start """

TOKEN_URL = 'http://openapi.baidu.com/oauth/2.0/token'


def fetch_token():
    params = {'grant_type': 'client_credentials',
              'client_id': API_KEY,
              'client_secret': SECRET_KEY}
    post_data = urlencode(params)
    if IS_PY3:
        post_data = post_data.encode('utf-8')
    req = Request(TOKEN_URL, post_data)
    try:
        f = urlopen(req)
        result_str = f.read()
    except URLError as err:
        # print('token http response http code : ' + str(err.code))
        result_str = err.read()
    if IS_PY3:
        result_str = result_str.decode()

    # print(result_str)
    result = json.loads(result_str)
    # print(result)
    if 'access_token' in result.keys() and 'scope' in result.keys():
        if SCOPE and (not SCOPE in result['scope'].split(' ')):  # SCOPE = False 忽略检查
            raise DemoError('scope is not correct')
        # print('SUCCESS WITH TOKEN: %s ; EXPIRES IN SECONDS: %s' % (result['access_token'], result['expires_in']))
        return result['access_token']
    else:
        raise DemoError('MAYBE API_KEY or SECRET_KEY not correct: access_token or scope not found in token response')


"""  TOKEN end """


def voice2words(input_path, output_path=None):
    t1 = time.time()
    token = fetch_token()

    """
    httpHandler = urllib2.HTTPHandler(debuglevel=1)
    opener = urllib2.build_opener(httpHandler)
    urllib2.install_opener(opener)
    """

    format_ = input_path[-3:]
    speech_data = []
    with open(input_path, 'rb') as speech_file:
        speech_data = speech_file.read()
    length = len(speech_data)
    if length == 0:
        raise DemoError('file %s length read 0 bytes' % input_path)

    params = {'cuid': CUID, 'token': token, 'dev_pid': DEV_PID}
    params_query = urlencode(params)

    headers = {
        'Content-Type': 'audio/' + format_ + '; rate=' + str(RATE),
        'Content-Length': length
    }

    url = ASR_URL + "?" + params_query
    # print("url is", url)
    # print("header is", headers)
    # print post_data
    req = Request(ASR_URL + "?" + params_query, speech_data, headers)
    try:
        begin = timer()
        f = urlopen(req)
        result_str = f.read()
        # print("Request time cost %f" % (timer() - begin))
    except URLError as err:
        # print('asr http response http code : ' + str(err.code))
        result_str = err.read()

    if IS_PY3:
        result_str = str(result_str, 'utf-8')
    d = json.loads(result_str)
    # print(d)
    result_str = d["result"][0]
    # print(result_str)
    if output_path:
        with open(output_path, "w", encoding='utf-8') as of:
            of.write(result_str)
    t2 = time.time()
    print('voice2words:', t2 - t1)
    return result_str


if __name__ == '__main__':
    voice2words('result.wav', 'result.txt')
