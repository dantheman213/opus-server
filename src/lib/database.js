const mongoose = require('mongoose');

module.exports = class Database {
    static async connect() {
        const result = await mongoose.createConnection('mongodb://mongo:27017/opus_db', {
            poolSize: 5,
            useNewUrlParser: true
        });

        if (!result) {
            console.log('ERROR: Connecting to database FAILED!');
            console.log(err);
            process.exit(1);
        } else {
            console.log('Connected to database successfully...!');
        }
    }
};
