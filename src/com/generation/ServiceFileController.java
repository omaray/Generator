package com.generation;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.generation.GeneratorApp.FileType;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;

public class ServiceFileController extends AnchorPane implements Initializable {
    
    private GeneratorApp application;
    private String serviceName;
    private String resourceName;
    private List<Pair<String,String>> parameters;
    private GeneratorApp.FileType fileType;
    
    @FXML TextArea textArea;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
    
    public void setFileType(GeneratorApp.FileType fileType) {
        this.fileType = fileType;
    }
    
    public void processReturnBack(ActionEvent event) {
        if (this.fileType == GeneratorApp.FileType.Proto) {
            application.navigateToServiceDefinition();
        } else {
            application.navigateToServiceTrialNoGeneration();
        }
    }
    
    public void loadFile() {
        if (this.fileType == FileType.Proto) {
            GeneratorApp.fileController.loadProtoFile();
        } else if (this.fileType == FileType.Client) {
            GeneratorApp.fileController.loadClientFile();
        } else if (this.fileType == FileType.Server) {
            GeneratorApp.fileController.loadServerFile();
        }
    }
    
    private void loadProtoFile() {
        ProtoGenerator protoGenerator = new ProtoGenerator(this.serviceName, this.resourceName, this.parameters);
        protoGenerator.generate();

        String protoContent = Util.readFromFile(
                Constants.PROTO_FILE_PATH + Util.upperCamelToLowerUnderscore(serviceName) + Constants.PROTO_EXTENSION);
        
        textArea.setText(protoContent);
    }
    
    private void loadClientFile() {
        String clientContent = Util.readFromFile(
                Constants.CLIENT_FILE_PATH + this.serviceName + Constants.CLIENT_FILE_EXTENSION);
        
        textArea.setText(clientContent);
    }
    
    private void loadServerFile() {
        String serverContent = Util.readFromFile(
                Constants.SERVER_FILE_PATH + this.serviceName + Constants.SERVER_FILE_EXTENSION);
        
        textArea.setText(serverContent);
    }
}
