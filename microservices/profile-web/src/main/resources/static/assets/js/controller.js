'use strict';

/* Controllers */

angular.module('SharedServices', [])
    .config(function ($httpProvider) {
        $httpProvider.responseInterceptors.push('myHttpInterceptor');
        var spinnerFunction = function (data, headersGetter) {
            $('#mydiv, .ajax-loader').show();
            return data;
        };
        $httpProvider.defaults.transformRequest.push(spinnerFunction);
    })
    .factory('myHttpInterceptor', function ($q, $window) {
        return function (promise) {
            return promise.then(function (response) {
                $('#mydiv, .ajax-loader').hide();
                $('.hidden-content').removeClass('hidden-content');
                return response;
            }, function (response) {
                $('#mydiv, .ajax-loader').hide();
                $('.hidden-content').removeClass('hidden-content');
                return $q.reject(response);
            });
        };
    });

var contentappControllers = angular.module('contentappControllers', ['SharedServices']);

contentApp.controller('AccountCtrl', ['$scope', '$http', '$templateCache',
    function ($scope, $http) {
        $scope.url = '/api/profile/v1/profiles/';
        $scope.authUrl = '/api/user/uaa/v1/me';
        $scope.accounts = {};

        var fetchAccounts = function () {
            $http({
                method: 'GET',
                url: $scope.authUrl
            }).success(function (data) {
                $scope.user = data;
                $http({
                    method: 'GET',
                    url: $scope.url + $scope.user.username
                }).success(function (data) {
                    $scope.accounts = data;
                }).error(function (data) {
                    $scope.accounts = data;
                });
            }).error(function () {
                scope.user = {};
            });


        };

        fetchAccounts();
    }]);

contentApp.controller('HeaderCtrl', ['$scope', '$http',
    function ($scope, $http) {
        $scope.authUrl = '/api/user/uaa/v1/me';
        $scope.user = {};

        $scope.logout = function () {
            $http.post('logout', {}).success(function () {
                $rootScope.authenticated = false;
                $scope.user = {};
                $location.path("/");
                $location.reload($location.path);
                $rootScope.$broadcast('logout', "update");
            }).error(function (data) {
                $scope.user = {};
                $rootScope.$broadcast('logout', "update");
            });
        };


        var fetchUser = function () {
            $http({
                method: 'GET',
                url: $scope.authUrl
            }).success(function (data, status, headers, config) {
                $scope.user = data;
            }).error(function (data, status, headers, config) {
                scope.user = {};
            });
        };

        fetchUser();
    }]);

contentApp.controller('UpdateProfileCtrl', ['$rootScope', '$scope', '$http', '$location',
    function ($rootScope, $scope, $http) {
        $scope.updateProfile = function () {
            var req = {
                method: 'POST',
                url: '/api/profile/v1/profiles/' + $scope.accounts.username,
                headers: {
                    'Content-Type': "application/json"
                },
                data: {
                    "username": $scope.accounts.username,
                    "firstName": $scope.accounts.firstName,
                    "lastName": $scope.accounts.lastName,
                    "email": $scope.accounts.email
                }
            };

            $http(req).error(function (data) {
                alert(data.message)
            });
        }
    }]);

contentApp.filter('rawHtml', ['$sce', function ($sce) {
    return function (val) {
        return $sce.trustAsHtml(val);
    };
}]);

