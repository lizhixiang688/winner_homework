package Search_bar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class RecordsDao {
    RecordsSQLiteOpenHelper recordHelper;
    SQLiteDatabase recordDb;

    public RecordsDao(Context context){
        recordHelper = new RecordsSQLiteOpenHelper(context);
    }


    public void addRecords(String record){
        if (!isHasRecord(record)){
            recordDb = recordHelper.getWritableDatabase();
            ContentValues value = new ContentValues();
            value.put("name",record);
            recordDb.insert("records",null,value);
            recordDb.close();
        }
    }


    public List<String> getRecordsList(){
        List<String> recordsList = new ArrayList<String>();
        recordDb = recordHelper.getReadableDatabase();
        Cursor cursor = recordDb.query("records",null,null,null,null,null,null);
        while (cursor.moveToNext()){
            String record = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            recordsList.add(record);
        }
        recordDb.close();
        cursor.close();
        return recordsList;
    }


    public void clearRecords(){
        String sql = "delete from records";
        recordDb = recordHelper.getWritableDatabase();
        recordDb.execSQL(sql);
        recordDb.close();
    }

        //这里是模糊查询，虽然我好像没有用到 emmm。
    public List<String> querySimilarRecords(String record){
        String sql = "select * from records where name like'%" + record + "%' order by name";
        List<String> similarRecordsList = new ArrayList<String>();
        recordDb = recordHelper.getReadableDatabase();
        Cursor cursor = recordDb.rawQuery(sql,null);
        while (cursor.moveToNext()){
            String myRecord = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            similarRecordsList.add(myRecord);
        }
        recordDb.close();
        cursor.close();
        return similarRecordsList;
    }



    private boolean isHasRecord(String record){
        boolean isHasRecord = false;
        recordDb = recordHelper.getReadableDatabase();
        Cursor cursor = recordDb.query("records",null,null,null,null,null,null);
        while (cursor.moveToNext()){
            if (record.equals(cursor.getString(cursor.getColumnIndexOrThrow("name")))){
                isHasRecord = true;
                break;
            }
        }
        recordDb.close();
        cursor.close();
        return isHasRecord;
    }
}