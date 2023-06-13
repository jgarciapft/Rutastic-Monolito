angular.module('Rutastic')
    .factory('routesFactory', ['$http', '$location', function ($http, $location) {

        let restBaseUrl = `https://${$location.host()}:${$location.port()}/Rutastic/rest/rutas`

        // FACTORY PROPERTIES

        let routesFactory = {
            CATEGORYSEP: ', ' // String that splits different route categories in categories string
        }

        // FACTORY INTERFACE

        /**
         * Retrieve a route by its ID
         *
         * @param routeId The ID of the route to be retrieved
         * @return {HttpPromise|Promise|PromiseLike<T>|Promise<T>} A promise which resolves to the response object
         * that contains the requested route
         */
        routesFactory.getRoute = function (routeId) {
            return $http
                .get(`${restBaseUrl}/${routeId}`)
                .then(response => response);
        }

        /**
         * Retrieve a descending ordered list with the routes with more kudos this week. Routes with negative or 0
         * kudo balance are not taking into account here. Consider the initial day of a week to be monday
         *
         * @return {HttpPromise|Promise|PromiseLike<T>|Promise<T>} A promise which resolves to an array of top routes
         */
        routesFactory.getTop5RoutesOfTheWeek = function () {
            return $http
                .get(`${restBaseUrl}/estadisticas?e=topRutasSemanal`)
                .then(response => response.data);
        }

        /**
         * Retrieve a descending ordered list with the routes with more kudos this month. Routes with negative or 0
         * kudo balance are not taking into account here
         *
         * @return {HttpPromise|Promise|PromiseLike<T>|Promise<T>} A promise which resolves to an array of top routes
         */
        routesFactory.getTop5RoutesOfTheMonth = function () {
            return $http
                .get(`${restBaseUrl}/estadisticas?e=topRutasMensual`)
                .then(response => response.data);
        }

        /**
         * Retrieve similar routes to a given one by its ID and a common similarity. These can be either distance,
         * skill level or route categories set
         *
         * @param routeId Route ID of the route for which similar routes are being retrieved
         * @param similarity Common similarity to all retrieved routes
         * @param limit Limit the number of retrieved related routes
         * @param distanceDelta Distance delta when the similarity is 'distancia'
         * @return {HttpPromise|Promise|PromiseLike<T>|Promise<T>} A promise which resolves to the array of related routes
         */
        routesFactory.getRelatedRoutes = function (routeId, similarity, limit, distanceDelta) {

            let limitQueryParam = limit > 0 ? `limite=${limit}` : '';
            let distanceDeltaQueryParam = distanceDelta > 0 ? `deltaDistancia=${distanceDelta}` : '';

            // Build query string based on which parameters have been provided

            let extraQueryBits =
                `${limitQueryParam ? '&' + limitQueryParam : ''}${distanceDeltaQueryParam ? '&' + distanceDeltaQueryParam : ''}`

            return $http
                .get(`${restBaseUrl}/${routeId}/similares?por=${similarity}${extraQueryBits}`)
                .then(response => response.data);
        }

        /**
         * Create a new route
         *
         * @param route The route object containing the route info
         * @return {HttpPromise|Promise|PromiseLike<T>|Promise<T>} A promise which resolves to the response object,
         * which has attaches to a new property 'routeId' the id of the newly created route
         */
        routesFactory.createRoute = function (route) {
            return $http
                .post(restBaseUrl, route)
                .then(response => {

                    // Retrieve from the location header the ID of the newly created route

                    let locationHeaderSplit = response.headers(['location']).split('/');
                    response.routeId = locationHeaderSplit[locationHeaderSplit.length - 1]; // Get last entry

                    return response;
                });
        }

        /**
         * Update the data of a route with new provided values
         *
         * @param route Object with the new values for the route
         * @return {HttpPromise|Promise|PromiseLike<T>|Promise<T>} A promise which resolves to the HTTP status code
         * of the response
         */
        routesFactory.updateRoute = function (route) {
            return $http
                .put(`${restBaseUrl}/${route.id}`, route)
                .then(response => response.status);
        }

        /**
         * Delete a stored route by its ID
         *
         * @param routeId The ID of the route to be deleted
         * @return {HttpPromise|Promise|PromiseLike<T>|Promise<T>} A promise that resolves to the HTTP status code
         * of the response
         */
        routesFactory.deleteRoute = function (routeId) {
            return $http
                .delete(`${restBaseUrl}/${routeId}`)
                .then(response => response.status);
        }

        /**
         * Update the blocked state of a route
         *
         * @param routeId The ID of the route whose blocked status is being updated
         * @param action A string that specifies the new blocked state of the route. Legal values are 'blocked' to
         * block a route, and 'unblocked' to unblock a route
         * @return {HttpPromise|Promise|PromiseLike<T>|Promise<T>} A promise which resolves to the HTTP status code
         * of the response
         */
        routesFactory.setNewBlockedState = function (routeId, action) {
            return $http
                .put(`${restBaseUrl}/${routeId}/estado?accion=${action === 'blocked' ? 'bloquear' : action === 'unblocked' ? 'desbloquear' : ''}`, null)
                .then(response => response.status);
        }

        /**
         * Update the kudo rating of a route on behalf of the logged user
         *
         * @param routeId ID of the route target of the kudo rating update
         * @param action Either a 1 (give kudo or retract from giving one) or a -1 (take a kudo or retract
         * from taking one)
         */
        routesFactory.updateKudoRating = function (routeId, action) {
            return $http
                .put(`${restBaseUrl}/${routeId}/kudos?accion=${action === 1 ? 'dar' : action === -1 ? 'quitar' : ''}`, null)
                .then(response => response.status);
        }

        return routesFactory;
    }])