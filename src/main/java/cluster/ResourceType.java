package cluster;

/**
 * @Author : Lknight
 * @Date : 2017/6/1
 * @Description : TODO
 * @Version : v1.0
 */
public enum  ResourceType {
    Blog, News, Forum;

    public static void main(String... args){
        System.out.print(ResourceType.Blog.name().toLowerCase());
    }
}
