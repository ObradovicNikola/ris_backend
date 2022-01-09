<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<html>
<head>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles/style.css">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
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
		<table border="1">
			<tr><th>Naslov</th><th>Autor</th><th>Izdavac</th><th>Godina izdanja</th></tr>
			<c:forEach items="${knjige }" var="k">
				<tr>
					<td>${k.naslov }</td>
					<td>${k.autor }</td>
					<td>${k.izdavac }</td>
					<td>${k.godinaIzdanja }</td>
				</tr>
			</c:forEach>
		</table>

</div>
</div>
</body>
</html>