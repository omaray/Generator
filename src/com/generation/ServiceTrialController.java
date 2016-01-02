package com.generation;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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
    private ClientCompiler clientCompiler;
    private ClientRunner clientRunner;
    
    @FXML Label createId;
    @FXML Label deleteId;
    @FXML Label updateId;
    @FXML Label getId;
    @FXML TextField createIdValue;
    @FXML TextField deleteIdValue;
    @FXML TextField updateIdValue;
    @FXML TextField getIdValue;
    @FXML TextArea createFields;
    @FXML TextArea updateFields;
    @FXML TextArea getResult;
    @FXML TextArea listResult;

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
        
        this.clientCompiler = new ClientCompiler(serviceName);
        clientCompiler.execute();
        
        this.serverCompiler = new ServerCompiler(serviceName);
        this.serverCompiler.execute();
        
        this.serverRunner = new ServerRunner(serviceName);
        this.serverRunner.execute();
        
        createId.setText(parameters.get(0).getLeft());
        deleteId.setText(parameters.get(0).getLeft());
        updateId.setText(parameters.get(0).getLeft());
        getId.setText(parameters.get(0).getLeft());
        
        initializeFields(this.createFields);
        initializeFields(this.updateFields);
    }
    
    public void processCreate(ActionEvent event) {
        ArrayList<Object> arguments = processCreateUpdateHelper(this.createIdValue, this.createFields);
        initializeClientRunner("localhost");
        this.clientRunner.create(arguments);
    }
    
    public void processUpdate(ActionEvent event) {
        ArrayList<Object> arguments = processCreateUpdateHelper(this.updateIdValue, this.updateFields);
        initializeClientRunner("localhost");
        this.clientRunner.update(arguments);
    }

    public void processDelete(ActionEvent event) {
        ArrayList<Object> arguments = processDeleteGetHelper(this.deleteIdValue);

        initializeClientRunner("localhost");
        this.clientRunner.delete(arguments);
    }

    public void processGet(ActionEvent event) {
        ArrayList<Object> arguments = processDeleteGetHelper(this.getIdValue);

        initializeClientRunner("localhost");
        Object result = this.clientRunner.get(arguments);
        if (result != null) {
            this.getResult.setText(result.toString());
        } else {
            this.getResult.setText("sorry - no love");
        }
    }
    
    public void processList(ActionEvent event) {
        initializeClientRunner("localhost");
        Object result = this.clientRunner.list(new ArrayList<Object>());
        if (result != null) {
            this.listResult.setText(result.toString());
        } else {
            this.listResult.setText("really no love here");
        }
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
    
    private void initializeFields(TextArea textArea) {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < parameters.size(); i++) { 
            Pair<String,String> param = parameters.get(i);
            sb.append(param.getLeft() + " = ");
            sb.append("\n");
        }
        
        textArea.setText(sb.toString());
    }
    
    private void initializeClientRunner(String host) {
        if (this.clientRunner == null) {
            this.clientRunner = new ClientRunner(this.serviceName, host, this.parameters);
            clientRunner.execute();
        }
    }
    
    private ArrayList<Object> processCreateUpdateHelper(TextField idValue, TextArea actionFields) {
        ArrayList<String> fields = new ArrayList<String>();
        fields.add(idValue.getText());
        
        String[] entries = actionFields.getText().split("\n");
        for (String s : entries) {
            String[] elements = s.split("[ ]*=[ ]*");
            fields.add(elements[1]);
        }
        
        ArrayList<Object> arguments = new ArrayList<Object>();
        for (int i = 0; i < this.parameters.size(); i++) {
            Pair<String,String> param = this.parameters.get(i);
            String value = fields.get(i);
            arguments.add(Util.instantiateObjectFromUserInput(value, param.getRight()));
        }
        
        return arguments;
    }
    
    private ArrayList<Object> processDeleteGetHelper(TextField idValue) {
        String value = idValue.getText();
        Pair<String,String> param = this.parameters.get(0);
        ArrayList<Object> arguments = new ArrayList<Object>();
        arguments.add(Util.instantiateObjectFromUserInput(value, param.getRight()));
        
        return arguments;
    }
}
