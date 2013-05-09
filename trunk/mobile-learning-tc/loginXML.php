<rss version="2.0"  xmlns:atom="http://www.w3.org/2005/Atom">
    <channel>
        <title>Login Pengguna</title>
	<link>http://elearning.if.its.ac.id</link>
<?php
    include "config.php";
    
    $username = $_REQUEST["username"];
    
    $query = "SELECT * FROM mdl_user WHERE username = '$username'";
    $numRow = mysql_num_rows(mysql_query($query));
    
    if ($numRow >= 1) {
        $result = mysql_query($query);
        while ($row = mysql_fetch_array($result)) {
?>
        <item>
            <title><?php echo $row["id"]; ?></title>
            <pubdate><?php echo $row["password"]; ?></pubdate>
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
            <pubdate><?php echo ""; ?></pubdate>
            <link><?php echo ""; ?></link>
	</item>
<?php
    }
    mysql_close();
?>
    </channel>
</rss>