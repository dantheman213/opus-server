class Scanner {
    static async run() {
        const Database = require('../src/lib/database');
        await Database.connect();

        const startTime = new Date();
        const recursive = require('recursive-readdir');

        recursive('/media', (err, files) => {
            const mediaFiles = files.filter(f => /^.*\.(wav|mp3|ogg|wma|aif|aiff|aifc|aac|flac|alac)$/.test(f.toLowerCase()));
            const endTime = new Date();
            console.log(mediaFiles);
            console.log(`Finished in ${endTime - startTime}ms.`);
        });
    }
}

Scanner.run();
