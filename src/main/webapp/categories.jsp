<%@ page language="java" contentType="text/html; charset=BIG5"
    pageEncoding="BIG5" session="true"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="javax.servlet.http.HttpSession" %>
<%@ page import="tools.gcs.PhotoSet" %>
<%@ page import="tools.gcs.Photo" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="BIG5">
<title>Results</title>
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
ga('send', 'pageview', location.pathname);
</script>
<script async src='https://www.google-analytics.com/analytics.js'></script>
<!-- End Google Analytics -->


<%
	HashMap<String, PhotoSet> categories =
		(HashMap<String, PhotoSet>) session.getAttribute("categories");
%>

<div class="m-4 align-middle text-center bg-light p-4 rounded-3">
	<h1>Results</h1>
	<nav>
	  <div class="nav nav-tabs" id="nav-tab" role="tablist">
	    <button class="nav-link active" id="nav-categories-tab" data-bs-toggle="tab" data-bs-target="#nav-categories" type="button" role="tab" aria-controls="nav-categories" aria-selected="true">Categories</button>
	    <button class="nav-link" id="nav-Photos-tab" data-bs-toggle="tab" data-bs-target="#nav-Photos" type="button" role="tab" aria-controls="nav-Photos" aria-selected="false">Photos</button>
	  </div>
	</nav>
	<div class="tab-content" id="nav-tabContent">
	  <div class="tab-pane fade show active" id="nav-categories" role="tabpanel" aria-labelledby="nav-categories-tab">
	  	<div class="d-flex flex-wrap bd-highlight mb-3">
	  		<c:forEach items="${ categories }" var="category">
				<div class="card m-2" style="width: 18rem;">
				  <div class="card-header">
				    ${ category.getKey().substring(0, 1).toUpperCase() }${ category.getKey().substring(1) }
				  </div>
				  <div class="card-body">
				  	<p class="card-text">Likes - ${ category.getValue().getTotalLikesCount() }</p>
				  	<p class="card-text">Comments - ${ category.getValue().getTotalCommentsCount() }</p>
				  	<p class="card-text">Score - ${ category.getValue().getTotalScore() }</p>
					<button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#${ category.getKey() }-photos">
					  Photos <span class="badge bg-secondary mx-1">${ category.getValue().getPhotoAmount()}</span>
					</button>
				  </div>
					<!-- Modal -->
					<div class="modal fade" id="${ category.getKey() }-photos" tabindex="-1" aria-labelledby="${ category.getKey() }-photos-label" aria-hidden="true">
					  <div class="modal-dialog">
					    <div class="modal-content">
					      <div class="modal-header">
					        <h5 class="modal-title" id="${ category.getKey() }-photos-label">
					        	${ category.getKey().substring(0, 1).toUpperCase() }${ category.getKey().substring(1) }
					        	<span class="badge bg-secondary mx-1">${ category.getValue().getPhotoAmount()}</span>
					        </h5>
					        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
					      </div>
					      <div class="modal-body">
					      	<ul class="list-group list-group-flush">
							  	<c:forEach items="${ category.getValue().getPhotoNames() }" var="photoName">
									<li class="list-group-item">
									<a href="https://www.facebook.com/${ photoName }" target="_blank">
					    				${ photoName }
					    			</a>
									</li>
								</c:forEach>
							</ul>  	
					      </div>
					      <div class="modal-footer">
					        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
					      </div>
					    </div>
					  </div>
					</div>
				</div>
			</c:forEach>
	  	</div>
	  	
	  </div>
	  <div class="tab-pane fade" id="nav-Photos" role="tabpanel" aria-labelledby="nav-Photos-tab">
	  	<div class="d-flex flex-wrap bd-highlight mb-3">
	  		<c:forEach items="${ categories }" var="category">
				<c:forEach items="${ category.getValue().getPhotos() }" var="photo">
					<div class="card m-2">
					  <div class="card-header">
					  	
					    <a href="https://www.facebook.com/${ photo.getName() }" target="_blank">
					    	${ photo.getName() }
					    </a>
					  </div>
					  <div class="card-body">
					    <p class="card-text">Category -
					        ${ category.getKey().substring(0, 1).toUpperCase() }${ category.getKey().substring(1) } 
						</p>
					    <p class="card-text">Score - ${ photo.getScore() }</p>
					  </div>
					</div>
				</c:forEach>
			</c:forEach>
	  	</div>
	  </div>
	</div>
</div>

</body>
</html>