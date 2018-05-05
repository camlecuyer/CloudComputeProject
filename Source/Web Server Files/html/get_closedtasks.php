<?php include "../inc/dbinfo.inc"; ?>
<?php
	header('Content-Type: application/json');
	header("Access-Control-Allow-Origin: *");

	$response = array();
	$response["results"] = array();
	$results = array();

	$connection = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD, DB_DATABASE);

	if (mysqli_connect_errno())
	{
		$response["success"] = 0;
		$response["message"] = "Failed to connect to database";

		echo json_encode($response);
	}
	else
	{
		$db = mysqli_select_db($connection, DB_DATABASE);

		$result = mysqli_query($connection, "SELECT TaskID, DivisionID, BuildingID, IssueID, TaskDesc, TaskDateClosed, TaskFloorNumber, TaskLocation FROM Tasks WHERE TaskIsClosed = 1 ORDER BY TaskDateOpened DESC LIMIT 10");

		if(!empty($result))
		{
			if(mysqli_num_rows($result) > 0)
			{
				$results["tasks"] = array();

				while($row = mysqli_fetch_array($result))
				{

					$issues = array();
					$issues["TaskID"] = $row["TaskID"];
					$issues["DivisionID"] = $row["DivisionID"];
					$issues["BuildingID"] = $row["BuildingID"];
					$issues["IssueID"] = $row["IssueID"];
					$issues["TaskDesc"] = $row["TaskDesc"];
					$issues["TaskDateClosed"] = $row["TaskDateClosed"];
					$issues["TaskFloorNumber"] = $row["TaskFloorNumber"];
					$issues["TaskLocation"] = $row["TaskLocation"];

					array_push($results["tasks"], $issues);

					$response["success"] = 1;
					$response["message"] = "Tasks found";
				}

				array_push($response["results"], $results);

				echo json_encode($response);
				mysqli_close($connection);
				return;
			}
			else
			{
				$response["success"] = 0;
				$response["message"] = "No tasks found";

				echo json_encode($response);
				mysqli_close($connection);
				return;
			}
		}
		else
		{
			$response["success"] = 0;
			$response["message"] = "No table task found";

			echo json_encode($response);
			mysqli_close($connection);
			return;
		}
	}
?>
