angular.module('Rutastic')
    .controller('routeDetailsController',
        ['$routeParams', '$location', 'routesFactory', 'usersFactory', 'kudoEntriesFactory', 'routeQueryFactory',
            function ($routeParams, $location, routesFactory, usersFactory, kudoEntriesFactory, routeQueryFactory) {

                let routeDetailsVM = this;

                routeDetailsVM.loggedUser = usersFactory.loggedUser
                routeDetailsVM.route = {}
                routeDetailsVM.kudoEntry = undefined
                routeDetailsVM.relatedRoutesByDistance = [] // Similar routes to this one by similar distance
                routeDetailsVM.relatedRoutesBySkillLevel = [] // Similar routes to this one by same skill level
                routeDetailsVM.relatedRoutesByCategories = [] // Similar routes to this one by same set of categories
                routeDetailsVM.currentSimilarity = 0
                routeDetailsVM.currentRelatedRoutesCollection = []

                routeDetailsVM.functions = {
                    /**
                     * Try getting the data of the requested route and, if there's a logged user, try retrieving the kudo
                     * entry associated to this user and route
                     */
                    readRequestedRoute: function () {

                        // Check that the requested route ID can be obtained and perform an early validation

                        if ($routeParams.ID !== undefined && $routeParams.ID > 0) {
                            routesFactory
                                .getRoute($routeParams.ID)
                                .then(function (response) {
                                    routeDetailsVM.route = response.data;
                                    console.log(`Retrieved route with ID (${$routeParams.ID}) | Status: ${response.status}`);

                                    // If there's a logged user try retrieving the associated kudo entry

                                    if (routeDetailsVM.loggedUser !== undefined)
                                        routeDetailsVM.functions.readAssociatedKudoEntry();


                                }, function (response) {
                                    alert('La ruta solicitada no existe');
                                    console.log(`Error retrieving the route with ID (${$routeParams.ID}) | Status: ${response.status}`);
                                    $location.path('/rutas/FiltrarRutas');
                                })
                                .then(function () {
                                    // Retrieve all related routes
                                    routeDetailsVM.functions.readAllRelatedRoutes();
                                })
                        } else {
                            alert('El identificador de la ruta no es válido');
                            console.log('ERROR READING THE REQUESTED ROUTE. ROUTE PARAMETER (ID) NOT SET OR INVALID');
                            $location.path('/rutas/FiltrarRutas');
                        }
                    },
                    /**
                     * Try retrieving the kudo entry associated to the logged user and the requested route. This object
                     * holds if the logged user voted for the requested route
                     */
                    readAssociatedKudoEntry: function () {
                        if (routeDetailsVM.loggedUser !== undefined) { // Check for a logged user first
                            kudoEntriesFactory
                                .getKudoEntryOfUserForRoute(routeDetailsVM.loggedUser.id, routeDetailsVM.route.id)
                                .then(function (kudoEntry) {
                                    /*
                                     * Check if any entry could be retrieved, other way an empty response may overwrite
                                     * the undefined value (no entry) for an empty string
                                     */
                                    routeDetailsVM.kudoEntry = typeof kudoEntry !== 'string' ? kudoEntry : undefined
                                });
                        }
                    },
                    /**
                     * Attempts to read all related routes to the one stored within this controller
                     */
                    readAllRelatedRoutes: function () {

                        // If the distance is below 500 m just use the actual distance as the delta to not exclude similarly short routes

                        let distanceDelta = routeDetailsVM.route.distance >= 500 ?
                            routeDetailsVM.route.distance / 4 : routeDetailsVM.route.distance;

                        // 3 Similar routes by similar distance (with more kudos)

                        routesFactory
                            .getRelatedRoutes(routeDetailsVM.route.id, 'distancia', 3, distanceDelta)
                            .then(function (relatedRoutes) {
                                routeDetailsVM.relatedRoutesByDistance = relatedRoutes;
                            });

                        // 3 Similar routes by same skill level (with more kudos)

                        routesFactory
                            .getRelatedRoutes(routeDetailsVM.route.id, 'dificultad', 3, 0)
                            .then(function (relatedRoutes) {
                                routeDetailsVM.relatedRoutesBySkillLevel = relatedRoutes;
                            });

                        // 3 Similar routes by same set of route categories (with more kudos)

                        routesFactory
                            .getRelatedRoutes(routeDetailsVM.route.id, 'categorias', 3, 0)
                            .then(function (relatedRoutes) {
                                routeDetailsVM.relatedRoutesByCategories = relatedRoutes;
                            });
                    },
                    /**
                     * Update the blocked state of the requested route
                     */
                    toggleRouteBlockedState: function () {
                        routesFactory
                            .setNewBlockedState(routeDetailsVM.route.id, routeDetailsVM.route.blocked ? 'unblocked' : 'blocked')
                            .then(function (status) {
                                routeDetailsVM.functions.reflectRouteChange();
                                console.log(`Route ${routeDetailsVM.route.blocked ? 'unblocked' : 'blocked'} | Status: ${status}`);
                            }, function (status) {
                                alert(`Error al ${routeDetailsVM.route.blocked ? 'desbloquear' : 'bloquear'} la ruta. Puede que intente bloquear una ruta ya bloqueada y viceversa`);
                                console.log(`Route blocked state update failed for route with ID (${routeDetailsVM.route.id}) | Status: ${status}`);
                            });
                    },
                    /**
                     * Update the kudo rating for the requested route
                     *
                     * @param newRating Either a 1 (give kudo or retract from giving one) or a -1 (take a kudo or retract
                     * from taking one)
                     */
                    updateKudoRating: function (newRating) {
                        routesFactory
                            .updateKudoRating(routeDetailsVM.route.id, newRating)
                            .then(function (status) {
                                routeDetailsVM.functions.reflectRouteChange();
                                console.log(`Updated kudo rating (${newRating}) | Status: ${status}`);
                            }, function (status) {
                                alert('No se pudo cambiar la puntuación de la ruta. Pruebe más tarde');
                                console.log(`Kudo rating update failed for route with ID (${routeDetailsVM.route.id}) | Status: ${status}`);
                            })
                    },
                    /**
                     * Updates any change made to this route, making them visible to the view, and refresh the current
                     * route query to maintain consistency across views
                     */
                    reflectRouteChange: function () {
                        routeDetailsVM.functions.readRequestedRoute();
                        routeQueryFactory.refreshFilter();
                    },
                    /**
                     * Updates de related routes the user should see depending on selected similarity (distance,
                     * skill level or categories)
                     */
                    changeCurrentRelatedRoutesCollection: function () {
                        switch (routeDetailsVM.currentSimilarity) {
                            case "1":
                                routeDetailsVM.currentRelatedRoutesCollection = routeDetailsVM.relatedRoutesByDistance;
                                break;
                            case "2":
                                routeDetailsVM.currentRelatedRoutesCollection = routeDetailsVM.relatedRoutesBySkillLevel;
                                break;
                            case "3":
                                routeDetailsVM.currentRelatedRoutesCollection = routeDetailsVM.relatedRoutesByCategories;
                                break;
                        }
                    }
                }

                // On controller instantiation read the requested route

                routeDetailsVM.functions.readRequestedRoute();
            }])