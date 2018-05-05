<?php include "../inc/dbinfo.inc"; ?>
<?php
	header("Access-Control-Allow-Origin: *");
	$response = array();

	if(isset($_POST["email"]) && isset($_POST["pass"]))
	{
		$email = $_POST["email"];
		$pass = $_POST["pass"];

		$connection = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD, DB_DATABASE);

		if (mysqli_connect_errno()) echo "Failed to connect ot MySQL" . mysqli_connect_error();

		$sqlTask = "SELECT UserEmail, UserPassword From Users WHERE (UserEmail = ?)";

		$stmtTask = mysqli_prepare($connection, $sqlTask);

		mysqli_stmt_bind_param($stmtTask, "s", $email);

		mysqli_stmt_execute($stmtTask);

		mysqli_stmt_bind_result($stmtTask, $rEmail, $rPass);

		mysqli_stmt_fetch($stmtTask);

		if(strcmp($rEmail, $email) == 0 && strcmp($rPass, $pass) == 0)
		{
			$response["success"] = 1;
			$response["message"] = "Match";
//			$response["other"] = $task;

			echo json_encode($response);
			mysqli_close($connection);
		}
		else
		{
			$response["success"] = 0;
			$response["message"] = "No Match";

			echo json_encode($response);
			mysqli_close($connection);
		}
	}
	else
	{
		$response["success"] = 0;
		$response["message"] = "Missing parameters";

		echo json_encode($response);
	}
?>
