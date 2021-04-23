<%@ page language="java" contentType="text/html; charset=BIG5"
    pageEncoding="BIG5"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
    
<!DOCTYPE html>
<html>
<head>
<meta charset="BIG5">
<title>Choosing Categories</title>
<!-- CSS only -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-eOJMYsd53ii+scO/bJGFsiCZc+5NDVN2yr8+0RDqr0Ql0h+rP48ckxlpbzKgwra6" crossorigin="anonymous">
<!-- JavaScript Bundle with Popper -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta3/dist/js/bootstrap.bundle.min.js" integrity="sha384-JEW9xMcG8R+pH31jmWH6WWP0WintQrMb4s7ZOdauHnUtxwoG2vI5DkLtS3qm9Ekf" crossorigin="anonymous"></script>
</head>
<body>
<div class="container">
	<h1>Choosing Categories</h1>
	<div class="d-flex flex-row bd-highlight mb-3">
		<form action="/Categorize">
			<c:forEach items="${ suggested }" var="label">			
				<div class="form-check">
				  <input class="form-check-input" type="checkbox" name="choosed_label" value="${ label }" id="${ label }">
				  <label class="form-check-label" for="${ label }">
				    ${ label }
				  </label>
				</div>
			</c:forEach>
			<button type="submit" class="btn btn-primary">Submit</button>
		</form>
	</div>
</div>


</body>
</html>