package com.generation;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class ServiceDefinitionController extends AnchorPane implements Initializable {
    
    private GeneratorApp application;
    @FXML TextField serviceName;
    @FXML TextField resourceName;
    @FXML TextArea resourceDefinition;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO Auto-generated method stub
    }

    public void setApp(GeneratorApp application){
        this.application = application;
    }
    
    public void processViewDefinition(ActionEvent event) {
        application.navigateToServiceProto(
                this.serviceName.getText(), 
                this.resourceName.getText(), 
                getParameters(this.resourceDefinition.getText()));
    }
    
    private List<Pair<String,String>> getParameters(String text) {
        // parse the TextArea here
        ArrayList<Pair<String,String>> parameters = new ArrayList<Pair<String,String>>();
        parameters.add(new Pair<String,String>("id", "number"));
        parameters.add(new Pair<String,String>("name", "string"));
        parameters.add(new Pair<String,String>("email", "string"));
        
        return parameters;
    }

}
