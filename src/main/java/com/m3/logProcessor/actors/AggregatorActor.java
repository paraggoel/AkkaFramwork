package com.m3.logProcessor.actors;

import com.m3.logProcessor.actors.FileParserActor.LineToProcess;
import com.m3.logProcessor.actors.FileParserActor.ParserEvent;

import akka.actor.AbstractActor;
import akka.actor.Props;

/**
 * This class is used to count number of words per line
 * @author parag
 *
 */
public class AggregatorActor extends AbstractActor{
	private int wordCount;

	/**
	 * Default Constructor for AggregatorActor
	 */
	public AggregatorActor() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Receive createReceive() {
		return receiveBuilder()
			      .match(ParserEvent.class, fc -> {
			    	  if(fc==ParserEvent.START_OF_FILE) {
			    		  System.out.println("Started processing of file");
			    	  }
			    	  if(fc==ParserEvent.END_OF_FILE) {
			    		  System.out.println("End of file processing");
			    		  System.out.println("Total words in file is: "+wordCount);
			    	  }
			    	  
			      }).match(LineToProcess.class, lp -> {
			    	  //Check if lp is not null and line is not blank
			    	  if(lp.getLine() != null && !lp.getLine().isEmpty()) {
			    		  System.out.println("Aggregartor processing :: event:"+lp.getEvent()+ "and line: "+lp.getLine());
				    	  wordCount+=lp.getLine().split("\\s+").length;
			    	  }
			      }).build();
   	    }
	
	static public Props props() {
	    return Props.create(AggregatorActor.class, ()-> new AggregatorActor());
	  }
}
