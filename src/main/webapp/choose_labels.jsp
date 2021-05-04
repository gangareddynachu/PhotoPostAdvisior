<%@ page language="java" contentType="text/html; charset=BIG5"
    pageEncoding="BIG5"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
    
<!DOCTYPE html>
<html>
<head>
<meta charset="BIG5">
<title>Labels Choosing</title>
<!-- CSS only -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-eOJMYsd53ii+scO/bJGFsiCZc+5NDVN2yr8+0RDqr0Ql0h+rP48ckxlpbzKgwra6" crossorigin="anonymous">
<!-- JavaScript Bundle with Popper -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta3/dist/js/bootstrap.bundle.min.js" integrity="sha384-JEW9xMcG8R+pH31jmWH6WWP0WintQrMb4s7ZOdauHnUtxwoG2vI5DkLtS3qm9Ekf" crossorigin="anonymous"></script>
</head>
<body class="bg-dark">

<!-- Google Analytics -->
<script>
window.ga=window.ga||function(){(ga.q=ga.q||[]).push(arguments)};ga.l=+new Date;
ga('create', 'UA-XXXXX-Y', 'auto');
ga('send', 'timing', 'Java processing', 'photo categorizing', 30000);
</script>
<script async src='https://www.google-analytics.com/analytics.js'></script>
<!-- End Google Analytics -->


<div class="m-4 align-middle text-center bg-light p-4 rounded-3">
	<h1>Labels Choosing</h1>
	<p>Please choose the labels for categorizing the photos</p>
	<form action="/Categorize">
		<div class="d-flex bd-highlight m-4 flex-wrap">
			<c:forEach items="${ suggested }" var="label">			
				<div class="form-check m-2 p-2">
				  <input class="form-check-input" type="checkbox" name="choosed_label" value="${ label }" id="${ label }">
				  <label class="form-check-label" for="${ label }">
				    ${ label }
				  </label>
				</div>
			</c:forEach>
		</div>
		<button type="submit" class="btn btn-primary">Submit</button>
	</form>
</div>


</body>
</html>