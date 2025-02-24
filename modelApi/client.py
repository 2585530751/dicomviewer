import requests
 
# user_info = {'name': 'jack', 'password': '123'}
# r = requests.post("http://127.0.0.1:8085/model/first", json=user_info)

file_data = {'file': open('apple.jpg', 'rb')}
user_info = {'info': 'Lenna'}
r = requests.post("http://127.0.0.1:8085/model/segment", data=user_info, files=file_data)

print(r.text)