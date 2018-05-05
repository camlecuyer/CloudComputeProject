<?php include "../inc/dbinfo.inc"; ?>
<?php
	header('Content-Type: application/json');
	header("Access-Control-Allow-Origin: *");

	$response = array();

	if(isset($_POST["issueid"]))
	{
		$issue = $_POST["issueid"];

		$connection = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD, DB_DATABASE);

		if (mysqli_connect_errno()) echo "Failed to connect ot MySQL" . mysqli_connect_error();

		$sqlTask = "Update Issues SET IssueIsClosed = 1, IssueDateClosed = CURDATE() WHERE (IssueID = ?)";

		$stmtTask = mysqli_prepare($connection, $sqlTask);

		mysqli_stmt_bind_param($stmtTask, "s", $issue);

		mysqli_stmt_execute($stmtTask);

		$checkTask = mysqli_stmt_affected_rows($stmtTask);

		if($checkTask == 1)
		{
			$response["success"] = 1;
			$response["message"] = "Successfully updated Issue";
//			$response["other"] = $issue;

			echo json_encode($response);
			mysqli_close($connection);
		}
		else
		{
			$response["success"] = 0;
			$response["message"] = "Failed to update Issue";

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
