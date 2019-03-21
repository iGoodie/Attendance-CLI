package com.programmer.igoodie;

import java.io.File;
import java.util.Arrays;
import java.util.Properties;
import java.util.Scanner;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.programmer.igoodie.command.Command;
import com.programmer.igoodie.command.meta.CommandParser;
import com.programmer.igoodie.command.named.NamedCommand;
import com.programmer.igoodie.command.structured.StructuredCommand;
import com.programmer.igoodie.config.AttendanceCLIConfig;
import com.programmer.igoodie.mode.Mode;
import com.programmer.igoodie.register.Modes;
import com.programmer.igoodie.util.WorkbookUtils;
import com.programmer.igoodie.utils.io.FileUtils;
import com.programmer.igoodie.utils.system.Syntax;

import lombok.Getter;
import lombok.Setter;

public final class AttendanceCLI implements AttendanceCLIConstants {

	public static final Scanner SCANNER = new Scanner(System.in);

	private static boolean running = true;
	
	private @Getter static Mode currentMode = Modes.getMainMode();
	private @Getter static AttendanceCLIConfig configs;
	
	private @Getter @Setter static File currentWorkbookFile;
	private @Getter @Setter static Workbook currentWorkbook;
	private @Getter @Setter static Sheet currentAttendanceSheet;

	public static void main(String[] args) {
		System.out.printf("✓ -  Welcome to %s! - Version %s\n\n", PROGRAM_NAME, PROGRAM_VERSION);
		
		// Change external path to %USER_DIR%/workspace
		FileUtils.setExternalDataPath(System.getProperty("user.dir") + "/workspace");

		System.out.printf("! - Your workbook(s) should be placed under /workspace folder.\n");
		System.out.print("\n---\n\n");
		
		// Load properties from file and construct configurations
		File propsFile = FileUtils.getExternalFile(AttendanceCLIConfig.CONFIG_FILE_NAME);
		Properties props = FileUtils.readProperties(propsFile);
		if(Syntax.falsey(props)) {
			System.out.println("X - Config file (configurations.properties) "
					+ "is missing/corrupted. Program terminating.");
			return;
		}
		configs = new AttendanceCLIConfig(props);
		
		// Load workbook and select sheet
		System.out.println("! - A sheet from a workbook is needed in order to start modifying.\n");
		handleInput("LOAD");
		System.out.print("\n---\n\n");

		// Start CLI loop
		while (running) {
			System.out.print(currentMode.getInputPrefix());
			String rawInput = SCANNER.nextLine();

			try {
				handleInput(rawInput);
			} catch(Exception e) {
				e.printStackTrace();
				System.out.println("X - Internal error. Report this to the developer.");
			}

			System.out.print("\n---\n\n");
		}

		// Execute termination logic
		System.out.printf("✓ -  Termination of %s succeeded. Goodbye!", PROGRAM_NAME);
	}

	public static boolean performAutosave() {
		if(!configs.autosaveEnabled)
			return true;
		
		if(Syntax.falsey(currentWorkbook))
			return true;
		
		File autosaveFile = FileUtils.getExternalFile(configs.autosaveFile);
		if(!WorkbookUtils.save(currentWorkbook, autosaveFile)) 
			return false;

		return true;
	}

	public static void changeMode(Mode mode) {
		currentMode = mode;
	}
	
	public static void handleInput(String rawInput) {
		String[] args = CommandParser.intoTokens(rawInput);
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
			System.out.println("X - Unknown command. Execute HELP command for more info about commands.");
			// TODO FEAT: Suggest amongst all the commands
		}
	}

	private static void handleNamedCommand(NamedCommand command, String[] args) {
		// Invalid argument length
		if (!command.correctLength(args)) {
			System.out.printf("X - Invalid argument length for command: %s\n", command.getName());
			System.out.printf("! - Usage: %s\n", command.getUsage());
			return;
		}

		// Invalid syntax for command
		if (!command.correctSyntax(args)) {
			System.out.printf("X - Invalid syntax for command: %s\n", command.getName());
			System.out.printf("! - Usage: %s\n", command.getUsage());
			return;
		}

		// Execute and check success
		if (!command.execute(currentAttendanceSheet, args)) {
//			System.out.printf("X - Error executing command: %s\n", command.getName());
		}
	}

	private static void handleStructuredCommand(StructuredCommand command, String[] structure) {
		// Invalid argument length
		if (!command.correctLength(structure)) {
			System.out.printf("X - Invalid argument length for structure: %s\n", String.join(" ", structure));
			System.out.printf("! - Usage: %s\n", command.getUsage());
			return;
		}

		// Invalid syntax for command
		if (!command.correctSyntax(structure)) {
			System.out.printf("X - Invalid syntax for structure: %s\n", String.join(" ", structure));
			System.out.printf("! - Usage: %s\n", command.getUsage());
			return;
		}

		// Execute and check success
		if (!command.execute(currentAttendanceSheet, structure)) {
//			System.out.printf("X - Error executing strucure: %s\n", String.join(" ", structure));
		}
	}

	public static void terminate() {
		running = false;
		performAutosave();
	}

}
