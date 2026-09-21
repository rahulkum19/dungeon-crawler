package com.comp301.a09dungeon.controller;

public interface Controller {
  void moveUp();

  void moveDown();

  void moveLeft();

  void moveRight();

  void moveUpHard();

  void moveDownHard();

  void moveLeftHard();

  void moveRightHard();

  void setEasyMode();

  void setHardMode();

  void startGame();

  void restartGame();
}
