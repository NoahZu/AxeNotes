package noahzu.github.io.axenotes.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import noahzu.github.io.axenotes.entity.AxeNote;
import noahzu.github.io.axenotes.entity.AxePicture;

/**
 * Created by Administrator on 2016/3/7.
 */
public class AxeDbHelper extends SQLiteOpenHelper{
    private static AxeDbHelper axeDbHelper;
    private static final String DB_NAME = "axe_note.db";

    private static final String CREATE_TABLE_SQL = "create table axe_table (" +
            "_id integer primary key autoincrement ," +
            "only_id long not null,"+
            "title varchar(128)," +
            "content text," +
            "date varchar(128)," +
            "location varchar(512)," +
            "pic_path varchar(512)," +
            "video_path varchar(512)," +
            "video_pic_path varchar(512)" +
            ");";

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
        // TODO: 2016/3/7 加入图片和视频以后要把后边的字段实现
        StringBuffer sbPath = new StringBuffer();
        for(AxePicture axePicture : axeNote.pictures){
            sbPath.append(axePicture.path+";");
        }
        sbPath.deleteCharAt(sbPath.length()-1);
        String sql = "insert into axe_table values(null,"+axeNote.onlyId+",'"+axeNote.title+"','"+axeNote.content+"','"+axeNote.date+"','"+axeNote.location+"','"+sbPath.toString()+"',null,null)";
        getWritableDatabase().execSQL(sql);
    }

    /**
     * 查询所有note
     * @return
     */
    public List<AxeNote> getAllAxeNote(){
        String sql = "select * from axe_table";
        List<AxeNote> axeNotes = new ArrayList<>();
        Cursor cursor = getWritableDatabase().rawQuery(sql,new String[]{});

        while (cursor.moveToNext()){
            AxeNote axeNote = new AxeNote(cursor.getLong(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5));
            String[] paths = cursor.getString(6).split(";");
            for(int i = 0;i<paths.length;i++){
                axeNote.pictures.add(new AxePicture(paths[i]));
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
        try {
            String sql = "delete from axe_table where only_id="+deletedAxeNote.onlyId+";";
            getWritableDatabase().execSQL(sql);
            return true;
        }
        catch (Exception e){
            return false;
        }
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
        StringBuffer sbPath = new StringBuffer();
        for(AxePicture axePicture : axeNote.pictures){
            sbPath.append(axePicture.path+";");
        }
        String sql = "update axe_table set title='"+axeNote.title+"' ,content='"+axeNote.content+"',location='"+axeNote.location+"',pic_path = '"+sbPath.toString()+"' where only_id = "+axeNote.onlyId+";";
        getWritableDatabase().execSQL(sql);
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
        db.execSQL(CREATE_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
