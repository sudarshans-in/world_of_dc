package org.dcoffice.cachar.service;

import org.apache.poi.ss.usermodel.*;
import org.dcoffice.cachar.entity.PollingParty;
import org.dcoffice.cachar.entity.Member;
import org.dcoffice.cachar.repository.PollingPartyRepository;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class PollingPartyExcelService {

    private final PollingPartyRepository repository;

    public PollingPartyExcelService(PollingPartyRepository repository) {
        this.repository = repository;
    }

    public void uploadExcel(InputStream inputStream) {

        try (Workbook workbook = WorkbookFactory.create(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);
            List<PollingParty> list = new ArrayList<>();

            boolean isHeader = true;

            for (Row row : sheet) {

                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                PollingParty p = new PollingParty();

                p.setAcNo(get(row, 0));
                p.setPsNo(get(row, 1));
                p.setPsName(get(row, 2));
                p.setPartyNo(get(row, 3));

                // Create members list with roles
                List<Member> members = new ArrayList<>();
                
                String presidingOfficer = get(row, 4);
                if (!presidingOfficer.isBlank()) {
                    members.add(new Member("PRESIDING_OFFICER", presidingOfficer, get(row, 9)));
                }
                
                String pollingOfficer1 = get(row, 5);
                if (!pollingOfficer1.isBlank()) {
                    members.add(new Member("POLLING_OFFICER_1", pollingOfficer1, ""));
                }
                
                String pollingOfficer2 = get(row, 6);
                if (!pollingOfficer2.isBlank()) {
                    members.add(new Member("POLLING_OFFICER_2", pollingOfficer2, ""));
                }
                
                String pollingOfficer3 = get(row, 7);
                if (!pollingOfficer3.isBlank()) {
                    members.add(new Member("POLLING_OFFICER_3", pollingOfficer3, ""));
                }
                
                String reserveOfficer = get(row, 8);
                if (!reserveOfficer.isBlank()) {
                    members.add(new Member("RESERVE_OFFICER", reserveOfficer, ""));
                }
                
                p.setMembers(members);

                p.setUploadTime(System.currentTimeMillis());

                list.add(p);
            }

            repository.saveAll(list);

        } catch (Exception e) {
            throw new RuntimeException("Excel upload failed: " + e.getMessage());
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