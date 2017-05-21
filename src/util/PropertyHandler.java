package util;

import java.io.*;
import java.util.Properties;

/**
 * @Author : Lknight
 * @Date : 2017/5/20
 * @Description : 通过读取global.properties配置文件，加载全局参数
 * 配置信息包括数据库连接信息，消息队列信息等
 * @Version : v1.0
 */

public class PropertyHandler {
    private static Properties properties = new Properties();
    private static final String dirPath = System.getProperty("user.dir");
    private static final String propertyPath = dirPath.concat(File.separator).concat("global.properties");
    static{
        try{
            InputStream in = new BufferedInputStream(new FileInputStream(propertyPath));
            properties.load(in);
            in.close();
        } catch (FileNotFoundException fnfe){
            fnfe.printStackTrace();
        } catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

    public static Properties getProperties(){
        return properties;
    }

    /**
     * @Description: 根据key获取对应配置参数
    * */
    public static String getProperty(String key){
        return properties.getProperty(key);
    }

    public static void main(String[] args){
        System.out.println(PropertyHandler.getProperties());
    }

}
