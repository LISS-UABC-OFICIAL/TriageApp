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

		$consulta="select idEmergency, em_Name, em_Location, em_RegDate, em_Lat, em_Lon, em_Alt, em_Type1, em_Type2 from emergency where em_IsClosed = 0 ";
		$resultado=mysqli_query($conexion,$consulta);

	
		while($registro=mysqli_fetch_array($resultado)){
			
			$result["idEmergency"]=$registro['idEmergency'];
			$result["em_Name"]=$registro['em_Name'];
			$result["em_Location"]=$registro['em_Location'];
			$result["em_RegDate"]=$registro['em_RegDate'];
			$result["em_Lat"]=$registro['em_Lat'];
			$result["em_Lon"]=$registro['em_Lon'];
			$result["em_Alt"]=$registro['em_Alt'];
			$result["em_Type"]=$registro['em_Type1'];
			$result["em_Type2"]=$registro['em_Type2'];

            $json['Emergency'][]=$result;
		
		}//*/


        mysqli_close($conexion);
        echo json_encode($json);
        
?>