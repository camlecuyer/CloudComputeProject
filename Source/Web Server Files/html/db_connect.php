<?php include "../inc/dbinfo.inc"; ?>
<?php
class DB_CONNECT {
	function __construct() {
		$this->connect();
	}

	function __destruct() {
		$this->close();
	}

	function connect() {
		$con = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD);

		if(mysqli_connect_errno()) echo "Failed to connect to MySQL" . mysqli_connect_error();

		$db = mysqli_select_db($con, DB_DATABASE);

		return $con;
	}

	function close() {
		mysqli_close();
	}
}

$db = new DB_CONNECT();
echo "a";
echo $db.connect();
?>
