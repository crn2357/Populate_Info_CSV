import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;


class Populate_Info {
	public static void main(String[] args) {
		String reportCSVFilename = args[0];
	    String inProgressExcelFilename = args[1];
	    ArrayList<String> columnNamesEntrys = new ArrayList<>();
	    ArrayList<String> requirementsNamesEntrys = new ArrayList<>();
	    ArrayList<String> inProgressColumnNums = new ArrayList<>();
	    ArrayList<String> reportColumnNums = new ArrayList<>();
	    ArrayList<String> inProgressRequirementsNums = new ArrayList<>();
	    ArrayList<String> reportRequirementsNums = new ArrayList<>();
	    Scanner scan = new Scanner(System.in);
	    String entry = "";
	    while (true) {
	    	System.out.println("Enter column names to populate('next' to continue):");
	    	entry = scan.nextLine().toLowerCase();
	    	if (!entry.equals("next")) {
	    		columnNamesEntrys.add(entry);
	    	} else {
	    		break;
	    	}
	    }
	    while (true) {
		    System.out.println("Enter requirement names('end' to continue):");
		    entry = scan.nextLine().toLowerCase();
		    if (!entry.equals("end")) {
		    	requirementsNamesEntrys.add(entry);
		    } else {
		    	break;
		    }
	    }
		scan.close();
	    File report = new File(reportCSVFilename);
	    File inProgress = new File(inProgressExcelFilename);
	    FileInputStream reportInputStream = null;
	    FileInputStream inProgressInputStream = null;
		
		try {
			reportInputStream = new FileInputStream(report);
			Workbook reportWorkbook = WorkbookFactory.create(reportInputStream);
			Sheet reportSheet = reportWorkbook.getSheetAt(0);
			inProgressInputStream = new FileInputStream(inProgress);
			Workbook inprogressWorkbook = WorkbookFactory.create(inProgressInputStream);
			Sheet inProgressSheet = reportWorkbook.getSheetAt(0);
			DataFormatter formatter = new DataFormatter();
			for (int i = 0; i < inProgressSheet.getLastRowNum(); i++) {
				Row inProgressRow = null;
				if (i == 0) {
					inProgressRow = inProgressSheet.getRow(0);
					for (int k = 0; k < inProgressRow.getLastCellNum(); k++) {
						for (String inProgrssColumnEntry: columnNamesEntrys) {
							if (inProgressRow.getCell(k).getStringCellValue().toLowerCase().equals(inProgrssColumnEntry)) {
								inProgressColumnNums.add(formatter.formatCellValue(inProgressRow.getCell(k)));
							} else {
								inProgressColumnNums.add("-1");
							}
						}
						for (String inProgrssRequirementEntry: requirementsNamesEntrys) {
							if (inProgressRow.getCell(k).getStringCellValue().toLowerCase().equals(inProgrssRequirementEntry)) {
								inProgressRequirementsNums.add(formatter.formatCellValue(inProgressRow.getCell(k)));
							} else {
								inProgressRequirementsNums.add("-1");
							}
						}
					}
					continue;
				} else {
					inProgressRow = inProgressSheet.getRow(i);
				}
				for (int j = 0; j < reportSheet.getLastRowNum(); j++) {
					Row reportRow = null;
					if (j == 0) {
						reportRow = reportSheet.getRow(0);
						for (int k = 0; k < reportRow.getLastCellNum(); k++) {
							for (String reportColumnEntry: columnNamesEntrys) {
								if (reportRow.getCell(k).getStringCellValue().toLowerCase().equals(reportColumnEntry)) {
									reportColumnNums.add(formatter.formatCellValue(inProgressRow.getCell(k)));
								} else {
									reportColumnNums.add("-1");
								}
							}
							for (String reportColumn: requirementsNamesEntrys) {
								if (reportRow.getCell(k).getStringCellValue().toLowerCase().equals(reportColumn)) {
									reportRequirementsNums.add(formatter.formatCellValue(reportRow.getCell(k)));
								} else {
									reportRequirementsNums.add("-1");
								}
							}
						}
						continue;
					} else {
						reportRow = reportSheet.getRow(j);
					}
					
					if(reportRequirementsNums.size() >= inProgressRequirementsNums.size()) {
						for (int v = 0; v < reportRequirementsNums.size(); v++) {
							for (int c = 0; c < inProgressRequirementsNums.size(); c++) {
								String temp1 = reportRequirementsNums.get(v);
								String temp2 = inProgressRequirementsNums.get(c);
								if (reportRequirementsNums.get(v).equals(inProgressRequirementsNums.get(c)) && !reportRequirementsNums.get(v).equals("-1")) {
									String temp3 = formatter.formatCellValue(inProgressRow.getCell(c));
									String temp4 = formatter.formatCellValue(reportRow.getCell(v));
									if(inProgressRow.getCell(c).equals(reportRow.getCell(v))) {										
										//loop through column names and place the report value in the inprogress sheet
										for (String columnName: columnNamesEntrys) {
											if (reportColumnNums.contains(columnName) && inProgressColumnNums.contains(columnName)) {
												int inProgressIndex = inProgressColumnNums.indexOf(columnName);
												int reportValueIndex = reportColumnNums.indexOf(columnName);
												String reportValue = reportColumnNums.get(reportValueIndex);
												Cell destinationCell = inProgressRow.getCell(inProgressIndex);
												destinationCell.setCellValue(reportValue);
											}
										}
									}
								}
							}
						}
					} else {
						//do reverse of the above funct. Loop through to find equal requirements and loop again to place the desired value from the 
						//report in the inprogress report
						for (int v = 0; v < inProgressRequirementsNums.size(); v++) {
							for (int c = 0; c < reportRequirementsNums.size(); c++) {
								if (reportRequirementsNums.get(c).equals(inProgressRequirementsNums.get(v)) && !reportRequirementsNums.get(v).equals("-1")) {
									if(inProgressRow.getCell(v).equals(reportRow.getCell(c))) {										
										//loop through column names and place the report value in the inprogress sheet
										for (String columnName: columnNamesEntrys) {
											if (reportColumnNums.contains(columnName) && inProgressColumnNums.contains(columnName)) {
												int inProgressIndex = inProgressColumnNums.indexOf(columnName);
												int reportValueIndex = reportColumnNums.indexOf(columnName);
												String reportValue = reportColumnNums.get(reportValueIndex);
												Cell destinationCell = inProgressRow.getCell(inProgressIndex);
												destinationCell.setCellValue(reportValue);
											}
										}
									}
								}
							}
						}
					}
				}
			}
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
public class populate_Info_CSV {
    
}
