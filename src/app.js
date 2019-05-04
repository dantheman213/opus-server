class Application {
    static async start() {
        const Database = require('./lib/database');
        await Database.connect();

        const express = require('express');
        const app = express();

        const HomeApi = require('./api/home.api');
        new HomeApi(app);

        const MediaApi = require('./api/media.api');
        new MediaApi(app);

        const port = 3000;
        app.listen(port, () => console.log(`Opus Server listening on port ${port}!`));
    }
}

Application.start();
