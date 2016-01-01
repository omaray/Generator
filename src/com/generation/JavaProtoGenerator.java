package com.generation;

import java.util.ArrayList;

public class JavaProtoGenerator implements Generator {
    private String protoFileName;
            
    public JavaProtoGenerator(String protoFileName) {
        this.protoFileName = protoFileName;
    }
    
    public void clear() {
        // delete all the generated java files
    }
    
    public void generate() {
        String protoc = Constants.PROTOC_PATH;
        String protoPath = Constants.PROTOC_PROTO_PATH;
        String javaOut = Constants.PROTOC_JAVA_OUT_PATH;
        String file = Constants.PROTO_FILE_PATH + this.protoFileName + Constants.PROTO_EXTENSION;
        
        ArrayList<String> command = new ArrayList<String>();
        command.add(protoc);
        command.add(protoPath);
        command.add(javaOut);
        command.add(file);
        
        Util.executeProcess(command, true);
    }
    
    public static void main(String[] args) {
        JavaProtoGenerator javaGenerator = new JavaProtoGenerator("address_book.proto");
        javaGenerator.generate();
    }
}
