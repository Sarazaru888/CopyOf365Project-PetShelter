package com.example.csc365project;

import java.io.Console;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.util.Random;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;

public class Pet {
    private Button viewButton;
    private int pet_id;
    private String name;
    private String type;
    private int age;
    private String description;
    private String shelter;
    private Button deleteButton;
    private Button adoptButton;
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://ambari-node5.csc.calpoly.edu:3306/365pets";
    private static final String USER = "365pets";
    private static final String PASS = "animals";

    // Constructor
    public Pet(String name, String type, int age, String description, String shelter) {
        this.name = name;
        this.type = type;
        this.age = age;
        this.description = description;
        this.shelter = shelter;

        this.deleteButton = new Button("Delete Pet");
        this.deleteButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.out.println("Deleting Pet: ID = " + getPet_id() + " | Name = " + getName() + ".");
                deletePet(getPet_id());
            }
        });

        this.adoptButton = new Button("Adopt Pet");
        this.adoptButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(LoginView.user_id == 0){
                    System.out.println("You are not logged in. Please login to adopt a pet.");
                } else {
                    adoptPet(getPet_id());
                }
            }
        });

        this.viewButton = new Button("View Pet");
        this.viewButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                viewPet();
            }
        });
    }

    // Getters and setters
    public Button getViewButton() {return viewButton;}
    public void setViewButton(Button button) {this.viewButton = button;}

    public int getPet_id() { return pet_id; }
    public void setPet_id(int pet_id) { this.pet_id = pet_id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getShelter() { return shelter; }
    public void setShelter(String shelter) { this.shelter = shelter; }

    public Button getDeleteButton() {return deleteButton;}
    public void setDeleteButton(Button button) {this.deleteButton = button;}

    public Button getAdoptButton() {return adoptButton;}
    public void setAdoptButton(Button button) {this.adoptButton = button;}

    // Fetch all pets from the database
    public static List<Pet> getPets() {
        List<Pet> pets = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();

            // SQL query with JOIN to get the shelter name
            String sql = "SELECT pets.*, shelters.shelter_name FROM (pets " +
                        "LEFT JOIN shelters ON pets.shelter_id = shelters.shelter_id)" +
                        "WHERE status = 'available'"+
                        " ORDER BY pets.type ASC";
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                String name = rs.getString("name");
                String type = rs.getString("type");
                int age = rs.getInt("age");
                String description = rs.getString("description");
                String shelter = rs.getString("shelter_name"); // Get the shelter name from the JOIN
                Pet newPet = new Pet(name, type, age, description, shelter);
                newPet.setPet_id(rs.getInt("pet_id"));
                pets.add(newPet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Clean up resources
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return pets;
    }
    public static List<Pet> getPets(String nameFilter, String typeFilter, String ageFilter, String descriptionFilter, String shelterFilter){
        List<Pet> pets = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            if(!nameFilter.isEmpty()){
                nameFilter = " AND name LIKE '%"+nameFilter+"%'";
            }
            if(!typeFilter.isEmpty()){
                typeFilter = " AND type LIKE '%"+typeFilter+"%'";
            }
            if(!ageFilter.isEmpty()){
                ageFilter = " AND type ='"+ageFilter+"%'";
            }
            if(!descriptionFilter.isEmpty()){
                descriptionFilter = " AND description LIKE '%"+descriptionFilter+"%'";
            }
            if(!shelterFilter.isEmpty()){
                shelterFilter = " AND description LIKE '%"+shelterFilter+"%'";
            }
            // SQL query with JOIN to get the shelter name
            String sql = "SELECT pets.*, shelters.shelter_name FROM (pets " +
                    "LEFT JOIN shelters ON pets.shelter_id = shelters.shelter_id)" +
                    " WHERE status = 'available'"+nameFilter+typeFilter+ageFilter+
                    descriptionFilter+shelterFilter+
                    " ORDER BY pets.type ASC";
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                String name = rs.getString("name");
                String type = rs.getString("type");
                int age = rs.getInt("age");
                String description = rs.getString("description");
                String shelter = rs.getString("shelter_name"); // Get the shelter name from the JOIN
                Pet newPet = new Pet(name, type, age, description, shelter);
                newPet.setPet_id(rs.getInt("pet_id"));
                pets.add(newPet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Clean up resources
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return pets;
    }
    // Insert a new pet into the database
    public void insertPet() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            System.out.println("asdf");

            // First, get the shelter ID based on the shelter name
            int shelterId = -1;
            String getShelterIdSql = "SELECT shelter_id FROM shelters WHERE shelter_name = ?";
            PreparedStatement getShelterIdStmt = conn.prepareStatement(getShelterIdSql);
            getShelterIdStmt.setString(1, this.shelter);
            rs = getShelterIdStmt.executeQuery();
            if (rs.next()) {
                shelterId = rs.getInt("shelter_id");
            }
            getShelterIdStmt.close();

            // Check if a valid shelter ID was found
            if (shelterId == -1) {
                System.out.println("Shelter not found.");
                return;
            }

            // Insert the new pet
            String sql = "INSERT INTO pets (name, type, age, description, shelter_id) VALUES (?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, this.name);
            pstmt.setString(2, this.type);
            pstmt.setInt(3, this.age);
            pstmt.setString(4, this.description);
            pstmt.setInt(5, shelterId);

            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Clean up resources
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    // // Delete a pet from the database
    public static void deletePet(int pet_id) {
        try {
            System.out.println("Deleting pet with pet_id: " + pet_id);

            Class.forName(JDBC_DRIVER);
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            String sql = "DELETE FROM pets WHERE pet_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, pet_id);

            pstmt.executeUpdate();

            PetView view = PetView.getPetView();
            view.refreshTable();

            pstmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Insert a new pet into the database
    public void adoptPet(int pet_id) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            // Insert the new pet
            String sql = "INSERT INTO adoptions (user_id, pet_id, adoption_date) VALUES (?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, LoginView.user_id);
            pstmt.setInt(2, pet_id);
            pstmt.setDate(3, Date.valueOf(LocalDate.now()));

            pstmt.executeUpdate();

            try {
                String updateStatusSQL = "UPDATE pets SET status = 'adopted' WHERE pet_id = ?";
                pstmt = conn.prepareStatement(updateStatusSQL);
                pstmt.setInt(1, pet_id);

                pstmt.executeUpdate();

                System.out.println("User " + LoginView.user_id + " adopted " + getName() + " (ID: " + getPet_id() + ")");
            } catch (Exception e2) {
                e2.printStackTrace();
            }

            System.out.println("Updating tables!");
            PetView view = PetView.getPetView();
            view.refreshTable();
            AdoptionView adoptionView = AdoptionView.getAdoptionView();
            adoptionView.refreshTable();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Clean up resources
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void viewPet() {
        Stage viewStage = new Stage();
        viewStage.setTitle("View Pet");
        TilePane tilePane = new TilePane();

        System.out.println(getType());

        Random random = new Random();
        int imageNum = random.nextInt(3) + 1;
        String imagePath = new File("src/main/resources/assets/" + getType() + imageNum + ".jpg").getAbsolutePath();
        Image image = new Image("file:" + imagePath);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(200);
        imageView.setFitHeight(120);

        Label label = new Label(getName() + " (Age " + getAge() + ")");
        tilePane.getChildren().addAll(imageView, label);

        Scene scene = new Scene(tilePane, 200, 200);
        viewStage.setScene(scene);

        viewStage.setX(viewButton.getScene().getWindow().getX() + 50);
        viewStage.setY(viewButton.getScene().getWindow().getY() + 50);
        viewStage.show();
    }
}
