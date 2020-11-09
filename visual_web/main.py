import os
from flask import Flask, g, redirect, flash, render_template, request, session, abort, url_for
from flask_mysqldb import MySQL
from functools import wraps

app = Flask(__name__)
app.static_folder = 'static'
app.config['MYSQL_HOST'] = '0.0.0.0'
app.config['MYSQL_USER'] = 'root'
app.config['MYSQL_PASSWORD'] = ''
app.config['MYSQL_DB'] = 'agegenderexpression'
mysql = MySQL(app)

def login_required(f):
    @wraps(f)
    def decorate_function(*args, **kwargs):
        if g.user is None:
            return redirect(url_for('login', next=request.url))
        return f(*args, **kwargs)

    return decorate_function


@app.route('/')
def home():
    if not session.get('logged_in'):
        return render_template('login.html')
    else:
        return render_template('index.html')


@app.route('/login', methods=['POST'])
def do_login():
    user = request.form['email']
    pwd = request.form['pass']
    cur = mysql.connection.cursor()
    cur.execute(
        "SELECT * FROM Account, Admin WHERE username = %s AND password = %s AND Account.AdminId = Admin.PersonId",
        (str(user), str(pwd)))
    fetch_data = cur.fetchone()
    try:
        if len(fetch_data) == 5:
            session['logged_in'] = True
    except:
        flash('Tài khoản hoặc mật khẩu sai')
    return home()


@app.route('/logout')
def do_logout():
    session['logged_in'] = False
    return home()


if __name__ == "__main__":
    app.secret_key = os.urandom(12)
    app.run(debug=True, host='0.0.0.0', port=8855)
