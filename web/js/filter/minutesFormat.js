angular.module('Rutastic')
    // Displays minutes either in minutes or hours, depending on the minutes' magnitude
    .filter('minutesFormat', function () {
        return function (minutes) {
            return minutes > 60 ? (minutes / 60.0).toFixed(2) : minutes;
        }
    });