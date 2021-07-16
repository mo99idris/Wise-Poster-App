<?php

include "conn.php";
$st=$conn->prepare("select profile_img from users where id=?");
$st->bind_param("s",$_GET["id"]);
$st->execute();
$rs=$st->get_result();
$row=$rs->fetch_assoc();
echo json_encode($row);
mysqli_close($conn);
?>

