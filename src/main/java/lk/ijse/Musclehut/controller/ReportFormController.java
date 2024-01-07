package lk.ijse.Musclehut.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.Musclehut.db.DbConnection;
import lk.ijse.Musclehut.model.IncomeReport;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;


import java.io.InputStream;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ReportFormController implements Initializable {
    @FXML
    private TableColumn<IncomeReport, String> colCost;

    @FXML
    private TableColumn<IncomeReport, String> colExerciseSoldQty;

    @FXML
    private TableColumn<IncomeReport, String> colMonth;

    @FXML
    private TableColumn<IncomeReport, String> colScheduleCount;

    @FXML
    private TableView<IncomeReport> tblReport;

        @Override
        public void initialize(URL location, ResourceBundle resourceBundle) {
            colMonth.setCellValueFactory(new PropertyValueFactory<>("date"));
            colScheduleCount.setCellValueFactory(new PropertyValueFactory<>("numOfShedule"));
            colExerciseSoldQty.setCellValueFactory(new PropertyValueFactory<>("numberOfSoldExercise"));
            colCost.setCellValueFactory(new PropertyValueFactory<>("finalCost"));
            try {
                tblReport.setItems(loadMonthlyIncomeReport());
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }


        private ObservableList<IncomeReport> loadMonthlyIncomeReport() throws SQLException, ClassNotFoundException {
            ResultSet resultSet = DbConnection.getInstance().getConnection().prepareStatement(
                    "SELECT EXTRACT(MONTH FROM s.date) AS month, " +
                            "COUNT(sd.s_id) AS numOfShedule, " +
                            "SUM(sd.count) AS numberOfSoldExercise, " +
                            "SUM(sd.count * e.price) AS finalCost " +
                            "FROM schedule s " +
                            "JOIN schedule_detail sd ON s.s_id = sd.s_id " +
                            "JOIN exercise e ON sd.code = e.code " +
                            "GROUP BY EXTRACT(MONTH FROM s.date)"
            ).executeQuery();
            ObservableList<IncomeReport> tempo = FXCollections.observableArrayList();

            while (resultSet.next()) {
                String date = resultSet.getString("month");
                int numOfShedule = resultSet.getInt("numOfShedule");
                int numberOfSoldExercise = resultSet.getInt("numberOfSoldExercise");
                double finalCost = resultSet.getDouble("finalCost");

                tempo.add(new IncomeReport(date, numOfShedule, numberOfSoldExercise, finalCost));
            }
            return tempo;
        }

    @FXML
    void btnPrintReportOnAction(ActionEvent event) throws SQLException, JRException {
        InputStream resourceAsStream = getClass().getResourceAsStream("/report/PayShedule.jrxml");
        JasperDesign load = JRXmlLoader.load(resourceAsStream);

        JRDesignQuery jrDesignQuery = new JRDesignQuery();
        jrDesignQuery.setText("SELECT * FROM schedule_detail");
        load.setQuery(jrDesignQuery);

        JasperReport jasperReport = JasperCompileManager.compileReport(load);
        JasperPrint jasperPrint = JasperFillManager.fillReport(
                jasperReport,
                null,
                DbConnection.getInstance().getConnection()
        );
        JasperViewer.viewReport(jasperPrint,false);
    }
}
