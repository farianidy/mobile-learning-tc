<?php
    include "config.php";
    
    $query = "SELECT c.id as fileid, c.mimetype, c.contextid, d.intro, FROM_UNIXTIME(d.timemodified, '%d %b %y') AS date, c.author, c.license, c.filename, c.component, c.filearea
                FROM mdl_course_modules AS a
                    INNER JOIN mdl_context AS b ON b.instanceid = a.id
                    INNER JOIN mdl_files AS c ON c.contextid = b.id
                    INNER JOIN mdl_resource AS d ON d.id = a.instance
                    AND a.id = '" . $_GET["cmid"] . "'
                WHERE c.mimetype IS NOT NULL";
    
    $result = mysql_query($query);
    if ($result) {
        while ($row = mysql_fetch_array($result)) {
            $output[] = $row;
        }
        echo json_encode($output);
    }
    mysql_close();
?>
