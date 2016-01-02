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
        serviceName.setText("AddressBook");
        resourceName.setText("Person");
        StringBuilder sb = new StringBuilder();
        sb.append("id: number");
        sb.append("\n");
        sb.append("name: string");
        sb.append("\n");
        sb.append("email: string");
        resourceDefinition.setText(sb.toString());
    }

    public void setApp(GeneratorApp application){
        this.application = application;
    }
    
    public void processViewDefinition(ActionEvent event) {
        application.navigateToServiceFile(
                this.serviceName.getText(), 
                this.resourceName.getText(), 
                getParameters(this.resourceDefinition.getText()),
                GeneratorApp.FileType.Proto);
    }
    
    public void processTestService(ActionEvent event) {
        application.navigateToServiceTrial(
                this.serviceName.getText(), 
                this.resourceName.getText(), 
                getParameters(this.resourceDefinition.getText()));
    }
    
    private List<Pair<String,String>> getParameters(String text) {
        ArrayList<Pair<String,String>> parameters = new ArrayList<Pair<String,String>>();
        
        String[] fields = resourceDefinition.getText().split("\n");
        for (String field : fields) {
            String[] fieldSplit = field.split("[ ]*:[ ]*");
            Pair<String,String> parameter = new Pair<String,String>(fieldSplit[0], fieldSplit[1]);
            parameters.add(parameter);
        }
        
        return parameters;
    }
}
