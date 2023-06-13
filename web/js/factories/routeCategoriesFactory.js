angular.module('Rutastic')
    .factory('routeCategoriesFactory', ['$http', '$location', function ($http, $location) {

        let restBaseUrl = `https://${$location.host()}:${$location.port()}/Rutastic/rest/categoriasruta`;

        return {
            /**
             * Retrieve all the registered route categories
             *
             * @return {HttpPromise|Promise|PromiseLike<T>|Promise<T>} A promise which resolves to the response object,
             * which contains the collection of route categories
             */
            getAllRouteCategories: function () {
                return $http
                    .get(restBaseUrl)
                    .then(response => response);
            }
        }
    }])