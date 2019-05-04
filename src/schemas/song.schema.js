const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const songSchema = new Schema({
    title: String,
    artist: String,
    album: String,
    path: String,
    createdAt: Date,
    updatedAt: Date
});

module.exports = songSchema;
