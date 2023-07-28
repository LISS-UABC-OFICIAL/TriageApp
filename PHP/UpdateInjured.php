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

	$idPatient=$_POST["idPatient"];
	$Color=$_POST["Color"];
    $isContaminated=$_POST["isContaminated"];
    $ContType=$_POST["ContType"];
	$regUser=$_POST["regUser"];
	$state=$_POST["state"];
	$regDate=$_POST["regDate"];
	$detination=$_POST["detination"];
	$ambulance=$_POST["ambulance"];
	$bed=$_POST["bed"];
	$name=$_POST["name"];
	$gender=$_POST["gender"];
	$age=$_POST["age"];
	$injuries=$_POST["injuries"];


	$sql="UPDATE injured SET Color= ? , regUser= ?, isContaminated= ?, ContType= ?, state= ?, regDate= ?, detination= ?, ambulance=?, bed=?, name=?, gender=?, age=?, injuries=? WHERE idPatient= ?";
	$stm=$conexion->prepare($sql);
	$stm->bind_param('siissssssssssi',$Color,$regUser,$isContaminated,$ContType,$state,$regDate,$detination,$ambulance,$bed,$name,$gender,$age,$injuries,$idPatient);
		
	if($stm->execute()){
		echo "actualiza";
	}else{
		echo "noActualiza";
	}
	mysqli_close($conexion);
?>