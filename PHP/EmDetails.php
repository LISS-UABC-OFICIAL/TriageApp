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

    if(isset($_GET["idEmergency"])){
        $idEmergency=$_GET["idEmergency"];

        $conexion = mysqli_connect($hostname_localhost,$username_localhost,$password_localhost,$database_localhost);

        $consulta="select em_Name, em_Location, em_RegDate, em_Lat, em_Lon, em_Alt, em_Type1, em_Type2, em_IsClosed, em_CloseDate, Emergencycol from emergency where idEmergency= '{$idEmergency}'";
        $resultado=mysqli_query($conexion, $consulta);

        if($registro=mysqli_fetch_array($resultado)){
            $result["Nombre"]=$registro['em_Name'];
            $result["Ubicacion"]=$registro['em_Location'];
            $result["FechaRegistro"]=$registro['em_RegDate'];
            $result["Latitud"]=$registro['em_Lat'];
            $result["Longitud"]=$registro['em_Lon'];
            $result["Altitud"]=$registro['em_Alt'];
            $result["Tipo"]=$registro['em_Type1'];
            $result["Tipo2"]=$registro['em_Type2'];
            $result["Cerrado"]=$registro['em_IsClosed'];
            $result["FechaCierre"]=$registro['em_CloseDate'];
            $result["Emergencycol"]=$registro['Emergencycol'];
            $json['emergency'][]=$result;

        } else {

            $resultar["Nombre"]='No registro';
            $resultar["Ubicacion"]='No registro';
            $resultar["FechaRegistro"]='No registro';
            $resultar["Latitud"]='No registro';
            $resultar["Longitud"]='No registro';
            $resultar["Altitud"]='No registro';
            $resultar["Tipo"]='No registro';
            $resultar["Tipo2"]='No registro';
            $resultar["Cerrado"]='No registro';
            $resultar["FechaCierre"]='No registro';
            $resultar["Emergencycol"]='No registro';
            $json['emergency'][]=$resultar;

        }

        mysqli_close($conexion);
        echo json_encode($json);
        
    } else {
        $resultar["success"]=0;
        $resultar["message"]='Servicio web no retorna';
		$json['emergency'][]=$resultar;
		echo json_encode($json);
    }

?>