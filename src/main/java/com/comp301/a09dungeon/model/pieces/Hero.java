package com.comp301.a09dungeon.model.pieces;

import com.comp301.a09dungeon.model.board.Posn;

public class Hero extends APiece implements MovablePiece {
  public Hero() {
    super("Hero", "/newhero.jpg");
  }

  public CollisionResult collide(Piece other) {
    if (other == null) {
      return new CollisionResult(0, CollisionResult.Result.CONTINUE);
    }
    if (other.getName().equals("Heart")) {
      return new CollisionResult(0, CollisionResult.Result.CONTINUE);
    }
    if (other.getName().equals("Treasure")) {
      Treasure treasure = (Treasure) other;
      return new CollisionResult(treasure.getValue(), CollisionResult.Result.CONTINUE);
    }
    if (other.getName().equals("Enemy")) {
      return new CollisionResult(0, CollisionResult.Result.GAME_OVER);
    }
    if (other.getName().equals("Exit")) {
      return new CollisionResult(0, CollisionResult.Result.NEXT_LEVEL);
    }
    if (other.getName().equals("Wall")) {
      throw new IllegalArgumentException("Collision with wall");
    }
    throw new IllegalArgumentException("Collision with unknown piece");
  }

  public String getName() {
    return super.getName();
  }

  public void setPosition(Posn p) {
    super.setPosn(p);
  }

  public Posn getPosition() {
    return super.getPosn();
  }
}
