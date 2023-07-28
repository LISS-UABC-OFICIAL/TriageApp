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

      $idUser = $_POST["idUser"];
      $idEmergency = $_POST["idEmergency"];
      $userAccessLevel = 0;

      /*$idUser = 2;
      $idEmergency = 5;
      $userAccessLevel = 0;*/
	  
	  $sql="INSERT INTO emergency_staff(User_idUser, Emergency_idEmergency, userAccessLevel) VALUES (?, ?, ?)";
	  $stm=$conexion->prepare($sql);
	  $stm->bind_param('iii', $idUser, $idEmergency, $userAccessLevel);
	  
	  if($stm->execute()){
		  echo "Registra";
	  }else{
		  echo "NoRegistra";
	  }
	  mysqli_close($conexion);
	  
?>