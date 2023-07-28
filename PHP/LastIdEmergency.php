<?PHP
    /*$hostname_localhost ="127.0.0.1";
$database_localhost ="registroIncidentes";
$username_localhost ="root";
$password_localhost ="";*/

$hostname_localhost ="localhost";
$database_localhost ="u614793440_triage";
$username_localhost ="u614793440_triage";
$password_localhost ="Triagedb1";

    $json=array();
    //$json1=array();
    //$json2=array();
				
		$conexion = mysqli_connect($hostname_localhost,$username_localhost,$password_localhost,$database_localhost);

		$consulta="SELECT idEmergency FROM emergency ORDER BY idEmergency desc Limit 1";
		$resultado=mysqli_query($conexion,$consulta);

	
		while($registro=mysqli_fetch_array($resultado)){
			
			$result["idEmergency"]=$registro['idEmergency'];

            $json['Emergency'][]=$result;
		
		}//*/


        mysqli_close($conexion);
        echo json_encode($json);
        
?>