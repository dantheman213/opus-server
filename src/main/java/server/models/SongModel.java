package server.models;

import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.util.Date;

public class SongModel {
    public ObjectId _id;
    public String artist;
    public String album;
    public String composer;
    public String genre;
    public String title;
    public int year;
    public String bitrate;
    public String filePath;
    public Date updatedAt;
}
