angular.module('Rutastic')
    // Controller related to the style and formatting of persistent view, outside templates
    .controller('pageStyleController', ['stylesFactory', function (stylesFactory) {

        let pageStyleVM = this;

        pageStyleVM.functions = {
            hasWhiteBg: stylesFactory.hasWhiteBg,
            isRegistrationForm: stylesFactory.isRegistrationForm
        }
    }]);