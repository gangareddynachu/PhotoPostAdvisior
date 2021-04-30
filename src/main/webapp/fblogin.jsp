<%@ page language="java" contentType="text/html; charset=BIG5"
    pageEncoding="BIG5"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="BIG5">
<title>Insert title here</title>
</head>
<body>
<script>
	function statusChangeCallback(response) {
		if (response.status === 'connected') {
			console.log(response);
			let token = response.authResponse.accessToken
			window.location.href = '/Test?access_token=' + token;
  		} else {
  			document.getElementById('status').innerHTML =
				'Please log into this webpage.';
		}
	}
	
	function FBLogin() {
		FB.login(function(response){ // step 1
			FB.getLoginStatus(function(response) {
				statusChangeCallback(response);
			});
		}, {scope: 'public_profile, email, pages_manage_posts, pages_read_engagement'}); // step 2
		
	}
	
	window.fbAsyncInit = function() {
		FB.init({
			appId            : '1024365154762700',
			autoLogAppEvents : true,
			xfbml            : true,
			version          : 'v10.0'
		});
	  
		FB.getLoginStatus(function(response) {
			statusChangeCallback(response);
		});
	};
	
</script>
<script async defer crossorigin="anonymous" src="https://connect.facebook.net/en_US/sdk.js"></script>

<button onclick="FBLogin()">Login</button>
<div id="status"></div>
<div id="email"></div>
<img id="pic">

</body>
</html>