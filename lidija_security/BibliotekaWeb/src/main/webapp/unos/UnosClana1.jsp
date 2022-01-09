<%@ page import="java.util.Date"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/styles/style.css">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Unos clana</title>
</head>
<body>
	<div class="sidenav">
		<a href="/Library/Onama.jsp">O nama</a> <a
			href="/Library/auth/getSve">Spisak knjiga</a> <a
			href="/Library/Kontakt.jsp">Kontakt</a>
		<security:authorize access="isAuthenticated()">
			<a href="/Library/unos/UnosKnjige.jsp">Unos nove knjige</a>
			<a href="/Library/clanovi/unosClana">Unos novog clana</a>
			<a href="/Library/reports/SviClanovi.pdf">Svi clanovi -
				izvestaj</a>
			<a href="/Library/ClanoviUPeriodu.jsp">Clanovi u periodu -
				izvestaj</a>
			<a href="/Library/auth/logout">Odjava</a>
		</security:authorize>

	</div>
	<div class="main">
		<img src="${pageContext.request.contextPath}/img/knjiga.jpg"
			class="center" />
		<div class="center" style="font-size: 20px; text-align: left;">

			<c:set var="today" value="<%=new Date()%>" />
			<form:form action="/Library/clanovi/saveClan1" method="post"
				modelAttribute="clan">
				<table>
					<tr>
						<td>Kategorija:</td>
						<td>
							<!--  					<form:select name="idKat" path="kategorija">
						<form:options items="${kategorije}" itemValue="idkategorije" 
											itemLabel="nazivkategorije"/>
					</form:select>
--> <form:select path="kategorija" items="${kategorije}"
								itemValue="idkategorije" itemLabel="nazivkategorije" />
						</td>
					</tr>
					<tr>
						<td>Ime:</td>
						<td><form:input type="text" path="ime" value="${clan.ime}" /></td>
					</tr>
					<tr>
						<td>Prezime:</td>
						<td><form:input type="text" path="prezime" /></td>
					</tr>
					<tr>
						<td>Adresa:</td>
						<td><form:input type="text" path="adresa" /></td>
					</tr>
					<tr>
						<td>Datum rodjenja:</td>
						<td><form:input type="date" path="datumRodjenja" /></td>
					</tr>
					<tr>
						<td>Datum upisa:</td>
						<td><form:input type="date" path="datumUpisa"
								value='<fmt:formatDate
							pattern="yyyy-MM-dd" value="${today}"/>' /></td>
					</tr>
					<tr>
						<td><input type="submit" value="Sacuvaj"></td>
					</tr>
				</table>
			</form:form>
			<br>
			<c:if test="${!empty clanSaved}">
${poruka} ${clanSaved.clanskibroj}
</c:if>
</div>
</div>
</body>
</html>