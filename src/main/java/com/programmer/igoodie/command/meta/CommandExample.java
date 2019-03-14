package com.programmer.igoodie.command.meta;

import lombok.Getter;
import lombok.Setter;

public class CommandExample {

	public static CommandExample[] of(String[][] examplesLayout) {
		CommandExample[] examples = new CommandExample[examplesLayout.length];
		
		for(int i=0; i<examplesLayout.length; i++) {			
			String[] layout = examplesLayout[i];
			examples[i] = new CommandExample(layout[0], layout[1]);
		}
		
		return examples;
	}

	protected @Getter @Setter String statement;
	protected @Getter @Setter String description;

	public CommandExample() {
	}

	public CommandExample(String statement, String description) {
		this.statement = statement;
		this.description = description;
	}

	@Override
	public String toString() {
		return String.format("%s %s", statement, description);
	}

}
