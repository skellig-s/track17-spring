package track.msgtest.messenger.store;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import track.msgtest.messenger.User;
import track.msgtest.messenger.net.MessengerServer;

import java.sql.*;

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
            connection = DriverManager.getConnection("jdbc:sqlite:" + PATH_TO_DB);
            stmt = connection.createStatement();

        } catch (SQLException ex) {
            log.error("can't connect to DB: " + ex);
        }
    }

    public void printAll() {
        final String sql = "SELECT * FROM users_server;";

        try {
//            stmt = connection.createStatement();
            // 4) Набор "строк" таблицы - результат SELECT
            rs = stmt.executeQuery(sql);

            // 5) Структура ResultSet - получаем строки, пока есть
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

}
