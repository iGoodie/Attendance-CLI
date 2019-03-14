package com.programmer.igoodie.command.named;

import org.apache.poi.ss.usermodel.Sheet;

import com.programmer.igoodie.AttenderCLI;

public class CommandExit extends NamedCommand {

	public CommandExit() {
		super("EXIT");
	}
	
	@Override
	public String getUsage() {
		return "EXIT";
	}
	
	@Override
	public String getDescription() {
		return "Attempts to save currently open workbook. Then terminates the program.";
	}
	
	@Override
	public boolean correctLength(String[] commandArgs) {
		return commandArgs.length == 0;
	}

	@Override
	public boolean execute(Sheet sheet, String[] args) {
		AttenderCLI.terminate();
		return true;
	}
	
}
