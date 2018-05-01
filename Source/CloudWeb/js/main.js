var app = angular.module('reportResponse', ['ui.bootstrap']);

app.controller('login-control', function($scope, $http, $window)
{
    $scope.login = function()
    {
        if ($scope.user != null && $scope.password != null) {
            $.post("http://18.219.51.47/get_user.php", {
                    email: $scope.user, pass: $scope.password
                },
                function (result) {
                    var res = JSON.parse(result);

                    if (res["success"] == "1") {
                        $window.location.href = "./main.html";
                    }
                    else {
                        window.alert("Incorrect Login")
                        console.log(res["message"]);
                    }
                });
        }
    };
});

app.controller('report-control', function($scope, $http, $modal, $window) {
    $scope.init = function() {
        $scope.getIssues();

        $scope.getTasks();
    };

    $scope.getIssues = function() {
        if(result.data.success == "1")
        {
            $scope.issues = result.data.results[0].issues;
        }
    };

    $scope.collapseIssue = function() {
        this.cols.expanded = !this.cols.expanded;
     };

    $scope.collapseTask = function() {
        this.taskCol.expanded = !this.taskCol.expanded;
    };

    $scope.collapseTaskHome = function() {
        this.homeCol.expanded = !this.homeCol.expanded;
    };

    $scope.collapseTaskClosed = function() {
        this.homeClosed.expanded = !this.homeClosed.expanded;
    };

    $scope.addTask = function (issue) {
        var issueToConvert = issue;

        $modal.open({
            templateUrl: "addTask.html",
            backdrop: true,
            windowClass: 'modal',
            controller: function ($scope, $modalInstance, $log) {
                $scope.floors = [0, 1, 2, 3, 4, 5];

                $scope.issue = issueToConvert;

                $scope.init = function () {

                    $http.get("http://18.219.51.47/get_items.php").then(function (result) {
                        $scope.categories = result.data.results[0].categories;
                        $scope.buildings = result.data.results[0].buildings;
                        $scope.divisions = result.data.results[0].divisions;
                    });
                };

                $scope.add = function () {
                    if ($scope.taskDesc != null && $scope.selectedDiv != null && $scope.selectedFloor != null && $scope.selectedBuild != null) {
                        var buildIndex = -1;
                        var divIndex = -1;
                        $log.log("Add," + $scope.issue.IssueID + "," + $scope.taskDesc);

                        $scope.buildings.forEach((build, index) => {
                            if ($scope.selectedBuild == build.BuildingName) {
                                buildIndex = index;
                            }
                        });

                        $scope.divisions.forEach((divs, index) => {
                            if ($scope.selectedDiv == divs.DivisionName) {
                                divIndex = index;
                            }
                        });
                        $log.log($scope.buildings[buildIndex].BuildingID + "," + $scope.divisions[divIndex].DivisionID + "," + $scope.selectedFloor);

                        $.post("http://18.219.51.47/post_task.php", {
                                divid: $scope.divisions[divIndex].DivisionID,
                                buildid: $scope.buildings[buildIndex].BuildingID,
                                issid: $scope.issue.IssueID,
                                desc: $scope.taskDesc,
                                floor: $scope.selectedFloor,
                                loc: $scope.issue.IssueLocation
                            },
                            function (result) {
                                var res = JSON.parse(result);
                                if (res["success"] == "1") {
                                    var $scope = angular.element($('[ng-controller]')).scope();

                                    $scope.closeIssue(issueToConvert);
                                    window.alert("Task Created");

                                }
                                else {
                                    window.alert("Task Creation Failed");
                                    console.log(res["message"]);
                                }
                            });
                        $modalInstance.dismiss('cancel');
                    }
                    else {
                        window.alert("Please fill out all fields. For Outside, choose any valid floor.")
                    }
                };

                $scope.close = function () {

                    $modalInstance.dismiss('cancel');
                };
            }
        });
    };

    $scope.selectIssue = function(index) {
        $scope.selected = $scope.issues[index];
        console.log($scope.selected);
    };

    $scope.getTasks = function () {
        $http.get("http://18.219.51.47/get_tasks.php").then(function (result) {
            if (result.data.success == "1") {
                $scope.tasks = result.data.results[0].tasks;
            }
        });

        $http.get("http://18.219.51.47/get_closedtasks.php").then(function (result) {
            if (result.data.success == "1") {
                $scope.closedtasks = result.data.results[0].tasks;
            }
        });
    };

    $scope.closeIssue = function (issue) {
        $.post("http://18.219.51.47/put_issue.php", {
                issueid: issue.IssueID
            },
            function (result) {
                var res = result;

                if (res["success"] == "1") {
                    var $scope = angular.element($('[ng-controller]')).scope();
                    $scope.getIssues();
                    $scope.getTasks();
                    window.alert("Issue closed");
                }
                else {
                    window.alert("Issue not closed");
                    console.log(res["message"]);
                }
            });
    };
});
