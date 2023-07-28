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

    if(isset($_GET["idEmergency"])){

        $idEmergency=$_GET["idEmergency"];
        
        $conexion = mysqli_connect($hostname_localhost,$username_localhost,$password_localhost,$database_localhost);

        $consulta="SELECT idPatient,location,Color,latitude,longitude,altitude FROM injured WHERE state = 'En espera' AND idEm = '{$idEmergency}'";
        $resultado=mysqli_query($conexion,$consulta);

    
        while($registro=mysqli_fetch_array($resultado)){
            
            $result["idPatient"]=$registro['idPatient'];
            $result["location"]=$registro['location'];
            $result["Color"]=$registro['Color'];
            $result["latitude"]=$registro['latitude'];
            $result["longitude"]=$registro['longitude'];
            $result["altitude"]=$registro['altitude'];

            $json['injured'][]=$result;
        
        }//*/
        
        mysqli_close($conexion);
        echo json_encode($json);
        
    } else {
        $resultar["success"]=0;
        $resultar["message"]='Servicio web no retorna';
        $json['injured'][]=$resultar;
        echo json_encode($json);
    }
        
?>