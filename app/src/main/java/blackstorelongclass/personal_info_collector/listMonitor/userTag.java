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
     * 用内容创建标签
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

    /**
     * 用"类"创建标签
     * @param tag
     * 标签名称
     * @param c
     * 类名,比如 java.Lang.Double.class
     */
    public userTag(String tag, Class<?> c){
        this.Tag = tag;
        this.C = c;
    }

    /**
     * 获取标签内容
     * @return
     * 返回Object型的内容,获取之后需要强制类型转换
     */
    public Object getObject(){
        return Content;
    }

    /**
     * 获取标签标题
     * @return
     * 返回String型标题
     */
    public String getTitle(){
        return Tag;
    }

    /**
     * 获取标签类型
     * @return
     * 返回"类"类型
     */
    public Class<?> getClassType(){
        return C;
    }

    /**
     * 是否为数字类型
     * @return bool
     */
    public boolean isDouble(){
        if(C == java.lang.Double.class) return true;
        else
            return false;
    }

    /**
     * 是否为日期类型
     * @return bool
     */
    public boolean isCalendar(){
        if(C == java.util.GregorianCalendar.class) return true;
        else if(C == java.lang.Long.class) return true;
        else return false;
    }

    /**
     * 是否为字符串类型
     * @return bool
     */
    public boolean isStr(){
        if(C == java.lang.String.class) return true;
        else return false;
    }
}