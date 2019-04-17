import json
import time
from aip import AipNlp

APP_ID = '16036471'
API_KEY = 'iReHkSNHqeVxE0NH3aiVKKAO'
SECRET_KEY = 'yvtPvFr4uk6YiF4vH1B79OACo0uEBWFU'


def emotion(s):
    t1 = time.time()
    client = AipNlp(APP_ID, API_KEY, SECRET_KEY)
    d = client.emotion(s)
    # print(d)
    label1 = d['items'][0]['label']
    label2 = d['items'][0]['subitems'][0]['label'] if len(d['items'][0]['subitems']) else ''
    prob = [0., 0., 0.]
    for item in d['items']:
        label = item['label']
        if label == 'optimistic':
            prob[0] = item['prob']
        elif label == 'neutral':
            prob[1] = item['prob']
        else:
            prob[2] = item['prob']
    t2 = time.time()
    print('emotion:', t2 - t1)
    return label1, label2, prob


if __name__ == '__main__':
    print(emotion('吓死我了！'))

'''
{
    'log_id': 5315853066651554417, 
    'text': '本来今天高高兴兴', 
    'items': [
        {
            'subitems': [{'prob': 0.501008, 'label': 'happy'}], 
            'replies': ['笑得真可爱'], 
            'prob': 0.501008, 
            'label': 'optimistic'
        }, 
        {'subitems': [], 'replies': [], 'prob': 0.49872, 'label': 'neutral'}, 
        {'subitems': [], 'replies': [], 'prob': 0.000272128, 'label': 'pessimistic'}]}
'''