const express = require('express');

const port = 3000;
const app = express();

const HomeApi = require('./api/home.api');
new HomeApi(app);

const MediaApi = require('./api/media.api');
new MediaApi(app);

app.listen(port, () => console.log(`Opus Server listening on port ${port}!`));
