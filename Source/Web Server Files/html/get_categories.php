<?php include "../inc/dbinfo.inc"; ?>
<?php
	header('Content-Type: application/json');

	$response = array();

	$connection = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD, DB_DATABASE);

	if (mysqli_connect_errno()) echo "Failed to connect to MySQL" . mysqli_connect_error();

	$db = mysqli_select_db($connection, DB_DATABASE);

	$result = mysqli_query($connection, "SELECT * FROM reportResponse.Categories");

	if(!empty($result)) {
		if(mysqli_num_rows($result) > 0) {
			$response["categories"] = array();

			while($row = mysqli_fetch_array($result)) {

				$categories = array();
				$categories["CategoryID"] = $row["CategoryID"];
				$categories["CatName"] = $row["CatName"];
				$categories["CatDesc"] = $row["CatDesc"];

				array_push($response["categories"], $categories);
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
