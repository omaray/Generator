package com.generation;

import java.io.File;
import java.util.ArrayList;

public class ServerCompiler implements Executor { 
    private String serviceName;
    
    public ServerCompiler(String serviceName) {
        this.serviceName = serviceName;
    }
    
    public void execute() {
        String javac = Constants.JAVAC_PATH;
        String serverFile = Constants.SERVER_FILE_PATH + this.serviceName + Constants.SERVER_FILE_EXTENSION;
        String generatedFilesPath = Constants.SERVER_GENERATED_FILES_PATH;
        String classPath = Constants.SERVER_COMPILER_CLASS_PATH;
        String outDir = Constants.SERVER_OUT_DIR;
        
        ArrayList<String> command = new ArrayList<String>();
        command.add(javac);
        command.add(serverFile);
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
        ServerCompiler serverCompiler = new ServerCompiler("AddressBook");
        serverCompiler.execute();
    }
}
