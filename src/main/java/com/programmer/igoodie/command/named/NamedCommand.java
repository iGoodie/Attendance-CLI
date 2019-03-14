package com.programmer.igoodie.command.named;

import com.programmer.igoodie.command.Command;

import lombok.Getter;

public abstract class NamedCommand extends Command {

	@Getter	String name;
	
	public NamedCommand(String name) {
		this.name = name.toUpperCase();
	}
	
	@Override
	public String getUsage() {
		return name;
	}
	
}
