<?php

include "conn.php";
$st=$conn->prepare("select * from users");

$st->execute();
$rs=$st->get_result();
$ar=array();
while($row=$rs->fetch_assoc())
    array_push ($ar, $row);

echo json_encode($ar);
  mysqli_close($conn);

?>