package blackstorelongclass.personal_info_collector.listMonitor;

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

    public userList(String title){
        titleOfList = title;
        titleOfTag = new ArrayList<String>();
        contentOfList = new HashMap<String, userTag>();
        size = 0;
    }

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

    public userTag getTag(String title){
        return contentOfList.get(title);
    }

    public ArrayList<String> getTitleList(){
        return titleOfTag;
    }

    public int getListSize(){
        return size;
    }
}
