package org.example.xtremo.ui.table;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.TableColumn;
import org.example.xtremo.model.dto.GameDTO;

import java.util.ArrayList;
import java.util.List;

public class TableColumnFactory {

    public static List<TableColumn<GameDTO, ?>> createColumns() {
        List<TableColumn<GameDTO, ?>> columns = new ArrayList<>();

        columns.add(createGameIdColumn());
        columns.add(createGameTypeColumn());
        columns.add(createPlayer1Column());
        columns.add(createPlayer2Column());
        columns.add(createResultColumn());

        return columns;
    }

    private static TableColumn<GameDTO, Integer> createGameIdColumn() {
        TableColumn<GameDTO, Integer> col = new TableColumn<>("Game ID");
        col.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().gameId()).asObject());
        return col;
    }

    private static TableColumn<GameDTO, String> createGameTypeColumn() {
        TableColumn<GameDTO, String> col = new TableColumn<>("Type");
        col.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().gameType().name()));
        return col;
    }

    private static TableColumn<GameDTO, String> createPlayer1Column() {
        TableColumn<GameDTO, String> col = new TableColumn<>("Player 1");
        col.setCellValueFactory(cell -> new SimpleObjectProperty<>(
                cell.getValue().player1Id() + getWinnerMark(cell.getValue(), cell.getValue().player1Id())
        ));
        return col;
    }

    private static TableColumn<GameDTO, String> createPlayer2Column() {
        TableColumn<GameDTO, String> col = new TableColumn<>("Player 2");
        col.setCellValueFactory(cell -> new SimpleObjectProperty<>(
                cell.getValue().player2Id() != null
                        ? cell.getValue().player2Id() + getWinnerMark(cell.getValue(), cell.getValue().player2Id())
                        : "-"
        ));
        return col;
    }

    private static TableColumn<GameDTO, String> createResultColumn() {
        TableColumn<GameDTO, String> col = new TableColumn<>("Result");
        col.setCellValueFactory(cell -> new SimpleObjectProperty<>(
                cell.getValue().gameResult() != null ? cell.getValue().gameResult().name() : "N/A"
        ));
        return col;
    }

    private static String getWinnerMark(GameDTO game, Integer playerId) {
        return game.winnerId() != null && game.winnerId().equals(playerId) ? " (W)" : "";
    }
}
