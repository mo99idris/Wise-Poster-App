<?php

include "conn.php";
$st=$conn->prepare("SELECT report_text, Ruser_id , name,Rdate
FROM report,users
WHERE Ruser_id=id
ORDER by Rdate desc");

$st->execute();
$rs=$st->get_result();
$ar=array();
while($row=$rs->fetch_assoc())
    array_push ($ar, $row);

echo json_encode($ar);
  mysqli_close($conn);

?>