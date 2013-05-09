<?php
    $host = "localhost";
    $username = "root";
    $password = "";
    $databasename = "moodle";
    
    $connection = mysql_connect($host, $username, $password) or die("Could not connect: " . mysql_error());
    mysql_select_db($databasename, $connection) or die("Database Error");
?>