'''
emotion:
    l1      str         emotion level 1
    l2      str         emotion level 2, maybe ''
    prob    float[3]    probability of o, n, p
'''
# 情绪一级分类标签；optimistic（正向情绪）、neutral（中性情绪）、pessimistic（负向情绪）
EMOTION_L1 = ['optimistic', 'neutral', 'pessimistic']
# 情绪二级分类标签：闲聊模型正向（like喜爱、happy愉快）、闲聊模型负向（angry愤怒、disgusting厌恶、fearful恐惧、sad悲伤）
EMOTION_L2 = ['like', 'happy', 'angry', 'disgusting', 'fearful', 'sad', '']


def emotion2int(emotion):
    l1 = emotion['l1']
    l2 = emotion['l2']
    prob = emotion['prob']
    return EMOTION_L1.index(l1)
