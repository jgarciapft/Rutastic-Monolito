angular.module('Rutastic')
    .factory('kudoEntriesFactory', ['$http', '$location', 'usersFactory', function ($http, $location, usersFactory) {

        let restBaseUrl = `https://${$location.host()}:${$location.port()}/Rutastic/rest/kudos`

        // FACTORY INTERFACE

        let kudoEntriesFactory = {
            /**
             * Retrieve all kudo entries associated with an user
             *
             * @param userId ID of the user
             * @return {HttpPromise|Promise|PromiseLike<T>|Promise<T>} A promise which resolves to the array of
             * kudo entries
             */
            getKudoEntriesOfUser: function (userId) {
                return $http
                    .get(`${restBaseUrl}/${userId}`)
                    .then(response => response.data);
            },
            /**
             * Retrieve all kudo entries associated for the logged user, if any
             *
             * @return {HttpPromise|Promise|PromiseLike<T>|Promise<T>} A promise which resolves to the array of
             * kudo entries
             */
            getKudoEntriesOfLoggedUser: function () {
                if (usersFactory.loggedUser !== undefined) // Check that there's a logged user first
                    return kudoEntriesFactory.getKudoEntriesOfUser(usersFactory.loggedUser.id);
            },
            /**
             * Get the kudo entry (if any) associated to an user and a route
             *
             * @param userId ID of the user
             * @param routeId ID of the route
             * @return {HttpPromise|Promise|PromiseLike<T>|Promise<T>} A promise which resolves to the requested kudo entry
             */
            getKudoEntryOfUserForRoute: function (userId, routeId) {
                return $http
                    .get(`${restBaseUrl}/${userId}/${routeId}`)
                    .then(response => response.data);
            }
        };

        return kudoEntriesFactory;
    }])