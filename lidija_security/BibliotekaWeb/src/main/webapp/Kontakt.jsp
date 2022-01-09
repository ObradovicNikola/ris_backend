<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles/style.css">
<meta charset="UTF-8">
<title>Biblioteka DMI</title>
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
<img src="./img/knjiga.jpg" class="center"/>
<div class="center" style="font-size:20px; text-align:center;">
Adresa: Trg Dositeja Obradovica 4, Novi Sad <br/>
Telefon: 021/123-456
<i>	Prijavite se klikom na
<a href="/Library/auth/loginPage">Link</a>
</i>
</div>
</div>
</body>
</html>