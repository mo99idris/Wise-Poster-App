<?php

include "conn.php";
$st=$conn->prepare("SELECT  Fpost_id , Fuser_id ,p_text,p_date,image ,profile_img,name 
FROM users us
INNER JOIN posts po
ON  us.id = po.admin_id
INNER JOIN favorites fa
ON po.post_id = fa.Fpost_id
WHERE fa.Fuser_id = ?
ORDER BY Fpost_id DESC");
$st->bind_param("s",$_GET["Fuser_id"],);
$st->execute();
$rs=$st->get_result();
$ar=array();
while($row=$rs->fetch_assoc())
    array_push ($ar, $row);

echo json_encode($ar);
  mysqli_close($conn);

?>