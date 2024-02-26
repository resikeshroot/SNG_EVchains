from flask import *
from database import *

admin=Blueprint("admin",__name__)


@admin.route("/adm")
def adm():
    return render_template("admin.html")

@admin.route("/admin_view_charging_center")
def admin_view_charging_center():
    data={}
    
    a="select * from charging_center inner join login using(login_id)"
    res=select(a)
    
    data['view']=res
    
    if 'action' in request.args:
        action=request.args['action']
        id=request.args['id']
    
    else:
        action=None
        
    if action=='accept':
        a="update login set usertype='charging_center' where login_id='%s'"%(id)
        update(a)
        return '''<script>alert('accepted');window.location='/admin_view_charging_center'</script>'''
    
    if action=='reject':
        s="delete from login where login_id='%s'"%(id)
        delete(s)
        c="delete from charging_center where login_id='%s'"%(id)
        delete(c)
        return '''<script>alert('deleted');window.location='/admin_view_charging_center'</script>'''
    
    if action=='block':
         a="update login set usertype='blocked' where login_id='%s'"%(id)
         update(a)
         return '''<script>alert('blocked');window.location='/admin_view_charging_center'</script>'''
    if action=='unblock':
         a="update login set usertype='charging_center' where login_id='%s'"%(id)
         update(a)
         return '''<script>alert('unblocked');window.location='/admin_view_charging_center'</script>''' 
        
    return render_template("admin_view_charging_center.html",data=data)

@admin.route("/admin_view_showroom")
def admin_view_showroom():
    data={}
    
    a="select * from showroom inner join login using(login_id)"
    res=select(a)
    
    data['view']=res
    
    if 'action' in request.args:
        action=request.args['action']
        id=request.args['id']
    
    else:
        action=None
        
    if action=='accept':
        a="update login set usertype='showroom' where login_id='%s'"%(id)
        update(a)
        return '''<script>alert('accepted');window.location='/admin_view_showroom'</script>'''
    
    if action=='reject':
        s="delete from login where login_id='%s'"%(id)
        delete(s)
        c="delete from showroom where login_id='%s'"%(id)
        delete(c)
        return '''<script>alert('deleted');window.location='/admin_view_showroom'</script>'''
    
    if action=='block':
         a="update login set usertype='blocked' where login_id='%s'"%(id)
         update(a)
         return '''<script>alert('blocked');window.location='/admin_view_showroom'</script>'''
    if action=='unblock':
         a="update login set usertype='showroom' where login_id='%s'"%(id)
         update(a)
         return '''<script>alert('unblocked');window.location='/admin_view_showroom'</script>''' 
        
    return render_template("admin_view_showroom.html",data=data)



@admin.route("/admin_view_service_center")
def admin_view_service_center():
    data={}
    
    a="select * from service_center inner join login using(login_id)"
    res=select(a)
    
    data['view']=res
    
    if 'action' in request.args:
        action=request.args['action']
        id=request.args['id']
    
    else:
        action=None
        
    if action=='accept':
        a="update login set usertype='service_center' where login_id='%s'"%(id)
        update(a)
        return '''<script>alert('accepted');window.location='/admin_view_service_center'</script>'''
    
    if action=='reject':
        s="delete from login where login_id='%s'"%(id)
        delete(s)
        c="delete from service_center where login_id='%s'"%(id)
        delete(c)
        return '''<script>alert('deleted');window.location='/admin_view_service_center'</script>'''
    
    if action=='block':
         a="update login set usertype='blocked' where login_id='%s'"%(id)
         update(a)
         return '''<script>alert('blocked');window.location='/admin_view_service_center'</script>'''
    if action=='unblock':
         a="update login set usertype='service_center' where login_id='%s'"%(id)
         update(a)
         return '''<script>alert('unblocked');window.location='/admin_view_service_center'</script>''' 
        
    return render_template("admin_view_service_center.html",data=data)


@admin.route("/admin_viewcomplaint")
def viewcomplaint():
    data={}
    
    a="select * from complaints  inner join user on user.login_id=complaints.sender_id"
    res=select(a)
    
    data['view']=res
    
 
    
    return render_template("adminviewcomplaint.html",data=data)


@admin.route("/admin_send_reply",methods=['post','get'])
def admin_send_complaint():
    id=request.args['id']
    if 'submit' in request.form:
        rep=request.form['rep']
        
        a="update complaints set reply='%s' where sender_id='%s'"%(rep,id)
        update(a)
        return '''<script>alert('success');window.location='/admin_viewcomplaint'</script>'''
    return render_template("admin_send_reply.html")


@admin.route("/admin_view_feedback")
def view_feedback():
    data={}
    
    d="select * from feedback inner join charging_center using(center_id)"
    res=select(d)
    
    data['view']=res
    
    return render_template("admin_view_feedback.html",data=data)

@admin.route("/admin_view_user")
def admin_view_user():
    data={}
    
    f="select * from user"
    res=select(f)
    
    data['view']=res
    
    return render_template("admin_view_user.html",data=data)



