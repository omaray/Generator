package com.generation;

import java.util.ArrayList;

public class GrpcProtoGenerator implements Generator {
    private String protoFileName;
            
    public GrpcProtoGenerator(String protoFileName) {
        this.protoFileName = protoFileName;
    }
    
    public void clear() {
     // delete the generated grpc file
    }
    
    public void generate() {
        String protoc = Constants.PROTOC_PATH;
        String plugin = Constants.PROTOC_GRPC_PLUGIN;
        String protoPath = Constants.PROTOC_GRPC_PROTO_PATH;
        String grpcOut = Constants.PROTOC_GRPC_JAVA_OUT_PATH;
        String file = Constants.PROTO_FILE_PATH + this.protoFileName + Constants.PROTO_EXTENSION;
        
        ArrayList<String> command = new ArrayList<String>();
        command.add(protoc);
        command.add(plugin);
        command.add(protoPath);
        command.add(grpcOut);
        command.add(file);
        
        Util.executeProcess(command, true);
    }
    
    public static void main(String[] args) {
        GrpcProtoGenerator grpcGenerator = new GrpcProtoGenerator("address_book.proto");
        grpcGenerator.generate();
    }
}
