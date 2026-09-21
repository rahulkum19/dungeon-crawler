package com.comp301.a09dungeon.model.pieces;

public class Treasure extends APiece {
  private final int value = 50;

  public Treasure() {
    super("Treasure", "/newtreasure.jpg");
  }

  public int getValue() {
    return value;
  }

  public String getName() {
    return super.getName();
  }
}
