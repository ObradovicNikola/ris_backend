<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles/style.css">
<meta charset="UTF-8">
<title>Clanovi upisani u datom periodu</title>
</head>
<body>
<div class="sidenav">
  <a href="/Library/Onama.jsp">O nama</a>
  <a href="/Library/auth/getSve">Spisak knjiga</a>
  <a href="/Library/Kontakt.jsp">Kontakt</a>
 <security:authorize access="isAuthenticated()">
	<a href="/Library/unos/UnosKnjige.jsp">Unos nove knjige</a>
	<a href="/Library/clanovi/unosClana">Unos novog clana</a>
	<a href="/Library/reports/SviClanovi.pdf">Svi clanovi - izvestaj</a>
	<a href="/Library/ClanoviUPeriodu.jsp">Clanovi u periodu - izvestaj</a>
	<a href="/Library/auth/logout">Odjava</a>
</security:authorize>
  
</div>
<div class="main">
	<img src="${pageContext.request.contextPath}/img/knjiga.jpg" class="center"/>
<div class="center" style="font-size:20px; text-align:left;">
	<form action="/Library/reports/ClanoviUPeriodu.pdf" method="get">
		Datum od:<input name="datumOd"><br/>
		Datum do:<input name="datumDo"><br/>
		<input type="submit" value="Prikaz">
	</form>
</div>
</div>
</body>
</html>