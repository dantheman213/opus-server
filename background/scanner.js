const Database = require('../src/lib/database');
const mongoose = require('mongoose');
const recursive = require('recursive-readdir');

class Scanner {
    static checkItem(mediaPath) {

    }

    static async run() {
        const startTime = new Date();
        await Database.connect();

        recursive('/media', (err, files) => {
            const mediaFiles = files.filter(f => /^.*\.(wav|mp3|ogg|wma|aif|aiff|aifc|aac|flac|alac)$/.test(f.toLowerCase()));
            const endTime = new Date();

            for (const audioFile of mediaFiles) {
                Scanner.checkItem(audioFile);
            }

            console.log(`Finished in ${endTime - startTime}ms.`);
        });
    }
}

Scanner.run();
