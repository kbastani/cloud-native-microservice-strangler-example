'use strict';

/* App Module */

var contentApp = angular.module('contentApp', [
    'ngRoute',
    'contentappControllers'
]);

contentApp.config(['$routeProvider',
    function ($routeProvider) {
        $routeProvider.when('/', {
            templateUrl: 'assets/partials/account.html',
            controller: 'AccountCtrl'
        }).otherwise({
            redirectTo: '/'
        });
    }]);



