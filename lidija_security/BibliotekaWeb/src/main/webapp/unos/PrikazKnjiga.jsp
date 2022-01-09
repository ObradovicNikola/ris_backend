<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Prikaz knjiga</title>
</head>
<body>
	<form action="/Library/knjige/getPrimerciZaNaslovIGodinu" method="get">
		Naslov knjige: 
		<select name="knjiga">
			<c:forEach items="${knjige }" var="k">
				<option value="${k.naslov }">${k.naslov }</option>
			</c:forEach>
		</select><br/>
		Godina izdanja: <input type="text" name="godinaIzdanja">
		<input type="submit" value="Prikaz">
	</form>
</body>
</html>