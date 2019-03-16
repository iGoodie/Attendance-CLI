package com.programmer.igoodie.command.structured;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import com.programmer.igoodie.AttendanceCLI;
import com.programmer.igoodie.mode.ModeWeek;
import com.programmer.igoodie.util.SheetUtils;
import com.programmer.igoodie.util.StringValidator;
import com.programmer.igoodie.utils.system.Syntax;

public class CommandAttendWeekly extends StructuredCommand {

	@Override
	public String getUsage() {
		return "<attendee_id>";
	}
	
	@Override
	public String getDescription() {
		return "Marks targeted week of attendee as attended.";
	}
	
	@Override
	public boolean correctLength(String[] structure) {
		return structure.length == 1;
	}
	
	@Override
	public boolean correctSyntax(String[] structure) {
		return StringValidator.isNumeric(structure[0]);
	}
	
	@Override
	public boolean execute(Sheet sheet, String[] args) {
		int weekNo = ((ModeWeek)parentMode).getWeek();
		int colIndex = SheetUtils.colIndex(AttendanceCLI.getConfigs().attendeeIdCol);
		
		double attendeeId = Double.parseDouble(args[0]);
		
		Row attendeeRow = SheetUtils.linearFindRow(sheet, colIndex, attendeeId);
		
		if (Syntax.falsey(attendeeRow)) {
			System.out.printf("X - No attendee with id %.0f was found.\n", attendeeId);
			return false;
		}
		
		int weekCol = SheetUtils.weekCol(weekNo, AttendanceCLI.getConfigs());
		
		SheetUtils.markCell(attendeeRow, weekCol, AttendanceCLI.getConfigs().attendedSign);
		System.out.printf("✓ - Marked %.0f as attended at week %d\n", attendeeId, weekNo);
		AttendanceCLI.performAutosave();
		
		return true;
	}
	
}
