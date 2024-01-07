package lk.ijse.Musclehut.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import lk.ijse.Musclehut.db.DbConnection;
import lk.ijse.Musclehut.dto.ExerciseDto;
import lk.ijse.Musclehut.dto.MemberDto;
import lk.ijse.Musclehut.dto.PayScheduleDto;
import lk.ijse.Musclehut.dto.tm.CartTm;
import lk.ijse.Musclehut.model.ExerciseModel;
import lk.ijse.Musclehut.model.MemberModel;
import lk.ijse.Musclehut.model.PayScheduleModel;
import lk.ijse.Musclehut.model.ScheduleModel;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PayScheduleFormContoller {
    private final MemberModel memberModel = new MemberModel();
    private final ExerciseModel exerciseModel = new ExerciseModel();
    private final ScheduleModel scheduleModel = new ScheduleModel();
    private final ObservableList<CartTm> obList = FXCollections.observableArrayList();
    @FXML
    private JFXButton btnAddToCart;

    @FXML
    private JFXComboBox<String> cmbExerciseCode;

    @FXML
    private JFXComboBox<String> cmbMemberId;

    @FXML
    private TableColumn<?, ?> colAction;

    @FXML
    private TableColumn<?, ?> colCount;

    @FXML
    private TableColumn<?, ?> colDescription;

    @FXML
    private TableColumn<?, ?> colExerciseCode;

    @FXML
    private TableColumn<?, ?> colPrice;

    @FXML
    private TableColumn<?, ?> colTotal;

    @FXML
    private Label lblCountOfMonth;

    @FXML
    private Label lblDescription;

    @FXML
    private Label lblMemberName;

    @FXML
    private Label lblNetTotal;

    @FXML
    private Label lblPrice;

    @FXML
    private Label lblScheduleDate;

    @FXML
    private Label lblScheduleId;

    @FXML
    private TableView<CartTm> tblScheduleCart;

    @FXML
    private TextField txtCount;

    private final PayScheduleModel payScheduleModel = new PayScheduleModel();

    public void initialize() {
        setCellValueFactory();
        generateNextScheduleId();
        setDate();
        loadMemberIds();
        loadExerciseCodes();
    }

    private void setCellValueFactory() {
        colExerciseCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colCount.setCellValueFactory(new PropertyValueFactory<>("count"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("Price"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("tot"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("btn"));
    }

    private void generateNextScheduleId() {
        try {
            String scheduleId = scheduleModel.generateNextScheduleId();
            lblScheduleId.setText(scheduleId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadExerciseCodes() {
        ObservableList<String> obList = FXCollections.observableArrayList();
        try {
            List<ExerciseDto> itemList = exerciseModel.loadAllExercises();

            for (ExerciseDto exerciseDto : itemList) {
                obList.add(exerciseDto.getCode());
            }

            cmbExerciseCode.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadMemberIds() {
        ObservableList<String> obList = FXCollections.observableArrayList();
        try {
            List<MemberDto> cusList = memberModel.loadAllMembers();

            for (MemberDto dto : cusList) {
                obList.add(dto.getId());
            }
            cmbMemberId.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setDate() {
        String date = String.valueOf(LocalDate.now());
        lblScheduleDate.setText(date);
    }


    @FXML
    void btnAddToCartOnAction(ActionEvent event) {
        String code = cmbExerciseCode.getValue();
        String description = lblDescription.getText();
        int count = Integer.parseInt(txtCount.getText());
        double Price = Double.parseDouble(lblPrice.getText());
        double total = count * Price;
        Button btn = new Button("remove");
        btn.setCursor(Cursor.HAND);

        btn.setOnAction((e) -> {
            ButtonType yes = new ButtonType("yes", ButtonBar.ButtonData.OK_DONE);
            ButtonType no = new ButtonType("no", ButtonBar.ButtonData.CANCEL_CLOSE);

            Optional<ButtonType> type = new Alert(Alert.AlertType.INFORMATION, "Are you sure to remove?", yes, no).showAndWait();

            if (type.orElse(no) == yes) {
                int index = tblScheduleCart.getSelectionModel().getSelectedIndex();
                obList.remove(index);
                tblScheduleCart.refresh();

                calculateNetTotal();
            }
        });

        for (int i = 0; i < tblScheduleCart.getItems().size(); i++) {
            if (code.equals(colExerciseCode.getCellData(i))) {
                count += (int) colCount.getCellData(i);
                total = count * Price;

                obList.get(i).setCount(count);
                obList.get(i).setTot(total);

                tblScheduleCart.refresh();
                calculateNetTotal();
                return;
            }
        }

        obList.add(new CartTm(
                code,
                description,
                count,
                Price,
                total,
                btn
        ));

        tblScheduleCart.setItems(obList);
        calculateNetTotal();
        txtCount.clear();
    }

    private void calculateNetTotal() {
        double total = 0;
        for (int i = 0; i < tblScheduleCart.getItems().size(); i++) {
            total += (double) colTotal.getCellData(i);
        }

        lblNetTotal.setText(String.valueOf(total));
    }

    @FXML
    void btnNewMemberOnAction(ActionEvent event) throws SQLException, IOException {
        Parent anchorPane = FXMLLoader.load(getClass().getResource("/view/member_form.fxml"));
        Scene scene = new Scene(anchorPane);

        Stage stage = new Stage();
        stage.setTitle("Member Manage");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();

    }

    @FXML
    void btnPlayScheduleOnAction(ActionEvent event) {
        String scheduleId = lblScheduleId.getText();
        String memId = cmbMemberId.getValue();
        LocalDate date = LocalDate.parse(lblScheduleDate.getText());

        List<CartTm> tmList = new ArrayList<>();

        for (CartTm cartTm : obList) {
            tmList.add(cartTm);
        }

        var payScheduleDto = new PayScheduleDto(
                scheduleId,
                memId,
                date,
                tmList
        );
        ObservableList<CartTm> data = tblScheduleCart.getItems();
        try {
            boolean isSuccess = payScheduleModel.paySchedule(payScheduleDto);
            if (isSuccess) {

                JasperDesign jasperDesign = JRXmlLoader.load(new File("C:\\Users\\shaki\\Downloads\\Fitness\\Muscle-Hut (3)\\Muscle-hut\\src\\main\\resources\\report\\r1.jrxml"));
                JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, new JRBeanCollectionDataSource(data));
                JasperViewer.viewReport(jasperPrint, false);
                System.out.println("Done");
                new Alert(Alert.AlertType.CONFIRMATION, "order completed!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (JRException e) {
//            throw new RuntimeException(e);
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }

    }


    @FXML
    void cmbExerciseOnAction(ActionEvent event) {
        String code = cmbExerciseCode.getValue();

        txtCount.requestFocus();

        try {
            ExerciseDto dto = exerciseModel.searchExercise(code);

            lblDescription.setText(dto.getDescription());
            lblPrice.setText(String.valueOf(dto.getPrice()));
            lblCountOfMonth.setText(String.valueOf(dto.getCountOfMonth()));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void cmbMemberOnAction(ActionEvent event) throws IOException, SQLException {
        String m_id = cmbMemberId.getValue();
        MemberDto dto = memberModel.searchMember(m_id);

        lblMemberName.setText(dto.getName());
    }

    @FXML
    void txtCountOnAction(ActionEvent event) {
        btnAddToCartOnAction(event);
    }
}
