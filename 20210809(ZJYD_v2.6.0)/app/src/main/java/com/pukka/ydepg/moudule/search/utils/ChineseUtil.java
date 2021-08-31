package com.pukka.ydepg.moudule.search.utils;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;

import androidx.annotation.ColorInt;

import com.pukka.ydepg.common.utils.LogUtil.SuperLog;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.util.Set;

public class ChineseUtil {

    private final static String TAG = ChineseUtil.class.getSimpleName();

    //匹配关键字高亮色 浅绿色
    @ColorInt
    private static final int MATCH_COLOR = 0xff21b02f;

    private static String searchKey = null;

    /**
     * 获取汉字串拼音首字母，英文/数字字符不变
     * @param chinese 汉字串
     * @return 汉语拼音首字母
     */
    public static String getFirstSpell(String chinese) {
        StringBuilder pybf = new StringBuilder();
        char[] arr = chinese.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > 128) {
                try {
                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(arr[i], defaultFormat);
                    if (temp != null && temp.length>0) { //汉语特殊字符如【等获取拼音时temp为空数组,因此需要判断temp.length>0
                        pybf.append(temp[0].charAt(0));
                    } else {
                        pybf.append(arr[i]);
                    }
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            } else {
                //arr[i] <= 128为ASCII码特殊字符
                pybf.append(arr[i]);
            }
        }
        //return pybf.toString().replaceAll("\\W", "").trim(); // \W匹配特殊字符 即非字母、非数字、非汉字、非_
        return pybf.toString().toUpperCase();//else分支可能加入小写字母(即原始字符串中的小写英文字母)
    }

    /**
     * Code shared by String and AbstractStringBuilder to do searches. The
     * source is the character array being searched, and the target
     * is the string being searched for.
     * 区别:可以包含特殊字符搜索,比如对字符串"dAfac]1svcxv2"搜索字符串"cs"则返回3,即"c]s"可以命中搜索
     *     返回值int[0]=4,int[3]=
     *
     * @param   source       the characters being searched.
     * @param   target       the characters being searched for.
     * @param   fromIndex    the index to begin searching from.
     * @return  int[0] : the index of the first occurrence of the specified substring,starting at the specified index,
     *                   or {@code -1} if there is no such occurrence.
     *          int[1] : the index of the last  occurrence of the specified substring,starting at the specified index,
     *                   or {@code -1} if there is no such occurrence
     */
    private static int[] indexIncludeSpecial(String source, String target, int fromIndex){
        int[] result = new int[2];
        result[0] = -1;
        result[1] = -1;
        final int sourceLength = source.length();
        final int targetLength = target.length();
        if (fromIndex >= sourceLength) {
            return result;
        }
        if (fromIndex < 0) {
            fromIndex = 0;
        }
        if (targetLength == 0) {
            result[0] = fromIndex;
            result[1] = fromIndex;
            return result;
        }

        char firstChar = target.charAt(0);

        //在源字符串中最大可能匹配的位置index
        int max = (sourceLength - targetLength);

        for (int i = fromIndex; i <= max; i++) {
            /* Look for first character. */
            if (source.charAt(i)!= firstChar) {
                continue;
            }

            /* Found first character, now look at the rest of v2 */
            // i=源串中第一个匹配目标串的index
            int j = i + 1;//
            // j源串cursor
            // k目标串cursor
            int k;
            for (k = 1 ; k<targetLength ; j++){
                //源串中除了[英文/数字/空格]以外的字符认为是特殊字符,跳过继续比对下一个字符
                while ( j<sourceLength && !isNumberOrLetter(source.charAt(j))){
                    j++;
                }

                if(source.charAt(j) == target.charAt(k)){
                    k++;
                    continue;
                }else{
                    break;//结束内层循环
                }
            }

            if (k == targetLength) {
                /* Found whole string. */
                result[0] = i;
                result[1] = j;
                break;//结束外层循环
            }
        }
        return result;
    }

    //判断一个字符是否是英文字母或数字或空格
    private static boolean isNumberOrLetter(char c){
        // c==' ' 空格认为是普通字符加入比对
        if(('0'<=c&&c<='9')||('A'<=c&&c<='Z')||c==' '){
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取汉字串拼音首字母，英文字符不变
     * @param  text 文本内容
     * @return 高亮匹配关键词的文本内容
     */
    public static SpannableStringBuilder getHighLightBuilder(String text){
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        try{
            //获取源字符串的首字母拼音(大写)
            String firstSpell = getFirstSpell(text);

            final Set<String> setFirstSpell = PinyinUtil.converterToFirstSpell(text);

            //匹配关键字
            String key = searchKey;

            //不支持多音字的方法
            key = getFirstSpell(key);

            final int keyLength   = key.length();       //搜索[词]的长度
            final int textLength  = text.length();      //搜索结果[内容名字汉字等]长度
            final int spellLength = firstSpell.length();//搜索结果[内容名字拼音首字母]长度


            //builder.setSpan(new AbsoluteSizeSpan(getResources().getDimensionPixelSize(R.dimen.search_result_name_text_size), false), 0 , text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            if(spellLength != textLength){
                SuperLog.error(TAG,"Length Error spellLength != textLength ,text="+text+" spell="+firstSpell);
                return builder;
            }

            boolean result = false;
            //多音字每个音都处理
            for(String singleTone : setFirstSpell){
                firstSpell = singleTone.toUpperCase();

                int[] match;
                int index = 0;
                final int max = textLength - keyLength;
                while (index<=max){
                    match = indexIncludeSpecial(firstSpell,key,index);
                    if(match[0]>=0){
                        index = match[1];
                        builder.setSpan(new ForegroundColorSpan(MATCH_COLOR), match[0],index, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        result = true;
                    } else {
                        break;
                    }
                }

                if(result){
                    break;
                }
            }

        } catch (Exception e){
            SuperLog.error(TAG,"Text="+text+" key="+searchKey);
            SuperLog.error(TAG,e);
        }
        return builder;
    }

    public static void setSearchKey(String searchKey) {
        ChineseUtil.searchKey = searchKey;
    }
}