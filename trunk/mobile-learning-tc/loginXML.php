<?xml version="1.0" encoding="UTF-8"?>
<rss version="2.0"  xmlns:atom="http://www.w3.org/2005/Atom">
	<channel>
		<title>User Login</title>
		<link>http://elearning.if.its.ac.id</link>
		<description></description>
		<language>id</language>
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
		<item>
			<title><?php echo $row["id"]; ?></title>
			<link><?php echo $row["firstname"] . " " . $row["lastname"]; ?></link>
		</item>
<?php
		}
		mysql_free_result($result);
	}
	else {
?>
		<item>
			<title><?php echo 0; ?></title>
			<link><?php echo ""; ?></link>
		</item>
<?php
	}
	mysql_close();
?>
	</channel>
</rss>