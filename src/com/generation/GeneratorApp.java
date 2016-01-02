package com.generation;

import java.io.InputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class GeneratorApp extends Application {
    private Group root = new Group();
    
    @Override 
    public void start(Stage primaryStage) throws Exception {
         primaryStage.setResizable(false);
         primaryStage.setScene(new Scene(createInitialContent()));
         primaryStage.show();
    }
    
    public void navigateToServiceDefinition(){
        gotoServiceDefinition();
    }
    
    public void navigateToServiceTrial() {
        gotoServiceTrial();
    }
    
    public void navigateToServiceProto(String serviceName, String resourceName, List<Pair<String,String>> parameters) {
        gotoServiceProto(serviceName, resourceName, parameters);
    }
    
    private Parent createInitialContent() {
        gotoServiceDefinition();
        return root;
    }
    
    private void gotoServiceDefinition() {
        try {
            ServiceDefinitionController definition = 
                    (ServiceDefinitionController) replaceSceneContent("ServiceDefinition.fxml");
            definition.setApp(this);
        } catch (Exception ex) {
            Logger.getLogger(GeneratorApp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void gotoServiceTrial() {
        try {
            ServiceTrialController trial = 
                    (ServiceTrialController) replaceSceneContent("ServiceTrial.fxml");
            trial.setApp(this);
        } catch (Exception ex) {
            Logger.getLogger(GeneratorApp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void gotoServiceProto(String serviceName, String resourceName, List<Pair<String,String>> parameters) {
        try {
            ServiceProtoController proto = 
                    (ServiceProtoController) replaceSceneContent("ServiceProto.fxml");
            proto.setApp(this);
            proto.setServiceName(serviceName);
            proto.setResourceName(resourceName);
            proto.setParameters(parameters);
            proto.loadProtoFile();
        } catch (Exception ex) {
            Logger.getLogger(GeneratorApp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private Initializable replaceSceneContent(String fxml) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        InputStream in = GeneratorApp.class.getResourceAsStream(fxml);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setLocation(GeneratorApp.class.getResource(fxml));
        AnchorPane page;
        try {
            page = (AnchorPane) loader.load(in);
        } finally {
            in.close();
        }
        
        root.getChildren().removeAll();
        root.getChildren().clear();
        root.getChildren().addAll(page);
        return (Initializable) loader.getController();
    }    

    public static void main(String[] args) {
        Application.launch(args);
    }
}
