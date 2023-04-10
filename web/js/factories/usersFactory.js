angular.module('Rutastic')
    // Services related to users resource
    .factory('usersFactory', ['$http', function ($http) {

        let restBaseUrl = 'https://localhost:8443/Rutastic/rest/usuarios';

        // FACTORY PROPERTIES

        let usersFactory = {
            loggedUser: undefined, // Holds the logged user within this app
        }

        // FACTORY INTERFACE

        /**
         * Try retrieving the logged user and update property that holds it. If there's no user logged then
         * the property doesn't change (remains undefined)
         *
         * @return {HttpPromise|Promise|PromiseLike<T>|Promise<T>} A promise that doesn't resolve to anything
         */
        usersFactory.getLoggedUser = function () {
            return $http
                .get(`${restBaseUrl}/q?recurso=usuariologgeado`)
                .then(response => {

                    // Check that the response contains a valid user object before updating the logged user property

                    if (typeof response.data !== 'string') {
                        usersFactory.loggedUser = response.data;
                        console.log(`Successfully retrieved logged user (${usersFactory.loggedUser.username})`);
                    } else {
                        console.log('ERROR RETRIEVING THE LOGGED USER. There might not be a logged user yet');
                    }
                })
        }

        /**
         * Retrieve all registered usernames
         *
         * @return {HttpPromise|Promise|PromiseLike<T>|Promise<T>} A promise which resolves to an array of user model
         * objects, with all the personal info cleared, just the username left
         */
        usersFactory.getAllUsernames = function () {
            return $http
                .get(`${restBaseUrl}`)
                .then(response => response.data);
        };

        /**
         * Retrieve an user by its username
         *
         * @param username The username of the user to be retrieved
         * @return {HttpPromise|Promise|PromiseLike<T>|Promise<T>} An HTTP promise with the user and the status code
         */
        usersFactory.getUser = function (username) {
            return $http
                .get(`${restBaseUrl}/${username}`)
                .then(response => response);
        }

        /**
         * Retrieve a list of the users who are authors of the top monthly routes, ordered by descending number
         * of top monthly routes. For an user to be taken into account at least one of their routes need to have
         * received a kudo rating
         *
         * @return {HttpPromise|Promise|PromiseLike<T>|Promise<T>} A promise which resolves to an array of the top users
         */
        usersFactory.getTop5UsersByTopRoutes = function () {
            return $http
                .get(`${restBaseUrl}/estadisticas?e=top5UsuariosPorTopRutas`)
                .then(response => response.data);
        }

        /**
         * Retrieve a list of users ordered by descending average kudo rating of the routes they're authors of.
         * For an user to be taken into account at least one of their routes need to have received a kudo rating
         *
         * @return {HttpPromise|Promise|PromiseLike<T>|Promise<T>} A promise which resolves to an array of the top users
         */
        usersFactory.getTop5UsersByAvgKudos = function () {
            return $http
                .get(`${restBaseUrl}/estadisticas?e=top5UsuariosPorMediaKudos`)
                .then(response => response.data);
        }

        /**
         * Register a new user of this app
         *
         * @param newUser The new user
         * @return {HttpPromise|Promise|PromiseLike<T>|Promise<T>} A promise that resolves to the status code of the
         * response
         */
        usersFactory.registerNewUser = function (newUser) {
            return $http
                .post(restBaseUrl, newUser)
                .then(response => response.status);
        }

        /**
         * Update the data of an user with new provided values
         *
         * @param user User object that contains the new values
         * @return {HttpPromise|Promise|PromiseLike<T>|Promise<T>} A promise that resolves to the status code of the
         * response
         */
        usersFactory.updateUser = function (user) {
            return $http
                .put(`${restBaseUrl}/${user.username}`, user)
                .then(response => response.status);
        }

        /**
         * Delete the profile of an existing user
         *
         * @param username The username of the user being deleted
         * @return {HttpPromise|Promise|PromiseLike<T>|Promise<T>} A promise that resolves to the status code of the
         * response
         */
        usersFactory.deleteUser = function (username) {
            return $http
                .delete(`${restBaseUrl}/${username}`)
                .then(response => response.status);
        }

        return usersFactory;
    }]);