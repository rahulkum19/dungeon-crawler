package com.comp301.a09dungeon.view;

import com.comp301.a09dungeon.controller.Controller;
import com.comp301.a09dungeon.model.Model;
import com.comp301.a09dungeon.model.Observer;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class View implements FXComponent, Observer {
  private final Controller controller;
  private final Model model;
  private final Stage stage;
  private boolean musicStart = false;
  private final Music music;

  public View(Controller controller, Model model, Stage stage) {
    this.controller = controller;
    this.model = model;
    this.stage = stage;
    this.music = new Music();
  }

  public Parent render() {
    if (model.getStatus() == Model.STATUS.START_GAME) {
      if (!musicStart) {
        music.play();
        musicStart = true;
      }
      return new TitleScreenView(controller, model).render();
    } else if (model.getStatus() == Model.STATUS.IN_PROGRESS) {
      return new GameView(controller, model).render();
    } else {
      return new EndView(controller, model).render();
    }
  }

  @Override
  public void update() {
    Scene scene = new Scene(this.render(), 750, 725);
    scene.getStylesheets().add("style/dungeon.css");
    stage.setScene(scene);
  }
}
