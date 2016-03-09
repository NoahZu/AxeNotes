package noahzu.github.io.axenotes.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/3/7.
 */
public class AxeVideo implements Serializable{
    private String thumbPath;
    private String path;



    public String getThumbPath() {
        return thumbPath;
    }

    public void setThumbPath(String thumbPath) {
        this.thumbPath = thumbPath;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
