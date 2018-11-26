package cs601.project4.repository;

import com.google.gson.JsonObject;
import cs601.project4.entity.Event;
import cs601.project4.jdbc.ConnectionUtil;
import cs601.project4.utils.Config;
import cs601.project4.utils.HttpUtils;
import org.eclipse.jetty.http.HttpStatus;

import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EventRepository {
    private static EventRepository instance;
    private final String USER_SERVICE_URL = Config.getInstance().getProperty("userUrl");

    public static synchronized EventRepository getInstace() {
        if(instance == null){
            instance = new EventRepository();
        }
        return instance;
    }
    private final String SQL_INSERT = "insert into `event` (`userId`,`name`,`numTickets`,`numTicketsAvail`) " +
            "values (?,?,?,?)";

    private final String SQL_UPDATE = "update `event` set `userId`=?,`name`=?,`numTickets`=?,`numTicketsAvail`=? " +
            "where `id`=?";

    public Event create(Event entity, Connection connection) throws SQLException{
        PreparedStatement statement = connection.prepareStatement(SQL_INSERT,
                PreparedStatement.RETURN_GENERATED_KEYS);
        statement.setLong(1, entity.getUserId());
        statement.setString(2, entity.getName());
        statement.setInt(3, entity.getNumTickets());
        statement.setInt(4, entity.getNumTicketsAvail());

        int affectedRow = statement.executeUpdate();
        if(affectedRow == 0){
            return null;
        }
        ResultSet rs = statement.getGeneratedKeys();
        if(rs.next()){
            entity.setId(rs.getLong(1));
            return entity;
        } else{
            return null;
        }
    }

    public boolean buyTickets(Event entity, long userId, int numTickets, Connection connection) throws SQLException{
        PreparedStatement statement = connection.prepareStatement(SQL_UPDATE);
        statement.setLong(1, entity.getUserId());
        statement.setString(2, entity.getName());
        statement.setInt(3, entity.getNumTickets());
        statement.setInt(4, entity.getNumTicketsAvail() - numTickets);
        statement.setLong(5, entity.getId());

        int affectedRow = statement.executeUpdate();
        if(affectedRow != 0){
            String path = "/" + userId + "/tickets/add";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("eventid", entity.getId());
            jsonObject.addProperty("tickets", numTickets);
            Response response = HttpUtils.callPostRequest(USER_SERVICE_URL, path, jsonObject.toString());
            if(response.getStatus()== HttpStatus.OK_200){
                return true;
            }
        }
        return false;
    }

    public Event findById(long id, Connection connection) throws SQLException {
            PreparedStatement statement = connection.prepareStatement("select * from `event` where `id` = ?");
            statement.setLong(1,id);
            ResultSet rs = statement.executeQuery();
            if(rs.next()){
                Event event = new Event(rs);
                return event;
            } else{
                return null;
            }
    }
}
