
<?php

include "conn.php";
   
 if($_POST['p_text'] && $_POST['p_date'] && $_POST['admin_id'] && $_POST['image']) {

     $tweet = $_POST['p_text'];
     $date = $_POST['p_date'];
     $adminID = $_POST['admin_id'];
     $image = base64_decode($_POST['image']);

    $namaimage =  rand(1, 10000);
    $tanggal = date("Y-m-d");
 
    $nama = "image-".$tanggal.$namaimage.".jpeg";

    $select= "INSERT into posts(admin_id , p_text, p_date, image)
			 VALUES ('$adminID', '$tweet', '$date' ,'$nama')";
	mysqli_query($conn,$select);

    $target_dir = "uploads/".$nama;
    file_put_contents($target_dir, $image);
}

elseif($_POST['p_text'] && $_POST['p_date'] && $_POST['admin_id'])  {

    
     $tweet = $_POST['p_text'];
     $date = $_POST['p_date'];
     $adminID = $_POST['admin_id'];
     
    $select= "INSERT into posts(admin_id , p_text, p_date)
			 VALUES ('$adminID', '$tweet', '$date')";
	mysqli_query($conn,$select);
  
}

elseif($_POST['p_text'] && $_POST['post_id'] && $_POST['image']) {


  $tweet = $_POST['p_text'];
    $postID = $_POST['post_id'];
    $image = base64_decode($_POST['image']);

    $namaimage =  rand(1, 10000);
    $tanggal = date("Y-m-d");
 
    $nama = "image-".$tanggal.$namaimage.".jpeg";
 
    $select= "update posts set image='$nama', p_text='$tweet'
			 where post_id='$postID' ";
	mysqli_query($conn,$select);

    $target_dir = "uploads/".$nama;
    file_put_contents($target_dir, $image);

   
	if(isset($_POST['path_pic'])) {

	$filePath = $_POST['path_pic'];
	
	if(file_exists($filePath)) {

	  unlink($filePath);
}

}
}

elseif($_POST['p_text'] && $_POST['post_id'] && $_POST['update_ph']) {

    $tweet = $_POST['p_text'];
    $postID = $_POST['post_id'];
 
    $select= "update posts set p_text='$tweet'
			 where post_id='$postID' ";
	mysqli_query($conn,$select);


}

else {

 $tweet = $_POST['p_text'];
    $postID = $_POST['post_id'];
 
    $select= "update posts set p_text='$tweet', image= NULL
			 where post_id='$postID' ";
	mysqli_query($conn,$select);

if(isset($_POST['path_pic'])) {

	$filePath = $_POST['path_pic'];
	
	if(file_exists($filePath)) {

	  unlink($filePath);
}

}
}


   
?>