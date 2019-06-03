import socket
import json

ts = socket.socket()
port = 7777
ts.connect(('172.16.4.182', port))
data = {"msg_type": "log_in", "user_name": "lzm", "password": "123456"}
json_data = json.dumps(data)
json_data = json_data.encode('utf-8')
ts.send(json_data)
result = ts.recv(1024)
print(result.decode('utf-8'))
