import os
import json
import threading


add_msg_lock = threading.Lock()
add_friend_lock = threading.Lock()


class MessageManagement:
    def __init__(self, user1_index, user2_index):
        self.msg_list = []
        if (user1_index != 0 and user1_index < user2_index) or user2_index == 0:
            self.user1_index = user1_index
            self.user2_index = user2_index
        else:
            self.user1_index = user2_index
            self.user2_index = user1_index
        self.file_path = "./UserDataBase/" + str(self.user1_index) + "_" + str(self.user2_index) + ".msg"
        if not os.path.isfile(self.file_path):
            try:
                file = open(self.file_path, "w")
                file.close()
            except:
                print("创建对话记录文件失败")

    def get_last_msg(self):
        self.update_msg_list()
        if len(self.msg_list) > 0:
            return self.msg_list[0]
        return "text", self.user1_index, "", 0, ''

    def update_msg_list(self):
        file = open(self.file_path, "r")
        self.msg_list = []
        try:
            read_buffer = file.read().splitlines()
            read_buffer_size = len(read_buffer)
            i = 0
            while i < read_buffer_size:
                msg_type = read_buffer[i]
                user_index = int(read_buffer[i + 1])
                text = read_buffer[i + 2]
                emo = int(read_buffer[i + 3])
                time = read_buffer[i + 4]
                self.msg_list.append((msg_type, user_index, text, emo, time))
                i += 5
        finally:
            file.close()

    def get_msg_list(self):
        self.update_msg_list()
        return self.msg_list

    def add_msg(self, msg_type, user_index, text, emo, time):
        with add_friend_lock:
            self.update_msg_list()
            self.msg_list.insert(0, (msg_type, user_index, text, emo, time))
            if len(self.msg_list) > 20:
                self.msg_list.pop()
            file = open(self.file_path, "w")
            write_buffer = []
            for msg_type, user_index, text, emo, time in self.msg_list:
                write_buffer.append(msg_type + "\n")
                write_buffer.append(str(user_index) + "\n")
                write_buffer.append(text + "\n")
                write_buffer.append(str(emo) + "\n")
                write_buffer.append(time + "\n")
            try:
                file.writelines(write_buffer)
            finally:
                file.close()


class User:
    def __init__(self, name, index):
        self.name = name
        self.index = index
        self.password = ''
        self.friend_index_list = []

    def create(self, password):
        self.password = password
        self.friend_index_list = []
        user_file_path = "./UserDataBase/" + str(self.index) + ".data"
        user_file = open(user_file_path, "w")
        try:
            user_file.write(self.password + '\n')
        finally:
            user_file.close()

    def is_friend_exist(self, friend):
        self.update_friend_list()
        for index in self.friend_index_list:
            if friend.index == index:
                return True
        return False

    def add_friend(self, friend):
        user_file_path = "./UserDataBase/" + str(self.index) + ".data"
        user_file = open(user_file_path, "a")
        try:
            user_file.write(str(friend.index) + '\n')
        finally:
            user_file.close()

    def update_password(self):
        user_file_path = "./UserDataBase/" + str(self.index) + ".data"
        user_file = open(user_file_path, "r")
        try:
            self.password = user_file.readline().splitlines()[0]
        finally:
            user_file.close()

    def update_friend_list(self):
        user_file_path = "./UserDataBase/" + str(self.index) + ".data"
        user_file = open(user_file_path, "r")
        try:
            read_buffer = user_file.read().splitlines()
            del (read_buffer[0])
            self.friend_index_list = []
            for friend in read_buffer:
                self.friend_index_list.append(int(friend))
        finally:
            user_file.close()


class DataBase:
    def __init__(self):
        database_path = "./UserDataBase"
        if not os.path.exists(database_path):
            try:
                os.mkdir(database_path)
            except:
                print("创建Database目录失败")
        user_list_file_path = "./UserDataBase/UserList.data"
        if not os.path.isfile(user_list_file_path):
            try:
                file = open(user_list_file_path, "w")
                file.write("\n")
                file.close()
            except:
                print("创建用户总目录失败")

        self.user_count = 1
        self.user_list = []
        self.update_user_list()

    def update_user_list(self):
        user_list_file_path = "./UserDataBase/UserList.data"
        try:
            user_list_file = open(user_list_file_path, "r")
        except:
            print("打开用户总文件失败")
        try:
            read_buffer = user_list_file.read().splitlines()
        finally:
            user_list_file.close()

        user_index = 0
        for user_name in read_buffer:
            self.user_list.append((user_name, user_index))
            user_index += 1
        self.user_count = user_index

    def add_user(self, user_name):
        with add_friend_lock:
            user_list_file_path = "./UserDataBase/UserList.data"
            user_list_file = open(user_list_file_path, "a")
            try:
                user_list_file.write(user_name + '\n')
            finally:
                user_list_file.close()
            self.update_user_list()

    def get_user_index(self, user_name):
        self.update_user_list()
        for name, index in self.user_list:
            if name == user_name:
                return index
        return -1

    def get_user_name(self, user_index):
        self.update_user_list()
        for name, index in self.user_list:
            if index == user_index:
                return name
        return None

    def is_user_exist(self, user_name):
        return self.get_user_index(user_name) != -1


db = DataBase()


# sign_up: String->String->Boolean
def sign_up(name, password):
    global db
    if db.is_user_exist(name):
        return False
    db.add_user(name)
    user = User(name, db.get_user_index(name))
    user.create(password)
    return True


# log_in: String->String->Boolean
def log_in(name, password):
    global db
    if not db.is_user_exist(name):
        return False
    user = User(name, db.get_user_index(name))
    user.update_password()
    if password != user.password:
        return False
    return True


# add_friend: String->String->Boolean
def add_friend(user1_name, user2_name):
    global db
    if (not db.is_user_exist(user1_name)) or (not db.is_user_exist(user2_name)):
        return False
    user1 = User(user1_name, db.get_user_index(user1_name))
    user2 = User(user2_name, db.get_user_index(user2_name))
    if user1.is_friend_exist(user2):
        return False
    user1.add_friend(user2)
    user2.add_friend(user1)
    return True


# add_msg: String->String->Message->Void
def add_msg(user1_name, user2_name, message):
    global db
    user1_index = db.get_user_index(user1_name)
    user2_index = db.get_user_index(user2_name)
    control = MessageManagement(user1_index, user2_index)
    msg_type = message['content_type']
    text = message['text']
    user_index = user2_index if message['another'] else user1_index
    emo = message['emo']
    time = message['time']
    control.add_msg(msg_type, user_index, text, emo, time)
    return


# msq_req: String->String->Json
def get_message(user1_name, user2_name):
    global db
    user1_index = db.get_user_index(user1_name)
    user2_index = db.get_user_index(user2_name)
    control = MessageManagement(user1_index, user2_index)
    msg_list = control.get_msg_list()[::-1]
    content_list = []
    num_of_voices = 0
    for msg_type, user_index, text, emo, time in msg_list:
        another = user_index != user1_index
        if msg_type == "voice":
            num_of_voices += 1
            content_list.append({"content_type": "voice", "text": text, 'another': another, 'emo': emo, 'time': 1})
        else:
            content_list.append({"content_type": "text", "text": text, 'another': another, 'emo': emo, 'time': 1})
    return {"messages": content_list, "num_of_voices": num_of_voices}


# friend_list_req: String->Json
def get_contact(user_name):
    global db
    user = User(user_name, db.get_user_index(user_name))
    user.update_friend_list()
    last_msg_list = []
    for friend_index in user.friend_index_list:
        last_msg = MessageManagement(user.index, friend_index).get_last_msg()
        if last_msg[0] == "voice":
            last_msg_list.append({"user_name": db.get_user_name(friend_index), "last_msg": "voice",
                                  "time": last_msg[4]})
        else:
            last_msg_list.append({"user_name": db.get_user_name(friend_index), "last_msg": last_msg[2],
                                  "time": last_msg[4]})
    return {"contacts": last_msg_list}
