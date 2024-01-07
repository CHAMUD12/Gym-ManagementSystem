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
import lk.ijse.Musclehut.dto.MemberDto;
import lk.ijse.Musclehut.dto.tm.MemberTm;
import lk.ijse.Musclehut.model.MemberModel;

import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;

public class MemberFormController {
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
    private TableView<MemberTm> tblMember;

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

    public void initialize() {
        setCellValueFactory();
        loadAllCustomers();
    }
    private void setCellValueFactory() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
    }
    private void loadAllCustomers() {
        var model = new MemberModel();

        ObservableList<MemberTm> obList = FXCollections.observableArrayList();

        try {
            List<MemberDto> dtoList = model.getAllMembers();

            for(MemberDto dto : dtoList) {
                obList.add(
                        new MemberTm(
                                dto.getId(),
                                dto.getName(),
                                dto.getAddress(),
                                dto.getPhone(),
                                dto.getEmail()
                        )
                );
            }

            tblMember.setItems(obList);
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

        var memberModel = new MemberModel();
        try {
            boolean isDeleted = memberModel.deleteMember(id);

            if(isDeleted) {
                tblMember.refresh();
                new Alert(Alert.AlertType.CONFIRMATION, "customer deleted!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        boolean isMemberIDValidated = ValidateMember();
        boolean isMemberNameValidated = ValidateMember();
        boolean isMemberAddressValidated = ValidateMember();
        boolean isMemberPhoneValidated = ValidateMember();
        boolean isMemberEmailValidated = ValidateMember();

        if (isMemberIDValidated && isMemberNameValidated && isMemberAddressValidated && isMemberPhoneValidated && isMemberEmailValidated) {

            String id = txtId.getText();
            String name = txtName.getText();
            String address = txtAddress.getText();
            String phone = txtPhone.getText();
            String email = txtEmail.getText();

            var dto = new MemberDto(id, name, address, phone, email);

            var model = new MemberModel();
            try {
                boolean isSaved = model.saveMember(dto);
                if (isSaved) {
                    new Alert(Alert.AlertType.CONFIRMATION, "member saved!").show();
                    clearFields();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }
        }
    }
    private boolean ValidateMember() {

        String idText = txtId.getText();
        boolean isMemberIDValidated = Pattern.compile("[M][0-9]{3}").matcher(idText).matches();
        if (!isMemberIDValidated) {
            new Alert(Alert.AlertType.ERROR, "invalid member id!").show();
            return false;
        }

        String nameText = txtName.getText();
        boolean isMemberNameValidated = Pattern.compile("^[A-Za-z]+([ '-][A-Za-z]+)*$").matcher(nameText).matches();
        if (!isMemberNameValidated) {
            new Alert(Alert.AlertType.ERROR, "invalid member name!").show();
            return false;
        }

        String addressText = txtAddress.getText();
        boolean isMemberAddressValidated = Pattern.compile("^[A-Za-z]+([ '-][A-Za-z]+)*$").matcher(addressText).matches();
        if (!isMemberAddressValidated) {
            new Alert(Alert.AlertType.ERROR, "invalid member address!").show();
            return false;
        }

        String phoneText = txtPhone.getText();
        boolean isMemberPhoneValidated = Pattern.compile("[0-9]{10}").matcher(phoneText).matches();
        if (!isMemberPhoneValidated) {
            new Alert(Alert.AlertType.ERROR, "invalid member Phone!").show();
            return false;
        }

        String emailText = txtEmail.getText();
        boolean isMemberEmailValidated = Pattern.compile("[^A-Z]+(.com)").matcher(emailText).matches();
        if (!isMemberEmailValidated) {
            new Alert(Alert.AlertType.ERROR, "invalid member Email!").show();
            return false;
        }
        return true;
    }
    void clearFields() {
        txtId.setText("");
        txtName.setText("");
        txtAddress.setText("");
        txtPhone.setText("");
        txtEmail.setText("");
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String id = txtId.getText();
        String name = txtName.getText();
        String address = txtAddress.getText();
        String phone = txtPhone.getText();
        String email = txtEmail.getText();

        var dto = new MemberDto(id, name, address, phone, email);

        var model = new MemberModel();
        try {
            boolean isUpdated = model.updateMember(dto);
            System.out.println(isUpdated);
            if(isUpdated) {
                new Alert(Alert.AlertType.CONFIRMATION, "customer updated!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }

    }

    @FXML
    void txtSearchOnAction(ActionEvent event) {
        String id = txtId.getText();

        var model = new MemberModel();
        try {
            MemberDto dto = model.searchMember(id);

            if(dto != null) {
                fillFields(dto);
            } else {
                new Alert(Alert.AlertType.INFORMATION, "customer not found!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }

    }

    private void fillFields(MemberDto dto) {
        txtId.setText(dto.getId());
        txtName.setText(dto.getName());
        txtAddress.setText(dto.getAddress());
        txtPhone.setText(dto.getPhone());
        txtEmail.setText(dto.getEmail());
    }

}
