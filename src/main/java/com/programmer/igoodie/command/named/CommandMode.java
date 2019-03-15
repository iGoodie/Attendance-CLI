package com.programmer.igoodie.command.named;

import org.apache.poi.ss.usermodel.Sheet;

import com.programmer.igoodie.AttendanceCLI;
import com.programmer.igoodie.command.meta.CommandExample;
import com.programmer.igoodie.mode.Mode;
import com.programmer.igoodie.register.Modes;
import com.programmer.igoodie.utils.system.Syntax;

public class CommandMode extends NamedCommand {

	public CommandMode() {
		super("MODE");
	}
	
	@Override
	public String getUsage() {
		return name + " <mode_name>";
	}
	
	@Override
	public String getDescription() {
		return "Enters into a modifying mode.";
	}
	
	@Override
	public CommandExample[] getExamples() {
		return CommandExample.of(new String[][] {
			{ "MODE MAIN_MENU|~|DEFAULT|EXIT", "Enters back to main menu mode" },
			{ "MODE WEEK", "Enters into modifying by week numbers mode" },
		});
	}
	
	@Override
	public boolean correctLength(String[] commandArgs) {
		return commandArgs.length == 1
				|| commandArgs.length == 0;
	}
	
	@Override
	public boolean execute(Sheet sheet, String[] args) {
		if(args.length == 0) {
			System.out.println("Modes:");
			Modes.modeStream().forEach(mode -> System.out.printf(" $ %s %s\n", name, mode.getName()));
			return true;
		}
		
		Mode mode = Modes.getMode(args[0]);
		
		if(Syntax.falsey(mode)) {
			System.out.printf("X - %s mode not found.\n", args[0]);
			return false;
		}
		
		if(AttendanceCLI.getCurrentMode().equals(mode)) {
			System.out.printf("X - %s mode is already active.\n", mode.getName());
			return false;
		}
		
		AttendanceCLI.changeMode(mode);
		
		return true;
	}
	
}
