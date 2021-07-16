<?php

include "conn.php";

 $response = array();
 
 if($_POST['Fpost_id'] && $_POST['Fuser_id']){
    
     $postID = $_POST['Fpost_id'];
     $userID = $_POST['Fuser_id'];
     
     $stmt = $conn->prepare("DELETE from favorites where Fpost_id = ? 
		And Fuser_id= ?");

     $stmt->bind_param("is",$postID,$userID);
  
   if($stmt->execute() == TRUE){
     
         $response['error'] = false;
         $response['message'] = "course created successfully!";
     } else{
         // if we get any error we are passing error to our object.
         $response['error'] = true;
         $response['message'] = "failed\n ".$conn->error;
     }
 } else{
     // this msethod is called when user
     // donot enter sufficient parameters. 
     $response['error'] = true;
     $response['message'] = "Insufficient parameters";
 }
 // at last we are prinintg our response which we get. 
 echo json_encode($response);
 ?>