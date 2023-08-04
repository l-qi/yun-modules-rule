package com.yun.rule.common;

import com.alibaba.csp.sentinel.util.StringUtil;
import com.alibaba.nacos.common.packagescan.resource.ClassPathResource;
import com.alibaba.nacos.common.packagescan.resource.Resource;
import com.yun.rule.model.Cnarea;
import com.yun.rule.model.Value;
import lombok.extern.log4j.Log4j2;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.yun.rule.enums.RuleTypeEnum.ADDRESS;
import static com.yun.rule.enums.RuleTypeEnum.ZIP;

/**
 * @author 琪
 * 2023/7/28
 */
public class RegionZipCommon {

    /**
     * 城市信息和邮编
     */
    private static final String canareaPath = "cnarea_2023.csv";

    /**
     * @param data data
     */
    public static void checkRegionZip(Map<String, Value> data) {
        //地址
        Value addrValue = data.get(ADDRESS.getCode());
        String address = (String) addrValue.getVal();
        //邮编
        Value zipValue = data.get(ZIP.getCode());
        String zip = (String) zipValue.getVal();

        try {
            List<Cnarea> cnareas = getCnarea();
            if (StringUtil.isNotEmpty(address) && StringUtil.isNotEmpty(zip)) {
                //对比地址和邮编是否正确
                //获取地址所在地
                String getzip = getZip(address, cnareas);
                //如果地址错误就通过邮编去获取地址
                if (!zip.equals(getzip)) {
                    addrValue.setVal(getAddress(zip, cnareas));
                }
            }
            if (StringUtil.isNotEmpty(address) && StringUtil.isEmpty(zip)) {
                //根据邮编获取地址
                addrValue.setVal(getAddress(zip, cnareas));
            }
            if (StringUtil.isEmpty(address) && StringUtil.isNotEmpty(zip)) {
                //根据地址获取邮编
                zipValue.setVal(getZip(address, cnareas));
            }

        } catch (Exception e) {
        }
    }

    /**
     * 获取地址
     *
     * @param zip     邮编
     * @param cnareas 城市信息
     * @return 地址
     */
    private static String getAddress(String zip, List<Cnarea> cnareas) {
        Cnarea cnarea = cnareas.stream()
                .filter(c -> c.getZipCode().equals(zip))
                .collect(Collectors.toList()).get(0);
        return getAddress(cnarea.getName(), cnareas, cnarea.getParentCode());
    }

    private static String getAddress(String address, List<Cnarea> cnareas, String areaCode) {
        Cnarea cnarea = cnareas.stream()
                .filter(c -> c.getAreaCode().equals(areaCode))
                .collect(Collectors.toList()).get(0);
        //拼接地址
        if (cnarea.getLevel().equals("1")) {
            return cnarea.getName() + address;
        }
        return getAddress(cnarea.getName() + address, cnareas, cnarea.getParentCode());
    }

    /**
     * 通过地址获取邮编
     *
     * @param address 地址
     */
    private static String getZip(String address, List<Cnarea> cnareas) {
        List<Cnarea> collect = cnareas.stream()
                .filter(cnarea -> address.contains(cnarea.getMergerName().split(",")[0]))
                .collect(Collectors.toList());
        return getZip(address, collect, 1);
    }

    private static String getZip(String address, List<Cnarea> cnareas, final int i) {
        List<Cnarea> collect = cnareas.stream()
                .filter(cnarea -> {
                    String[] split = cnarea.getMergerName().split(",");
                    if (split.length > i)
                        return address.contains(split[i]);
                    return false;
                })
                .collect(Collectors.toList());
        if (collect.size() > 0) {
            return getZip(address, collect, i + 1);
        }
        return cnareas.get(0).getZipCode();
    }


    /**
     * 获取城市信息及邮编
     *
     * @return
     */
    private static List<Cnarea> getCnarea() throws IOException {

        Resource resource = new ClassPathResource(canareaPath);
        InputStream is = resource.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);

        List<Cnarea> cnareas = new ArrayList<>();
        try {
            String data;
            while ((data = br.readLine()) != null) {
                String[] split = data.split(",");
                Cnarea cnarea = new Cnarea(split[0], split[1], split[2], split[3], split[4], split[5], split[6], mergerName(split));
                cnareas.add(cnarea);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            br.close();
            isr.close();
            is.close();
        }
        return cnareas;
    }

    private static String mergerName(String[] split) {
        StringBuilder mergerName = new StringBuilder();
        for (int i = 7; i < split.length; i++) {
            mergerName.append(",").append(split[i]);
        }
        return mergerName.substring(1);
    }
}
