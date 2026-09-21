package com.comp301.a09dungeon.controller;

import com.comp301.a09dungeon.model.Model;

public class ControllerImpl implements Controller {
  private final Model model;

  public ControllerImpl(Model model) {
    this.model = model;
  }

  @Override
  public void moveUp() {
    model.setModeStatus(Model.MODE.EASY);
    model.moveUp();
  }

  @Override
  public void moveDown() {
    model.setModeStatus(Model.MODE.EASY);
    model.moveDown();
  }

  @Override
  public void moveLeft() {
    model.setModeStatus(Model.MODE.EASY);
    model.moveLeft();
  }

  @Override
  public void moveRight() {
    model.setModeStatus(Model.MODE.EASY);
    model.moveRight();
  }

  @Override
  public void moveUpHard() {
    model.setModeStatus(Model.MODE.HARD);
    model.moveUpHard();
  }

  @Override
  public void moveDownHard() {
    model.setModeStatus(Model.MODE.HARD);
    model.moveDownHard();
  }

  @Override
  public void moveLeftHard() {
    model.setModeStatus(Model.MODE.HARD);
    model.moveLeftHard();
  }

  @Override
  public void moveRightHard() {
    model.setModeStatus(Model.MODE.HARD);
    model.moveRightHard();
  }

  @Override
  public void setEasyMode() {
    model.setModeStatus(Model.MODE.EASY);
  }

  @Override
  public void setHardMode() {
    model.setModeStatus(Model.MODE.HARD);
  }

  @Override
  public void startGame() {
    model.startGame();
  }

  public void restartGame() {
    model.setStatus(Model.STATUS.START_GAME);
  }
}
