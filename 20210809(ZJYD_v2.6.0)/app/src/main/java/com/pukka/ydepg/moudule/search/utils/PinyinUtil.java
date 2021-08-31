package com.pukka.ydepg.moudule.search.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PinyinUtil {

    //入参为单个汉字的char
    private static List<String> duoYinZi(char zi) throws Exception {
        System.out.println("单个汉字char：["+zi+"]");

        // 汉字转拼音配置对象
        HanyuPinyinOutputFormat config = new HanyuPinyinOutputFormat();
        config.setCaseType(HanyuPinyinCaseType.UPPERCASE);      // 大写
        config.setToneType(HanyuPinyinToneType.WITHOUT_TONE);  	// 没有音调数字
        // 取得当前汉字的所有汉语拼音全拼  如"长"->[zhang,chang]
        String[] pyArray = PinyinHelper.toHanyuPinyinStringArray(zi, config);

        List<String> pinyinName = new ArrayList<String>();
        if (pyArray != null && pyArray.length > 0) {
            //for循环处理当前汉字所有拼音(多音字)
            for (int pyIndex = 0; pyIndex < pyArray.length; pyIndex++) {
                // 取拼音的首字母
                char first = pyArray[pyIndex].charAt(0);
                int index = pinyinName.indexOf(String.valueOf(first));
                if (index < 0) {
                    pinyinName.add(String.valueOf(first));
                }
            }
        } else {
            //无拼音则直接输出,如特殊(中文)符号"（"
            pinyinName.add(String.valueOf(zi));
        }
        return pinyinName;
    }

    /**
     * 汉字转换位汉语拼音首字母
     * 英文字符不变，特殊字符丢失 支持多音字
     * 生成方式如（长沙市长:cssc,zssz,zssc,cssz）
     */
    public static Set<String> converterToFirstSpell(String chines) throws Exception {
        System.out.println("输入中文是：["+chines+"]");
        if (chines.isEmpty()) {
            return null;
        }
        // 打散字符串
        char[] ziCharArray = chines.toCharArray();
        // 拼音集合
        List<List<String>> list = new ArrayList<List<String>>();
        int size = ziCharArray.length;
        //循环处理中文字串的每一个汉字
        for (int i = 0; i < size; i++) {
            char c = ziCharArray[i];
            if (c > 128) {	// 128 ASCII码 可见字符
                //是汉字(中文字符算汉字,将在多音字方法List<String> duoYinZi(char)中处理)转拼音后作为输出结果
                List<String> result = duoYinZi(c);
                list.add(result);
            } else {
                //ascii可见字符和数字等直接作为输出结果
                List<String> result = new ArrayList<String>();
                result.add(String.valueOf(ziCharArray[i]));
                list.add(result);
            }
        }
        System.out.println("单个汉字转首字母(含多音字) List<List<String>>:"+list);
        return parseTheChineseByObject(list);
    }

    //获取汉字字串的所有首拼音字母组合
    //如输入(长沙市长)[[Z, C], [S], [S], [Z, C]] => 则输出[ZSSC, CSSZ, ZSSZ, CSSC]
    private static Set<String> parseTheChineseByObject(List<List<String>> list) {
        Set<String> result = new HashSet<String>();
        if (list != null && list.size()>0) {
            result.addAll(list.get(0));
        }
        int size = list.size();
        for (int i = 1; i < size; i++) {
            Set<String> compositePYTemp = new HashSet<String>();
            for (String pinyinFast : result) {
                for (String c : list.get(i)) {
                    String str = pinyinFast + c;
                    compositePYTemp.add(str.toUpperCase());
                }
            }
            result = compositePYTemp;
        }
        return result;
    }
}
