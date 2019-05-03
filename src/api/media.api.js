const MediaController = require('../controllers/media.controller');

module.exports = class MediaApi {
    constructor(app) {
        app.get('/media/refresh', MediaController.refresh);

    }
};
