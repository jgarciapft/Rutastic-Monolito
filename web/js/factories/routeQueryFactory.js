angular.module('Rutastic')
    .factory('routeQueryFactory',
        ['$http', '$httpParamSerializer', '$location', 'kudoEntriesFactory', 'usersFactory', 'routesFactory',
            function ($http, $httpParamSerializer, $location, kudoEntriesFactory, usersFactory, routesFactory) {

                let restUrl = `https://${$location.host()}:${$location.port()}/Rutastic/rest/rutas/filtro`;

                // FACTORY PROPERTIES

                let routeQueryFactory = {
                    latestRouteQuery: {}, // Object representing the latest executed route query
                    filteredRoutes: [], // Collection of routes that made through the route filter
                    topWeeklyRoutes: [], // Top users in descending order by top monthly routes
                    topMonthlyRoutes: [], // Top users in descending order by average kudo ratings of their routes
                    topUsersByTopRoutes: [], // Top routes of the week by kudo ratings submitted this week
                    topUsersByAvgKudos: [], // Top routes of the month by kudo ratings submitted this month
                    kudoEntriesModMap: {} // An object whose keys are route IDs and entries the kudo modifier for that route and logged user
                }

                // FACTORY INTERFACE

                /**
                 * Execute a filter retrieve a subset of the stored routes that matches the filter spawned by the
                 * specified query object. A call to this function also updates the latest executed query, which is
                 * stored within this factory
                 *
                 * @param query Object representing a route query
                 * @return {HttpPromise|Promise|PromiseLike<T>|Promise<T>} A promise which doesn't resolve to anything
                 */
                routeQueryFactory.executeFilter = function (query) {
                    routeQueryFactory.latestRouteQuery = query; // Update the latest executed route query

                    return $http
                        .get(`${restUrl}?${$httpParamSerializer(query)}`)
                        .then(response => {
                            routeQueryFactory.filteredRoutes = response.data;
                            console.log(`Retrieved (${routeQueryFactory.filteredRoutes.length}) routes from route query`);
                        })
                        // Retrieve top users and routes
                        .then(function () {
                            routeQueryFactory.getTopCharts();
                        })
                        // If there's a logged user retrieve his kudo entries as well and populate the entries map
                        .then(function () {
                            if (usersFactory.loggedUser !== undefined) {
                                kudoEntriesFactory
                                    .getKudoEntriesOfLoggedUser()
                                    .then(kudoEntries => {
                                            // Map each kudo entry { route : id, modifier: mod } to --> { id : mod }
                                            routeQueryFactory.kudoEntriesModMap = kudoEntries.reduce((map, kudoEntry) => {
                                                map[kudoEntry.route] = kudoEntry.modifier;
                                                return map;
                                            }, {});
                                            console.log(`Retrieved (${kudoEntries.length}) kudo entries submitted by the logged user`);
                                        }
                                    );
                            }
                        })
                }

                /**
                 * Retrieve top users and top routes
                 */
                routeQueryFactory.getTopCharts = function () {
                    usersFactory
                        .getTop5UsersByTopRoutes()
                        .then(function (topUsersArray) {
                            routeQueryFactory.topUsersByTopRoutes = topUsersArray;
                        }, function () {
                            console.log('Couldn\'t retrieve top users by top routes');
                        });
                    usersFactory
                        .getTop5UsersByAvgKudos()
                        .then(function (topUsersArray) {
                            routeQueryFactory.topUsersByAvgKudos = topUsersArray;
                        }, function () {
                            console.log('Couldn\'t retrieve top users by average kudo ratings');
                        });
                    routesFactory
                        .getTop5RoutesOfTheWeek()
                        .then(function (topRoutesArray) {
                            routeQueryFactory.topWeeklyRoutes = topRoutesArray;
                        }, function () {
                            console.log('Couldn\'t retrieve top weekly routes');
                        });
                    routesFactory
                        .getTop5RoutesOfTheMonth()
                        .then(function (topRoutesArray) {
                            routeQueryFactory.topMonthlyRoutes = topRoutesArray;
                        }, function () {
                            console.log('Couldn\'t retrieve top monthly routes');
                        });
                }

                /**
                 * Refresh the filtered routes by executing again the latest executed route query
                 *
                 * @return {HttpPromise|Promise|PromiseLike<T>|Promise<T>} A promise which doesn't resolve to anything
                 */
                routeQueryFactory.refreshFilter = function () {
                    return routeQueryFactory.executeFilter(routeQueryFactory.latestRouteQuery);
                }

                return routeQueryFactory;
            }])