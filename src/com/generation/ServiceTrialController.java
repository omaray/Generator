package com.generation;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class ServiceTrialController extends AnchorPane implements Initializable {
    
    private GeneratorApp application;
    private String serviceName;
    private String resourceName;
    private List<Pair<String,String>> parameters;
    
    private boolean localMode;
    private DirectoryGenerator directoryManager;
    private ProtoGenerator protoGenerator;
    private JavaProtoGenerator javaProtoGenerator;
    private GrpcProtoGenerator grpcProtoGenerator;
    private ServerGenerator serverGenerator;
    private ClientGenerator clientGenerator;
    private ServerCompiler serverCompiler;
    private ServerRunner serverRunner;
    private ClientCompiler clientCompiler;
    private ClientRunner clientRunnerLocal;
    private ClientRunner clientRunnerDocker;
    DockerFileGenerator dockerFileGenerator;
    DockerContainerRunner dockerContainerRunner;
    
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
    @FXML Button deployButton;

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
        this.localMode = true;
        
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
        
        this.clientRunnerLocal = null;
        this.clientRunnerDocker = null;
        
        this.dockerFileGenerator = new DockerFileGenerator(serviceName);
        this.dockerContainerRunner = new DockerContainerRunner();
        
        this.serverCompiler = new ServerCompiler(serviceName);
        this.serverCompiler.execute();
        
        this.serverRunner = new ServerRunner(serviceName);
        this.serverRunner.execute();
        
        initializeControls();
    }
    
    public void processCreate(ActionEvent event) {
        ArrayList<Object> arguments = processCreateUpdateHelper(this.createIdValue, this.createFields);
        ClientRunner clientRunner = getClientRunner();
        clientRunner.create(arguments);
    }
    
    public void processUpdate(ActionEvent event) {
        ArrayList<Object> arguments = processCreateUpdateHelper(this.updateIdValue, this.updateFields);
        ClientRunner clientRunner = getClientRunner();
        clientRunner.update(arguments);
    }

    public void processDelete(ActionEvent event) {
        ArrayList<Object> arguments = processDeleteGetHelper(this.deleteIdValue);
        ClientRunner clientRunner = getClientRunner();
        clientRunner.delete(arguments);
    }

    public void processGet(ActionEvent event) {
        ArrayList<Object> arguments = processDeleteGetHelper(this.getIdValue);
        ClientRunner clientRunner = getClientRunner();
        Object result = clientRunner.get(arguments);
        if (result != null) {
            this.getResult.setText(result.toString());
        } else {
            this.getResult.setText("sorry - no love");
        }
    }
    
    public void processList(ActionEvent event) {
        ClientRunner clientRunner = getClientRunner();
        Object result = clientRunner.list(new ArrayList<Object>());
        if (result != null) {
            this.listResult.setText(result.toString());
        } else {
            this.listResult.setText("really no love here");
        }
    }
    
    public void processStop(ActionEvent event) {
        this.serverRunner.shutdown();
    }
    
    public void processDeploy(ActionEvent event) {
        this.localMode = false;
        if (this.clientRunnerLocal != null) {
            this.clientRunnerLocal.shutdown();
            this.clientRunnerLocal = null;
        }
        
        this.serverRunner.shutdown();
        this.dockerFileGenerator.generate();
        this.dockerContainerRunner.execute();
        this.deployButton.setText("Deployed!");
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
        if (this.clientRunnerLocal != null) {
            this.clientRunnerLocal.shutdown();
        }
        
        if (this.clientRunnerDocker != null) {
            this.clientRunnerDocker.shutdown();
        }
        
        this.serverRunner.shutdown();
        application.navigateToServiceDefinition();
    }
    
    private void initializeControls() {
        this.createId.setText(parameters.get(0).getLeft());
        this.deleteId.setText(parameters.get(0).getLeft());
        this.updateId.setText(parameters.get(0).getLeft());
        this.getId.setText(parameters.get(0).getLeft());
        
        this.createIdValue.setText("");
        this.updateIdValue.setText("");
        this.deleteIdValue.setText("");
        this.getIdValue.setText("");
        
        this.getResult.setText("");
        this.listResult.setText("");
        initializeFields(this.createFields);
        initializeFields(this.updateFields);
        
        this.deployButton.setText("Deploy");
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
    
    private ClientRunner getClientRunner() {
        ClientRunner clientRunner = null;
        
        if (this.localMode) {
            if (this.clientRunnerLocal == null) {
                this.clientRunnerLocal = new ClientRunner(this.serviceName, Constants.LOCALHOST, this.parameters);
                this.clientRunnerLocal.execute();
            }
            
            clientRunner = this.clientRunnerLocal;
        } else {
            if (this.clientRunnerDocker == null) {
                this.clientRunnerDocker = new ClientRunner(this.serviceName, Constants.REMOTEHOST, this.parameters);
                this.clientRunnerDocker.execute();
            }
            
            clientRunner = this.clientRunnerDocker;
        }
        
        return clientRunner;
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
