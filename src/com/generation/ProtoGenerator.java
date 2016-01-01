package com.generation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProtoGenerator implements Generator {
	private String serviceName;
	private String serviceNameLower;
	private String resourceName;
	private List<Pair<String,String>> parameters;
	
	public ProtoGenerator(String serviceName, String resourceName, List<Pair<String,String>> parameters) {
		this.serviceName = serviceName;
		this.serviceNameLower = Util.upperCamelToLowerUnderscore(serviceName);
		this.resourceName = resourceName;
		this.parameters = Util.convertToProto(parameters);
	}
	
	public void clear() { 
	    // delete the generated proto file
	}
	
	public void generate() {
		Map<String,String> tokens = new HashMap<String,String>();
        tokens.put("service", this.serviceName);
        tokens.put("service_lower_case", this.serviceName.toLowerCase());
        tokens.put("resource", this.resourceName);
        tokens.put("resource_lower_case", this.resourceName.toLowerCase());
	    
        String protoSnippet = Util.readFromFile(Constants.PROTO_SNIP);
        String protoSnippetExpanded = Util.expandTemplate(protoSnippet, tokens);
		String resourceDefinition = getResourceDefinition();
		String resourceIdentifier = getResourceIdentifier();
		
		String proto = String.format(
		        protoSnippetExpanded, 
				resourceDefinition,
                resourceIdentifier,
                resourceIdentifier
				);
		
		Util.writeToFile(proto, Constants.PROTO_FILE_PATH + this.serviceNameLower + Constants.PROTO_EXTENSION);
	}
	
	private String getResourceIdentifier() {
	    String resourceId = this.parameters.get(0).getRight() + " " + this.parameters.get(0).getLeft();
	    return resourceId;
	}
	
	private String getResourceDefinition() {
	    StringBuilder sb = new StringBuilder();
	    int count = 1;
	    for (Pair<String,String> pair : this.parameters) {
	        String field = pair.getRight() + " " + pair.getLeft().toLowerCase() + " = " + String.valueOf(count) + ";";
	        sb.append(field);
	        if (count != this.parameters.size()) {
    	        sb.append("\n");
    	        sb.append("\t");
	        }
	        
	        count++;
	    }
	    
	    return sb.toString();
	}
	
	public static void main(String[] args) {
	    ArrayList<Pair<String, String>> parameters = new ArrayList<Pair<String, String>>();
	    parameters.add(new Pair<String,String>("id", "number"));
	    parameters.add(new Pair<String,String>("name", "string"));
	    parameters.add(new Pair<String,String>("email", "string"));
		
	    ProtoGenerator generator = new ProtoGenerator("AddressBook", "Person", parameters);
		generator.generate();
	}
}
