package com.sunrise.dental;

import sunrise.dental.dao.UserDAO;
import sunrise.dental.model.User;

public class LoginTest {

    public static void main(String[] args) {

        UserDAO userDAO = new UserDAO();

        String email = "admin@sunrise.com";
        String password = "admin123";

        User user = userDAO.login(email, password);

        if (user != null) {

            System.out.println("Login Successful!");
            System.out.println(
                    "Welcome: "
                    + user.getFirstName()
                    + " "
                    + user.getLastName()
            );

            System.out.println(
                    "Role: "
                    + user.getRole()
            );

        } else {

            System.out.println(
                    "Invalid Email or Password!"
            );
        }
    }
}