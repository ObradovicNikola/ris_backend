<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
<head>
 <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles/style.css">
<meta charset="UTF-8">
<title>Unos nove knjige</title>
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
	<form action="/Library/knjige/saveKnjiga" method="post">
		<table>
			<tr>
				<td>Naslov:</td>
				<td><input type="text" value="${knjiga.naslov}"></td>
			</tr>
			<tr>
				<td>Autor:</td>
				<td><input type="text" name="autor"></td>
			</tr>
			<tr>
				<td>Izdavac:</td>
				<td><input type="text" name="izdavac"></td>
			</tr>
			<tr>
				<td>Godina izdanja:</td>
				<td><input type="text" name="godinaIzdanja"></td>
			</tr>
			<tr>
				<td><input type="submit" value="Sacuvaj"></td>
			</tr>
		</table>
	</form>
</div>
</div>
</body>
</html>