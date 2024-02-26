# @customer.route("/customerviewproduct",methods=['post','get'])
# def customerviewproduct():
#     data={}
#     if 'submit' in request.form:
#         sr=request.form['search']
#         c="SELECT * FROM product INNER JOIN subcategory USING(subcategory_id) INNER JOIN category USING(category_id) inner join purchase_child using(product_id) where product_name like'{}%' and product.status='active'".format(sr)
#         q=select(c)
#         data['view']=q
#     return render_template("customerviewproduct.html",data=data)