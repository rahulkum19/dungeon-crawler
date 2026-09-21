package com.comp301.a09dungeon.model;

import com.comp301.a09dungeon.model.board.Posn;
import com.comp301.a09dungeon.model.pieces.Piece;

public interface Model extends Subject {
  int getWidth();

  int getHeight();

  int getLives();

  Piece get(Posn p);

  int getCurScore();

  int getHighScore();

  int getLevel();

  // Change status from IN_PROGRESS and END_GAME
  STATUS getStatus();

  void setStatus(STATUS status);

  MODE getModeStatus();

  void setModeStatus(MODE mode);

  void startGame();

  void endGame();

  void moveUp();

  void moveDown();

  void moveLeft();

  void moveRight();

  void moveUpHard();

  void moveDownHard();

  void moveLeftHard();

  void moveRightHard();

  enum STATUS {
    START_GAME,
    END_GAME,
    IN_PROGRESS
  }

  enum MODE {
    EASY,
    HARD
  }
}
