package com.generation;

import java.io.File;
import java.util.ArrayList;

public class ClientCompiler implements Executor {    
    private String serviceName;
    
    public ClientCompiler(String serviceName) {
        this.serviceName = serviceName;
    }
    
    public void execute() {
        String javac = Constants.JAVAC_PATH;
        String clientFile = Constants.CLIENT_FILE_PATH + this.serviceName + Constants.CLIENT_FILE_EXTENSION;
        String generatedFilesPath = Constants.CLIENT_GENERATED_FILES_PATH;
        String classPath = Constants.CLIENT_COMPILER_CLASS_PATH;
        String outDir = Constants.CLIENT_OUT_DIR;
        
        ArrayList<String> command = new ArrayList<String>();
        command.add(javac);
        command.add(clientFile);
        addGeneratedFiles(command, generatedFilesPath);
        command.add("-cp");
        command.add(classPath);
        command.add("-d");
        command.add(outDir);
        
        Util.executeProcess(command, true);
    }
    
    private void addGeneratedFiles(ArrayList<String> command, String path) {
        File file = new File(path);
        for (File f: file.listFiles()) {
            command.add(f.getPath());
        }
    }
    
    public static void main(String[] args) {
        ClientCompiler clientCompiler = new ClientCompiler("AddressBook");
        clientCompiler.execute();
    }
}
