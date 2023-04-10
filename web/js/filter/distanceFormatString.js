angular.module('Rutastic')
    // Displays the appropriate unit for a distance format filter
    .filter('distanceFormatString', function () {
        return function (distanceMeasure) {
            return distanceMeasure > 1000 ? 'km' : 'm';
        }
    })