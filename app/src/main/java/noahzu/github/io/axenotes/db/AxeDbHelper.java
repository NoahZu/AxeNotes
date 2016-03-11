package noahzu.github.io.axenotes.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import noahzu.github.io.axenotes.entity.AxeMedia;
import noahzu.github.io.axenotes.entity.AxeNote;
import noahzu.github.io.axenotes.entity.AxePicture;

/**
 * Created by Administrator on 2016/3/7.
 */
public class AxeDbHelper extends SQLiteOpenHelper{
    private static AxeDbHelper axeDbHelper;
    private static final String DB_NAME = "axe_note.db";

    private static final String CREATE_NOTE_TABLE_SQL = "create table axe_table (" +
            "_id integer primary key autoincrement ," +
            "only_id long not null,"+
            "title varchar(128)," +
            "content text," +
            "date varchar(128)," +
            "location varchar(512)" +
            ");";

    private static final String CREATE_PICTURE_TABLE_SQL = "create table axe_picture(" +
            "only_id long not null," +
            "path varchar(128));";
    //type 1:video 2:voice
    private static final String CREATE_MEDIA_TABLE_SQL = "create table axe_media (" +
            "only_id long not null," +
            "type integer not null,"+
            "title varchar(128)," +
            "path varchar(128));";

    public static AxeDbHelper getInstance(Context context){
        if(axeDbHelper == null){
            synchronized (AxeDbHelper.class){
                if (axeDbHelper == null){
                    axeDbHelper = new AxeDbHelper(context,DB_NAME,null,1);
                }
            }
        }
        return axeDbHelper;
    }

    /**
     * 添加axeNote
     * @param axeNote
     */
    public void addAxeNote(AxeNote axeNote){
        String sql = "insert into axe_table values(null,"+axeNote.onlyId+",'"+axeNote.title+"','"+axeNote.content+"','"+axeNote.date+"','"+axeNote.location+"');";
        getWritableDatabase().execSQL(sql);
        //插入图片
        String pictureSql;
        for(int i = 0;i < axeNote.pictures.size();i++){
            pictureSql = "insert into axe_picture values("+axeNote.onlyId+",'"+axeNote.pictures.get(i).path+"');";
            getWritableDatabase().execSQL(pictureSql);
        }
        String mediaSql;
        for(int j = 0;j<axeNote.medias.size();j++){
            mediaSql = "insert into axe_media values("+axeNote.onlyId+","+axeNote.medias.get(j).type+",'"+axeNote.medias.get(j).title+"','"+axeNote.medias.get(j).path+"');";
            getWritableDatabase().execSQL(mediaSql);
        }
    }

    /**
     * 查询所有note
     * @return
     */
    public List<AxeNote> getAllAxeNote(){
        String sql = "select * from axe_table;";
        Cursor cursor = getWritableDatabase().rawQuery(sql, new String[]{});
        List<AxeNote> axeNotes = new ArrayList<>();
        while (cursor.moveToNext()){
            AxeNote axeNote = new AxeNote(
                    cursor.getLong(cursor.getColumnIndex("only_id")),
                    cursor.getString(cursor.getColumnIndex("title")),
                    cursor.getString(cursor.getColumnIndex("content")),
                    cursor.getString(cursor.getColumnIndex("date")),
                    cursor.getString(cursor.getColumnIndex("location"))
            );
            String pictureSql = "select * from axe_picture where only_id = "+axeNote.onlyId+";";
            Cursor picCursor = getWritableDatabase().rawQuery(pictureSql, new String[]{});
            while(picCursor.moveToNext()){
                axeNote.pictures.add(new AxePicture(cursor.getString(1)));
            }
            String mediaSql = "select * from axe_media where only_id = "+axeNote.onlyId+";";
            Cursor mediaCursor = getWritableDatabase().rawQuery(mediaSql,new String[]{});
            while (mediaCursor.moveToNext()){
                axeNote.medias.add(new AxeMedia(mediaCursor.getInt(1),mediaCursor.getString(2),mediaCursor.getString(3)));
            }
            axeNotes.add(axeNote);
        }
        return axeNotes;
    }

    /**
     * 删除某一条固定的笔记，onlyid可以唯一识别笔记，他是根据笔记创建时的毫秒时间来生成的一个id
     * @param deletedAxeNote
     * @return
     */
    public boolean deleteAxeNote(AxeNote deletedAxeNote){
            String delTableSql = "delete from axe_table where only_id = "+deletedAxeNote.onlyId+";";
            String delPicSql = "delete from axe_picture where only_id = "+deletedAxeNote.onlyId+";";
            String delMediaSql = "delete from axe_media where only_id = "+deletedAxeNote.onlyId+";";
            getWritableDatabase().execSQL(delTableSql);
            getWritableDatabase().execSQL(delPicSql);
            getWritableDatabase().execSQL(delMediaSql);
            return true;
    }

    /**
     * 批量删除笔记
     * @param axeNotes
     * @return 删除笔记的数目
     */
    public int deleteAxeNotes(List<AxeNote> axeNotes)
    {
        int count = 0;
        for (AxeNote axeNote : axeNotes){
            if (deleteAxeNote(axeNote)){
                count++;
            }
        }
        return count;
    }

    /**
     * 更新
     * @param axeNote
     */
    public void updateAxeNote(AxeNote axeNote){
        deleteAxeNote(axeNote);
        addAxeNote(axeNote);
    }

    /**
     * 根据keyword查询
     * @param keyWord
     * @return
     */
    public List<AxeNote> query(String keyWord){
        List<AxeNote> axeNotes = new ArrayList<>();
        List<AxeNote> allNotes = getAllAxeNote();
        for(AxeNote axeNote : allNotes){
            if(axeNote.content.contains(keyWord)){
                axeNotes.add(axeNote);
            }
        }
        return axeNotes;
    }


    public AxeDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_NOTE_TABLE_SQL);
        db.execSQL(CREATE_PICTURE_TABLE_SQL);
        db.execSQL(CREATE_MEDIA_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO: 2016/3/10 what ?
    }
}
