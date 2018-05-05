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

                $result = mysqli_query($connection, "SELECT COUNT(IssueID) AS Count, BuildingID FROM Issues GROUP BY BuildingID ORDER BY Count DESC LIMIT 10");

                $result2 = mysqli_query($connection, "SELECT COUNT(IssueID) AS Count, CategoryID FROM Issues GROUP BY CategoryID ORDER BY Count DESC LIMIT 10");

                if(!empty($result) && !empty($result2))
                {
                        if((mysqli_num_rows($result) > 0) && (mysqli_num_rows($result2) > 0))
                        {
                                $results["build"] = array();

                                while($row = mysqli_fetch_array($result))
                                {
                                        $issues = array();
                                        $issues["Count"] = $row["Count"];
                                        $issues["BuildingID"] = $row["BuildingID"];

                                        array_push($results["build"], $issues);
                                }

                                $results["cat"] = array();

                                while($row = mysqli_fetch_array($result2))
                                {
                                        $issues = array();
                                        $issues["Count"] = $row["Count"];
                                        $issues["CategoryID"] = $row["CategoryID"];

                                        array_push($results["cat"], $issues);
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
