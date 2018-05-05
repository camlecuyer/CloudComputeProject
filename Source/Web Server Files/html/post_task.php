<?php include "../inc/dbinfo.inc"; ?>
<?php
	header("Access-Control-Allow-Origin: *");

	$response = array();

	if(isset($_POST["divid"]) && isset($_POST["buildid"]) && isset($_POST["issid"]) && isset($_POST["desc"]) &&
		isset($_POST["floor"]) && isset($_POST["loc"]))
	{
		$div = $_POST["divid"];
		$build = $_POST["buildid"];
		$issue = $_POST["issid"];
		$desc = $_POST["desc"];
		$floor = $_POST["floor"];
		$loc = $_POST["loc"];

		$connection = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD, DB_DATABASE);

		if (mysqli_connect_errno()) echo "Failed to connect ot MySQL" . mysqli_connect_error();

		$sqlTask = "INSERT INTO Tasks(DivisionID, BuildingID, IssueID, TaskDesc, TaskDateOpened, TaskFloorNumber, TaskLocation) VALUES(?,?,?,?,CURDATE(),?,?)";

		$stmtTask = mysqli_prepare($connection, $sqlTask);

		mysqli_stmt_bind_param($stmtTask, "ssssis", $div, $build, $issue, $desc, $floor, $loc);

		mysqli_stmt_execute($stmtTask);

		$checkTask = mysqli_stmt_affected_rows($stmtTask);

		if($checkTask == 1)
		{
			$response["success"] = 1;
			$response["message"] = "Successfully uploaded Task";
//			$response["other"] = $div . "," . $build . "," . $desc . "," . $floor . "," . $loc;

			echo json_encode($response);
			mysqli_close($connection);
		}
		else
		{
			$response["success"] = 0;
			$response["message"] = "Failed to create Task";

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
