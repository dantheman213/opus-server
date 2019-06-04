package server.models.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.bson.types.ObjectId;

public class UserDomainModel {
    @JsonSerialize(using = ToStringSerializer.class)
    public ObjectId _id;
    public String name;
    public String username;
    public String passwordHash;
    public String passwordSalt;
    public boolean isAdmin;
}
