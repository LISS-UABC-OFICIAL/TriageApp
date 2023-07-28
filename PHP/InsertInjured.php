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

      
      $idEm = $_POST["idEm"];
	  $location = $_POST["location"];
      $Color = $_POST["Color"];
      $isContaminated = $_POST["isContaminated"];
      $ContType = $_POST["ContType"];
      $regUser = $_POST["regUser"];
      $state = $_POST["state"];
      $regDate = $_POST["regDate"];
	  $latitude = doubleval($_POST["latitude"]);	
	  $longitude = doubleval($_POST["longitude"]);
      $altitude = doubleval($_POST["altitude"]);

      /*$idEm = 3;
	  $location = "Calle Nardo 9716";
      $Color = "Rojo";
      $isContaminated = 0;
      $CountType = "";
      $regUser = 0;
      $state = "En espera";
      $regDate = "2023-05-11 12:59:0";
	  $latitude = 32.482874;	
	  $longitude = -116.848258;
      $altitude = 34.304903;*/
	  
	  $sql="INSERT INTO injured(idEm, location, Color, isContaminated, ContType, regUser, state, regDate, latitude, longitude, altitude)  VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	  $stm=$conexion->prepare($sql);
	  $stm->bind_param('ssssssssddd',$idEm,$location,$Color,$isContaminated,$ContType,$regUser,$state,$regDate,$latitude,$longitude,$altitude);
	  
	  if($stm->execute()){
		  echo "Registra";
	  }else{
		  echo "NoRegistra";
	  }
	  mysqli_close($conexion);
	  
?>