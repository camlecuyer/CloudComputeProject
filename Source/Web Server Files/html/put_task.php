<?php include "../inc/dbinfo.inc"; ?>
<?php
	header('Content-Type: application/json');
	header("Access-Control-Allow-Origin: *");

	$response = array();

	if(isset($_POST["taskid"]))
	{
		$task = $_POST["taskid"];

		$connection = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD, DB_DATABASE);

		if (mysqli_connect_errno()) echo "Failed to connect ot MySQL" . mysqli_connect_error();

		$sqlTask = "Update Tasks SET TaskIsClosed = 1, TaskDateClosed = CURDATE() WHERE (TaskID = ?)";

		$stmtTask = mysqli_prepare($connection, $sqlTask);

		mysqli_stmt_bind_param($stmtTask, "s", $task);

		mysqli_stmt_execute($stmtTask);

		$checkTask = mysqli_stmt_affected_rows($stmtTask);

		if($checkTask == 1)
		{
			$response["success"] = 1;
			$response["message"] = "Successfully updated Task";
//			$response["other"] = $task;

			echo json_encode($response);
			mysqli_close($connection);
		}
		else
		{
			$response["success"] = 0;
			$response["message"] = "Failed to update Task";

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
