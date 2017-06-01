package model.dao;
import cluster.ResourceType;
import model.Resource;
import util.DBUtil;
import util.TimeUtil;

import java.util.*;

/**
 * @Author : Lknight
 * @Date : 2017/5/21
 * @Description : 新闻，博客，论坛内容的数据库交互
 * @Version : v1.0
 */
public class ResourceDAO {

    /**
     * @Description: 私有函数，代码公用
     * @Parameters: type-[n/b/f], sql-查询语句
     * @Return: 查询列表
     * */
    private static List<Resource> getResource(ResourceType type, String sql){
        DBUtil db = new DBUtil();
        LinkedList<Resource> news = new LinkedList<Resource>();
        ArrayList<HashMap> qs = db.select(sql);
        for(HashMap map : qs){
            if(null != map.get("id") && null != map.get("content")) {
                news.add(new Resource(type, map.get("id").toString(), map.get("content").toString()));
            }
        }
        db.close();
        return new ArrayList<Resource>(news);
    }

    /**
     * @Description: 根据参数获取不同表[Blog, News, Forum]
     * */
    public static List<Resource> getResource(ResourceType type){
        String querySql = "select " + type.name().toLowerCase() + "_article_id as id, chinese_content as content from " + type.name();
        return getResource(type, querySql);
    }

    /**
     * @Description: 获取一段时间内(几天)的文章
     * @Parameters: day-从此刻向前推day天
     * @Return: 此段时间内的文章列表
     * */
    public static List<Resource> getResource(ResourceType type, long day){
        long since = System.currentTimeMillis() - day * 24 * 60 * 60 * 1000;
        String sinceString = TimeUtil.format(since);
        String querySql = "select " + type.name().toLowerCase() + "_article_id as id, chinese_content as content from " + type.name() + " where real_time > '" + sinceString + "'";
        return getResource(type, querySql);
    }

    /**
     * @Description: 获取所有新闻
    * */
    public static List<Resource>getNews(){
        String querySql = "select news_article_id as id, chinese_content as content from News";
        return getResource(ResourceType.News, querySql);
    }

    /**
     * @Description: 获取一段时间内(几天)的新闻
     * @Parameters: day-从此刻向前推day天
     * @Return: 此段时间内的新闻列表
     * */
    public static List<Resource>getNews(long day){
        long since = System.currentTimeMillis() - day * 24 * 60 * 60 * 1000;
        String sinceString = TimeUtil.format(since);
        String querySql = "select news_article_id as id, chinese_content as content from News where real_time > '" + sinceString + "'";
        return getResource(ResourceType.News, querySql);
    }

    /**
     * @Description: 聚类结果写回数据库
     * @TODO
    * */

    public static void main(String[] args) {
        // TODO Auto-generated

        for(Resource rs : getNews(35)){
            System.out.println(rs.toString());
        }

    }
}
