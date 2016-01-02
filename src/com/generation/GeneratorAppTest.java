package com.generation;

import java.util.ArrayList;
import java.util.List;

public class GeneratorAppTest {
    
    public static void main(String[] args) {
        DirectoryGenerator directoryManager = new DirectoryGenerator();
        directoryManager.clear();
        directoryManager.generate();
        
        // COLLECT INPUT FROM THE USER FROM THE UI
        String serviceName = "AddressBook";
        String resourceName = "Person";
        ArrayList<Pair<String, String>> parameters = new ArrayList<Pair<String, String>>();
        parameters.add(new Pair<String,String>("id", "number"));
        parameters.add(new Pair<String,String>("name", "string"));
        parameters.add(new Pair<String,String>("email", "string"));
        // COLLECT INPUT FROM THE USER FROM THE UI
        
        ProtoGenerator protoGenerator = new ProtoGenerator(serviceName, resourceName, parameters);
        protoGenerator.generate();
        
        JavaProtoGenerator javaProtoGenerator = new JavaProtoGenerator(Util.upperCamelToLowerUnderscore(serviceName));
        javaProtoGenerator.generate();
        
        GrpcProtoGenerator grpcProtoGenerator = new GrpcProtoGenerator(Util.upperCamelToLowerUnderscore(serviceName));
        grpcProtoGenerator.generate();
        
        ServerGenerator serverGenerator = new ServerGenerator(serviceName, resourceName, parameters);
        serverGenerator.generate();
        
        ClientGenerator clientGenerator = new ClientGenerator(serviceName, resourceName, parameters);
        clientGenerator.generate();
        
        ServerCompiler serverCompiler = new ServerCompiler(serviceName);
        serverCompiler.execute();
        
        ServerRunner serverRunner = new ServerRunner(serviceName);
        serverRunner.execute();
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        ClientCompiler clientCompiler = new ClientCompiler(serviceName);
        clientCompiler.execute();
        
        ClientRunner clientRunner = new ClientRunner(serviceName, "localhost", parameters);
        clientRunner.execute();
        
        ArrayList<Object> argCreate1 = new ArrayList<Object>();
        argCreate1.add(new Integer(1));
        argCreate1.add(new String("eric"));
        argCreate1.add(new String("eric@hotmail.com"));
        clientRunner.create(argCreate1);
        
        ArrayList<Object> argCreate2 = new ArrayList<Object>();
        argCreate2.add(new Integer(2));
        argCreate2.add(new String("jean"));
        argCreate2.add(new String("jean@hotmail.com"));
        clientRunner.create(argCreate2);
        
        ArrayList<Object> argCreate3 = new ArrayList<Object>();
        argCreate3.add(new Integer(3));
        argCreate3.add(new String("mark"));
        argCreate3.add(new String("mark@hotmail.com"));
        clientRunner.create(argCreate3);
        
        List<?> result2 = (List<?>)clientRunner.list(new ArrayList<Object>());
        for (Object o : result2) {
            clientRunner.printResource(o);
        }
        
        clientRunner.shutdown();
        serverRunner.shutdown();

        // Create the Dockerfile and run a container
        DockerFileGenerator dockerFileGenerator = new DockerFileGenerator(serviceName);
        dockerFileGenerator.generate();
        DockerContainerRunner dockerContainerRunner = new DockerContainerRunner();
        dockerContainerRunner.execute();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Update the client's endpoint and create a Person
        ClientRunner clientRunnerDocker = new ClientRunner(serviceName, "192.168.99.100", parameters);
        clientRunnerDocker.execute();
        ArrayList<Object> argCreateDocker = new ArrayList<Object>();
        argCreateDocker.add(new Integer(9));
        argCreateDocker.add(new String("riwa"));
        argCreateDocker.add(new String("riwa@hotmail.com"));
        clientRunnerDocker.create(argCreateDocker);
        clientRunnerDocker.shutdown();
    }
}
