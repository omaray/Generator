package com.generation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerGenerator implements Generator {
    private String serviceName;
    private String resourceName;
    private List<Pair<String,String>> parameters;
    
    public ServerGenerator(String serviceName, String resourceName, List<Pair<String,String>> parameters) {
        this.serviceName = serviceName;
        this.resourceName = resourceName;
        this.parameters = Util.convertToJava(parameters);
    }
    
    public void clear() {
     // delete the generated server file
    }
    
    public void generate() {
        String serverSnippet = Util.readFromFile(Constants.SERVER_SNIP);
        Map<String,String> tokens = new HashMap<String,String>();
        tokens.put("service", this.serviceName);
        tokens.put("service_lower_case", this.serviceName.toLowerCase());
        tokens.put("resource", this.resourceName);
        tokens.put("resource_lower_case", this.resourceName.toLowerCase());
        tokens.put("get_identifier", "get" + Util.lowerUnderscoreToUpperCamel(this.parameters.get(0).getLeft().toLowerCase()));
        tokens.put("get_identifier_type", this.parameters.get(0).getRight().toLowerCase());
        tokens.put("identifier_lower_case", this.parameters.get(0).getLeft().toLowerCase());
        
        String serverSnippetExpanded = Util.expandTemplate(serverSnippet, tokens);
        Util.writeToFile(serverSnippetExpanded, Constants.SERVER_FILE_PATH + this.serviceName + Constants.SERVER_FILE_EXTENSION);
    }

    public static void main(String[] args) {
        ArrayList<Pair<String, String>> parameters = new ArrayList<Pair<String, String>>();
        parameters.add(new Pair<String,String>("id", "number"));
        parameters.add(new Pair<String,String>("name", "string"));
        parameters.add(new Pair<String,String>("email", "string"));
        
        ServerGenerator generator = new ServerGenerator("AddressBook", "Person", parameters);
        generator.generate();
    }
}
