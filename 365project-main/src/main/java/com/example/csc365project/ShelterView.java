package com.example.csc365project;

import java.sql.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;

public class ShelterView {

    private TableView<Shelter> table = new TableView<>();
    public static ObservableList<Shelter> data = FXCollections.observableArrayList();
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://ambari-node5.csc.calpoly.edu:3306/365pets";
    private static final String USER = "365pets";
    private static final String PASS = "animals";

    public BorderPane createShelterViewPane() {
        loadShelterData();
        
        BorderPane shelterPane = new BorderPane();

        // Shelter Table
        table.setEditable(true);
        
        TableColumn<Shelter, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Shelter, String> addressCol = new TableColumn<>("Address");
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));

        TableColumn<Shelter, String> contactInfoCol = new TableColumn<>("Contact Info");
        contactInfoCol.setCellValueFactory(new PropertyValueFactory<>("contactInfo"));

        TableColumn<Shelter, String> capacityCol = new TableColumn<>("Capacity");
        capacityCol.setCellValueFactory(new PropertyValueFactory<>("capacity"));

        table.getColumns().addAll(nameCol, addressCol, contactInfoCol, capacityCol);
        table.setItems(data);

        shelterPane.setCenter(table);

        return shelterPane;
    }

    private void loadShelterData() {
        // Clear any existing data
        data.clear();

        try {
            Class.forName(JDBC_DRIVER);
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM shelters";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int id = rs.getInt("shelter_id");
                String name = rs.getString("shelter_name");
                String address = rs.getString("shelter_address");
                String contactInfo = rs.getString("contact_info");
                int capacity = rs.getInt("capacity");

                data.add(new Shelter(id, name, address, contactInfo, capacity));
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
