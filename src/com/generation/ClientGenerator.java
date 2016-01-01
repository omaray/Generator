package com.generation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientGenerator implements Generator {
    private String serviceName;
    private String resourceName;
    private List<Pair<String,String>> parameters;
    
    public ClientGenerator(String serviceName, String resourceName, List<Pair<String,String>> parameters) {
        this.serviceName = serviceName;
        this.resourceName = resourceName;
        this.parameters = Util.convertToJava(parameters);
    }
    
    public void clear() {
        // delete the generated client file
    }
    
    public void generate() {
        String clientSnippet = Util.readFromFile(Constants.CLIENT_SNIP);
        Map<String,String> tokens = new HashMap<String,String>();
        tokens.put("service", this.serviceName);
        tokens.put("service_lower_case", this.serviceName.toLowerCase());
        tokens.put("resource", this.resourceName);
        tokens.put("resource_lower_case", this.resourceName.toLowerCase());        
        tokens.put("set_identifier", "set" + Util.lowerUnderscoreToUpperCamel(this.parameters.get(0).getLeft().toLowerCase()));
        tokens.put("get_identifier_type", this.parameters.get(0).getRight().toLowerCase());
        tokens.put("identifier_lower_case", this.parameters.get(0).getLeft().toLowerCase());
        tokens.put("parameters_list", getParametersList());
        tokens.put("set_parameters", getSetParameters());
        
        String clientSnippetExpanded = Util.expandTemplate(clientSnippet, tokens);
        Util.writeToFile(clientSnippetExpanded, Constants.CLIENT_FILE_PATH + this.serviceName + Constants.CLIENT_FILE_EXTENSION);
    }
    
    private String getParametersList() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.parameters.size(); i++) {
            Pair<String,String> param = this.parameters.get(i);
            sb.append(param.getRight());
            sb.append(" ");
            sb.append(param.getLeft());
            if (i < this.parameters.size() - 1) {
                sb.append(", ");
            }
        }
        
        return sb.toString();
    }
    
    private String getSetParameters() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.parameters.size(); i++) {
            Pair<String,String> param = this.parameters.get(i);
            sb.append("set");
            sb.append(Util.lowerUnderscoreToUpperCamel(param.getLeft()));
            sb.append("(");
            sb.append(param.getLeft());
            sb.append(")");
            if (i < this.parameters.size() - 1) {
                sb.append(".");
            }
        }
        
        return sb.toString();
    }

    public static void main(String[] args) {
        ArrayList<Pair<String, String>> parameters = new ArrayList<Pair<String, String>>();
        parameters.add(new Pair<String,String>("id", "number"));
        parameters.add(new Pair<String,String>("name", "string"));
        parameters.add(new Pair<String,String>("email", "string"));
        
        ClientGenerator generator = new ClientGenerator("AddressBook", "Person", parameters);
        generator.generate();
    }
}
