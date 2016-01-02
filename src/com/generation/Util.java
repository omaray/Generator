package com.generation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.google.common.base.CaseFormat;

public class Util {
    
    private static final Logger logger = Logger.getLogger(Util.class.getName());
    
    // Type mapping between user input, proto, and Java
    public final static Map<String,String> userToProto;
    public final static Map<String,String> userToJava;
    public final static Map<String,String> protoToJava;
    public final static Map<String,String> javaToProto;
    public final static Map<String,Class<?>> javaToClass;
    public final static Map<String,String> javaTypeClass;
    static
    {
        userToProto = new HashMap<String,String>();
        userToProto.put("number", "int32");
        userToProto.put("string", "string");
        userToProto.put("bool", "bool");
        userToProto.put("double", "double");
        
        userToJava = new HashMap<String,String>();
        userToJava.put("number", "int");
        userToJava.put("string", "String");
        userToJava.put("bool", "boolean");
        userToJava.put("double", "double");
        
        protoToJava = new HashMap<String,String>();
        protoToJava.put("int32", "int");
        protoToJava.put("string", "String");
        protoToJava.put("bool", "boolean");
        protoToJava.put("double", "double");
        
        javaToProto = new HashMap<String,String>();
        javaToProto.put("int", "int32");
        javaToProto.put("String", "string");
        javaToProto.put("boolean", "bool");
        javaToProto.put("double", "double");
        
        javaToClass = new HashMap<String,Class<?>>();
        javaToClass.put("int", int.class);
        javaToClass.put("String", String.class);
        javaToClass.put("boolean", boolean.class);
        javaToClass.put("double", double.class);
        
        javaTypeClass = new HashMap<String,String>();
        javaTypeClass.put("int", "Integer");
        javaTypeClass.put("String", "String");
        javaTypeClass.put("boolean", "Boolean");
        javaTypeClass.put("double", "Double");
    }
    
    // Convert each parameter's type to a proto type
    public static List<Pair<String,String>> convertToProto(List<Pair<String,String>> parameters) {
        List<Pair<String,String>> newParameters = new ArrayList<Pair<String,String>>();
        for (Pair<String,String> param : parameters) {
            if (protoToJava.containsKey(param.getRight())) {
                newParameters.add(param);
            }
            else if (userToProto.containsKey(param.getRight())) {
                newParameters.add(new Pair<String,String>(param.getLeft(), userToProto.get(param.getRight())));
            }
            else if (javaToProto.containsKey(param.getRight())) {
                newParameters.add(new Pair<String,String>(param.getLeft(), javaToProto.get(param.getRight())));
            }
            else {
                logger.log(Level.SEVERE, "Type is unknown");
            }
        }
        
        return newParameters;
    }
    
    // Convert each parameter's type to a Java type
    public static List<Pair<String,String>> convertToJava(List<Pair<String,String>> parameters) {
        List<Pair<String,String>> newParameters = new ArrayList<Pair<String,String>>();
        for (Pair<String,String> param : parameters) {
            if (javaToProto.containsKey(param.getRight())) {
                newParameters.add(param);
            }
            else if (userToJava.containsKey(param.getRight())) {
                newParameters.add(new Pair<String,String>(param.getLeft(), userToJava.get(param.getRight())));
            }
            else if (protoToJava.containsKey(param.getRight())) {
                newParameters.add(new Pair<String,String>(param.getLeft(), protoToJava.get(param.getRight())));
            }
            else {
                logger.log(Level.SEVERE, "Type is unknown");
            }
        }
        
        return newParameters;
    }
    
    // Expands the template by replacing tokens with their values
    public static String expandTemplate(String template, Map<String,String> tokens) {
        String patternString = "%(" + String.join("|", tokens.keySet()) + ")%";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(template);

        StringBuffer sb = new StringBuffer();
        while(matcher.find()) {
            matcher.appendReplacement(sb, tokens.get(matcher.group(1)));
        }
        
        matcher.appendTail(sb);

        return sb.toString();
    }
    
    // Write the content to the specified file
    public static void writeToFile(String content, String fileName) {
        try {
            PrintWriter printWriter = new PrintWriter(fileName);
            BufferedWriter bufferedWriter = new BufferedWriter(printWriter);
            bufferedWriter.write(content);
            
            bufferedWriter.close();
            
        } catch(FileNotFoundException ex) {
            logger.log(Level.SEVERE, "Unable to find the file" + fileName, ex);
        } catch(IOException ex) {
            logger.log(Level.SEVERE, "Error writing to the file" + fileName, ex);
        }
    }
    
    // Read the content from the specified file
    public static String readFromFile(String fileName) {
        String line = null;
        StringBuilder sb = new StringBuilder();
        
        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
            
            bufferedReader.close();
            
        } catch(FileNotFoundException ex) {
            logger.log(Level.SEVERE, "Unable to find the file" + fileName, ex);
        } catch(IOException ex) {
            logger.log(Level.SEVERE, "Error reading the file" + fileName, ex);
        }
        
        return sb.toString();
    }
    
    // Start a new process with the given command
    public static Process executeProcess(List<String> command, boolean wait) {
        System.out.println(command.toString());
        
        ProcessBuilder pb = new ProcessBuilder(command);
        Map<String,String> env = pb.environment();
        String currentPath = env.get(Constants.PATH_ENV);
        String updatedPath = currentPath + ":" + Constants.PATH_ENV_VALUE;
        env.put(Constants.PATH_ENV, updatedPath);

        Process process = null;
        try {
            process = pb.start();
            Util.IOThreadHandler outputHandler = new IOThreadHandler(process.getErrorStream());
            outputHandler.start();
            
            if (wait) {
                Thread.sleep(1000);
                int exitValue = process.waitFor();
                System.out.println("Exit value is: " + exitValue);
                logger.info(outputHandler.getOutput());
            }
        } catch(Exception ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        
        return process;
    }
    
    // Helper class to handle error streaming in separate thread
    private static class IOThreadHandler extends Thread {
        private InputStream inputStream;
        private StringBuilder output;
        
        IOThreadHandler(InputStream inputStream) {
            this.inputStream = inputStream;
            output = new StringBuilder();
        }
        
        public void run() {
            Scanner br = null;
            try {
                br = new Scanner(new InputStreamReader(this.inputStream));
                String line = null;
                while (br.hasNextLine()) {
                    line = br.nextLine();
                    output.append(line + System.getProperty("line.separator"));
                }
            } finally {
                br.close();
            }
        }
        
        public String getOutput() {
            return output.toString();
        }
    }
    
    // Create directories helper
    public static void createDirectories(String path) {
        File file = new File(path);
        file.mkdirs();
    }
    
    // Delete directories helper
    public static void deleteDirectories(String path) {
        File file = new File(path);
        deleteDirectoriesRecursive(file);
    }
    
    private static void deleteDirectoriesRecursive(File file) {
        if (file.isDirectory()) {
            for (File f: file.listFiles()) {
                deleteDirectoriesRecursive(f);
            }
        }
        
        file.delete();
    }
    
    public static Object instantiateObjectFromUserInput(String value, String type) {
        if (type.equals("string")) {
            return value;
        } else if (type.equals("number")) {
            return Integer.parseInt(value);
        } else if (type.equals("bool")) {
            return Boolean.parseBoolean(value);
        } else if (type.equals("double")) {
            return Double.parseDouble("value");
        }
        
        return null;
    }
    
    public static String upperCamelToUpperUnderscore(String name) {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, name);
    }
    
    public static String upperCamelToLowerCamel(String name) {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, name);
    }
    
    public static String upperCamelToLowerUnderscore(String name) {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name);
    }
    
    public static String lowerUnderscoreToUpperUnderscore(String name) {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_UNDERSCORE, name);
    }
      
    public static String lowerUnderscoreToUpperCamel(String name) {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, name);
    }
      
    public static String lowerUnderscoreToLowerCamel(String name) {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, name);
    }
      
    public static String lowerCamelToUpperCamel(String name) {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, name);
    }
}
