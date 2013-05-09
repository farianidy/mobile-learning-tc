<?php
    include "config.php";
    
    $query = "SELECT intro, FROM_UNIXTIME(allowsubmissionsfromdate, '%d %b %y %h:%i') AS fromdate, FROM_UNIXTIME(duedate, '%d %b %y %h:%i') AS duedate, FROM_UNIXTIME(timemodified, '%d %b %y %h:%i') AS turneddate
                FROM mdl_assign
                WHERE id = '" . $_GET["assignmentid"] . "'";
    
    $result = mysql_query($query);
    if ($result) {
        while ($row = mysql_fetch_array($result)) {
            $output[] = $row;
        }
        echo json_encode($output);
    }
    mysql_close();
?> 