package util;

import java.io.File;
import java.util.HashSet;
import java.util.List;

/**
 * @Author : Lknight
 * @Date : 2017/5/22
 * @Description : 通过停用词文件加载停用词表
 * @Version : v1.0
 */
public class Stopwords {
    private static final HashSet stopwords = new HashSet();
    static{
        final String stopwordsPath = System.getProperty("user.dir").concat(File.separator).concat(PropertyHandler.getProperty("stopwordsFile"));
        List<String> words = FileUtil.readLines(stopwordsPath);
        stopwords.addAll(words);
    }

    public static boolean isStopword(String word){
        return stopwords.contains(word);
    }

    public String toString(){
        return "stopwords.data : " + stopwords.toString();
    }


    public static void main(String[] args) {
        // TODO Auto-generated
        System.out.println(stopwords.toString());
    }
}
