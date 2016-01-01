package com.generation;

import java.util.ArrayList;

public class DockerImageGenerator implements Generator {
    
    public void clear() {
        
    }
    
    public void generate() {
        executeScript();
    }
    
    private void executeScript() {
        
        ArrayList<String> command = new ArrayList<String>();
        command.add(Constants.SHELL);
        command.add(Constants.DOCKER_SCRIPT);
        
        Util.executeProcess(command, true);
    }

    public static void main(String[] args) {
        DockerImageGenerator dockerImageGenerator = new DockerImageGenerator();
        dockerImageGenerator.generate();
    }
}
