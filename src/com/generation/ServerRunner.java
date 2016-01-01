package com.generation;

import java.util.ArrayList;

public class ServerRunner implements Executor {
    private String serviceName;
    private Process serverProcess;
    
    public ServerRunner(String serviceName) {
        this.serviceName = serviceName;
    }
    
    public void execute() {
        String java = Constants.JAVA_PATH;
        String classPath = Constants.SERVER_RUNNER_CLASS_PATH;
        String classFile = Constants.PACKAGE + this.serviceName + Constants.SERVER_CLASS_FILE_EXTENSION;
        
        ArrayList<String> command = new ArrayList<String>();
        command.add(java);
        command.add("-cp");
        command.add(classPath);
        command.add(classFile);
        
        this.serverProcess = Util.executeProcess(command, false);
    }
    
    public void shutdown() {
        this.serverProcess.destroy();
    }
    
    public static void main(String[] args) {
        ServerRunner serverRunner = new ServerRunner("AddressBook");
        serverRunner.execute();
    }
}
