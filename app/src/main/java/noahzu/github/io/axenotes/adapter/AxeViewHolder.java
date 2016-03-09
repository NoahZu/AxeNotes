package noahzu.github.io.axenotes.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import noahzu.github.io.axenotes.R;
import noahzu.github.io.axenotes.entity.AxeNote;

/**
 * Created by Administrator on 2016/3/7.
 */
public class AxeViewHolder extends RecyclerView.ViewHolder {
    private TextView titleText;
    private TextView dateText;
    private TextView picNumberText;
    private TextView videoNumberText;
    private CheckBox isShowSelectCb;
    public View contentView;

    public AxeViewHolder(View contentView) {
        super(contentView);
        this.contentView = contentView;
        titleText = (TextView) contentView.findViewById(R.id.text_title);
        dateText = (TextView) contentView.findViewById(R.id.text_date);
        picNumberText = (TextView) contentView.findViewById(R.id.text_pic_num);
        videoNumberText = (TextView) contentView.findViewById(R.id.text_video_num);
    }

    public void setData(AxeNote axeNote){
        titleText.setText(axeNote.title);
        dateText.setText(axeNote.date);
        picNumberText.setText(axeNote.pictures.size()+"");
        videoNumberText.setText(axeNote.videos.size() + "");

        if(axeNote.isSelected){
            contentView.setBackgroundResource(android.R.color.darker_gray);
        }else {
            contentView.setBackgroundResource(android.R.color.white);
        }

    }
}
