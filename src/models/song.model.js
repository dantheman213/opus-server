const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const songSchema = new Schema({
    title: String,
    artist: String,
    album: String,
    duration: Number,
    genre: String,
    yearReleased: Number,
    bitRate: Number,
    encoder: String,
    path: String,
    createdAt: Date,
    updatedAt: Date
});

const SongModel = mongoose.model('Song', songSchema, 'songs');
module.exports = SongModel;
