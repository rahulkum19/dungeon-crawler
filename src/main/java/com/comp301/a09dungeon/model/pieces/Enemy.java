package com.comp301.a09dungeon.model.pieces;

public class Enemy extends APiece implements MovablePiece {

  public Enemy() {
    super("Enemy", "/newenemy.jpg");
  }

  public CollisionResult collide(Piece other) {
    if (other == null) {
      return new CollisionResult(0, CollisionResult.Result.CONTINUE);
    }
    if (other.getName().equals("Treasure")) {
      return new CollisionResult(0, CollisionResult.Result.CONTINUE);
    }
    if (other.getName().equals("Heart")) {
      return new CollisionResult(0, CollisionResult.Result.CONTINUE);
    }
    if (other.getName().equals("Exit")) {
      throw new IllegalArgumentException("Enemy collision with exit");
    }
    if (other.getName().equals("Hero")) {
      return new CollisionResult(0, CollisionResult.Result.GAME_OVER);
    }
    if (other.getName().equals("Wall")) {
      throw new IllegalArgumentException("Enemy collision with wall");
    }
    throw new IllegalArgumentException("Enemy collision with unknown piece");
  }

  public String getName() {
    return super.getName();
  }
}
