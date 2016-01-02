package com.generation;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;

public class ServiceProtoController extends AnchorPane implements Initializable {
    
    private GeneratorApp application;
    private String serviceName;
    private String resourceName;
    private List<Pair<String,String>> parameters;
    
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
    
    public void loadProtoFile() {
        ProtoGenerator protoGenerator = new ProtoGenerator(this.serviceName, this.resourceName, this.parameters);
        protoGenerator.generate();

        String protoContent = Util.readFromFile(Constants.PROTO_FILE_PATH + this.serviceName + Constants.PROTO_EXTENSION);
        textArea.setText(protoContent);
    }
    
    public void processReturnToDefinition(ActionEvent event) {
        application.navigateToServiceDefinition();
    }
}
