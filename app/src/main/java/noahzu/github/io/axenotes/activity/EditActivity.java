package noahzu.github.io.axenotes.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import noahzu.github.io.axenotes.R;
import noahzu.github.io.axenotes.entity.AxeNote;
import noahzu.github.io.axenotes.entity.AxePicture;
import noahzu.github.io.axenotes.entity.City;
import noahzu.github.io.axenotes.utils.StringUtils;
import noahzu.github.io.axenotes.utils.TimeUtils;
import noahzu.github.io.axenotes.widget.AxeEditText;

public class EditActivity extends AppCompatActivity implements AMapLocationListener{

    public static final String UPDATE_NOTE = "UPDATE_NOTE";
    public static final String ADD_NOTE = "ADD_NOTE";
    private static final int RESULT_LOAD_IMAGE = 3;
    private static final String IMAGE_HOLDER = "axeImg";

    private Toolbar toolbar;
    private AxeEditText contentEdit;
    private FloatingActionButton addButton;
    private EditText titleEdit;

    private TextView dateText;
    private TextView locationText;

    private Menu menu;
    private AxeNote axeNote;
    private List<AxePicture> pictures;

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
        //设置定位回调监听
        mLocationClient.setLocationListener(this);
        //声明mLocationOption对象
        AMapLocationClientOption mLocationOption = null;
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
         mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
         mLocationOption.setOnceLocation(false);
        //设置是否强制刷新WIFI，默认为强制刷新
         mLocationOption.setWifiActiveScan(false);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
         mLocationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
         mLocationOption.setInterval(2000);
        //给定位客户端对象设置定位参数
          mLocationClient.setLocationOption(mLocationOption);
    }

    private void loadData() {
        mainAIntent = getIntent().getIntExtra(MainActivity.TYPE,2);
        if(mainAIntent == MainActivity.UPDATE_NOTE) {
            axeNote = (AxeNote) getIntent().getSerializableExtra(MainActivity.CURRENT_NOTE);
            setContentText();
            titleEdit.setText(axeNote.title);
        }else {
            long onlyId = TimeUtils.getOnlyIdByDate();
            axeNote = new AxeNote(onlyId);
        }
        String date = StringUtils.formatDate(new Date());
        dateText.setText(date);
    }

    private void setContentText() {
        SpannableStringBuilder builder = new SpannableStringBuilder(axeNote.content);
        String flag = IMAGE_HOLDER;
        Pattern pattern = Pattern.compile("/storage/emulated/.+?\\.\\w{3}");
        Matcher matcher =  pattern.matcher(axeNote.content);
        while (matcher.find())
        {
            Bitmap bitmap =  BitmapFactory.decodeFile(matcher.group());
            builder.setSpan(new ImageSpan(bitmap), matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        contentEdit.setText(builder);
    }

    private List<AxePicture> getBitmapsFromString(String str){
        List<AxePicture> pictures = new ArrayList<>();

        Pattern pattern = Pattern.compile("/storage/emulated/.+?\\.\\w{3}");
        Matcher matcher =  pattern.matcher(str);
        int i = 0;
        while (matcher.find())
        {
            pictures.add(new AxePicture(matcher.group()));
        }
        return pictures;
    }


    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.edit_toolbar);
        contentEdit = (AxeEditText) findViewById(R.id.edit_note_content);

        contentEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        addButton = (FloatingActionButton) findViewById(R.id.fb_add);
        setSupportActionBar(toolbar);//将Toolbar的对象设置进去替换actionBar

        titleEdit = (EditText) findViewById(R.id.edit_note_title);
        dateText = (TextView) findViewById(R.id.edit_note_date);
        locationText = (TextView) findViewById(R.id.edit_note_location);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
            contentEdit.insertBitmap(bitmap, picturePath);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_save:
                //将note传回MainActivity，有MainActivity负责存储的逻辑
                sendBack();
                break;
            case R.id.action_location:
                //用户点击了定位按钮,将定位按钮切换成正在定位的图标
                this.menu.findItem(R.id.action_location).setIcon(R.mipmap.icon_locating);
                //启动定位
                mLocationClient.startLocation();
                break;
            case R.id.action_share:
                // TODO: 2016/3/8 分享
                break;
        }
        return true;
    }

    private void sendBack() {
        String content = contentEdit.getText().toString();
        String date =  dateText.getText().toString();
        String title = titleEdit.getText().toString();
        String location = locationText.getText().toString();
        Intent intent = new Intent();
        List<AxePicture> pictures = getBitmapsFromString(contentEdit.getText().toString());
        if(mainAIntent == MainActivity.UPDATE_NOTE){
            axeNote.title = title;
            axeNote.content = content;
            axeNote.location = location;
            axeNote.date =date;
            axeNote.pictures.clear();
            axeNote.pictures.addAll(pictures);
            intent.putExtra(UPDATE_NOTE, axeNote);
            setResult(UPDATE_OK, intent);
        }
        else if(mainAIntent == MainActivity.ADD_NOTE){
            axeNote.title = title;
            axeNote.content = content;
            axeNote.date = date;
            axeNote.location = location;
            axeNote.pictures.clear();
            axeNote.pictures.addAll(pictures);
            intent.putExtra(ADD_NOTE, axeNote);
            setResult(ADD_OK,intent);
        }
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu,menu);
        this.menu = menu;
        return true;
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        //定位成功
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                City city = new City(amapLocation);
                setLocation(city);
            } else {
                Toast.makeText(this,"定位失败，请打开gps、wifi、gprs中的任意一个重新定位",Toast.LENGTH_LONG).show();

            }
        }
        this.menu.findItem(R.id.action_location).setIcon(R.mipmap.icon_location);
    }

    private void setLocation(City city) {
        locationText.setText(city.getSimpleLocation());
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stopLocation();
        mLocationClient.onDestroy();
    }
}
