# Dungeon Crawler 2D

### A 2D Grid-Based Dungeon Game in Java & JavaFX
**Tech Stack:** Java 23 | JavaFX 21 | Apache Maven | MVC Architecture

---

## Overview

Dungeon Crawler 2D is a turn-based dungeon exploration game built in Java and JavaFX. The application follows the Model-View-Controller (MVC) and Observer design patterns, keeping game rules, board state, and movement logic completely separate from the UI rendering and keyboard controls.

Players navigate procedurally generated dungeon floors filled with walls, enemies, and collectible items. The goal is to collect treasure, recover health with bonus hearts, avoid roaming enemies, and reach the exit portal to advance to deeper, progressively harder levels.

---

## Key Features

- **Procedural Level Generation**: Randomizes the placement of walls, treasures, hearts, enemies, and the exit portal on each floor while ensuring valid board boundaries and collision checks.
- **Dual-Mode Turn-Based Enemy AI**:
  - **Easy Mode**: Enemies pick random, valid adjacent tiles within the dungeon boundaries.
  - **Hard Mode**: Aggressive target-tracking movement where enemies calculate the coordinate distance to the player and move directly toward them.
- **Life & Health System**: Collectible heart items grant extra hit points, allowing players to survive an enemy collision and eliminate the enemy.
- **Session Scoring & High Scores**: Tracks points earned per floor (+50 per treasure chest) and updates high scores across playthroughs.
- **Audio Playback**: Plays a continuous dungeon theme soundtrack using JavaFX Media (`MediaPlayer`).
- **Observer-Driven UI Updates**: Uses the Observer pattern so JavaFX views update automatically in response to state changes in the model, keeping the UI cleanly decoupled from game logic.

---

## Architecture & Design Patterns

### Model-View-Controller (MVC)

- **Model**: Manages the 2D grid matrix, entity coordinates (`Posn`), player score, health, difficulty, and room progression. Encapsulates entity hierarchies (`Piece`, `APiece`, `MovablePiece`) and turn logic.
- **View**: JavaFX views (`TitleScreenView`, `GameView`, `EndView`) observe the model and render the game board and UI without directly handling game rules.
- **Controller**: Listens for keyboard input (WASD) and on-screen button presses, translating player actions into method calls on the model.

### Observer Pattern

The model implements the `Subject` interface, allowing any view implementing `Observer` to register via `addObserver(Observer o)`. When game state changes (such as player movement or collisions), the model calls `notifyObservers()`, prompting the views to re-render automatically.

### Collision Logic

When an entity moves into a target tile, the game evaluates the collision outcome:

- **Hero & Empty Tile**: Safe movement; the turn concludes normally.
- **Hero & Treasure**: Grants +50 points and collects the chest.
- **Hero & Heart**: Gains +1 extra life and collects the heart.
- **Hero & Exit**: Advances the player to the next dungeon floor (`CollisionResult.Result.NEXT_LEVEL`).
- **Hero & Enemy**: Consumes 1 bonus life to eliminate the enemy; if the player has no extra lives, triggers `Result.GAME_OVER`.
- **Enemy & Board**: Enemies consume unprotected treasures and damage the hero, but cannot move through walls or exits.

---

## Project Structure

```text
src/
|-- main/
|-- java/com/comp301/a09dungeon/
|   |-- Main.java                        # Application entry point
|   |-- controller/
|   |   |-- Controller.java              # Controller interface
|   |   `-- ControllerImpl.java          # Event-handling implementation
|   |-- model/
|   |   |-- Model.java                   # Core model contract
|   |   |-- ModelImpl.java               # Game rules and turn lifecycle
|   |   |-- Observer.java                # Observer interface
|   |   |-- Subject.java                 # Subject interface
|   |   |-- board/
|   |   |   |-- Board.java               # Board layout contract
|   |   |   |-- BoardImpl.java           # Grid matrix & movement validation
|   |   |   `-- Posn.java                # Immutable (row, col) coordinates
|   |   `-- pieces/
|   |       |-- Piece.java               # Entity contract
|   |       |-- APiece.java              # Abstract base entity
|   |       |-- MovablePiece.java        # Move / collision interface
|   |       |-- CollisionResult.java     # Collision outcome container
|   |       |-- Hero.java                # Player avatar
|   |       |-- Enemy.java               # Hostile entity
|   |       |-- Treasure.java            # Collectible point item
|   |       |-- Heart.java               # Collectible health item
|   |       |-- Wall.java                # Impassable barrier
|   |       `-- Exit.java                # Level transition gate
|   `-- view/
|       |-- AppLauncher.java             # JavaFX Application bootstrapper
|       |-- FXComponent.java             # View component interface
|       |-- View.java                    # Master view router
|       |-- TitleScreenView.java         # Start / settings menu
|       |-- GameView.java                # Main dungeon viewport
|       |-- EndView.java                 # Game-over & retry screen
|       `-- Music.java                   # Background audio controller
`-- resources/
    |-- *.jpg                            # Sprites (hero, enemy, tiles, UI buttons)
    |-- music.mp3                        # Background soundtrack
    `-- style/dungeon.css                # JavaFX styling definitions
pom.xml                                  # Maven configuration & plugins
```

---

## Getting Started

### Prerequisites

- **Java Development Kit (JDK)**: Version 21 or higher (JDK 23 recommended).
- **Apache Maven**: Version 3.8+ installed and available on PATH.

### Running the Game

#### Option 1: Via Terminal (Maven)
Compile and launch the game directly from the project root:

```bash
mvn clean javafx:run
```

#### Option 2: Via IntelliJ IDEA
1. Open the project in IntelliJ IDEA.
2. Open the **Maven** tool window on the right sidebar.
3. Expand **Plugins** -> **javafx**.
4. Double-click **javafx:run**.

---

## How to Play

### Controls

| Action | Keyboard | On-Screen Button |
| :--- | :---: | :---: |
| Move Up | W | Click Up Button (▲) |
| Move Down | S | Click Down Button (▼) |
| Move Left | A | Click Left Button (◀) |
| Move Right | D | Click Right Button (▶) |

### Game Mechanics

1. **Survive & Advance**: Step into the exit portal to progress to the next room. Each subsequent floor adds more enemies.
2. **Collect Treasure**: Pick up golden chests to boost your session score by 50 points each.
3. **Collect Hearts**: Gathering hearts grants extra life protection against enemy collisions.
4. **Avoid Death**: Colliding with an enemy without extra lives ends the expedition and records your high score.

### Difficulty Modes

- **Easy Mode**: Enemies roam randomly across valid adjoining tiles.
- **Hard Mode**: Enemy AI computes vector deltas toward the hero, actively cornering and hunting the player down.

