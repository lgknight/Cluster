package util;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @Author : Lknight
 * @Date : 2017/5/22
 * @Description : 提供文件读写接口
 * @Version : v2.0
 */
public class FileUtil {


    /**
     * @Description： 按行读取绝对路径文件
     * @Parameters: absolutePath-绝对路径
     * @Return： 每行作为列表的一个元素，返回所有行列表
     */
    public static List<String> readLines(String absolutePath){
        File file = new File(absolutePath);
        LinkedList<String> lines = new LinkedList<String>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            try {
                String line = br.readLine();
                while (line != null) {
                    lines.add(line);
                    line = br.readLine();
                }
            } catch (IOException ioe){
                ioe.printStackTrace();
            } finally {
                if(null != br) {
                    try {
                        br.close();
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                }
            }
        } catch (FileNotFoundException fnfe){
            fnfe.printStackTrace();
        }
        return new ArrayList<String>(lines);
    }


    /**
     * @Description： 按行写入文件
     * @Parameters: absolutePath-绝对路径，文件存在则覆盖
     * lines-待写入的数据行
     * @Return： void
     * @Example:
     * String path = "E:\\1\\data";
     * ArrayList<String> data = new ArrayList<>();
     * data.add("first line");
     * data.add("second line");
     * writeLines(path, data);
     * */
    public static void writeLines(String absolutePath, List<String> lines){
        File file = new File(absolutePath);
        if(lines.size() != 0) {
            try {
                if (!file.exists()) {
                    file.createNewFile();
                }
                FileWriter fileWriter = new FileWriter(absolutePath);
                BufferedWriter bufferWriter = new BufferedWriter(fileWriter);
                bufferWriter.write(lines.get(0));
                for (int i = 1; i < lines.size(); i++) {
                    bufferWriter.write("\n" + lines.get(i));
                }
                if (null != bufferWriter) {
                    bufferWriter.close();
                }
                if (null != fileWriter) {
                    fileWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @Description: Create a directory by calling mkdir();
     * @Parameters: dirFile
     */
    public static void mkdir(File dirFile) {
        try {
            if (dirFile.exists() == true) {
                System.err.println("The folder exists.");
            } else {
                System.err.println("The folder do not exist,now trying to create one...");
                if (dirFile.mkdir() == true) {
                    System.out.println("Create successfully!");
                } else {
                    System.err.println("Disable to make the folder,please check the disk is full or not.");
                }
            }
        } catch (Exception err) {
            System.err.println("ELS - Chart : unexpected error");
            err.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // TODO Auto-generated

//        String path = "E:\\1\\data";
//        ArrayList<String> data = new ArrayList<String>();
//        data.add("first line");
//        data.add("second line");
//        writeLines(path, data);
    }
}
