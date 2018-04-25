var app = angular.module('reportResponse', []);

app.controller('login-control', function($scope, $http, $window)
{
    $scope.login = function()
    {
        if ($scope.user != null && $scope.password != null)
        {
            $.post("http://18.219.51.47/get_user.php",{
            email: $scope.user, pass: $scope.password},
                function(result){
                    var res = JSON.parse(result);

                    if(res["success"] == "1")
                    {
                        $window.location.href = "./main.html";
                    }
                    else
                    {
                        window.alert("Incorrect Login")
                        console.log(res["message"]);
                    }
                });


            /*$http({
                url: "http://18.219.51.47/get_user.php",
                method: "POST",
                params: {
                    email: $scope.user,
                    pass: $scope.password
                }//,
                //headers: {'Content-Type': 'application/x-www-form-urlencoded'}
            }).then(function(response) {
                var res = response.data;

                if(res["suceess"] == "1")
                {
                    $window.location.href = "/main.html";
                }
                else
                {
                    window.alert("Incorrect Login")
                    console.log(res["message"]);
                }
            });*/
        }
        /*
        * temp = new JSONObject(response);

                            Toast.makeText(getApplicationContext(), temp.getString("message"), Toast.LENGTH_SHORT).show();
                            loginButton.setEnabled(true);

                            if(temp.getString("success").equals("1"))
*/
    };
});

app.controller('report-control', function($scope, $http) {
    $scope.init = function() {

        $scope.getIssues();

        $scope.getTasks();

        $http.get("http://18.219.51.47/get_items.php").then(function(result) {
            $scope.categories = result.data.results[1].categories;
            $scope.buildings = result.data.results[0].buildings;
            $scope.divisions = result.data.results[2].divisions;
        });

    }

    $scope.getIssues = function() {
        $http.get("http://18.219.51.47/get_issues.php").then(function(result) {
            $scope.issues = result.data.results[0].issues;
        });
    }

    $scope.getTasks = function() {
        $http.get("http://18.219.51.47/get_tasks.php").then(function(result) {
            $scope.tasks = result.data.results[0].tasks;
        });

        $http.get("http://18.219.51.47/get_closedtasks.php").then(function(result) {
            $scope.closedtasks = result.data.results[0].tasks;
        });
    }
});
