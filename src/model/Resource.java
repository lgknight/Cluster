package model;

/**
 * Created by Lknight on 2017/5/20.
 * 类别：新闻-"n", 博客-"b", 论坛-"f"
 * 新闻：id,content
 * 博客：id,content
 * 论坛：id,content
 */
public class Resource {
    private String type;
    private String id;
    private String content;

    public Resource(String type, String id, String content){
        this.type = type;
        this.id = id;
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
