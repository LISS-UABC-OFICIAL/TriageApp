<?php
/*$hostname_localhost ="127.0.0.1";
$database_localhost ="registroIncidentes";
$username_localhost ="root";
$password_localhost ="";*/

$hostname_localhost ="localhost";
$database_localhost ="u614793440_triage";
$username_localhost ="u614793440_triage";
$password_localhost ="Triagedb1";

$conexion=mysqli_connect($hostname_localhost,$username_localhost,$password_localhost,$database_localhost);

    $idEmergency = $_POST["idEmergency"];
    $em_ClosedDate = $_POST["em_ClosedDate"];
    $em_IsCLosed = 1;

    /*$idEmergency = "19";
    $em_ClosedDate = "2023-05-30 16:07:06";
    $em_IsCLosed = 1;*/
    
    //UPDATE `registroincidentes`.`emergency` SET `em_IsClosed` = '1', `em_CloseDate` = '2023-05-30 16:07:06' WHERE (`idEmergency` = '20');

    $sql = "UPDATE emergency SET em_IsClosed = ?, em_CloseDate = ? WHERE idEmergency = ?";
    $stm = $conexion->prepare($sql);
    $stm->bind_param('sss', $em_IsCLosed, $em_ClosedDate, $idEmergency);
		
	if($stm->execute()){
		echo "actualizado";
	}else{
		echo "noActualiza";
	}
	mysqli_close($conexion);
?>

