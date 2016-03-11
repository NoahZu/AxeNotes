package noahzu.github.io.axenotes.entity;

import android.net.Uri;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/3/9.
 */
public class AxeMedia implements Serializable{

    public static final int TYPE_VIDEO = 1;
    public static final int TYPE_VOICE = 2;


    public String title;
    public int type;
    public String path;

    public AxeMedia(int type,String title,String path){
        this.type = type;
        this.path = path;
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public String getPath() {
        return path;
    }

    public String getTitle() {
        return title;
    }
}
