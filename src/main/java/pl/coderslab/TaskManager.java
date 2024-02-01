package pl.coderslab;

import pl.coderslab.entity.User;
import pl.coderslab.entity.UserDao;

import java.sql.SQLException;
import java.util.Scanner;

public class TaskManager {


    public static void main(String[] args) throws SQLException {
        Scanner scan = new Scanner(System.in);
        User user = new User().createUser(); // tworzenie użytkownika
        UserDao new1 = new UserDao();


    }
}