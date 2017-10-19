package server.controllers;
import server.database.DBConnection;
import server.models.*;
import server.endpoints.UserEndpoint;
import server.utility.Digester;

import server.database.DBConnection;
import server.models.*;
import server.endpoints.UserEndpoint;
import server.utility.Globals;

import java.util.ArrayList;

public class UserController {
    private User currentUser;
    private Digester digester;
    private MainController mainController;
    private DBConnection dbConnection;

    public UserController (DBConnection dbConnection){
        this.currentUser = currentUser;
        this.dbConnection = dbConnection;
        this.digester = new Digester();
    }

    public UserController ( ) {

    }



   // public void logOut() {
   //     mainController.logout();
   // }

     // sets the user to the coherent value of the current user
    public void setCurrentUser(User user){
        this.currentUser = user;
    }

    /**
     *
     * @param user
     * hashes the password+salt of the user and sends it back to DBConnection
     */
    public boolean addUser(User user){


        //sets a users password to a hashed with salt password and returns a boolean value if a user has been created

        String hashedPassword = digester.hashWithSalt(user.getPassword());

        user.setPassword(hashedPassword);
        boolean result = dbConnection.addUser(user);

        //Conditional logging for creation of user
        if(result) {
            Globals.log.writeLog(getClass().getName(), this, "Creation of user" + user.getUsername() + " successful", 0);

        } else {
            Globals.log.writeLog(getClass().getName(), this, "Creation of user" + user.getUsername() + " failed", 2);
        }

        return result;
    }

    // Adds an item to the order list
    public boolean addOrder(int id, ArrayList<Item> items){
        boolean result = dbConnection.addOrder(id, items);

        //Conditional logging for adding an order
        if(result) {
            Globals.log.writeLog(getClass().getName(), this, "Added order id: " + id + " to " + currentUser.getUsername() + "'s orders successful", 0);
        } else {
            Globals.log.writeLog(getClass().getName(), this, "Adding order id: " + id + " to" + currentUser.getUsername() + "'s orders failed", 2);
        }
        return result;
    }

    public ArrayList<Order> findOrderById(int userId) {
        ArrayList<Order> orders = dbConnection.findOrderById(userId);
        return orders;
    }

    public ArrayList<Item> getItems(){
        ArrayList<Item> items = dbConnection.getItems();
        return items;
    }

    // Authorizes a user, by hashing the input password first, and comparing it to the stored hashed password
    public User authorizeUser(User user){
        String hashedPassword = digester.hashWithSalt(user.getPassword());
        user.setPassword(hashedPassword);
        User userCheck = dbConnection.authorizeUser(user);

        //Conditional logging for user authorization
        if (userCheck != null) {

            Globals.log.writeLog(getClass().getName(), this, "Log-in attempt by" + userCheck.getUsername() + " successful", 0);

        } else {

            Globals.log.writeLog(getClass().getName(), this, "Log-in attempt by" + userCheck.getUsername() + " failed", 0);
        }
        return userCheck;
    }



}
