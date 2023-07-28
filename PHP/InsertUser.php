<?PHP
/*$hostname_localhost ="127.0.0.1";
$database_localhost ="registroIncidentes";
$username_localhost ="root";
$password_localhost ="";*/

$hostname_localhost ="localhost";
$database_localhost ="u614793440_triage";
$username_localhost ="u614793440_triage";
$password_localhost ="Triagedb1";

$conexion=mysqli_connect($hostname_localhost,$username_localhost,$password_localhost,$database_localhost);

	 
	  $username = $_POST["username"];
      $pword = $_POST["pword"];
      $fullName = $_POST["fullName"];
      $mail = $_POST["mail"];
      $userDependency = $_POST["userDependency"];
      $appAccess = 0;
	  
	  $sql="INSERT INTO user(username, pword, fullName, mail, userDependency, appAccess) VALUES (?, ?, ?, ?, ?, ?)";
	  $stm=$conexion->prepare($sql);
	  $stm->bind_param('sssssi', $username, $pword, $fullName, $mail, $userDependency, $appAccess);
	  
	  if($stm->execute()){
		  echo "Registra";
	  }else{
		  echo "NoRegistra";
	  }
	  mysqli_close($conexion);
	  
?>