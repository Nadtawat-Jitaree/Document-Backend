package com.document.service;

import java.io.InputStream;
import java.util.Base64;
import java.util.Map;


import org.springframework.stereotype.Service;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;


@Slf4j
@Service
public class ReportService {

    @SneakyThrows
    public String exportPDF(String fileReportName, Map<String, Object> parameters, JRDataSource dataSource) {
        InputStream in = getClass().getResourceAsStream("/reports/" + fileReportName);
        // Compile the Jasper report from .jrxml to .japser
        JasperReport jasperReport = JasperCompileManager.compileReport(in);

        // Fill the report
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,
                dataSource);

        byte[] bReport = JasperExportManager.exportReportToPdf(jasperPrint);
        return new String(Base64.getEncoder().encode(bReport));
    }

    

  

}
