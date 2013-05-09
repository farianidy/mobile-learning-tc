<rss version="2.0" xmlns:atom="http://www.w3.org/2005/Atom">
    <channel>
        <title>Peserta Mata Kuliah</title>
        <description></description>
<?php
    include "config.php";
    
    $query = "SELECT DISTINCT d.id as userid, d.firstname, d.lastname, e.roleid, f.name as role
                FROM mdl_enrol AS a
                    INNER JOIN mdl_course AS b ON a.courseid = b.id
                    INNER JOIN mdl_user_enrolments AS c ON c.enrolid = a.id
                    INNER JOIN mdl_user AS d ON d.id = c.userid
                    INNER JOIN mdl_role_assignments AS e ON e.userid = d.id
                    INNER JOIN mdl_role_names AS f ON f.roleid = e.roleid
                    AND b.id = '" . $_GET["courseid"] . "'
                    AND d.id != '" . $_GET["userid"] . "'
                ORDER BY e.roleid";
    
    $result = mysql_query($query);
    while ($row = mysql_fetch_array($result)) {
?>
        <item>
            <link><?php echo $row["userid"]; ?></link>
            <title><?php echo $row["firstname"] . " " . $row["lastname"]; ?></title>
            <pubdate><?php echo $row["roleid"]; ?></pubdate>
            <description><?php echo $row["role"]; ?></description>
        </item>
<?php
    }
?>
    </channel>
</rss>
<?php
    mysql_close();
?>