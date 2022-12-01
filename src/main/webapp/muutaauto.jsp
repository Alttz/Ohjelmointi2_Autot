<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src="scripts/main.js"></script>
<script src="scripts/io.js"></script>
<link rel="stylesheet" type="text/css" href="css/main.css">
<title>Auton tietojen muutos</title>
</head>
<body onload="asetaFocus('rekno')" onkeydown="tutkiKey(event, 'paivita')">
<form name="lomake">
	<table>
		<thead>
			<tr>
				<th colspan="5" class="oikealle"><a id="linkki" href="listaaautot.jsp">Takaisin listaukseen</a></th>
			</tr>
			<tr>
				<th>Rekisterinumero</th>
				<th>Merkki</th>
				<th>Malli</th>
				<th>Vuosi</th>
				<th></th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td><input type="text" name="rekno" id="rekno" /></td>
				<td><input type="text" name="merkki" id="merkki" /></td>
				<td><input type="text" name="malli" id="malli" /></td>
				<td><input type="text" name="vuosi" id="vuosi" /></td>
				<td><input type="button" id="tallenna" value="Hyväksy" onclick="tutkiJaPaivita()" /></td>
			</tr>
		</tbody>
	</table>
	<input type="hidden" name="id" id="id">
</form>
<span id="ilmo"></span>
</body>
<script>
haeAuto();
</script>
</html>