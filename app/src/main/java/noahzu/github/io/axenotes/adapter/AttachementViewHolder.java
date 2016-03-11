package noahzu.github.io.axenotes.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import noahzu.github.io.axenotes.R;
import noahzu.github.io.axenotes.entity.AxeMedia;

/**
 * Created by Administrator on 2016/3/9.
 */
public class AttachementViewHolder extends RecyclerView.ViewHolder {

    private TextView textMediaTitle;
    private ImageView imgMediaIcon;
    public View itemView;


    public AttachementViewHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;
        textMediaTitle = (TextView) itemView.findViewById(R.id.text_media_title);
        imgMediaIcon = (ImageView) itemView.findViewById(R.id.img_media_icon);

    }

    public void setData(AxeMedia axeMedia){
        if(axeMedia.getType() == AxeMedia.TYPE_VIDEO){
            imgMediaIcon.setImageResource(R.mipmap.video_icon);
        }else {
            imgMediaIcon.setImageResource(R.mipmap.voice_icon);
        }
        textMediaTitle.setText(axeMedia.getTitle());
    }

}
