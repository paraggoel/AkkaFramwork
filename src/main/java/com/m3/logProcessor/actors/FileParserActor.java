package com.m3.logProcessor.actors;

import java.util.List;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;


public class FileParserActor extends AbstractActor{
	/**
	 * enum defined for the different type of event triggered on file.
	 * @author parag
	 *
	 */
	public static enum ParserEvent{
		START_OF_FILE,LINE,END_OF_FILE
	}

	private ActorRef aggregator;
	
	/**
	 *  Default Constructor of the class
	 */
	public FileParserActor() {
	}

	/**
	 * Parameterized constructor
	 * @param aggregator
	 */
	public FileParserActor(ActorRef aggregator) {
	    this.aggregator = aggregator;
	}

	
	
	@Override
	public Receive createReceive() {
		return receiveBuilder()
			      .match(FileContent.class, fc -> {
			    	  aggregator.tell(ParserEvent.START_OF_FILE, ActorRef.noSender());
			    	  for(String line: fc.getLines()) {
			    		  aggregator.tell(new LineToProcess(line), ActorRef.noSender());
			    	  }
			    	  aggregator.tell(ParserEvent.END_OF_FILE, ActorRef.noSender());
			      }).build();
	}
	
	static public Props props(ActorRef nextActor) {
	    return Props.create(FileParserActor.class, () -> new FileParserActor(nextActor));
	  }
	

	/**
	 * Static class for reading lines from file
	 * @author parag
	 *
	 */
	public static class FileContent{
		private final List<String> lines;
		public FileContent(List<String> lines) {
			this.lines = lines;
		}
		
		public List<String> getLines(){
			return lines;
		}
	}
	

   /**
    * static class to process line on LINE event	
    * @author parag
    *
    */
   public static class LineToProcess{
	   ParserEvent event = ParserEvent.LINE;
	   String line;
	   public LineToProcess(String line) {
		   this.line = line;
	    }
	   public ParserEvent getEvent() {
		return event;
	    }
	public String getLine() {
		return line;
		}
	   
     }

}
