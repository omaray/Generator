package com.generation;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

public class ClientRunner implements Executor {    
    private String serviceName;
    private String clientClassName;
    private List<Pair<String,String>> parameters;
    private URLClassLoader classLoader;
    private Class<?> clientClass;
    private Object clientInstance;
    
    public ClientRunner(String serviceName, List<Pair<String,String>> parameters) {
        this.serviceName = serviceName;
        this.clientClassName = Constants.PACKAGE + this.serviceName + Constants.CLIENT_CLASS_FILE_EXTENSION;
        this.parameters = Util.convertToJava(parameters);
    }
    
    public void execute() {
        try {
            File classFile = new File(Constants.CLIENT_OUT_DIR);
            ArrayList<URL> urls = new ArrayList<URL>();
            urls.add(classFile.toURI().toURL());
            addURLFiles(urls, Constants.LIB_PATH);
            
            URL[] urlsArray = new URL[urls.size()];
            urlsArray = urls.toArray(urlsArray);
            
            this.classLoader = new URLClassLoader(urlsArray);
            this.clientClass = classLoader.loadClass(this.clientClassName);
            Constructor<?> ctor = clientClass.getConstructor(String.class, int.class);
            this.clientInstance = ctor.newInstance("localhost", Constants.PORT);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void shutdown() {
        invokeMethod("shutdown", new ArrayList<Object>());
    }
    
    public Object create(ArrayList<Object> arguments) {
        return invokeMethod("create", arguments);
    }
    
    public Object update(ArrayList<Object> arguments) {
        return invokeMethod("update", arguments);
    }
    
    public Object delete(ArrayList<Object> arguments) {
        return invokeMethod("delete", arguments);
    }
    
    public Object get(ArrayList<Object> arguments) {
        return invokeMethod("get", arguments);
    }
    
    public Object list(ArrayList<Object> arguments) {
        return invokeMethod("list", arguments);
    }
    
    public void printResource(Object resource) {
        try {
            for (Pair<String,String> param : this.parameters) {
                String getter = "get" + Util.lowerCamelToUpperCamel(param.getLeft());
                Method method = resource.getClass().getMethod(getter);
                System.out.println(method.invoke(resource));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private void addURLFiles(ArrayList<URL> urls, String path) {
        File file = new File(path);
        for (File f: file.listFiles()) {
            try {
                urls.add(f.toURI().toURL());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    
    private Object invokeMethod(String methodName, ArrayList<Object> arguments) {
        Object result = null;
        
        ArrayList<Class<?>> classTypes = new ArrayList<Class<?>>();
        for (Pair<String,String> param : this.parameters) {
            classTypes.add(Util.javaToClass.get(param.getRight()));
        }
        
        try {
            Method method = null;
            
            if (arguments.size() == 0) {
                method = this.clientClass.getMethod(methodName);
                result = method.invoke(this.clientInstance);
            } else if (arguments.size() == 1) {
                method = this.clientClass.getMethod(methodName, classTypes.get(0));
                result = method.invoke(this.clientInstance, arguments.get(0));
            } else if (arguments.size() == 2) {
                method = this.clientClass.getMethod(methodName, classTypes.get(0), classTypes.get(1));
                result = method.invoke(this.clientInstance, arguments.get(0), arguments.get(1));
            } else if (arguments.size() == 3) {
                method = this.clientClass.getMethod(methodName, classTypes.get(0), classTypes.get(1), classTypes.get(2));
                result = method.invoke(this.clientInstance, arguments.get(0), arguments.get(1), arguments.get(2));
            } else if (arguments.size() == 4) {
                method = this.clientClass.getMethod(methodName, classTypes.get(0), classTypes.get(1), classTypes.get(2), classTypes.get(3));
                result = method.invoke(this.clientInstance, arguments.get(0), arguments.get(1), arguments.get(2), arguments.get(3));
            } else {
                throw new Exception("Too many arguments");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return result;
    }
    
    public static void main(String[] args) {
        ArrayList<Pair<String, String>> parameters = new ArrayList<Pair<String, String>>();
        parameters.add(new Pair<String,String>("id", "number"));
        parameters.add(new Pair<String,String>("name", "string"));
        parameters.add(new Pair<String,String>("email", "string"));
        
        ClientRunner clientRunner = new ClientRunner("AddressBook", parameters);
        clientRunner.execute();
        
        // CREATE
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
        
        // UPDATE
        ArrayList<Object> argUpdate1 = new ArrayList<Object>();
        argUpdate1.add(new Integer(2));
        argUpdate1.add(new String("jeannot"));
        argUpdate1.add(new String("jeannot@hotmail.com"));
        clientRunner.update(argUpdate1);
        
        // DELETE
        ArrayList<Object> argDelete1 = new ArrayList<Object>();
        argDelete1.add(new Integer(1));
        clientRunner.delete(argDelete1);
        
        // GET
        ArrayList<Object> argGet1 = new ArrayList<Object>();
        argGet1.add(new Integer(2));
        Object result = clientRunner.get(argGet1);
        clientRunner.printResource(result);
        
        // LIST
        List<?> result2 = (List<?>)clientRunner.list(new ArrayList<Object>());
        for (Object o : result2) {
            clientRunner.printResource(o);
        }
        
        clientRunner.shutdown();
    }
}
