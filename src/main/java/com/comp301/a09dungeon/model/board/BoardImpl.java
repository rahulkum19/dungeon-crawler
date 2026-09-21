package com.comp301.a09dungeon.model.board;

import com.comp301.a09dungeon.model.pieces.*;

import java.util.*;

public class BoardImpl implements Board {
  private final Piece[][] board;
  private final int row;
  private final int col;
  private final Map<String, Integer> initValues = new HashMap<String, Integer>();
  private Posn heroPosition;
  private final Random random;
  private int heroLives;

  public BoardImpl(int col, int row) {
    board = new Piece[row][col];
    this.row = row;
    this.col = col;
    heroPosition = null;
    this.random = new Random();
    this.heroLives = 0;
  }

  public BoardImpl(Piece[][] array) {
    board = array;
    this.row = board.length;
    this.col = board[0].length;
    heroPosition = null;
    this.random = new Random();
    this.heroLives = 0;
  }

  @Override
  public void init(int enemies, int treasures, int walls) {
    for (int i = 0; i < row; i++) {
      for (int j = 0; j < col; j++) {
        board[i][j] = null;
      }
    }
    heroLives = 0;
    heroPosition = null;
    isHero = false;
    initValues.clear();

    initValues.put("Hero", 1);
    initValues.put("Exit", 1);
    initValues.put("Heart", 2);
    initValues.put("Enemy", enemies);
    initValues.put("Treasure", treasures);
    initValues.put("Wall", walls);

    int totalPieces = enemies + treasures + walls + 2 + 2;
    int boardSpace = row * col;
    if (totalPieces > boardSpace) {
      throw new IllegalArgumentException();
    }
    while (totalPieces > 0) {
      int randRow = random.nextInt(row);
      int randCol = random.nextInt(col);
      if (board[randRow][randCol] == null) {
        placePiece(randRow, randCol);
        totalPieces -= 1;
      }
    }
  }

  public void placePiece(int row, int col) {
    if (initValues.get("Hero") == 1) {
      Hero hero = new Hero();
      board[row][col] = hero;
      heroPosition = new Posn(row, col);
      hero.setPosn(heroPosition);
      initValues.put("Hero", 0);
    } else if (initValues.get("Exit") == 1) {
      Exit exit = new Exit();
      board[row][col] = exit;
      exit.setPosn(new Posn(row, col));
      initValues.put("Exit", 0);
    } else if (initValues.get("Heart") > 0) {
      Heart heart = new Heart();
      board[row][col] = heart;
      heart.setPosn(new Posn(row, col));
      initValues.put("Heart", initValues.get("Heart") - 1);
    } else if (initValues.get("Enemy") > 0) {
      Enemy enemy = new Enemy();
      board[row][col] = enemy;
      enemy.setPosn(new Posn(row, col));
      initValues.put("Enemy", initValues.get("Enemy") - 1);
    } else if (initValues.get("Treasure") > 0) {
      Treasure treasure = new Treasure();
      board[row][col] = treasure;
      treasure.setPosn(new Posn(row, col));
      initValues.put("Treasure", initValues.get("Treasure") - 1);
    } else if (initValues.get("Wall") > 0) {
      Wall wall = new Wall();
      board[row][col] = wall;
      wall.setPosn(new Posn(row, col));
      initValues.put("Wall", initValues.get("Wall") - 1);
    }
  }

  @Override
  public int getWidth() {
    return col;
  }

  @Override
  public int getHeight() {
    return row;
  }

  public int getLives() {
    return heroLives;
  }

  @Override
  public Piece get(Posn posn) {
    return board[posn.getRow()][posn.getCol()];
  }

  @Override
  public void set(Piece p, Posn newPos) {
    board[newPos.getRow()][newPos.getCol()] = p;
  }

  boolean isHero = false;

  public boolean isEmpty(int i, int j) {
    if ((i >= 0) && (i < row) && (j >= 0) && (j < col)) {
      if ((board[i][j] == null
          || board[i][j].getName().equals("Treasure")
          || board[i][j].getName().equals("Hero"))) {
        if (board[i][j] != null && board[i][j].getName().equals("Hero")) {
          isHero = true;
        }
        return true;
      }
    }
    return false;
  }

  @Override
  public CollisionResult moveHero(int drow, int dcol) {

    Hero hero = (Hero) board[heroPosition.getRow()][heroPosition.getCol()];

    int newRow = hero.getPosn().getRow() + drow;
    int newCol = hero.getPosn().getCol() + dcol;
    int value = 0;
    if (newRow < 0 || newRow >= row || newCol < 0 || newCol >= col) {
      return new CollisionResult(value, CollisionResult.Result.CONTINUE);
    }
    Piece newSpot = board[newRow][newCol];
    if (newSpot == null || !newSpot.getName().equals("Wall")) {
      board[hero.getPosn().getRow()][hero.getPosn().getCol()] = null;
      heroPosition = new Posn(newRow, newCol);
      hero.setPosn(heroPosition);
      board[newRow][newCol] = hero;
    }
    if (newSpot != null) {
      if (newSpot.getName().equals("Wall")) {
        try {
          hero.collide(newSpot);
        } catch (IllegalArgumentException e) {
          return new CollisionResult(value, CollisionResult.Result.CONTINUE);
        }
      }
      if (newSpot.getName().equals("Exit")) {
        heroLives = 0;
        return hero.collide(newSpot);
      }
      if (newSpot.getName().equals("Heart")) {
        heroLives++;
      }
      if (newSpot.getName().equals("Treasure")) {
        value += ((Treasure) newSpot).getValue();
      }
      if (newSpot.getName().equals("Enemy")) {
        if (heroLives == 0) {
          return hero.collide(newSpot);
        } else {
          heroLives--;
        }
      }
    }

    List<Piece> movedEnemies = new ArrayList<>();

    for (int i = 0; i < row; i++) {
      for (int j = 0; j < col; j++) {
        if (board[i][j] != null
            && board[i][j].getName().equals("Enemy")
            && !movedEnemies.contains(board[i][j])) {
          Enemy enemy = (Enemy) board[i][j];
          movedEnemies.add(enemy);
          int moveDirection = random.nextInt(2);
          int randDirection = random.nextInt(2);

          int newEnemyRow = i;
          int newEnemyCol = j;

          if (randDirection == 0) {
            if (moveDirection == 0) {
              newEnemyRow += 1;
            } else {
              newEnemyRow += -1;
            }

          } else {
            if (moveDirection == 0) {
              newEnemyCol += 1;
            } else {
              newEnemyCol += -1;
            }
          }

          if (newEnemyRow < 0
              || newEnemyRow >= row
              || newEnemyCol < 0
              || newEnemyCol >= col
              || (board[newEnemyRow][newEnemyCol] != null
                  && (board[newEnemyRow][newEnemyCol].getName().equals("Wall")
                      || board[newEnemyRow][newEnemyCol].getName().equals("Exit")
                      || board[newEnemyRow][newEnemyCol].getName().equals("Enemy")))) {
            if (isEmpty(i + 1, j)) {
              if (isHero) {
                if (heroLives == 0) {
                  return new CollisionResult(value, CollisionResult.Result.GAME_OVER);
                } else {
                  heroLives--;
                  board[i][j] = null;
                  continue;
                }
              }
              board[i][j] = null;
              newEnemyRow = i + 1;
              newEnemyCol = j;
              enemy.setPosn(new Posn(i + 1, j));
              board[i + 1][j] = enemy;
            } else if (isEmpty(i - 1, j)) {
              if (isHero) {
                if (heroLives == 0) {
                  return new CollisionResult(value, CollisionResult.Result.GAME_OVER);
                } else {
                  heroLives--;
                  board[i][j] = null;
                  continue;
                }
              }
              board[i][j] = null;
              board[i - 1][j] = enemy;
              enemy.setPosn(new Posn(i - 1, j));
              newEnemyRow = i - 1;
              newEnemyCol = j;
            } else if (isEmpty(i, j + 1)) {
              if (isHero) {
                if (heroLives == 0) {
                  return new CollisionResult(value, CollisionResult.Result.GAME_OVER);
                } else {
                  heroLives--;
                  board[i][j] = null;
                  continue;
                }
              }
              board[i][j] = null;
              board[i][j + 1] = enemy;
              enemy.setPosn(new Posn(i, j + 1));
              newEnemyRow = i;
              newEnemyCol = j + 1;
            } else if (isEmpty(i, j - 1)) {
              if (isHero) {
                if (heroLives == 0) {
                  return new CollisionResult(value, CollisionResult.Result.GAME_OVER);
                } else {
                  heroLives--;
                  board[i][j] = null;
                  continue;
                }
              }
              board[i][j] = null;
              enemy.setPosn(new Posn(i, j - 1));
              board[i][j - 1] = enemy;
              newEnemyRow = i;
              newEnemyCol = j - 1;
            } else {
              continue;
            }
          }
          Piece newEnemySpot = board[newEnemyRow][newEnemyCol];
          if (newEnemySpot != null) {
            if (newEnemySpot.getName().equals("Hero")) {
              if (heroLives == 0) {
                board[i][j] = null;
                enemy.setPosn(new Posn(newEnemyRow, newEnemyCol));
                board[newEnemyRow][newEnemyCol] = enemy;
                return new CollisionResult(value, CollisionResult.Result.GAME_OVER);
              } else {
                heroLives--;
                board[i][j] = null;
                continue;
              }
            }
          }
          if (newEnemySpot == null
              || newEnemySpot.getName().equals("Treasure")
              || newEnemySpot.getName().equals("Heart")) {
            board[i][j] = null;
            enemy.setPosn(new Posn(newEnemyRow, newEnemyCol));
            board[newEnemyRow][newEnemyCol] = enemy;
          }
        }
      }
    }
    return new CollisionResult(value, CollisionResult.Result.CONTINUE);
  }

  @Override
  public CollisionResult moveHeroHardMode(int drow, int dcol) {
    int oldHeroRow = heroPosition.getRow();
    int oldHeroCol = heroPosition.getCol();

    Hero hero = (Hero) board[heroPosition.getRow()][heroPosition.getCol()];

    int newRow = hero.getPosn().getRow() + drow;
    int newCol = hero.getPosn().getCol() + dcol;
    int value = 0;
    if (newRow < 0 || newRow >= row || newCol < 0 || newCol >= col) {
      return new CollisionResult(value, CollisionResult.Result.CONTINUE);
    }
    Piece newSpot = board[newRow][newCol];
    if (newSpot == null || !newSpot.getName().equals("Wall")) {
      board[hero.getPosn().getRow()][hero.getPosn().getCol()] = null;
      heroPosition = new Posn(newRow, newCol);
      hero.setPosn(heroPosition);
      board[newRow][newCol] = hero;
    }
    if (newSpot != null) {
      if (newSpot.getName().equals("Wall")) {
        try {
          hero.collide(newSpot);
        } catch (IllegalArgumentException e) {
          return new CollisionResult(value, CollisionResult.Result.CONTINUE);
        }
      }
      if (newSpot.getName().equals("Heart")) {
        heroLives++;
      }
      if (newSpot.getName().equals("Exit")) {
        heroLives = 0;
        return hero.collide(newSpot);
      }
      if (newSpot.getName().equals("Treasure")) {
        value += ((Treasure) newSpot).getValue();
      }
      if (newSpot.getName().equals("Enemy")) {
        if (heroLives == 0) {
          return hero.collide(newSpot);
        } else {
          heroLives--;
        }
      }
    }

    List<Piece> movedEnemies = new ArrayList<>();
    int heroRow = oldHeroRow;
    int heroCol = oldHeroCol;

    for (int i = 0; i < row; i++) {
      for (int j = 0; j < col; j++) {
        if (board[i][j] != null
            && board[i][j].getName().equals("Enemy")
            && !movedEnemies.contains(board[i][j])) {
          Enemy enemy = (Enemy) board[i][j];
          movedEnemies.add(enemy);

          int newEnemyRow = i;
          int newEnemyCol = j;

          int rowDist = Math.abs(heroRow - i);
          int colDist = Math.abs(heroCol - j);

          if (rowDist > colDist) {
            if (heroRow < i) {
              newEnemyRow = i - 1;
            } else {
              newEnemyRow = i + 1;
            }
          } else {
            if (heroCol < j) {
              newEnemyCol = j - 1;
            } else if (heroCol > j) {
              newEnemyCol = j + 1;
            }
          }

          if (newEnemyRow < 0
              || newEnemyRow >= row
              || newEnemyCol < 0
              || newEnemyCol >= col
              || (board[newEnemyRow][newEnemyCol] != null
                  && (board[newEnemyRow][newEnemyCol].getName().equals("Wall")
                      || board[newEnemyRow][newEnemyCol].getName().equals("Exit")
                      || board[newEnemyRow][newEnemyCol].getName().equals("Enemy")))) {
            if (heroRow >= i && heroCol >= j) {
              if (isEmpty(i + 1, j)) {
                if (isHero) {
                  if (heroLives == 0) {
                    return new CollisionResult(value, CollisionResult.Result.GAME_OVER);
                  } else {
                    heroLives--;
                    board[i][j] = null;
                    continue;
                  }
                }
                board[i][j] = null;
                newEnemyRow = i + 1;
                newEnemyCol = j;
                enemy.setPosn(new Posn(i + 1, j));
                board[i + 1][j] = enemy;
              } else if (isEmpty(i, j + 1)) {
                if (isHero) {
                  if (heroLives == 0) {
                    return new CollisionResult(value, CollisionResult.Result.GAME_OVER);
                  } else {
                    heroLives--;
                    board[i][j] = null;
                    continue;
                  }
                }
                board[i][j] = null;
                newEnemyRow = i;
                newEnemyCol = j + 1;
                enemy.setPosn(new Posn(i, j + 1));
                board[i][j + 1] = enemy;
              } else {
                continue;
              }
            } else if (heroRow <= i && heroCol >= j) {
              if (isEmpty(i - 1, j)) {
                if (isHero) {
                  if (heroLives == 0) {
                    return new CollisionResult(value, CollisionResult.Result.GAME_OVER);
                  } else {
                    heroLives--;
                    board[i][j] = null;
                    continue;
                  }
                }
                board[i][j] = null;
                newEnemyRow = i - 1;
                newEnemyCol = j;
                enemy.setPosn(new Posn(i - 1, j));
                board[i - 1][j] = enemy;
              } else if (isEmpty(i, j + 1)) {
                if (isHero) {
                  if (heroLives == 0) {
                    return new CollisionResult(value, CollisionResult.Result.GAME_OVER);
                  } else {
                    heroLives--;
                    board[i][j] = null;
                    continue;
                  }
                }
                board[i][j] = null;
                newEnemyRow = i;
                newEnemyCol = j + 1;
                enemy.setPosn(new Posn(i, j + 1));
                board[i][j + 1] = enemy;
              } else {
                continue;
              }
            } else if (heroRow >= i && heroCol <= j) {
              if (isEmpty(i + 1, j)) {
                if (isHero) {
                  if (heroLives == 0) {
                    return new CollisionResult(value, CollisionResult.Result.GAME_OVER);
                  } else {
                    heroLives--;
                    board[i][j] = null;
                    continue;
                  }
                }
                board[i][j] = null;
                newEnemyRow = i + 1;
                newEnemyCol = j;
                enemy.setPosn(new Posn(i + 1, j));
                board[i + 1][j] = enemy;
              } else if (isEmpty(i, j - 1)) {
                if (isHero) {
                  if (heroLives == 0) {
                    return new CollisionResult(value, CollisionResult.Result.GAME_OVER);
                  } else {
                    heroLives--;
                    board[i][j] = null;
                    continue;
                  }
                }
                board[i][j] = null;
                newEnemyRow = i;
                newEnemyCol = j - 1;
                enemy.setPosn(new Posn(i, j - 1));
                board[i][j - 1] = enemy;
              } else {
                continue;
              }
            } else if (heroRow <= i && heroCol <= j) {
              if (isEmpty(i - 1, j)) {
                if (isHero) {
                  if (heroLives == 0) {
                    return new CollisionResult(value, CollisionResult.Result.GAME_OVER);
                  } else {
                    heroLives--;
                    board[i][j] = null;
                    continue;
                  }
                }
                board[i][j] = null;
                newEnemyRow = i - 1;
                newEnemyCol = j;
                enemy.setPosn(new Posn(i - 1, j));
                board[i - 1][j] = enemy;
              } else if (isEmpty(i, j - 1)) {
                if (isHero) {
                  if (heroLives == 0) {
                    return new CollisionResult(value, CollisionResult.Result.GAME_OVER);
                  } else {
                    heroLives--;
                    board[i][j] = null;
                    continue;
                  }
                }
                board[i][j] = null;
                newEnemyRow = i;
                newEnemyCol = j - 1;
                enemy.setPosn(new Posn(i, j - 1));
                board[i][j - 1] = enemy;
              } else {
                continue;
              }
            } else {
              continue;
            }
          }
          Piece newEnemySpot = board[newEnemyRow][newEnemyCol];
          if (newEnemySpot != null) {
            if (newEnemySpot.getName().equals("Hero")) {
              if (heroLives == 0) {
                board[i][j] = null;
                enemy.setPosn(new Posn(newEnemyRow, newEnemyCol));
                board[newEnemyRow][newEnemyCol] = enemy;
                return new CollisionResult(value, CollisionResult.Result.GAME_OVER);
              } else {
                heroLives--;
                board[i][j] = null;
                continue;
              }
            }
          }
          if (newEnemySpot == null
              || newEnemySpot.getName().equals("Treasure")
              || newEnemySpot.getName().equals("Heart")) {
            board[i][j] = null;
            enemy.setPosn(new Posn(newEnemyRow, newEnemyCol));
            board[newEnemyRow][newEnemyCol] = enemy;
          }
        }
      }
    }
    return new CollisionResult(value, CollisionResult.Result.CONTINUE);
  }
}
