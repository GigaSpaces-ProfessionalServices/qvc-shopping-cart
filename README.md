This project provides an outline for the maven source code tree for the qvc-shopping-cart project.

The Chrome Postman extension can be used for testing the service URLs. 

It is available from https://chrome.google.com/webstore/detail/postman/fhbjgbiflinjbdggehcddcbncdddomop?hl=en.

Example calls are as follows:

<table>
	<tr>
		<th>URL</th>
		<th>Request Body</th>
	</tr>
	<tr>
		<td>http://localhost:8093/shoppingcart-rest/cart/101</td>
		<td>{'id':101,'user':'sudip','address':{'street':'abc','city':'south brunswick','country':'usa'},'items':[{'name':'book','count':1},{'name':'toy','count':4}]}</td>
	</tr>
	<tr>
		<td>http://localhost:8093/shoppingcart-rest/cart/102</td>
		<td>{'id':102,'user':'chris','address':{'street':'mno','city':'columbia','country':'usa'},'items':[{'name':'book','count':3},{'name':'toy','count':9}]}</td>
	</tr>
	<tr>
		<td>http://localhost:8093/shoppingcart-rest/cart/103</td>
		<td>{'id':103,'user':'ali','address':{'street':'pqr','city':'new york','country':'usa'},'items':[{'name':'book','count':3},{'name':'toy','count':9},{'name':'cd','count':4}]}</td>
	</tr>
	<tr>
		<td>http://localhost:8093/shoppingcart-rest/cart/104</td>
		<td>{'id':104,'user':'rob','address':{'street':'xyz','city':'new york','country':'usa'},'items':[{'name':'cd','count':3},{'name':'toy','count':3},{'name':'software','count':1}]}</td>
	</tr>
	<tr>
		<td>http://localhost:8093/shoppingcart-rest/updatePaymentData/101</td>
		<td>{'cardNumber':'1234987613579751','holderName':'Sudip Bhattacharya','validUpto':'07/20','cvvCode':147}</td>
	</tr>
	<tr>
		<td>http://localhost:8093/shoppingcart-rest/updatePaymentData/102</td>
		<td>{'cardNumber':'9876975112341357','holderName':'Chris Abbonizio','validUpto':'04/22','cvvCode':246}</td>
	</tr>
	<tr>
		<td>http://localhost:8093/shoppingcart-rest/updatePaymentData/103</td>
		<td>{'cardNumber':'9751123498761357','holderName':'Ali Hodroj','validUpto':'03/17','cvvCode':375}</td>
	</tr>
	<tr>
		<td>http://localhost:8093/shoppingcart-rest/updatePaymentData/104</td>
		<td>{'cardNumber':'1357912349876751','holderName':'Rob Ray','validUpto':'03/22','cvvCode':387}</td>
	</tr>
</table>

It is possible to refer to a key inside a dynamic property field in a query. For example, the following queries work fine:
 
<pre>
select uid,* from com.qvc.shoppingcart.common.Cart WHERE paymentData.info.holderName = 'Sudip Bhattacharya'

select uid,* from com.qvc.shoppingcart.common.Cart WHERE paymentData.info.holderName in ('Sudip Bhattacharya', 'Rob Ray')
</pre>



