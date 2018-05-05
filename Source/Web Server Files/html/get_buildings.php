<?php include "../inc/dbinfo.inc"; ?>
<?php
	header('Content-Type: application/json');

	$response = array();

	$connection = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD, DB_DATABASE);

	if (mysqli_connect_errno()) echo "Failed to connect to MySQL" . mysqli_connect_error();

	$db = mysqli_select_db($connection, DB_DATABASE);

	$result = mysqli_query($connection, "SELECT * FROM reportResponse.Buildings");

	if(!empty($result)) {
		if(mysqli_num_rows($result) > 0) {
			$response["buildings"] = array();

			while($row = mysqli_fetch_array($result)) {

				$buildings = array();
				$buildings["BuildingID"] = $row["BuildingID"];
				$buildings["BuildingName"] = $row["BuildingName"];

				array_push($response["buildings"], $buildings);
			}

			$response["success"] = 1;
			$response["message"] = "Success";

			echo json_encode($response);
		}
		else
		{
			$response["success"] = 0;
			$response["message"] = "No categories found";

			echo json_encode($response);
		}
	}
	else
	{
		$response["success"] = 0;
		$response["message"] = "No categories found";

		echo json_encode($response);
	}
?>
