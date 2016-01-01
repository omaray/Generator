package com.generation;

import java.util.HashMap;
import java.util.Map;

public class DockerFileGenerator implements Generator {
    private String serviceName;
    
    public DockerFileGenerator(String serviceName) {
        this.serviceName = serviceName;
    }
    
    public void clear() {
        
    }
    
    public void generate() {
        String dockerfileSnippet = Util.readFromFile(Constants.DOCKERFILE_SNIP);
        Map<String,String> tokens = new HashMap<String,String>();
        tokens.put("service", this.serviceName);
        
        String dockerfileSnippetExpanded = Util.expandTemplate(dockerfileSnippet, tokens);
        Util.writeToFile(dockerfileSnippetExpanded, Constants.DOCKERFILE_PATH);   
    }

    public static void main(String[] args) {
        DockerFileGenerator dockerFileGenerator = new DockerFileGenerator("AddressBook");
        dockerFileGenerator.generate();
    }
}
