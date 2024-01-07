package lk.ijse.Musclehut.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;
import lk.ijse.Musclehut.db.DbConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BarChartFormController {
    @FXML
    private AreaChart<?, ?> barChart;

    @FXML
    private AnchorPane barChartAnchorPane;

    public void initialize() throws SQLException, ClassNotFoundException {

        XYChart.Series series1 = new XYChart.Series();
        series1.setName("2023");

        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement(
                "SELECT date, COUNT(*) FROM schedule GROUP BY date");
        ResultSet rst = stm.executeQuery();
        String prevDay = "";

        while (rst.next()) {
            String date = rst.getString(1);
            int count = rst.getInt(2);
            series1.getData().add(new XYChart.Data<>(date, count));
            prevDay = date;
        }
        barChart.getData().addAll(series1);
    }

    @FXML
    void monthlyClickOnAction(ActionEvent event) {
        Navigation.adminORstaffUI("report_form", barChartAnchorPane);

    }

}
