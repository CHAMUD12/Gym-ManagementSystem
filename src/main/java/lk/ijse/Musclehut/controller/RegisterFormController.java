package lk.ijse.Musclehut.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.Musclehut.dto.UserDto;
import lk.ijse.Musclehut.model.UserModel;

import java.io.IOException;
import java.sql.SQLException;
import java.util.regex.Pattern;

public class RegisterFormController {

    @FXML
    private AnchorPane root;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtPw;

    @FXML
    private TextField txtUserName;

    @FXML
    void btnRegisterOnAction(ActionEvent event) {
        try {
            boolean userCheck = txtEmail.getText().equals(UserModel.getEmail(txtEmail.getText()));
            if (!userCheck) {
                if (ValidateRegister()) {
                    UserDto userDto = new UserDto();
                    userDto.setEmail(txtEmail.getText());
                    userDto.setName(txtUserName.getText());
                    userDto.setPassword(txtPw.getText());

                    boolean saved = UserModel.saveUser(userDto);
                    if (saved) {
                        new Alert(Alert.AlertType.CONFIRMATION, "user Saved").show();
                    } else {
                        new Alert(Alert.AlertType.ERROR, "user  not saved").show();
                    }
                } else {
                    new Alert(Alert.AlertType.WARNING, "Already exist ").show();
                }
            }
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

    private boolean ValidateRegister() {

        String emailText = txtEmail.getText();
        boolean isEmployeeEmailValidated = Pattern.compile("[^A-Z]+(@gmail.com)").matcher(emailText).matches();
        if (!isEmployeeEmailValidated) {
            new Alert(Alert.AlertType.ERROR, "invalid employee Email!").show();
            return false;
        }
        return true;
    }

    @FXML
    void hyperLoginHereOnAction(ActionEvent event) throws IOException {
        Parent rootNode = FXMLLoader.load(this.getClass().getResource("/view/login_form.fxml"));

        Scene scene = new Scene(rootNode);

        root.getChildren().clear();
        Stage stage = (Stage) root.getScene().getWindow();

        stage.setScene(scene);
        stage.setTitle("Login Form");
    }
}
