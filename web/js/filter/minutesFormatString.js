angular.module('Rutastic')
    // Displays the appropriate unit for a minutes format filter
    .filter('minutesFormatString', function () {
        return function (minutes) {
            return minutes > 60 ? 'h' : 'min';
        }
    });