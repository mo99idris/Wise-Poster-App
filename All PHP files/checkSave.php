<?php

include "conn.php";
$st=$conn->prepare("select Fpost_id from favorites where Fuser_id=?");
$st->bind_param("s",$_GET["Fuser_id"]);

$st->execute();
$rs=$st->get_result();
$ar=array();
while($row=$rs->fetch_assoc())
    array_push ($ar, $row);

echo json_encode($ar);
  mysqli_close($conn);
?>

