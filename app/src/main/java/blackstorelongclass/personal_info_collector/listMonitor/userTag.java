package blackstorelongclass.personal_info_collector.listMonitor;

/**
 * Created by Od1gree on 2017/10/27.
 *
 */

public class userTag {
    //String type;
    Object Content;
    String Tag;
    Class<?> C;

    /**
     * 创建标签的函数
     * @param tag
     * 标签名称
     * @param content
     * 标签内容
     */
    public userTag(String tag, Object content){
        this.Tag = tag;
        this.Content = content;
        this.C = content.getClass();
    }

    public userTag(String tag, Class<?> c){
        this.Tag = tag;
        this.C = c;
    }

    public Object getObject(){
        return Content;
    }

    public String getTitle(){
        return Tag;
    }

    public Class<?> getClassType(){
        return C;
    }

    public boolean isDouble(){
        if(C == java.lang.Double.class) return true;
        else
            return false;
    }

    public boolean isGregorianCalendar(){
        if(C == java.util.GregorianCalendar.class) return true;
        else return false;
    }

    public boolean isStr(){
        if(C == java.lang.String.class) return true;
        else return false;
    }
}
