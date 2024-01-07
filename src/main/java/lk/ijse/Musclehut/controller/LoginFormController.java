package lk.ijse.Musclehut.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.Musclehut.model.UserModel;

import java.io.IOException;
import java.sql.SQLException;

public class
LoginFormController {
    @FXML
    private AnchorPane rootNode;

    @FXML
    private TextField txtPassword;

    @FXML
    private TextField txtUserName;

    private final String adminUserName = "admin";
    private final String adminPassword = "1234";


    @FXML
    void btnLoginOnAction(ActionEvent event) throws IOException {
        String enteredUserName = txtUserName.getText();
        String enteredPassword = txtPassword.getText();
        if (enteredUserName.equals(adminUserName) && enteredPassword.equals(adminPassword)) {
            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/admin_form.fxml"));
            Scene scene = new Scene(anchorPane);
            Stage stage = (Stage) rootNode.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Admin Form");
            stage.centerOnScreen();

            stage.show();
        }
        try {
            boolean useIsExist = UserModel.isExistUser(enteredUserName, enteredPassword);
            if (useIsExist) {

                navigateToStaffWindow();
            }
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();

        }
    }

    @FXML
    void btnSignUpOnAction(ActionEvent event) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/register_form.fxml"));
        Scene scene = new Scene(anchorPane);
        Stage stage = (Stage) rootNode.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Register Form");
        stage.centerOnScreen();
        stage.show();
    }
    private void navigateToStaffWindow() throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/staff_form.fxml"));
        Scene scene = new Scene(anchorPane);
        Stage stage = (Stage) rootNode.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Staff Form");
        stage.centerOnScreen();
        stage.show();
        }

    @FXML
    void btnForgetPasswordOnAction(ActionEvent event) {

    }

}
