package org.dcoffice.cachar.service;

import org.apache.poi.ss.usermodel.*;
import org.dcoffice.cachar.entity.VehicleDetails;
import org.dcoffice.cachar.repository.VehicleRepository;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class VehicleExcelService {

    private final VehicleRepository repository;

    public VehicleExcelService(VehicleRepository repository) {
        this.repository = repository;
    }

    public void uploadExcel(InputStream inputStream) {

        try (Workbook workbook = WorkbookFactory.create(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);
            List<VehicleDetails> list = new ArrayList<>();

            boolean isHeader = true;

            for (Row row : sheet) {

                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                VehicleDetails v = new VehicleDetails();

                // 🔗 Linking fields (same as polling party)
                v.setAcNo(get(row, 0));
                v.setPsNo(get(row, 1));
                v.setPsName(get(row, 2));

                // 🚗 Vehicle fields
                v.setVehicleNo(get(row, 3));
                v.setDriverName(get(row, 4));
                v.setDriverMobile(get(row, 5));
                v.setVehicleType(get(row, 6));

                // safe parsing
                String cap = get(row, 7);
                v.setCapacity(cap.isEmpty() ? 0 : Integer.parseInt(cap));

                v.setRoute(get(row, 8));
                v.setRemarks(get(row, 9));

                v.setUploadTime(System.currentTimeMillis());

                list.add(v);
            }

            repository.saveAll(list);

        } catch (Exception e) {
            throw new RuntimeException("Vehicle Excel upload failed: " + e.getMessage());
        }
    }

    private String get(Row row, int index) {
        Cell cell = row.getCell(index);
        if (cell == null) return "";

        switch (cell.getCellType()) {
            case STRING: return cell.getStringCellValue().trim();
            case NUMERIC: return String.valueOf((long) cell.getNumericCellValue());
            default: return "";
        }
    }
}