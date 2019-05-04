const mm = require('music-metadata');
const recursive = require('recursive-readdir');
const Database = require('../src/lib/database');
const mongoose = require('mongoose');
const songSchema = require('../src/schemas/song.schema');

class Scanner {
    static async checkItem(mediaPath) {
        const songs = mongoose.model('songs', songSchema);
        const query = songs.where({ path: mediaPath });
        const result = await query.findOne();
        if (result) {
            console.log(`Skipping existing item ${mediaPath}`);
            result.updatedAt = new Date();
            await result.save();
        } else {
            console.log(`Adding ${mediaPath} to database...`);
            const metadata = await mm.parseFile(mediaPath);
            console.log(metadata);
            // const song = {
            //     title:
            // }
        }
    }

    static async run() {
        const startTime = new Date();
        await Database.connect();

        recursive('/media', async (err, files) => {
            const mediaFiles = files.filter(f => /^.*\.(wav|mp3|ogg|wma|aif|aiff|aifc|aac|flac|alac)$/.test(f.toLowerCase()));
            console.log(`Found ${mediaFiles.length} media items...! Starting ingestion...`);

            for (const audioFile of mediaFiles) {
                await Scanner.checkItem(audioFile);
            }

            const endTime = new Date();
            console.log(`Finished in ${endTime - startTime}ms.`);
        });
    }
}

Scanner.run();
