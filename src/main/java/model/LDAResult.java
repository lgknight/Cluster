package model;

/**
 * @Author : Lknight
 * @Date : 2017/6/1
 * @Description : TODO
 * @Version : v1.0
 */
public class LDAResult {

    String docId;
    int topicId;
    String topicVoc;//话题代表词汇

    public LDAResult(String docId, int topicId, String topicVoc){
        this.docId = docId;
        this.topicId = topicId;
        this.topicVoc = topicVoc;
    }
    public String toString(){
        return "[LDAResult]: docId = " + docId + ", topidId = " + topicId + ", topicVoc = " + topicVoc;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public int getTopicId() {
        return topicId;
    }

    public void setTopicId(int topicId) {
        this.topicId = topicId;
    }

    public String getTopicVoc() {
        return topicVoc;
    }

    public void setTopicVoc(String topicVoc) {
        this.topicVoc = topicVoc;
    }

    public static void main(String[] args) {
        // TODO Auto-generated
    }
}
