package server.models.database;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.bson.types.ObjectId;

import java.util.Date;

public class SongModel {
    @JsonSerialize(using = ToStringSerializer.class)
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
