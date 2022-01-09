<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>  
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	Prikaz primeraka za naslov ${naslov } i godinu izdanja ${godinaIzdanja}
	<table border="1">
		<tr><th>Inventarni broj</th><th>Zaduzenja</th></tr>
		<c:forEach items="${primerci }" var="p">
			<tr>
				<td>${p.invBroj }</td>
				<td><a href="/Library/knjige/zaduzenjaPrimerka?idP=${p.invBroj}">Zaduzenja primerka</a></td>
			</tr>
		</c:forEach>
	
	</table>
</body>
</html>