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

	$idEmergency=$_POST["idEmergency"];
	$em_Name=$_POST["em_Name"];
    $em_Type1=$_POST["em_Type1"];
    $em_Type2=$_POST["em_Type2"];

	$sql="UPDATE emergency SET em_Name= ?, em_Type1= ?, em_Type2= ? WHERE idEmergency= ?";
	$stm=$conexion->prepare($sql);
	$stm->bind_param('sssi',$em_Name,$em_Type1,$em_Type2,$idEmergency);
		
	if($stm->execute()){
		echo "actualiza";
	}else{
		echo "noActualiza";
	}
	mysqli_close($conexion);
?>