angular.module('Rutastic')
    // Services related to the style and formatting of a view
    .factory('stylesFactory', ['$location', function ($location) {
        return {
            /**
             * @return {boolean} Returns whether the view corresponding to the current route should have a white background
             */
            hasWhiteBg: function () {
                return $location.path().match(/(FiltrarRutas|DetallesRuta)/) !== null;
            },
            /**
             * @return {boolean} Whether the user is currently at the registration page
             */
            isRegistrationForm: function () {
                return $location.path().match(/Registro/) !== null;
            }
        }
    }])