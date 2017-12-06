package blackstorelongclass.personal_info_collector.listMonitor;

import android.content.Intent;
import android.util.Log;
import android.util.Pair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Created by abc123one on 2017/10/27.
 */

public class userList {
    Map<String, userTag> contentOfList;
    String titleOfList;
    ArrayList<String> titleOfTag;
    int size;

    /**
     * 使用表单标题创建自定义表单
     * @param title
     * 表单标题
     */
    public userList(String title){
        titleOfList = title;
        titleOfTag = new ArrayList<String>();
        contentOfList = new HashMap<String, userTag>();
        size = 0;
    }

    /**
     * 向自定义表单中添加标签
     * @param title
     * 标签标题 string类型
     * @param tag
     * 自定义标签
     */
    public void addTag(String title, userTag tag){
        contentOfList.put(title, tag);
        titleOfTag.add(size++,title);
    }


    public boolean addTag(String title, userTag tag, int priLevel){
        if(priLevel>=0 && priLevel<= size) {
            contentOfList.put(title, tag);
            titleOfTag.add(priLevel,title);
            size++;
            return true;
        }
        else
            return false;
    }

    /**
     * 通过标签标题获取一个标签
     * @param title
     * 标签标题
     * @return
     * 匹配到的标签,否则为null
     */
    public userTag getTag(String title){
        return contentOfList.get(title);
    }

    /**
     * 通过Tag的index获取一个标签
     * @param index
     * Tag的索引值
     * @return
     * userTag内容,如果索引超出范围,则返回null!!
     */
    public userTag getTag(int index) {
        int size = titleOfTag.size();
        if(index>=size) {
            Log.i("bslc", "bslc_userList_getTag():index out of bound! index=" + index + " bound="+size);
            return null;
        }
        return contentOfList.get(titleOfTag.get(index));
    }

    public ArrayList<String> getTitleList(){
        return titleOfTag;
    }

    public int getListSize(){
        return size;
    }

    public String getListTitle(){
        return titleOfList;
    }

    public long getTime(){
        for(String item:titleOfTag){ //遍历每一个tag寻找时间类型
            userTag tempTag = contentOfList.get(item);
            if(tempTag.isCalendar()) {
                return (long)tempTag.Content;
            }
        }
        return 0;
    }

    public Pair<Double,Double> getPosition(){
        for(String item:titleOfTag){ //遍历每一个tag寻找时间类型
            userTag tempTag = contentOfList.get(item);
            if(tempTag.isPos()) {
                return (Pair<Double,Double>)tempTag.Content;
            }
        }
        return null;
    }
}