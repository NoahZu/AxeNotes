package noahzu.github.io.axenotes.activity;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.Toast;

import java.util.ArrayList;

import noahzu.github.io.axenotes.R;
import noahzu.github.io.axenotes.adapter.AttachmentAdapter;
import noahzu.github.io.axenotes.entity.AxeMedia;
import noahzu.github.io.axenotes.entity.AxeNote;
import noahzu.github.io.axenotes.widget.RecycleViewDivider;

public class AttachmentActivity extends AppCompatActivity {

    private static final int REQUEST_VIDEO_CAPTURE = 1;
    private static final String TAG = "AttachmentActivity";
    private static final int REQUEST_VOICE_CAPTURE = 2;
    private static final int SAVE_ATTACH = 3;
    public static final String SAVED_AXENOTE = "saved_axenote";
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
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new RecycleViewDivider(this,LinearLayoutManager.HORIZONTAL));
        toolbar = (Toolbar) findViewById(R.id.attachment__toolbar);
        toolbar.setTitle(axeNote.title + "的附件");
        setSupportActionBar(toolbar);
        recyclerView.setAdapter(adapter);
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
                switch (item.getItemId()) {
                    case R.id.action_add_video:
                        // TODO: 2016/3/9 跳转到添加video
                        captureVideo();
                        break;
                    case R.id.action_add_voice:
                        //// TODO: 2016/3/9 跳转到添加语音
                        captureVoice();
                        break;
                }
                popupMenu.dismiss();
                return true;
            }
        });
        popupMenu.show();
    }

    private void captureVoice() {
        Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);

        startActivityForResult(intent,REQUEST_VOICE_CAPTURE);
    }

    private void captureVideo() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            Uri videoUri = intent.getData();
            String path = videoUri.getPath();
            axeNote.medias.add(new AxeMedia(AxeMedia.TYPE_VIDEO,path));
            adapter.notifyDataSetChanged();
        }
        if(requestCode == REQUEST_VOICE_CAPTURE && resultCode == RESULT_OK){
            Uri uri = intent.getData();
            String path = uri.getPath();
            axeNote.medias.add(new AxeMedia(AxeMedia.TYPE_VOICE,path));
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.attachment_menu,menu);
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            // TODO: 2016/3/9 如果点击了返回按钮，就将附件保存
            Intent intent = new Intent();
            intent.putExtra(SAVED_AXENOTE,axeNote);
            setResult(RESULT_OK, intent);
            finish();
            Toast.makeText(getApplicationContext(),"附件已经保存",Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}
