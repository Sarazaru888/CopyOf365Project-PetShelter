package com.example.csc365project;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


public class LoginView  {
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://ambari-node5.csc.calpoly.edu:3306/365pets";
    private static final String USER = "365pets";
    private static final String PASS = "animals";

    public static Text loginTitle;

    public static int user_id = 0;
    public static String role = "guest";
    public static void main(String[] args) {}

    public GridPane createLoginPane() {

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text sceneTitle = new Text("Welcome! Sign in here!");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(sceneTitle, 0,0,2,1);
        loginTitle = sceneTitle;

        Label username = new Label("Username: ");
        grid.add(username, 0,1);
        TextField usernameInput = new TextField();
        grid.add(usernameInput, 1, 1);

        Label password = new Label("Password: ");
        grid.add(password, 0, 2);
        PasswordField pwField = new PasswordField();
        grid.add(pwField, 1, 2);

        Button submit = new Button("Sign in");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(submit);
        grid.add(hbBtn, 1, 4);

        Button signout = new Button("Sign Out");
        HBox signoutBtn = new HBox(10);
        signoutBtn.setAlignment(Pos.BOTTOM_RIGHT);
        signoutBtn.getChildren().add(signout);
        grid.add(signoutBtn, 0, 4);

        final Text actionTarget = new Text();
        grid.add(actionTarget, 1, 6);

        submit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                actionTarget.setFill(Color.FIREBRICK);
                if(!(role.equals("guest"))){
                    actionTarget.setText("Please log out first.");
                } else {
                    String name = usernameInput.getText();
                    String pass = pwField.getText();
                    int id = Login(name, pass);
                    if(id > 0){
                        actionTarget.setText("Sign in button pressed. Welcome back, " + name);
                        loginTitle.setText("Welcome back, " + name + "!");
                        user_id = id;
                        System.out.println("Logged in as user " + user_id + " (Role: " + role + ")");
                        AdoptionView adoptionView = AdoptionView.getAdoptionView();
                        adoptionView.refreshTable();
                        adoptionView.addUnadopt();
                        PetView.getPetView().addAdopt();
                        if(role.equals("admin")){
                            PetView.getPetView().addDelete();
                        }
                    }
                    else {
                        actionTarget.setText("Incorrect username or password.");
                    }
                }
                usernameInput.clear();
                pwField.clear();
            }
        });

        signout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                actionTarget.setFill(Color.FIREBRICK);
                if(role.equals("guest")){
                    actionTarget.setText("Please sign in first.");
                }
                else {
                    actionTarget.setText("Successfully Signed Out.");
                    loginTitle.setText("Welcome! Sign in here!");
                    if(role.equals("admin")){
                        PetView.getPetView().removeColumn("Delete");
                        System.out.println("Removed delete column");
                    }
                    PetView.getPetView().removeColumn("Adopt");
                    System.out.println("Removed adopt column");
                    AdoptionView.getAdoptionView().removeUnadopt();
                    System.out.println("Removed unadopt column");
                    user_id = 0;
                    role = "guest";
                    AdoptionView adoptionView = AdoptionView.getAdoptionView();
                    adoptionView.refreshTable();
                }
            }
        });

        return grid;
    }

    public int Login(String user, String pass){
        int id = 0;
        try {
            Class.forName(JDBC_DRIVER);
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = conn.createStatement();
            String sql = "SELECT user_id, role FROM users " +
                         "WHERE '"+user+"' = username " +
                         "AND '"+pass+"' = password;";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                id = rs.getInt("user_id");
                role = rs.getString("role");

            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }
}
