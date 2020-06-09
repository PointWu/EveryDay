package com.example.everydayis.activity;
/**
 * DESC:
 * Author:七月无雨
 * Data:主页
 **/
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.everydayis.R;
import java.io.File;
import java.util.Calendar;
import java.util.TimeZone;
public class DemoActivity extends Activity {
    private static final int FILE_SELECT_CODE = 100;
    private static final int REQUEST_SHARE_FILE_CODE = 120;
    private static final int REQUEST_WRITE_STORAGE_PERMISSION = 121;
    private TextView get;
    private Uri shareFileUrl = null;
    private TextView tv_time;
    private TextView tv;
    private TextView con;
    private TextView blew;
    private Button btn,share;
    private static String mYear;
    private static String mMonth;
    private static String mDay;
    private static String mWay;
    private static final int msgKey1 = 1;
    private TextView tvShareFileUri;
    ImageView aaa ;
    String picPath;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    //请求状态码
    private static int REQUEST_PERMISSION_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        tv_time = (TextView) findViewById(R.id.mytime);
        tv = (TextView) findViewById(R.id.test);
        con = (TextView) findViewById(R.id.con);
        blew = (TextView) findViewById(R.id.blew);
        Button btn = (Button) findViewById(R.id.btn);
        Button btnleft = (Button) findViewById(R.id.btnleft);
        textTask();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE_PERMISSION);
            } else {
                Toast.makeText(this, "缺少文件读写权限，可能会造成无法分享文件", Toast.LENGTH_SHORT).show();
            }
        }
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DemoActivity.this,AddDiaryActivity.class);
                startActivity(intent);
            }
        });
        btnleft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DemoActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        View v = LayoutInflater.from(this).inflate(R.layout.view_photo, null, false);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
            }
        }
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;     // 屏幕宽度（像素）
        int height = metric.heightPixels;   // 屏幕高度（像素）
        ImageUtils.layoutView(v,width,height);
        final ScrollView tv = (ScrollView) v.findViewById(R.id.textView);
        aaa = (ImageView) findViewById(R.id.aaa);
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                picPath = ImageUtils.viewSaveToImage(tv,"makemone");
                Log.i("2333", picPath);
                Uri imageUri = Uri.fromFile(new File(picPath));
                Log.i("2333", imageUri.toString());
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_STREAM, imageUri);
                intent.setType("image/*");
                startActivity(Intent.createChooser(intent,"分享到 "));
            }
        };

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().post(runnable);
            }
        });
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                Log.i("MainActivity", "申请的权限为：" + permissions[i] + ",申请结果：" + grantResults[i]);
            }
        }
    }

    public void textTask() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        mYear = String.valueOf(cal.get(Calendar.YEAR)); // 获取当前年份
        mMonth = String.valueOf(cal.get(Calendar.MONTH) + 1);// 获取当前月份
        mDay = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
        tv_time.setText(mYear + "年" + mMonth + "月" + mDay + "日");
        mWay = String.valueOf(cal.get(Calendar.DAY_OF_WEEK));
        if ("1".equals(mWay)) {
            mWay = "天";
        } else if ("2".equals(mWay)) {
            mWay = "一";
        } else if ("3".equals(mWay)) {
            mWay = "二";
        } else if ("4".equals(mWay)) {
            mWay = "三";
        } else if ("5".equals(mWay)) {
            mWay = "四";
        } else if ("6".equals(mWay)) {
            mWay = "五";
        } else if ("7".equals(mWay)) {
            mWay = "六";
        }
        if (mWay == "一") {
            tv.setText("忌颓废");
            con.setText("世界，大约是不堪拯救了，但单个的人总是能被拯救的");
            blew.setText("诗人，布罗茨基/《文明的孩子》");
        } else if (mWay == "二") {
            tv.setText("宜洒脱");
            con.setText("最折磨人的不是脑力活动也不是体力活动，而是操心");
            blew.setText("翻译家，傅雷/《傅雷家书》");
        } else if (mWay == "三") {
            tv.setText("宜翻书");
            con.setText("我从未知道过有什么苦恼是不能为一小时的读书所派遣");
            blew.setText("思想家，孟德斯鸠/《读书》");
        } else if (mWay == "四") {
            tv.setText("忌复制");
            con.setText("我今天的生活，绝不是我昨天生活的冷淡抄袭。");
            blew.setText("作家，司汤达/《红与黑》");
        } else if (mWay == "五") {
            tv.setText("宜充实");
            con.setText("你必须内心丰富，才能摆脱这些生活表面相似");
            blew.setText("作家，王朔/《致女儿书》");
        } else if (mWay == "六") {
            tv.setText("忌跟风");
            con.setText("习惯，是伟大的消音器。");
            blew.setText("导演，保罗/《孤独及其所创造的》");
        } else {
            tv.setText("宜睡觉");
            con.setText("告诉你个秘密，明天就周一了");
            blew.setText("退堂鼓演奏家，吴禹辉/《好好生活》");
        }
    }
}
