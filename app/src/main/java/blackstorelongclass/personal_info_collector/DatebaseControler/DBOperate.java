package blackstorelongclass.personal_info_collector.DatebaseControler;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Vector;

import android.app.Application;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Pair;

import blackstorelongclass.personal_info_collector.listMonitor.*;

import static android.support.v7.appcompat.R.id.info;

/**
 * Created by ycd on 2017/11/11.
 */

public class DBOperate extends AppCompatActivity {
    private SQLiteDatabase db;
    public DBOperate() {
        File file = new File("/data/data/blackstorelongclass.personal_info_collector/databases/User.db");
        this.db = SQLiteDatabase.openOrCreateDatabase(file,null);
    }
    public void db_close() {
        this.db.close();
    }
    public boolean create_newTable(String SQL_create, String SQL_createConfig) {
        /*DBHelper DBH;
        DBH=new DBHelper(this);*/
        //SQLiteDatabase db=DBH.getWritableDatabase();
        try{
            this.db.execSQL(SQL_createConfig);
            this.db.execSQL(SQL_create);
        }catch(Exception e){
            Log.i("bslc","bslc_DBOperate_create_newTable(): create fail!");
            e.printStackTrace();
        }
        Log.i("bslc","bslc_DBOperate_create_newTable(): create success!");
        return true;
    }
    public boolean delete_Table(String listName) {//删除表单时检查联系表中的项，存在也进行删除
        String SQL_tableDROP="DROP TABLE "+listName+";";
        try{
            this.db.execSQL(SQL_tableDROP);
        }catch(Exception e) {
            Log.i("bslc","bslc_DBOperate_delete_Table(): delete fail!");
            e.printStackTrace();
        }
        String SQL_deleteConfig="DELETE FROM Config WHERE listName='"+listName+"';";
        try{
            this.db.execSQL(SQL_deleteConfig);
        }catch(Exception e) {
            Log.i("bslc","bslc_DBOperate_delete_Table(): delete fail!");
            e.printStackTrace();
        }
        return true;
    }
    public boolean insert_newItem(String SQL_insert) {
        try{
            this.db.execSQL(SQL_insert);
        }catch(Exception e){
            Log.i("bslc","bslc_DBOperate_insert_newItem(): insert fail!");
            e.printStackTrace();
        }
        Log.i("bslc","bslc_DBOperate_insert_newItem(): insert success!");
        return true;
    }
    public boolean delete_item(String SQL_delete) {
        try{
            this.db.execSQL(SQL_delete);
        }catch(Exception e){
            Log.i("bslc","bslc_DBOperate_delete_Item(): delete fail!");
            e.printStackTrace();
        }
        Log.i("bslc","bslc_DBOperate_delete_Item(): delete success!");
        return true;
    }
    public boolean update_item(String SQL_update) {
        try{
            this.db.execSQL(SQL_update);
        }catch(Exception e){
            Log.i("bslc","bslc_DBOperate_update_Item(): update fail!" + e.getMessage());
        }
        Log.i("bslc","bslc_DBOperate_update_Item(): update success!");
        return true;
    }

    public ArrayList<String> get_tableNames() {
        ArrayList<String> tableNames=new ArrayList<String>();
        Cursor cursor;
        String SQL_search="SELECT name FROM sqlite_master WHERE type='table' order by name;";
        cursor=this.db.rawQuery(SQL_search,null);
        int i=0;
        while (cursor.moveToNext()) {
            i++;
            if (i>2) {
                String tableName = cursor.getString(cursor.getColumnIndex("name"));
                if(tableName.equals("sqlite_sequence"))
                    continue;
                if (tableName.equals("android_metadata"))
                    continue;
                tableNames.add(tableName);
            }
        }
        cursor.close();
        Log.i("bslc","bslc_DBOperate_get_tableNames():tableNames="+tableNames);
        return tableNames;
    }
    public ArrayList<String> get_tagNames(String listName) {
        //String SQL_getTagName="pragma table_info("+listName+");";
        String SQL_getTagName="SELECT * FROM "+listName+";";
        String tagTypes=get_tagTypes(listName);
        Cursor cursor;
        cursor=this.db.rawQuery(SQL_getTagName,null);
        ArrayList<String> tagNames=new ArrayList<String>();
        int i=0;
        int count=0; //用于计数经纬度
        cursor.moveToNext();
        for (i=1;i<cursor.getColumnCount();i++) {
            String currentTagName = cursor.getColumnName(i);
            if (tagTypes.charAt(i-1)=='4'&&count==0){
                count++;
                break;
            }
            else if (tagTypes.charAt(i-1)=='4')
            {
                currentTagName=currentTagName.substring(0,currentTagName.length()-1);
            }
            Log.i("bslc","bslc_DBOperate_get_tagNames():currentTagName="+currentTagName);
            tagNames.add(currentTagName);
        }
        cursor.close();
        return tagNames;
    }
    public String get_tagTypes(String listName) {
        String SQL_searchConfig="SELECT * FROM Config WHERE listName='"+listName+"';";
        String tagTypes;
        Cursor cursor;
        cursor=this.db.rawQuery(SQL_searchConfig,null);
        cursor.moveToNext();
        tagTypes=cursor.getString(2);
        cursor.close();
        Log.i("bslc","bslc_DBOperate_get_tagTypes();tagTypes="+tagTypes);
        return tagTypes;
    }
    public ArrayList<userList> queryForString(String item) {
        ArrayList<userList> rt=new ArrayList<userList>();
        ArrayList<String> allListName=get_tableNames();
        ArrayList<String> listsContainStrType=new ArrayList<>();
        ArrayList<ArrayList<String>> tagNamesForStr=new ArrayList<>();
        int i,j;
        //首先找到tag类型中包含String的表单名以及对应tag名
        for (i=0;i<allListName.size();i++)
        {
            String listName_temp=allListName.get(i);
            String tagType_temp=get_tagTypes(listName_temp);
            ArrayList<String> tagNames_temp=get_tagNames(listName_temp);
            ArrayList<String> oneListTagNames_temp=new ArrayList<String>();
            for (j=0;j<tagType_temp.length();j++)
            {
                if (tagType_temp.charAt(j)=='3')
                {
                    oneListTagNames_temp.add(tagNames_temp.get(j));
                }
            }
            listsContainStrType.add(listName_temp);
            tagNamesForStr.add(oneListTagNames_temp);
        }
        //之后开始循环搜索tag下是否有待查项
        for (i=0;i<listsContainStrType.size();i++)
        {
            for (j=0;j<tagNamesForStr.get(i).size();j++)
            {
                String SQL_search="SELECT * FROM "+listsContainStrType.get(i)+" WHERE "+tagNamesForStr.get(i).get(j)+"='"+item+"';";
                String SQL_searchConfig="SELECT * FROM Config WHERE listName='"+listsContainStrType.get(i)+"';";
                String SQL_getTagName="SELECT * FROM "+listsContainStrType.get(i)+";";
                String tagType;
                Cursor cursor;
                cursor=this.db.rawQuery(SQL_searchConfig,null);
                cursor.moveToNext();
                tagType=cursor.getString(2);
                cursor=this.db.rawQuery(SQL_getTagName,null);
                Vector<String> tagName=new Vector<String>();
                int k=0;
                cursor.moveToNext();
                for (k=1;k<cursor.getColumnCount();k++) {
                    String currentTagName = cursor.getColumnName(k);
                    tagName.add(currentTagName);
                }
                int size=tagType.length();
                cursor=this.db.rawQuery(SQL_search,null);
                while (cursor.moveToNext())
                {
                    userList temp1=new userList(listsContainStrType.get(i));
                    double x=0,y=0;
                    for (i=0;i<size;i++) {
                        String tag;
                        Object content;
                        userTag temp2;
                        tag = tagName.elementAt(i);
                        if (tagType.charAt(i) == '1') {
                            content = cursor.getDouble(i + 1);
                        } else if (tagType.charAt(i) == '2') {
                            content = cursor.getLong(i + 1);
                        } else if (tagType.charAt(i) == '3') {
                            content = (String) cursor.getString(i + 1);
                        } else if (tagType.charAt(i) == '4')   //经纬度分别为double
                        {
                            if (tag.charAt(tag.length() - 1) == 'x') {
                                x = cursor.getDouble(i + 1);
                                break;
                            } else {
                                y = cursor.getDouble(i + 1);
                                Pair<Double, Double> position = new Pair<>(x, y);
                                content = position;
                                tag = tag.substring(0, tag.length() - 1);
                                temp2 = new userTag(tag, content);
                                temp1.addTag(tag, temp2);
                                break;
                            }
                        } else {
                            content = null;
                        }
                        temp2 = new userTag(tag, content);
                        temp1.addTag(tag, temp2);
                    }
                    rt.add(temp1);
                }
            }
        }
        return rt;
    }
    public ArrayList<userList> queryForNum(double item) {
        ArrayList<userList> rt=new ArrayList<userList>();
        ArrayList<String> allListName=get_tableNames();
        ArrayList<String> listsContainNumType=new ArrayList<>();
        ArrayList<ArrayList<String>> tagNamesForNum=new ArrayList<>();
        int i,j;
        //首先找到tag类型中包含double的表单名以及对应tag名
        for (i=0;i<allListName.size();i++)
        {
            String listName_temp=allListName.get(i);
            String tagType_temp=get_tagTypes(listName_temp);
            ArrayList<String> tagNames_temp=get_tagNames(listName_temp);
            ArrayList<String> oneListTagNames_temp=new ArrayList<String>();
            for (j=0;j<tagType_temp.length();j++)
            {
                if (tagType_temp.charAt(j)=='1')
                {
                    oneListTagNames_temp.add(tagNames_temp.get(j));
                }
            }
            listsContainNumType.add(listName_temp);
            tagNamesForNum.add(oneListTagNames_temp);
        }
        //之后开始循环搜索tag下是否有待查项
        for (i=0;i<listsContainNumType.size();i++)
        {
            for (j=0;j<tagNamesForNum.get(i).size();j++)
            {
                String SQL_search="SELECT * FROM "+listsContainNumType.get(i)+" WHERE "+tagNamesForNum.get(i).get(j)+"="+item+";";
                String SQL_searchConfig="SELECT * FROM Config WHERE listName='"+listsContainNumType.get(i)+"';";
                String SQL_getTagName="SELECT * FROM "+listsContainNumType.get(i)+";";
                String tagType;
                Cursor cursor;
                cursor=this.db.rawQuery(SQL_searchConfig,null);
                cursor.moveToNext();
                tagType=cursor.getString(2);
                cursor=this.db.rawQuery(SQL_getTagName,null);
                Vector<String> tagName=new Vector<String>();
                int k=0;
                cursor.moveToNext();
                for (k=1;k<cursor.getColumnCount();k++) {
                    String currentTagName = cursor.getColumnName(k);
                    tagName.add(currentTagName);
                }
                int size=tagType.length();
                cursor=this.db.rawQuery(SQL_search,null);
                while (cursor.moveToNext())
                {
                    userList temp1=new userList(listsContainNumType.get(i));
                    double x=0,y=0;
                    for (i=0;i<size;i++) {
                        String tag;
                        Object content;
                        userTag temp2;
                        tag = tagName.elementAt(i);
                        if (tagType.charAt(i) == '1') {
                            content = cursor.getDouble(i + 1);
                        } else if (tagType.charAt(i) == '2') {
                            content = cursor.getLong(i + 1);
                        } else if (tagType.charAt(i) == '3') {
                            content = (String) cursor.getString(i + 1);
                        } else if (tagType.charAt(i) == '4')   //经纬度分别为double
                        {
                            if (tag.charAt(tag.length() - 1) == 'x') {
                                x = cursor.getDouble(i + 1);
                                break;
                            } else {
                                y = cursor.getDouble(i + 1);
                                Pair<Double, Double> position = new Pair<>(x, y);
                                content = position;
                                tag = tag.substring(0, tag.length() - 1);
                                temp2 = new userTag(tag, content);
                                temp1.addTag(tag, temp2);
                                break;
                            }
                        } else {
                            content = null;
                        }
                        temp2 = new userTag(tag, content);
                        temp1.addTag(tag, temp2);
                    }
                    rt.add(temp1);
                }
            }
        }
        return rt;
    }
    public ArrayList<userList> get_allItems(String listName) {
        ArrayList<userList> info=new ArrayList<userList>();
        Log.i("bslc","bslc_DBOperate_get_allItems():listName="+listName);
        String SQL_searchConfig="SELECT * FROM Config WHERE listName='"+listName+"';";
        String SQL_getTagName="SELECT * FROM "+listName+";";
        String tagType;
        Cursor cursor;
        cursor=this.db.rawQuery(SQL_searchConfig,null);
        cursor.moveToNext();
        tagType=cursor.getString(2);
        Log.i("bslc","bslc_DBOperate_get_allItems():tagType="+tagType);
        cursor=this.db.rawQuery(SQL_getTagName,null);
        Vector<String> tagName=new Vector<String>();
        int i=0;
        cursor.moveToNext();
        for (i=1;i<cursor.getColumnCount();i++) {
            String currentTagName = cursor.getColumnName(i);
            Log.i("bslc","bslc_DBOperate_get_allItems():added currentTagName:"+currentTagName);
            tagName.add(currentTagName);
        }
        int timeTag=0;
        String timeName;
        for (i=0;i<tagType.length();i++) {
            if (tagType.charAt(i)=='2') {
                timeTag=i;
                break;
            }
        }
        timeName=tagName.elementAt(timeTag);
        String SQL_getItems="SELECT * FROM "+listName+" ORDER BY "+timeName+" ASC;";
        int size=tagType.length();
        cursor=this.db.rawQuery(SQL_getItems,null);
        while (cursor.moveToNext()) {
            userList temp1=new userList(listName);
            double x=0,y=0;
            for (i=0;i<size;i++) {
                String tag;
                Object content;
                userTag temp2;
                tag=tagName.elementAt(i);
                if (tagType.charAt(i)=='1')
                {
                    content=cursor.getDouble(i+1);
                }
                else if (tagType.charAt(i)=='2')
                {
                    content=cursor.getLong(i+1);
                    Log.i("bslc","bslc_DBOperate_get_allItems(): time="+content);
                }
                else if (tagType.charAt(i)=='3')
                {
                    content=(String)cursor.getString(i+1);
                }
                else if (tagType.charAt(i)=='4')   //经纬度分别为double
                {
                    if (tag.charAt(tag.length()-1)=='x')
                    {
                        x=cursor.getDouble(i+1);
                        break;
                    }
                    else
                    {
                        y=cursor.getDouble(i+1);
                        Pair<Double,Double> position=new Pair<>(x,y);
                        content=position;
                        tag=tag.substring(0,tag.length()-1);
                        temp2=new userTag(tag,content);
                        temp1.addTag(tag,temp2);
                        Log.i("bslc","bslc_DBOperate_get_allItems():tagType=position;content="+content);
                        break;
                    }
                }
                else
                {
                    content=null;
                }
                temp2=new userTag(tag,content);
                temp1.addTag(tag,temp2);
            }
            info.add(temp1);
        }
        cursor.close();
        Log.i("bslc","bslc_DBOperate_get_allItems(): finish!");
        return info;
    }
    public userList get_specificItem(String SQL_searchItem,String listName) {
        String SQL_searchConfig="SELECT * FROM Config WHERE listName='"+listName+"';";
        String SQL_getTagName="SELECT * FROM "+listName+";";
        Log.i("bslc","bslc_DBOperate_get_specificItem():listName:"+listName);
        int i=0,size=0;
        String tagType;
        Vector<String> tagNames=new Vector<String>();
        Cursor cursor;
        cursor=this.db.rawQuery(SQL_searchConfig,null);
        cursor.moveToNext();
        tagType=cursor.getString(2);
        Log.i("bslc","bslc_DBOperate_get_specificItem():tagType="+tagType);
        userList specificItem;
        cursor=this.db.rawQuery(SQL_getTagName,null);
        cursor.moveToNext();
        for (i=1;i<cursor.getColumnCount();i++) {
            tagNames.add(cursor.getColumnName(i));
        }
        size=tagType.length();
        cursor=this.db.rawQuery(SQL_searchItem,null);
        //cursor.moveToNext();
        specificItem=new userList(listName);
        while (cursor.moveToNext()) {
            double x=0,y=0;
            for (i=0;i<size;i++) {
                userTag temp;
                String tag;
                Object content;
                tag=tagNames.elementAt(i);
                if (tagType.charAt(i)=='1')  //数字类型为double
                {
                    content=cursor.getDouble(i+1);
                    Log.i("bslc","bslc_DBOperate_get_specifigItem():tagType=double;content="+content);
                }
                else if (tagType.charAt(i)=='2')   //时间类型为long
                {
                    content=cursor.getLong(i+1);
                    Log.i("bslc","bslc_DBOperate_get_specifigItem():tagType=time;content="+content);
                }
                else if (tagType.charAt(i)=='3')  //文字类型为string
                {
                    content=(String)cursor.getString(i+1);
                    Log.i("bslc","bslc_DBOperate_get_specificItem():tagType=string;content="+content);
                }
                else if (tagType.charAt(i)=='4')   //经纬度分别为double
                {
                    if (tag.charAt(tag.length()-1)=='x')
                    {
                        x=cursor.getDouble(i+1);
                        break;
                    }
                    else
                    {
                        y=cursor.getDouble(i+1);
                        Pair<Double,Double> position=new Pair<>(x,y);
                        content=position;
                        tag=tag.substring(0,tag.length()-1);
                        temp=new userTag(tag,content);
                        specificItem.addTag(tag,temp);
                        Log.i("bslc","bslc_DBOperate_get_specificItem():tagType=position;content="+content);
                        break;
                    }
                }
                else
                {
                    Log.i("bslc","bslc_DBOperate_get_specificItem():can't figure out data type");
                    content=null;
                }
                temp=new userTag(tag,content);
                specificItem.addTag(tag,temp);
            }
        }
        cursor.close();
        Log.i("bslc","bslc_DBOperate_get_specificItem():finish!");
        return specificItem;
    }
    public boolean linkItemWithItemTag(String list1Name,double item1Time,String list2Name,double item2Time,String tagName){
        String SQL_insert="INSERT INTO Link (list1Name,item1Time,list2Name,item2Time,tagName) VALUES ('"+list1Name+"', "+item1Time+", '"+
                list2Name+"', "+item2Time+", '"+tagName+"' );";
        try{
            this.db.execSQL(SQL_insert);
        }catch(Exception e){
            e.printStackTrace();
        }
        return true;
    }
    public Pair<Pair<String,Long>,String> link_rightSearch(String list1Name,long item1Time) {
        String SQL_search="SELECT * FROM Link WHERE list1Name='"+list1Name+"' AND item1Time="+item1Time+";";
        Cursor cursor;
        cursor=this.db.rawQuery(SQL_search,null);
        if (cursor.moveToNext())
        {
            return null;
        }
        else
        {
            String list2Name,tagName;
            long item2Time;
            list2Name=cursor.getString(3);
            item2Time=cursor.getLong(4);
            tagName=cursor.getString(5);
            Pair<String,Long> temp=new Pair<>(list2Name,item2Time);
            Pair<Pair<String,Long>,String> rightItem=new Pair<>(temp,tagName);
            return rightItem;
        }

    }
    public ArrayList<Pair<Pair<String,Long>,String>> link_leftSearch(String list2Name,long item2Time) {
        String SQL_search="SELECT * FROM Link WHERE list2Name='"+list2Name+"' AND item2Time="+item2Time+";";
        Cursor cursor;
        cursor=this.db.rawQuery(SQL_search,null);
        ArrayList<Pair<Pair<String,Long>,String>> leftItems=new ArrayList<>();
        if (cursor.moveToNext())
        {
            do{
                String list1Name;
                long item1Time;
                String tagName;
                list1Name=cursor.getString(1);
                item1Time=cursor.getLong(2);
                tagName=cursor.getString(5);
                Pair<String,Long> temp=new Pair<>(list1Name,item1Time);
                Pair<Pair<String,Long>,String> temp1=new Pair<>(temp,tagName);
                leftItems.add(temp1);
            }while(cursor.moveToNext());
        }
        else
        {
            return null;
        }
        return leftItems;
    }
    public boolean link_delete(String list1Name,long item1Time,String list2Name,long item2Time,String tagName) {
        String SQL_delete="DELETE FROM Link WHERE list1Name ='"+list1Name+"' AND item1Time="+item1Time+" AND list2Name='"+list2Name
                +"' AND item2Time="+item2Time+" AND tagName='"+tagName+"';";
        try{
            this.db.execSQL(SQL_delete);
        }catch(Exception e){
            e.printStackTrace();
        }
        return true;
    }
}
