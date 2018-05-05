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

//		$result = mysqli_query($connection, "SELECT IssueID, CategoryID, BuildingID, Photos.PhotoImage, IssueDesc, IssueFloorNumber, IssueEmail, IssuePhone, IssueFirstName, IssueLocation FROM Issues INNER JOIN Photos ON Issues.PhotoID = Photos.PhotoID WHERE IssueIsClosed = 0 ORDER BY IssueDateOpened ASC LIMIT 15");
		$result = mysqli_query($connection, "SELECT IssueID, CategoryID, BuildingID, PhotoID, IssueDesc, IssueDateOpened, IssueFloorNumber, IssueEmail, IssuePhone, IssueFirstName, IssueLocation FROM Issues WHERE IssueIsClosed = 0 ORDER BY IssueDateOpened ASC LIMIT 15");

		if(!empty($result))
		{
			if(mysqli_num_rows($result) > 0)
			{
				$results["issues"] = array();

				while($row = mysqli_fetch_array($result))
				{

					$issues = array();
					$issues["IssueID"] = $row["IssueID"];
					$issues["CategoryID"] = $row["CategoryID"];
					$issues["BuildingID"] = $row["BuildingID"];
					$issues["PhotoID"] = $row["PhotoID"];
					$issues["IssueDateOpened"] = $row["IssueDateOpened"];
					$issues["IssueDesc"] = $row["IssueDesc"];
					$issues["IssueFloorNumber"] = $row["IssueFloorNumber"];
					$issues["IssueEmail"] = $row["IssueEmail"];
					$issues["IssuePhone"] = $row["IssuePhone"];
					$issues["IssueFirstName"] = $row["IssueFirstName"];
					$issues["IssueLocation"] = $row["IssueLocation"];

					array_push($results["issues"], $issues);
				}
					$response["success"] = 1;
					$response["message"] = "Issues found";
					array_push($response["results"], $results);
					echo json_encode($response);
					mysqli_close($connection);
					return;
			}
			else
			{
				$response["success"] = 0;
				$response["message"] = "No issues found";

				echo json_encode($response);
				mysqli_close($connection);
				return;
			}
		}
		else
		{
			$response["success"] = 0;
			$response["message"] = "No table issue found";

			echo json_encode($response);
			mysqli_close($connection);
			return;
		}
	}
?>
