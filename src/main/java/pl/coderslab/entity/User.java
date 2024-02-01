package pl.coderslab.entity;

import org.mindrot.jbcrypt.BCrypt;

import java.util.Scanner;

public class User {
    private int id;
    private String userName;
    private String email;
    private String password;

    protected User(int id, String userName, String email, String password) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.password = password;
    }
    public User(String userName, String email, String password) {
        this.userName = userName;
        this.email=email;
        this.password=password;
    }
    public User () {
    }
    public User createUser () {
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter username: ");
        String userName = scan.nextLine();
        System.out.println("Enter mail: ");
        String userEmail = scan.nextLine();
        System.out.println("Enter your password: ");
        String userPassword = UserDao.hashPassword(scan.nextLine());
        return new User(userName,userEmail,userPassword);
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }


}
