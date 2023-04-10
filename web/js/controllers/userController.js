angular.module('Rutastic')
    .controller('userController',
        ['$location', '$routeParams', '$window', 'usersFactory', function ($location, $routeParams, $window, usersFactory) {
            let userVM = this;

            userVM.loggedUser = usersFactory.loggedUser
            userVM.user = {} // User being edited
            userVM.editionErrorMessage = '' // Message shown to the user in case any error arises during profile edition
            userVM.deletionErrorMessage = '' // Message shown to the user in case any error arises during profile deletion

            userVM.functions = {
                /**
                 * Get and store within the controller the requested user profile by the route parameter 'ID'
                 */
                readUserBeingEdited: function () {

                    // Check that the requested username can be obtained and perform an early validation

                    if ($routeParams.username !== undefined && $routeParams.username.trim().length > 0) {
                        usersFactory
                            .getUser($routeParams.username)
                            .then(function (response) {
                                userVM.user = response.data;
                                console.log(`Successfully retrieved the user with username (${$routeParams.username}) | Status: ${response.status}`);
                            }, function (response) {
                                console.log(`Error retrieving the user with username (${$routeParams.username}) | Status: ${response.status})`);
                            })
                    } else {
                        alert('El nombre de usuario no es válido');
                        console.log('ERROR READING THE REQUESTED USER. ROUTE PARAMETER (USERNAME) NOT SET OR INVALID');
                        $location.path('/');
                    }
                },
                /**
                 * Form submission handler for user profile edition
                 */
                submitEditionForm: function () {
                    usersFactory
                        .updateUser(userVM.user)
                        .then(function (status) {
                            console.log(`Successfully edited the user with username (${userVM.user.username}) | Status: ${status}`);

                            // On successful edition send the user to the landing page

                            $location.path('/');
                            userVM.editionErrorMessage = ''; // On successful edition clear any error message
                        }, function (status) {
                            userVM.editionErrorMessage = 'Compruebe que los campos sean válidos. Puede que el correo ya esté en uso por otro usuario';
                            console.log(`Error editing the user with username (${userVM.user.username}) | Status : ${status}`)
                        })
                },
                /**
                 * Form submission handler for user profile deletion
                 */
                submitDeletionForm: function () {
                    usersFactory
                        .deleteUser(userVM.user.username)
                        .then(function (status) {

                            // On successful user profile deletion log out the recently deleted user

                            $window.location.href = 'https://localhost:8443/Rutastic/Logout.do';
                            userVM.deletionErrorMessage = ''; // On successful edition clear any error message

                            console.log(`USER WITH USERNAME (${userVM.user.username}) WAS DELETED | Status : ${status}`);
                        }, function (status) {
                            userVM.deletionErrorMessage = 'Algo fue mal eliminando su perfil. Pruebe de nuevo más tarde';
                            console.log(`Error deleting the user with username (${userVM.user.username}) | Status: ${status}`);
                        })
                }
            }

            // On controller instantiation attempt to read the requested user profile

            userVM.functions.readUserBeingEdited();
        }])