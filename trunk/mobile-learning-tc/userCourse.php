<rss version="2.0" xmlns:atom="http://www.w3.org/2005/Atom">
    <channel>
        <title>Mata Kuliah Pengguna</title>
	<description></description>
<?php
    include "config.php";
    
    $query = "SELECT a.courseid, a.fullname, b.teacherid, b.firstname, b.lastname
                FROM (SELECT a.courseid, b.fullname
                    FROM mdl_enrol AS a
                    INNER JOIN mdl_course AS b ON a.courseid = b.id
                    INNER JOIN mdl_user_enrolments AS c ON c.enrolid = a.id
                    INNER JOIN mdl_user AS d ON d.id = c.userid
                    AND d.id = '" . $_GET["userid"] . "') AS a
                LEFT JOIN (SELECT a.id as teacherid, a.firstname, a.lastname, c.shortname, d.instanceid as 
                courseid
                    FROM mdl_user AS a, mdl_role_assignments AS b, mdl_role AS c, mdl_context AS d
                    WHERE a.id = b.userid AND c.id = b.roleid AND b.contextid = d.id 
                        AND d.contextlevel = '50'
                        AND (b.roleid = '3' OR b.roleid = '4')) AS b
                ON a.courseid = b.courseid";
    
    $result = mysql_query($query);
    while ($row = mysql_fetch_array($result)) {
?>
        <item>
            <link><?php echo $row["courseid"]; ?></link>
            <title><?php echo $row["fullname"]; ?></title>
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