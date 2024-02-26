from flask import *
from database import *

cc=Blueprint("charging_center",__name__)


@cc.route("/chargingcenter")
def chargingcenter():
    return render_template("charging_center.html")


@cc.route("/faculty_reg",methods=['get','post'])
def faculty_reg():
    if 'submit' in request.form:
        name=request.form['name']
        des=request.form['des']
        
        a="insert into faculty values(null,'%s','%s','%s')"%(session['ccid'],name,des)
        insert(a)
        
        
    return render_template("faculty_reg.html")

@cc.route("/viewfaculty")
def viewfaculty():
    data={}
    
    a="select * from faculty"
    res=select(a)
    
    data['view']=res
    
    if 'action' in request.args:
        action=request.args['action']
        id=request.args['id']
        
    else:
        action=None
        
    if action=='delete':
        d="delete from faculty where faculty_id='%s'"%(id) 
        delete(d) 
        return '''<script>alert('deleted');window.location='/viewfaculty'</script>'''  
    return render_template("viewfaculty.html",data=data)

@cc.route("/manage_faculty")
def manage_faculty():
    return render_template("managefaculty.html")



@cc.route("/cc_send_complaint",methods=['post','get'])
def send_complaint():
    
    data={}
    
    g="select * from complaints"
    res=select(g)
    
    data['view']=res
    if 'submit' in request.form:
        comp=request.form['com']
        
        a="insert into complaints values(null,'%s','%s','pending',curdate())"%(session['ccid'],comp)
        insert(a)
        return '''<script>alert('sended');window.location='/cc_send_complaint'</script>''' 
    return render_template("cc_send_complaint.html",data=data) 

@cc.route("/cc_send_feedback",methods=['get','post'])
def send_feedback():
    data={}
    
    h="select * from feedback where sender_id=(select center_id from charging_center where login_id='%s')"%(session['log'])
    res=select(h)
    
    data['view']=res
    if 'submit' in request.form:
        fb=request.form['fb']
        
        d="insert into feedback values(null,'%s','%s',curdate())"%(session['ccid'],fb)
        insert(d)
        return '''<script>alert('sended');window.location='/cc_send_feedback'</script>''' 
    return render_template("cc_send_feedback.html",data=data)


@cc.route("/cc_verify_booking")
def cc_verify_booking():
    data={}
    
    a="select * from booking_slot inner join slots using(center_id) inner join user using(user_id)"
    res=select(a)
    
    data['view']=res
    
    if 'action' in request.args:
        action=request.args['action']
        id=request.args['id']
    else:
        action=None
    
    if action=='accept':
        a="update booking_slot set status='booked' where book_id='%s'"%(id)
        update(a)
        return '''<script>alert('Accepted');window.location='/cc_verify_booking'</script>''' 
    
    if action=='reject':
        a="update booking_slot set status='rejected' where book_id='%s'"%(id)
        update(a)
        return '''<script>alert('Rejected');window.location='/cc_verify_booking'</script>''' 
    return render_template("cc_verify_booking.html",data=data)

@cc.route("/cc_verify_user")
def cc_verify_user():
    data={}
    
    a="select * from user inner join login using(login_id)"
    res=select(a)
    
    data['view']=res
    
    if 'action' in request.args:
        action=request.args['action']
        id=request.args['id']
    else:
        action=None
    
    if action=='accept':
        a="update login set usertype='user' where login_id='%s'"%(id)
        update(a)
        return '''<script>alert('accepted');window.location='/cc_verify_user'</script>''' 
    
    if action=='delete':
        s="delete from user where login_id='%s'"%(id)
        delete(s)
        d="delete from login where login_id='%s'"%(id)
        delete(d)
        return '''<script>alert('deleted');window.location='/cc_verify_user'</script>''' 
    return render_template("cc_verify_user.html",data=data)





@cc.route("/manage_slots",methods=['post','get'])
def manage_slots():
    data={}
    
    if 'submit' in request.form:
        slot=request.form['slot']
     
      
        
        
        a="insert into slots values(null,'%s','%s')"%(slot,session['ccid'])
        insert(a)
        return '''<script>alert('Added');window.location='/manage_slots'</script>''' 
    
    c="select * from slots where center_id='%s'"%(session['ccid'])
    res=select(c)
    data['view']=res
    
    if 'action' in request.args:
        action=request.args['action']
        id=request.args['id']
    else:
        action=None
    
    if action=='update':
        a="select * from slots where slot_id='%s'"%(id)
        up=select(a)
        
        data['up']=up
        
    if 'update' in request.form:
        slot=request.form['slot']
      
        
        
        q="update slots set slots='%s' where slot_id='%s'"%(slot,id)
        update(q)
        return '''<script>alert('updated');window.location='/manage_slots'</script>''' 
        
        
        
    
    
    
    if action=='delete':
        s="delete from slots where slot_id='%s'"%(id)
        delete(s)
        
        return '''<script>alert('deleted');window.location='/manage_slots'</script>''' 
    return render_template("cc_slot_reg.html",data=data)



@cc.route("/cc_view_booking")
def cc_view_booking():
    data={}
    
    a="select * from booking_slot inner join slots using(slot_id) inner join user using(user_id)  where charging_center_id='%s'"%(session['cid'])
    res=select(a)
    
    data['view']=res
    return render_template("cc_view_booking.html",data=data)
@cc.route("/cc_view_payment")
def cc_view_payment():
    data={}
    a="select * from cc_payment inner join booking_slot using(book_id) inner join user using(user_id) inner join slots using(slot_id) where charging_center_id='%s'"%(session['cid'])
    res=select(a)
    data['view']=res
    return render_template("cc_view_payment.html",data=data)
    




    
    
        
        
        
