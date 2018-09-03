(function () {
    'use strict';

    angular
        .module('gatewayApp')
        .factory('EchoService', EchoService)
        .factory('StoreService', StoreService)
        .factory('JobService', JobService);

    EchoService.$inject = ['$resource'];
    JobService.$inject = ['$resource'];
    StoreService.$inject = ['$resource'];

    function EchoService($resource) {
        return $resource('echo/echo', {}, {
            'echo': {
                method: 'GET',
                transformResponse: function (data, headers, statusCode) {
                    return {
                        data: data,
                        responseStatusCode: statusCode
                    };
                },

            }
        });
    }

    function JobService($resource) {
        return {
            "create": $resource('jobs/jobs', {}, {
                'jobs': {
                    method: 'POST',
                    transformResponse: function (data, headers, statusCode) {
                        return {
                            data: data,
                            responseStatusCode: statusCode
                        };
                    },

                },

            }),
            "get": $resource('jobs/api/job-results/:jobKey', {}, {
                'jobs': {
                    method: 'GET',
                    params: {jobKey: '@jobKey'},
                    transformResponse: function (data, headers, statusCode) {
                        return {
                            data: data,
                            responseStatusCode: statusCode
                        };
                    },

                },

            })
        };
    }

    function StoreService($resource) {
        return $resource('store/store', {}, {
            'create': {
                method: 'POST',
                transformResponse: function (data, headers, statusCode) {
                    return {
                        data: data,
                        responseStatusCode: statusCode
                    };
                },

            }
        });
    }
})();
