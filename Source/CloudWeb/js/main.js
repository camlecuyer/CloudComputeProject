var app = angular.module('reportResponse', []);

app.controller('login-control', function($scope, $http)
{
    $scope.login = function()
    {
        if ($scope.user != null && $scope.password != null)
        {

            $http({
                url: "http://18.219.51.47/get_user.php",
                method: "POST",
                params: {
                    email: $scope.user,
                    pass: $scope.password
                },
                headers: {'Content-Type': 'application/x-www-form-urlencoded'}
            }).then(function(response) {
                //var res = JSON.parse(response);

                if(response["suceess"] == "1")
                {

                }
                else
                {
                    console.log(response["message"]);
                }
            });
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
    var init = function() {

    }

    init();
});
