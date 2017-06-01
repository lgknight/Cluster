package cluster;

import ldaLib.Documents;
import ldaLib.LdaGibbsSampling;
import ldaLib.LdaModel;
import model.LDAResult;
import model.dao.LDAResultDAO;
import util.FileUtil;
import util.PropertyHandler;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Lknight on 2017/5/20.
 * 获取聚类数据
 * 如：获取一周内采集的新闻列表进行聚类
 * 聚类类别数
 * 聚类时间
 * 聚类效果指标
 */
public class ClusterResource implements Runnable{
    ResourceType type;
    int spanDay;
    public ClusterResource(ResourceType type, int spanDay){
        this.type = type;
        this.spanDay = spanDay;
    }

    public void run(){
        cluster(type, spanDay);
    }
    public static void test(){
        ResourceType type = ResourceType.Blog;
        cluster(type, 1);
    }

    public static void cluster(ResourceType type, int day){

        //加载lda参数，初始化文档集合
        LdaGibbsSampling.modelparameters ldaparameters = new LdaGibbsSampling.modelparameters();
        LdaGibbsSampling.loadParameters(ldaparameters);
        Documents docSet = new Documents(type, day);
        LdaModel model = new LdaModel(ldaparameters);
        System.out.println("1 Initialize the model ...");
        model.initializeModel(docSet);

        //开始聚类训练
        System.out.println("2 Learning and Saving the model ...");
        try {
            model.inferenceModel();
        } catch (Exception e){
            e.printStackTrace();
        }
        //更新lda聚类结果
        System.out.println("3 Output the final model ...");
        LDAResultDAO.updateResource(ResourceType.Blog, model.doc2topic());
//        for(LDAResult r : model.doc2topic()){
//            System.out.println(r.toString());
//            LDAResultDAO.updateResource(type, r);
//        }
        System.out.println("Done!");
    }
    public static void main(String[] args) {
        // TODO Auto-generated
    }


}

