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

	 
	  $em_Name=$_POST["em_Name"];
	  $em_Location=$_POST["em_Location"];
	  $em_RegDate=$_POST["em_RegDate"];
	  $em_Lat=doubleval($_POST["em_Lat"]);	
	  $em_Lon=doubleval($_POST["em_Lon"]);
      $em_Alt=doubleval($_POST["em_Alt"]);
	  $em_Type1=$_POST["em_Type1"];
	  $em_Type2=$_POST["em_Type2"];
      $em_IsCLosed=0;

      /*$em_Name="Test";
	  $em_Location="Ubicacion";
	  $em_RegDate="2023-05-11 12:59:02";
	  $em_Lat=32.482874;	
	  $em_Lon=-116.848258;
      $em_Alt=34.304903;
	  $em_Type1="Choque";
	  $em_Type2="Derrumbe";
      $em_IsCLosed=0;*/
	  
	  $sql="INSERT INTO emergency(em_Name, em_Location, em_RegDate, em_Lat, em_Lon, em_Alt, em_Type1, em_Type2, em_IsCLosed) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
	  $stm=$conexion->prepare($sql);
	  $stm->bind_param('sssdddsss',$em_Name,$em_Location,$em_RegDate,$em_Lat,$em_Lon,$em_Alt ,$em_Type1,$em_Type2,$em_IsCLosed);
	  
	  if($stm->execute()){
		  echo "Registra";
	  }else{
		  echo "No Registra";
	  }
	  mysqli_close($conexion);
	  
?>