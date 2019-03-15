package com.programmer.igoodie.command.named;

import org.apache.poi.ss.usermodel.Sheet;

import com.programmer.igoodie.AttendanceCLI;
import com.programmer.igoodie.command.meta.CommandExample;

public class CommandAutosave extends NamedCommand {

	public CommandAutosave() {
		super("AUTOSAVE");
	}
	
	@Override
	public String getUsage() {
		return name + " [ON|OFF]";
	}
	
	@Override
	public String getDescription() {
		return "Enables or disables auto saving on autosave file under /workspace folder.";
	}
	
	@Override
	public CommandExample[] getExamples() {
		return CommandExample.of(new String[][] {
			{ "AUTOSAVE", "Displays whether autosaving is enabled or not." },
			{ "AUTOSAVE ON", "Enables autosaving." },
			{ "AUTOSAVE OFF", "Disables autosaving" },
		});
	}
	
	@Override
	public boolean correctLength(String[] commandArgs) {
		return commandArgs.length == 0
				|| commandArgs.length == 1;
	}
	
	@Override
	public boolean correctSyntax(String[] commandArgs) {
		if(commandArgs.length == 0)
			return true;
		
		return commandArgs[0].toUpperCase().matches("ON|OFF");
	}
	
	@Override
	public boolean execute(Sheet sheet, String[] args) {
		if(args.length == 0) {
			String enabled = AttendanceCLI.getConfigs().autosaveEnabled ? "enabled" : "disabled";
			System.out.printf("✓ - Autosaving is currently %s\n", enabled);
			return true;
		}
		
		boolean enable = args[0].toUpperCase().equals("ON");
		AttendanceCLI.getConfigs().autosaveEnabled = enable;
		AttendanceCLI.getConfigs().save();
		return true;
	}
	
}
