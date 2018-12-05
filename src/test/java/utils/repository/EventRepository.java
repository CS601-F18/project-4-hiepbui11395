package utils.repository;

import cs601.project4.jdbc.ConnectionUtil;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EventRepository {
    private static EventRepository instance;

    private EventRepository(){}

    public static synchronized EventRepository getInstance(){
        if(instance==null){
            instance = new EventRepository();
        }
        return instance;
    }


    private final String SQL_DELETE = "delete from `event`";
    private final String SQL_COUNT = "select count(*) from `event`";

    public void deleteAll(){
        BasicDataSource dataSource = ConnectionUtil.getMyConnection();
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_DELETE))
        {
            statement.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public int count(){
        BasicDataSource dataSource = ConnectionUtil.getMyConnection();
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_COUNT);
            ResultSet rs = statement.executeQuery())
        {
            if(rs.next()){
                return rs.getInt(1);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return 0;
    }
}
