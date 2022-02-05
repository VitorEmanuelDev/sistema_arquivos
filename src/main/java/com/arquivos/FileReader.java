package com.arquivos;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileReader {

    public void read(Path path) throws IOException {
    	 BufferedReader reader = Files.newBufferedReader(path);
    	 String line;	 
    	  while ((line = reader.readLine()) != null)
              System.out.println(line);
    }
}