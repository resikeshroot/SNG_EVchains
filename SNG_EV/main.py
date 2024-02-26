from flask import *
from admin import admin
from charging_center import cc
from public import public

from api import api

app=Flask(__name__)

app.secret_key="ev_chain"

app.register_blueprint(admin)
app.register_blueprint(public)
app.register_blueprint(cc)

app.register_blueprint(api,url_prefix='/api')


app.run(debug=True,port=5500,host="0.0.0.0")