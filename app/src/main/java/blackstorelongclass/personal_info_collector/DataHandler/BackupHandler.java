package blackstorelongclass.personal_info_collector.DataHandler;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;
import android.util.Pair;
import android.util.Xml;

import blackstorelongclass.personal_info_collector.listMonitor.userList;
import blackstorelongclass.personal_info_collector.listMonitor.userTag;
import jxl.Cell;
import jxl.CellType;
import jxl.DateCell;
import jxl.NumberCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.format.CellFormat;
import jxl.read.biff.BiffException;
import jxl.write.DateTime;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class BackupHandler {
    private final static String TAG = "ExcelUtil";

    public static List<List<Object>> read(String file_name) {
        String extension = file_name.lastIndexOf(".") == -1 ? "" : file_name
                .substring(file_name.lastIndexOf(".") + 1);
        if ("xls".equals(extension)) {// 2003
            Log.d(TAG, "read2003XLS, extension:" + extension);
            return read2003XLS(file_name);
        } else if ("xlsx".equals(extension)) {
            Log.d(TAG, "read2007XLSX, extension:" + extension);
            return read2007XLSX(file_name);
        } else {
            Log.d(TAG, "不支持的文件类型, extension:" + extension);
            return null;
        }
    }

    public static List<List<Object>> read2003XLS(String path) {
        List<List<Object>> dataList = new ArrayList<List<Object>>();
        try {
            Workbook book = Workbook.getWorkbook(new File(path));
            // book.getNumberOfSheets();  //获取sheet页的数目
            // 获得第一个工作表对象
            Sheet sheet = book.getSheet(0);
            String titleOfList = sheet.getName();
            int Rows = sheet.getRows();
            int Cols = sheet.getColumns();
            Log.d(TAG, "当前工作表的名字:" + sheet.getName());
            Log.d(TAG, "总行数:" + Rows + ", 总列数:" + Cols);

            List<Object> objList = new ArrayList<Object>();
            String val = null;
            for (int i = 0; i < Rows; i++) {
                boolean null_row = true;
                for (int j = 0; j < Cols; j++) {
                    // getCell(Col,Row)获得单元格的值，注意getCell格式是先列后行，不是常见的先行后列
                    Log.d(TAG, (sheet.getCell(j, i)).getContents() + "\t");
                    val = (sheet.getCell(j, i)).getContents();
                    if (val == null || val.equals("")) {
                        val = "null";
                    } else {
                        null_row = false;
                    }
                    objList.add(val);
                }
                Log.d(TAG, "\n");
                if (null_row != true) {
                    dataList.add(objList);
                    null_row = true;
                }
                objList = new ArrayList<Object>();
            }
            book.close();
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }

        return dataList;
    }

    public static ArrayList<ArrayList<userList>> readXlsFile(String path){
        ArrayList<ArrayList<userList>> userData = new ArrayList<>();
        try {
            Workbook workbook = Workbook.getWorkbook(new File(path));
            int numOfSheets = workbook.getNumberOfSheets();
            for(int i=0;i<numOfSheets;i++){
                ArrayList<userList> currentList = readXlsPage(path,i);
                userData.add(currentList);
            }
            workbook.close();
        } catch (IOException e) {
            Log.i("bslc","bslc_BackupHandler_readXlsFile(): IOException ERROR: "+e.getMessage());
        } catch (BiffException e) {
            Log.i("bslc","bslc_BackupHandler_readXlsFile(): BiffException ERROR: "+e.getMessage());
        }
        return userData;
    }

    public static ArrayList<userList> readXlsPage(String path, int index){
        Workbook workbook;
        listHandler listhandler = new listHandler("name");
        try{
            workbook = Workbook.getWorkbook(new File(path));
        } catch (Exception e){
            Log.i("bslc","bslc_BackupHandler_readxls():error:"+e.getMessage());
            return null;
        }
            Sheet sheet = workbook.getSheet(index);
            //read title of userList
            String titleOfList = sheet.getName();
            //读取
            int Rows = sheet.getRows(); //行
            int Cols = sheet.getColumns(); //列

            Log.i("bslc", "bslc_BackupHandler_readxls():sheet name=" + sheet.getName());
            Log.i("bslc", "bslc_BackupHandler_readxls():总行数:" + Rows + ", 总列数:" + Cols);

            ArrayList<String> titleList = new ArrayList<>();

            //read titles of userTag
            userList demoList = new userList(titleOfList);
            for(int j = 0; j< Cols; j++) {
                Class<?> cellType = java.lang.String.class;
                String typeFlag = (sheet.getCell(j, 1)).getContents();
                switch (typeFlag){
                    case "STRING": cellType = java.lang.String.class;
                        break;
                    case "NUMBER": cellType = java.lang.Double.class;
                        break;
                    case "TIME": cellType = java.lang.Long.class;
                        break;
                    default:
                        break;
                }
                String tagTitle = (sheet.getCell(j, 0)).getContents();
                userTag currentTag = new userTag(tagTitle,cellType);
                Log.i("bslc","bslc_BackupHandler_readxls(): get Cell name "+ tagTitle + " type " + cellType);
                demoList.addTag(tagTitle,currentTag);
            }
            listhandler.addNewList(demoList);
            //create ArraryList of userList
            ArrayList<userList> userData = new ArrayList<>();
            for (int i = 2; i < Rows; i++) { //行
                Log.i("bslc", "bslc_BackupHandler_readxls():row="+i);
                //创建新list项
                userList currentUserList = new userList(titleOfList);
                boolean null_row = true;
                int j=0;
                for (j = 0; j < Cols; j++) { //列
                    // getCell(Col,Row)获得单元格的值，注意getCell格式是先列后行，不是常见的先行后列
                    userTag demoTag = demoList.getTag(j);
                    Cell currentCell = sheet.getCell(j,i);
                    if(currentCell.getType().equals(CellType.EMPTY)){
                        break;
                    }
                    if(demoTag.isStr()){
                        userTag currentTag;
                        try {
                            currentTag = new userTag(demoTag.getTitle(),currentCell.getContents());
                        } catch (Exception e) {
                            Log.i("bslc", "bslc_BackupHandler_readXlsPage(): demoTag isStr ERROR" + e.getMessage());
                            break;
                        }
                        currentUserList.addTag(demoTag.getTitle(),currentTag);
                    }
                    else if(demoTag.isDouble()){
                        //NumberCell currentCell = (NumberCell) sheet.getCell(j,i);
                        userTag currentTag;
                        try {
                            currentTag = new userTag(demoTag.getTitle(),((NumberCell)currentCell).getValue());
                        } catch (Exception e) {
                            Log.i("bslc", "bslc_BackupHandler_readXlsPage(): demoTag isDouble ERROR" + e.getMessage());
                            break;
                        }

                        currentUserList.addTag(demoTag.getTitle(),currentTag);
                    }
                    else if(demoTag.isCalendar()){
                        //DateCell currentCell = (DateCell) sheet.getCell(j,i);
                        Date date;
                        try{
                            date = ((DateCell)currentCell).getDate();
                        }catch (Exception e){
                            Log.i("bslc", "bslc_BackupHandler_readXlsPage(): demoTag isCalendar ERROR" + e.getMessage());
                            break;
                        }
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(date);
                        userTag currentTag = new userTag(demoTag.getTitle(),calendar);
                        currentUserList.addTag(demoTag.getTitle(),currentTag);
                    }
                    else{
                        //Cell currentCell = sheet.getCell(j,i);
                        double positionX=0,positionY=0;
                        try {
                            String positionStr = currentCell.getContents();
                            String[] positions = positionStr.split(",");
                            positionX = Double.parseDouble(positions[0]);
                            positionY = Double.parseDouble(positions[1]);
                        }catch (Exception e){
                            Log.i("bslc", "bslc_BackupHandler_readXlsPage(): demoTag isPos ERROR" + e.getMessage());
                            break;
                        }
                        Pair<Double,Double> pos = new Pair<>(positionX,positionY);
                        userTag currentTag = new userTag(demoTag.getTitle(),pos);
                        currentUserList.addTag(demoTag.getTitle(),currentTag);
                    }


                    CellType type =  currentCell.getType();
                    Log.i("bslc", "bslc_BackupHandler_readxls():column="+j);
                    Log.i("bslc", "bslc_BackupHandler_readxls():cell content is "+currentCell.getContents() + "\t");
                    Log.i("blsc", "blsc_BackupHandler_readxls():cell type"+type);
                }
                if(j==Cols) {
                    userData.add(currentUserList);
                    listhandler.addNewData(currentUserList);
                }
                else{
                    continue;
                }
                //objList = new ArrayList<Object>();
            }
            workbook.close();
            return userData;
    }

    public static List<List<Object>> read2007XLSX(String path) {
        List<List<Object>> dataList = new ArrayList<List<Object>>();
        String str_c = "";
        String v = null;
        boolean flat = false;
        List<String> ls = new ArrayList<String>();
        try {
            ZipFile xlsxFile = new ZipFile(new File(path));
            ZipEntry sharedStringXML = xlsxFile.getEntry("xl/sharedStrings.xml");
            if (sharedStringXML == null) {
                Log.d(TAG, "空文件:" + path);
                return dataList;
            }
            InputStream inputStream = xlsxFile.getInputStream(sharedStringXML);
            XmlPullParser xmlParser = Xml.newPullParser();
            xmlParser.setInput(inputStream, "utf-8");
            int evtType = xmlParser.getEventType();
            while (evtType != XmlPullParser.END_DOCUMENT) {
                switch (evtType) {
                    case XmlPullParser.START_TAG:
                        String tag = xmlParser.getName();
                        if (tag.equalsIgnoreCase("t")) {
                            ls.add(xmlParser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                    default:
                        break;
                }
                evtType = xmlParser.next();
            }
            ZipEntry sheetXML = xlsxFile.getEntry("xl/worksheets/sheet1.xml");
            InputStream inputStreamsheet = xlsxFile.getInputStream(sheetXML);
            XmlPullParser xmlParsersheet = Xml.newPullParser();
            xmlParsersheet.setInput(inputStreamsheet, "utf-8");
            int evtTypesheet = xmlParsersheet.getEventType();
            List<Object> objList = new ArrayList<Object>();
            String val = null;
            boolean null_row = true;

            while (evtTypesheet != XmlPullParser.END_DOCUMENT) {
                switch (evtTypesheet) {
                    case XmlPullParser.START_TAG:
                        String tag = xmlParsersheet.getName();
                        if (tag.equalsIgnoreCase("row")) {
                        } else if (tag.equalsIgnoreCase("c")) {
                            String t = xmlParsersheet.getAttributeValue(null, "t");
                            if (t == null) { // 非字符串型，可能是整型
                                // Log.d(TAG, flat + "没有");
                                flat = false;
                            } else {
                                flat = true; // 字符串型
                                // Log.d(TAG, flat + "有");
                            }
                        } else if (tag.equalsIgnoreCase("v")) {
                            v = xmlParsersheet.nextText();
                            if (v != null) {
                                if (flat) {
                                    str_c += ls.get(Integer.parseInt(v)) + "  ";
                                    val = ls.get(Integer.parseInt(v));
                                    null_row = false;
                                } else {
                                    str_c += v + "  ";
                                    val = v;
                                }
                                objList.add(val);
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (xmlParsersheet.getName().equalsIgnoreCase("row") && v != null) {
                            str_c += "\n";
                            if (!null_row) {
                                dataList.add(objList);
                                null_row = true;
                            }
                            objList = new ArrayList<Object>();
                        }
                        break;
                }
                evtTypesheet = xmlParsersheet.next();
            }
            Log.d(TAG, str_c);
        } catch (ZipException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        if (str_c == null) {
            str_c = "解析文件出现问题";
            Log.d(TAG, str_c);
        }

        return dataList;
    }

    public static int writeExcel2003(String file_name, List<List<Object>> data_list) {
        try {
            WritableWorkbook book = Workbook.createWorkbook(new File(file_name));
            WritableSheet sheet1 = book.createSheet("sheet1", 0);
            for (int i = 0; i < data_list.size(); i++) {
                List<Object> obj_list = data_list.get(i);
                for (int j = 0; j < obj_list.size(); j++) {
                    Label label = new Label(j, i, obj_list.get(j).toString());
                    sheet1.addCell(label);
                }
            }
            book.write();
            book.close();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        return 0;
    }

    public static boolean writeXlsFile(String Path){
        listHandler listhandler = new listHandler("name");
        ArrayList<String> tableList = listhandler.getTableList();
        WritableWorkbook book;
        try {
            book = Workbook.createWorkbook(new File(Path));
        } catch (IOException e) {
            Log.i("bslc","bslc_BackupHandler_writeXlsFile():create book ERROR! "+ e.getMessage());
            return false;
        }
        int indexOfSheet = 0;
        for(String title:tableList){
            ArrayList<userList> currentList = listhandler.getTableAllData(title);
            userList demoList = listhandler.getDataType(title);
            WritableSheet currentSheet = book.createSheet(demoList.getListTitle(),indexOfSheet);
            ArrayList<String> tagList = demoList.getTitleList();
            Log.i("bslc","bslc_BackupHandler_writeXlsFile(): start to write table name="
                    + title + " indexOfColumn=0");
            //write titles with types for a TABLE
            int indexOfColumn = 0;
            for(String tagName:tagList){
                Label tagNameLabel = new Label(indexOfColumn,0,tagName);
                userTag currentTag = demoList.getTag(tagName);
                try {
                    currentSheet.addCell(tagNameLabel);
                } catch (WriteException e) {
                    Log.i("bslc","bslc_BackupHandler_writeXlsFile():write cell ERROR! "+ e.getMessage());
                }

                String typeString;
                if(currentTag.isCalendar())     typeString = "TIME";
                else if(currentTag.isDouble())  typeString = "NUMBER";
                else if(currentTag.isPos())     typeString = "POSITION";
                else                            typeString = "STRING";
                Label tagTypeLabel = new Label(indexOfColumn,1,typeString);
                Log.i("bslc","bslc_BackupHandler_writeXlsFile(): current tag name ="
                        + tagName +"tag type =" + typeString);

                try {
                    currentSheet.addCell(tagTypeLabel);
                }catch (Exception e){
                    Log.i("bslc","bslc_BackupHandler_writeXlsFile():write cell ERROR! "+ e.getMessage());
                }

                indexOfColumn++;
            }

            //write Data for TABLE
            int indexOfRow = 2;
            for(userList currentUL:currentList){
                indexOfColumn = 0;
                for(String tagName:tagList){
                    userTag currentTag = currentUL.getTag(tagName);

                    //judge cell type
                    if(currentTag.isCalendar()){
                        Date date = new Date((Long) currentTag.getObject());
                        DateTime datetimeCell = new DateTime(indexOfColumn,indexOfRow,date);
                        try {
                            currentSheet.addCell(datetimeCell);
                        }catch (Exception e){
                            Log.i("bslc","bslc_BackupHandler_writeXlsFile():write cell ERROR! "+
                                    "judged to calendar type: "+ e.getMessage());
                        }
                    }
                    else if(currentTag.isDouble()){
                        Number numberCell = new Number(indexOfColumn,indexOfRow
                                                        ,(Double)currentTag.getObject()
                        );
                        try {
                            currentSheet.addCell(numberCell);
                        }catch (Exception e){
                            Log.i("bslc","bslc_BackupHandler_writeXlsFile():write cell ERROR! "+
                                    "judged to double type"+ e.getMessage());
                        }
                    }
                    else if(currentTag.isPos()){
                        Pair<Double,Double> position = (Pair<Double,Double>)currentTag.getObject();
                        Label label = new Label(indexOfColumn,indexOfRow
                                                ,position.first+","+position.second
                        );
                        try {
                            currentSheet.addCell(label);
                        }catch (Exception e){
                            Log.i("bslc","bslc_BackupHandler_writeXlsFile():write cell ERROR! "+
                                    "judged to position type"+ e.getMessage());
                        }
                    }
                    else{
                        Label label = new Label(indexOfColumn
                                                ,indexOfRow
                                                ,(String)currentTag.getObject()
                        );
                        try {
                            currentSheet.addCell(label);
                        }catch (Exception e){
                            Log.i("bslc","bslc_BackupHandler_writeXlsFile():write cell ERROR! "+
                                    "judged to String type"+ e.getMessage());
                        }
                    }
                    indexOfColumn++;
                }
                indexOfRow++;
            }
            //end of writing a sheet
            try {
                book.write();
            } catch (IOException e) {
                Log.i("bslc","bslc_BackupHandler_writeXlsFile():write sheet ERROR! "+e.getMessage());
            }
            indexOfSheet++;
        }
        try {
            book.close();
        } catch (Exception e) {
            Log.i("bslc","bslc_BackupHandler_writeXlsFile():close book ERROR! "+e.getMessage());
            return false;
        }
        return true;
    }

}
