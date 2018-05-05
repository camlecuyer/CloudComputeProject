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

		$result = mysqli_query($connection, "SELECT * FROM reportResponse.Categories");

		if(!empty($result))
		{
			if(mysqli_num_rows($result) > 0)
			{
				$results["categories"] = array();

				while($row = mysqli_fetch_array($result))
				{

					$categories = array();
					$categories["CategoryID"] = $row["CategoryID"];
					$categories["CatName"] = $row["CatName"];
					$categories["CatDesc"] = $row["CatDesc"];

					array_push($results["categories"], $categories);
				}
			}
			else
			{
				$response["success"] = 0;
				$response["message"] = "No categories found";

				echo json_encode($response);
				mysqli_close($connection);
				return;
			}
		}
		else
		{
			$response["success"] = 0;
			$response["message"] = "No table category found";

			echo json_encode($response);
			mysqli_close($connection);
			return;
		}

		$result = mysqli_query($connection, "SELECT * FROM reportResponse.Buildings");

		if(!empty($result))
		{
			if(mysqli_num_rows($result) > 0)
			{
				$results["buildings"] = array();

				while($row = mysqli_fetch_array($result))
				{
					$buildings = array();
					$buildings["BuildingID"] = $row["BuildingID"];
					$buildings["BuildingName"] = $row["BuildingName"];

					array_push($results["buildings"], $buildings);
				}
			}
			else
			{
				$response["success"] = 0;
				$response["message"] = "No buildings found";

				echo json_encode($response);
				mysqli_close($connection);
				return;
			}
		}
		else
		{
			$response["success"] = 0;
			$response["message"] = "No table buildings";

			echo json_encode($response);
			mysqli_close($connection);
			return;
		}

		$result = mysqli_query($connection, "SELECT * FROM reportResponse.Divisions");

		if(!empty($result))
		{
			if(mysqli_num_rows($result) > 0)
			{
				$results["divisions"] = array();

				while($row = mysqli_fetch_array($result))
				{
					$divisions = array();
					$divisions["DivisionID"] = $row["DivisionID"];
					$divisions["DivisionName"] = $row["DivisionName"];

					array_push($results["divisions"], $divisions);
				}

				array_push($response["results"], $results);

				$response["success"] = 1;
				$response["message"] = "Success";

				echo json_encode($response);
				mysqli_close($connection);
			}
			else
			{
				$response["success"] = 0;
				$response["message"] = "No divisions found";

				echo json_encode($response);
				mysqli_close($connection);
				return;
			}
		}
		else
		{
			$response["success"] = 0;
			$response["message"] = "No division table";

			echo json_encode($response);
			mysqli_close($connection);
			return;
		}
	}
?>
