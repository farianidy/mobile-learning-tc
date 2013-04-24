<?xml version="1.0" encoding="UTF-8"?>
<rss version="2.0" xmlns:atom="http://www.w3.org/2005/Atom">
	<channel>
		<title>Available Course by Semester</title>
		<description></description>
<?php
	include "config.php";
					
	$query = "SELECT DISTINCT a.courseid, b.fullname as coursename, d.id as teacherid, d.firstname, d.lastname
				FROM mdl_enrol AS a
					INNER JOIN mdl_course AS b ON a.courseid = b.id
					INNER JOIN mdl_user_enrolments AS c ON c.enrolid = a.id
					INNER JOIN mdl_user AS d ON d.id = c.userid
					INNER JOIN mdl_context AS e ON e.instanceid = b.id
					INNER JOIN mdl_role_assignments AS f ON f.userid = d.id
					AND b.category = '" . $_GET["semesterid"] ."' AND f.roleid = '3'";
	
	$result = mysql_query($query);
	while ($row = mysql_fetch_array($result)) {
?>
		<item>
			<link><?php echo $row["courseid"]; ?></link>
			<title><?php echo $row["coursename"]; ?></title>
			<pubdate><?php echo $row["teacherid"]; ?></pubdate>
			<description><?php echo $row["firstname"] . " " . $row["lastname"]; ?></description>
		</item>
<?php
	}
?>
	</channel>
</rss>
<?php
	mysql_close();
?>