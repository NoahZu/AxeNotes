package noahzu.github.io.axenotes.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/3/7.
 */
public class AxeNote implements Serializable{
    public String title;
    public String content;
    public String date;
    public String location;
    public long onlyId;//唯一的id，与数据库的主键不同，删除的时候用它来确认
    public List<AxePicture> pictures;
    public List<AxeMedia> medias;

    public boolean isSelected;

    public AxeNote(long onlyId){
        this.onlyId = onlyId;
        pictures = new ArrayList<>();
        medias = new ArrayList<>();
    }

    public AxeNote(long onlyId,String title, String content, String date, String location) {
        this.title = title;
        this.content = content;
        this.date = date;
        this.location = location;
        this.onlyId = onlyId;
        pictures = new ArrayList<>();
        medias = new ArrayList<>();
    }

    public void updateSelf(AxeNote axeNote){
        title = axeNote.title;
        content = axeNote.content;
        date = axeNote.date;
        location = axeNote.location;
        pictures.clear();
        pictures.addAll(axeNote.pictures);
        medias.clear();
        medias.addAll(axeNote.medias);
    }
}
