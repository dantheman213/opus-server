const homeController = require('../controllers/home.controller');

module.exports = class HomeApi {
    constructor(app) {
        app.get('/', homeController.index);

    }
};
