angular.module('Rutastic')
    .controller('routeHandlerController',
        ['$location', '$routeParams', 'routesFactory', 'routeCategoriesFactory', 'routeQueryFactory', 'usersFactory',
            function ($location, $routeParams, routesFactory, routeCategoriesFactory, routeQueryFactory, usersFactory) {

                let routeHandlerVM = this;

                routeHandlerVM.loggedUser = usersFactory.loggedUser
                routeHandlerVM.route = {}
                routeHandlerVM.routecategories = [] // Possible route categories
                routeHandlerVM.selectedCategoryNames = [] // Names of the categories that should be selected on route edition
                routeHandlerVM.crudErrorMessage = '' // Message shown to the user in case any error arises during route creation / edition
                routeHandlerVM.deletionErrorMessage = '' // Message shown to the user in case any error arises during route deletion
                routeHandlerVM.CATEGORYSEP = routesFactory.CATEGORYSEP // String used to split category names on a route object model

                routeHandlerVM.functions = {
                    /**
                     * Attempt to read the requested user for edition / deletion
                     */
                    readRequestedRoute: function () {

                        // Check if the route parameter ID is set and perform an early validation

                        if ($routeParams.ID !== undefined && $routeParams.ID > 0) {

                            let targetRouteID = $routeParams.ID;

                            routesFactory
                                .getRoute(targetRouteID)
                                .then(
                                    function (response) {
                                        routeHandlerVM.route = response.data;
                                        // Populate already selected categories
                                        routeHandlerVM.selectedCategoryNames =
                                            routeHandlerVM.route.categories.split(routeHandlerVM.CATEGORYSEP);

                                        console.log(`Retrieved route with ID (${targetRouteID})`);

                                        // Populate route categories

                                        routeHandlerVM.functions.readRouteCategories();
                                    },
                                    function (response) {
                                        alert('La ruta solicitada no existe');
                                        console.log(`Error retrieving the route with ID (${targetRouteID}) | Status: ${response.status}`);
                                        $location.path('/');
                                    }
                                );
                        } else {
                            alert('El identificador de la ruta no es válido');
                            console.log('ERROR READING THE REQUESTED ROUTE. ROUTE PARAMETER (ID) NOT SET OR INVALID');
                            $location.path('/');
                        }
                    },
                    /**
                     * Attempts to read all assignable route categories for a route
                     */
                    readRouteCategories: function () {
                        routeCategoriesFactory
                            .getAllRouteCategories()
                            .then(
                                function (response) {
                                    routeHandlerVM.routecategories = response.data;
                                }, function (response) {
                                    console.log(`Error retrieving all route categories | Status: ${response.status}`);
                                })
                    },
                    /**
                     * Form submission handler for route creation and edition
                     */
                    submitRouteForm: function () {

                        /*
                         * Merge each selected category into a single categories string separated by the category
                         * separator. Needed to make a new route / edited route valid for the backend
                         */
                        routeHandlerVM.route.categories = routeHandlerVM.route.categories.join(routeHandlerVM.CATEGORYSEP);

                        // Decide whether the submission came from the route creation or edition form

                        if (routeHandlerVM.functions.isCreationForm()) { // CREATION form
                            routesFactory
                                .createRoute(routeHandlerVM.route)
                                .then(
                                    function (response) {

                                        // On success send the user to check the details of the route he's just created

                                        $location.path(`/rutas/DetallesRuta/${(response.routeId)}`);
                                        routeHandlerVM.crudErrorMessage = ''; // Clear any error message

                                        console.log(`Created route with ID (${(response.routeId)}) | Status: ${response.status}`);
                                    },
                                    function (response) {
                                        routeHandlerVM.crudErrorMessage = 'Revise que los campos sean válidos. Debe proporcionar un título, descripción, una o varias categorías registradas, la distancia, elevación y duración como números positivos y una dificultad registrada';
                                        console.log(`An error occurred while creating a route | Status: ${response.status}`);
                                    })
                        } else if (routeHandlerVM.functions.isEditionForm()) { // EDITION form
                            routesFactory
                                .updateRoute(routeHandlerVM.route)
                                .then(
                                    function (status) {
                                        routeQueryFactory.refreshFilter(); // Refresh the route filter to reflect any changes

                                        // On success send the user to check the details of the route he's just edited

                                        $location.path(`/rutas/DetallesRuta/${routeHandlerVM.route.id}`);
                                        routeHandlerVM.crudErrorMessage = ''; // Clear any error message
                                        console.log(`Updated route with ID (${routeHandlerVM.route.id}) | Status: ${status}`);
                                    },
                                    function (status) {
                                        routeHandlerVM.crudErrorMessage = 'Revise que los sean validos y pruebe de nuevo. Debe proporcionar un título, descripción, una o varias categorías registradas, la distancia, elevación y duración como números positivos y una dificultad registrada. También es posible que no sea el autor de la ruta y no pueda editarla';
                                        console.log(`An error ocurred while updating the route with ID (${routeHandlerVM.route.id}) | Status: ${status}`)
                                    }
                                )
                        }
                    },
                    /**
                     * Form submission handler for route deletion
                     */
                    submitDeletionForm: function () {
                        routesFactory
                            .deleteRoute(routeHandlerVM.route.id)
                            .then(
                                function (status) {
                                    routeQueryFactory.refreshFilter(); // Refresh the route filter to reflect route deletion

                                    // On successful deletion send the user to the landing page

                                    $location.path('/');

                                    routeHandlerVM.deletionErrorMessage = ''; // Clear any error message
                                    console.log(`Route with ID (${routeHandlerVM.route.id}) successfully deleted | Status: ${status}`);
                                },
                                function (status) {
                                    routeHandlerVM.deletionErrorMessage = 'Algo fue mal eliminando la ruta. Pruebe de nuevo más tarde';
                                    console.log(`Error deleting the route with ID (${routeHandlerVM.user.id}) | Status: ${status}`);
                                }
                            )
                    },
                    /**
                     * @param pathToTest Path to test against the current path. It should start with a leading slash ('/')
                     * @return {boolean} If the current path matches the provided one
                     */
                    testPath: function (pathToTest) {
                        return $location.path() === pathToTest;
                    },
                    isCreationForm: function () {
                        return routeHandlerVM.functions.testPath('/rutas/CrearRuta');
                    },
                    isEditionForm: function () {
                        return routeHandlerVM.functions.testPath(`/rutas/EditarRuta/${$routeParams.ID}`);
                    }
                };

                // On controller instantiation read route categories and attempt to read the requested in case of route edition / deletion

                if (routeHandlerVM.functions.isEditionForm())
                    routeHandlerVM.functions.readRequestedRoute();
                else
                    routeHandlerVM.functions.readRouteCategories();
            }])