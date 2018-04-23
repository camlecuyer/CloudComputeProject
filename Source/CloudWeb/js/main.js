var app = angular.module('reportResponse', []);

app.controller('login-control', function($scope, $http) {
    $scope.login = function() {
        /*$http({
            url: user.details_path,
            method: "GET",
            params: {user_id: user.id}
        });*/
    };
});

app.controller('report-control', function($scope, $http) {

});
