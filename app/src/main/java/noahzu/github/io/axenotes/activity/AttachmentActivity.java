package noahzu.github.io.axenotes.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.PopupMenu;

import java.util.ArrayList;

import noahzu.github.io.axenotes.R;
import noahzu.github.io.axenotes.adapter.AttachmentAdapter;
import noahzu.github.io.axenotes.entity.AxeNote;

public class AttachmentActivity extends AppCompatActivity {

    private AxeNote axeNote;

    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private AttachmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attachment);


        loadAxeNote();
        initView();
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.attachment_list);
        toolbar = (Toolbar) findViewById(R.id.attachment__toolbar);
        toolbar.setTitle(axeNote.title + "的附件");
        setSupportActionBar(toolbar);

    }

    private void loadAxeNote() {
        axeNote = (AxeNote) getIntent().getSerializableExtra(EditActivity.EDIT_NOTE);
        if (axeNote.medias == null){
            axeNote.medias = new ArrayList<>();
        }
        adapter = new AttachmentAdapter(this,axeNote.medias);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_add_attachment:
                //TODO: 2016/3/9 询问添加什么样的附件 视频 or 语音
                showPopupMenu();
                break;
        }
        return true;
    }

    private void showPopupMenu() {
        final PopupMenu popupMenu = new PopupMenu(this,toolbar);
        popupMenu.getMenuInflater().inflate(R.menu.add_media_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_add_video:
                        // TODO: 2016/3/9 跳转到添加video 
                        break;
                    case R.id.action_add_voice:
                        //// TODO: 2016/3/9 跳转到添加语音
                        break;
                }
                popupMenu.dismiss();
                return true;
            }
        });
//        popupMenu.show();
        popupMenu.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.attachment_menu,menu);
        return true;
    }
}
