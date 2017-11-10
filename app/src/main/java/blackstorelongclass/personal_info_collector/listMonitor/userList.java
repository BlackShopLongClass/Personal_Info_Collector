package blackstorelongclass.personal_info_collector.listMonitor;

import java.util.Collection;
import java.util.Map;

/**
 * Created by abc123one on 2017/10/27.
 */

public class userList {
    Map<String, userTag> contentOfList;
    String titleOfList;
    String[] titleOfTag;
    int size;

    public userList(String title){
        titleOfList = title;
        titleOfTag = new String[]{};
        size = 0;
    }

    public void addTag(String title, userTag tag){
        contentOfList.put(title, tag);
        titleOfTag[size] = title;
    }

    public boolean addTag(String title, userTag tag, int priLevel){
        if(priLevel>=0 && priLevel<= size) {
            contentOfList.put(title, tag);
            titleOfTag[size] = titleOfTag[priLevel];
            titleOfTag[priLevel] = title;
            return true;
        }
        else
            return false;
    }

    public userTag getTag(String title){
        return contentOfList.get(title);
    }

    public String[] getTitleList(){
        return titleOfTag;
    }

    public int getListSize(){
        return size;
    }
}
