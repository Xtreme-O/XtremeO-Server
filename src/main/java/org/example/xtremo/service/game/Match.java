/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.example.xtremo.service.game;

import java.util.Map;
import org.example.xtremo.network.ClientHandler;
import org.example.xtremo.network.protocol.Action;
import org.example.xtremo.network.protocol.DataType;
import org.example.xtremo.network.protocol.Header;
import org.example.xtremo.network.protocol.Message;
import org.example.xtremo.network.protocol.MessageType;


/**
 *
 * @author Elsobky
 */
public class Match {

    private static final char EMPTY = '-';
    private static final char X = 'X';
    private static final char O = 'O';

    private final ClientHandler playerX;
    private final ClientHandler playerO;
    private final char[][] board = new char[3][3];

    private ClientHandler currentTurn;
    private boolean finished = false;

    public Match(ClientHandler p1, ClientHandler p2) {
        this.playerX = p1;
        this.playerO = p2;

        initBoard();

        p1.setMatch(this);
        p2.setMatch(this);

        p1.setSymbol(X);
        p2.setSymbol(O);

        currentTurn = playerX;
    }

    private void initBoard() {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                board[i][j] = EMPTY;
    }

    public void start() {
        playerX.send(new Message(
                new Header(Action.START, MessageType.EVENT),
                Map.of(DataType.SYMBOL, X)
        ));

        playerO.send(new Message(
                new Header(Action.START, MessageType.EVENT),
                Map.of(DataType.SYMBOL, O)
        ));

        notifyTurn();
    }

    public synchronized void handleMove(ClientHandler player, int row, int col) {
        if (finished) return;

        if (player != currentTurn) {
            player.send(new Message(
                new Header(Action.ERROR, MessageType.ERROR),
                Map.of(DataType.MESSEGE, "NOT_YOUR_TURN")
            ));
            return;
        }

        if (board[row][col] != EMPTY) {
            player.send(new Message(
                new Header(Action.ERROR, MessageType.ERROR),
                Map.of(DataType.MESSEGE, "INVALID_MOVE")
            ));
            return;
        }

        board[row][col] = player.getSymbol();

        broadcast(new Message(
                new Header(Action.MOVE, MessageType.EVENT),
                Map.of(
                        DataType.ROW, row,
                        DataType.COL, col,
                        DataType.SYMBOL, player.getSymbol()
                )
        ));

        if (checkWin(player.getSymbol())) {
            finished = true;
            broadcast(new Message(
                    new Header(Action.WIN, MessageType.EVENT),
                    Map.of(DataType.SYMBOL, player.getSymbol())
            ));
            return;
        }

        if (isDraw()) {
            finished = true;
            broadcast(new Message(
                    new Header(Action.DRAW, MessageType.EVENT),
                    null
            ));
            return;
        }

        switchTurn();
    }

    private void switchTurn() {
        currentTurn = (currentTurn == playerX) ? playerO : playerX;
        notifyTurn();
    }

    private void notifyTurn() {
        currentTurn.send(new Message(
                new Header(Action.TURN, MessageType.EVENT),
                null
        ));
    }

    private void broadcast(Message msg) {
        playerX.send(msg);
        playerO.send(msg);
    }

    private boolean isDraw() {
        for (char[] row : board)
            for (char c : row)
                if (c == EMPTY) return false;
        return true;
    }

    private boolean checkWin(char s) {
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == s && board[i][1] == s && board[i][2] == s) return true;
            if (board[0][i] == s && board[1][i] == s && board[2][i] == s) return true;
        }
        return (board[0][0] == s && board[1][1] == s && board[2][2] == s)
            || (board[0][2] == s && board[1][1] == s && board[2][0] == s);
    }
}

