import requests
from datetime import datetime as dt
import time

# address = '1194 duong lang, dong da, ha noi'
# key = '&key=AIzaSyAkLMHYfm2RZbGFdZEe8mkM_cIK9x0FY38'
# response = requests.get('https://maps.googleapis.com/maps/api/geocode/json?address=' + address.replace(" ", "+")+key)
#
# json_payload = response.json()
#
# print(json_payload['results'][0]['geometry']['location']['lat'])
# date = '2020-08-19'
# times = '09:10'
# res = datetime.datetime.strptime(date, '%Y-%m-%d').date()
# t = datetime.datetime.strptime(times, '%H:%m').time()
# print(res)
# print(t)
# now = dt.now()
# current_time = now.strftime('%Y-%m-%d %H:%M:%S');
# print(current_time);
# print(time.strftime('%H:%M:%S', time.localtime()))

from txfcm import TXFCMNotification
from twisted.internet import reactor

push_service = TXFCMNotification(api_key="AAAAhtopyIM:APA91bHCqG31uzCgKX5QxnMjdSaPsM5kXRmNomx-4g_ySvJOVn9mOpJ20Yu_L2GdLgExWU8TLbwkg6JiABhd-5wRaK_iPWYAR9v5eBX2GWH3z6odt9ifd4jWmXKFgHzase-H9ApspTH1")

# Your api-key can be gotten from:  https://console.firebase.google.com/project/<project-name>/settings/cloudmessaging
# Send to multiple devices by passing a list of ids.
registration_ids = [
    'eC3XVWVUWXM:APA91bFSXh4yDWZRgQqdRHGBdALkQEsUBYR5kVkVYdpXBNv_0VTCb6KSpa4MozuimoEyvUExWQhk5Og1OzAmXjKMU0Mg98PnOxrzPJoD9CCOwX_0QAR3fRa_sGxypYzFrKDTc4txGBfk']
message_title = "Uber update"
message_body = "Hope you're having fun this weekend, don't forget to check today's news"
df = push_service.notify_multiple_devices(registration_ids=registration_ids, message_title=message_title,
                                          message_body=message_body)


def got_result(result):
    print(result)


df.addBoth(got_result)
reactor.run()
