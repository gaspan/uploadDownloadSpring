package PernikApplication.example.test.controller;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

@RestController
public class TestController {

    final Logger LOGGER = (Logger) LoggerFactory.getLogger(TestController.class);
    @GetMapping("/download")
    public void downloadExcel(HttpServletResponse response)
            throws IOException {
        final Logger LOGGER = (Logger) LoggerFactory.getLogger(TestController.class);
        try (Workbook workbook = new HSSFWorkbook()) {
            Sheet spreadsheet = workbook.createSheet("excel template");
            HSSFRow row;
            Map<String, Object[]> dataDiri = new TreeMap<String, Object[]>();
            dataDiri.put("1", new Object[]{"No", "Nama", "Alamat"});

            Set<String> keyId = dataDiri.keySet();
            int rowid = 0;
            for (String key : keyId) {
                row = (HSSFRow) spreadsheet.createRow(rowid++);
                Object[] objectArr = dataDiri.get(key);
                int cellid = 0;

                for (Object obj : objectArr) {
                    Cell cell = row.createCell(cellid++);
                    cell.setCellValue((String) obj);
                }
            }
            response.setContentType("application/octet-stream");
            DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
            String currentDateTime = dateFormatter.format(new Date());

            String headerKey = "Content-Disposition";
            String headerValue = "attachment; filename=\"users_" + currentDateTime + ".xlsx\"";
            response.setHeader(headerKey, headerValue);

            ServletOutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            workbook.close();

            outputStream.close();
        }
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadData(@RequestParam("file") MultipartFile file) throws IOException {
//        File convertFile = new File("/var/tmp/" + file.getOriginalFilename());
        File convertFile = new File("/home/ariya/java/latihan/uploadDownload/test (1)/test/src/main/resources/" + file.getOriginalFilename());
        convertFile.createNewFile();

        FileOutputStream fout = new FileOutputStream(convertFile);

        LOGGER.info("ini log", convertFile.getAbsolutePath());
        fout.write(file.getBytes());
        fout.close();
        return "File is upload Successfully";
    }

}
