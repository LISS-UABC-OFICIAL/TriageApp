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

        $consulta="SELECT user.idUser, user.fullName, user.mail, emergency_staff.userAccessLevel 
        FROM u614793440_triage.user INNER JOIN emergency_staff ON emergency_staff.User_idUser = user.idUser
        WHERE emergency_staff.Emergency_idEmergency = '{$idEmergency}'";
        $resultado=mysqli_query($conexion, $consulta);

        while($registro=mysqli_fetch_array($resultado)){
            
            $result["idUser"]=$registro['idUser'];
            $result["fullName"]=$registro['fullName'];
            $result["accessLevel"]=$registro['userAccessLevel'];

            $json['staff'][]=$result;

        }

        mysqli_close($conexion);
        echo json_encode($json);
        
    } else {
        $resultar["success"]=0;
        $resultar["message"]='Servicio web no retorna';
		$json['staff'][]=$resultar;
		echo json_encode($json);
    }

?>