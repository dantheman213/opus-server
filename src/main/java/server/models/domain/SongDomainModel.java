package server.models.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import org.bson.types.ObjectId;

import java.util.Date;

public class SongDomainModel {
    @JsonSerialize(using = ToStringSerializer.class)
    public ObjectId _id;
    public String artist;
    public String album;
    public String trackNumber;
    public String composer;
    public String genre;
    public String title;
    public float duration;
    public String releaseDate;
    public String bitrate;
    public String filePath;
    public Date updatedAt;
}
