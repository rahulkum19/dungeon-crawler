package com.comp301.a09dungeon.view;

import com.comp301.a09dungeon.controller.Controller;
import com.comp301.a09dungeon.model.Model;
import com.comp301.a09dungeon.model.board.Posn;
import com.comp301.a09dungeon.model.pieces.Piece;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.awt.*;

public class GameView implements FXComponent {
  private final Controller controller;
  private final Model model;

  public GameView(Controller controller, Model model) {
    this.controller = controller;
    this.model = model;
  }

  @Override
  public Parent render() {
    BorderPane pane = new BorderPane();
    GridPane grid = new GridPane();
    for (int i = 0; i < model.getHeight(); i++) {
      for (int j = 0; j < model.getWidth(); j++) {
        Piece current = model.get(new Posn(i, j));
        if (current != null) {
          ImageView iv = new ImageView(new Image("/newfloor.jpg"));
          iv.setFitHeight(75);
          iv.setFitWidth(75);
          grid.add(iv, j, i);
          ImageView iv1 = new ImageView(new Image(current.getResourcePath()));
          iv1.setFitHeight(50);
          iv1.setFitWidth(50);
          StackPane spane = new StackPane(iv, iv1);
          spane.setStyle("-fx-border-color: black; -fx-border-radius: 1px; -fx-border-width: 1px;");
          grid.add(spane, j, i);
        } else {
          ImageView iv = new ImageView(new Image("/newfloor.jpg"));
          iv.setFitHeight(75);
          iv.setFitWidth(75);
          StackPane spane = new StackPane(iv);
          spane.setStyle("-fx-border-color: black; -fx-border-radius: 1px; -fx-border-width: 1px;");
          grid.add(spane, j, i);
        }
      }
    }
    grid.setAlignment(Pos.CENTER);
    grid.setTranslateY(25);
    pane.setCenter(grid);

    ImageView upButton = new ImageView(new Image("/upbutton.jpg"));
    upButton.setFitHeight(55);
    upButton.setFitWidth(55);
    Button up = new Button();
    up.setGraphic(upButton);
    up.setOnAction(
        e -> {
          if (model.getModeStatus() == Model.MODE.HARD) {
            controller.moveUpHard();
          } else {
            controller.moveUp();
          }
        });
    up.setStyle("-fx-background-color: #496266; ");
    up.setTranslateY(5.25);

    ImageView downButton = new ImageView(new Image("/downbutton.jpg"));
    downButton.setFitHeight(55);
    downButton.setFitWidth(55);
    Button down = new Button();
    down.setGraphic(downButton);
    down.setOnAction(
        e -> {
          if (model.getModeStatus() == Model.MODE.HARD) {
            controller.moveDownHard();
          } else {
            controller.moveDown();
          }
        });
    down.setStyle("-fx-background-color: #496266; ");

    ImageView leftButton = new ImageView(new Image("/leftbutton.jpg"));
    leftButton.setFitHeight(55);
    leftButton.setFitWidth(55);
    Button left = new Button();
    left.setGraphic(leftButton);
    left.setOnAction(
        e -> {
          if (model.getModeStatus() == Model.MODE.HARD) {
            controller.moveLeftHard();
          } else {
            controller.moveLeft();
          }
        });
    left.setStyle("-fx-background-color: #496266; ");
    left.setTranslateX(14);

    ImageView rightButton = new ImageView(new Image("/rightbutton.jpg"));
    rightButton.setFitHeight(55);
    rightButton.setFitWidth(55);
    Button right = new Button();
    right.setGraphic(rightButton);
    right.setOnAction(
        e -> {
          if (model.getModeStatus() == Model.MODE.HARD) {
            controller.moveRightHard();
          } else {
            controller.moveRight();
          }
        });
    right.setStyle("-fx-background-color: #496266; ");
    right.setTranslateX(-14);

    pane.setOnKeyPressed(
        e -> {
          KeyCode code = e.getCode();
          if (code == KeyCode.W) {
            up.fire();
          } else if (code == KeyCode.A) {
            left.fire();
          } else if (code == KeyCode.S) {
            down.fire();
          } else if (code == KeyCode.D) {
            right.fire();
          }
        });

    HBox bottom = new HBox(5, left, down, right);
    bottom.setAlignment(Pos.CENTER);
    HBox top = new HBox(up);
    top.setAlignment(Pos.CENTER);
    VBox controls = new VBox(top, bottom);
    pane.setBottom(controls);

    Label highScore = new Label("High Score: " + model.getHighScore());
    highScore.setFont(new Font("Times New Roman", 20));
    highScore.setStyle(
        "-fx-text-fill: #AFB9AF; -fx-font-weight: bold; -fx-padding: 5px; -fx-border-color: #AFB9AF; -fx-border-radius: 5px; -fx-border-width: 1.5px;");

    Label lastScore = new Label("Current Score: " + model.getCurScore());
    lastScore.setFont(new Font("Times New Roman", 20));
    lastScore.setStyle(
        "-fx-text-fill: #AFB9AF; -fx-font-weight: bold; -fx-padding: 5px; -fx-border-color: #AFB9AF; -fx-border-radius: 5px; -fx-border-width: 1.5px;");

    VBox score = new VBox(10, highScore, lastScore);

    Text heart =
        new Text("Grab a heart to gain an extra life!\nLose a life when attacked by an enemy.");
    heart.setFont(new Font("Times New Roman", 20));
    heart.setStyle("-fx-fill: #AFB9AF; -fx-font-weight: bold;");
    heart.setTextAlignment(TextAlignment.CENTER);

    HBox scores = new HBox(25, score, heart);
    VBox hearts = new VBox(5);
    for (int i = 0; i < model.getLives(); i++) {
      ImageView iv = new ImageView(new Image("/newheart.jpg"));
      iv.setFitWidth(40);
      iv.setFitHeight(40);
      hearts.getChildren().add(iv);
    }
    scores.getChildren().add(hearts);

    scores.setAlignment(Pos.CENTER);
    scores.setTranslateY(20);
    pane.setTop(scores);
    pane.setStyle("-fx-background-color: #496266; ");
    return pane;
  }
}
