package model.dao;

import cluster.ResourceType;
import model.LDAResult;
import util.DBUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author : Lknight
 * @Date : 2017/6/1
 * @Description : TODO
 * @Version : v1.0
 */
public class LDAResultDAO {

    /**
     * @Description: 批量更新聚类结果
     * */
    public static void updateResource(ResourceType type, List<LDAResult> ldaResults){
        DBUtil db = new DBUtil(false);

        String updateSql = "update " + type.name() + " set topicId = ?, topicVoc = ? where " + type.name().toLowerCase() + "_article_id = ?";
        List<List> listParams = new ArrayList<List>();
        for(LDAResult lda : ldaResults){
            List params = new ArrayList();
            params.add(lda.getTopicId());
            params.add(lda.getTopicVoc());
            params.add(lda.getDocId());
            listParams.add(params);
        }
        db.batchUpdate(updateSql, listParams);
        db.close();
    }

    /**
     * @Description： 单个更新一篇文章聚类结果
     * */
    public static void updateResource(ResourceType type, LDAResult ldaResult){
        DBUtil db = new DBUtil();

        String updateSql = "update " + type.name() + " set topicId = ?, topicVoc = ? where " + type.name().toLowerCase() + "_article_id = ?";
        List params = new ArrayList();
        params.add(ldaResult.getTopicId());
        params.add(ldaResult.getTopicVoc());
        params.add(ldaResult.getDocId());
        db.update(updateSql, params);
        db.close();
    }

    public static void main(String[] args) {
        // TODO Auto-generated
    }
}
