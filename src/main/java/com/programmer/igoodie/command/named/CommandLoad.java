package com.programmer.igoodie.command.named;

import org.apache.poi.ss.usermodel.Sheet;

import com.programmer.igoodie.AttenderCLI;

public class CommandLoad extends NamedCommand {

	public CommandLoad() {
		super("LOAD");
	}
	
	@Override
	public String getDescription() {
		return "Reselects a workbook and sheet from /data folder.";
	}
	
	@Override
	public boolean correctLength(String[] commandArgs) {
		return commandArgs.length == 0;
	}
	
	@Override
	public boolean execute(Sheet sheet, String[] args) {
		// TODO FEAT: Load from given path
		
		System.out.println();
		
		AttenderCLI.performAutosave();
		AttenderCLI.selectWorkbook();
		AttenderCLI.selectAttendanceSheet();
		
		return true;
	}
	
}
