package com.m3.logProcessor.actors;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.m3.logProcessor.actors.FileParserActor.FileContent;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;


/**
* The FileScanner program scans for files in a given directory
* 
* @author parag
* @version 1.0
*/
public class FileScannerActor extends AbstractActor {
	
private ActorRef parserActor;

public FileScannerActor(ActorRef parserActor) {
     this.parserActor = parserActor;
  }

static public Props props(String message, ActorRef parserActor) {
    return Props.create(FileScannerActor.class, () -> new FileScannerActor(parserActor));
  }

/**
 * Invoked by the Actor System to scan a given directory
 * 
 * @param directoryPath
 *            The message to process
 */
public void processDirectoryPath(String directoryPath) {
    if (directoryPath != null ) {
        // Only top level files in the directory are read.No recursion is done
        File directory = new File(directoryPath);
        
        // Incase of large number of files.
        File[] files = directory.listFiles();
        
        if(files.length ==0) {
        	System.out.println("No file in directory");
        	return;
        }

        // To only count the files and ignore any folders          
        for (File file : files) {
            if (file.isFile()) {
            	try {
					parserActor.tell(new FileContent(Files.readAllLines(file.toPath())), ActorRef.noSender());
				} catch (IOException e) {
				   e.printStackTrace();
				}
            }
         }
        
       }
 }

/**
 * static class to get Directory for scanning the file
 * @author parag
 *
 */
static public class DirectoryToScan {
    public final String fileName;

    public DirectoryToScan(String fileName) {
        this.fileName = fileName;
    }

	public String getFileName() {
		return fileName;
	}
  }

@Override
public Receive createReceive() {
  return receiveBuilder()
      .match(DirectoryToScan.class, wtg -> {
        System.out.println("scanning directory :"+wtg.getFileName());
        Path path = Paths.get(wtg.getFileName());
        if(Files.exists(path) && Files.isDirectory(path)) {
        	processDirectoryPath(wtg.getFileName());
        }else {
        	System.out.println("Invalid path input or not a directory");
        }
      }).build();
   }
}