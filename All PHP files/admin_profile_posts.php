<?php

include "conn.php";
$st=$conn->prepare("SELECT image,profile_img,name, post_id,p_text,p_date 
FROM users 
INNER JOIN posts 
ON posts.admin_id = users.id
WHERE posts.admin_id=?");
$st->bind_param("s",$_GET["admin_id"]);
$st->execute();
$rs=$st->get_result();
$ar=array();
while($row=$rs->fetch_assoc())
    array_push ($ar, $row);

echo json_encode($ar);
  mysqli_close($conn);

?>