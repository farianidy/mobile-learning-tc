<?php
	include "config.php";
	
	$username = $_REQUEST["username"];
	$password = $_REQUEST["password"];
	
	$passwordsaltmain = ">=6W{18=sVhepsu%f+QcE0q}4ep";
	$passwordhash = md5($password.$passwordsaltmain);
	
	$query = "SELECT * FROM mdl_user WHERE username = '$username' AND password = '$passwordhash'";	
	$numRow = mysql_num_rows(mysql_query($query));
	
	if ($numRow >= 1) {
		$result = mysql_query($query);
		while ($row = mysql_fetch_array($result)) {
			echo $row["firstname"] . " " . $row["lastname"];
		}
		mysql_free_result($result);
	}
	else {
		echo "";
	}
	mysql_close();
?>