package com.programmer.igoodie.command.structured;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import com.programmer.igoodie.AttenderCLI;
import com.programmer.igoodie.command.meta.CommandExample;
import com.programmer.igoodie.util.SheetUtils;
import com.programmer.igoodie.util.StringValidator;
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
				{ "1800123 1 2 9 14", "Include attence of 1800123 on week#1, week#2, week#9, week#14." },
				{ "1800123 *", "Include attendance of 1800123 on all weeks." }, { "1800123 * 1 2 9 14",
						"Include attence of 1800123 on all weeks except week#1, week#2, week#9, week#14" } });
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
		double attendeeId = Double.parseDouble(args[0]);

		// Asterisk expression input
		if (args[1].equals("*")) {
			ArrayList<Integer> weekExclusionList = new ArrayList<Integer>();

			for (int i = 2; i < args.length; i++) {
				if (!StringValidator.isNumeric(args[i])) {
					System.out.println("X - Week arguments are not parsable.");
					return false;
				}

				weekExclusionList.add(Integer.parseInt(args[i]));
			}

			pushAttendancesExcept(sheet, attendeeId, weekExclusionList.stream().mapToInt(i -> i).toArray());
			return true;
		}

		pushAttendances(sheet, attendeeId, Arrays.stream(args, 1, args.length).mapToInt(Integer::parseInt).toArray());
		return true;
	}

	private void pushAttendances(Sheet sheet, double attendeeId, int[] weeks) {
		int colIndex = SheetUtils.colIndex(AttenderCLI.getConfigs().attendeeIdCol);
		Row attendeeRow = SheetUtils.linearFindRow(sheet, colIndex, attendeeId);

		if (Syntax.falsey(attendeeRow)) {
			System.out.printf("X - No attendee with id %.0f was found.\n", attendeeId);
			return;
		}

		for (int week : weeks) {
			int weekCol = SheetUtils.weekCol(week, AttenderCLI.getConfigs());
			
			if (weekCol == -1) {
				System.out.printf("X - Week no %d out of bounds.\n", week);
				continue;
			}
			
			if (SheetUtils.markCell(attendeeRow, weekCol, AttenderCLI.getConfigs().attendedSign))
				System.out.printf("✓ - Marked %.0f attended at week %d\n", attendeeId, week);
		}
	}

	private void pushAttendancesExcept(Sheet sheet, double attendeeId, int[] exceptWeeks) {
		int weekLength = (int) (AttenderCLI.getConfigs().weekFinishCol - AttenderCLI.getConfigs().weekStartCol) + 1;
		List<Integer> weeks = IntStream.rangeClosed(1, weekLength).boxed().collect(Collectors.toList());

		for (int exceptWeek : exceptWeeks) {
			weeks.remove((Integer) exceptWeek);
		}

		pushAttendances(sheet, attendeeId, weeks.stream().mapToInt(i -> i).toArray());
	}

}
