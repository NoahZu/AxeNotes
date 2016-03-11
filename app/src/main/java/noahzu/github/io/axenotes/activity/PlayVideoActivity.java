package noahzu.github.io.axenotes.activity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import noahzu.github.io.axenotes.R;
import noahzu.github.io.axenotes.entity.AxeMedia;

public class PlayVideoActivity extends AppCompatActivity {

    private VideoView videoView;
    private AxeMedia axeMedia;
    private ImageView replayImage;
    private boolean isPlaying;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);

        loadData();
        initView();
    }

    private void loadData() {
        axeMedia = (AxeMedia) getIntent().getSerializableExtra(AttachmentActivity.PLAY_VIDEO);
        if(axeMedia.getType()  != AxeMedia.TYPE_VIDEO){
            Toast.makeText(this,"格式不对",Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void initView() {
        replayImage = (ImageView) findViewById(R.id.img_replay);
        replayImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isPlaying){
                    replayImage.setVisibility(View.GONE);
                    videoView.start();
                }
            }
        });

        videoView = (VideoView) findViewById(R.id.video_view);
        videoView.setVideoURI(Uri.parse(axeMedia.getPath()));
        videoView.setMediaController(new MediaController(this));
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                videoView.start();
                isPlaying = true;
                replayImage.setVisibility(View.GONE);
            }
        });
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                isPlaying = false;
                replayImage.setVisibility(View.VISIBLE);
            }
        });
    }
}
