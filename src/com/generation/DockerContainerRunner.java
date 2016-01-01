package com.generation;

import java.util.ArrayList;

public class DockerContainerRunner implements Executor {
    
    public void execute() {
        executeScript();
    }
    
    private void executeScript() {
        
        ArrayList<String> command = new ArrayList<String>();
        command.add(Constants.SHELL);
        command.add(Constants.DOCKER_SCRIPT);
        
        Util.executeProcess(command, true);
    }

    public static void main(String[] args) {
        DockerContainerRunner dockerContainerRunning = new DockerContainerRunner();
        dockerContainerRunning.execute();
    }
}
