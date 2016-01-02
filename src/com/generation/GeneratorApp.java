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
    
    public enum PageType {
        Definition, Trial, File
    };
    
    public enum FileType {
        Proto, Client, Server
    }
    
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
        gotoServiceTrial(serviceName, resourceName, parameters, true);
    }
    
    public void navigateToServiceTrialNoGeneration() {
        gotoServiceTrial(null, null, null, false);
    }
    
    public void navigateToServiceFile(
            String serviceName, String resourceName, List<Pair<String,String>> parameters, FileType fileType) {
        gotoServiceFile(serviceName, resourceName, parameters, fileType);
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
    
    private void gotoServiceTrial(
            String serviceName, String resourceName, List<Pair<String,String>> parameters, boolean generate) {
        try {
            if (GeneratorApp.trialController == null) {
                GeneratorApp.trialController = 
                        (ServiceTrialController) replaceSceneContent("ServiceTrial.fxml", PageType.Trial);
                GeneratorApp.trialController.setApp(this);
            } else {
                updateRoot(GeneratorApp.trialPane);
            }
            
            if (generate) {
                GeneratorApp.trialController.setServiceName(serviceName);
                GeneratorApp.trialController.setResourceName(resourceName);
                GeneratorApp.trialController.setParameters(parameters);
                GeneratorApp.trialController.setup();
            }
            
        } catch (Exception ex) {
            Logger.getLogger(GeneratorApp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void gotoServiceFile(
            String serviceName, String resourceName, List<Pair<String,String>> parameters, FileType fileType) {
        try {
            if (GeneratorApp.fileController == null) {
                GeneratorApp.fileController = 
                        (ServiceFileController) replaceSceneContent("ServiceFile.fxml", PageType.File);
                GeneratorApp.fileController.setApp(this);
            } else {
                updateRoot(GeneratorApp.filePane);
            }
            
            GeneratorApp.fileController.setServiceName(serviceName);
            GeneratorApp.fileController.setResourceName(resourceName);
            GeneratorApp.fileController.setParameters(parameters);
            GeneratorApp.fileController.setFileType(fileType);
            GeneratorApp.fileController.loadFile();
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
        } else if (pageType == PageType.Trial) {
            GeneratorApp.trialPane = page;
        }else if (pageType == PageType.File) {
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
