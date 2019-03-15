package com.programmer.igoodie.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import com.programmer.igoodie.config.AttendanceCLIConfig;
import com.programmer.igoodie.utils.system.Syntax;

public class SheetUtils {

	public static Row linearFindRow(Sheet sheet, int col, Object value) {
		for (Row row : sheet) {
			Cell cell = row.getCell(col);

			switch (cell.getCellType()) {
			case STRING:
				if (cell.getStringCellValue().equals(value))
					return row;
				break;

			case NUMERIC:
				if (!(value instanceof Double))
					break;

				if (cell.getNumericCellValue() == (double) value)
					return row;
				break;

			default:
				break;
			}
		}

		return null;
	}

	public static int rowIndex(int row) {
		return row - 1;
	}

	public static int colIndex(char col) {
		return col - 'A';
	}

	public static int weekCol(int weekNo, AttendanceCLIConfig configs) {
		int weekLength = configs.weekLength();

		if (weekNo <= 0 || weekNo > weekLength)
			return -1;

		return weekNo + colIndex(configs.weekStartCol) - 1;
	}

	public static boolean markCell(Row row, int col, String value) {
		if (Syntax.falsey(row))
			return false;

		Cell cell = row.getCell(col);
		
		// TODO FIX: Keep EVERY cell style
		
		if (Syntax.falsey(cell)) {
			cell = row.createCell(col);
		}
		
		cell.setCellValue(value);

		return true;
	}

}
