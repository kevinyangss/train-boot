package com.kevin.excel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName Test
 * @Description TODO
 * @Author kevin.yang
 * @Date 2021/4/29 17:40
 */
public class Test {

    public static void main(String[] args) throws Exception {
        String[] title = new String[] {"id", "name", "address", "mobile", "remark"};
        List list = genData();
        Map<String, Integer> colNameMap = new HashMap<>();
        for (int i = 0; i < title.length; i++) {
            colNameMap.putIfAbsent(title[i], i);
        }
        List<String[]> result = PageTask.convertListToLink(colNameMap, list);
        System.out.println(result);
    }

    private static List genData(){
        int rowNum = 10;
        List<PersonDto> list = new ArrayList<>(rowNum);

        PersonDto dataBean = null;
        for (int i = 0; i < rowNum; i++) {
            dataBean = new PersonDto(i, "name-" + i, "address-" + i, "18888888888", "remark-" + i);
            list.add(dataBean);
        }

        return list;
    }
}
