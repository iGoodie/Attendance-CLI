package com.programmer.igoodie.command.structured;

import com.programmer.igoodie.command.Command;

public abstract class StructuredCommand extends Command {

	@Override
	public boolean correctLength(String[] structure) {
		return super.correctLength(structure);
	}
	
	@Override
	public boolean correctSyntax(String[] structure) {
		return super.correctSyntax(structure);
	}
	
}
