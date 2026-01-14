
# XtremO Server

<img width="1482" height="912" alt="image2" src="https://github.com/user-attachments/assets/7f42df89-72aa-4f4a-8335-3ad38eac3a78" />
<img width="1494" height="923" alt="image" src="https://github.com/user-attachments/assets/50436c3a-73ed-4e00-a925-910608d1b91f" />


XtremO Server is a multiplayer game server application built with JavaFX that provides a comprehensive dashboard for managing and monitoring online gaming sessions. The server supports real-time multiplayer Tic-Tac-Toe games with features including player authentication, game session management, chat functionality, and player statistics tracking.

## Features

- **Player Authentication**: Secure registration and login system with password hashing
- **Real-time Multiplayer Gaming**: Support for Tic-Tac-Toe games with live game state synchronization
- **Game Session Management**: Automatic session creation, management, and cleanup
- **Player Statistics**: Track wins, losses, draws, and longest winning streaks
- **Chat System**: In-game messaging and global chat capabilities
- **Server Dashboard**: JavaFX-based monitoring interface with:
  - Active player count and live match tracking
  - Real-time game statistics and charts
  - Server controls (start/stop/restart)
  - Matchmaking and chat toggles
  - Comprehensive logging system
- **Database Persistence**: MySQL database for storing players, games, moves, and scores

## Prerequisites

Before running the application, ensure you have the following installed:

- **Java Development Kit (JDK) 21** or higher
- **Apache Maven 3.6+**
- **MySQL Server 8.0+**
- **JavaFX 13** (included via Maven dependencies)

## Installation

1. **Clone the repository**:
   ```bash
   git clone <repository-url>
   cd XtremO-Server
   ```

2. **Build the project**:
   ```bash
   mvn clean install
   ```

## Database Setup

1. **Create the MySQL database**:
   ```sql
   CREATE DATABASE xtremo;
   ```

2. **Create the required tables**:

```sql

    -----------------------------------------------------
-- DROP DATABASE (SAFE RESET)
-----------------------------------------------------
DROP DATABASE IF EXISTS xtremo;
CREATE DATABASE xtremo;
USE xtremo;

-----------------------------------------------------
-- USERS
-----------------------------------------------------
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255)
        COLLATE utf8mb4_0900_ai_ci
        NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    avatar_url VARCHAR(255),
    status VARCHAR(20) NOT NULL DEFAULT 'OFFLINE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_login TIMESTAMP NULL DEFAULT NULL,

    CONSTRAINT chk_user_status
        CHECK (status IN ('ONLINE', 'OFFLINE', 'INGAME'))
);

-----------------------------------------------------
-- GAMES
-----------------------------------------------------
CREATE TABLE games (
    game_id INT AUTO_INCREMENT PRIMARY KEY,
    game_type VARCHAR(100) NOT NULL,
    player1_id INT NOT NULL,
    player2_id INT,
    winner_id INT,
    game_result VARCHAR(20) NOT NULL DEFAULT 'IN_PROGRESS',
    started_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ended_at TIMESTAMP NULL DEFAULT NULL,
    is_recorded BOOLEAN DEFAULT FALSE,
    record_file_path VARCHAR(255),

    CONSTRAINT chk_game_type
        CHECK (game_type IN ('TIC_TAC_TOE')),

    CONSTRAINT chk_game_result
        CHECK (game_result IN ('IN_PROGRESS', 'WIN', 'DRAW')),

    CONSTRAINT chk_winner_logic
        CHECK (
            (game_result = 'WIN' AND winner_id IS NOT NULL)
            OR (game_result = 'DRAW' AND winner_id IS NULL)
            OR (game_result = 'IN_PROGRESS' AND winner_id IS NULL)
        ),

    CONSTRAINT fk_game_player1
        FOREIGN KEY (player1_id) REFERENCES users(user_id),

    CONSTRAINT fk_game_player2
        FOREIGN KEY (player2_id) REFERENCES users(user_id),

    CONSTRAINT fk_game_winner
        FOREIGN KEY (winner_id) REFERENCES users(user_id)
);

-----------------------------------------------------
-- GAME MOVES
-----------------------------------------------------
CREATE TABLE game_moves (
    move_id INT AUTO_INCREMENT PRIMARY KEY,
    game_id INT NOT NULL,
    player_id INT NOT NULL,
    move_number INT NOT NULL,
    move_data JSON NOT NULL,

    CONSTRAINT uk_game_move
        UNIQUE (game_id, move_number),

    CONSTRAINT fk_move_game
        FOREIGN KEY (game_id) REFERENCES games(game_id),

    CONSTRAINT fk_move_user
        FOREIGN KEY (player_id) REFERENCES users(user_id)
);

-----------------------------------------------------
-- USER SCORES
-----------------------------------------------------
CREATE TABLE user_scores (
    score_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    game_type VARCHAR(100) NOT NULL,
    wins INT DEFAULT 0,
    losses INT DEFAULT 0,
    draws INT DEFAULT 0,
    longest_streak INT DEFAULT 0,

    CONSTRAINT uk_user_game
        UNIQUE (user_id, game_type),

    CONSTRAINT chk_score_game_type
        CHECK (game_type IN ('TIC_TAC_TOE')),

    CONSTRAINT fk_score_user
        FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-----------------------------------------------------
-- INDEXES
-----------------------------------------------------

-- USERS
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_status ON users(status);

-- GAMES
CREATE INDEX idx_games_player1 ON games(player1_id);
CREATE INDEX idx_games_player2 ON games(player2_id);
CREATE INDEX idx_games_winner ON games(winner_id);
CREATE INDEX idx_games_type ON games(game_type);

-- GAME MOVES
CREATE INDEX idx_game_moves_game ON game_moves(game_id);
CREATE INDEX idx_game_moves_player ON game_moves(player_id);

-- USER SCORES
CREATE INDEX idx_user_scores_user ON user_scores(user_id);
CREATE INDEX idx_user_scores_game_type ON user_scores(game_type);

-----------------------------------------------------
-- DUMMY DATA
-----------------------------------------------------

-- USERS
INSERT INTO users (username, password_hash, avatar_url, status)
VALUES
('Alice',   'hash1', 'https://i.pravatar.cc/150?img=1', 'ONLINE'),
('Bob',     'hash2', 'https://i.pravatar.cc/150?img=2', 'OFFLINE'),
('Charlie', 'hash3', 'https://i.pravatar.cc/150?img=3', 'INGAME'),
('Diana',   'hash4', 'https://i.pravatar.cc/150?img=4', 'OFFLINE'),
('Eve',     'hash5', 'https://i.pravatar.cc/150?img=5', 'ONLINE');

-- GAMES
INSERT INTO games (game_type, player1_id, player2_id, winner_id, game_result, is_recorded, record_file_path)
VALUES
('TIC_TAC_TOE', 1, 2, 1, 'WIN', TRUE,  '/records/game1.json'),
('TIC_TAC_TOE', 3, 4, 4, 'WIN', FALSE, NULL),
('TIC_TAC_TOE', 2, 5, 5, 'WIN', TRUE,  '/records/game3.json'),
('TIC_TAC_TOE', 1, 3, NULL, 'DRAW', FALSE, NULL);

-- GAME MOVES
INSERT INTO game_moves (game_id, player_id, move_number, move_data)
VALUES
(1, 1, 1, '{"cell":"A1"}'),
(1, 2, 2, '{"cell":"B2"}'),
(1, 1, 3, '{"cell":"A2"}'),
(2, 3, 1, '{"cell":"A1"}'),
(2, 4, 2, '{"cell":"B1"}'),
(3, 2, 1, '{"cell":"C1"}'),
(3, 5, 2, '{"cell":"C2"}'),
(4, 1, 1, '{"cell":"A1"}'),
(4, 3, 2, '{"cell":"B2"}'),
(4, 1, 3, '{"cell":"C1"}'),
(4, 3, 4, '{"cell":"A2"}'); -- example moves for draw

-- USER SCORES
INSERT INTO user_scores (user_id, game_type, wins, losses, draws, longest_streak)
VALUES
(1, 'TIC_TAC_TOE', 5, 2, 1, 3),
(2, 'TIC_TAC_TOE', 3, 4, 1, 2),
(3, 'TIC_TAC_TOE', 2, 3, 1, 1),
(4, 'TIC_TAC_TOE', 4, 1, 1, 4),
(5, 'TIC_TAC_TOE', 6, 1, 0, 5);

```

## Configuration

1. **Configure database connection**:

   Edit `src/main/resources/application.properties`:

   ```properties
   # Database configuration
   db.url=jdbc:mysql://localhost:3306/xtremo?serverTimezone=GMT%2B2
   db.username=your_mysql_username
   db.password=your_mysql_password
   ```

   Replace `your_mysql_username` and `your_mysql_password` with your MySQL credentials.

## Running the Application

### Method 1: Using Maven (Recommended)

Run the application using the JavaFX Maven plugin:

```bash
mvn clean javafx:run
```

### Method 2: Using IDE (NetBeans/IntelliJ IDEA/Eclipse)

1. **NetBeans**:
   - Right-click on the project → Run
   - Or use the predefined actions in `nbactions.xml`

2. **IntelliJ IDEA**:
   - Open the project
   - Run the `org.example.xtremo.app.App` main class

3. **Eclipse**:
   - Right-click on `App.java` → Run As → Java Application

### Method 3: Debug Mode

To run with debugging enabled:

```bash
mvn clean javafx:run@debug
```

The debugger will listen on `localhost:8000`.

## Usage

### Starting the Server

1. Launch the application using one of the methods above
2. The JavaFX dashboard window will open automatically
3. The game server starts on **port 6666** by default
4. You'll see server logs in the dashboard terminal

### Server Dashboard Features

- **Active Players**: View the number of currently connected players
- **Live Matches**: Monitor ongoing game sessions
- **Game Statistics**: View charts showing player activity and game results
- **Server Controls**: 
  - Stop/Start server functionality
  - Restart server
- **Toggles**: Enable/disable matchmaking and chat features
- **Logs**: Real-time server activity logs

### Client Connection

Game clients should connect to:
- **Host**: `localhost` (or server IP address)
- **Port**: `6666`

## Project Structure

```
XtremO-Server/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── org/example/xtremo/
│   │   │       ├── app/              # Main application entry point
│   │   │       ├── config/           # Configuration loading
│   │   │       ├── controller/       # JavaFX controllers
│   │   │       ├── dao/              # Data Access Objects
│   │   │       ├── database/         # Database connection management
│   │   │       ├── handlers/         # Request handlers
│   │   │       ├── logging/          # Logging utilities
│   │   │       ├── mapper/           # Entity-DTO mappers
│   │   │       ├── model/            # Data models (entities, DTOs, enums)
│   │   │       ├── network/          # Network server and protocol
│   │   │       ├── service/          # Business logic services
│   │   │       ├── session/          # Game session management
│   │   │       ├── ui/               # UI components and utilities
│   │   │       └── utils/            # Utility classes
│   │   └── resources/
│   │       ├── application.properties
│   │       └── org/example/xtremo/view/  # FXML views and CSS
│   └── test/                         # Unit tests
├── pom.xml                           # Maven configuration
└── README.md                         # This file
```

## Key Components

- **Server** (`network/Server.java`): Main server socket that accepts client connections
- **PlayerConnectionHandler**: Handles individual client connections and message processing
- **SessionManager**: Manages active game sessions between players
- **GameService**: Business logic for game operations
- **AuthService**: Handles player authentication and registration
- **PlayerService**: Manages player data and status

## Technologies Used

- **Java 21**: Programming language
- **JavaFX 13**: UI framework for the server dashboard
- **Maven**: Build and dependency management
- **MySQL Connector/J 9.5.0**: Database connectivity
- **Gson 2.13.2**: JSON serialization/deserialization
- **ControlsFX 11.2.3**: Enhanced JavaFX controls
- **Ikonli 12.4.0**: Icon library for JavaFX
- **JUnit 5.13.4**: Testing framework

## Team Members

- **Abdelrahman Waheed**
- **Abdullah Elsobky**
- **Wahid Qandil**
- **Mona Hamed**
- **Ali Abdulkareem**

## Troubleshooting

### Database Connection Issues

- Ensure MySQL server is running
- Verify database credentials in `application.properties`
- Check that the `xtremo` database exists
- Ensure all required tables are created

### Port Already in Use

If port 6666 is already in use:
- Stop any other application using that port
- Or modify the port in `Server.java` (line 28)

### JavaFX Runtime Issues

- Ensure JavaFX dependencies are properly downloaded via Maven
- For Java 11+, JavaFX is not included in the JDK and must be provided via Maven

## License

This project is part of an educational assignment.

## Contributing

This is a team project. For contributions, please coordinate with the team members listed above.
