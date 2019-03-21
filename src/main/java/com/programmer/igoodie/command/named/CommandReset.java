package com.programmer.igoodie.command.named;

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

public class CommandReset extends NamedCommand {

	public CommandReset() {
		super("RESET");
	}
	
	@Override
	public String getUsage() {
		return name + " <attendee_id> [weeks]";
	}
	
	@Override
	public String getDescription() {
		return "Resets attendance status of attendee on given weeks, or all weeks.";
	}
	
	@Override
	public CommandExample[] getExamples() {
		return CommandExample.of(new String[][] {
			{ "RESET 1800123", "Resets attendance of 1800123 on every week" },
			{ "RESET 1800123 1 2 3", "Resets attendance of 1800123 on week#1, week#2 and week#3" },
			{ "RESET 1800123 * 1 2", "Resets attendance of 1800123 on every week except week#1 and week#2" },
		});
	}
	
	@Override
	public boolean correctLength(String[] commandArgs) {
		return commandArgs.length >= 2;
	}
	
	@Override
	public boolean correctSyntax(String[] commandArgs) {
		// First arg should be numeric
		if (!StringValidator.isNumeric(commandArgs[0]))
			return false;

		// Other args should be valid week format
		if (!Weeks.validateArgs(Arrays.copyOfRange(commandArgs, 1, commandArgs.length)))
			return false;

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
		if(Syntax.falsey(attendeeRow)) {
			System.out.printf("X - No attendee with id %.0f was found.\n", attendeeId);
			return false;
		}
		
		// For each week perform 
		weeks.forEach(week -> {
			int weekCol = SheetUtils.weekCol(week, AttendanceCLI.getConfigs());
			
			if (weekCol == -1) {
				System.out.printf("X - Week no %d out of bounds.\n", week);
				return;
			}
			
			SheetUtils.emptyCell(attendeeRow, weekCol);

			System.out.printf("✓ - Reset %.0f on week %d\n", attendeeId, week);
		});
		
		// Save after reseting
		AttendanceCLI.performAutosave();
		return true;
	}
	
}
