package com.comp301.a09dungeon.model;

import com.comp301.a09dungeon.model.board.Board;
import com.comp301.a09dungeon.model.board.BoardImpl;
import com.comp301.a09dungeon.model.board.Posn;
import com.comp301.a09dungeon.model.pieces.CollisionResult;
import com.comp301.a09dungeon.model.pieces.Piece;

import java.util.ArrayList;
import java.util.List;

public class ModelImpl implements Model {

  private final Board board;
  private int curScore;
  private int highScore;
  private STATUS status;
  private MODE mode;
  private int level;
  private final List<Observer> observers;

  public ModelImpl(int width, int height) {
    this.board = new BoardImpl(width, height);
    this.curScore = 0;
    this.highScore = 0;
    this.level = 0;
    this.status = STATUS.START_GAME;
    this.mode = MODE.EASY;
    observers = new ArrayList<>();
  }

  public ModelImpl(Board board) {
    this.board = board;
    this.curScore = 0;
    this.highScore = 0;
    this.level = 0;
    this.status = STATUS.START_GAME;
    this.mode = MODE.EASY;
    observers = new ArrayList<>();
  }

  @Override
  public int getWidth() {
    return board.getWidth();
  }

  @Override
  public int getHeight() {
    return board.getHeight();
  }

  @Override
  public int getLives() {
    return board.getLives();
  }

  @Override
  public Piece get(Posn p) {
    return board.get(p);
  }

  @Override
  public int getCurScore() {
    return curScore;
  }

  @Override
  public int getHighScore() {
    return highScore;
  }

  @Override
  public int getLevel() {
    return level;
  }

  @Override
  public STATUS getStatus() {
    return status;
  }

  public void setStatus(STATUS status) {
    this.status = status;
    notifyObservers();
  }

  public MODE getModeStatus() {
    return mode;
  }

  public void setModeStatus(MODE mode) {
    this.mode = mode;
    notifyObservers();
  }

  @Override
  public void startGame() {
    status = STATUS.IN_PROGRESS;
    curScore = 0;
    level = 1;
    if (level + 1 + 2 + 2 > board.getHeight() * board.getWidth()) {
      status = STATUS.END_GAME;
      notifyObservers();
      return;
    }
    board.init(level + 1, 3, 2);

    notifyObservers();
  }

  @Override
  public void endGame() {
    status = STATUS.END_GAME;
    if (curScore > highScore) {
      highScore = curScore;
    }
    notifyObservers();
  }

  @Override
  public void moveUp() {
    mode = MODE.EASY;

    CollisionResult collision = board.moveHero(-1, 0);
    curScore += collision.getPoints();
    if (collision.getResults() == CollisionResult.Result.NEXT_LEVEL) {
      level += 1;
      board.init(level + 1, 3, 2);
    }
    if (collision.getResults() == CollisionResult.Result.GAME_OVER) {
      endGame();
    }
    notifyObservers();
  }

  @Override
  public void moveDown() {
    mode = MODE.EASY;

    CollisionResult collision = board.moveHero(1, 0);
    curScore += collision.getPoints();
    if (collision.getResults() == CollisionResult.Result.NEXT_LEVEL) {
      level += 1;
      board.init(level + 1, 3, 2);
    }
    if (collision.getResults() == CollisionResult.Result.GAME_OVER) {
      endGame();
    }
    notifyObservers();
  }

  @Override
  public void moveLeft() {
    mode = MODE.EASY;

    CollisionResult collision = board.moveHero(0, -1);
    curScore += collision.getPoints();
    if (collision.getResults() == CollisionResult.Result.NEXT_LEVEL) {
      level += 1;
      board.init(level + 1, 3, 2);
    }
    if (collision.getResults() == CollisionResult.Result.GAME_OVER) {
      endGame();
    }
    notifyObservers();
  }

  @Override
  public void moveRight() {
    mode = MODE.EASY;
    CollisionResult collision = board.moveHero(0, 1);
    curScore += collision.getPoints();
    if (collision.getResults() == CollisionResult.Result.NEXT_LEVEL) {
      level += 1;
      board.init(level + 1, 3, 2);
    }
    if (collision.getResults() == CollisionResult.Result.GAME_OVER) {
      endGame();
    }
    notifyObservers();
  }

  @Override
  public void moveUpHard() {
    mode = MODE.HARD;
    CollisionResult collision = board.moveHeroHardMode(-1, 0);
    curScore += collision.getPoints();
    if (collision.getResults() == CollisionResult.Result.NEXT_LEVEL) {
      level += 1;
      board.init(level + 1, 3, 2);
    }
    if (collision.getResults() == CollisionResult.Result.GAME_OVER) {
      endGame();
    }
    notifyObservers();
  }

  @Override
  public void moveDownHard() {
    mode = MODE.HARD;
    CollisionResult collision = board.moveHeroHardMode(1, 0);
    curScore += collision.getPoints();
    if (collision.getResults() == CollisionResult.Result.NEXT_LEVEL) {
      level += 1;
      board.init(level + 1, 3, 2);
    }
    if (collision.getResults() == CollisionResult.Result.GAME_OVER) {
      endGame();
    }
    notifyObservers();
  }

  @Override
  public void moveLeftHard() {
    mode = MODE.HARD;
    CollisionResult collision = board.moveHeroHardMode(0, -1);
    curScore += collision.getPoints();
    if (collision.getResults() == CollisionResult.Result.NEXT_LEVEL) {
      level += 1;
      board.init(level + 1, 3, 2);
    }
    if (collision.getResults() == CollisionResult.Result.GAME_OVER) {
      endGame();
    }
    notifyObservers();
  }

  @Override
  public void moveRightHard() {
    mode = MODE.HARD;
    CollisionResult collision = board.moveHeroHardMode(0, 1);
    curScore += collision.getPoints();
    if (collision.getResults() == CollisionResult.Result.NEXT_LEVEL) {
      level += 1;
      board.init(level + 1, 3, 2);
    }
    if (collision.getResults() == CollisionResult.Result.GAME_OVER) {
      endGame();
    }
    notifyObservers();
  }

  @Override
  public void addObserver(Observer o) {
    if (o == null) {
      throw new IllegalArgumentException();
    }
    if (!observers.contains(o)) {
      observers.add(o);
    }
  }

  private void notifyObservers() {
    for (Observer o : observers) {
      o.update();
    }
  }
}
