package com.xyzcorp.api.emissionscalculator.utils;

import com.xyzcorp.api.emissionscalculator.dto.CompanyFleetDto;
import com.xyzcorp.api.emissionscalculator.exception.FleetFileParsingException;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static java.lang.String.format;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Component
@AllArgsConstructor
public class FleetFileOperations {

    public List<CompanyFleetDto> parse(MultipartFile file) {
        List<CompanyFleetDto> fleets = new ArrayList<>();

        try(Workbook workbook = new XSSFWorkbook(file.getInputStream())) {

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> itr = sheet.iterator();
            while (itr.hasNext()) {
                Row row = itr.next();
                if(row.getRowNum() == 0) { // skip first header row
                    continue;
                }
                if(isNull(row.getCell(0, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL))) {
                    break; // assume file reach the end and inspected black row.
                }
                // collect sheet info
                fleets.add(CompanyFleetDto.builder()
                        .employeeId(map(row,0))
                        .vehicle(map(row, 1))
                        .mileage(Double.valueOf(map(row, 2)))
                        .build());
            }
        } catch (Exception e) {
            throw new FleetFileParsingException(format("Unable to process the file. Error [%s]", e.getMessage()),
                    "E104", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return fleets;
    }

    private String map(Row row, int cellIndex) {
        if(nonNull(row) && nonNull(row.getCell(cellIndex))) {
            Cell cell = row.getCell(cellIndex);
            if(cell.getCellType() == CellType.NUMERIC) {
                return cell.getNumericCellValue() + "";
            } else if(cell.getCellType() == CellType.STRING) {
                return cell.getStringCellValue();
            } else if(cell.getCellType() == CellType.BOOLEAN) {
                return Boolean.toString(cell.getBooleanCellValue()) ;
            } else if(cell.getCellType() == CellType.BLANK) {
                return cell.getStringCellValue();
            } else {
                try {
                    return cell.getStringCellValue();
                } catch (Exception e) {
                    throw new FleetFileParsingException(format("unable to map file contents. Error [%s]",
                            e.getMessage()), "E105", HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
        }
        return null;
    }

}
