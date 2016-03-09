package noahzu.github.io.axenotes.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import noahzu.github.io.axenotes.R;
import noahzu.github.io.axenotes.entity.AxeMedia;
import noahzu.github.io.axenotes.entity.AxeNote;

/**
 * Created by Administrator on 2016/3/7.
 */
public class AxeViewHolder extends RecyclerView.ViewHolder {
    private TextView titleText;
    private TextView dateText;
    private TextView picNumberText;
    private TextView videoNumberText;
    private TextView voiceNumberText;
    public View contentView;

    public AxeViewHolder(View contentView) {
        super(contentView);
        this.contentView = contentView;
        titleText = (TextView) contentView.findViewById(R.id.text_title);
        dateText = (TextView) contentView.findViewById(R.id.text_date);
        picNumberText = (TextView) contentView.findViewById(R.id.text_pic_num);
        videoNumberText = (TextView) contentView.findViewById(R.id.text_video_num);
        voiceNumberText = (TextView) contentView.findViewById(R.id.text_voice_num);
    }

    public void setData(AxeNote axeNote){
        titleText.setText(axeNote.title);
        dateText.setText(axeNote.date);
        int videoCount = getVideoNumber(axeNote.medias);
        int voiceCount = axeNote.medias.size() - videoCount;
        picNumberText.setText(axeNote.pictures.size()+"");
        videoNumberText.setText(videoCount + "");
        voiceNumberText.setText(voiceCount + "");
        if(axeNote.isSelected){
            contentView.setBackgroundResource(android.R.color.darker_gray);
        }else {
            contentView.setBackgroundResource(android.R.color.white);
        }
    }
    public int getVideoNumber(List<AxeMedia> medias){
        int count = 0;
        for(AxeMedia axeMedia : medias){
            if(axeMedia.getType() == AxeMedia.TYPE_VIDEO){
                count ++;
            }
        }
        return count;
    }
}
