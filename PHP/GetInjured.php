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

	if(isset($_GET["idPatient"])){
		$idPatient=$_GET["idPatient"];
				
		$conexion = mysqli_connect($hostname_localhost,$username_localhost,$password_localhost,$database_localhost);

		$consulta="select injured.location, injured.Color, injured.state, user.fullName , injured.regDate, 
		injured.ContType, injured.ambulance, injured.detination, injured.bed, injured.name, 
		injured.gender, injured.age, injured.injuries from u614793440_triage.injured inner join 
		u614793440_triage.user on injured.regUser = user.idUser where idPatient= '{$idPatient}'";
		$resultado=mysqli_query($conexion,$consulta);
			
		if($registro=mysqli_fetch_array($resultado)){
			$result["location"]=$registro['location'];
			$result["Color"]=$registro['Color'];
			$result["state"]=$registro['state'];
			$result["fullName"]=$registro['fullName'];
			$result["regDate"]=$registro['regDate'];			
		
			$result["ContType"]=$registro['ContType'];
			$result["ambulance"]=$registro['ambulance'];
			$result["detination"]=$registro['detination'];
			$result["bed"]=$registro['bed'];
			$result["name"]=$registro['name'];
			$result["gender"]=$registro['gender'];
			$result["age"]=$registro['age'];
			$result["injuries"]=$registro['injuries'];
			$json['injured'][]=$result;
		
		}else{
			
			$resultar["location"]='no registra';
			$resultar["Color"]='no registra';
			$resultar["state"]='no registra';
			$resultar["regUser"]='no registra';
			$resultar["regDate"]='no registra';

			$resultar["ContType"]='no registra';
			$resultar["ambulance"]='no registra';
			$resultar["detination"]='no registra';
			$resultar["bed"]='no registra';
			$resultar["name"]='no registra';
			$resultar["gender"]='no registra';
			$resultar["age"]='no registra';
			$resultar["injuries"]='no registra';
			$json['injured'][]=$resultar;
		}
		
		mysqli_close($conexion);
		echo json_encode($json);
	}
	else{
		$resultar["success"]=0;
		$resultar["message"]='Ws no Retorna';
		$json['injured'][]=$resultar;
		echo json_encode($json);
	}
?>