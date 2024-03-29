package com.programmer.igoodie.command.structured;

import java.util.Arrays;
import java.util.stream.Stream;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import com.programmer.igoodie.AttendanceCLI;
import com.programmer.igoodie.command.meta.CommandExample;
import com.programmer.igoodie.util.SheetUtils;
import com.programmer.igoodie.util.StringValidator;
import com.programmer.igoodie.util.Weeks;
import com.programmer.igoodie.utils.system.Syntax;

public class CommandAttendInline extends StructuredCommand {

	@Override
	public String getUsage() {
		return "<attendee_id> <week_numbers>";
	}

	@Override
	public String getDescription() {
		return "Pushes attendance(s) of attendees for given week numbers";
	}

	@Override
	public CommandExample[] getExamples() {
		return CommandExample.of(new String[][] {
			{ "1800123 1 2", "Include attence of 1800123 on week#1 and week#2." },
			{ "1800123 *", "Include attendance of 1800123 on all weeks." },
			{ "1800123 * 1 2", "Include attendance of 1800123 on all weeks except week#1 and week#2" } });
	}

	@Override
	public boolean correctLength(String[] structure) {
		return structure.length >= 2;
	}

	@Override
	public boolean correctSyntax(String[] structure) {
		if (!StringValidator.isNumeric(structure[0]))
			return false;

		for (int i = structure[1].equals("*") ? 2 : 1; i < structure.length; i++) {
			if (!StringValidator.isNumeric(structure[i]))
				return false;
		}

		return true;
	}

	@Override
	public boolean execute(Sheet sheet, String[] args) {
		int weekLength = AttendanceCLI.getConfigs().weekLength();
		Stream<Integer> weeks = Weeks.parseNumbers(Arrays.copyOfRange(args, 1, args.length),weekLength).stream();

		double attendeeId = Double.parseDouble(args[0]);
		
		int attendeeIdCol = SheetUtils.colIndex(AttendanceCLI.getConfigs().attendeeIdCol);
		Row attendeeRow = SheetUtils.linearFindRow(sheet, attendeeIdCol, attendeeId);
		
		// Row with given id not found
		if (Syntax.falsey(attendeeRow)) {
			System.out.printf("X - No attendee with id %.0f was found.\n", attendeeId);
			return false;
		}
		
		weeks.forEach(week -> {
			int weekCol = SheetUtils.weekCol(week, AttendanceCLI.getConfigs());
			
			if (weekCol == -1) {
				System.out.printf("X - Week no %d out of bounds.\n", week);
				return;
			}
			
			if (SheetUtils.markCell(attendeeRow, weekCol, AttendanceCLI.getConfigs().attendedSign))
				System.out.printf("✓ - Marked %.0f as attended at week %d\n", attendeeId, week);
		});
		
		// Save after attending
		AttendanceCLI.performAutosave();
		return true;
	}

}
