const HomeController = require('../controllers/home.controller');

module.exports = class HomeApi {
    constructor(app) {
        app.get('/', HomeController.index);

    }
};
