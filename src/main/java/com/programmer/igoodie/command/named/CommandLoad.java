package com.programmer.igoodie.command.named;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.programmer.igoodie.AttendanceCLI;
import com.programmer.igoodie.utils.io.FileUtils;
import com.programmer.igoodie.utils.system.Syntax;

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
		
		if (!AttendanceCLI.performAutosave()) {
			System.out.printf("X - Autosaving before LOAD failed. "
					+ "This might be caused because the autosave file is being used by another process. %s\n", AttendanceCLI.getCurrentWorkbookFile().getPath());
			System.out.println("? - Do you still want to load another workbook?\n");
			
			while(true) {
				System.out.print("Answer (Y/N) > ");
				
				String answer = AttendanceCLI.SCANNER.nextLine();
				
				if (answer.equalsIgnoreCase("Y"))
					break;
				
				if (answer.equalsIgnoreCase("N"))
					return false;
			}			
		}
		
		System.out.println();
		selectWorkbook();
		System.out.print("\n");
		selectAttendanceSheet();
		
		return true;
	}
	
	public static void selectWorkbook() {
		// Filter workbook files from /workspace folder
		File[] workbooks = FileUtils.getExternalFile("")
				.listFiles((dir, name) -> !name.startsWith("~$") && name.toLowerCase().matches(".*\\.(xlsx|xlsb)"));

		// No workbooks were found. Terminate immidiately
		if (Syntax.falsey(workbooks) || workbooks.length == 0) {
			System.out.println("X - No workbook within /workspace folder. Terminating the program.");
			System.exit(0);
		}

		System.out.printf("? - Select one of the accesible workbooks (under /workspace folder):\n");

		// Print all the visible workspaces
		for (int i = 0; i < workbooks.length; i++) {
			System.out.printf("[%d] %s\n", i, workbooks[i].getName());
		}

		// Select workbook index and load it in
		boolean selected = false;
		while (!selected) {
			try {
				System.out.print("Select Workbook (index) > ");
				int selection = Integer.parseInt(AttendanceCLI.SCANNER.nextLine());

				if (selection >= 0 && selection < workbooks.length) {
					AttendanceCLI.setCurrentWorkbookFile(workbooks[selection]);
					
					FileInputStream fis = new FileInputStream(AttendanceCLI.getCurrentWorkbookFile());
					AttendanceCLI.setCurrentWorkbook(WorkbookFactory.create(fis));
					
					fis.close();
					selected = true;
					System.out.printf("✓ - Successfully loaded workbook: %s\n", AttendanceCLI.getCurrentWorkbookFile().getName());
					
				} else {
					System.out.printf("X - Index out of bound. Please enter some value between [%d,%d]\n\n", 0,
							workbooks.length - 1);
				}

			} catch (NumberFormatException e) {
				System.out.println("X - Invalid workbook index.\n");
			} catch (IOException e) {
				System.out.println("X - Selected workbook cannot be read. It might be corrupted.\n");
			}
		}
	}

	public static void selectAttendanceSheet() {
		// Print all the sheets that the workbook has
		System.out.printf("? - Select one of the sheets from %s: \n", AttendanceCLI.getCurrentWorkbookFile().getName());
		AttendanceCLI.getCurrentWorkbook().sheetIterator().forEachRemaining(
				sheet -> System.out.printf("[%d] %s\n", AttendanceCLI.getCurrentWorkbook().getSheetIndex(sheet), sheet.getSheetName()));

		// Select sheet index and save the reference
		boolean selected = false;
		Sheet sheet;
		while (!selected) {
			try {
				System.out.print("Select Sheet (name/index) > ");
				String selection = AttendanceCLI.SCANNER.nextLine();

				if (Syntax.truthy(sheet = AttendanceCLI.getCurrentWorkbook().getSheet(selection))) {
					AttendanceCLI.setCurrentAttendanceSheet(sheet);
					selected = true;
					System.out.printf("✓ - Successfully selected attendance sheet: %s\n",
							AttendanceCLI.getCurrentAttendanceSheet().getSheetName());
				} else {
					int selectionIndex = Integer.parseInt(selection);

					if (selectionIndex >= 0 && selectionIndex < AttendanceCLI.getCurrentWorkbook().getNumberOfSheets()) {
						AttendanceCLI.setCurrentAttendanceSheet(AttendanceCLI.getCurrentWorkbook().getSheetAt(selectionIndex));
						selected = true;
						System.out.printf("✓ - Successfully selected attendance sheet: %s\n",
								AttendanceCLI.getCurrentAttendanceSheet().getSheetName());
					} else {
						System.out.printf("X - Index out of bound. Please enter some value between [%d,%d]\n\n", 0,
								AttendanceCLI.getCurrentWorkbook().getNumberOfSheets() - 1);
					}
				}

			} catch (NumberFormatException e) {
				System.out.println("X - Invalid sheet indentifier.\n");
			}
		}
	}
	
}
