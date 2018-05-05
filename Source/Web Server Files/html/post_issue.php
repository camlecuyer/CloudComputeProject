<?php include "../inc/dbinfo.inc"; ?>
<?php
	header("Access-Control-Allow-Origin: *");

	$response = array();

	if(isset($_POST["catid"]) && isset($_POST["buildid"]) && isset($_POST["photo"]) && isset($_POST["desc"]) &&
		isset($_POST["floor"]) && isset($_POST["email"]) && isset($_POST["name"]) && isset($_POST["phone"])
		 && isset($_POST["loc"]))
	{
		$cat = $_POST["catid"];
		$build = $_POST["buildid"];
		$photo = $_POST["photo"];
		$desc = $_POST["desc"];
		$floor = $_POST["floor"];
		$email = $_POST["email"];
		$name = $_POST["name"];
		$phone = $_POST["phone"];
		$loc = $_POST["loc"];

		$connection = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD, DB_DATABASE);

		if (mysqli_connect_errno()) echo "Failed to connect ot MySQL" . mysqli_connect_error();

		$sql = "INSERT INTO Photos(PhotoImage) VALUES (?)";

		$stmt = mysqli_prepare($connection, $sql);

		$content = null;

		mysqli_stmt_bind_param($stmt, "s", $photo);

		mysqli_stmt_execute($stmt);

		$check = mysqli_stmt_affected_rows($stmt);

		if($check == 1)
		{
			$photoID = mysqli_insert_id($connection);
			mysqli_stmt_close($stmt);

			$sqlIssue = "INSERT INTO Issues(CategoryID, BuildingID, PhotoID, IssueDesc, IssueDateOpened, IssueFloorNumber, IssueEmail, IssuePhone, IssueFirstName, IssueLocation) VALUES(?,?,?,?,CURDATE(),?,?,?,?,?)";

			$stmtIssue = mysqli_prepare($connection, $sqlIssue);

			mysqli_stmt_bind_param($stmtIssue, "ssssissss", $cat, $build, $photoID, $desc, $floor, $email, $phone, $name, $loc);

			mysqli_stmt_execute($stmtIssue);

			$checkIssue = mysqli_stmt_affected_rows($stmtIssue);

			if($checkIssue == 1)
			{
				$response["success"] = 1;
				$response["message"] = "Successfully uploaded Issue";
//				$response["other"] = $cat . "," . $build . "," . $desc . "," . $floor . "," . $email . "," . $phone . "," . $name . "," . $loc;

				echo json_encode($response);
				mysqli_close($connection);
			}
			else
			{
				$response["success"] = 0;
				$response["message"] = "Failed to create Issue";

				echo json_encode($response);
				mysqli_close($connection);
			}
		}
		else
		{
			$response["success"] = 0;
			$response["message"] = "Insert Photo failed";

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
