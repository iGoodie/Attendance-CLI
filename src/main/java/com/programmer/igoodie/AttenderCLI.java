package com.programmer.igoodie;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;
import java.util.Scanner;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.programmer.igoodie.command.Command;
import com.programmer.igoodie.command.named.NamedCommand;
import com.programmer.igoodie.command.structured.StructuredCommand;
import com.programmer.igoodie.config.AttendanceCLIConfig;
import com.programmer.igoodie.mode.Mode;
import com.programmer.igoodie.register.Modes;
import com.programmer.igoodie.utils.io.FileUtils;
import com.programmer.igoodie.utils.system.Syntax;

import lombok.Getter;

public final class AttenderCLI implements AttendanceCLIConstants {

	private static final Scanner SCANNER = new Scanner(System.in);

	private static boolean running = true;
	private @Getter static Mode currentMode = Modes.getMainMode();
	private @Getter static AttendanceCLIConfig configs;
	private static File workbookFile;
	private static Workbook workbook;
	private static Sheet attendanceSheet;

	public static void main(String[] args) {
		System.out.printf("✓ -  Welcome to %s! - Version %s\n\n", PROGRAM_NAME, PROGRAM_VERSION);
		System.out.printf("! - Your workbook(s) should be placed under /data folder.\n");
		
		System.out.print("\n---\n\n");

		Properties props = FileUtils.readProperties(FileUtils.getExternalFile("configurations.properties"));
		
		if(Syntax.falsey(props)) {
			System.out.println("X - Config file (configurations.properties) "
					+ "is missing/corrupted. Program terminating.");
			return;
		}
		
		configs = new AttendanceCLIConfig(props);
		
		selectWorkbook();
		selectAttendanceSheet();
		
		System.out.print("---\n\n");

		while (running) {
			currentMode.printInputPrefix();

			String rawInput = SCANNER.nextLine();

			handleInput(rawInput);

			System.out.print("\n---\n\n");
		}

		System.out.printf("✓ -  Termination of %s succeeded. Goodbye!", PROGRAM_NAME);
	}

	public static void selectWorkbook() {
		// Filter workbook files from /data folder
		File[] workbooks = FileUtils.getExternalFile("")
				.listFiles((dir, name) -> name.toLowerCase().matches(".*\\.(xlsx|xlsb)"));

		// No workbooks were found. Terminate immidiately
		if (Syntax.falsey(workbooks) || workbooks.length == 0) {
			System.out.println("X - No workbook within /data folder. Terminating the program.");
			System.exit(0);
		}

		System.out.printf("? - Select one of the accesible workbooks (under /data folder):\n");

		// Print all the visible workspaces
		for (int i = 0; i < workbooks.length; i++) {
			System.out.printf("[%d] %s\n", i, workbooks[i].getName());
		}

		// Select workbook index and load it in
		boolean selected = false;
		while (!selected) {
			try {
				System.out.print("Select Workbook > ");
				int selection = Integer.parseInt(SCANNER.nextLine());

				if (selection >= 0 && selection < workbooks.length) {
					workbookFile = workbooks[selection];
					workbook = WorkbookFactory.create(workbookFile);
					selected = true;
					System.out.printf("✓ - Successfully loaded workbook: %s\n", workbookFile.getName());
				} else {
					System.out.printf("X - Index out of bound. Please enter some value between [%d,%d]\n", 0,
							workbooks.length - 1);
				}

			} catch (NumberFormatException e) {
				System.out.println("X - Invalid workbook index.");
			} catch (IOException e) {
				System.out.println("X - Selected workbook cannot be read. It might be corrupted.");
			}

			System.out.println();
		}
	}

	public static void selectAttendanceSheet() {
		// Print all the sheets that the workbook has
		System.out.printf("? - Select one of the sheets from %s: \n", workbookFile.getName());
		workbook.sheetIterator().forEachRemaining(
				sheet -> System.out.printf("[%d] %s\n", workbook.getSheetIndex(sheet), sheet.getSheetName()));

		// Select sheet index and save the reference
		boolean selected = false;
		Sheet sheet;
		while (!selected) {
			try {
				System.out.print("Select Sheet (name/index) > ");
				String selection = SCANNER.nextLine();

				if (Syntax.truthy(sheet = workbook.getSheet(selection))) {
					attendanceSheet = sheet;
					selected = true;
					System.out.printf("✓ - Successfully selected attendance sheet: %s\n",
							attendanceSheet.getSheetName());
				} else {
					int selectionIndex = Integer.parseInt(selection);

					if (selectionIndex >= 0 && selectionIndex < workbook.getNumberOfSheets()) {
						attendanceSheet = workbook.getSheetAt(selectionIndex);
						selected = true;
						System.out.printf("✓ - Successfully selected attendance sheet: %s\n",
								attendanceSheet.getSheetName());
					} else {
						System.out.printf("X - Index out of bound. Please enter some value between [%d,%d]\n", 0,
								workbook.getNumberOfSheets() - 1);
					}
				}

			} catch (NumberFormatException e) {
				System.out.println("X - Invalid sheet indentifier.");
			}

			System.out.println();
		}
	}

	public static void handleInput(String rawInput) {
		String[] args = rawInput.split("\\s+");
		Command command;

		// Named command found within the mode
		if (Syntax.truthy(command = currentMode.getCommand(args[0]))) {
			args = Arrays.copyOfRange(args, 1, args.length);
			handleNamedCommand((NamedCommand) command, args);
		}

		// Structured command found within the mode
		else if (Syntax.truthy(command = currentMode.getCommandByStructure(args))) {
			handleStructuredCommand((StructuredCommand) command, args);
		}

		// Command is not found amongst all the commands
		else {
			System.out.println("X - Unknown command");
		}
	}

	private static void handleNamedCommand(NamedCommand command, String[] args) {
		// Invalid argument length
		if (!command.correctLength(args)) {
			System.out.printf("X - Invalid argument length for command: %s\n", command.getName());
			return;
		}

		// Invalid syntax for command
		if (!command.correctSyntax(args)) {
			System.out.printf("X - Invalid syntax for command: %s\n", command.getName());
			return;
		}

		// Execute and check success
		if (!command.execute(attendanceSheet, args)) {
			System.out.printf("X - Internal error for command: %s\n", command.getName());
		}
	}

	private static void handleStructuredCommand(StructuredCommand command, String[] structure) {
		// Invalid argument length
		if (!command.correctLength(structure)) {
			System.out.printf("X - Invalid argument length for structure: %s\n", String.join(" ", structure));
			return;
		}

		// Invalid syntax for command
		if (!command.correctSyntax(structure)) {
			System.out.printf("X - Invalid syntax for structure: %s\n", String.join(" ", structure));
			return;
		}

		// Execute and check success
		if (!command.execute(attendanceSheet, structure)) {
			System.out.printf("X - Internal error for strucure: %s\n", String.join(" ", structure));
		}
	}

	public static void terminate() {
		running = false;
		// TODO: Save XLSX before terminating
	}

}
