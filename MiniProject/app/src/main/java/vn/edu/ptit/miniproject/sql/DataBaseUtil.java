package vn.edu.ptit.miniproject.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

import vn.edu.ptit.miniproject.model.ItemSaved;

/**
 * Created by QuangPC on 7/24/2017.
 */

public class DataBaseUtil {
    public static final String PATH = Environment.getDataDirectory().getPath()
            + "/data/vn.edu.ptit.miniproject/databases/";
    public static final String DB_NAME = "miniproject.sqlite";
    public static final String TABLE_NAME = "itemSave";
    public static final String TITLE = "title";
    public static final String ID= "id";
    public static final String DESC = "desc";
    public static final String PUB_DATE = "pudDate";
    public static final String IMAGE = "image";
    public static final String PATH_FILE = "path";
    public static final String FAVORITE = "favorite";

    private Context context;
    private SQLiteDatabase database;

    public DataBaseUtil(Context context) {
        this.context = context;
        copyFileToDevice();
    }

    private void copyFileToDevice() {
        File file = new File(PATH+DB_NAME);
        if(!file.exists()){
            File parent = file.getParentFile();
            parent.mkdirs();
            try {
                InputStream inputStream = context.getAssets().open(DB_NAME);
                FileOutputStream outputStream = new FileOutputStream(file);
                byte[] b = new byte[1024];
                int count = inputStream.read(b);
                while (count != -1){
                    outputStream.write(b,0,count);
                    count = inputStream.read(b);
                }
                inputStream.close();
                outputStream.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    private void openDatabase(){
        database = context.openOrCreateDatabase(DB_NAME,Context.MODE_PRIVATE,null);
        Log.e("OPEND","ok");
    }

    private void closeDatabase(){
        database.close();
    }

   public ArrayList<ItemSaved> getItems(){
       ArrayList<ItemSaved> arr = new ArrayList<>();
       openDatabase();
       Cursor cursor = database.query(TABLE_NAME, null, null,null,null,null,null);
       cursor.moveToFirst();
       while (!cursor.isAfterLast()){
           int id = cursor.getInt(cursor.getColumnIndex(ID));
           String title = cursor.getString(cursor.getColumnIndex(TITLE));
           String desc = cursor.getString(cursor.getColumnIndex(DESC));
           String puD = cursor.getString(cursor.getColumnIndex(PUB_DATE));
           String image = cursor.getString(cursor.getColumnIndex(IMAGE));
           String path = cursor.getString(cursor.getColumnIndex(PATH_FILE));
           int favorite = cursor.getInt(cursor.getColumnIndex(FAVORITE));
           ItemSaved item = new ItemSaved(id,title,desc,puD,image,path,favorite);
           arr.add(item);
           cursor.moveToNext();
       }
       closeDatabase();
       return arr;
   }
   public ArrayList<ItemSaved> getLikedItems(){
       ArrayList<ItemSaved> arrF = new ArrayList<>();
       openDatabase();
       Cursor cursor = database.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE "+FAVORITE+" = '1'",null);
       cursor.moveToFirst();
       while (!cursor.isAfterLast()){
           int id = cursor.getInt(cursor.getColumnIndex(ID));
           String title = cursor.getString(cursor.getColumnIndex(TITLE));
           String desc = cursor.getString(cursor.getColumnIndex(DESC));
           String puD = cursor.getString(cursor.getColumnIndex(PUB_DATE));
           String image = cursor.getString(cursor.getColumnIndex(IMAGE));
           String path = cursor.getString(cursor.getColumnIndex(PATH_FILE));
           int favorite = cursor.getInt(cursor.getColumnIndex(FAVORITE));
           ItemSaved item = new ItemSaved(id,title,desc,puD,image,path,favorite);
           arrF.add(item);
           cursor.moveToNext();
       }
       closeDatabase();
       return arrF;
   }

    public long insert(ItemSaved itemSaved){
        ContentValues contentValues = new ContentValues();
        contentValues.put(TITLE,itemSaved.getTitle());
        contentValues.put(DESC,itemSaved.getDesc());
        contentValues.put(PUB_DATE,itemSaved.getPubDate());
        contentValues.put(IMAGE,itemSaved.getImage());
        contentValues.put(PATH_FILE,itemSaved.getPathFile());
        openDatabase();
        long id = database.insert(TABLE_NAME,null,contentValues);
        closeDatabase();
        return id;
    }
    public int delete(int id){
        openDatabase();
        String where = ID + " = ?";
        String[] whereAgrs = {id + ""};
        int rows = database.delete(TABLE_NAME,where,whereAgrs);
        closeDatabase();
        return rows;
    }
    public int itemIsLike(ItemSaved itemSaved){
        ContentValues values = new ContentValues();
        values.put(FAVORITE,1);
        openDatabase();
        String where = ID+" = ?";
        String[] whereAgrs = {itemSaved.getId()+""};
        int rows = database.update(TABLE_NAME,values,where,whereAgrs);
        closeDatabase();
        return rows;
    }
    public int itemIsDisLike(ItemSaved itemSaved){
        ContentValues values = new ContentValues();
        values.put(FAVORITE,0);
        openDatabase();
        String where = ID+" = ?";
        String[] whereAgrs = {itemSaved.getId()+""};
        int rows = database.update(TABLE_NAME,values,where,whereAgrs);
        closeDatabase();
        return rows;
    }
}
