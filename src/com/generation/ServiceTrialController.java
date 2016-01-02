package com.generation;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

public class ServiceTrialController extends AnchorPane implements Initializable {
    
    private GeneratorApp application;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO Auto-generated method stub
        
    }
    
    public void setApp(GeneratorApp application){
        this.application = application;
    }
}
