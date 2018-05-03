package SlotMachine;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SlotMachineGUI extends Application {

    //to view the image
    private ImageView reel1Image, reel2Image, reel3Image;

    //variables of the 3 reels
    private Reel reel1;
    private Reel reel2;
    private Reel reel3;


    //array to store the details for statistics
    private static int[] statistcs = new int[4];
    // [0] - wins   [1] - loses   [2] - noOfGames   [3] - netCredit


    //buttons and labels used in the fx
    private Button addCoinBtn, betOneBtn, betMaxBtn, resetBtn, spinBtn, statsBtn, saveStatsBtn;
    private Label totalCredDisplay, betCredDisplay, resultDisplay, netCreditsDisplay;


    //variables
    private int betAmount = 0;
    private int totCredits = 10;
    private int netBetAmount = 0;
    private int resultCredit;
    int betMaxCount = 0;
    private double payout;
    private double payoutTotal = 0;


    @Override
    public void start(Stage primaryStage) throws Exception {


        primaryStage.setTitle("Slot Machine Game");

        //defining the buttons
        addCoinBtn = new Button("Add Coin");
        betOneBtn = new Button("Bet one");
        betMaxBtn = new Button("Bet Max");
        resetBtn = new Button("Reset");
        spinBtn = new Button("Spin");
        statsBtn = new Button("Statistics");
        saveStatsBtn = new Button("Save Statistics");

        //defining the labels
        betCredDisplay = new Label("   Bet : " + betAmount);
        totalCredDisplay = new Label("  Credit : " + totCredits);
        resultDisplay = new Label("   WELCOME!!   ");


        //defining the image views
        reel1Image = new ImageView();
        reel2Image = new ImageView();
        reel3Image = new ImageView();

        //setting the initial images for the reel
        reel1Image.setImage(new Image("/images/bell.png"));
        reel1Image.setFitWidth(250);
        reel1Image.setPreserveRatio(true);

        reel2Image.setImage(new Image("/images/redseven.png"));
        reel2Image.setFitWidth(250);
        reel2Image.setPreserveRatio(true);

        reel3Image.setImage(new Image("/images/cherry.png"));
        reel3Image.setFitWidth(250);
        reel3Image.setPreserveRatio(true);

        //BorderPane mainLayout = new BorderPane();
        GridPane mainLayout = new GridPane();
        //vboxes
        VBox betLabels = new VBox();
        VBox creditLabels = new VBox();
        VBox mainBtns = new VBox();
        VBox bettingBtns = new VBox();

        //hboxes
        HBox game = new HBox();
        HBox main = new HBox();
        HBox result = new HBox();


        //adding stuffs to some elements
        bettingBtns.getChildren().addAll(betOneBtn, betMaxBtn);
        betLabels.getChildren().addAll(betCredDisplay, bettingBtns);
        mainBtns.getChildren().addAll(spinBtn, resetBtn);
        creditLabels.getChildren().addAll(totalCredDisplay, addCoinBtn, statsBtn);
        game.getChildren().addAll(reel1Image, reel2Image, reel3Image);
        result.getChildren().add(resultDisplay);

        //Setting the style IDs
        bettingBtns.setId("hbox-style");
        mainBtns.setId("hbox-style");
        betLabels.setId("hbox-style");
        creditLabels.setId("hbox-style");
        result.setId("hbox-style");
        game.setId("gameBox-style");
        resultDisplay.setId("result-style");
        spinBtn.setId("spinAndRestBtn-style");
        resetBtn.setId("spinAndRestBtn-style");

        //Making the gui Responsive
        RowConstraints rc1 = new RowConstraints();
        rc1.setPercentHeight(20);
        RowConstraints rc2 = new RowConstraints();
        rc2.setPercentHeight(60);
        RowConstraints rc3 = new RowConstraints();
        rc3.setPercentHeight(50);

        ColumnConstraints colC1 = new ColumnConstraints();
        colC1.setPercentWidth(30);
        ColumnConstraints colC2 = new ColumnConstraints();
        colC2.setPercentWidth(100);
        ColumnConstraints colC3 = new ColumnConstraints();
        colC3.setPercentWidth(30);

        mainLayout.getRowConstraints().addAll(rc1, rc2, rc3);
        mainLayout.getColumnConstraints().addAll(colC1, colC2, colC3);

        //Adding child elements to the Border pane
        mainLayout.add(result, 1, 0);
        mainLayout.add(game, 1, 1);
        mainLayout.add(betLabels, 0, 2);
        mainLayout.add(mainBtns, 1, 2);
        mainLayout.add(creditLabels, 2, 2);
        mainLayout.setAlignment(Pos.CENTER);
        game.setAlignment(Pos.CENTER);
        result.setAlignment(Pos.TOP_CENTER);

        //game.setMaxSize(300, 300);
        Scene scene = new Scene(mainLayout, 1300, 800);
        primaryStage.setScene(scene);
        scene.getStylesheets().add("/Design/SlotMachine.css");
        primaryStage.sizeToScene();
        primaryStage.show();


        //method to add coin through the add coin button and display in the label
        addCoinBtn.setOnAction(e -> {
            addCoin();
        });

        //method to bet one credit at a time using betOne button
        betOneBtn.setOnAction(e -> {
            betOne();
        });

        //method to bet max at a time using betMax button
        betMaxBtn.setOnAction(e -> {
            betMax();
        });


        //method to spin the reel
        spinBtn.setOnAction(e -> {
            spin();
        });

        //method to reset the game using the reset button
        resetBtn.setOnAction(e -> {
            reset();
        });


        //method to stop reel spinning

        //method to stop the reel spinning
        game.setOnMouseClicked(e -> {
            reel3.suspend();
            reel2.suspend();
            reel1.suspend();
            result();

            //statistics gui
            statsBtn.setOnAction(ex -> {
                statsWindow();


            });

            saveStatsBtn.setOnAction(event -> {
                textFile();
            });
        });
    }


    //method for the add coin button
    public void addCoin() {
        totCredits++;
        totalCredDisplay.setText("Credit : " + String.valueOf(totCredits));
    }

    //action method for the bet one button
    public void betOne() {
        //to check if the total credits over
        if (totCredits <= 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Alert");
            alert.setHeaderText(null);
            alert.setContentText("GAME OVER!!!!");

            alert.showAndWait();
        } else {
            betAmount++;//increasing the bet amount by one
            totCredits--;//reducing the total credits by one
            totalCredDisplay.setText("Credit : " + String.valueOf(totCredits));
            betCredDisplay.setText("Bet : " + String.valueOf(betAmount));
            netBetAmount++;

        }
    }

    //method for the bet max button
    public void betMax() {
        //to check the no.of times the button is presses
        if (betMaxCount < 1) {
            //checking the total credits is over for the game
            if (totCredits > 0) {
                //checking if total credits for bet max is not enough
                if (totCredits > 2) {
                    betAmount += 3;
                    totCredits -= 3;
                    betCredDisplay.setText("Bet : " + String.valueOf(betAmount));
                    totalCredDisplay.setText("Credit : " + String.valueOf(totCredits));
                    netBetAmount = netBetAmount + 3;
                    betMaxCount++;


                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Alert");
                    alert.setHeaderText(null);
                    alert.setContentText("NOT ENOUGH CREDIT!!");

                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Alert");
                alert.setHeaderText(null);
                alert.setContentText("GAME OVER!!");

                alert.showAndWait();
            }

        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Alert");
            alert.setHeaderText(null);
            alert.setContentText("You can use the betmax button only once per spin!!!");

            alert.showAndWait();
            betMaxCount = 0;

        }
    }

    //method for the spin button
    public void spin() {
        if (betAmount == 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Alert");
            alert.setHeaderText(null);
            alert.setContentText("Bet cannot be zero!!");

            alert.showAndWait();
        } else {

            reel1 = new Reel(reel1Image);
            reel2 = new Reel(reel2Image);
            reel3 = new Reel(reel3Image);

            statistcs[2]++;//adding to the no of games in the statistics array
            reel1.start();
            reel2.start();
            reel3.start();
        }

    }

    public void reset() {
        totCredits += betAmount;
        betAmount = 0;
        betCredDisplay.setText(String.valueOf("Bet : " + betAmount));
        totalCredDisplay.setText(String.valueOf("Credit : " + totCredits));
        //setting the default images to the image views
        reel1Image.setImage(new Image("/images/bell.png"));
        reel2Image.setImage(new Image("/images/redseven.png"));
        reel3Image.setImage(new Image("/images/cherry.png"));
    }


    //method to find the results
    public void result() {
        //getting the Symbol's value when the reel stops
        int img1Value = reel1.getSymValue();
        int img2Value = reel2.getSymValue();
        int img3Value = reel3.getSymValue();

        if (img1Value == img2Value && img2Value == img3Value && img1Value == img3Value) {
            payout = (((double) 100 / (double) 216) * img1Value);
            payoutTotal += payout;
            resultDisplay.setText("Congratulations!! you get a free spin");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Alert");
            alert.setHeaderText(null);
            alert.setContentText(" spin the reel for the extra spin!!!");
            alert.showAndWait();
            betCredDisplay.setText(String.valueOf(betAmount));
            if (img1Value == 5 && img2Value == 5 && img3Value == 5) {
                payout = (((double) 100 / (double) 216) * img1Value);
                payoutTotal += payout;


                totCredits += (img1Value * betAmount);
                statistcs[0]++;
                totalCredDisplay.setText(String.valueOf("Credit : " + (totCredits)));
                resultDisplay.setText("   YOU WIN!!!  " + (img1Value * betAmount) + " credits  ");
                resultCredit = ((img1Value * betAmount) - betAmount);


            } else {
                alert.setTitle("Alert");
                alert.setHeaderText(null);
                alert.setContentText(" No credits won in the free spin!!");
                resultDisplay.setText("No credit!!!");
                alert.showAndWait();

            }

        }

        //Comparing the reel symbols to determine win or lost
        else if (img1Value == img2Value) {
            //method to find the payout
            payout = ((100 / 36) * img1Value);
            payoutTotal += payout;

            totCredits += (img1Value * betAmount);
            statistcs[0]++;
            totalCredDisplay.setText(String.valueOf("Credit : " + (totCredits)));
            resultDisplay.setText("   YOU WIN!!!  " + (img1Value * betAmount) + " credits   ");
            resultCredit = (img1Value * betAmount);
            statistcs[3] = statistcs[3] + (resultCredit - netBetAmount);
            betAmount = 0;
            betCredDisplay.setText(String.valueOf(betAmount));
            resultCredit = ((img1Value * betAmount) - betAmount);


        } else if (img2Value == img3Value) {
            //mthod to find the payout
            payout = ((100 / 36) * img2Value);
            payoutTotal += payout;
            totCredits += (img2Value * betAmount);
            totalCredDisplay.setText(String.valueOf("Credit : " + (totCredits)));
            resultDisplay.setText("   YOU WIN!!!  " + (img2Value * betAmount) + " credits   ");
            statistcs[0]++;
            resultCredit = ((img2Value * betAmount) - betAmount);

            betAmount = 0;
            betCredDisplay.setText(String.valueOf(betAmount));


        } else if (img1Value == img3Value) {
            //method to find the payout
            payout = ((100 / 36) * img1Value);
            payoutTotal += payout;
            //finding and updating the total cedits
            totCredits += (img1Value * betAmount);
            totalCredDisplay.setText(String.valueOf("Credit : " + (totCredits)));
            resultDisplay.setText("   YOU WIN!!!  " + (img1Value * betAmount) + " credits   ");
            resultCredit = ((img1Value * betAmount) - betAmount);
            betAmount = 0;
            betCredDisplay.setText(String.valueOf(betAmount));


            statistcs[0]++;

        } else {
            resultDisplay.setText("   YOU LOOSE! TRY AGAIN   ");
            totalCredDisplay.setText(String.valueOf("Credit : " + totCredits));
            statistcs[1]++;
            betAmount = 0;
            betCredDisplay.setText(String.valueOf(betAmount));

        }
    }

    //method to display the statistics window
    public void statsWindow() {
        Stage stage = new Stage();
        BorderPane main2 = new BorderPane();

        netCreditsDisplay = new Label();

        Button window = new Button();

        VBox graph = new VBox();
        graph.setId("stats");

        window.setText("Payout window");

        PieChart chart = new PieChart();
        PieChart.Data part1 = new PieChart.Data("Wins", statistcs[0]);
        PieChart.Data part2 = new PieChart.Data("Loses", statistcs[1]);

        chart.getData().addAll(part1, part2);

        //adding elements to the main pane
        graph.getChildren().addAll(chart, saveStatsBtn);
        main2.setLeft(graph);

        main2.setRight(window);
        main2.setBottom(netCreditsDisplay);


        netCreditsDisplay.setText("Avg Credit won: " + resultCredit);

        Scene scene1 = new Scene(main2, 800, 500);
        scene1.getStylesheets().add("/Design/SlotMachine.css");
        stage.setScene(scene1);
        stage.show();


        window.setOnAction(e -> {
            payoutNewWindow();
        });


    }


    // save the game details to the text file
    public void textFile() {
        try {
            String fileName = new SimpleDateFormat("yyyy-MM-dd-HH-mm'.txt'").format(new Date());
            File file = new File(fileName);
            PrintWriter writer = new PrintWriter(file, "UTF-8");
            writer.println("SLOT MACHINE STATISTICS");
            writer.println("Wins :" + statistcs[0]);
            writer.println("Loses:" + statistcs[1]);
            writer.println("Average number of credits netted per game:" + statistcs[3]);
            writer.println("payout percentage : " + (payoutTotal) + "%");
            writer.close();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Alert");
            alert.setHeaderText(null);
            alert.setContentText("Text file saved!!");

            alert.showAndWait();

        } catch (IOException e) {
            System.out.println("File Saving Error!");
        }
    }


    //method to display the payout window
    public void payoutNewWindow() {
        Stage stage = new Stage();
        BorderPane main3 = new BorderPane();

        Label payout = new Label();
        payout.setText(payoutTotal + "%");

        main3.setCenter(payout);


        Scene scene1 = new Scene(main3, 500, 500);
        scene1.getStylesheets().add("/Design/SlotMachine.css");
        stage.setScene(scene1);
        stage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }
}




