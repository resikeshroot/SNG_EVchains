from flask import *
from database import *
import uuid
from ev_review import review_pred
from ev_review import *
from ev_ch_3 import estimate_charging_time
from web3 import Web3,HTTPProvider
import json
import random
from flask import jsonify

import smtplib
from email.mime.text import MIMEText
from flask_mail import Mail

api=Blueprint("api",__name__)



@api.route("/view_profile")
def viewuser():
    data={}
    log=request.args['log_id']
    
    q="select * from user  where login_id='%s'"%(log)
    res=select(q)
    if res:
        data['status']="success"
        data['data']=res
        data['method']="viewprofile"
    else:
        data['status']='failed'
    data['method']="viewprofile"
    return str(data)


@api.route("/user_reg")
def user_reg():
    data={}
    fname=request.args['fname']
    lname=request.args['lname']
   
    address=request.args['address']
  
    gender=request.args['gender']
    phone=request.args['phone']
    email=request.args['email']
    uname=request.args['uname']
    psw=request.args['password']
    
    
    a="insert into login values(null,'%s','%s','user')"%(uname,psw)
    id=insert(a)
    
    c="insert into user values(null,'%s','%s','%s','%s','%s','%s','%s')"%(id,fname,lname,gender,email,phone,address)
    insert(c)
    data['status']='success'
    return str(data)


@api.route("/login")
def login():
	data={}

	uname=request.args['uname']
	pwd=request.args['password']


	print(uname,pwd)
	q="select * from login where username='%s' and password='%s'"%(uname,pwd)
	res=select(q)
	if res:
		
		data['status']='success'
		data['data']=res
	else:
		data['status']='failed'
	return str(data)


@api.route("/User_Complaint")
def User_Complaint():
	data={}
	lid=request.args['log_id']
	complaints=request.args['complaints']
	q="insert into complaint values(null,(select user_id from user where login_id='%s'),'%s','pending',curdate())"%(lid,complaints)
	print(q)
	res=insert(q)

	if res:
		data['status']='success'
	else:
		data['status']='failed'
	data['method']='User_Complaint'

	return str(data)

@api.route("/viewcomp")
def viewcomp():
	data={}
	lid=request.args['log_id']
	q="select * from complaint where user_id=(select user_id from user where login_id='%s')"%(lid)
	print(q)
	res=select(q)

	data['status']='success'
	data['method']='User_feedbak'
	data['data']=res

	return str(data)



@api.route("/User_feedback")
def User_feedback():
	data={}
 
	lid=request.args['log_id']
	feedback=request.args['feedback']
	q="insert into feedback values(null,(select user_id from user where login_id='%s'),'%s',curdate())"%(lid,feedback)
	print(q)
	res=insert(q)

	if res:
		data['status']='success'
	else:
		data['status']='failed'
	data['method']='User_feedback'

	return str(data)

@api.route("/viewfb")
def viewfb():
	data={}
	lid=request.args['log_id']
	q="select * from feedback where user_id=(select user_id from user where login_id='%s')"%(lid)
	print(q)
	res=select(q)

	data['status']='success'
	data['method']='User_feedbak'
	data['data']=res

	return str(data)


@api.route("/User_booking")
def User_booking():
    data={}
    lid=request.args['log_id']
    date=request.args['date']
    time=request.args['time']
    q="insert into booking values(null,'%s',(select user_id from user where login_id='%s'),'%s','%s','pending')"%(lid,lid,date,time)
    print(q)
    res=insert(q)
    if res:
        data['status']='success'
    else:
        data['status']='failed'
    data['method']='User_feedback'
    return str(data)

@api.route("/viewbooking")
def viewbooking():
	data={}
	lid=request.args['log_id']
	q="select * from booking where user_id=(select user_id from user where login_id='%s')"%(lid)
	print(q)
	res=select(q)

	data['status']='success'
	data['method']='User_feedbak'
	data['data']=res

	return str(data)


@api.route("/view_showroom")
def view_showroom():
	data={}
	lid=request.args['log_id']
	
	q="select * from showroom "
	print(q)
	res=select(q)

	data['status']='success'
	
	data['data']=res

	return str(data)

@api.route("/slotid")
def slot_id():
    data={}
    center_id=request.args['center_id']
    a="select * from slots where center_id='%s'"%(center_id)
    res=select(a)
    if res:
        data['status']='pass'
        data['data']=res
        data['method']='slot'
    else:
        data['status']='failed'
        data['method']='slot'
    return str(data)

@api.route('/booknow')
def booknow():
	data = {}
	logid = request.args['logid']
	slid=request.args['slid']
	center = request.args['center']
	time = request.args['time']
	clevel = request.args['clevel']
	dlevel = request.args['dlevel']
	bcapacity = request.args['bcapacity']
	data = estimate_charging_time(int(clevel), int(dlevel), int(bcapacity))
	ctym = data['charging_time']
	energy_consumed = data['energy_consumed']
	l = "select priceperunit from charging_center where center_id='%s'" % (center)
	ppu = select(l)
	val = ppu[0]['priceperunit']
	val = float(val)
	amount = energy_consumed * val

	# Calculate estimated charging time in hours and minutes
	ctym_minutes = int(ctym * 60)
	hours, minutes = divmod(ctym_minutes, 60)

	# Format minutes as a string with two digits
	minutes_str = '{:02d}'.format(minutes)

	# Split the time value into hours and minutes
	if ':' not in time:
		raise ValueError('Invalid time format')
	hours_str, minutes_str = time.split(':')
	hours = int(hours_str)
	minutes = int(minutes_str)

	# Add the estimated charging time to the original time
	minutes += ctym_minutes
	if minutes >= 60:
		hours += 1
		minutes -= 60
	new_hours = hours % 24
	new_minutes = minutes % 60
	new_time = f"{new_hours:02d}:{new_minutes:02d}"
	# print(new_time)

	check = "SELECT COUNT(*) as counts FROM booking_slot WHERE `date` = curdate() AND (time(time) BETWEEN '%s' AND '%s' OR time(end_time) BETWEEN '%s' AND '%s' OR '%s' BETWEEN time(time) AND time(end_time) OR '%s' BETWEEN time(time) AND time(end_time))and center_id='%s'" % (time, new_time, time, new_time, time, new_time, center)
	print(check)
	check1 = select(check)
	count = check1[0]['counts']

	otp = ""
	for _ in range(6):
		otp += str(random.randint(0, 9))

	print(otp)

	############################
	q0 = "select * from user where login_id='%s'" % (logid)
	ra = select(q0)
	umail = ra[0]['email']

	print(umail)


	rd = random.randrange(1000, 9999, 4)
	# msg=str(rd)
	msg = otp
	data['rd'] = rd
	print(rd)
	try:
		gmail = smtplib.SMTP('smtp.gmail.com', 587)
		gmail.ehlo()
		gmail.starttls()
		gmail.login('projectsriss2020@gmail.com', 'vroiyiwujcvnvade')
	except Exception as ex:
		print("Couldn't setup email!!" + str(ex))
	msg = MIMEText(msg)

	msg['Subject'] = 'OTP - RECHARGE BUNK'

	msg['To'] = umail

	msg['From'] = 'projectsriss2020@gmail.com'

	try:
		gmail.send_message(msg)
		print(msg)
		# flash("EMAIL SENED SUCCESFULLY")
		session['rd'] = rd
	# return redirect(url_for('public.enterotp'))
	# return redirect(url_for('staff.staff_view_users'))
	except Exception as ex:
		print("COULDN'T SEND EMAIL", str(ex))
	# return redirect(url_for('public.forgotpassword'))
	# return redirect(url_for('staff.staff_view_users'))
	################################
	if int(count) == 0:
		q1 = "INSERT INTO booking_slot VALUES (NULL, '%s','%s', (SELECT user_id FROM user WHERE login_id='%s'), curdate(), '%s', 'pending', '%s','%s','%s')" % (slid,center, logid, time, amount, new_time,otp)
		res = insert(q1)
		if res:
			data['status'] = "success"
			data['estimated_time'] = ctym
			data['method']='book'
		else:
			data['status'] = "failed"
			data['method']='book'
	else:
		data['status'] = "Reserved"
		data['method']='booked'
		data['message'] = 'Slot already reserved'
  

	return str(data)




@api.route("/view_charging_center")
def view_charging_center():
	data={}
	
	q="select * from charging_center inner join slots using(center_id)"
	print(q)
	res=select(q)
 
	data['method']='cc_view'

	data['status']='success'
	
	data['data']=res

	return str(data)


@api.route("/view_service_center")
def view_service_center():
	data={}
	lid=request.args['log_id']
	q="select * from service_center"
	print(q)
	res=select(q)

	data['status']='success'
	
	data['data']=res

	return str(data)


@api.route("/view_spare_parts")
def view_spare_parts():
	data={}
	lid=request.args['log_id']
	q="select * from spare_parts"
	print(q)
	res=select(q)

	data['status']='success'
	
	data['data']=res

	return str(data)


@api.route("/view_booked_slots")
def view_booked_slots():
	data={}
	lid=request.args['log_id']
	q="SELECT * FROM booking_slot INNER JOIN charging_center USING(center_id) INNER JOIN slots USING(center_id) WHERE user_id=(SELECT user_id FROM USER WHERE login_id='%s')"%(lid)
	print(q)
	res=select(q)

	data['status']='success'
	
	data['data']=res

	return str(data)


@api.route("/Spare_parts_order")
def spare_parts_order():
    data={}
    lid=request.args['log_id']
    q="select * from order_master inner join order_details using(order_master_id) inner join spare_parts using(spare_part_id) where user_id=(select user_id from user where login_id='%s') and status='carted' "%(lid)
    res=select(q)
    
    if res:
        data['status']='success'
        data['data']=res
    else:
        data['status']='failed'
    return str(data)


@api.route("/view_other_post")
def view_other_post():
	data={}
	lid=request.args['log_id']
	# q="select * from post_for_sell where login_id!='%s'"%(lid)
	q="select * from post_for_sell inner join user using(user_id) where user_id!=(select user_id from user where login_id='%s')"%(lid) 
#   where login_id!='%s'"%(lid)

	print(q)
	res=select(q)

	data['status']='success'
	
	data['data']=res

	return str(data)

@api.route("/view_service_request")
def view_service_request():
	data={}
	lid=request.args['log_id']
	scid=request.args['scid']
	q="select * from request inner join request_details using(request_id) inner join my_vehicle using(my_vehicle_id) INNER JOIN service USING(service_id) INNER JOIN service_center USING(service_center_id) where user_id=(select user_id from user where login_id='%s') and service_center_id='%s' and service_name !='pickup' and amount='pending' or status='pending' or status='calculated' "%(lid,scid)
	print(q)
	res=select(q)

	data['status']='success'
	
	data['data']=res
 
	return str(data)
 
 
@api.route("/view_pickup_request")
def view_pickup_request():
	data={}
	lid=request.args['log_id']
	scid=request.args['scid']
	q="select * from request inner join request_details using(request_id) inner join my_vehicle using(my_vehicle_id) INNER JOIN service USING(service_id) INNER JOIN service_center USING(service_center_id) where user_id=(select user_id from user where login_id='%s') and service_center_id='%s' and service_name='pickup'  "%(lid,scid)
	print(q)
	res=select(q)

	data['status']='success'
	
	data['data']=res

	return str(data)
@api.route("/view_service_history")
def view_service_history():
	data={}
	lid=request.args['log_id']
	q="select * from request inner join request_details using(request_id) inner join my_vehicle using(my_vehicle_id) INNER JOIN service USING(service_id) INNER JOIN service_center USING(service_center_id) where user_id=(select user_id from user where login_id='%s') and status='paid' "%(lid)
	print(q)
	res=select(q)

	data['status']='success'
	
	data['data']=res

	return str(data)

@api.route("/remove_request")
def remove_request():
	data={}
	lid=request.args['log_id']
	rid=request.args['rid']
	rdid=request.args['rdid']
	am=request.args['am']
	tm=request.args['tm']
 
	w="select * from request_details where request_details_id='%s'"%(rdid)
	n=select(w)
	

	q="delete from request_details where request_details_id='%s'"%(rdid)
	
	delete(q)
 
	d="update request set total_amount=total_amount-'%s' where request_id='%s'"%(int(am),rid)
	update(d)

	data['status']='success'
	
	

	return str(data)



@api.route("/user_complaint")
def user_complaint():
	data={}
	lid=request.args['log_id']
	complaints=request.args['complaints']
	q="insert into complaints values(null,'%s','%s','pending',curdate())"%(lid,complaints)
	print(q)
	res=insert(q)

	if res:
		data['status']='success'
	else:
		data['status']='failed'
	data['method']='user_complaint'

	return str(data)

@api.route("/view_complaint")
def view_complaint():
	data={}
	lid=request.args['log_id']
	q="select * from complaints where sender_id='%s'"%(lid)
	print(q)
	res=select(q)

	data['status']='success'
	data['method']='User_feedbak'
	data['data']=res

	return str(data)
    
    
    
@api.route("/user_feedback")
def user_feedback():
	data={}
	cenid=request.args['cenid']
	
	lid=request.args['log_id']
	feedback=request.args['feedback']
	q="insert into feedback values(null,'%s','%s',curdate(),'%s')"%(lid,feedback,cenid)
	print(q)
	res=insert(q)

	if res:
		data['status']='success'
	else:
		data['status']='failed'
	data['method']='send_feedback'

	return str(data)

@api.route("/view_feedback")
def view_feedback():
	data={}
	lid=request.args['log_id']
	cenid=request.args['cenid']

 
	q="select * from feedback where sender_id='%s' and center_id='%s'"%(lid,cenid)
	print(q)
	res=select(q)

	data['status']='success'
	data['method']='view_feedback'
	data['data']=res

	return str(data)

@api.route("/view_showroom_vehicle")
def view_showroom_vehicle():
	data={}
	lid=request.args['log_id']
	shid=request.args['shid']
 
	q="select * from showroom_vehicle where showroom_id='%s'"%(shid)
	print(q)
	res=select(q)

	data['status']='success'
	
	data['data']=res

	return str(data)
    
    
@api.route("/buy_vehicle")
def buy_vehicle():
    data={}
    lid=request.args['log_id']
    svid=request.args['svid']
    sid=request.args['sid']
    q="select * from showroom_vehicle where showroom_vehicle_id='%s' and showroom_id='%s'"%(svid,sid)
   
    res=select(q)
    data['status']='success'
    data['data']=res
    print(data['data'])
    
    return str(data)


@api.route("/car_payment")
def car_payment():
    data={}
    sid=request.args['sid']
    svid=request.args['svid']
    lid=request.args['login_id']
   
    amount=request.args['amount']
   
    a="insert into buy_vehicle values(null,'%s','%s','%s',curdate(),'paid')"%(svid,lid,amount)
    res=insert(a)
    
   
    if res:
        data['status']='success'
    else:
        data['status']='failed'
    
    return str(data)

@api.route("/view_my_vehicle")
def view_my_vehicle():
    data={}
    lid=request.args['log_id']
    q="select * from my_vehicle where user_id=(select user_id from user where login_id='%s')"%(lid)
    res=select(q)
    if res:
        data['status']='success'
        data['data']=res
        data['method']='vehicle'
    else:
        data['status']='failed'
        data['method']='vehicle'
    return str(data)


@api.route("/add_vehicle")
def add_vehicle():
	data={}
	lid=request.args['log_id']
    
 
	brand=request.args['brand']
	eng=request.args['eng']
	lno=request.args['lno']
	clr=request.args['clr']
	q="insert into my_vehicle values(null,(select user_id from user where login_id='%s'),'%s','%s','%s','%s')"%(lid,brand,eng,lno,clr)
	print(q)
	res=select(q)

	data['status']='success'
	
	data['data']=res

	return str(data)


@api.route("/brand")
def brand():
    data={}
    lid=request.args['log_id']
    q="select brand from showroom_vehicle inner join buy_vehicle using(showroom_vehicle_id) where user_id=(select user_id from user where login_id='%s')"%(lid)
    res=select(q)
    if res:
      data['status']='success'
      data['data']=res
      data['method']='brand'
    else:
        data['status']='failed'
        data['method']='brand'
    return str(data)

@api.route("/car")
def car():
    data={}
    lid=request.args['log_id']
    a="select * from my_vehicle where user_id=(select user_id from user where login_id='%s')"%(lid)
    res=select(a)
    print(res)
    if res:
        data['status']='success'
        data['data']=res
        data['method']='car'
        
    else:
        data['status']='failed'
        data['method']='car'
        
    return str(data)

@api.route("/car_compare1")
def car_compare1():
    data={}
   
    a="select * from showroom_vehicle"
    res=select(a)
    print(res)
    if res:
        data['status']='success'
        data['data']=res
        data['method']='car_compare'
        
    else:
        data['status']='failed'
        data['method']='car_compare'
        
    return str(data)

@api.route("/compare1")
def compare1():
    data={}
    cid1=request.args['cid1']
   
    a="select * from showroom_vehicle where showroom_vehicle_id='%s'"%(cid1)
    res=select(a)
    print(res)
    if res:
        data['status']='success'
        data['data']=res
        data['method']='compare1'
        
    else:
        data['status']='failed'
        data['method']='compare1'
        
    return str(data)


@api.route("/compare2")
def compare2():
    data={}
    cid2=request.args['cid2']
   
    a="select * from showroom_vehicle where showroom_vehicle_id='%s'"%(cid2)
    res=select(a)
    print(res)
    if res:
        data['status']='success'
        data['data']=res
        data['method']='compare2'
        
    else:
        data['status']='failed'
        data['method']='compare2'
        
    return str(data)


@api.route("/car_compare2")
def car_compare2():
    data={}
  
    a="select * from showroom_vehicle"
    res=select(a)
    print(res)
    if res:
        data['status']='success'
        data['data']=res
        data['method']='car_compare'
        
    else:
        data['status']='failed'
        data['method']='car_compare'
        
    return str(data)


@api.route("/service")
def service():
    data={}
    
    scid=request.args['scid']
    
    a="select * from service where service_center_id='%s'"%(scid)
    res=select(a)
    print(res)
    if res:
        data['status']='success'
        data['data']=res
        data['method']='service'
    else:
        data['status']='failed'
        data['method']='service'
        
    return str(data)

@api.route("send_service_request")
def send_service_request():
    data = {}
    cid = request.args['cid']
    sid = request.args['sid']
    
    c = "select * from request_details"
    v = select(c)
    
    m = "SELECT STATUS FROM request WHERE STATUS IN ('pending', 'requested', 'calculated')"
    hn = select(m)
    
    if hn:
        s = "select * from request where status='requested' and my_vehicle_id='%s'" % (cid)
        q = select(s)
        
        if q:
            session['rid'] = q[0]['request_id']
            t = "insert into request_details values(null, '%s', '%s', 'pending')" % (session['rid'], sid)
            insert(t)
            
            data['status'] = 'success'
            data['method'] = 'ssr'
        else:
            data['status'] = 'failed'
            data['method'] = 'ssr'
    else:
        rf = "SELECT STATUS FROM request WHERE STATUS IN ('paid')"
        hb = select(rf)
        
        if hb:
            a = "insert into request values(null, '%s', 'pending', 'requested', curdate(), 'pending')" % (cid)
            id = insert(a)
            b = "insert into request_details values(null, '%s', '%s', 'pending')" % (id, sid)
            insert(b)
            
            data['status'] = 'success'
            data['method'] = 'ssr'
        else:
            data['status'] = 'failed'
            data['method'] = 'ssr'
    
    return str(data)




@api.route('/add_spare_parts_to_cart',methods=['post','get'])
def add_spare_parts_to_cart():
    data={}
    lid=request.args['login_id']
    a="select user_id from user where login_id='%s'"%(lid)
    u=select(a)
    uid=u[0]['user_id']
        
    spid=request.args['spid']
    # nme=request.args['nme']
    # amnt=request.args['amnt']
    # img=request.args['img']
        
    p="SELECT * FROM spare_parts  where spare_part_id='%s'"%(spid)
    t=select(p)
    price=t[0]['amount']
    total=int(price)
    res="select * from order_master where user_id='%s' and status='carted'"%(uid)
    res1=select(res)

    if res1:
            session['omd']=res1[0]['order_master_id']
            status=res1[0]['status']
            if status=='carted':
                u="update order_master set total=(total+'%s') where order_master_id='%s'"%(total,session['omd'])
                update(u)
                i="insert into order_details values(null,'%s','%s',now(),'%s')"%(spid,total,session['omd'])
                insert(i)
                data['status']='success'
    else:
            r="insert into order_master values(null,'%s','carted','%s')"%(total,uid)
            y=insert(r)
            j="insert into order_details values(null,'%s','%s',curdate(),'%s')"%(spid,total,y)
            insert(j)
            
            data['status']='success'
    return str(data)
        
        
        

@api.route("/buy_spare")
def buy_spare():
    data={}
   
    spid=request.args['spid']
  
    q="select * from spare_parts where spare_part_id='%s'"%(spid)
    select(q)
   
    res=select(q)
    if res:
        data['status']='success'
        data['data']=res
    else:
        data['status']='failed'
    return str(data)


@api.route('/User_view_todaysbooking',methods=['get','post'])
def User_view_todaysbooking():
	data = {}
	logid = request.args['logid']
	center = request.args['center']
	q="select * from booking_slot inner join slots using(center_id) where center_id='%s' and `date`=curdate()"%(center)
	res=select(q)
	data['method']="User_view_bank"
	if res:
		data['status']="success"
		data['check']=res
	else:
		data['status']="failed"

	return str(data)

@api.route('/user_pay_amount', methods=['get', 'post'])
def user_pay_amount():
    data = {}
    bookid = request.args['bookid']
    amount = request.args['amount']
    
    q = "insert into booking_payment values(null, '%s', '%s', curdate(), 'paid')" % (bookid, amount)
    m = insert(q)
    
    w = "update booking_slot set status='paid' where book_id='%s'" % (bookid)
    res = update(w)
    
    if res:
        data['status'] = 'success'
    else:
        data['status'] = 'failed'
    
    return str(data)

@api.route('/User_cancel_booking',methods=['get','post'])
def User_cancel_booking():
	data={}
	data['method']="User_cancel_booking"
	loginid=request.args['logid']
	bookid = request.args['bookid']

	# qry="select * from booking inner join charging_center using(center_id) where status!='pending' and user_id=(select user_id from user where login_id='%s')"%(loginid)
	qry="update booking_slot set status='cancelled' where book_id='%s'"%(bookid)

	res=update(qry)
	if res:
				data['status']="success"
				data['check']=res
	else:
				data['status']="failed"
	return str(data)

     
# @api.route('/service_payment',methods=['post','get'])
# def add_spare_parts_to_cart():
#     data={}
#     lid=request.args['login_id']
#     a="select user_id from user where login_id='%s'"%(lid)
#     u=select(a)
#     uid=u[0]['user_id']
        
#     spid=request.args['spid']
#     # nme=request.args['nme']
#     # amnt=request.args['amnt']
#     # img=request.args['img']
        
#     p="SELECT * FROM spare_parts  where spare_part_id='%s'"%(spid)
#     t=select(p)
#     price=t[0]['amount']
#     total=int(price)
#     res="select * from order_master where user_id='%s' and status='carted'"%(uid)
#     res1=select(res)

#     if res1:
#             session['omd']=res1[0]['order_master_id']
#             status=res1[0]['status']
#             if status=='carted':
#                 u="update order_master set total=(total+'%s') where order_master_id='%s'"%(total,session['omd'])
#                 update(u)
#                 i="insert into order_details values(null,'%s','%s',now(),'%s')"%(spid,total,session['omd'])
#                 insert(i)
#                 data['status']='success'
#     else:
#             r="insert into order_master values(null,'%s','carted','%s')"%(total,uid)
#             y=insert(r)
#             j="insert into order_details values(null,'%s','%s',curdate(),'%s')"%(spid,total,y)
#             insert(j)
            
#             data['status']='success'
#     return str(data)

@api.route("/pickup_payment")
def pickup_payment():
    data={}
    rd=request.args['rd']
    
    
    a="update request set status='paid' where request_id='%s'"%(rd)
    re=update(a)
   
   
   
   
    
    
   
    if  re:
        data['status']='success'
    else:
        data['status']='failed'
    
    return str(data)


@api.route("/service_payment")
def service_payment():
    data={}
    rd=request.args['rd']
    
    
    a="update request set status='paid' where request_id='%s'"%(rd)
    re=update(a)
   
   
   
   
    
    
   
    if  re:
        data['status']='success'
    else:
        data['status']='failed'
    
    return str(data)
    
    

@api.route("/cart_payment")
def cart_payment():
    data={}
    omid=request.args['omid']
    amount=request.args['amount']
   
   
   
   
    a="insert into spare_payment values(null,'%s',curdate(),'%s')"%(amount,omid)
    res=insert(a)
    
    s="update order_master set status='paid' where order_master_id='%s'"%(omid)
    re=update(s)
    
   
    if res and re:
        data['status']='success'
    else:
        data['status']='failed'
    
    return str(data)


@api.route('/view_cc')
def viewshops():
	data={}
	lati=request.args['lati']
	longi=request.args['longi']
	q="SELECT *,(3959 * ACOS ( COS ( RADIANS('%s') ) * COS( RADIANS( latitude) ) * COS( RADIANS( longitude ) - RADIANS('%s') ) + SIN ( RADIANS('%s') ) * SIN( RADIANS(latitude ) ))) AS user_distance from charging_center inner join slots using(center_id) ORDER BY user_distance ASC" % (lati,longi,lati)
	res=select(q)
	if res:
		data['status']="success"
		data['data']=res
		data['method']='nearme'
	else:
		data['status']="failed"
		data['method']='nearme'
	
	return str(data)



@api.route("/Search_center")
def search_center():
    data = {}
    sr = request.args.get('search_center')

    d = "select * from charging_center inner join slots using(center_id) where place like '{}%'".format(sr)
    res = select(d)
    if res:
        data['status'] = 'success'
        data['data']=res
        data['method']='search'
    else:
        data['status'] = 'failed'
        data['method']='search'
    return jsonify(data)


@api.route("/user_upload_post",methods=['get','post'])
def upload_post():
    print("******************************************")
    data={}
   
    image=request.files['image']
    path="static/"+str(uuid.uuid4())+ image.filename
    image.save(path)
  

    lid=request.form['log_id']
    
    a="select * from user where login_id='%s'"%(lid)
    res=select(a)
    uid=res[0]['user_id']
  
    des=request.form['des']
    amount=request.form['amnt']
    
    
    x="insert into post_for_sell values(null,'%s','%s','%s','%s',curdate())"%(path,des,amount,uid)
    ff=insert(x)
    
    if ff:
        data['status']='success'
    else:
        data['status']='failed'
    return str(data)


@api.route('/User_view_bunk_review',methods=['get','post'])
def viewrating():
    data={}
    q="SELECT * FROM feedback inner join charging_center using(center_id) inner join slots using(center_id)  GROUP BY center_id ORDER BY feedback"
    res=select(q)
    review_dict = {}
    for i in res:
        center_id = i['center_id']
        review = i['feedback']
        if center_id not in review_dict:
            review_dict[center_id] = []
        review_dict[center_id].append(review)
    sorted_reviews = []
    for center_id, feedback in review_dict.items():
        rating = review_pred(feedback)
        sorted_reviews.append((center_id, rating))
    sorted_reviews = sorted(sorted_reviews, key=lambda x: x[1], reverse=True)
    sorted_center_id = [x[0] for x in sorted_reviews]
    sorted_center_id_str = ','.join(str(x) for x in sorted_center_id)
    print("sorted_reviews : ",sorted_reviews)
    q="SELECT charging_center.*, feedback, feedback.center_id FROM feedback inner join charging_center on feedback.center_id=charging_center.`center_id`  WHERE feedback.center_id IN ({}) group by FIND_IN_SET(feedback.center_id,'{}') ORDER BY FIND_IN_SET(feedback.center_id, '{}')".format(sorted_center_id_str, sorted_center_id_str,sorted_center_id_str)
    res=select(q)
    
    data['method']="User_view_bank"
    if res:
        data['status']="success"
        data['check']=res
    else:
        data['status']="failed"

    return str(data)




@api.route("/user_chat")
def user_chat():
    sender_id = request.args['log_id']
    data={}
    # a="SELECT t.teacher_id,t.login_id,t.first_name,t.last_name,c.date_time,c.message AS message,time_format(c.date_time, '%h:%i:%p') as time FROM teachers t INNER JOIN chats c ON t.login_id = c.sender_id WHERE (t.login_id, c.date_time) IN (SELECT sender_id, MAX(date_time) AS latest_time FROM chats GROUP BY sender_id)"
    # a="SELECT TIME_FORMAT(date_time, '%%h:%%i:%%p') AS time,t.*, c.* FROM teachers t LEFT JOIN chats c ON t.login_id = c.reciever_id WHERE c.date_time = (SELECT MAX(date_time) FROM chats WHERE sender_type = 'student' AND sender_id ='%s' AND reciever_id = t.login_id);"%(sender_id)
    a="SELECT c.*, TIME_FORMAT(c.date_time, '%%h:%%i:%%p') AS TIME,s.showroom_id,s.login_id, s.name FROM chats AS c INNER JOIN ( SELECT  CASE  WHEN sender_type = 'user' THEN sender_id ELSE receiver_id END AS user_id, CASE WHEN sender_type = 'user' THEN receiver_id ELSE sender_id END AS other_user_id, MAX(date_time) AS max_date_time FROM chats WHERE sender_id = '%s' OR receiver_id = '%s' GROUP BY user_id, other_user_id ) AS recent_messages ON ( (c.sender_id = recent_messages.user_id AND c.receiver_id = recent_messages.other_user_id) OR (c.sender_id = recent_messages.other_user_id AND c.receiver_id = recent_messages.user_id)) INNER JOIN showroom AS s ON (c.sender_id = s.login_id OR c.receiver_id = s.login_id) AND c.date_time = recent_messages.max_date_time  ORDER BY c.date_time DESC"%(sender_id,sender_id)
    res=select(a)
    
    if res:
        data['data']=res
        data['status']='success'
    else:
        data['status']='failed'
    return str(data)


@api.route('/chat',methods=['get','post'])
def chat():
    data={}
    
    chat=request.args['chat']
    receiver_id=request.args['id']
    sender_id=request.args['loginid']
    
    print(sender_id,'/////////////////////////////////////////////////')
    
    
    qry="insert into chats values(null,'%s','user','%s','showroom','%s',now())"%(sender_id,receiver_id,chat)
    res=insert(qry)
    if res:
        data['status']="success"
        
        data['method']="done"
        
        
        
    return str(data)


@api.route('/viewchat')
def viewchat():
    sender_id = request.args['logid']
    receiver_id = request.args['id']
    
    data = {}

    f = "SELECT *,TIME_FORMAT(date_time,'%%h:%%i:%%p') AS time FROM chats WHERE sender_id='%s' AND receiver_id='%s' UNION SELECT *,TIME_FORMAT(date_time,'%%h:%%i:%%p') AS time FROM chats WHERE sender_id='%s' AND receiver_id='%s' ORDER BY date_time" % (sender_id, receiver_id, receiver_id, sender_id)

    rg = select(f)
    
    if rg:
        data['status']="success"
        data['data']=rg
        data['method']="view"
    return str(data)


@api.route("/new_password")
def new_password():
    data={}
    log=request.args['lid']
    np=request.args['np']
    cp=request.args['cp']
    if np==cp:
        q="update login set password='%s' where login_id='%s' "%(cp,log)
        res=select(q)

        data['status']="success"
    else:
        data['status']='failed'
    return str(data)

@api.route("/forgot_password")
def forgot_password():
    data={}
    print("///////////////////////////")
    log=request.args['lid']
    email=request.args['email']
    phone=request.args['phone']
    q="select * from user where email='%s' and phone='%s' and login_id='%s'"%(email,phone,log)
    res=select(q)
    print(res,"/////////////////////////////////////////////////")
    if res:
     data['status']="success"
     data['data']=res
    else:
        data['status']='failed'

    return str(data)
    


    
    
   