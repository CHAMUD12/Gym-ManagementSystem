package lk.ijse.Musclehut.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.Musclehut.dto.EmployeeDto;
import lk.ijse.Musclehut.dto.MemberDto;
import lk.ijse.Musclehut.dto.tm.EmployeeTm;
import lk.ijse.Musclehut.model.EmployeeModel;
import lk.ijse.Musclehut.model.MemberModel;

import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;

public class EmployeeFormController {
    @FXML
    private TableColumn<?, ?> colAddress;

    @FXML
    private TableColumn<?, ?> colEmail;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private TableColumn<?, ?> colPhone;

    @FXML
    private TableColumn<?, ?> colSalary;

    @FXML
    private TableView<EmployeeTm> tblEmployee;

    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtPhone;

    @FXML
    private TextField txtSalary;

    public void initialize() {
        setCellValueFactory();
        loadAllEmployee();
    }
    private void setCellValueFactory() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colSalary.setCellValueFactory(new PropertyValueFactory<>("salary"));
    }
    private void loadAllEmployee() {
        var model = new EmployeeModel();

        ObservableList<EmployeeTm> obList = FXCollections.observableArrayList();

        try {
            List<EmployeeDto> dtoList = model.getAllEmployee();

            for(EmployeeDto dto : dtoList) {
                obList.add(
                        new EmployeeTm(
                                dto.getId(),
                                dto.getName(),
                                dto.getAddress(),
                                dto.getPhone(),
                                dto.getEmail(),
                                dto.getSalary()
                        )
                );
            }

            tblEmployee.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnBackOnAction(ActionEvent event) {

    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String id = txtId.getText();

        var employeeModel = new EmployeeModel();
        try {
            boolean isDeleted = employeeModel.deleteEmployee(id);

            if(isDeleted) {
                tblEmployee.refresh();
                new Alert(Alert.AlertType.CONFIRMATION, "employee deleted!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        boolean isEmployeeIDValidated = ValidateEmployee();
        boolean isEmployeeNameValidated = ValidateEmployee();
        boolean isEmployeeAddressValidated = ValidateEmployee();
        boolean isEmployeePhoneValidated = ValidateEmployee();
        boolean isEmployeeEmailValidated = ValidateEmployee();
        boolean isEmployeeSalaryValidated = ValidateEmployee();

        if (isEmployeeIDValidated && isEmployeeNameValidated && isEmployeeAddressValidated && isEmployeePhoneValidated && isEmployeeEmailValidated && isEmployeeSalaryValidated) {

            String id = txtId.getText();
            String name = txtName.getText();
            String address = txtAddress.getText();
            String phone = txtPhone.getText();
            String email = txtEmail.getText();
            String salary = txtSalary.getText();

            var dto = new EmployeeDto(id, name, address, phone, email, salary);

            var model = new EmployeeModel();
            try {
                boolean isSaved = model.saveEmployee(dto);
                if (isSaved) {
                    new Alert(Alert.AlertType.CONFIRMATION, "employee saved!").show();
                    clearFields();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }
        }
    }

    void clearFields() {
        txtId.setText("");
        txtName.setText("");
        txtAddress.setText("");
        txtPhone.setText("");
        txtEmail.setText("");
        txtSalary.setText("");
    }

    private boolean ValidateEmployee() {

        String idText = txtId.getText();
        boolean isEmployeeIDValidated = Pattern.compile("[E][0-9]{3}").matcher(idText).matches();
        if (!isEmployeeIDValidated) {
            new Alert(Alert.AlertType.ERROR, "invalid employee id!").show();
            return false;
        }

        String nameText = txtName.getText();
        boolean isEmployeeNameValidated = Pattern.compile("^[A-Za-z]+([ '-][A-Za-z]+)*$").matcher(nameText).matches();
        if (!isEmployeeNameValidated) {
            new Alert(Alert.AlertType.ERROR, "invalid employee name!").show();
            return false;
        }

        String addressText = txtAddress.getText();
        boolean isEmployeeAddressValidated = Pattern.compile("^[A-Za-z]+([ '-][A-Za-z]+)*$").matcher(addressText).matches();
        if (!isEmployeeAddressValidated) {
            new Alert(Alert.AlertType.ERROR, "invalid employee address!").show();
            return false;
        }

        String phoneText = txtPhone.getText();
        boolean isEmployeePhoneValidated = Pattern.compile("[0-9]{10}").matcher(phoneText).matches();
        if (!isEmployeePhoneValidated) {
            new Alert(Alert.AlertType.ERROR, "invalid employee Phone!").show();
            return false;
        }

        String emailText = txtEmail.getText();
        boolean isEmployeeEmailValidated = Pattern.compile("[^A-Z]+(@gmail.com)").matcher(emailText).matches();
        if (!isEmployeeEmailValidated) {
            new Alert(Alert.AlertType.ERROR, "invalid employee Email!").show();
            return false;
        }

        String salaryText = txtSalary.getText();
        boolean isEmployeeSalaryValidated = Pattern.compile("[0-9]{5}").matcher(salaryText).matches();
        if (!isEmployeeSalaryValidated) {
            new Alert(Alert.AlertType.ERROR, "invalid employee salary!").show();
            return false;
        }
        return true;
    }
    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String id = txtId.getText();
        String name = txtName.getText();
        String address = txtAddress.getText();
        String phone = txtPhone.getText();
        String email = txtEmail.getText();
        String salary = txtSalary.getText();

        var dto = new EmployeeDto(id, name, address, phone, email, salary);

        var model = new EmployeeModel();
        try {
            boolean isUpdated = model.updateEmployee(dto);
            System.out.println(isUpdated);
            if(isUpdated) {
                new Alert(Alert.AlertType.CONFIRMATION, "employee updated!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }

    }



    @FXML
    void txtSearchOnAction(ActionEvent event) {
        String id = txtId.getText();

        var model = new EmployeeModel();
        try {
            EmployeeDto dto = model.searchEmployee(id);

            if(dto != null) {
                fillFields(dto);
            } else {
                new Alert(Alert.AlertType.INFORMATION, "employee not found!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    private void fillFields(EmployeeDto dto) {
        txtId.setText(dto.getId());
        txtName.setText(dto.getName());
        txtAddress.setText(dto.getAddress());
        txtPhone.setText(dto.getPhone());
        txtEmail.setText(dto.getEmail());
        txtSalary.setText(dto.getSalary());
    }

}
