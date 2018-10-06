package com.m3.logProcessor;

import com.m3.logProcessor.actors.AggregatorActor;
import com.m3.logProcessor.actors.FileParserActor;
import com.m3.logProcessor.actors.FileScannerActor;
import com.m3.logProcessor.actors.FileScannerActor.DirectoryToScan;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

/**
 * Main class 
 * @author parag
 *
 */
public class LogProcessorMain {

	public void start(String directoryPath) {
		ActorSystem actorSystem = ActorSystem.create("logProcessor");
		final ActorRef aggregaratorRef = actorSystem.actorOf(AggregatorActor.props(), "aggregatorActor");
		final ActorRef fileParserActor = actorSystem.actorOf(FileParserActor.props(aggregaratorRef), "fileParserActor");
        final ActorRef scanFile =  actorSystem.actorOf(FileScannerActor.props(directoryPath, fileParserActor), "fileScanner");
        scanFile.tell(new DirectoryToScan(directoryPath), ActorRef.noSender()); 
      }

    public static void main(String[] args) {
        String path = args[0];
        System.out.println("Path is --> "+path);
    	LogProcessorMain application = new LogProcessorMain();
        application.start(path);
    }
}
