<?php ?>
<?xml version="1.0" encoding="UTF-8"?>
<rss version="2.0">
	<channel>
		<title>User Login</title>
		<link>http://elearning.if.its.ac.id</link>
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
?>
		<category><?php echo $row["id"]; ?></category>
		<description><?php echo $row["firstname"] . " " . $row["lastname"]; ?></description>
<?php
		}
		mysql_free_result($result);
	}
	else {
?>
		<category><?php echo 0; ?></category>
		<description><?php echo ""; ?></description>
	</channel>
</rss>
<?php
	}
	mysql_close();
?>