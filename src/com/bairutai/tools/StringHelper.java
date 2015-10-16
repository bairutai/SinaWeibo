package com.bairutai.tools;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class StringHelper {
    /** 
     * 得到 全拼 
     *  
     * @param src 
     * @return 
     */  
    public static String getPingYin(String str) {  
        char[] array = null;  
        array = str.toCharArray();  
        String[] string_array = new String[array.length];  
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();  
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);  
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);  
        format.setVCharType(HanyuPinyinVCharType.WITH_V);  
        String string = "";  
        int length = array.length;  
        try {  
            for (int i = 0; i < length; i++) {  
                // 判断是否为汉字字符  
                if (java.lang.Character.toString(array[i]).matches( "[\\u4E00-\\u9FA5]+")) {  
                	string_array = PinyinHelper.toHanyuPinyinStringArray(array[i], format);  
                	string += string_array[0];  
                } else {  
                	string += java.lang.Character.toString(array[i]);  
                }  
            }  
            return string;  
        } catch (BadHanyuPinyinOutputFormatCombination e1) {  
            e1.printStackTrace();  
        }  
        return string;  
    }  
  
    /** 
     * 得到首字母 
     *  
     * @param str 
     * @return 
     */  
    public static String getHeadChar(String str) {  
  
        String convert = "";  
        char word = str.charAt(0);  
        String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);  
        if (pinyinArray != null) {  
            convert += pinyinArray[0].charAt(0);  
        } else {  
            convert += word;  
        }  
        return convert.toUpperCase();  
    }  
      
    /** 
     * 得到中文首字母缩写 
     *  
     * @param str 
     * @return 
     */  
    public static String getPinYinHeadChar(String str) {  
  
        String convert = "";  
        for (int j = 0; j < str.length(); j++) {  
            char word = str.charAt(j);  
            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);  
            if (pinyinArray != null) {  
                convert += pinyinArray[0].charAt(0);  
            } else {  
                convert += word;  
            }  
        }  
        return convert.toUpperCase();  
    } 
}
