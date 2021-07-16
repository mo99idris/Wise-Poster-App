<?php

include "conn.php";
$st=$conn->prepare("select post_id ,admin_id from posts where 
			post_id = (select MAX(post_id) from posts) ");

$st->execute();
$rs=$st->get_result();
$row=$rs->fetch_assoc();
echo json_encode($row);
mysqli_close($conn);
?>

