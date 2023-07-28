<?PHP
/*$hostname_localhost ="127.0.0.1";
$database_localhost ="registroIncidentes";
$username_localhost ="root";
$password_localhost ="";*/

$hostname_localhost ="localhost";
$database_localhost ="u614793440_triage";
$username_localhost ="u614793440_triage";
$password_localhost ="Triagedb1";


	if(isset($_GET["idPatient"])){
		$idPatient=$_GET["idPatient"];
				
		$conexion = mysqli_connect($hostname_localhost,$username_localhost,$password_localhost,$database_localhost);

		$sql="DELETE FROM injured WHERE idPatient= ? ";
		$stm=$conexion->prepare($sql);
		$stm->bind_param('i',$idPatient);
			
		if($stm->execute()){
			echo "elimina";
		}else{
			echo "noElimina";
		}
		
		mysqli_close($conexion);
	}
	else{
		echo "noExiste";
	}

?>