package blackstorelongclass.personal_info_collector.DataHandler;
import blackstorelongclass.personal_info_collector.DatebaseControler.DBHelper;
import blackstorelongclass.personal_info_collector.DatebaseControler.DBOperate;
import blackstorelongclass.personal_info_collector.listMonitor.*;

import android.content.SharedPreferences;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;

import java.security.Timestamp;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by abc123one on 2017/11/11.
 */

public class listHandler extends AppCompatActivity{
    String name;
    ArrayList<String> tableList;

    public listHandler(String Name){
        name = Name;
        DBOperate DBO = new DBOperate();
        tableList = DBO.get_tableNames();
        Log.i("bslc","bslc_listHandler_listHandler():name="+name);
        //BackupHandler.readXlsFile("/data/data/blackstorelongclass.personal_info_collector/lists.xls");
        BackupHandler.writeXlsFile("/data/data/backstorelongclass.personal_info_collector/export.xls");
    }

    /**
     * 向所有表单名称列表中添加表单名称
     * @param table
     * 表单名称
     * @return
     * 成功与否
     */
    public boolean addTable(String table){
        return tableList.add(table);
    }

    /**
     * 获取所有表单名称
     * @return
     * ArrayList类型的数组
     */
    public ArrayList<String> getTableList(){
        Calendar time = Calendar.getInstance();
        Class<?> c = time.getClass();
        return tableList;
    }

    public String getUserName(){
        return name;
    }

    /**
     * 向数据库中添加新表单
     * @param List
     * 用户输入的数据类型
     * @return
     * 添加的成功与否
     */
    public boolean addNewList(userList List){
        addTable(List.getListTitle());
        int number = List.getListSize();
        String sentence = "CREATE TABLE "+ List.getListTitle() + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,";
        Log.i("bslc","bslc_listHandler_addNewList():sentence_before="+sentence);
        ArrayList<String> titleSet = List.getTitleList();
        String config = "INSERT INTO Config (listName,tagType) VALUES ('" +List.getListTitle() + "','";
        Log.i("bslc","bslc_listHandler_addNewList():config_before="+config);
        for(int i=0 ; i<number; i++){
            userTag t = List.getTag(titleSet.get(i));
            if(t.isDouble()) {
                sentence = sentence + t.getTitle() + " REAL";
                config += "1";
            }
            else if(t.isCalendar()) {
                sentence = sentence + t.getTitle() + " INTEGER";
                config += "2";
            }
            else if(t.isStr()) {
                sentence = sentence + t.getTitle() + " TEXT";
                config += "3";
            }
            if(i+1<number) sentence += ",";
        }
        sentence += ");";
        config += "');";
        DBOperate DBO=new DBOperate();
        Log.i("bslc","bslc_listHandler_addNewList():config_after="+config);
        Log.i("bslc","bslc_listHandler_addNewList():sentence_after="+sentence);
        return DBO.create_newTable(sentence,config);
    }

    /**
     * 向数据库中的某一个表单添加一项
     * @param List
     * 包含数据的内容的userList
     * @return
     * 添加的成功与否
     */
    public boolean addNewData(userList List){
        int number = List.getListSize();
        ArrayList<String> titleSet = List.getTitleList();
        String sentence = "INSERT INTO "+ List.getListTitle() +" (";
        Log.i("bslc","bslc_listHandler_addNewData():sentence_before="+sentence);
        for(int i=0; i<List.getListSize();i++){
            userTag t = List.getTag(titleSet.get(i));
            if(i>0) sentence += ",";
            if(t.isPos()){
                sentence = sentence + t.getTitle()+"x," + t.getTitle()+"y";
            }
            sentence += t.getTitle();
        }
        sentence += ") VALUES (";
        for (int i=0; i<List.getListSize();i++){
            if(i>0) sentence += " , ";
            userTag t = List.getTag(titleSet.get(i));
            //if(t.isStr()) sentence = sentence + "'" + (String)t.getObject() + "'";
            if(t.isCalendar())
                sentence = sentence + ((Calendar)t.getObject()).getTimeInMillis();
            else if(t.isDouble())
                sentence = sentence + (double)t.getObject();
            else if(t.isPos()) {
                Pair<Double,Double> position = (Pair<Double,Double>) t.getObject();
                sentence = sentence + position.first + "," + position.second;
            }
            else
                sentence = sentence + "'" + (String)t.getObject() + "'";
        }
        sentence += ");";
        Log.i("bslc","bslc_listHandler_addNewData():sentence_after="+sentence);
        DBOperate DBO=new DBOperate();
        return DBO.insert_newItem(sentence);
    }

    /**
     * 获取某一个表单的数据类型
     * @param tableName
     * 表单名称
     * @return
     * 包含数据类型的userList
     */
    public userList getDataType(String tableName){
        Log.i("bslc_listHandler","getDataType():Start get table name="+tableName);
        DBOperate DBO = new DBOperate();
        ArrayList<String> tableStr= DBO.get_tagNames(tableName);
        String tagType = DBO.get_tagTypes(tableName);
        Log.i("bslc","bslc_listHandler_getDataType():types of each tag="+tagType+"(1 for num;2 for date; 3 for word.)");
        userList u = new userList(tableName);
        for(int i=0;i<tableStr.size();i++) {
            Class<?> type = java.lang.String.class;
            switch (tagType.charAt(i)){
                case '1': type = java.lang.Double.class;
                    break;
                case '2': type = java.util.Calendar.class;
                    break;
                case '3': type = java.lang.String.class;
                    break;
                case '4': type = android.util.Pair.class;
                    break;
            }
            userTag tag = new userTag(tableStr.get(i),type);
            u.addTag(tableStr.get(i),tag);
        }
        return u;
    }

    /**
     * 获取一个表单的所有数据
     * @param table
     * 表单名称
     * @return
     * userList的ArraryList
     * 注意,返回的时间为Long类型,单位为毫秒,转换为calendar时使用setTimeInMills()方法.
     */
    public ArrayList<userList> getTableAllData(String table){
        DBOperate DBO= new DBOperate();
        return DBO.get_allItems(table);
    }

    /**
     * 查询一个表的某一项
     * @param table
     * 表名称
     * @param time
     * 字符串类型表示的时间,形如:"yyyy-MM-dd HH:mm:ss"
     * @return
     * 所得到的表单
     */
    public userList getATableData(String table,String time) throws ParseException {
        DBOperate DBO = new DBOperate();
        ArrayList<String> titles = DBO.get_tagNames(table);
        String tagTypes = DBO.get_tagTypes(table);
        Log.i("bslc","bslc_listHandler_getATableData():tagType="+tagTypes+"(1 for num;2 for date; 3 for word; 4 for position)");
        String resultString = "";
        for(int i=0;i<titles.size();i++){
            if(tagTypes.charAt(i) == '2') {
                resultString = titles.get(i);
                break;
            }
        }
        String sentence = "SELECT * FROM " + table + " WHERE " + resultString + "=" + timeStr2Long(time) + ";";
        Log.i("bslc","bslc_listHandler_getATableData():sentence="+sentence);
        return DBO.get_specificItem(sentence,table);
    }

    /**
     * 查询一个表单的一个时间点是否可用
     * @param table
     * 表单名称
     * @param time
     * 字符串类型的时间,形如:"yyyy-MM-dd HH:mm:ss"
     * @return
     * true:该时间可以使用
     * false:该时间不能使用
     * @throws ParseException
     */
    public boolean checkTimeAvaliable(String table, String time)throws ParseException{
        userList userlist = getATableData(table,time);
        ArrayList<String> arr = userlist.getTitleList();
        return arr.size()==0 ? true : false;
    }

    /**
     * 将字符串类型的时间转换为Unix时间戳
     * @param timeStr
     * @return
     * Long型时间戳
     * @throws ParseException
     */
    public long timeStr2Long(String timeStr) throws ParseException {
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date= sdf.parse(timeStr);
        return date.getTime();
    }

    /**
     * 查询所有表单的时间-标题键值对,用于展示时间轴时使用
     * @return
     * ArraryList<Pair>.其中Pair<Long,String>
     */
    public ArrayList<Pair> getTimeWithTitle(){
        DBOperate DBO = new DBOperate();
        ArrayList<ArrayList<Pair<Long,String>>> allTableArrary = new ArrayList<>();
        for(String title:tableList){
            Log.i("bslc","bslc_listHandler_getTimeWithTitle():fetch list from DB names +" + title);
            ArrayList<userList> userlist = DBO.get_allItems(title);
            ArrayList<Pair<Long,String>> currentTable = new ArrayList<>();
            for(userList u:userlist){
                Pair<Long,String> p = new Pair<>(u.getTime(),u.getListTitle());
                currentTable.add(p);
            }
            if(currentTable.isEmpty())
                continue;
            allTableArrary.add(currentTable);
        }
        ArrayList<Pair> resultList = new ArrayList<>();
        Log.i("bslc","bslc_listHandler_getTimeWithTitle():fetch finish, start to sort");
        while (true){
            int numOfTable = allTableArrary.size();
            int flag = 0;
            for(int i=1;i<numOfTable;i++){
                if(allTableArrary.get(i).get(0).first<allTableArrary.get(flag).get(0).first)
                    flag = i;
            }
            resultList.add(allTableArrary.get(flag).get(0));
            Log.i("bslc","bslc_listHandler_getTimeWithTitle():time="+allTableArrary.get(flag).get(0).first);
            allTableArrary.get(flag).remove(0);
            if(allTableArrary.get(flag).isEmpty()){
                allTableArrary.remove(flag);
                Log.i("bslc","bslc_listHandler_getTimeWithTitle():1 stack empty,"+(numOfTable-1)+" remains");
                if(allTableArrary.isEmpty())
                    break;
            }
        }
        return resultList;
    }

    /**
     * 获得用户所有表单所有项的<标题,地理位置>
     * @return
     * ArrayList<Pair<String,Pair<Double,Double>>>
     * 列表<<标题,<坐标x,坐标y>>
     */
    public ArrayList<Pair<String,Pair<Double,Double>>> getPositionWithTitle(){
        DBOperate DBO = new DBOperate();

        ArrayList<Pair<String,Pair<Double,Double>>> resultList = new ArrayList<>();
        for(String title:tableList){
            Log.i("bslc","bslc_listHandler_getTimeWithTitle():fetch list from DB names +" + title);
            ArrayList<userList> currentList = DBO.get_allItems(title);
            for(userList u:currentList){
                Pair<String,Pair<Double,Double>> dataSet = new Pair<>(title,u.getPosition());
                resultList.add(dataSet);
            }
        }
        return resultList;
    }

    /**
     * 编辑一个表单
     * @param List
     * 被编辑完成的表单所生成的userList
     * @param calendar
     * 编辑表单之前此表单的时间值,为Calendar
     * @return
     */
    public boolean editData(userList List, Calendar calendar){
        int number = List.getListSize();
        ArrayList<String> titleSet = List.getTitleList();
        String sentence = "UPDATE SET "+ List.getListTitle();
        Log.i("bslc","bslc_listHandler_editData():sentence_before="+sentence);
        String timeTagName="TIME";
        for(int i=0; i<List.getListSize();i++){
            userTag t = List.getTag(titleSet.get(i));
            if(i>0) sentence += ",";

            if(t.isPos()){
                Pair<Double,Double> position = (Pair<Double,Double>)t.getObject();
                sentence = sentence + t.getTitle()+"x=" + position.first +","
                        + t.getTitle()+"y" + position.second;
            }
            else {
                sentence += t.getTitle() + "=";

                //if(t.isStr()) sentence = sentence + "'" + (String)t.getObject() + "'";
                if (t.isCalendar()) {
                    sentence = sentence + ((Calendar) t.getObject()).getTimeInMillis();
                    timeTagName = t.getTitle();
                }
                else if (t.isDouble())
                    sentence = sentence + (double) t.getObject();

                else
                    sentence = sentence + "'" + (String) t.getObject() + "'";
            }
        }

        sentence += "WHERE" + timeTagName +"=" + calendar.getTimeInMillis();
        Log.i("bslc","bslc_listHandler_editData():sentence_after="+sentence);
        DBOperate DBO=new DBOperate();
        return DBO.update_item(sentence);
    }

    /**
     * 删除表单中的一项内容
     * @param listName
     * 表单名称
     * @param calendar
     * 被删除的项的时间
     * @return
     */
    public boolean deleteData(String listName, Calendar calendar){
        long time = calendar.getTimeInMillis();
        userList demoList = getDataType(listName);
        userTag timeTag = demoList.getTimeTag();
        String sql = "DELETE FROM " + listName + " WHERE " + timeTag.getTitle() + "=" + time;
        deleteBridgeNode(listName,calendar.getTimeInMillis());
        DBOperate DBO = new DBOperate();
        return DBO.delete_item(sql);
    }

    /**
     * 获得一个数据的所有关联项
     * @param userlist
     * 一个表单内的单独数据
     * @return
     * 返回<<表单名称,时间>,tag名称>
     *     其中表单名称为被连接的数据的表单名称,时间为被连接的表单的一个数据项的时间,tag名称为被连接的tag名称.
     */
    public Pair<Pair<String,Long>,String> getBridge(String title, Long time){
        DBOperate DBO = new DBOperate();
        return DBO.link_rightSearch(title,time);
    }

    /**
     * 获得一个数据所有被关联的项
     * @param userlist 一个表单内的单独数据
     * @return
     * 返回<表单名称,时间>
     *     其中表单名称为主动连接的数据表单名称,时间为主动连接的数据项的时间
     */
    public ArrayList<Pair<Pair<String,Long>,String>> getBeBridged(String title, Long time){
        DBOperate DBO = new DBOperate();
        return DBO.link_leftSearch(title,time);
    }

    /**
     * 将一个数据项与另一个数据项中的一个Tag进行关联
     * @param list1
     * 主动连接的表单名称
     * @param time1
     * 主动连接的数据项的时间,作为唯一标记
     * @param list2
     * 被连接的表单名称
     * @param time2
     * 被连接的数据项的时间,作为唯一标记
     * @param tagName
     * 被连接的数据项的Tag名称
     * @return 返回添加成功与否.
     */
    public boolean addBridge(String list1, long time1, String list2, long time2, String tagName){
        DBOperate DBO = new DBOperate();
        return DBO.linkItemWithItemTag(list1,time1,list2,time2,tagName);
    }

    public boolean deleteBridge(String list1, long time1, String list2, long time2, String tagName){
        DBOperate DBO = new DBOperate();
        return DBO.link_delete(list1,time1,list2,time2,tagName);
    }

    public void deleteBridgeNode(String title, Long time){
        Pair<Pair<String,Long>,String> outBridge = getBridge(title,time);
        if(outBridge != null)
            deleteBridge(title,time,outBridge.first.first,outBridge.first.second,outBridge.second);
        ArrayList<Pair<Pair<String,Long>,String>> inBridgeList = getBeBridged(title,time);
        for(Pair<Pair<String,Long>,String> inBridge:inBridgeList){
            deleteBridge(inBridge.first.first,inBridge.first.second,title,time,inBridge.second);
        }
    }

    public boolean deleteList(String title){
        DBOperate DBO = new DBOperate();
        return DBO.delete_Table(title);
    }
}
