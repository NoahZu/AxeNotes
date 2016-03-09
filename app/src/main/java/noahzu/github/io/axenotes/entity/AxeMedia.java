package noahzu.github.io.axenotes.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/3/9.
 */
public class AxeMedia implements Serializable{

    public static final int TYPE_VIDEO = 1;
    public static final int TYPE_VOICE = 2;


    public int type;
    public String path;

    public AxeMedia(int type,String path){
        this.type = type;
        this.path = path;
    }

    public int getType() {
        return type;
    }

    public String getPath() {
        return path;
    }
}
