# Dungeon Crawler 2D

[![Java](https://img.shields.io/badge/Java-23%2B-orange.svg)](https://www.oracle.com/java/)
[![JavaFX](https://img.shields.io/badge/JavaFX-21-blue.svg)](https://openjfx.io/)
[![Build Tool](https://img.shields.io/badge/Build-Maven-C71A36.svg)](https://maven.apache.org/)
[![License: MIT](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)

A dynamic, turn-based 2D dungeon crawler built in Java and JavaFX. The application strictly follows the **Model-View-Controller (MVC)** and **Observer** design patterns, cleanly decoupling gameplay rules and board algorithms from user input and graphical rendering.

---

## Table of Contents

- [Overview](#overview)
- [Key Features](#key-features)
- [Architecture & Design Patterns](#architecture--design-patterns)
  - [Model-View-Controller (MVC)](#model-view-controller-mvc)
  - [Observer Pattern](#observer-pattern)
  - [Collision State Machine](#collision-state-machine)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Build and Execution](#build-and-execution)
  - [Packaging Executable JAR](#packaging-executable-jar)
- [How to Play](#how-to-play)
  - [Controls](#controls)
  - [Game Mechanics](#game-mechanics)
- [Difficulty Modes](#difficulty-modes)

---

## Overview

In **Dungeon Crawler 2D**, players navigate a procedurally populated dungeon room full of obstacles, hostile adversaries, and hidden loot. The objective is to collect valuable treasure, collect life-extending hearts, outwit roaming enemies, and reach the exit portal to descend into deeper, progressively more dangerous dungeon levels.

---

## Key Features

- **Procedural Level Generation**: Each level randomizes the spatial placement of walls, treasures, hearts, enemies, exit portals, and the player hero within the board constraints.
- **Dynamic Turn-Based Enemy AI**: 
  - **Easy Mode**: Enemies pick non-colliding random directions within dungeon boundaries.
  - **Hard Mode**: Aggressive pathing logic where enemies actively hunt the player's coordinate position.
- **Life & Health System**: Players can discover and pick up hearts across the dungeon to withstand enemy attacks.
- **Score & High Score Tracking**: Persistent session scoring that rewards collected doubloons and tracks records across playthroughs.
- **Atmospheric Audio**: Integrated background audio track using JavaFX Media player.
- **Clean Architecture**: Complete separation of business rules (`model`) from UI rendering (`view`) and event processing (`controller`).

---

## Architecture & Design Patterns

```
                +-------------------------+
                |       Controller        |
                | (User Input Delegation) |
                +------------+------------+
                             |
                   mutates   |   queries
                             v
+------------------+  notifies  +------------------+
|       View       |<-----------|      Model       |
| (JavaFX / UI)    | (Observer) |   (Game State)   |
+------------------+            +------------------+
```

### Model-View-Controller (MVC)

1. **Model (`com.comp301.a09dungeon.model`)**:
   - Manages board matrix representation, positioning (`Posn`), score, lives, difficulty, and room progression.
   - Encapsulates game piece hierarchy (`Piece`, `APiece`, `MovablePiece`) and domain rules (hero movement, enemy turns, collisions).
2. **View (`com.comp301.a09dungeon.view`)**:
   - Driven by JavaFX components (`TitleScreenView`, `GameView`, `EndView`).
   - Purely reactive: subscribes to the model and reconstructs or repaints scene components whenever notified.
3. **Controller (`com.comp301.a09dungeon.controller`)**:
   - Receives events from buttons or keyboard keystrokes (`WASD`) and translates them into semantic commands (`moveUp()`, `moveUpHard()`, `startGame()`).

### Observer Pattern

The model implements `Subject`, allowing views implementing `Observer` to register themselves via `addObserver(Observer o)`. When a turn resolves or game state mutates, the model invokes `notifyObservers()`, decoupling the game logic from the UI lifecycle.

### Collision State Machine

Movement resolution evaluates interactions between moving entities and target grid cells using `CollisionResult`:
- **Hero & Empty Cell**: Free movement, turn proceeds.
- **Hero & Treasure**: Score increases (+50 points), piece is collected.
- **Hero & Heart**: Hero life count increments, piece is collected.
- **Hero & Exit**: Level completes, triggers `Result.NEXT_LEVEL`.
- **Hero & Enemy**: If hero has bonus lives, a life is consumed; otherwise triggers `Result.GAME_OVER`.
- **Enemy & Pieces**: Enemies consume unprotected treasures and damage the hero, but cannot step through walls or exits.

---

## Project Structure

```text
src/
├── main/
│   ├── java/com/comp301/a09dungeon/
│   │   ├── Main.java                        # Application entry point
│   │   ├── controller/
│   │   │   ├── Controller.java              # Controller interface
│   │   │   └── ControllerImpl.java          # Event-handling implementation
│   │   ├── model/
│   │   │   ├── Model.java                   # Core model contract
│   │   │   ├── ModelImpl.java               # Game rules and turn lifecycle
│   │   │   ├── Observer.java                # Observer interface
│   │   │   ├── Subject.java                 # Subject interface
│   │   │   ├── board/
│   │   │   │   ├── Board.java               # Board layout contract
│   │   │   │   ├── BoardImpl.java           # Grid matrix & movement validation
│   │   │   │   └── Posn.java                # Immutable (row, col) coordinates
│   │   │   └── pieces/
│   │   │       ├── Piece.java               # Entity contract
│   │   │       ├── APiece.java              # Abstract base entity
│   │   │       ├── MovablePiece.java        # Move/collision interface
│   │   │       ├── CollisionResult.java     # Collision outcome container
│   │   │       ├── Hero.java                # Player avatar
│   │   │       ├── Enemy.java               # Hostile entity
│   │   │       ├── Treasure.java            # Collectible point item
│   │   │       ├── Heart.java               # Collectible health item
│   │   │       ├── Wall.java                # Impassable barrier
│   │   │       └── Exit.java                # Level transition gate
│   │   └── view/
│   │       ├── AppLauncher.java             # JavaFX Application bootstrapper
│   │       ├── FXComponent.java             # View component interface
│   │       ├── View.java                    # View master router
│   │       ├── TitleScreenView.java         # Start / settings menu
│   │       ├── GameView.java                # Main dungeon viewport
│   │       ├── EndView.java                 # Game-over & retry screen
│   │       └── Music.java                   # Background audio controller
│   └── resources/
│       ├── *.jpg                            # Sprites (hero, enemy, tiles, UI buttons)
│       ├── music.mp3                        # Background soundtrack
│       └── style/dungeon.css                # JavaFX styling definitions
pom.xml                                      # Maven configuration & plugins
```

---

## Getting Started

### Prerequisites

- **Java Development Kit (JDK)**: Version 21 or higher (JDK 23 recommended).
- **Apache Maven**: Version 3.8+ installed and accessible on your `PATH`.

### Build and Execution

To compile and launch the game directly using the JavaFX Maven plugin:

```bash
mvn clean javafx:run
```

### Packaging Executable JAR

To build a standalone executable JAR containing all dependencies:

```bash
mvn clean package
```

Run the packaged artifact:

```bash
java -jar target/a09-dungeon-1.0-SNAPSHOT-jar-with-dependencies.jar
```

---

## How to Play

### Controls

| Action | Keyboard | On-Screen |
| :--- | :---: | :---: |
| Move Up | <kbd>W</kbd> | Click **▲** |
| Move Down | <kbd>S</kbd> | Click **▼** |
| Move Left | <kbd>A</kbd> | Click **◀** |
| Move Right | <kbd>D</kbd> | Click **▶** |

### Game Mechanics

1. **Survive & Advance**: Step into the exit portal to progress to the next room. Each subsequent floor adds more enemies.
2. **Collect Treasure**: Pick up golden chests to boost your session score by 50 points each.
3. **Collect Hearts**: Gathering hearts grants extra life protection against enemy collisions.
4. **Avoid Death**: Colliding with an enemy without extra lives ends the expedition and records your high score.

---

## Difficulty Modes

- **Easy Mode**: Enemies roam randomly across valid adjoining tiles.
- **Hard Mode**: Enemy AI computes vector deltas toward the hero, actively cornering and hunting the player down.

---

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
