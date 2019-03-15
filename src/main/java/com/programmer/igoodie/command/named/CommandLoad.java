package com.programmer.igoodie.command.named;

import org.apache.poi.ss.usermodel.Sheet;

import com.programmer.igoodie.AttendanceCLI;

public class CommandLoad extends NamedCommand {

	public CommandLoad() {
		super("LOAD");
	}
	
	@Override
	public String getDescription() {
		return "Reselects a workbook and sheet from /workspace folder.";
	}
	
	@Override
	public boolean correctLength(String[] commandArgs) {
		return commandArgs.length == 0;
	}
	
	@Override
	public boolean execute(Sheet sheet, String[] args) {
		// TODO FEAT: Load from given path
		
		System.out.println();
		
		AttendanceCLI.performAutosave();
		AttendanceCLI.selectWorkbook();
		AttendanceCLI.selectAttendanceSheet();
		
		return true;
	}
	
}
