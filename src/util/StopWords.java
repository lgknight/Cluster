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
public class StopWords {
    private static final HashSet stopWords = new HashSet();
    static{
        final String stopwordsPath = System.getProperty("user.dir").concat(File.separator).concat(PropertyHandler.getProperty("stopWordsFile"));
        List<String> words = FileUtil.readLines(stopwordsPath);
        stopWords.addAll(words);
    }

    public static boolean isStopWord(String word){
        return stopWords.contains(word);
    }

    public String toString(){
        return "stopwords.data : " + stopWords.toString();
    }


    public static void main(String[] args) {
        // TODO Auto-generated
        System.out.println(stopWords.toString());
    }
}
