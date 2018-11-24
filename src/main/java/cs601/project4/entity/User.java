package cs601.project4.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

public class User {
    private Long id;
    private String username;

    public User() {
    }

    public User(String username) {
        this.username = username;
    }

    public User(ResultSet rs) throws SQLException {
        this.id = rs.getLong("id");
        this.username = rs.getString("username");
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
