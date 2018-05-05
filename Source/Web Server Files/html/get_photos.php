<?php include "../inc/dbinfo.inc"; ?>
<?php
	header('Content-Type: image/jpeg');
	header("Access-Control-Allow-Origin: *");

	 if(isset($_GET["id"]))
        {
                $connection = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD, DB_DATABASE);

                $id = mysqli_real_escape_string($connection, $_GET["id"]);

                if (mysqli_connect_errno())
                {
                        echo "failed";
                }
                else
                {
                        $db = mysqli_select_db($connection, DB_DATABASE);

                        $sql = "SELECT * FROM Photos WHERE PhotoID = ?";

                        $result = mysqli_query($connection, "SELECT * FROM Photos WHERE PhotoID = $id");

                        $res = mysqli_fetch_array($result);
                        echo(base64_decode($res["PhotoImage"]));

                        mysqli_close($connection);
                }
        }
        else
        {
                echo("Missing parameters");
        }
?>
