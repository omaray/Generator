package com.generation;

public class Constants {
    
    // Tools
    public static final String SHELL = "sh";
    public static final String JAVA_PATH = "java";
    public static final String JAVAC_PATH = "javac";
    public static final String PROTOC_PATH = "/usr/local/bin/protoc";
    public static final String DOCKER = "/usr/local/bin/docker";
    public static final String DOCKER_MACHINE = "/usr/local/bin/docker-machine";
    
    // Common
    public static final String PATH_ENV = "PATH";
    public static final String LIB_PATH = "./lib";
    public static final String PACKAGE = "com.google.example.";
    public final static String PROTO_FILE_PATH = "./out/proto/";
    public static final String GENERATED_PATH = "./out/generated";
    public static final String GENERATED_FILES_PATH = "./out/generated/com/google/example";
    public static final String MAIN_FILES_PATH = "./out/main/com/google/example/";
    public static final String PATH_ENV_VALUE = "/Users/omaray/Development/google-cloud-sdk/bin:/usr/local/git/current/bin:/usr/local/bin:/usr/bin:/bin:/usr/local/sbin:/usr/sbin:/sbin:/opt/X11/bin:/Users/omaray/Development/apache-maven-3.3.3/bin";
    
    // Used mainly by ProtoGenerator
    public final static String PROTO_SNIP = "./resources/proto.snip";
    public final static String PROTO_EXTENSION = ".proto";

    // Used mainly by JavaProtoGenerator
    public static final String PROTOC_PROTO_PATH = "-I=" + PROTO_FILE_PATH; 
    public static final String PROTOC_JAVA_OUT_PATH = "--java_out=" + GENERATED_PATH;
    
    // Used mainly by GrpcProtoGenerator
    public static final String PROTOC_GRPC_PLUGIN = "--plugin=protoc-gen-grpc-java=./exe/protoc-gen-grpc-java";
    public static final String PROTOC_GRPC_PROTO_PATH = "--proto_path=" + PROTO_FILE_PATH;
    public static final String PROTOC_GRPC_JAVA_OUT_PATH = "--grpc-java_out=" + GENERATED_PATH;
    
    // Used mainly by ServerGenerator and ServerCompiler
    public final static String SERVER_SNIP = "./resources/server.snip";
    public final static String SERVER_FILE_PATH = MAIN_FILES_PATH;
    public final static String SERVER_FILE_EXTENSION = "Server.java";    
    public static final String SERVER_GENERATED_FILES_PATH = GENERATED_FILES_PATH;
    public static final String SERVER_COMPILER_CLASS_PATH = ".:./lib/*";
    public static final String SERVER_OUT_DIR = "./out/bin/server";
    
    // Used mainly by ClientGenerator and ClientCompiler
    public final static String CLIENT_SNIP = "./resources/client.snip";
    public final static String CLIENT_FILE_PATH = MAIN_FILES_PATH;
    public final static String CLIENT_FILE_EXTENSION = "Client.java";
    public static final String CLIENT_GENERATED_FILES_PATH = GENERATED_FILES_PATH;
    public static final String CLIENT_COMPILER_CLASS_PATH = ".:./lib/*";
    public static final String CLIENT_OUT_DIR = "./out/bin/client";
    
    // Used mainly by the ServerRunner
    public static final String SERVER_RUNNER_CLASS_PATH = ".:./lib/*:./out/bin/server";
    public static final String SERVER_CLASS_FILE_EXTENSION = "Server";
    
    // Used mainly by the ClientRunner
    public static final String CLIENT_CLASS_FILE_EXTENSION = "Client";
    public static final String LOCALHOST = "localhost";
    public static final String REMOTEHOST = "192.168.99.100";
    public static final int PORT = 50051;

    // Used mainly by DockerFileGenerator and DockerContainerRunner
    public static final String DOCKER_SCRIPT = "./resources/docker_script";
    public static final String DOCKERFILE_SNIP = "./resources/dockerfile.snip";
    public static final String DOCKERFILE_PATH = "./out/main/Dockerfile";
}
