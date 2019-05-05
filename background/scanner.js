const mm = require('music-metadata');
const recursive = require('recursive-readdir');
const Database = require('../src/lib/database');
let songs;

class Scanner {
    static async checkItem(mediaPath) {
        const result = await songs.findOne({ path: mediaPath });
        if (result) {
            console.log(`Skipping existing item ${mediaPath}`);
            result.updatedAt = new Date();
            await result.save();
        } else {
            console.log(`Adding ${mediaPath} to database...`);
            const metadata = await mm.parseFile(mediaPath);
            const song = new songs({
                title: metadata.common.title || 'Untitled Song',
                artist: metadata.common.albumartist || '',
                album: metadata.common.album || '',
                duration: metadata.format.duration || -1,
                genre: metadata.common.genre[0] || '',
                yearReleased: metadata.common.year || -1,
                bitRate: metadata.format.bitrate || -1,
                encoder: metadata.format.encoder || '',
                path: mediaPath,
                createdAt: new Date(),
                updatedAt: new Date()
            });

            try {
                await song.save();
                console.log(`${song.title} saved into database successfully.`);
            } catch (ex) {
                console.log(ex);
            }
        }
    }

    static async run() {
        const startTime = new Date();
        await Database.connect();
        songs = Database.db.collection('songs');

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
