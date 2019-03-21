package com.programmer.igoodie.command.named;

import java.nio.file.Path;

import org.apache.poi.ss.usermodel.Sheet;

import com.programmer.igoodie.AttendanceCLI;
import com.programmer.igoodie.command.meta.CommandExample;
import com.programmer.igoodie.util.WorkbookUtils;
import com.programmer.igoodie.utils.io.FileUtils;

public class CommandSave extends NamedCommand {

	public CommandSave() {
		super("SAVE");
	}
	
	@Override
	public String getUsage() {
		return name + " [file|exactPath]";
	}
	
	@Override
	public String getDescription() {
		return "Saves currently open Excel data into a file.";
	}
	
	@Override
	public CommandExample[] getExamples() {
		return CommandExample.of(new String[][] {
			{ "SAVE", "Overwrites currently open Excel data into same file" },
			{ "SAVE somefile.xlsx", "Saves currently open Excel data into /workspace/somefile.xlsx" },
			{ "SAVE C:/somefolder/somefile.xlsx", "Saves currently open Excel date into C:/somefolder/somefile.xlsx" },
		});
	}
	
	@Override
	public boolean correctLength(String[] commandArgs) {
		return commandArgs.length == 1
				|| commandArgs.length == 0;
	}
	
	@Override
	public boolean execute(Sheet sheet, String[] args) {
		Path savePath = evaluateSavePath(args);
		
		// Invalid output format
		if(!savePath.toString().matches(".*\\.(xlsx|xlsb)$")) {
			System.out.printf("X - Given path should end with XLSX or XLSB extension. (E.g test_output.xlsx)\n");
			return false;
		}
			
		// Save workbook to given path
		WorkbookUtils.save(AttendanceCLI.getCurrentWorkbook(), savePath.toFile());
		System.out.printf("✓ - Successfully saved into %s\n", savePath);
		return true;
	}
	
	private Path evaluateSavePath(String[] args) {
		if (args.length == 0)
			return AttendanceCLI.getCurrentWorkbookFile().toPath();
		
		Path savePath = Path.of(args[0]);
		
		// Path is relative to /workspace
		if(!savePath.isAbsolute())
			return Path.of(FileUtils.getExternalDataPath(), args[0]);
		
		return savePath;
	}

}
