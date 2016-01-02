package com.generation;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class ServiceTrialController extends AnchorPane implements Initializable {
    
    private GeneratorApp application;
    private String serviceName;
    private String resourceName;
    private List<Pair<String,String>> parameters;
    
    private DirectoryGenerator directoryManager;
    private ProtoGenerator protoGenerator;
    private JavaProtoGenerator javaProtoGenerator;
    private GrpcProtoGenerator grpcProtoGenerator;
    private ServerGenerator serverGenerator;
    private ClientGenerator clientGenerator;
    private ServerCompiler serverCompiler;
    private ServerRunner serverRunner;
    
    @FXML Label createId;
    @FXML Label deleteId;
    @FXML Label updateId;
    @FXML Label getId;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO Auto-generated method stub
        
    }
    
    public void setApp(GeneratorApp application){
        this.application = application;
    }
    
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
    
    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }
    
    public void setParameters(List<Pair<String,String>> parameters) {
        this.parameters = parameters;
    }
    
    public void setup() {
        this.directoryManager = new DirectoryGenerator();
        this.directoryManager.clear();
        this.directoryManager.generate();
        
        this.protoGenerator = new ProtoGenerator(serviceName, resourceName, parameters);
        this.protoGenerator.generate();
        
        this.javaProtoGenerator = new JavaProtoGenerator(Util.upperCamelToLowerUnderscore(serviceName));
        this.javaProtoGenerator.generate();
        
        this.grpcProtoGenerator = new GrpcProtoGenerator(Util.upperCamelToLowerUnderscore(serviceName));
        this.grpcProtoGenerator.generate();
        
        this.serverGenerator = new ServerGenerator(serviceName, resourceName, parameters);
        this.serverGenerator.generate();
        
        this.clientGenerator = new ClientGenerator(serviceName, resourceName, parameters);
        this.clientGenerator.generate();
        
        this.serverCompiler = new ServerCompiler(serviceName);
        this.serverCompiler.execute();
        
        this.serverRunner = new ServerRunner(serviceName);
        this.serverRunner.execute();
        
        createId.setText(Util.lowerCamelToUpperCamel(parameters.get(0).getLeft()));
        deleteId.setText(Util.lowerCamelToUpperCamel(parameters.get(0).getLeft()));
        updateId.setText(Util.lowerCamelToUpperCamel(parameters.get(0).getLeft()));
        getId.setText(Util.lowerCamelToUpperCamel(parameters.get(0).getLeft()));
    }
    
    public void processViewClient(ActionEvent event) {
        application.navigateToServiceFile(
                this.serviceName, 
                this.resourceName, 
                this.parameters,
                GeneratorApp.FileType.Client);
    }
    
    public void processViewServer(ActionEvent event) {
        application.navigateToServiceFile(
                this.serviceName, 
                this.resourceName, 
                this.parameters,
                GeneratorApp.FileType.Server);
    }
    
    public void processReturnToDefinition(ActionEvent event) {
        this.serverRunner.shutdown();
        application.navigateToServiceDefinition();
    }
}
