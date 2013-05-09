<rss version="2.0" xmlns:atom="http://www.w3.org/2005/Atom">
    <channel>
        <title>Status Tugas Pengguna</title>
        <description></description>
<?php
    include "config.php";
    
    $query = "SELECT id, name FROM mdl_course_categories";
    
    $result = mysql_query($query);
    while ($row = mysql_fetch_array($result)) {
?>
        <item>
            <link><?php echo $row["id"]; ?></link>
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