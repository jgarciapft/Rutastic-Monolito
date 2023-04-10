angular.module('Rutastic', ['ngRoute'])
    .config(function ($routeProvider) {
        $routeProvider
            // Default route mapped to the landing page
            .when('/', {
                controller: '',
                controllerAs: '',
                templateUrl: 'pages/landingPage.html',
                resolve: {
                    // Produce a 250 milliseconds delay that should be enough to allow the server to read the logged user
                    // Extracted from script.js used as example on https://docs.angularjs.org/api/ngRoute/service/$route
                    delay: function ($q, $timeout) {
                        var delay = $q.defer();
                        $timeout(delay.resolve, 250);
                        return delay.promise;
                    }
                }
            })
            .when('/Registro', {
                controller: 'registrationController',
                controllerAs: 'registrationVM',
                templateUrl: 'pages/registration.html'
            })
            .when('/usuarios/EditarPerfil/:username', {
                controller: 'userController',
                controllerAs: 'userVM',
                templateUrl: 'pages/editProfile.html'
            })
            .when('/rutas/CrearRuta', {
                controller: 'routeHandlerController',
                controllerAs: 'routeHandlerVM',
                templateUrl: 'pages/routeCRUD.html'
            })
            .when('/rutas/EditarRuta/:ID', {
                controller: 'routeHandlerController',
                controllerAs: 'routeHandlerVM',
                templateUrl: 'pages/routeCRUD.html'
            })
            .when('/rutas/FiltrarRutas', {
                controller: 'routeQueryController',
                controllerAs: 'routeQueryVM',
                templateUrl: 'pages/queryRoutes.html'
            })
            .when('/rutas/DetallesRuta/:ID', {
                controller: 'routeDetailsController',
                controllerAs: 'routeDetailsVM',
                templateUrl: 'pages/routeDetails.html'
            })
        ;
    });