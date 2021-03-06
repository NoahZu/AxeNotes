package noahzu.github.io.axenotes.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.sharesdk.framework.ShareSDK;
import noahzu.github.io.axenotes.OnekeyShare;
import noahzu.github.io.axenotes.R;
import noahzu.github.io.axenotes.entity.AxeNote;
import noahzu.github.io.axenotes.entity.AxePicture;
import noahzu.github.io.axenotes.entity.City;
import noahzu.github.io.axenotes.utils.BitmapUtil;
import noahzu.github.io.axenotes.utils.DeviceUtil;
import noahzu.github.io.axenotes.utils.FileUtils;
import noahzu.github.io.axenotes.utils.StringUtils;
import noahzu.github.io.axenotes.utils.TimeUtils;
import noahzu.github.io.axenotes.widget.AxeEditText;

public class EditActivity extends AppCompatActivity implements AMapLocationListener {

    public static final String UPDATE_NOTE = "UPDATE_NOTE";
    public static final String ADD_NOTE = "ADD_NOTE";
    private static final int REQUEST_OPEN_ALBUM = 3;
    private static final int REQUEST_OPEN_CAMERA = 5;
    private static final String IMAGE_HOLDER = "axeImg";
    public static final String CURRENT_NOTE = "current_note";
    private static final int REQUEST_ADD_ATTACHMENT = 4;
    public static final String EDIT_NOTE = "edit_note";

    private Toolbar toolbar;
    private AxeEditText contentEdit;
    private FloatingActionButton addButton;
    private EditText titleEdit;

    private TextView dateText;
    private TextView locationText;

    private Menu menu;
    private AxeNote axeNote;

    private int mainAIntent;

    public static final int UPDATE_OK = 5;
    public static final int ADD_OK = 6;


    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        initLocation();
        initView();
        loadData();
    }

    private void initLocation() {
        mLocationClient = new AMapLocationClient(getApplicationContext());
        mLocationClient.setLocationListener(this);
        AMapLocationClientOption mLocationOption = null;
        mLocationOption = new AMapLocationClientOption();
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationOption.setNeedAddress(true);
        mLocationOption.setOnceLocation(false);
        mLocationOption.setWifiActiveScan(false);
        mLocationOption.setMockEnable(false);
        mLocationOption.setInterval(2000);
        mLocationClient.setLocationOption(mLocationOption);
    }

    private void loadData() {
        mainAIntent = getIntent().getIntExtra(MainActivity.TYPE, 2);
        if (mainAIntent == MainActivity.UPDATE_NOTE) {
            axeNote = (AxeNote) getIntent().getSerializableExtra(MainActivity.CURRENT_NOTE);
            setContentText();
            titleEdit.setText(axeNote.title);
        } else {
            long onlyId = TimeUtils.getOnlyIdByDate();
            axeNote = new AxeNote(onlyId);
        }
        String date = StringUtils.formatDateTime(new Date());
        dateText.setText(date);
    }

    private void setContentText() {
        SpannableStringBuilder builder = new SpannableStringBuilder(axeNote.content);
        Pattern pattern = Pattern.compile("/storage/*/.+?\\.\\w{3}");
        Matcher matcher = pattern.matcher(axeNote.content);
        while (matcher.find()) {
            Bitmap bitmap = BitmapFactory.decodeFile(matcher.group());
            builder.setSpan(new ImageSpan(bitmap), matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        contentEdit.setText(builder);
    }

    private List<AxePicture> getBitmapsFromString(String str) {
        List<AxePicture> pictures = new ArrayList<>();

        Pattern pattern = Pattern.compile("/storage/*/.+?\\.\\w{3}");
        Matcher matcher = pattern.matcher(str);
        int i = 0;
        while (matcher.find()) {
            pictures.add(new AxePicture(matcher.group()));
        }
        return pictures;
    }


    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.edit_toolbar);
        contentEdit = (AxeEditText) findViewById(R.id.edit_note_content);

        addButton = (FloatingActionButton) findViewById(R.id.fb_add);
        setSupportActionBar(toolbar);//将Toolbar的对象设置进去替换actionBar

        titleEdit = (EditText) findViewById(R.id.edit_note_title);
        dateText = (TextView) findViewById(R.id.edit_note_date);
        locationText = (TextView) findViewById(R.id.edit_note_location);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditActivity.this, AttachmentActivity.class);
                axeNote.title = titleEdit.getText().toString();
                intent.putExtra(EDIT_NOTE, axeNote);
                startActivityForResult(intent, REQUEST_ADD_ATTACHMENT);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_OPEN_ALBUM && resultCode == RESULT_OK && null != data) {
            String picturePath = getBitmapPath(data);
            File file = new File(Environment.getExternalStorageDirectory(), "axeNotes");
            if (!file.exists()) {
                file.mkdir();
            }
            Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
            int screenWidth = DeviceUtil.getScreenInfo(this).x;
            if (bitmap.getWidth() > screenWidth) {
                bitmap = BitmapUtil.scaleImage(bitmap, screenWidth, ((int) (((float) screenWidth) / ((float) bitmap.getWidth()) * bitmap.getHeight())));
            }
            File bitmapFile = new File(file, StringUtils.formatDate(new Date(), "MMddhhmmss") + ".png");
            try {
                if (bitmapFile == null || !bitmapFile.exists()) {
                    bitmapFile.createNewFile();
                }
                FileOutputStream fileOutputStream = new FileOutputStream(bitmapFile);
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, fileOutputStream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            contentEdit.append("\n");
            contentEdit.insertBitmap(bitmap, bitmapFile.getAbsolutePath());
            contentEdit.append("\n");
        }
        else if (requestCode == REQUEST_ADD_ATTACHMENT && resultCode == RESULT_OK && null != data) {
            //查看附件完毕，刷新附件数目统计
            AxeNote axeNote = (AxeNote) data.getSerializableExtra(AttachmentActivity.SAVED_AXENOTE);
            this.axeNote.medias.clear();
            this.axeNote.medias.addAll(axeNote.medias);
        }
        else if(requestCode == REQUEST_OPEN_CAMERA && resultCode == RESULT_OK && null != data){
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            int screenWidth = DeviceUtil.getScreenInfo(this).x;
            bitmap = BitmapUtil.scaleImage(bitmap, screenWidth, ((int) (((float) screenWidth) / ((float) bitmap.getWidth()) * bitmap.getHeight())));
            String fileName = StringUtils.formatDate(new Date(),"yyyyMMddhhmmss") + ".png";
            File saveFile = FileUtils.createFile(Environment.getExternalStorageDirectory() + "/"+"axeNotes" + "/"+"pic", fileName);
            BitmapUtil.saveBitmap(saveFile.getAbsolutePath(),bitmap);
            contentEdit.append("\n");
            contentEdit.insertBitmap(bitmap, saveFile.getAbsolutePath());
            contentEdit.append("\n");
        }
    }

    private String getBitmapPath(Intent data) {
        Uri selectedImage = data.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        return picturePath;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                //将note传回MainActivity，有MainActivity负责存储的逻辑
                if (titleEdit.getText().toString().equals("") || contentEdit.getText().toString().equals("")) {
                    Toast.makeText(this, "标题和内容不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    sendBack();
                }
                break;
            case R.id.action_location:
                //用户点击了定位按钮,将定位按钮切换成正在定位的图标
                this.menu.findItem(R.id.action_location).setIcon(R.mipmap.icon_locating);
                //启动定位
                mLocationClient.startLocation();
                break;
            case R.id.action_share:
                // TODO: 2016/3/8 分享
                showShare();
                break;
            case R.id.action_add_pic:
                addPicture();
                break;
        }
        return true;
    }

    private void addPicture() {
        final PopupMenu popupMenu = new PopupMenu(this,toolbar);
        popupMenu.getMenuInflater().inflate(R.menu.add_picture_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_open_camera:
                        openAcmera();
                        break;
                    case R.id.action_open_album:
                        openAlbum();
                        break;
                }
                popupMenu.dismiss();
                return true;
            }
        });
        popupMenu.show();

    }

    /**
     * 打开相机去获取图片
     */
    private void openAcmera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_OPEN_CAMERA);
    }

    /**
     * 打开相册去获取图片
     */
    private void openAlbum() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, REQUEST_OPEN_ALBUM);
    }

    private void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        oks.setText("hi 我在斧子便签写了一段文字，分享给大家:\n" + getPureText(contentEdit.getText().toString()));
        List<AxePicture> pic = getBitmapsFromString(contentEdit.getText().toString());
        if (pic != null && pic.size() > 0) {
            oks.setImagePath(pic.get(0).path);
        }
        oks.show(this);
    }

    private String getPureText(String text) {
        List<AxePicture> pictures = getBitmapsFromString(text);
        if (pictures != null) {
            for (int i = 0; i < pictures.size(); i++) {
                text = text.replace(pictures.get(i).path, "[图片]");
            }
        }
        return text;
    }

    private void sendBack() {
        String content = contentEdit.getText().toString();
        String date = dateText.getText().toString();
        String title = titleEdit.getText().toString();
        String location = locationText.getText().toString();
        Intent intent = new Intent();
        List<AxePicture> pictures = getBitmapsFromString(contentEdit.getText().toString());
        if (mainAIntent == MainActivity.UPDATE_NOTE) {
            axeNote.title = title;
            axeNote.content = content;
            axeNote.location = location;
            axeNote.date = date;
            axeNote.pictures.clear();
            axeNote.pictures.addAll(pictures);
            intent.putExtra(UPDATE_NOTE, axeNote);
            setResult(UPDATE_OK, intent);
        } else if (mainAIntent == MainActivity.ADD_NOTE) {
            axeNote.title = title;
            axeNote.content = content;
            axeNote.date = date;
            axeNote.location = location;
            axeNote.pictures.clear();
            axeNote.pictures.addAll(pictures);
            intent.putExtra(ADD_NOTE, axeNote);
            setResult(ADD_OK, intent);
        }
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        //定位成功G:\AndroidProjectWorkSpace\AxeNotes\axe.jks
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                City city = new City(amapLocation);
                setLocation(city);
            } else {
                Toast.makeText(this, "定位失败，请打开gps、wifi、gprs中的任意一个重新定位", Toast.LENGTH_SHORT).show();

            }
        }
        this.menu.findItem(R.id.action_location).setIcon(R.mipmap.icon_location);
    }

    private void setLocation(City city) {
        locationText.setText(city.getSimpleLocation());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("警告！")
                    .setMessage("确定放弃本次输入吗？")
                    .setNegativeButton("取消", null)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .create().show();
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stopLocation();
        mLocationClient.onDestroy();
    }
}
