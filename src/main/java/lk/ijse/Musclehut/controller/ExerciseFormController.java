package lk.ijse.Musclehut.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.Musclehut.dto.ExerciseDto;
import lk.ijse.Musclehut.dto.tm.ExerciseTm;
import lk.ijse.Musclehut.model.ExerciseModel;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class ExerciseFormController {
    @FXML
    private TableColumn<?, ?> colCode;

    @FXML
    private TableColumn<?, ?> colCountOfMonth;

    @FXML
    private TableColumn<?, ?> colDescription;

    @FXML
    private TableColumn<?, ?> colPrice;

    @FXML
    private TableColumn<?, ?> colAction;

    @FXML
    private TableView<ExerciseTm> tblExercise;

    @FXML
    private TextField txtCode;

    @FXML
    private TextField txtCountOfMonth;

    @FXML
    private TextField txtDescription;

    @FXML
    private TextField txtPrice;

    private final ExerciseModel exerciseModel = new ExerciseModel();

    public void initialize() {
        setCellValueFactory();
        loadAllExercises();
        setListener();
    }
    private void setCellValueFactory() {
        colCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("Price"));
        colCountOfMonth.setCellValueFactory(new PropertyValueFactory<>("countOfMonth"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("btn"));
    }

    private void loadAllExercises() {
        try {
            List<ExerciseDto> dtoList = exerciseModel.loadAllExercises();

            ObservableList<ExerciseTm> obList = FXCollections.observableArrayList();

            for(ExerciseDto dto : dtoList) {
                Button btn = new Button("remove");
                btn.setCursor(Cursor.HAND);

                btn.setOnAction((e) -> {
                    ButtonType yes = new ButtonType("yes", ButtonBar.ButtonData.OK_DONE);
                    ButtonType no = new ButtonType("no", ButtonBar.ButtonData.CANCEL_CLOSE);

                    Optional<ButtonType> type = new Alert(Alert.AlertType.INFORMATION, "Are you sure to remove?", yes, no).showAndWait();

                    if(type.orElse(no) == yes) {
                        int selectedIndex = tblExercise.getSelectionModel().getSelectedIndex();
                        String code = (String) colCode.getCellData(selectedIndex);

                        deleteExercise(code);
                        dtoList.remove(dto);
                        obList.remove(selectedIndex);
                        tblExercise.refresh();
                    }
                });

                var tm = new ExerciseTm(
                        dto.getCode(),
                        dto.getDescription(),
                        dto.getPrice(),
                        dto.getCountOfMonth(),
                        btn
                );
                obList.add(tm);
            }
            tblExercise.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setListener() {
        tblExercise.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    var dto = new ExerciseDto(
                            newValue.getCode(),
                            newValue.getDescription(),
                            newValue.getPrice(),
                            newValue.getCountOfMonth()
                    );
                    setFields(dto);
                });
    }

    private void setFields(ExerciseDto dto) {
        txtCode.setText(dto.getCode());
        txtDescription.setText(dto.getDescription());
        txtPrice.setText(String.valueOf(dto.getPrice()));
        txtCountOfMonth.setText(String.valueOf(dto.getCountOfMonth()));
    }

    private void clearFields() {
        txtCode.setText("");
        txtDescription.setText("");
        txtPrice.setText("");
        txtCountOfMonth.setText("");
    }

    private void deleteExercise(String code) {
        try {
            boolean isDeleted = exerciseModel.deleteExercise(code);
            if(isDeleted)
                new Alert(Alert.AlertType.CONFIRMATION, "exercise deleted!").show();
        } catch (SQLException ex) {
            new Alert(Alert.AlertType.ERROR, ex.getMessage()).show();
        }
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {

        boolean isExerciseCodeValidated = ValidateExercise();
        boolean isExerciseDescriptionValidated = ValidateExercise();
        boolean isExercisePriceValidated = ValidateExercise();
        boolean isExerciseCountOfMonthValidated = ValidateExercise();

        if (isExerciseCodeValidated && isExerciseDescriptionValidated && isExercisePriceValidated && isExerciseCountOfMonthValidated) {

            String code = txtCode.getText();
            String description = txtDescription.getText();
            double Price = Double.parseDouble(txtPrice.getText());
            int countOfMonth = Integer.parseInt(txtCountOfMonth.getText());

            var exerciseDto = new ExerciseDto(code, description, Price, countOfMonth);

            try {
                boolean isSaved = exerciseModel.saveExercise(exerciseDto);
                if (isSaved) {
                    new Alert(Alert.AlertType.CONFIRMATION, "exersice saved!").show();
                    clearFields();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }
        }
    }

    private boolean ValidateExercise() {

        String codeText = txtCode.getText();
        boolean isExerciseCodeValidated = Pattern.compile("[E][X][0-9]{3}").matcher(codeText).matches();
        if (!isExerciseCodeValidated) {
            new Alert(Alert.AlertType.ERROR, "invalid exercise code!").show();
            return false;
        }

        String descriptionText = txtDescription.getText();
        boolean isExerciseDescriptionValidated = Pattern.compile("^[A-Za-z]+([ '-][A-Za-z]+)*$").matcher(descriptionText).matches();
        if (!isExerciseDescriptionValidated) {
            new Alert(Alert.AlertType.ERROR, "invalid exercise description!").show();
            return false;
        }

        String priceText = txtPrice.getText();
        boolean isExercisePriceValidated = Pattern.compile("[0-9]").matcher(priceText).matches();
        if (!isExercisePriceValidated) {
            new Alert(Alert.AlertType.ERROR, "invalid exercise price!").show();
            return false;
        }

        String countOfMonthText = txtCountOfMonth.getText();
        boolean isExerciseCountOfMonthValidated = Pattern.compile("[0-9]").matcher(countOfMonthText).matches();
        if (!isExerciseCountOfMonthValidated) {
            new Alert(Alert.AlertType.ERROR, "invalid exercise countOfMonth!").show();
            return false;
        }

        return true;
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String code = txtCode.getText();
        String description = txtDescription.getText();
        double Price = Double.parseDouble(txtPrice.getText());
        int countOfMonth = Integer.parseInt(txtCountOfMonth.getText());

//        var model = new ItemModel();
        try {
            boolean isUpdated = exerciseModel.updateExercise(new ExerciseDto(code, description, Price, countOfMonth));
            if (isUpdated) {
                new Alert(Alert.AlertType.CONFIRMATION, "exersice updated").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    void codeSerachOnAction(ActionEvent event) {
        String code = txtCode.getText();

        try {
            ExerciseDto dto = exerciseModel.searchExercise(code);
            if (dto != null) {
                setFields(dto);
            } else {
                new Alert(Alert.AlertType.INFORMATION, "exercise not found!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }
}
