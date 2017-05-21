package util;

import java.io.*;
import java.util.Properties;

/**
 * Created by Lknight on 2017/5/20.
 */
public class PropertyHandler {
    private static Properties properties = new Properties();
    private static final String dirPath = System.getProperty("user.dir");
    private static final String propertyPath = dirPath.concat(File.separator).concat(File.separator).concat("global.properties");
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

    public static String getProperty(String key){
        return properties.getProperty(key);
    }

    public static void main(String[] args){
        System.out.println(PropertyHandler.getProperties());
    }

}
