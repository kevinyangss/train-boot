package com.kevin.excel;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @ClassName ExcelUtil
 * @Description TODO
 * @Author kevin.yang
 * @Date 2021/4/29 16:34
 */
public class ExcelUtil {

    private static final Logger logger = LoggerFactory.getLogger(ExcelUtil.class.getName());

    public static SXSSFWorkbook exportExcel(String[] title, String sheetName, List list) {
        SXSSFWorkbook wb = new SXSSFWorkbook();
        int count = 1;
        CountDownLatch downLatch = new CountDownLatch(count);
        Executor executor = Executors.newFixedThreadPool(count);
        SXSSFSheet sheet = wb.createSheet(sheetName);
        CellStyle style = wb.createCellStyle();
        style.setWrapText(true);
        executor.execute(new PageTask(downLatch, sheet, title, style, list));
        try {
            downLatch.await();
        } catch (InterruptedException e) {
            logger.error(">>>>>>exportExcel downLatch await error: " + e.getMessage(), e);
        }
        return wb;
    }
}
