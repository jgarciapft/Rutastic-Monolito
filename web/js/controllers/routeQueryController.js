angular.module('Rutastic')
    .controller('routeQueryController',
        ['routeQueryFactory', 'routesFactory', 'usersFactory', function (routeQueryFactory, routesFactory, usersFactory) {

            let routeQueryVM = this;

            routeQueryVM.loggedUser = usersFactory.loggedUser
            routeQueryVM.factories = {
                routeQuery: routeQueryFactory
            }

            routeQueryVM.functions = {
                /**
                 * Update the kudo rating for the requested route
                 *
                 * @param routeId ID of the route target of the kudo rating update
                 * @param newRating Either a 1 (give kudo or retract from giving one) or a -1 (take a kudo or retract
                 * from taking one)
                 */
                updateKudoRating: function (routeId, newRating) {
                    routesFactory
                        .updateKudoRating(routeId, newRating)
                        .then(function (status) {
                            routeQueryFactory.refreshFilter();
                            console.log(`Updated kudo rating (${newRating}) for route with ID (${routeId}) | Status: ${status}`);
                        }, function (status) {
                            alert('No se pudo cambiar la puntuación de la ruta. Pruebe más tarde');
                            console.log(`Kudo rating update failed for route with ID (${routeId}) | Status: ${status}`);
                        })
                }
            }

            // On controller load make the query factory retrieve the top charts info

            routeQueryFactory.getTopCharts();
        }])