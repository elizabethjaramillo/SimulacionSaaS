<?php
if($_POST){
    $name = $_POST['name'];
    $email = $_POST['email'];
	$subject = $_POST['subject'];
    $message = $_POST['text'];
	
	$to = "info@spondonit.com";
	// subject
	$subject = 'From Cube+';
	// To send HTML mail, the Content-type header must be set
	$headers  = 'MIME-Version: 1.0' . "\r\n";
	$headers .= 'Content-type: text/html; charset=iso-8859-1' . "\r\n";
	
	// Additional headers
	
	//echo $headers .= 'From: '.$name.' <'.$email.'>' . "\r\n";
     

 	// Mail it
  	mail($to, $subject, $message, $headers);

}
?>