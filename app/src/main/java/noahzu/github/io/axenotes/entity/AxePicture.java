package noahzu.github.io.axenotes.entity;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/3/7.
 */
public class AxePicture implements Serializable,Comparable<AxePicture>{
    public String path;


    public AxePicture(String path) {
        this.path = path;
    }

    @Override
    public int compareTo(AxePicture another) {
        if (path == another.path){
            return 0;
        }else{
            return -1;
        }
    }
}
