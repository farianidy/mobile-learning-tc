<?php
	include "config.php";
	
	$username = $_REQUEST["username"];
	
	$query = "SELECT * FROM mdl_user WHERE username = '$username'";	
	$numRow = mysql_num_rows(mysql_query($query));
	
	if ($numRow >= 1) {
		$result = mysql_query($query);
		while ($row = mysql_fetch_array($result)) {
			echo $row["id"];
		}
		mysql_free_result($result);
		mysql_close();
	}
	else
		echo 0;
?>