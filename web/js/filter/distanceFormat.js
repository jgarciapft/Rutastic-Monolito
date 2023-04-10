angular.module('Rutastic')
    // Displays a distance measure of the metric system as meters or kilometers depending on the measure's magnitude
    .filter('distanceFormat', function () {
        return function (distanceMeasure) {
            return distanceMeasure > 1000 ? (distanceMeasure / 1000.0).toFixed(2) : distanceMeasure;
        }
    })