package com.kevin.excel;

import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName ExportExcelController
 * @Description TODO
 * @Author kevin.yang
 * @Date 2021/4/29 16:40
 */
@RestController
@RequestMapping("excel")
public class ExportExcelController {

    private static final Logger logger = LoggerFactory.getLogger(ExportExcelController.class.getName());

    public static final String[] TITLE = new String[] {"id", "name", "address", "mobile", "remark"};
    public static final String SHEET_NAME = "testsheet";

    @GetMapping(value = "/export", produces = "application/json")
    @ResponseBody
    public String exp2Excel(HttpServletResponse response) {
        String rtnMsg = "success";
        SXSSFWorkbook wb = null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String date = dateFormat.format(new Date());
            String fileName = date + ".xlsx";
            List demoDataList = genData();
            int size = demoDataList.size();
            logger.info(">>>>>>list：{" + size + "}");
            long start = System.currentTimeMillis();
            wb = ExcelUtil.exportExcel(TITLE, SHEET_NAME, demoDataList);// 从数据库select语句返回的list构建基于SXSSFWorkbook的excel对象wb
            long millis = System.currentTimeMillis() - start;
            long second = millis / 1000;
            logger.info(">>>>>>SXSSF Page Thread 导出" + size + "条数据，花费：" + second + "s/ " + millis + "ms");
            writeAndClose(response, fileName, wb);//把excel的对象写出到response流
        } catch (Exception e) {
            logger.error(">>>>>>export to excel error: " + e.getMessage(), e);
            rtnMsg = "failed" + ": " + e.getMessage();
        } finally {
            try {
                wb.dispose();
            } catch (Exception e) {
            }
        }
        return rtnMsg;
    }

    private List genData(){
        int maxrow = SpreadsheetVersion.EXCEL2007.getLastRowIndex();
        List<PersonDto> list = new ArrayList<>(maxrow);

        PersonDto dataBean = null;
        for (int i = 0; i < maxrow; i++) {
            dataBean = new PersonDto(i, "name-" + i, "address-" + i, "18888888888", "remark-" + i);
            list.add(dataBean);
        }

        return list;
    }

    private void writeAndClose(HttpServletResponse response, String fileName, Workbook wb) throws Exception {
        OutputStream os = null;
        try {
            this.setResponseHeader(response, fileName);
            os = response.getOutputStream();
            wb.write(os);
        } catch (Exception e) {
            throw new Exception("write excel content to response error: " + e.getMessage(), e);
        } finally {
            try {
                os.flush();
            } catch (Exception e) {
            }
            try {
                os.close();
            } catch (Exception e) {
            }
        }
    }

    private void setResponseHeader(HttpServletResponse response, String fileName) throws Exception {
        try {

            fileName = new String(fileName.getBytes(), "UTF-8");
            response.setContentType("application/octet-stream;charset=ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
        } catch (Exception ex) {
            throw new Exception("output excel to response header error: " + ex.getMessage(), ex);
        }
    }
}
