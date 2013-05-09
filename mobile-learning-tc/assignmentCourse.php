<rss version="2.0" xmlns:atom="http://www.w3.org/2005/Atom">
    <channel>
        <title>Tugas Mata Kuliah</title>
	<description></description>
<?php
    include "config.php";
    
    $query = "SELECT a.id, a.name, a.intro, FROM_UNIXTIME(a.allowsubmissionsfromdate, '%d %b %y') AS fromdate, FROM_UNIXTIME(a.duedate, '%d %b %y') AS duedate
                FROM mdl_assign AS a
                    INNER JOIN mdl_course AS b ON b.id = a.course
                    AND b.id = '" . $_GET["courseid"] ."'";
    
    $result = mysql_query($query);
    while ($row = mysql_fetch_array($result)) {
?>
        <item>
            <link><?php echo $row["id"]; ?></link>
            <title><?php echo $row["name"]; ?></title>
            <pubdate><?php echo $row["intro"]; ?></pubdate>
            <description><?php echo $row["fromdate"] . " - " . $row["duedate"]; ?></description>
        </item>
<?php
    }
?>
    </channel>
</rss>
<?php
    mysql_close();
?>