<?php

include "conn.php";
$st=$conn->prepare("SELECT  name,profile_img, p_text , p_date , image , post_id ,admin_id
from users,posts
where admin_id = id
ORDER BY post_id DESC");

$st->execute();
$rs=$st->get_result();
$ar=array();
while($row=$rs->fetch_assoc())
    array_push ($ar, $row);

echo json_encode($ar);
  mysqli_close($conn);

?>