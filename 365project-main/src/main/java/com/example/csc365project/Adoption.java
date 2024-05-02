package com.example.csc365project;

import java.io.Console;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

public class Adoption {
    private final int adoptionId;
    private final String userName;
    private final int pet_id;
    private final String petName;
    private final String petType;
    private final int petAge;
    private final String petDescription;
    private final String adoptionDate;
    private Button unadoptButton;
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://ambari-node5.csc.calpoly.edu:3306/365pets";
    private static final String USER = "365pets";
    private static final String PASS = "animals";

    public Adoption(int adoptionId, String userName, int pet_id, String petName, String petType, int petAge, String petDescription, String adoptionDate) {
        this.adoptionId = adoptionId;
        this.userName = userName;
        this.pet_id = pet_id;
        this.petName = petName;
        this.petType = petType;
        this.petAge = petAge;
        this.petDescription = petDescription;
        this.adoptionDate = adoptionDate;

        this.unadoptButton = new Button("Unadopt Pet");
        this.unadoptButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(LoginView.user_id == 0){
                    System.out.println("You are not logged in. Please login to undo a pet adoption.");
                } else {
                    unadoptPet(getAdoptionId(), getPet_id());
                }
            }
        });
    }

    // Getters
    public int getAdoptionId() { return adoptionId; }
    public String getUserName() { return userName; }
    public int getPet_id() {return pet_id;}
    public String getPetName() { return petName; }
    public String getAdoptionDate() { return adoptionDate; }
    public String getPetType() {return petType;}
    public String getPetDescription() {return petDescription;}
    public int getPetAge() {return petAge;}

    public Button getUnadoptButton() {return unadoptButton;}
    public void setUnadoptButton(Button button) {this.unadoptButton = button;}

    public void unadoptPet(int adoptionId, int pet_id) {
        try {
            System.out.println("Undoing adoption of pet with pet_id: " + adoptionId);

            Class.forName(JDBC_DRIVER);
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            String sql = "DELETE FROM adoptions WHERE adoption_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, adoptionId);

            pstmt.executeUpdate();

            try {
                String updateStatusSQL = "UPDATE pets SET status = 'available' WHERE pet_id = ?";
                pstmt = conn.prepareStatement(updateStatusSQL);
                pstmt.setInt(1, pet_id);

                pstmt.executeUpdate();

                System.out.println("User " + LoginView.user_id + " undid adoption " + getPetName() + " (ID: " + getPet_id() + ")");
            } catch (Exception e2) {
                e2.printStackTrace();
            }

            System.out.println("Updating tables!");
            PetView view = PetView.getPetView();
            view.refreshTable();
            AdoptionView adoptionView = AdoptionView.getAdoptionView();
            adoptionView.refreshTable();

            pstmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}