package com.example.csc365project;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Pet Adoption System");

        // create all views
        LoginView loginView = new LoginView();
        PetView petView = new PetView();
        AdoptionView adoptionView = new AdoptionView();
        ShelterView shelterView = new ShelterView();

        // Create the TabPane
        TabPane tabPane = new TabPane();

        // Create tabs
        Tab addPetTab = new Tab("Add Pet");
        addPetTab.setClosable(false); // So the tab cannot be closed
        addPetTab.setContent(petView.createPetFormPane());

        Tab petsTab = new Tab("Pets");
        petsTab.setClosable(false);
        petsTab.setContent(petView.createPetManagementPane());
        
        Tab adoptionsTab = new Tab("Adoptions");
        adoptionsTab.setClosable(false);
        adoptionsTab.setContent(adoptionView.createAdoptionManagementPane());

        Tab sheltersTab = new Tab("Shelters");
        sheltersTab.setClosable(false);
        sheltersTab.setContent(shelterView.createShelterViewPane());

        Tab accountTab = new Tab("Account");
        accountTab.setClosable(false);
        accountTab.setContent(loginView.createLoginPane());
        
        // Add tabs to the TabPane
        tabPane.getTabs().addAll(accountTab, addPetTab, petsTab, adoptionsTab, sheltersTab);

        // Create a layout and add TabPane to it
        BorderPane rootLayout = new BorderPane();
        rootLayout.setTop(tabPane);

        // Create a Scene with the layout
        Scene scene = new Scene(rootLayout, 720, 673); // Width and height based on your image
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
