(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', 'Principal', 'LoginService', '$state', 'EchoService', 'JobService', 'StoreService'];

    function HomeController ($scope, Principal, LoginService, $state, EchoService, JobService, StoreService) {
        var vm = this;

        vm.account = null;
        vm.isAuthenticated = null;
        vm.login = LoginService.open;
        vm.register = register;
        $scope.$on('authenticationSuccess', function() {
            getAccount();
        });

        vm.echoMessage = "A message";
        vm.echoErrorMessage = "A message with error";
        vm.jobMessage = "A job message";
        vm.jobErrorMessage = "A job message with error";
        vm.storeMessage = "A store message";
        vm.storeErrorMessage = "A store message with error";

        getAccount();

        function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
            });
        }
        function register () {
            $state.go('register');
        }

        vm.echo = function() {
            EchoService.echo({message: vm.echoMessage}, function (result) {
                vm.resultStatus = result.responseStatusCode;
                vm.result = result.data;
            },function (result) { // on error
                vm.resultStatus = result.data.responseStatusCode;
                vm.result = result.data.data;
            });
        };

        vm.echoError = function() {
            EchoService.echo({message: vm.echoErrorMessage, error: true}, function (result) {
                vm.resultStatus = result.responseStatusCode;
                vm.result = result.data;
            },function (result) { // on error
                vm.resultStatus = result.data.responseStatusCode;
                vm.result = result.data.data;
            });
        };

        vm.job = function () {
            JobService.create.jobs({message: vm.jobMessage, shouldFail: false}, {}, function (result) {
                vm.resultStatus = result.responseStatusCode;
                vm.result = result.data;
                var jobKey = JSON.parse(result.data).jobKey;

                console.log(jobKey);

                setTimeout(function() {
                    JobService.get.jobs({jobKey: jobKey}, {}, function (r) {
                        vm.result += '\n' + r.data;
                    })
                }, 5000);

            },function (result) { // on error
                vm.resultStatus = result.data.responseStatusCode;
                vm.result = result.data.data;
            });

        };


        vm.jobError = function () {
            JobService.create.jobs({message: vm.jobErrorMessage, shouldFail: true}, {}, function (result) {
                vm.resultStatus = result.responseStatusCode;
                vm.result = result.data;
                var jobKey = JSON.parse(result.data).jobKey;

                console.log(jobKey);

                setTimeout(function() {
                    JobService.get.jobs({jobKey: jobKey}, {}, function (r) {
                        vm.result += '\n' + r.data;
                    })
                }, 5000);

            },function (result) { // on error
                vm.resultStatus = result.data.responseStatusCode;
                vm.result = result.data.data;
            });

        };

        vm.store = function() {
            StoreService.create({message: vm.storeMessage, shouldFail: false}, {}, function (result) {
                vm.resultStatus = result.responseStatusCode;
                vm.result = result.data;
            },function (result) { // on error
                vm.resultStatus = result.data.responseStatusCode;
                vm.result = result.data.data;
            });
        };

        vm.storeError = function() {
            StoreService.create({message: vm.storeErrorMessage, shouldFail: true}, {}, function (result) {
                vm.resultStatus = result.responseStatusCode;
                vm.result = result.data;
            },function (result) { // on error
                vm.resultStatus = result.data.responseStatusCode;
                vm.result = result.data.data;
            });
        };


    }
})();
