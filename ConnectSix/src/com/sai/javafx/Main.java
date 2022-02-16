package com.sai.javafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {
    Controller controller = new Controller();
    @Override
    public void start(Stage primaryStage) throws Exception{
        MenuBar menuBar = CreateMenu();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("game.fxml"));
        GridPane rootGridPane = loader.load();
        controller = loader.getController();
        controller.createPlayGround();
        menuBar.prefWidthProperty().bind(primaryStage.widthProperty());
        Pane pane = (Pane) rootGridPane.getChildren().get(0);
        pane.getChildren().addAll(menuBar);


        Scene scene = new Scene(rootGridPane);
        primaryStage.setTitle("ConnectSix");
        primaryStage.getIcons().add(new Image("file:logo.png"));
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private MenuBar CreateMenu() {
        MenuBar menuBar = new MenuBar();
        // file menu..
        Menu fileMenu = new Menu("File");
        //about menu...
        Menu aboutMenu = new Menu("About");

        Menu gameMenu = new Menu("Game Options");

        MenuItem newMenuItem = new MenuItem("New");

        newMenuItem.setOnAction(event -> controller.restartGame());

        SeparatorMenuItem separatorMenuItem = new SeparatorMenuItem();

        MenuItem restartMenuItem = new MenuItem("Restart");

        restartMenuItem.setOnAction(event -> controller.restartGame());

        gameMenu.getItems().addAll(newMenuItem, separatorMenuItem, restartMenuItem);

        SeparatorMenuItem separatorMenuItem1 = new SeparatorMenuItem();

        MenuItem exitMenuItem = new MenuItem("Exit");

        exitMenuItem.setOnAction(event -> controller.exitApp());

        fileMenu.getItems().addAll(gameMenu, separatorMenuItem1, exitMenuItem);



        MenuItem aboutMenuItem = new MenuItem("About Game");
        aboutMenuItem.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Instruction");
            alert.setTitle("Game Instruction");
            alert.setContentText("Connect Four is a two-player connecting game in which the " +
                                 "players first choose a color and then take turns dropping " +
                                 "colored discs from the top into a seven-column, six-row vertically " +
                                 "suspended grid. The pieces fall straight down, occupying the next available" +
                                 " space within the column. The objective of the game is to be the first to form " +
                                 "a horizontal, vertical, or diagonal line of four of one's own discs. " +
                                 "Connect Four is a solved game. Any player can always win by playing the right moves.");
            alert.show();
        });
        SeparatorMenuItem separatorMenuItem2 = new SeparatorMenuItem();
        MenuItem aboutDeveloperMenuItem = new MenuItem("About Developer");
        aboutDeveloperMenuItem.setOnAction(event -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Developer Information");
                alert.setContentText("Developer : B.Sai Mouli " + "\n"+ "Developed On : 18th Feb 2021");
                alert.show();
        });
        aboutMenu.getItems().addAll(aboutMenuItem, separatorMenuItem2, aboutDeveloperMenuItem);

        menuBar.getMenus().addAll(fileMenu,aboutMenu);

        return menuBar;
    }


    public static void main(String[] args) {
        launch(args);
    }
}
