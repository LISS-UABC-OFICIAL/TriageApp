<?php
    /*$hostname_localhost ="127.0.0.1";
$database_localhost ="registroIncidentes";
$username_localhost ="root";
$password_localhost ="";*/

$hostname_localhost ="localhost";
$database_localhost ="u614793440_triage";
$username_localhost ="u614793440_triage";
$password_localhost ="Triagedb1";

    $con = mysqli_connect($hostname_localhost,$username_localhost,$password_localhost,$database_localhost);
    
    $user = $_POST["username"];
    $pass = $_POST["pword"];

    //$user = "PedroPRZ";
    //$pass = "pedro12";
	
    $statement = mysqli_prepare($con, "SELECT * FROM user WHERE username = ? AND pword = ?");
    mysqli_stmt_bind_param($statement,"ss",$user,$pass);
    mysqli_stmt_execute($statement);
    
    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement,$id,$user,$pass,$name,$mail,$dependency,$access);
    
    $response = array();
    $response["success"] = false;  
    
    while(mysqli_stmt_fetch($statement)){
        $response["success"] = true;
        $response["idUser"] = $id;  
		$response["username"] = $user;
		$response["pword"] = $pass;
		$response["fullName"] = $name;
        $response["mail"] = $mail;
        $response["userDependency"] = $dependency;
        $response["appAccess"] = $access;
    }
    
    echo json_encode($response);
	
?>