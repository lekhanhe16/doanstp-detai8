import json
from flask import Flask, request, jsonify
from flask_mysqldb import MySQL
from datetime import datetime as dt
from txfcm import TXFCMNotification
from twisted.internet import reactor
from threading import Thread
import time

app = Flask(__name__)
app.config['MYSQL_HOST'] = '0.0.0.0'
app.config['MYSQL_USER'] = 'root'
app.config['MYSQL_PASSWORD'] = ''
app.config['MYSQL_DB'] = 'doanstp'

mysql = MySQL(app)
key = '&key=AIzaSyAkLMHYfm2RZbGFdZEe8mkM_cIK9x0FY38'

url = 'http://192.168.0.103:5000/'
push_service = TXFCMNotification(
    api_key='AAAAhtopyIM:APA91bHCqG31uzCgKX5QxnMjdSaPsM5kXRmNomx-4g_ySvJOVn9mOpJ20Yu_L2GdLgExWU8TLbwkg6JiABhd-5wRaK_iPWYAR9v5eBX2GWH3z6odt9ifd4jWmXKFgHzase-H9ApspTH1')

Thread(target=reactor.run, args=(False,)).start()


@app.route('/')
def home():
    return "HELLO WORLD"


@app.route('/login', methods=['POST'])
def login():
    content = request.get_json()
    username = content['nameValuePairs']['username']
    password = content['nameValuePairs']['password']
    cur = mysql.connection.cursor()
    cur.execute(
        "SELECT COUNT(id) as num, id, username, password  FROM account WHERE username='" + username + "' AND password='"
        + password + "'")
    fetchData = cur.fetchone()
    print(fetchData)

    if int(fetchData[0]) == 1:
        return jsonify({'result': int(fetchData[1])})
    else:
        return jsonify({'result': -1})


@app.route('/register', methods=['POST'])
def register():
    content = request.get_json()
    username = content['nameValuePairs']['username']
    password = content['nameValuePairs']['password']
    name = content['nameValuePairs']['name']
    phone = content['nameValuePairs']['phone']
    birthyear = content['nameValuePairs']['birthyear']
    position = content['nameValuePairs']['position']
    cur = mysql.connection.cursor()

    cur.execute('SELECT COUNT(id) FROM account WHERE username = %s', (str(username),))
    if int(cur.fetchone()[0] == 1):
        return jsonify({'result': -1})
    else:
        cur.execute("INSERT INTO account VALUES(default, '" + username + "', '" + password + "')")
        mysql.connection.commit()
        lastRowID1 = cur.lastrowid

        cur.execute("INSERT INTO player VALUES(default, NULL, %s, %s, %s, %s, false, %s, %s)",
                    (int(lastRowID1), str(name), int(birthyear), str(position), str(""), str(phone)))

        mysql.connection.commit()
        lastRowID2 = cur.lastrowid
        cur.execute("INSERT INTO playerstatistic VALUES (default, %s, 0,0,0,0,0,0)", (int(lastRowID2),))
        mysql.connection.commit()
        cur.close()
        return jsonify({"result": lastRowID1})


@app.route('/newteam', methods=['POST'])
def create_new_tem():
    content = request.get_json()
    accountid = content['nameValuePairs']['accountid']
    street = content['nameValuePairs']['street']
    district = content['nameValuePairs']['district']
    city = content['nameValuePairs']['city']
    phone = content['nameValuePairs']['phone']
    teamname = content['nameValuePairs']['teamname']
    icon = content['nameValuePairs']['icon']

    cur = mysql.connection.cursor()
    cur.execute('SELECT COUNT(id) FROM team WHERE team.name=%s', (str(teamname),))
    retNum = int(cur.fetchone()[0])
    if retNum == 1:
        return jsonify({'result': -1})
    else:
        # response = requests.get(
        #     'https://maps.googleapis.com/maps/api/geocode/json?address=' + str(address).replace(" ", "+") + key)
        #
        # json_payload = response.json()
        # lat = json_payload['results'][0]['geometry']['location']['lat']
        # lng = json_payload['results'][0]['geometry']['location']['lng']
        lat = 20.981211
        lng = 105.787136

        cur.execute(
            "INSERT INTO teamlocation VALUES(default, %s, %s, %s, %s, %s)",
            (str(street), str(district), str(city), float(lat), float(lng))
        )
        mysql.connection.commit()
        lastInsertID = cur.lastrowid

        cur.execute(
            "INSERT INTO team VALUES (default, %s, %s, 1, %s, %s, 0, 0, 0)",
            (int(lastInsertID), str(teamname), str(phone), str(icon))
        )

        mysql.connection.commit()
        lastInsertID = cur.lastrowid

        cur.execute("UPDATE player SET teamid=%s, iscaptain = true WHERE accountid=%s",
                    (int(lastInsertID), int(accountid)))
        mysql.connection.commit()
        return jsonify({'result': lastInsertID})


@app.route('/getplayer', methods=['POST'])
def get_player():
    content = request.get_json()
    id = content['nameValuePairs']['id']

    cur = mysql.connection.cursor()
    cur.execute("SELECT * FROM player WHERE accountid=%s", (int(id),))
    fetchData = cur.fetchone()
    print("ehy" + str(fetchData))
    if ord(fetchData[6]) == 1:
        isCaptain = True
    else:
        isCaptain = False
    if fetchData[1] is not None:
        cur.execute("SELECT team.id, name, rating, phone, icon, win, draw, lose,"
                    "teamlocationid, street, district, city, latitude, longtitude"
                    " FROM team, teamlocation WHERE team.id = %s AND team.teamlocationid = teamlocation.id",
                    (int(fetchData[1]),))
        fetchData2 = cur.fetchone()
        # cur = mysql.connection.cursor()
        cur.execute("SELECT * FROM player WHERE teamid =%s", (int(fetchData2[0]),))
        fetchData3 = cur.fetchall()
        # fetch = []
        # for d in fetchData3:
        #     fetch.append(d)

        # print(fetch[0])
        players = json.dumps([{'id': data[0], 'teamid': data[1],
                               'accountid': data[2], 'name': data[3],
                               'birthyear': data[4], 'position': data[5],
                               'isCaptain': bool(ord(data[6])), 'icon': data[7], 'phone': data[8]}
                              for data in fetchData3])
        print(players)
        cur.close()
        return jsonify({'id': fetchData[0],
                        'teamid': {'tid': fetchData2[0],
                                   'tname': fetchData2[1],
                                   'trating': fetchData2[2],
                                   'tphone': fetchData2[3],
                                   'ticon': fetchData2[4],
                                   'twin': fetchData2[5],
                                   'tdraw': fetchData2[6],
                                   'tlose': fetchData2[7],
                                   'tlid': fetchData2[8],
                                   'street': fetchData2[9],
                                   'district': fetchData2[10],
                                   'city': fetchData2[11],
                                   'lat': fetchData2[12],
                                   'lon': fetchData2[13]},
                        'accountid': fetchData[2],
                        'name': fetchData[3],
                        'birthyear': fetchData[4],
                        'position': fetchData[5],
                        'iscaptain': isCaptain,
                        'icon': fetchData[7],
                        'phone': fetchData[8],
                        'teamplayers': json.loads(players.replace("\'", '"'))})
    else:
        cur.execute("SELECT team.id, name, rating, phone, icon, win, draw, lose,"
                    "teamlocationid, street, district, city, latitude, longtitude"
                    " FROM team, teamlocation WHERE team.teamlocationid = teamlocation.id")
        fetchData2 = cur.fetchall()
        teams = json.dumps([{'id': data[0], 'name': data[1],
                             'rating': data[2], 'phone': data[3],
                             'icon': data[4], 'win': data[5], 'draw': data[6], 'lose': data[7],
                             'teamlocationid': data[8], 'street': data[9], 'district': data[10],
                             'city': data[11], 'latitude': data[12], 'longtitude': data[13]}
                            for data in fetchData2])

        res = get_applications(fetchData[0])
        cur.close()
        return jsonify({'id': fetchData[0],
                        'teamid': {'result': 0},
                        'accountid': fetchData[2],
                        'name': fetchData[3],
                        'birthyear': fetchData[4],
                        'position': fetchData[5],
                        'iscaptain': isCaptain,
                        'icon': fetchData[7],
                        'phone': fetchData[8],
                        'teams': json.loads(teams.replace("\'", '"')),
                        'apply': res})


@app.route('/getteamplayers', methods=['POST'])
def get_team_player():
    content = request.get_json()
    teamid = content['nameValuePairs']['teamid']

    cur = mysql.connection.cursor()
    cur.execute("SELECT * FROM player WHERE teamid =%s", (int(teamid),))
    fetchData = cur.fetchall()
    players = json.dumps([{'id': data[0], 'teamid': data[1],
                           'accountid': data[2], 'name': data[3],
                           'birthyear': data[4], 'position': data[5],
                           'isCaptain': bool(ord(data[6])), 'icon': data[7], 'phone': data[8]}
                          for data in fetchData])
    cur.close()
    return jsonify({'teamplayers': json.loads(players.replace("\'", '"'))})


def get_applications(playerid):
    cur = mysql.connection.cursor()
    cur.execute("SELECT id, playerid, teamid FROM application WHERE playerid=%s", (int(playerid),))
    fetchData = cur.fetchall()
    teamids = json.dumps([{'teamid': data[2]} for data in fetchData])
    cur.close()
    return json.loads(teamids.replace("\'", '"'))


@app.route('/rejectapply', methods=['POST'])
def reject_applicant():
    content = request.get_json()
    playerid = content['nameValuePairs']['playerid']
    teamid = content['nameValuePairs']['teamid']
    cur = mysql.connection.cursor()
    cur.execute("DELETE FROM application WHERE playerid=%s AND teamid=%s AND isapprove=false",
                (int(playerid), int(teamid)))
    mysql.connection.commit()
    cur.close()
    return jsonify({'result': cur.lastrowid})


@app.route('/acceptapply', methods=['POST'])
def accept_applicant():
    content = request.get_json()
    playerid = content['nameValuePairs']['playerid']
    teamid = content['nameValuePairs']['teamid']
    teamname = content['nameValuePairs']['teamname']
    cur = mysql.connection.cursor()
    cur.execute("UPDATE application SET isapprove=%s WHERE playerid=%s AND teamid=%s",
                (bool(True), int(playerid), int(teamid)))
    mysql.connection.commit()
    cur.execute("UPDATE player SET teamid=%s WHERE id=%s",
                (int(teamid), int(playerid)))
    mysql.connection.commit()
    cur.execute("DELETE FROM application WHERE playerid=%s AND NOT (isapprove=true)", (int(playerid),))
    mysql.connection.commit()
    cur.execute("SELECT regid FROM player WHERE id = %s", (int(playerid),))
    print("plauer" + str(playerid))
    fetch_data = cur.fetchone()
    message_title = "Tham gia doi bong moi"
    message_body = "Chao mung ban den voi " + str(teamname) + "."

    df = push_service.notify_single_device(registration_id=str(fetch_data[0]), message_title=message_title,
                                           message_body=message_body)

    df.addBoth(got_result)
    # reactor.run()
    cur.close()
    return jsonify({'result': cur.lastrowid})


@app.route('/getapplicants', methods=['POST'])
def get_applicants():
    content = request.get_json()
    teamid = content['nameValuePairs']['teamid']
    cur = mysql.connection.cursor()
    cur.execute("SELECT playerstatistic.id, goals, tackle, block, assist, save, penalty,"
                "playerstatistic.playerid, name, birthyear, position, icon, phone "
                "FROM player, playerstatistic, application "
                "WHERE player.id = playerstatistic.id AND application.teamid=%s "
                "AND application.isapprove = False "
                "AND player.id = application.playerid",
                (int(teamid),))
    fetchdata = cur.fetchall()

    result = json.dumps([{'id': data[0],
                          'goals': data[1], 'tackle': data[2], 'block': data[3], 'assist': data[4], 'save': data[5],
                          'penalty': data[6],
                          'player': {'pid': data[7],
                                     'pname': data[8],
                                     'pbirth': data[9],
                                     'ppos': data[10],
                                     'picon': data[11],
                                     'pphone': data[12]}} for data in fetchdata])
    cur.close()
    # print(result)
    return jsonify({'result': json.loads(result.replace("\'", '"'))})


@app.route('/applytoteam', methods=['POST'])
def apply_to_team():
    content = request.get_json()
    playerid = content['nameValuePairs']['playerid']
    teamid = content['nameValuePairs']['teamid']

    cur = mysql.connection.cursor()
    cur.execute("INSERT INTO application VALUES (default, %s, %s, false)", (int(playerid), int(teamid)))
    mysql.connection.commit()
    lastInsert = cur.lastrowid
    cur.execute(
        "SELECT regid, player.name FROM player where (player.iscaptain = true AND player.teamid = %s) OR "
        "player.id = %s "
        "ORDER BY iscaptain DESC",
        (int(teamid), int(playerid)))
    fetch_data = cur.fetchall()

    message_title = "Yeu cau tham gia"
    message_body = str(fetch_data[1][1]) + " muon tham gia vao doi cua ban."

    df = push_service.notify_single_device(registration_id=str(fetch_data[0][0]), message_title=message_title,
                                           message_body=message_body)
    df.addBoth(got_result)

    # Thread(target=reactor.run, args=(False,)).start()

    cur.close()
    return jsonify({'result': lastInsert})


@app.route('/cancelapply', methods=['POST'])
def cancel_apply():
    content = request.get_json()
    playerid = content['nameValuePairs']['playerid']
    teamid = content['nameValuePairs']['teamid']
    cur = mysql.connection.cursor()
    cur.execute("DELETE FROM application WHERE playerid=%s AND teamid=%s", (int(playerid), int(teamid)))
    mysql.connection.commit()
    deleterow = cur.lastrowid
    cur.close()
    return jsonify({'result': deleterow})


@app.route('/creatematch', methods=['POST'])
def create_new_match():
    content = request.get_json()
    teamid = content['nameValuePairs']['teamid']
    day = content['nameValuePairs']['day']
    place = content['nameValuePairs']['place']
    time = content['nameValuePairs']['time']

    matchday = str(day) + ' ' + str(time) + ':00'
    cur = mysql.connection.cursor()
    cur.execute("SELECT * FROM (SELECT draftedmatch.id, TIMESTAMPDIFF(MINUTE, CAST(CAST(draftedmatch.day as datetime) "
                "+ draftedmatch.time AS datetime), %s) as subtraction "
                "FROM draftedmatch_team, draftedmatch "
                "WHERE draftedmatch_team.teamid= %s "
                "AND draftedmatch_team.draftedmatchid = draftedmatch.id ) as busytable "
                "WHERE (abs(subtraction) <121 AND abs(subtraction) >1)", (str(matchday), int(teamid)))
    count = len(cur.fetchall())

    if count > 0:
        return jsonify({'result': 'Bi trung lich'})
    else:
        cur.execute("INSERT INTO draftedmatch VALUES (default, false,false, %s, %s, %s, false)",
                    # (dt.strptime(day, '%Y-%m-%d').date(), str(place), dt.strptime(time, '%H:%M').time()))
                    (str(day), str(place), str(time)))
        lastId = cur.lastrowid
        mysql.connection.commit()

        cur.execute("INSERT INTO draftedmatch_team VALUES (default, %s, %s, 'create', true, 0)",
                    (int(lastId), int(teamid)))
        mysql.connection.commit()
        cur.close()
        return jsonify({'result': 'ok'})


@app.route('/searchformatch', methods=['POST'])
def search_for_match():
    content = request.get_json()
    query = content['nameValuePairs']['query']
    print(str(query))
    cur = mysql.connection.cursor()
    cur.execute(str(query))
    fetchData = cur.fetchall()

    result = json.dumps(
        [{'team': {'tid': data[0], 'tname': data[1], 'trating': data[2], 'tphone': data[3], 'ticon': data[4],
                   'win': data[5], 'draw': data[6], 'lose': data[7]},
          'teamlocation': {'tlid': data[8], 'street': data[9], 'district': data[10], 'city': data[11],
                           'latitude': data[12], 'longtitude': data[13]},
          'match': {'dmid': data[14],
                    'day': str(data[15]),
                    'place': data[16],
                    'time': ':'.join(str(data[17]).split(':')[:2]),
                    'dmteamid': data[18],
                    'role': data[19]}}
         for data in fetchData])
    cur.close()
    return jsonify({'result': json.loads(result.replace("\'", '"'))})


@app.route('/searchhostmatch', methods=['POST'])
def search_host_match():
    content = request.get_json()
    query = content['nameValuePairs']['query']
    print('this ' + str(query))
    cur = mysql.connection.cursor()
    cur.execute(str(query))
    fetchData = cur.fetchall()
    print(fetchData)
    result = json.dumps(
        [{'team': {'tid': data[0], 'tname': data[1], 'trating': data[2], 'tphone': data[3], 'ticon': data[4],
                   'win': data[5], 'draw': data[6], 'lose': data[7]},
          # 'teamlocation': {'tlid': data[8], 'street': data[9], 'district': data[10], 'city': data[11],
          #                  'latitude': data[12], 'longtitude': data[13]},
          'match': {'dmid': data[8],
                    'day': str(data[9]),
                    'place': data[10],
                    'time': ':'.join(str(data[11]).split(':')[:2]),
                    'dmteamid': data[12],
                    'role': data[13],
                    'isready': bool(data[14]),
                    'isaccept': bool(ord(data[15])),
                    'isdenied': bool(ord(data[16])),
                    'isend': bool(data[17]),
                    'resstt': data[18],
                    'scoreid': data[19],
                    'finalscore': data[20],
                    'stats': data[21]}}
         for data in fetchData])
    cur.close()
    return jsonify({'result': json.loads(result.replace("\'", '"'))})


@app.route('/joinmatch', methods=['POST'])
def join_match():
    content = request.get_json()
    teamid = content['nameValuePairs']['teamid']
    matchid = content['nameValuePairs']['matchid']
    cur = mysql.connection.cursor()
    cur.execute("INSERT INTO draftedmatch_team VALUES (default, %s, %s, 'guest', false, 0)",
                (int(matchid), int(teamid)))
    mysql.connection.commit()
    cur.connection
    return jsonify({'result': cur.lastrowid})


@app.route('/invite', methods=['POST'])
def invite():
    content = request.get_json()
    invitor = content['nameValuePairs']['invite']
    invited = content['nameValuePairs']['invited']
    time = content['nameValuePairs']['time']
    place = content['nameValuePairs']['place']
    day = content['nameValuePairs']['day']
    cur = mysql.connection.cursor()
    matchday = str(day) + ' ' + str(time) + ':00'

    cur.execute("SELECT * FROM (SELECT draftedmatch.id, TIMESTAMPDIFF(MINUTE, CAST(CAST(draftedmatch.day as datetime) "
                "+ draftedmatch.time AS datetime), %s) as subtraction "
                "FROM draftedmatch_team, draftedmatch "
                "WHERE (draftedmatch_team.teamid= %s OR draftedmatch_team.teamid = %s) "
                "AND (isready = false OR (isready=true AND isaccepted=false)) "
                "AND draftedmatch_team.draftedmatchid = draftedmatch.id ) as busytable "
                "WHERE (abs(subtraction) <121 AND abs(subtraction) >1)", (str(matchday), int(invitor), int(invited)))
    count = len(cur.fetchall())

    if count > 0:
        return jsonify({'result': 'Bi trung lich'})

    else:
        cur.execute("INSERT INTO draftedmatch VALUES (default, false, false, %s, %s, %s, false)",
                    (str(day), str(place), str(time)))
        lastid = cur.lastrowid
        mysql.connection.commit()
        cur.execute("INSERT INTO draftedmatch_team VALUES (default, %s, %s, 'invite', true, 0)",
                    (int(lastid), int(invitor)))
        cur.execute("INSERT INTO draftedmatch_team VALUES (default, %s, %s, 'invited', false, 0)",
                    (int(lastid), int(invited)))
        mysql.connection.commit()
        cur.close()
        return jsonify({'result': 'OK'})


@app.route('/acceptmatch', methods=['POST'])
def accept_match():
    content = request.get_json()
    teamid = content['nameValuePairs']['teamid']
    matchid = content['nameValuePairs']['matchid']
    cur = mysql.connection.cursor()
    cur.execute("UPDATE draftedmatch_team SET isready = true WHERE teamid=%s AND draftedmatchid=%s",
                (int(teamid), int(matchid)))
    mysql.connection.commit()
    cur.close()
    return 'OK'


@app.route('/denymatch', methods=['POST'])
def deny_match():
    content = request.get_json()
    teamid = content['nameValuePairs']['teamid']
    matchid = content['nameValuePairs']['matchid']
    command = content['nameValuePairs']['command']
    cur = mysql.connection.cursor()

    if command != 2:
        cur.execute("UPDATE draftedmatch_team SET isready = false WHERE teamid=%s AND draftedmatchid=%s",
                    (int(teamid), int(matchid)))
        mysql.connection.commit()

    if command == 1:
        cur.execute("UPDATE draftedmatch SET isdenied=true WHERE id=%s",
                    (int(matchid),))
        mysql.connection.commit()

    if command == 2:
        cur.execute("DELETE FROM draftedmatch_team WHERE teamid = %s AND draftedmatchid=%s",
                    (int(teamid), int(matchid)))
        mysql.connection.commit()

    cur.close()
    return 'OK'


@app.route('/searchinvitedmatch', methods=['POST'])
def search_for_invited_match():
    content = request.get_json()
    query = content['nameValuePairs']['query']
    print('invite: ' + str(query))
    cur = mysql.connection.cursor()
    cur.execute(str(query))
    fetchData = cur.fetchall()
    print(fetchData)
    result = json.dumps(
        [{'team': {'tid': data[0], 'tname': data[1], 'trating': data[2], 'tphone': data[3], 'ticon': data[4],
                   'win': data[5], 'draw': data[6], 'lose': data[7]},
          'match': {'dmid': data[8],
                    'day': str(data[9]),
                    'place': data[10],
                    'time': ':'.join(str(data[11]).split(':')[:2]),
                    'dmteamid': data[12],
                    'role': data[13],
                    'isready': bool(data[14]),
                    'isaccept': bool(ord(data[15])),
                    'isdenied': bool(ord(data[16])),
                    'isend': bool(data[17]),
                    'resstt': data[18],
                    'scoreid': data[19],
                    'finalscore': data[20],
                    'stats': data[21]},
          'players': json.loads(get_team_players(data[0], cur).replace("\'", '"'))}
         for data in fetchData])

    cur.close()
    return jsonify({'result': json.loads(result.replace("\'", '"'))})


def get_team_players(teamid, cur):
    cur.execute("SELECT * FROM player WHERE teamid =%s", (int(teamid),))
    fetchData3 = cur.fetchall()
    players = json.dumps([{'id': data[0], 'teamid': data[1],
                           'accountid': data[2], 'name': data[3],
                           'birthyear': data[4], 'position': data[5],
                           'isCaptain': bool(ord(data[6])), 'icon': data[7], 'phone': data[8]}
                          for data in fetchData3])
    return players


@app.route('/updateregid', methods=['POST'])
def update_reg_id():
    content = request.get_json()
    token = content['nameValuePairs']['regid']
    playerid = content['nameValuePairs']['playerid']
    cur = mysql.connection.cursor()
    cur.execute("UPDATE player SET regid=%s WHERE id=%s", (str(token), int(playerid)))
    mysql.connection.commit()
    cur.close()
    return 'OK'


@app.route('/autoaccept', methods=['GET'])
def auto_accept_match():
    cur = mysql.connection.cursor()
    # while True:
    current_time = dt.strftime(dt.now(), '%Y-%m-%d %H:%M:%S')
    cur.execute(
        "SELECT tblcount.id, tblcount.day, tblcount.place, tblcount.time FROM (SELECT  dm1.Id, COUNT(isready) as ready,"
        "dm1.day, dm1.place, dm1.time "
        "FROM draftedmatch_team, draftedmatch dm1 "
        "WHERE draftedmatch_team.draftedmatchid = dm1.Id "
        "AND isready=true "
        "AND NOT isaccepted = true "
        "group by dm1.Id) AS tblcount "
        "WHERE tblcount.ready = 2")
    acceptable_match = cur.fetchall()
    print(acceptable_match)
    updated = []
    for id in acceptable_match:
        print(id)
        cur.execute("UPDATE draftedmatch SET isaccepted=true "
                    "WHERE id=%s "
                    "AND TIMESTAMPDIFF(SECOND, CAST(CAST(draftedmatch.day as datetime) "
                    "+ draftedmatch.time AS datetime), %s) >= 1", (int(id[0]), str(current_time)))

        updated.append(id[0])
        mysql.connection.commit()
        cur.execute("INSERT INTO scoretable VALUES (default, %s, 'b', 'a' )", (int(id[0]),))
        mysql.connection.commit()
        cur.execute("SELECT teamid FROM draftedmatch_team WHERE draftedmatchid = %s AND isready=true"
                    " ORDER BY role, draftedmatchid", (int(id[0]),))
        fd = cur.fetchall()
        cur.execute("SELECT regid, team.name from team, player WHERE team.id = player.teamid "
                    "AND team.id=%s", (int(fd[0][0]),))
        regid_team_1 = cur.fetchall()
        cur.execute("SELECT regid, team.name from team, player WHERE team.id = player.teamid "
                    "AND team.id=%s", (int(fd[1][0]),))
        regid_team_2 = cur.fetchall()
        registration_ids_1 = []
        registration_ids_2 = []
        for regid in regid_team_1:
            if regid[0] is not None:
                registration_ids_1.append(str(regid[0]))
        for regid in regid_team_2:
            if regid[0] is not None:
                registration_ids_2.append(str(regid[0]))

        message_title = "Tran dau moi"
        message_body_1 = "Doi ban co tran dau voi " + str(regid_team_2[0][1]) + " vao " + str(id[3]) + " " + \
                         str(id[1]) + " tai " + str(id[2])
        message_body_2 = "Doi ban co tran dau voi " + str(regid_team_1[0][1]) + " vao " + str(id[3]) + " " + \
                         str(id[1]) + " tai " + str(id[2])
        if len(registration_ids_1) > 0:
            df1 = push_service.notify_multiple_devices(registration_ids=registration_ids_1, message_title=message_title,
                                                       message_body=message_body_1)
            df1.addBoth(got_result)

        if len(registration_ids_2) > 0:
            df2 = push_service.notify_multiple_devices(registration_ids=registration_ids_2, message_title=message_title,
                                                       message_body=message_body_2)
            df2.addBoth(got_result)

    cur.close()
    return jsonify({'result': updated})


def got_result(result):
    print(result)


if __name__ == "__main__":
    app.run(debug=True, host='0.0.0.0', port=5000)
