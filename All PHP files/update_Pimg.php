<?php

include "conn.php";
   
    $userID = $_POST['id'];
    $image = base64_decode($_POST['profile_img']);

    $namaimage =  rand(1, 10000);
    $tanggal = date("Y-m-d");
 
    $nama = "image-".$tanggal.$namaimage.".jpeg";
 
    $select= "update users set profile_img='$nama'
			 where id='$userID' ";
	mysqli_query($conn,$select);

    $target_dir = "uploads/".$nama;
    file_put_contents($target_dir, $image);

if(isset($_POST['path_pic'])) {

	$filePath = $_POST['path_pic'];
	
	if(file_exists($filePath)) {

	  unlink($filePath);
}

}
   
?>