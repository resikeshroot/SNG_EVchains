from flask import *
from database import *


public=Blueprint("public",__name__)

@public.route("/")
def home():
    return render_template("home.html")

@public.route("/index")
def index():
    return render_template("index.html")



@public.route("/login",methods=['post','get'])
def login():
    
    if 'login' in request.form:
        uname=request.form['uname']
        psw=request.form['psw']
        
        a="select * from login where username='%s' and password='%s'"%(uname,psw)
        res=select(a)
        
        session['log']=res[0]['login_id']
        
        if res[0]['usertype']=='admin':
            return redirect(url_for("admin.adm"))
        
        elif res[0]['usertype']=='charging_center':
            d="select * from charging_center where login_id='%s'"%(session['log'])
            re=select(d)
            session['ccid']=re[0]['center_id']
            
            return redirect(url_for("charging_center.chargingcenter"))
        
        elif res[0]['usertype']=='showroom':
            d="select * from showroom where login_id='%s'"%(session['log'])
            re=select(d)
            session['sh']=re[0]['showroom_id']
            
            return redirect(url_for("sh.showroom"))
        
        elif res[0]['usertype']=='service_center':
            d="select * from service_center where login_id='%s'"%(session['log'])
            re=select(d)
            session['sc']=re[0]['service_center_id']
            
            return redirect(url_for("sc.service_center"))
        else:
            return '''<script>alert("Invalid User");window.location="/login"</script>'''
            
        
    return render_template("login.html")

@public.route("/charging_center_register",methods=['post','get'])
def charging_center_register():
    if 'reg' in request.form:
        cn=request.form['cn']
        phone=request.form['phone']
        email=request.form['email']
        lati=request.form['latitude']
        long=request.form['longitude']
        place=request.form['place']
        uname=request.form['uname']
        psw=request.form['psw']
        ppu=request.form['ppu']
        power=request.form['power']
        
        
        a="insert into login values(null,'%s','%s','pending')"%(uname,psw)
        id=insert(a)
        
        b="insert into charging_center values(null,'%s','%s','%s','%s','%s','%s','%s','%s','%s')"%(id,cn,place,email,phone,lati,long,ppu,power)
        insert(b)
        return '''<script>alert('success');window.location='/'</script>'''
    return render_template("charging_center_register.html")
        
        
@public.route("/showroom_reg",methods=['post','get'])
def showroom_register():
    if 'reg' in request.form:
        sn=request.form['sn']
        phone=request.form['phone']
        email=request.form['email']
   
   
        uname=request.form['uname']
        psw=request.form['psw']
        
        
        a="insert into login values(null,'%s','%s','pending')"%(uname,psw)
        id=insert(a)
        
        b="insert into showroom values(null,'%s','%s','%s','%s')"%(sn,email,phone,id)
        insert(b)
        return '''<script>alert('success');window.location='/'</script>'''
    return render_template("showroom_reg.html")



@public.route("/sc_reg",methods=['post','get'])
def service_center_register():
    if 'reg' in request.form:
        sn=request.form['sn']
        phone=request.form['phone']
        email=request.form['email']
   
   
        uname=request.form['uname']
        psw=request.form['psw']
        
        
        a="insert into login values(null,'%s','%s','pending')"%(uname,psw)
        id=insert(a)
        
        b="insert into service_center values(null,'%s','%s','%s','%s')"%(sn,email,phone,id)
        insert(b)
        return '''<script>alert('success');window.location='/'</script>'''
    return render_template("service_center_register.html")
    
    
    