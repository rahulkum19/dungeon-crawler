package com.comp301.a09dungeon.view;

import com.comp301.a09dungeon.controller.Controller;
import com.comp301.a09dungeon.model.Model;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.awt.*;

public class TitleScreenView implements FXComponent {
  private final Controller controller;
  private final Model model;

  public TitleScreenView(Controller controller, Model model) {
    this.controller = controller;
    this.model = model;
  }

  @Override
  public Parent render() {
    StackPane pane = new StackPane();

    Label title = new Label("  Dungeon Crawler  ");
    title.setFont(new Font("Times New Roman", 50));
    title.setStyle(
        "-fx-font-weight: bold; -fx-padding: 5px; -fx-text-fill: black; -fx-background-color: #AFB9AF; -fx-background-radius: 5px; ");

    Label highScore = new Label(" High Score: " + model.getHighScore() + " ");
    highScore.setFont(new Font("Times New Roman", 20));
    highScore.setStyle(
        "-fx-text-fill: #AFB9AF; -fx-font-weight: bold; -fx-padding: 7px; -fx-border-color: #AFB9AF; -fx-border-radius: 5px; -fx-border-width: 1.5px;");

    Label lastScore = new Label(" Last Score: " + model.getCurScore() + " ");
    lastScore.setFont(new Font("Times New Roman", 20));
    lastScore.setStyle(
        "-fx-text-fill: #AFB9AF; -fx-font-weight: bold; -fx-padding: 7px; -fx-border-color: #AFB9AF; -fx-border-radius: 5px; -fx-border-width: 1.5px;");

    HBox scores = new HBox(20, highScore, lastScore);
    scores.setAlignment(Pos.CENTER);

    Label name = new Label("By Rahul Kumaresan");
    name.setFont(new Font("Times New Roman", 20));
    name.setStyle("-fx-text-fill: #AFB9AF; -fx-font-weight: bold;");

    Label diff = new Label("Difficulty: ");
    diff.setFont(new Font("Times New Roman", 17.5));
    diff.setStyle("-fx-text-fill: #AFB9AF; -fx-font-weight: bold;");

    ToggleButton mode = new ToggleButton("Easy Mode");
    mode.setFont(new Font("Times New Roman", 17.5));
    mode.setStyle(
        "-fx-text-fill: black; -fx-background-color: #AFD9AF; -fx-background-radius: 7px;");
    mode.setOnAction(
        e -> {
          if (mode.isSelected()) {
            mode.setText("Hard Mode");
            mode.setFont(new Font("Times New Roman", 17.5));
            mode.setStyle(
                "-fx-text-fill: black; -fx-background-color: #CFB9AF; -fx-background-radius: 7px;");

          } else {
            mode.setText("Easy Mode");
            mode.setFont(new Font("Times New Roman", 17.5));
            mode.setStyle(
                "-fx-text-fill: black; -fx-background-color: #AFD9AF; -fx-background-radius: 7px;");
          }
        });

    Button button = new Button("Start Game");
    button.setOnAction(
        e -> {
          if (mode.isSelected()) {
            controller.setHardMode();
          } else {
            controller.setEasyMode();
          }
          controller.startGame();
        });
    button.setFont(new Font("Times New Roman", 20));
    button.setStyle(
        "-fx-text-fill: black; -fx-background-color: #AFB9AF; -fx-background-radius: 7px; -fx-border-color: #AFBFAF; -fx-border-radius: 5px; -fx-border-width: 2px;");
    DropShadow ds = new DropShadow();
    ds.setColor(Color.BLACK);
    ds.setWidth(20);
    ds.setHeight(20);
    ds.setSpread(0.6);
    button.setEffect(ds);

    HBox difficulty = new HBox(10, diff, mode);
    difficulty.setAlignment(Pos.CENTER);

    VBox layout = new VBox(25, title, scores, name, button, difficulty);
    layout.setAlignment(Pos.CENTER);
    pane.setStyle("-fx-background-color: #496266; ");
    pane.getChildren().add(layout);
    return pane;
  }
}
