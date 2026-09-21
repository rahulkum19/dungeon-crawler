package com.comp301.a09dungeon.view;

import com.comp301.a09dungeon.controller.Controller;
import com.comp301.a09dungeon.controller.ControllerImpl;
import com.comp301.a09dungeon.model.Model;
import com.comp301.a09dungeon.model.ModelImpl;
import com.comp301.a09dungeon.model.board.Board;
import com.comp301.a09dungeon.model.board.BoardImpl;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.naming.ldap.Control;

public class AppLauncher extends Application {
  @Override
  public void start(Stage stage) {
    Board board = new BoardImpl(6, 6);
    Model model = new ModelImpl(board);
    Controller pc = new ControllerImpl(model);
    View view = new View(pc, model, stage);
    model.addObserver(view);

    Scene scene = new Scene(view.render(), 750, 725);
    stage.setScene(scene);
    stage.setTitle("Rahul Kumaresan's Dungeon Crawler");
    stage.show();
  }
}
