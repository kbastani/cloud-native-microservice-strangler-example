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
        $scope.url = '/api/profile/v1/profiles';
        $scope.accounts = {};

        var fetchAccounts = function () {
            $http({
                method: 'GET',
                url: $scope.url
            }).success(function (data) {
                $scope.accounts = data;
            }).error(function (data, status, headers, config) {
                $scope.accounts = data;
            });
        };

        fetchAccounts();
    }]);

contentApp.controller('HeaderCtrl', ['$scope', '$http',
    function ($scope, $http) {
        $scope.authUrl = '/api/user/uaa/v1/me';
        $scope.meUrl = '/api/user/uaa/v1/me';
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

contentApp.filter('rawHtml', ['$sce', function ($sce) {
    return function (val) {
        return $sce.trustAsHtml(val);
    };
}]);

