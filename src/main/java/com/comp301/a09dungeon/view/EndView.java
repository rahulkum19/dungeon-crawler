package com.comp301.a09dungeon.view;

import com.comp301.a09dungeon.controller.Controller;
import com.comp301.a09dungeon.model.Model;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;

import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.awt.*;

public class EndView implements FXComponent {
  private final Controller controller;
  private final Model model;

  public EndView(Controller controller, Model model) {
    this.controller = controller;
    this.model = model;
  }

  @Override
  public Parent render() {
    DropShadow ds = new DropShadow();
    ds.setColor(Color.RED);
    ds.setWidth(20);
    ds.setHeight(20);
    ds.setSpread(0.4);

    StackPane pane = new StackPane();
    Label gameOver = new Label("GAME OVER!");
    gameOver.setFont(new Font("Times New Roman", 35));
    gameOver.setStyle(
        "-fx-padding: 8px; -fx-text-fill: red; -fx-background-color: #AFB9AF; -fx-background-radius: 5px; ");
    gameOver.setEffect(ds);
    Button end = new Button("Main Menu");
    end.setOnAction(e -> controller.restartGame());
    end.setFont(new Font("Times New Roman", 20));
    end.setStyle(
        "-fx-text-fill: black; -fx-background-color: #AFB9AF; -fx-background-radius: 7px; -fx-border-color: #AFBFAF; -fx-border-radius: 5px; -fx-border-width: 2px;");

    Label lastScore = new Label(" Your Score: " + model.getCurScore() + " ");
    lastScore.setFont(new Font("Times New Roman", 20));
    lastScore.setStyle("-fx-text-fill: #AFB9AF; -fx-font-weight: bold; -fx-padding: 7px;");

    VBox layout = new VBox(35, gameOver, lastScore, end);
    layout.setAlignment(Pos.CENTER);

    pane.getChildren().add(layout);
    pane.setStyle("-fx-background-color: #496266; ");
    return pane;
  }
}
