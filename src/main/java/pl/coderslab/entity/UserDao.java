package pl.coderslab.entity;

import org.mindrot.jbcrypt.BCrypt;
import pl.coderslab.DBUtil;

import java.sql.*;
import java.util.Arrays;
import java.util.Scanner;

public class UserDao {
    private static final String CREATE_USER_QUERY =
            "INSERT INTO users(username, email, password) VALUES (?, ?, ?)";

    public static String getCreateUserQuery() {
        return CREATE_USER_QUERY;
    }
    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
    public User create (User user) throws SQLException {
        if (isEmailExist(user.getEmail())) {
            System.out.println("EMAIL already is in DataBase");
            return null;
        } else {
            try (Connection con = DBUtil.getConnection()) {
                PreparedStatement statement = con.prepareStatement(CREATE_USER_QUERY, Statement.RETURN_GENERATED_KEYS);
                statement.setString(1,user.getUserName());
                statement.setString(2,user.getEmail());
                statement.setString(3,user.getPassword());
                statement.executeUpdate();
                ResultSet rs = statement.getGeneratedKeys(); // pobieramy ID z bazy danych które automatycznie zostało pobrane
                while (rs.next()) {
                    user.setId(rs.getInt(1)); //uzupełniamy pobrane ID z DB do usera
                }
                con.close();
                return user;
            } catch (SQLException a){
                a.printStackTrace();
                return null;
            }
        }
    }
    public boolean isEmailExist (String email) throws SQLException {
        boolean checker = false;
        Connection con = DBUtil.getConnection();
        String QUERY = "select case  when exists(select users.email from users where email=?) then 'true' else 'false' end";
        PreparedStatement statement = con.prepareStatement(QUERY);
        statement.setString(1,email);

        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            checker = rs.getBoolean(1);
        }
        con.close();
        return checker;
    }
    public boolean isIDExist (int id) throws SQLException {
        boolean checker = false;
        Connection con = DBUtil.getConnection();
        String QUERY = "select case  when exists(select users.id from users where id=?) then 'true' else 'false' end";
        PreparedStatement statement = con.prepareStatement(QUERY);
        statement.setInt(1,id);

        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            checker = rs.getBoolean(1);
        }
        con.close();
        return checker;
    }
    public User readById (int userId) throws SQLException {
        Connection con = DBUtil.getConnection();
        PreparedStatement statement = con.prepareStatement("select * from users where id=?");
        statement.setInt(1,userId);
        ResultSet rs = statement.executeQuery();
        User tmpUser = new User();
        while (rs.next()) {
            tmpUser = new User(rs.getInt("id"),rs.getString("username"),rs.getString("email"),rs.getString("password"));
        }
        con.close();
        return tmpUser;
    }
    public void showMeUser (int userId) throws SQLException {
        User tempUser = readById(userId);
        System.out.println("User Id: "+ tempUser.getId() +
                "\nUsername: " + tempUser.getUserName()+
                "\nEmail: " + tempUser.getEmail() +
                "\nPassword: " + tempUser.getPassword());
    }
    public void update (User user) throws SQLException {
        if (!isIDExist(user.getId())) {
            System.out.println("ID does not exist");
        } else {
            showMeUser(user.getId());
            String QUERY = "UPDATE users SET username=?,email=?, password=? WHERE id=?";
            Scanner scan = new Scanner(System.in);
            System.out.println("New username: ");
            user.setUserName(scan.nextLine());
            System.out.println("New email: ");
            user.setEmail(scan.nextLine());
            System.out.println("New Password: ");
            user.setPassword(UserDao.hashPassword(scan.nextLine()));

            Connection con = DBUtil.getConnection();
            PreparedStatement statement = con.prepareStatement(QUERY);
            statement.setString(1,user.getUserName());
            statement.setString(2,user.getEmail());
            statement.setString(3,user.getPassword());
            statement.setInt(4,user.getId());
            statement.executeUpdate();
            con.close();
        }
    }
    public void delete (int userId) throws SQLException {
        if (!isIDExist(userId)) {
            System.out.println("ID does not exist");
        } else {
            String QUERY = "delete from users where id=?";
            Connection con = DBUtil.getConnection();
            PreparedStatement statement = con.prepareStatement(QUERY);
            statement.setInt(1,userId);
            statement.executeUpdate();
            con.close();
        }
    }
    public void findAll () throws SQLException {
        User [] users = new User[0];
        String QUERY = "select * from users";
        Connection con = DBUtil.getConnection();
        PreparedStatement statement = con.prepareStatement(QUERY);
        ResultSet rs = statement.executeQuery();
        User tmpUser = new User();
        while (rs.next()) {
            users = Arrays.copyOf(users,users.length+1); // rozszerzanie tablicy
            tmpUser = new User(rs.getInt("id"),rs.getString("username"),rs.getString("email"),rs.getString("password"));
            // zapisanie kolejnego usera z DB
            users [users.length-1] = tmpUser; // dodanie nowego usera na ostatniej pozycji w tablicy
        }
        for (User a: users) {
            System.out.println("ID: "+ a.getId() + " username: "+a.getUserName()+" email: "+a.getEmail()+" password: "+ a.getPassword());
        }
    }
}
