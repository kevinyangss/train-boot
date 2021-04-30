package com.kevin.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * @ClassName PageTask
 * @Description TODO
 * @Author kevin.yang
 * @Date 2021/4/29 16:37
 */
public class PageTask implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(PageTask.class.getName());

    private CountDownLatch countDownLatch;
    private Sheet sheet;
    private String[] title;
    private CellStyle style;
    private List list;

    private Map<String, Integer> colNameMap = new HashMap<>();

    public PageTask(CountDownLatch countDownLatch, Sheet sheet, String[] title, CellStyle style, List list) {
        this.countDownLatch = countDownLatch;
        this.sheet = sheet;
        this.title = title;
        this.style = style;
        this.list = list;
    }

    @Override
    public void run() {
        List<String[]> excelRows = null;
        try {
            Row row = sheet.createRow(0);
            Cell cell = null;
            for (int i = 0; i < title.length; i++) {
                cell = row.createCell(i);
                cell.setCellValue(title[i]);
                cell.setCellStyle(style);

                colNameMap.putIfAbsent(title[i], i);
            }
            excelRows = convertListToLink(colNameMap, this.list);
            logger.info(">>>>>>after convert to List<LinkedList<String>> the size->" + excelRows.size());
            for (int i = 0; i < list.size(); i++) {
                String[] list1 = excelRows.get(i);
                row = sheet.createRow(i + 1);
                for (int j = 0; j < title.length; j++) {
                    row.createCell(j).setCellValue(list1[j]);
                }
            }
        } catch (Exception e) {
            logger.error(">>>>>>PageTask error: " + e.getMessage(), e);
        } finally {
            if (countDownLatch != null) {
                countDownLatch.countDown();
            }
        }
    }

    public static List<String[]> convertListToLink(Map<String, Integer> colNameMap, List list) throws Exception {
        List<String[]> rows = new ArrayList<String[]>();
        try {
            for (int i = 0; i < list.size(); i++) {
//                LinkedList<String> linkedList = new LinkedList<>();
                String[] dataArr = new String[colNameMap.size()];
                Object entityBean = list.get(i);
                Method[] m = entityBean.getClass().getDeclaredMethods();
                for (int index = 0; index < m.length; index++) {
                    String methodName = m[index].getName().toLowerCase();
                    String methodNamePrefix = methodName.substring(0, 3);
                    // logger.info(">>>>>>method prefix->" + methodNamePrefix);
                    if (methodNamePrefix.equals("get")) {
                        String methodNameSuffix = methodName.substring(3);
                        // logger.info(">>>>>>getMethod->" + m[i].getName());
                        Integer idx = colNameMap.get(methodNameSuffix);
                        String value =
                                m[index].invoke(entityBean) == null ? "" : m[index].invoke(entityBean).toString();
//                        linkedList.add(value);
                        dataArr[idx] = value;
//                        linkedList.add(idx, value);
                    }
                }
                rows.add(dataArr);
            }
        } catch (Exception e) {
            logger.error(">>>>>>convertListToLink error: " + e.getMessage(), e);
            throw new Exception(">>>>>>convertListToLink error: " + e.getMessage(), e);
        }
        return rows;
    }
}
