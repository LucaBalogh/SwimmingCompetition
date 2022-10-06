package project.client.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import project.model.Participant;
import project.services.IProjectServices;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class LoginController extends UnicastRemoteObject {

    private IProjectServices server;
    private ProjectController projectCtrl;
    private Participant crtUser;
    @FXML
    TextField user;
    @FXML
    TextField password;

    Parent mainChatParent;

    public void setServer(IProjectServices s){
        server=s;
    }

    public LoginController() throws RemoteException {

    }


    public void setParent(Parent p){
        mainChatParent=p;
    }

    public void pressLogin(ActionEvent actionEvent) {
        //Parent root;
        String nume = user.getText();
        String passwd = password.getText();
        crtUser = new Participant(nume,0, passwd);


        try{
            server.login(crtUser, projectCtrl);
           // Util.writeLog("User succesfully logged in "+crtUser.getId());
            Stage stage=new Stage();
            stage.setTitle("Project Window for " + crtUser.getName());
            stage.setScene(new Scene(mainChatParent));

            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    projectCtrl.logout();
                    System.exit(0);
                }
            });
            stage.show();
            projectCtrl.setUser(crtUser);
            projectCtrl.initModel();
            ((Node)(actionEvent.getSource())).getScene().getWindow().hide();

        }   catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Project");
                alert.setHeaderText("Authentication failure");
                alert.setContentText("Wrong username or password");
                alert.showAndWait();
            }


    }





    public void pressCancel(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void setUser(Participant user) {
        this.crtUser = user;
    }

    public void setProjectController(ProjectController projectController) {
        this.projectCtrl = projectController;
    }


}
