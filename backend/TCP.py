import socket
from database import *
from message.message_backend import message_backend
from gesture_chooser import emotion2int
from wav_aac import wav2acc, aac2wav


class MessageDealer(threading.Thread):
    def __init__(self, msg, client_s):
        super(MessageDealer, self).__init__()
        self.in_msg = msg
        self.client_s = client_s

    def run(self):
        # {content}:
        #   - {"content_type": "text", "text": str, "emo": int, 'another': bool, 'time': str}
        #   - {"content_type": "voice", "voice": str, "emo": int, 'another': bool, 'time': str}
        in_msg = self.in_msg
        msg_type = in_msg['msg_type']
        out_msg = {}
        out_files = []
        if msg_type == 'log_in':
            # log_in请求："user_name": str，"password": str, "msg_type": "log_in"
            # 返回："state": "success/failure"
            user_name = in_msg['user_name']
            password = in_msg['password']
            ret = log_in(user_name, password)
            out_msg = {"state": "success" if ret else "failure"}
        elif msg_type == 'sign_up':
            # sign_up请求："user_name": str, "password": str,"msg_type": "sign_up"
            # 返回："state": "success/failure"
            user_name = in_msg['user_name']
            password = in_msg['password']
            ret = sign_up(user_name, password)
            out_msg = {"state": "success" if ret else "failure"}
        elif msg_type == 'get_contact':
            # get_contact请求："user_name": str, "msg_type": "get_contact"
            # 返回："contacts": [{"user_name": str, "last_msg": str/"voice"}, ...]
            user_name = in_msg['user_name']
            ret = get_contact(user_name)
            out_msg = ret
        elif msg_type == 'get_message':
            # get_message请求："from": str, "to": str, "msg_type": "get_message"
            # 返回："messages": [{content}, ...], "num_of_voices": int
            # 然后依次发给我语音的文件，文件名称为发送时间.aac 例：Jun_2__2019_12_14_54_PM.aac
            pa = in_msg['from']
            pb = in_msg['to']
            ret = get_message(pa, pb)
            out_msg = ret
            out_files = [c['voice'] for c in ret['messages'] if c['content_type'] == 'voice']
        elif msg_type == 'add_friend':
            # add_friend请求："from": str, "to": str, "msg_type": "add_friend"
            # 返回："state": "success/failure"
            pa = in_msg['from']
            pb = in_msg['to']
            ret = add_friend(pa, pb)
            out_msg = {"state": "success" if ret else "failure"}
        elif msg_type == 'send_text':
            # send_text请求: "from": str, "to", str, "content": str, "time": "时间，格式同上例", "msg_type": "send_text"
            # 返回：向两方客户端发送"refresh"字符串，不用json直接传字符串就行
            req = {
                'user_id': in_msg['from'],
                'input_type': 1,
                'req_type': 0 if in_msg['to'] else 1,
                'input_val': in_msg['content']
            }
            resp = message_backend(req)
            pa = in_msg['from']
            pb = in_msg['to']
            text = in_msg['content']
            emo = resp['emotion']
            time = in_msg['time']
            msg = {"content_type": "text", "text": text, "emo": emotion2int(emo), 'another': False, 'time': time}
            add_msg(pa, pb, msg)
            if not pb:
                # PVE
                bot = resp['bot']
                abs_voice_wav = bot['voice_path']
                rel_voice_aac = 'data/ai_' + time + '.aac'
                data_voice_aac = 'ai_' + time + '.aac'
                wav2acc(abs_voice_wav, rel_voice_aac)
                emo = bot['emotion']
                msg2 = {"content_type": "voice", "voice": data_voice_aac, "emo": emotion2int(emo), 'another': True,
                        'time': time}
                add_msg(pa, pb, msg2)
            out_msg = 'refresh'
        elif msg_type == 'send_voice':
            # send_voice请求："from": str, "to", str, "time": "时间，格式同上例"，"msg_type": "send_voice"
            # 接收语音文件并进行保存
            # 然后向两方客户端发送“refresh”字符串
            rel_voice_aac = 'data/' + in_msg['time'] + '.aac'
            rel_voice_wav = 'data/' + in_msg['time'] + '.wav'
            aac2wav(rel_voice_aac, rel_voice_wav)
            req = {
                'user_id': in_msg['from'],
                'input_type': 0,
                'req_type': 0 if in_msg['to'] else 1,
                'input_val': rel_voice_wav
            }
            resp = message_backend(req)
            pa = in_msg['from']
            pb = in_msg['to']
            data_voice_aac = in_msg['time'] + '.acc'
            emo = resp['emotion']
            time = in_msg['time']
            msg = {"content_type": "voice", "voice": data_voice_aac, "emo": emotion2int(emo), 'another': False,
                   'time': time}
            add_msg(pa, pb, msg)
            if not pb:
                # PVE
                bot = resp['bot']
                abs_voice_wav = bot['voice_path']
                rel_voice_aac = 'data/ai_' + time + '.aac'
                data_voice_aac = 'ai_' + time + '.aac'
                wav2acc(abs_voice_wav, rel_voice_aac)
                emo = bot['emotion']
                msg2 = {"content_type": "voice", "voice": data_voice_aac, "emo": emotion2int(emo), 'another': True,
                        'time': time}
                add_msg(pa, pb, msg2)
            out_msg = 'refresh'

        if out_msg:
            send_str(out_msg, self.client_s)
            for file in out_files:
                send_file(file, self.client_s)
        else:
            assert False, 'MessageDealer Error: No Such Type' + str(msg_type)


server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)


def send_str(msg, client):
    msg = json.dumps(msg)
    print('##### send str:', msg, '#####')
    client.send(msg.encode('utf-8'))
    print('##### success: send str #####')


def send_file(file_name, client):
    print('##### send file:', file_name, '#####')
    file_path = 'data/' + file_name
    file_size = os.path.getsize(file_path)
    head = {
        'file_name': file_name,
        'file_size': file_size
    }
    head_info = json.dumps(head)
    head_info_len = len(head_info)
    client.send(json.dumps(head_info_len).encode('utf-8'))
    ack = client.recv(100000)
    if ack.decode('utf-8') == 'received':
        pass
    else:
        print('##### client rejected file #####')
    client.send(head_info.encode('utf-8'))
    with open(file_path, 'rb') as f:
        data = f.read()
        client.sendall(data)
    print('##### success: send file #####')


def recv_json(length, client):  # 接收并打包为json
    data = client.recv(length)
    data = data.decode('utf-8')
    print('##### receive msg:', data, '#####')
    res = json.loads(data)
    return res


if __name__ == '__main__':
    port = 7777
    server_socket.bind(('0.0.0.0', port))
    server_socket.listen(20)

    while True:
        print("##### waiting for connection #####")
        client_socket, addr = server_socket.accept()
        print("##### connected to %s #####" % str(addr))
        try:
            msg = recv_json(10000000, client_socket)
            thread = MessageDealer(msg, client_socket)
            thread.run()
        except:
            send_str("NACK", client_socket)
        client_socket.close()
