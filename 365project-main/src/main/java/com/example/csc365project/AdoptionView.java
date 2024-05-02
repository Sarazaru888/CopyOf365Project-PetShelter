package com.example.csc365project;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import java.sql.*;
import java.util.Iterator;

public class AdoptionView {
    private static AdoptionView view;

    public AdoptionView(){
        view = this;
    }

    private TableView<Adoption> table = new TableView<>();
    private ObservableList<Adoption> data = FXCollections.observableArrayList();
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://ambari-node5.csc.calpoly.edu:3306/365pets";
    private static final String USER = "365pets";
    private static final String PASS = "animals";

    public BorderPane createAdoptionManagementPane() {
        loadAdoptionData();

        BorderPane adoptionsPane = new BorderPane();

        // Adoption Table
        table.setEditable(true);
        TableColumn<Adoption, Integer> adoptionIdCol = new TableColumn<>("Adoption ID");
        adoptionIdCol.setCellValueFactory(new PropertyValueFactory<>("adoptionId"));
        
        TableColumn<Adoption, String> userNameCol = new TableColumn<>("User Name");
        userNameCol.setCellValueFactory(new PropertyValueFactory<>("userName"));

        TableColumn<Adoption, String> petNameCol = new TableColumn<>("Pet Name");
        petNameCol.setCellValueFactory(new PropertyValueFactory<>("petName"));

        TableColumn<Adoption, String> petTypeCol = new TableColumn<>("Pet Type");
        petTypeCol.setCellValueFactory(new PropertyValueFactory<>("petType"));

        TableColumn<Adoption, String> petAgeCol = new TableColumn<>("Pet Age");
        petAgeCol.setCellValueFactory(new PropertyValueFactory<>("petAge"));

        TableColumn<Adoption, String> petDescCol = new TableColumn<>("Pet Description");
        petDescCol.setCellValueFactory(new PropertyValueFactory<>("petDescription"));

        TableColumn<Adoption, String> adoptionDateCol = new TableColumn<>("Adoption Date");
        adoptionDateCol.setCellValueFactory(new PropertyValueFactory<>("adoptionDate"));


        // Add columns to table
        table.getColumns().addAll(userNameCol, petNameCol, petAgeCol, petTypeCol, petDescCol, adoptionDateCol);
        table.setItems(data);

        adoptionsPane.setCenter(table);

        return adoptionsPane;
    }

    private void loadAdoptionData() {
        data.clear();

        try {
            Class.forName(JDBC_DRIVER);
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = conn.createStatement();
            String sql;

            if(LoginView.user_id == 0){
                sql = "SELECT adoptions.adoption_id, users.username, pets.pet_id, pets.name, pets.type, pets.age, pets.description, adoptions.adoption_date " +
                        "FROM adoptions " +
                        "JOIN users ON adoptions.user_id = users.user_id " +
                        "JOIN pets ON adoptions.pet_id = pets.pet_id";
            } else {
                sql = "SELECT adoptions.adoption_id, users.username, pets.pet_id, pets.name, pets.type, pets.age, pets.description, adoptions.adoption_date " +
                        "FROM adoptions " +
                        "JOIN users ON adoptions.user_id = users.user_id " +
                        "JOIN pets ON adoptions.pet_id = pets.pet_id " +
                        "WHERE users.user_id = " + LoginView.user_id;
            }
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int adoptionId = rs.getInt("adoption_id");
                String userName = rs.getString("username");
                int pet_id = rs.getInt("pet_id");
                String petName = rs.getString("name");
                String petType = rs.getString("type");
                int petAge = rs.getInt("age");
                String petDescription = rs.getString("description");
                String adoptionDate = rs.getString("adoption_date");
                data.add(new Adoption(adoptionId, userName, pet_id, petName, petType, petAge, petDescription, adoptionDate));
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addUnadopt(){
        TableColumn<Adoption, String> unadoptCol = new TableColumn<>("Unadopt");
        unadoptCol.setCellValueFactory(new PropertyValueFactory<Adoption, String>("unadoptButton"));
        table.getColumns().add(unadoptCol);
    }

    public void removeUnadopt() {
        Iterator<TableColumn<Adoption, ?>> iterator = table.getColumns().iterator();
        while (iterator.hasNext()) {
            TableColumn<Adoption, ?> column = iterator.next();
            if (column.getText().equals("Unadopt")) {
                iterator.remove();
                break;
            }
        }
    }

    public void refreshTable() {
        loadAdoptionData();
    }

    public static AdoptionView getAdoptionView(){
        return view;
    }
}
