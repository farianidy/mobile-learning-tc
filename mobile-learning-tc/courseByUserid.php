<?xml version="1.0" encoding="UTF-8"?>
<rss version="2.0" xmlns:atom="http://www.w3.org/2005/Atom">
	<channel>
		<title>Course Taken by User</title>
		<description></description>
<?php
	include "config.php";
	
	$query = "SELECT a.courseid, b.fullname
				FROM mdl_enrol AS a
				INNER JOIN mdl_course AS b ON a.courseid = b.id
				INNER JOIN mdl_user_enrolments AS c ON c.enrolid = a.id
				INNER JOIN mdl_user AS d ON d.id = c.userid
				AND d.id = '" . $_GET['userid'] . "'";
	
	$result = mysql_query($query);
	while ($row = mysql_fetch_array($result)) {
?>
		<item>
			<link><?php echo $row["courseid"]; ?></link>
			<title><?php echo $row["fullname"]; ?></title>
		</item>
<?php
	}
?>
	</channel>
</rss>
<?php
	mysql_close();
?>