angular.module('Rutastic')
    .controller('registrationController', ['$window', 'usersFactory', function ($window, usersFactory) {

        let registrationVM = this;

        registrationVM.user = {} // Holds data of a potential new user
        registrationVM.errorMessage = '' // Message shown to the user in case an error arises during registration

        registrationVM.functions = {
            /**
             * Submission handle for the registration form. Try registering a new user and redirect the user to
             * the login page
             */
            submitRegistrationForm: function () {
                usersFactory
                    .registerNewUser(registrationVM.user)
                    .then(function (status) {

                        // On successful registration redirect the user to the login page

                        $window.location.href = 'https://localhost:8443/Rutastic/Login.do';
                        registrationVM.errorMessage = ''; // On successful registration clear the error message

                        console.log(`New user registered successfully | Status: ${status}`);
                    }, function (status) {
                        registrationVM.errorMessage = 'Compruebe que el email es válido y que ha proporcionado un nombre de usuario y contraseña. Puede que el nombre de usuario o correo ya esté registrado';

                        console.log(`Error registering a new user | Status: ${status}`);
                    });
            }
        }
    }
    ]);