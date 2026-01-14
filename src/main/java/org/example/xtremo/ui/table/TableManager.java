package org.example.xtremo.ui.table;

import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.example.xtremo.model.dto.GameDTO;

import java.util.List;

public class TableManager {

    private final TableView<GameDTO> tableView;

    public TableManager(TableView<GameDTO> tableView) {
        this.tableView = tableView;
        setupColumns();
    }

    private void setupColumns() {
        if (tableView == null) return;

        tableView.getColumns().clear();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        List<TableColumn<GameDTO, ?>> columns = TableColumnFactory.createColumns();
        tableView.getColumns().addAll(columns);

        double totalColumns = columns.size();
        for (TableColumn<?, ?> col : columns) {
            col.prefWidthProperty().bind(tableView.widthProperty().divide(totalColumns));
        }
    }

    public void setData(List<GameDTO> data) {
        if (tableView == null || data == null) return;
        tableView.getItems().setAll(FXCollections.observableArrayList(data));
    }
}
