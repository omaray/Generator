package com.generation;

public class DirectoryGenerator implements Generator {
    public void generate() {
        Util.createDirectories(Constants.PROTO_FILE_PATH);
        Util.createDirectories(Constants.GENERATED_PATH);
        Util.createDirectories(Constants.MAIN_FILES_PATH);
        Util.createDirectories(Constants.SERVER_OUT_DIR);
        Util.createDirectories(Constants.CLIENT_OUT_DIR);
    }
    
    public void clear() {
        Util.deleteDirectories("out");
    }
    
    public static void main(String[] args) {
        DirectoryGenerator directoryGenerator= new DirectoryGenerator();
        directoryGenerator.generate();
        directoryGenerator.clear();
    }
}
