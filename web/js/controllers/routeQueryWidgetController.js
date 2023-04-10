angular.module('Rutastic')
    .controller('routeQueryWidgetController',
        ['$location', 'routeQueryFactory', 'usersFactory', function ($location, routeQueryFactory, usersFactory) {
            let routeQueryWidgetVM = this;

            routeQueryWidgetVM.loggedUser = usersFactory.loggedUser
            routeQueryWidgetVM.allUsernames = []
            routeQueryWidgetVM.routeQuery = routeQueryFactory.latestRouteQuery // Initialize with the latest route query

            routeQueryWidgetVM.functions = {
                /**
                 * Attempt to read all usernames for the advanced filter
                 */
                readAllUsernames: function () {
                    usersFactory
                        .getAllUsernames()
                        .then(function (usersArray) {
                            routeQueryWidgetVM.allUsernames = usersArray;
                        })
                },
                /**
                 * Use the route query stored in this controller to execute the filter that will retrieve
                 * the matching routes from the backend
                 */
                submitQuery: function () {
                    routeQueryFactory
                        .executeFilter(routeQueryWidgetVM.routeQuery)
                        .then(function () {
                            // When the filter execution has finished send the user to the route filter view
                            $location.path('/rutas/FiltrarRutas');
                        }, function () {
                            alert('Ocurrió un error al procesar la consulta. Pruebe de nuevo más tarde');
                            console.log('[RouteQueryWidgetController] Something went wrong while executing the route filter');
                        })
                },
                /**
                 * @param pathToTest Path to test against the current path. It should start with a leading slash ('/')
                 * @return {boolean} If the current path matches the provided one
                 */
                testPath: function (pathToTest) {
                    return $location.path() === pathToTest;
                }
            }

            // On controller load read all usernames

            routeQueryWidgetVM.functions.readAllUsernames();
        }]);