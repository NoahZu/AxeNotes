package noahzu.github.io.axenotes.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import noahzu.github.io.axenotes.R;
import noahzu.github.io.axenotes.entity.AxeNote;

/**
 * Created by Administrator on 2016/3/7.
 */
public class AxeAdapter extends RecyclerView.Adapter<AxeViewHolder> {
    private List<AxeNote> notes;
    private Context context;
    private LayoutInflater layoutInflater;

    private OnAxeItemClickListener onAxeItemClickListener;
    private OnAxeItemLongClickListener onAxeItemLongClickListener;

    public AxeAdapter(Context context,List<AxeNote> notes){
        this.context = context;
        this.notes = notes;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public AxeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AxeViewHolder(layoutInflater.inflate(R.layout.item_note_list,null));
    }

    @Override
    public void onBindViewHolder(AxeViewHolder holder, final int position) {
        holder.setData(notes.get(position));
        holder.contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onAxeItemClickListener != null) {
                    onAxeItemClickListener.onClick(v,position);
                }
            }
        });
        holder.contentView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(onAxeItemLongClickListener != null){
                    onAxeItemLongClickListener.onLongClick(v,position);
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }


    public interface OnAxeItemClickListener{
        void onClick(View view,int position);
    }

    public interface OnAxeItemLongClickListener{
        void onLongClick(View view,int position);
    }

    public void setOnAxeItemLongClickListener(OnAxeItemLongClickListener onAxeItemLongClickListener) {
        this.onAxeItemLongClickListener = onAxeItemLongClickListener;
    }

    public void setOnAxeItemClickListener(OnAxeItemClickListener onAxeItemClickListener) {
        this.onAxeItemClickListener = onAxeItemClickListener;
    }
}
