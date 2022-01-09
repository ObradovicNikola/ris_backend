<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Unos clana</title>
</head>
<body>
	<form action="/Library/clanovi/saveClan" method="post">
		<table>
			<tr>
				<td>Kategorija:</td>
				<td>
					<select name="idKat">
						<c:forEach items="${kategorije}" var="k">
							<option value="${k.idkategorije}">${k.nazivkategorije}</option>
						</c:forEach>
					</select>
				</td>
			</tr>
			<tr>
				<td>Ime:</td>
				<td><input type="text" name="ime"></td>
			</tr>
			<tr>
				<td>Prezime:</td>
				<td><input type="text" name="prezime"></td>
			</tr>
			<tr>
				<td>Adresa:</td>
				<td><input type="text" name="adresa"></td>
			</tr>
			<tr>
				<td>Datum rodjenja:</td>
				<td><input type="date" name="datumRodjenja"></td>
			</tr>
			<tr>
				<td>Datum upisa:</td>
				<td><input type="date" name="datumUpisa"></td>
			</tr>
			<tr>
				<td><input type="submit" value="Sacuvaj"></td>
			</tr>
		</table>	
	</form>
<br>
<c:if test="${!empty clan}">
Clan je uspesno sacuvan. Clanski broj je ${clan.clanskibroj}
</c:if>
</body>
</html>