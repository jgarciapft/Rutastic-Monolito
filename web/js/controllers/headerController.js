angular.module('Rutastic')
    // Persistent Controller in charge of the navbar. Persistent across the use of the app if user doesn't refresh the page
    .controller('headerController', ['usersFactory', function (usersFactory) {

        let headerVM = this;

        headerVM.loggedUser = {}

        headerVM.functions = {
            /**
             * Retrieve the currently logged user within this web app
             */
            readLoggedUser: function () {
                usersFactory
                    .getLoggedUser()
                    .then(function () {
                        headerVM.loggedUser = usersFactory.loggedUser;
                    })
            }
        }

        // On controller instantiation read logged user and maintain it while the user is using this app

        headerVM.functions.readLoggedUser();
    }]);