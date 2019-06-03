import subprocess


def wav2acc(wav, aac):
    subprocess.run(['ffmpeg', '-y', '-i', wav, '-c:a', 'aac', '-b:a', '128k', aac])


def aac2wav(aac, wav):
    subprocess.run(['ffmpeg', '-y', '-i', aac, wav])
