package noahzu.github.io.axenotes.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.media.MediaPlayer;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import noahzu.github.io.axenotes.R;
import noahzu.github.io.axenotes.adapter.AttachmentAdapter;
import noahzu.github.io.axenotes.entity.AxeMedia;
import noahzu.github.io.axenotes.entity.AxeNote;
import noahzu.github.io.axenotes.utils.StringUtils;
import noahzu.github.io.axenotes.widget.RecycleViewDivider;

public class AttachmentActivity extends AppCompatActivity {

    private static final int REQUEST_VIDEO_CAPTURE = 1;
    private static final String TAG = "AttachmentActivity";
    private static final int REQUEST_VOICE_CAPTURE = 2;
    private static final int SAVE_ATTACH = 3;
    public static final String SAVED_AXENOTE = "saved_axenote";
    public static final String PLAY_VIDEO = "play_video";
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
        recyclerView.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.HORIZONTAL));
        toolbar = (Toolbar) findViewById(R.id.attachment__toolbar);
        toolbar.setTitle(axeNote.title + "的附件");
        setSupportActionBar(toolbar);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new AttachmentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(AttachmentActivity.this, PlayVideoActivity.class);
                AxeMedia media = axeNote.medias.get(position);
                if (media.getType() == AxeMedia.TYPE_VIDEO) {
                    intent.putExtra(PLAY_VIDEO, media);
                    startActivity(intent);
                } else {
                    playSound(media);
                }
            }
        });
    }

    private void playSound(AxeMedia media) {
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(this,Uri.parse(media.path));
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    Toast.makeText(AttachmentActivity.this, "播放开始", Toast.LENGTH_SHORT).show();
                    mp.start();
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    Toast.makeText(AttachmentActivity.this, "播放结束", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

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
                        captureVideo();
                        break;
                    case R.id.action_add_voice:
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
            String path = videoUri.toString();
            String title = StringUtils.formatDate(new Date());
            AxeMedia axeMedia = new AxeMedia(AxeMedia.TYPE_VIDEO,title,path);
            axeNote.medias.add(axeMedia);
            adapter.notifyDataSetChanged();
        }
        if(requestCode == REQUEST_VOICE_CAPTURE && resultCode == RESULT_OK){
            Uri uri = intent.getData();
            String path = uri.toString();
            String title = StringUtils.formatDate(new Date());
            AxeMedia axeMedia = new AxeMedia(AxeMedia.TYPE_VOICE,title,path);
            axeNote.medias.add(axeMedia);
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
            Intent intent = new Intent();
            intent.putExtra(SAVED_AXENOTE,axeNote);
            setResult(RESULT_OK, intent);
            finish();
            Toast.makeText(getApplicationContext(),"附件已经保存",Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}
