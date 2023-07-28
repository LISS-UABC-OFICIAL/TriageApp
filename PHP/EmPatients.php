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

        $consulta="SELECT injured.idPatient, injured.location, injured.Color, injured.isContaminated, injured.ContType, injured.regUser, injured.state, injured.regDate, injured.latitude, 
        injured.longitude, injured.altitude, injured.detination, injured.ambulance, injured.bed, injured.name, injured.gender, injured.age, injured.injuries 
        FROM u614793440_triage.injured INNER JOIN emergency_patients ON emergency_patients.idPatient = injured.idPatient
        WHERE emergency_patients.idEmergency = '{$idEmergency}'";
        $resultado=mysqli_query($conexion, $consulta);

        while($registro=mysqli_fetch_array($resultado)){
            
            $result["idPatient"]=$registro['idPatient'];
            $result["location"]=$registro['location'];
            $result["Color"]=$registro['Color'];
            $result["isContaminated"]=$registro['isContaminated'];
            $result["ContType"]=$registro['ContType'];
            $result["regUser"]=$registro['regUser'];
            $result["state"]=$registro['state'];
            $result["regDate"]=$registro['regDate'];
            $result["latitude"]=$registro['latitude'];
            $result["longitude"]=$registro['longitude'];
            $result["altitude"]=$registro['altitude'];
            $result["detination"]=$registro['detination'];
            $result["ambulance"]=$registro['ambulance'];
            $result["bed"]=$registro['bed'];
            $result["name"]=$registro['name'];
            $result["gender"]=$registro['gender'];
            $result["age"]=$registro['age'];
            $result["injuries "]=$registro['injuries'];

            $json['injured'][]=$result;

        }

        mysqli_close($conexion);
        echo json_encode($json);
        
    } else {
        $resultar["success"]=0;
        $resultar["message"]='Servicio web no retorna';
		$json['injured'][]=$resultar;
		echo json_encode($json);
    }

?>