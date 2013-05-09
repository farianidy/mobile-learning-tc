<rss version="2.0" xmlns:atom="http://www.w3.org/2005/Atom">
    <channel>
        <title>Sumber Daya Mata Kuliah</title>
	<description></description>
<?php
    include "config.php";
    
    $query = "SELECT a.id as cmid, d.id as resid, d.name
                FROM mdl_course_modules AS a
                    INNER JOIN mdl_course AS b ON b.id = a.course
                    INNER JOIN mdl_modules AS c ON c.id = a.module
                    INNER JOIN mdl_resource AS d ON d.id = a.instance
                    AND c.id = '17' AND b.id = '" . $_GET["courseid"] ."'";    
    
    $result = mysql_query($query);
    while ($row = mysql_fetch_array($result)) {
?>
        <item>
            <link><?php echo $row["cmid"]; ?></link>
            <pubdate><?php echo $row["resid"]; ?></pubdate>
            <title><?php echo $row["name"]; ?></title>
        </item>
<?php
    }
?>
    </channel>
</rss>
<?php
    mysql_close();
?>