package noahzu.github.io.axenotes.widget;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/3/8.
 */
public class NoteEditorView extends LinearLayout {
    private List<View> childs;


    public NoteEditorView(Context context) {
        super(context);
        init();
    }

    private void init() {
        setGravity(Gravity.CENTER_HORIZONTAL);
        childs = new ArrayList<>();
    }

    public void add(View view){
        if(view instanceof TextView){

        }else if(view instanceof ImageView){

        }else if(view instanceof ImageButton){

        }
    }

}
