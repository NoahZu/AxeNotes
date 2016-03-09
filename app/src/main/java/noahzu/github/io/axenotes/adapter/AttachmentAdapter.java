package noahzu.github.io.axenotes.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import noahzu.github.io.axenotes.R;
import noahzu.github.io.axenotes.entity.AxeMedia;

/**
 * Created by Administrator on 2016/3/9.
 */
public class AttachmentAdapter extends RecyclerView.Adapter<AttachementViewHolder> {
    private List<AxeMedia> medias;
    private Context context;
    private LayoutInflater layoutInflater;

    public AttachmentAdapter(Context context,List<AxeMedia> medias){
        this.context = context;
        this.medias = medias;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public AttachementViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_attach_list,null);
        return new AttachementViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AttachementViewHolder holder, final int position) {
        holder.setData(medias.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(v,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.medias.size();
    }

    private OnItemClickListener onItemClickListener;
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
    public interface OnItemClickListener{
        void onItemClick(View view,int position);
    }
}
