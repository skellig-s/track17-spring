package track.msgtest.messenger.store;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import track.lessons.lesson9.QueryExecutor;
import track.msgtest.messenger.User;
import track.msgtest.messenger.net.MessengerServer;

import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by alex on 15.05.17.
 */
public class DBstore {

    private static Logger log = LoggerFactory.getLogger(MessengerServer.class);
    private Statement stmt = null;
    private ResultSet rs = null;
    private Connection connection;

    public static final String PATH_TO_DB = "/home/alex/IdeaProjects/track17-spring/track.sqlite";

    public DBstore() {
        try {
//            InitialContext initContext= new InitialContext();
//            DataSource ds = (DataSource) initContext.lookup("jdbc:sqlite:" + PATH_TO_DB);
//            Connection connection = ds.getConnection();

            connection = DriverManager.getConnection("jdbc:sqlite:" + PATH_TO_DB);
            stmt = connection.createStatement();

        } catch (Exception ex) {
            log.error("can't connect to DB: " + ex);
        }
    }

    public void printAll() {
        final String sql = "SELECT * FROM users_server;";

        try {
//            stmt = connection.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                // Column index starts with 1
                Integer id = rs.getInt(1);          // 1 - ID
                String name = rs.getString("name"); // 2 - name
                String pass = rs.getString("pass");

                System.out.println(String.format("ID: %d, name: %s, pass: %s", id, name, pass));

            }
//            connection.close();
        } catch (SQLException ex) {
            log.error("cant"+ ex);
        }
    }

    public void addUser(User user) {
        String name = user.getName();
        String pass = user.getPass();
        final String addUserComm = "INSERT INTO users_server(name, pass) VALUES (?, ?)";
        try {
//            stmt = connection.createStatement();
            PreparedStatement ps = connection.prepareStatement(addUserComm);
            ps.setString(1, name);
            ps.setString(2, pass);
            ps.executeUpdate();
//            connection.close();
        } catch (SQLException ex) {
            log.error("cant"+ ex);
        }

    }

    public User getUser(String login, String pass) {
        User user = null;
//        QueryExecutor exec = new QueryExecutor();
//        List<User> users = exec.execQuery(connection, "SELECT * FROM users;", )
        final String sql = "SELECT * FROM users_server WHERE name= ? AND pass= ?;";
        try {
//            stmt = connection.createStatement();
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, login);
            ps.setString(2, pass);
            rs = ps.executeQuery();
            while (rs.next()) {
                Integer id = rs.getInt(1);
                String name = rs.getString("name");
                String passss = rs.getString("pass");
                user = new User(name, passss);
                user.setId(id);
                System.out.println(String.format("needed user id: %d name: %s, pass: %s", user.getId(), user.getName(), user.getPass()));

            }
//            connection.close();
        } catch (SQLException ex) {
            log.error("cant"+ ex);
        }
        return user;
    }

    public List<Long> getUsersIdByChatId(long chatId) {
        List<Long> usersList = new LinkedList<>();
        final String sql = "SELECT * FROM chat_membership WHERE chat_id=?;";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setLong(1, chatId);
            rs = ps.executeQuery();
            while (rs.next()) {
                Long userId = rs.getLong("user_id");
                usersList.add(userId);
            }
//            connection.close();
        } catch (SQLException ex) {
            log.error("cant"+ ex);
        }
        return usersList;
    }

}
