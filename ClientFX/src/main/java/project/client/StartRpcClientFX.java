package project.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import project.client.gui.LoginController;
import project.client.gui.ProjectController;
import project.services.IProjectServices;


public class StartRpcClientFX extends Application{
    private Stage primaryStage;

    public void start(Stage primaryStage) throws Exception {
            try {
                System.out.println("In start");
                ApplicationContext factory = new ClassPathXmlApplicationContext("classpath:spring-client.xml");
                IProjectServices server = (IProjectServices) factory.getBean("projectService");
                System.out.println("Obtained a reference to remote server!");

                FXMLLoader loader = new FXMLLoader(
                        getClass().getClassLoader().getResource("Login.fxml"));
                Parent root = loader.load();


                LoginController ctrl =
                        loader.<LoginController>getController();
                ctrl.setServer(server);


                FXMLLoader cloader = new FXMLLoader(
                        getClass().getClassLoader().getResource("Main.fxml"));
                Parent croot = cloader.load();


                ProjectController projectCtrl =
                        cloader.<ProjectController>getController();
                projectCtrl.setServer(server);

                ctrl.setProjectController(projectCtrl);
                ctrl.setParent(croot);

                primaryStage.setTitle("Welcome");
                primaryStage.setScene(new Scene(root, 300, 130));
                primaryStage.show();

        } catch (Exception e) {
            System.err.println("Initialization exception" + e);
            e.printStackTrace();
            }
    }
}


