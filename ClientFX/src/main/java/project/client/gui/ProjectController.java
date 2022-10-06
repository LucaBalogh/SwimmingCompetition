package project.client.gui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import project.model.Inscriere;
import project.model.Participant;
import project.model.ProbaDTO;
import project.services.IProjectObserver;
import project.services.IProjectServices;
import project.services.ProjectException;

import java.io.Serializable;
import java.net.URL;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ProjectController extends UnicastRemoteObject implements Initializable, IProjectObserver, Serializable {
    ObservableList<ProbaDTO> modelPrr = FXCollections.observableArrayList();
    ObservableList<Inscriere> modelI = FXCollections.observableArrayList();

    private IProjectServices server;
    private Participant user;

    @FXML
    TableColumn<ProbaDTO, String> tableColumnDistanta;
    @FXML
    TableColumn<ProbaDTO, String> tableColumnStil;
    @FXML
    TableColumn<ProbaDTO, Integer> tableColumnNumber;
    @FXML
    TableView<ProbaDTO> tableViewProbaDTO;
    @FXML
    TableColumn<Inscriere, String> tableColumnParticipant;
    @FXML
    TableColumn<Inscriere, String> tableColumnProba;
    @FXML
    TableView<Inscriere> tableViewI;
    @FXML
    TextField filterText;
    @FXML
    private TextField textFieldParticipant;
    @FXML
    private TextField textFieldProba;

    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {

        tableColumnDistanta.setCellValueFactory(new PropertyValueFactory<ProbaDTO, String>("Distanta"));
        tableColumnStil.setCellValueFactory(new PropertyValueFactory<ProbaDTO,String>("Stil"));
        tableColumnNumber.setCellValueFactory(new PropertyValueFactory<ProbaDTO,Integer>("Number"));

        tableViewProbaDTO.setItems(modelPrr);

        tableColumnParticipant.setCellValueFactory(new PropertyValueFactory<Inscriere, String>("Participant"));
        tableColumnProba.setCellValueFactory(new PropertyValueFactory<Inscriere,String>("Proba"));

        tableViewI.setItems(modelI);
    }

    public void initModel(){

        List<ProbaDTO> proba = null;
        try {
            proba = server.getProbe();
        } catch (ProjectException e) {
            e.printStackTrace();
        }
        modelPrr.setAll(proba);

        Iterable<Inscriere> ins = null;
        try {
            ins = server.getInscrieri();
        } catch (ProjectException e) {
            e.printStackTrace();
        }
        List<Inscriere> iList = StreamSupport.stream(ins.spliterator(), false)
                .collect(Collectors.toList());
        modelI.setAll(iList);
    }

    public void handleSearch(ActionEvent actionEvent) {
        List<Inscriere> ins = new ArrayList<>();
        List<Inscriere> inss = null;
        if(filterText.getText().equals(""))
            initModel();
        else{
            String idFilter = "";
            idFilter = filterText.getText();
            try {
                inss = server.getByProba(idFilter);
            } catch (ProjectException e) {
                e.printStackTrace();
            }
            modelI.setAll(inss);
        }
        filterText.setText("");
    }

    public ProjectController() throws RemoteException{
        //this.server = server;
        System.out.println("Constructor ProjectController");
    }


    public ProjectController(IProjectServices server) throws RemoteException {
        this.server = server;
        System.out.println("Constructor ProjectController cu server param");
    }

    public void setServer(IProjectServices s) {
        server = s;
    }


    public void login(String id, String pass) throws ProjectException {
        Participant userL = new Participant(id, 0,pass);
        server.login(userL, this);
        user = userL;
        System.out.println("Autentificarea e ok!!!");

    }

    public void handleLogout(ActionEvent actionEvent) {
        logout();
        ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
    }


    public void handleSaveInscriere(ActionEvent actionEvent) {
        String part = textFieldParticipant.getText();
        if (part.isEmpty()) Util.showWarning("Name field is empty","Please entry a valid name!");

        String proba = textFieldProba.getText();

        if (proba.isEmpty()) Util.showWarning("Proba empty","Please fill in the proba field before saving it!");
        try {
            saveInscriere(part, proba);

        } catch (ProjectException e) {
            Util.showWarning("Communication error","Your server probably closed connection");
        }

        textFieldProba.setText("");
        textFieldParticipant.setText("");
    }

     void logout() {
        try {
            server.logout(user, this);
        } catch (ProjectException e) {
            System.out.println("Logout error " + e);
        }

    }


    public void saveInscriere(String p, String pr) throws ProjectException {
        Inscriere ins = new Inscriere(p,pr);
        server.addInscriere(ins);
    }

    public void savedInscriere(List<Inscriere> ins) throws ProjectException {
        Platform.runLater(new Runnable(){
            @Override
            public void run(){
                List<Inscriere> iList = StreamSupport.stream(ins.spliterator(), false)
                        .collect(Collectors.toList());
                modelI.setAll(iList);
            }
        });
    }

    public void savedProbe(List<ProbaDTO> pr) throws ProjectException {
        Platform.runLater(new Runnable(){
            @Override
            public void run(){
                List<ProbaDTO> prList = StreamSupport.stream(pr.spliterator(), false)
                        .collect(Collectors.toList());
                modelPrr.setAll(prList);
            }
        });
    }

    public void setUser(Participant user) {
        this.user = user;
    }
}
