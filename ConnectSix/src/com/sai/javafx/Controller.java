package com.sai.javafx;
/* Created By B.SaiMouli.
    On 19/06/2021.
 */
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Controller implements Initializable {

    @FXML
    public GridPane rootGridPane; //#CAF0F8
    @FXML
    public Pane menuBarPane,insertableDiskPane;

    @FXML
    public VBox vbox;

    @FXML
    public ColorPicker playerOneColourChoiceBox,playerTwoColourChoiceBox;

    @FXML
    public TextField playerOneTextField,playerTwoTextFiled;

    @FXML
    public Button enterButton;

    @FXML
    public Label playerLabel;

    @FXML
    public Label turnLabel;

    private String diskOneColour = "#F4D35E";

    private  String diskTwoColour = "#588157";

    private String playerOneName = "Player One";

    private String playerTwoName = "Player Two";

    private final int circleDiameter = 78 ;

    private final float circleRadius = (float) circleDiameter/2;

    private final int totalRows = 8;

    private final int totalColumns = 10;

    private boolean isPlayerOneTurn = true;

    private final Disk [][] insertDiskArray = new Disk[totalRows][totalColumns];

    private boolean isGameEnded = false;

    private boolean isInsertAble = true;

    private String checkName1, checkName2;



    public void createPlayGround(){
        Shape playArea = playAreaWithHoles();
        rootGridPane.add(playArea,0,1);
        List<Rectangle> rectangleList = clickableBox();
        for (Rectangle rectangle:rectangleList) {
            rootGridPane.add(rectangle,0,1);
        }
    }

    private List<Rectangle> clickableBox(){
        List<Rectangle> clickableRectangleList = new ArrayList<>();
        for (int col = 0; col < totalColumns; col++) {

            Rectangle clickableBoxes = new Rectangle(circleDiameter, (circleDiameter + 1) * (totalRows + 1));
            clickableBoxes.setTranslateX(col* (circleDiameter + 6) + circleRadius / 2);
            clickableBoxes.setFill(Color.TRANSPARENT);

            clickableBoxes.setOnMouseEntered(event -> clickableBoxes.setFill(Color.valueOf("#ECF2D780")));

            clickableBoxes.setOnMouseExited(event -> clickableBoxes.setFill(Color.TRANSPARENT));

            int finalCol = col;
                enterButton.setOnAction(event -> {

                    checkName1 = playerOneTextField.getText();
                    checkName2 = playerTwoTextFiled.getText();
                    boolean check = checkName1.contains(checkName2);
                    System.out.println(check);
                    if( ( !playerOneTextField.getText().isEmpty() && !playerTwoTextFiled.getText().isEmpty() )  && !check ){

                            playerOneName = playerOneTextField.getText();
                            playerTwoName = playerTwoTextFiled.getText();

                    }else {

                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setContentText("Name of Player One And Two are Same Please Set Different Name");
                        alert.show();
                    }
                    playerLabel.setText(playerOneName);
                    if(playerOneColourChoiceBox.getValue() != playerTwoColourChoiceBox.getValue()) {
                        diskOneColour = playerOneColourChoiceBox.getValue().toString();
                        diskTwoColour = playerTwoColourChoiceBox.getValue().toString();
                    }else{
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setContentText("Disk Colour is Not Selected and are set to default Colors");
                        alert.show();
                    }

                    System.out.println(diskOneColour + " " + diskTwoColour);
                });


                clickableBoxes.setOnMouseClicked(event -> {
                    if(isInsertAble) {
                        insertDisk(new Disk(isPlayerOneTurn), finalCol);
                        isInsertAble = false;
                    }
                });
            clickableRectangleList.add(clickableBoxes);
        }
        return clickableRectangleList;
    }

    private void insertDisk(Disk disk, int column){
        int row = totalRows-1;
        if(!isGameEnded ) {
            while (row > 0) {
//                System.out.println("isEmpty : "  + insertDiskArray[row][column]);
                if (checkDiskIsPresent(row, column) == null) {
                    break;
                } else {
                    row--;
                }
            }
            if(insertDiskArray[row][column] == null) { // Statement prevents from adding disk on top of disk as shown in image Overlapping Disk..
                insertDiskArray[row][column] = disk;
                insertableDiskPane.getChildren().add(disk);
                disk.setTranslateX(column * (circleDiameter + 6) + circleRadius / 2);

                TranslateTransition transition = new TranslateTransition(Duration.seconds(0.5), disk);
                transition.setToY(row * (circleDiameter + 6) + circleRadius / 2);
                transition.play();
                int finalRow = row;
                transition.setOnFinished(event -> {
                    if (gameEnded(finalRow, column)) {

                        playerLabel.setText("Game Over");
                        turnLabel.setText((isPlayerOneTurn ? playerOneName : playerTwoName) + " Won");
                        Platform.runLater(this::alertBox);
                        isGameEnded = true;
                        return;
                    }
                    isInsertAble = true;
                    isPlayerOneTurn = !isPlayerOneTurn;
                    playerLabel.setText(isPlayerOneTurn ? playerOneName : playerTwoName);
                });
            }
        }
    }

    private void alertBox() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        String str = isPlayerOneTurn ? diskOneColour : diskTwoColour;
        alert.setHeaderText( (isPlayerOneTurn? playerOneName : playerTwoName) + " Won the Match of Disk Colour : " + str );
        alert.setTitle("Game Over");
        alert.setContentText("Want to Play Again");
        ButtonType yesButton = new ButtonType("Yes");
        ButtonType noButton = new ButtonType("No Exit");

        alert.getButtonTypes().setAll(yesButton,noButton);
        Optional<ButtonType> buttonPressed = alert.showAndWait();
        if(buttonPressed.get() == yesButton){
            restartGame();
        }else {
            exitApp();
        }

    }

    private boolean gameEnded(int row, int column) {

        List<Point2D> horizontalPoints = IntStream.rangeClosed(column - 5,column + 5).mapToObj(col -> new Point2D(row,col)).collect(Collectors.toList());

        List<Point2D> verticalPoints = IntStream.rangeClosed(row -5, row + 5).mapToObj(r -> new Point2D(r,column)).collect(Collectors.toList());

        Point2D startPoint = new Point2D(row-3,column+3);

        List<Point2D> diagonalPoints = IntStream.rangeClosed(0,8).mapToObj(i -> startPoint.add(i,-i)).collect(Collectors.toList());

        Point2D startPoint1 = new Point2D(row-3, column-3);

        List<Point2D> reverseDiagonalPoints = IntStream.rangeClosed(0,8).mapToObj(i -> startPoint1.add(i,i)).collect(Collectors.toList());

        return checkCombination(horizontalPoints) || checkCombination(verticalPoints) || checkCombination(diagonalPoints) || checkCombination(reverseDiagonalPoints);

    }

    private boolean checkCombination(List<Point2D> verticalPoints) {
        int count = 0;

        for (Point2D points:verticalPoints) {
            int rowIndexForArray = (int) points.getX();
            int columnIndexForArray = (int) points.getY();

            Disk disk = checkDiskIsPresent(rowIndexForArray,columnIndexForArray);

            if(disk != null && disk.isPlayerOneTurn == isPlayerOneTurn){
                System.out.println(disk.isPlayerOneTurn);
                count++;
                System.out.println(count);
                if(count == 6){
                    return true;
                }
            }else{
                count = 0;
            }

        }
        return false;
    }

    private Disk checkDiskIsPresent(int row, int column) {
        if(row >= totalRows || row < 0 || column >= totalColumns || column < 0){
            return null;
        }

        return insertDiskArray[row][column];
    }

    private Shape playAreaWithHoles() {

        Shape playArea = new Rectangle((circleDiameter+1)*(totalColumns + 1),(circleDiameter+1)*(totalRows + 1));

        for (int row = 0; row < totalRows; row++) {

            for (int col = 0; col < totalColumns; col++) {

                Circle circle = new Circle(circleRadius);
                circle.setCenterX(circleRadius);
                circle.setCenterY(circleRadius);
                circle.setTranslateX(col*(circleDiameter + 6) + circleRadius/2);
                circle.setTranslateY(row*(circleDiameter + 6) + circleRadius/2);

                playArea = Shape.subtract(playArea,circle);
            }
        }
        playArea.setFill(Paint.valueOf("#0A9396"));//B6AD90 F1FAEE  6930C3
        return playArea;
    }

    public void exitApp() {
        Platform.exit();
        System.exit(0);
    }

    public void restartGame() {
        insertableDiskPane.getChildren().clear();
        for (int row = 0; row < insertDiskArray.length; row++) {
            for (int col = 0; col < insertDiskArray[row].length; col++) {
                insertDiskArray[row][col] = null;
            }
        }
        isPlayerOneTurn = true;
        isInsertAble = true;
        isGameEnded = false;
        diskOneColour = "#F4D35E";
        diskTwoColour = "#588157";
        playerOneName = "Player One";
        playerTwoName = "Player Two";
        playerLabel.setText(playerOneName);
        turnLabel.setText("Turn");
        playerOneTextField.clear();
        playerTwoTextFiled.clear();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Colours And Names Are Set to default Values");
        alert.show();
    }

    private class Disk extends Circle {
            private final boolean isPlayerOneTurn ;

            public Disk(boolean isPlayerOneTurn){
                this.isPlayerOneTurn = isPlayerOneTurn;
                setRadius(circleRadius);
                setFill(isPlayerOneTurn ? Color.valueOf(diskOneColour) : Color.valueOf(diskTwoColour));
                setCenterX(circleRadius);
                setCenterY(circleRadius);
            }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

}

