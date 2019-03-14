package com.programmer.igoodie.mode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Stream;

import com.programmer.igoodie.command.Command;
import com.programmer.igoodie.command.named.NamedCommand;
import com.programmer.igoodie.command.structured.StructuredCommand;

import lombok.Getter;

public abstract class Mode {

	protected @Getter String name;

	HashMap<String, Command> namedCommands;
	ArrayList<StructuredCommand> structuredCommands;

	public Mode(String name) {
		this.name = name;
		this.namedCommands = new HashMap<>();
		this.structuredCommands = new ArrayList<>();
		registerCommands();
	}

	public Command getCommand(String alias) {
		return namedCommands.get(alias.toUpperCase());
	}

	public Command getCommandByStructure(String[] structure) {
		for(StructuredCommand command : structuredCommands) {
			if(command.correctLength(structure) && command.correctSyntax(structure))
				return command;
		}
		
		return null;
	}

	public Stream<? extends Command> commandStream() {
		return Stream.concat(namedCommands.values().stream(), 
				structuredCommands.stream());
	}
	
	protected abstract void registerCommands();

	public void registerCommand(NamedCommand command, String... aliases) {
		command.setParentMode(this);
		
		namedCommands.put(command.getName(), command);
		
		for (String alias : aliases)
			namedCommands.put(alias, command);
	}
	
	public void registerCommand(StructuredCommand command) {
		command.setParentMode(this);
		
		structuredCommands.add(command);
	}
	
	public void printInputPrefix() {
		System.out.print("$ ");
	}
	
}
