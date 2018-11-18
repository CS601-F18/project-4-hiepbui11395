package cs601.project4.repository;

import cs601.project4.entity.User;
import cs601.project4.jdbc.ConnectionUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepository {
    private final String SQL_INSERT = "insert into `user` (username,password,email,phoneNumber)" +
            "values (?,?,?,?)";

    public long save(User entity) throws SQLException{
        try(
                Connection connection = ConnectionUtils.getConnection();
                PreparedStatement statement = connection.prepareStatement(SQL_INSERT,
                        PreparedStatement.RETURN_GENERATED_KEYS);
                ){
            statement.setString(1, entity.getUsername());
            statement.setString(2, entity.getPassword());
            statement.setString(3, entity.getEmail());
            statement.setString(4, entity.getPhoneNumber());
            int afftectedRow = statement.executeUpdate();
            if(afftectedRow == 0){
                throw new SQLException("Creating user failed - no row affected");
            }
            ResultSet rs = statement.getGeneratedKeys();
            if(rs.next()){
                entity.setId(rs.getLong(1));
            } else{
                throw new SQLException("Creating user failed - no Id obtained");
            }
        }
        return entity.getId();
    }
}
