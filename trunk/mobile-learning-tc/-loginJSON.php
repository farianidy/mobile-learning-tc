<?php
    include "config.php";
    
    $username = $_REQUEST["username"];
    $password = $_REQUEST["password"];
    
    $passwordsaltmain = ">=6W{18=sVhepsu%f+QcE0q}4ep";
    $passwordhash = md5($password.$passwordsaltmain);
    
    $query = "SELECT * FROM mdl_user WHERE username = '$username' AND password = '$passwordhash'";
    $result = mysql_query($query);
    
    if ($result) {
        while ($row = mysql_fetch_assoc($result)) {
            $output[] = $row;
	}
	echo json_encode($output);
	mysql_free_result($result);
    }
    mysql_close();
?>