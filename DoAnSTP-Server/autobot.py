import requests
from txfcm import TXFCMNotification
from twisted.internet import reactor
import threading
import time

url = 'http://192.168.0.105:5000/'

def auto_accepted_match():
    while True:
        response = requests.get(url + 'autoaccept')
        print(time.strftime('%Y-%m-%d %H:%M:%S', time.localtime()) + ": updated matches: " + str(
            response.json()['result']))
        time.sleep(60)


if __name__ == "__main__":
    thread = threading.Thread(target=auto_accepted_match)
    print("start thread...")
    thread.start()
    thread.join()
