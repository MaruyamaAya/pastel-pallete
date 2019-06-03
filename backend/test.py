from pydub.audio_segment import AudioSegment
import subprocess

# song = AudioSegment.from_wav('test2.wav')
# song.export('test2.aac', format='aac')
# ffmpeg -i input.wav -c:a libfdk_aac -b:a 128k output.m4a
# subprocess.run(['ffmpeg', '-y', '-i', 'test2.wav', '-c:a', 'aac', '-b:a', '128k', 'test2.aac'])
subprocess.run(['ffmpeg', '-y', '-i', 'test2.aac', 'test3.wav'])
