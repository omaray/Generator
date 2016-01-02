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
    
    public static ServiceDefinitionController definitionController;
    public static ServiceTrialController trialController;
    public static ServiceFileController fileController;
    public static AnchorPane definitionPane;
    public static AnchorPane trialPane;
    public static AnchorPane filePane;
    
    private enum PageType {
        Definition, Trial, File
    };
    
    @Override 
    public void start(Stage primaryStage) throws Exception {
         primaryStage.setResizable(false);
         primaryStage.setScene(new Scene(createInitialContent()));
         primaryStage.show();
    }
    
    public void navigateToServiceDefinition(){
        gotoServiceDefinition();
    }
    
    public void navigateToServiceTrial(String serviceName, String resourceName, List<Pair<String,String>> parameters) {
        gotoServiceTrial();
    }
    
    public void navigateToServiceFile(String serviceName, String resourceName, List<Pair<String,String>> parameters) {
        gotoServiceFile(serviceName, resourceName, parameters);
    }
    
    private Parent createInitialContent() {
        gotoServiceDefinition();
        return root;
    }
    
    private void gotoServiceDefinition() {
        try {
            if (GeneratorApp.definitionController == null) {
                GeneratorApp.definitionController = 
                        (ServiceDefinitionController) replaceSceneContent("ServiceDefinition.fxml", PageType.Definition);
                GeneratorApp.definitionController.setApp(this);
            } else {
                updateRoot(GeneratorApp.definitionPane);
            }
        } catch (Exception ex) {
            Logger.getLogger(GeneratorApp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void gotoServiceTrial() {
        try {
            if (GeneratorApp.trialController == null) {
                GeneratorApp.trialController = 
                        (ServiceTrialController) replaceSceneContent("ServiceTrial.fxml", PageType.Trial);
                GeneratorApp.trialController.setApp(this);
            } else {
                updateRoot(GeneratorApp.trialPane);
            }
        } catch (Exception ex) {
            Logger.getLogger(GeneratorApp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void gotoServiceFile(String serviceName, String resourceName, List<Pair<String,String>> parameters) {
        try {
            if (GeneratorApp.fileController == null) {
                GeneratorApp.fileController = 
                        (ServiceFileController) replaceSceneContent("ServiceFile.fxml", PageType.File);
                GeneratorApp.fileController.setApp(this);
            } else {
                updateRoot(GeneratorApp.filePane);
            }
            
            fileController.setServiceName(serviceName);
            fileController.setResourceName(resourceName);
            fileController.setParameters(parameters);
            fileController.loadProtoFile();
        } catch (Exception ex) {
            Logger.getLogger(GeneratorApp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private Initializable replaceSceneContent(String fxml, PageType pageType) throws Exception {
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
        
        if (pageType == PageType.Definition) {
            GeneratorApp.definitionPane = page;
        }
        
        if (pageType == PageType.Trial) {
            GeneratorApp.trialPane = page;
        }
        
        if (pageType == PageType.File) {
            GeneratorApp.filePane = page;
        }
        
        updateRoot(page);
        return (Initializable) loader.getController();
    }
    
    private void updateRoot(AnchorPane page) {
        root.getChildren().removeAll();
        root.getChildren().clear();
        root.getChildren().addAll(page);
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
