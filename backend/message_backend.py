import json
import sys
import time
from chatbot import chat_robot
from query import query
from voice2words import voice2words
from words2voice import words2voice
from emotion import emotion

# 情绪一级分类标签；optimistic（正向情绪）、neutral（中性情绪）、pessimistic（负向情绪）
EMOTION_L1 = ['pessimistic', 'neutral', 'optimistic']
# 情绪二级分类标签：闲聊模型正向（like喜爱、happy愉快）、闲聊模型负向（angry愤怒、disgusting厌恶、fearful恐惧、sad悲伤）
EMOTION_L2 = ['like', 'happy', 'angry', 'disgusting', 'fearful', 'sad', '']


def message_backend(req):
    '''
    process message, get emotion values and voice
    :param req: request, a dir contains:
                user_id     str     user id
                input_type  int     0-voice, 1-text
                req_type    int     0-pvp, 1-pve, 2-query
                input_val   str     input text or wav path(absolute)
    :return: a dir contains:
                err         str     err msg if sth is wrong, else ''
                voice_path  str     path of output voice, '' if not necessary
                emotions    dir     result of emotion, contains:
                                l1      str         emotion level 1
                                l2      str         emotion level 2, maybe ''
                                prob    float[3]    probability of o, n, p
                bot         dir     voice and emotion of bot, contains:
                                voice_path
                                emotions
    '''

    def get_empty_resp():
        return {
            'err': '',
            'voice_path': '',
            'emotion': {
                'l1': 'neutral',
                'l2': '',
                'prob': [0., 1., 0.]
            },
            'bot': {
                'voice_path': '',
                'emotion': {
                    'l1': 'neutral',
                    'l2': '',
                    'prob': [0., 1., 0.]
                }
            }
        }

    def err_(err_info):
        resp = get_empty_resp()
        resp['err'] = err_info
        return resp

    user_id = req['user_id']
    input_type = req['input_type']
    req_type = req['req_type']
    input_val = req['input_val']
    if input_type == 0:
        voice_path = input_val
        text = voice2words(voice_path)
    elif input_type == 1:
        voice_path = ''
        text = input_val
    else:
        return err_('unexpected input_type: ' + str(input_type))

    resp = get_empty_resp()
    l1, l2, prob = emotion(text)
    if l1 not in EMOTION_L1:
        return err_('unexpected emotion l1:' + l1)
    if l2 not in EMOTION_L2:
        return err_('unexpected emotion l2:' + l2)

    if req_type == 0:
        # pvp
        if not voice_path:
            voice_path = sys.path[0] + '\\' + user_id + '.wav'
            words2voice(text, voice_path)
    elif req_type == 1 or req_type == 2:
        if req_type == 1:
            # pve
            reply = chat_robot(text)
        else:
            # query
            reply = query(text)
        l1_, l2_, prob_ = emotion(reply)
        if l1_ not in EMOTION_L1:
            return err_('unexpected emotion l1_:' + l1_)
        if l2_ not in EMOTION_L2:
            return err_('unexpected emotion l2_:' + l2_)
        voice_path_ = sys.path[0] + '\\' + user_id + '_bot.wav'
        words2voice(reply, voice_path_)
        resp['bot']['voice_path'] = voice_path_
        resp['bot']['emotion']['l1'] = l1_
        resp['bot']['emotion']['l2'] = l2_
        resp['bot']['emotion']['prob'] = prob_
    else:
        return err_('unexpected req_type: ' + str(req_type))

    resp['voice_path'] = voice_path
    resp['emotion']['l1'] = l1
    resp['emotion']['l2'] = l2
    resp['emotion']['prob'] = prob
    return resp


if __name__ == '__main__':
    # d = json.loads(sys.argv[1])
    d = {
        'user_id': 'test1',
        'input_type': 0,
        'req_type': 1,
        'input_val': 'E:\\PythonProjects\\Live2D_Backend\\test_bot.wav'
    }
    t1 = time.time()
    r = message_backend(d)
    t2 = time.time()
    print(r)
    print(t2 - t1)
