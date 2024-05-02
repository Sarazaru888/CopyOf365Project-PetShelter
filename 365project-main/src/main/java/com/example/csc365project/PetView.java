package com.example.csc365project;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Iterator;

public class PetView  {
    private static PetView view;
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://ambari-node5.csc.calpoly.edu:3306/365pets";
    private static final String USER = "365pets";
    private static final String PASS = "animals";
    public PetView(){
        view = this;
    }

    public static void main(String[] args) {}

    private TableView<Pet> table = new TableView<>();
    private ObservableList<Pet> data = FXCollections.observableArrayList();

    public BorderPane createPetManagementPane() {

        BorderPane petsPane = new BorderPane();

        //filter button
        Button filter = new Button("Filter");
        filter.setPrefWidth(79);
        //name filter
        TextField nameInput = new TextField();
        nameInput.setPromptText("Name");
        nameInput.setPrefWidth(62);
        //type filter
        TextField typeInput = new TextField();
        typeInput.setPromptText("Type");
        typeInput.setPrefWidth(45);
        //age filter
        TextField ageInput = new TextField();
        ageInput.setPromptText("Age");
        ageInput.setPrefWidth(41);
        //description filter
        TextField descriptionInput = new TextField();
        descriptionInput.setPromptText("Description");
        descriptionInput.setPrefWidth(81);
        //shelter filter
        TextField shelterInput = new TextField();
        shelterInput.setPromptText("Shelter");
        shelterInput.setPrefWidth(133);

        filter.setOnAction(e -> {
            // Logic to add/edit pet information
            refreshTable(nameInput.getText(),typeInput.getText(),ageInput.getText(),
                    descriptionInput.getText(), shelterInput.getText());
            nameInput.clear();
            typeInput.clear();
            ageInput.clear();
            descriptionInput.clear();
            shelterInput.clear();
        });
        HBox hbox = new HBox(filter,nameInput,typeInput,ageInput,descriptionInput,shelterInput);
        petsPane.setTop(hbox);


        // Pet Table
        table.setEditable(true);
        TableColumn<Pet, Number> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("pet_id"));

        TableColumn<Pet, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Pet, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        // Age column
        TableColumn<Pet, Number> ageCol = new TableColumn<>("Age");
        ageCol.setCellValueFactory(new PropertyValueFactory<>("age"));

        // Description column
        TableColumn<Pet, String> descriptionCol = new TableColumn<>("Description");
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));

        // Shelter column
        TableColumn<Pet, String> shelterCol = new TableColumn<>("Shelter");
        shelterCol.setCellValueFactory(new PropertyValueFactory<>("shelter"));

        TableColumn<Pet, String> viewCol = new TableColumn<>("View");
        viewCol.setCellValueFactory(new PropertyValueFactory<Pet, String>("viewButton"));

        // Add columns to table
        table.getColumns().addAll(viewCol, nameCol, typeCol, ageCol, descriptionCol, shelterCol);

        table.setItems(data);

        petsPane.setCenter(table);

        data.addAll(Pet.getPets());

        return petsPane;
    }

    public BorderPane createPetFormPane() {

        BorderPane petPane = new BorderPane();
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(5);
    
        // Name field
        TextField nameField = new TextField();
        nameField.setPromptText("Name");
        GridPane.setConstraints(nameField, 0, 0);
        grid.getChildren().add(nameField);
    
        // Type field
        ObservableList<String> petTypes =
                FXCollections.observableArrayList(
                        "cat", "dog","lizard",
                        "snake", "turtle", "goldfish", "hamster",
                        "bird", "rabbit"/*, "                                         "*/
                );
        FXCollections.sort(petTypes);
        ComboBox typeField = new ComboBox(petTypes);
        typeField.setPrefWidth(200);
        typeField.setPromptText("Type");
        GridPane.setConstraints(typeField, 0, 1);
        grid.getChildren().add(typeField);
        // Age field
        TextField ageField = new TextField();
        ageField.setPromptText("Age");
        GridPane.setConstraints(ageField, 0, 2);
        grid.getChildren().add(ageField);
    
        // Description field
        TextField descriptionField = new TextField();
        descriptionField.setPromptText("Description");
        GridPane.setConstraints(descriptionField, 0, 3);
        grid.getChildren().add(descriptionField);
    
        // Shelter field
        ObservableList<String> shelters = getShelters();
        FXCollections.sort(shelters);
        ComboBox shelterField = new ComboBox(shelters);
        shelterField.setPrefWidth(200);
        shelterField.setPromptText("Shelter");
        GridPane.setConstraints(shelterField, 0, 4);
        grid.getChildren().add(shelterField);
        // Submit button
        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> {
            // Logic to add/edit pet information
            String name = nameField.getText();
            String type = (String) typeField.getValue();
            int age = Integer.parseInt(ageField.getText());
            String description = descriptionField.getText();
            String shelter = (String) shelterField.getValue();
    
            Pet newPet = new Pet(name, type, age, description, shelter);
            data.add(newPet);
            newPet.insertPet();

            // Clear fields after adding
            nameField.clear();
            typeField.getSelectionModel().clearSelection();
            ageField.clear();
            descriptionField.clear();
            shelterField.getSelectionModel().clearSelection();
        });
        GridPane.setConstraints(submitButton, 0, 5);
        grid.getChildren().add(submitButton);
    
        // Add the GridPane to the center of the BorderPane
        petPane.setCenter(grid);
        return petPane;
    }

    public void refreshTable() {
        table.getItems().clear();
        data.addAll(Pet.getPets());
    }

    public void refreshTable(String nameFilter, String typeFilter, String ageFilter, String descriptionFilter, String shelterFilter){
        table.getItems().clear();
        data.addAll(Pet.getPets(nameFilter, typeFilter, ageFilter, descriptionFilter, shelterFilter));
    }

    public void addDelete(){
        TableColumn<Pet, String> deleteCol = new TableColumn<>("Delete");
        deleteCol.setCellValueFactory(new PropertyValueFactory<Pet, String>("deleteButton"));
        table.getColumns().add(deleteCol);
    }

    public void addAdopt(){
        TableColumn<Pet, String> adoptCol = new TableColumn<>("Adopt");
        adoptCol.setCellValueFactory(new PropertyValueFactory<Pet, String>("adoptButton"));
        table.getColumns().add(adoptCol);
    }

    public void removeColumn(String columnName) {
        Iterator<TableColumn<Pet, ?>> iterator = table.getColumns().iterator();
        while (iterator.hasNext()) {
            TableColumn<Pet, ?> column = iterator.next();
            if (column.getText().equals(columnName)) {
                iterator.remove();
                break;
            }
        }
    }

    public static PetView getPetView(){
        return view;
    }

    public ObservableList<String> getShelters(){
        ObservableList<String> shelterData = FXCollections.observableArrayList();
        try {
            Class.forName(JDBC_DRIVER);
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = conn.createStatement();
            String sql = "SELECT shelter_name FROM shelters";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {

                shelterData.add(rs.getString("shelter_name"));
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return shelterData;
    }
}
