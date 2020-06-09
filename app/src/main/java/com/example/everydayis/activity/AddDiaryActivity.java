package com.example.everydayis.activity;
/**
 * DESC:
 * Author:七月无雨
 * Data:添加日记
 **/
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.support.v7.widget.AppCompatEditText;
import android.widget.Toast;
import com.example.everydayis.R;
public class AddDiaryActivity<ImageGetter> extends AppCompatActivity {
    private EditText titleText;
    private MyEditText contentText;
    private Button save,b;
    int id;
    String myPath;
    private TextView time1;
    private static final int msgKey1 = 1;
    private static final int PHOTO_SUCCESS = 1;
    private static final int CAMERA_SUCCESS = 2;
    public static final String TAG="RightFragment";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_diary);
        titleText=(EditText) this.findViewById(R.id.title);
        contentText=(MyEditText) this.findViewById(R.id.content);
        b=(Button)findViewById(R.id.phtoto);
        time1=(TextView) this.findViewById(R.id.time1);
        save=(Button) this.findViewById(R.id.save);
        //显示时间
        new  TimeThread().start();
        //插入图片——选择插入的图片
        b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final CharSequence[] items = { "手机相册", "相机拍摄" };
                AlertDialog dlg = new AlertDialog.Builder(AddDiaryActivity.this).setTitle("你想怎么获取图片？").setItems(items,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                //这里item是根据选择的方式,
                                //在items数组里面定义了两种方式, 拍照的下标为1所以就调用拍照方法
                                if(item==1){
                                    Intent getImageByCamera= new Intent("android.media.action.IMAGE_CAPTURE");
                                    startActivityForResult(getImageByCamera, CAMERA_SUCCESS);
                                }else{
                                    Intent getImage = new Intent(Intent.ACTION_GET_CONTENT);
                                    getImage.addCategory(Intent.CATEGORY_OPENABLE);
                                    getImage.setType("image/*");
                                    startActivityForResult(getImage, PHOTO_SUCCESS);
                                }
                            }
                        }).create();
                dlg.show();
            }
        });

        //获取上一个界面传递过来的参数
        final Bundle bundle = this.getIntent().getBundleExtra("diary");
        //如果传过来的参数是空的
        if(bundle==null){
            /* 因为setOnclicklistener方法传的参数是OnClickListener接口，
               所以你需要new一个接口当做参数传入，
               至于为什么setOnclickListener必须要写在Button.后，
               这是因为对象调用方法都是对象名.方法名这样写的
            */
            save.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    String title=titleText.getText().toString();
                    String content=contentText.getText().toString();
                    DiaryService diaryService=new DiaryService(AddDiaryActivity.this);
                    Diary diary=new Diary(title, content, fomate());
                    diaryService.save(diary);
                    Intent intent=new Intent();
                    intent.setClass(AddDiaryActivity.this, DiaryActivity.class);
                    startActivity(intent);
                }
            });
        }else{
            titleText.setText(bundle.getString("title"));
            String content = bundle.getString("content");
//            contentText.setText(bundle.getString("content"));
            ///解析图片
            SpannableString span_str = new SpannableString(content);
            int startindex=0;
            Pattern p = Pattern.compile("/sdcard/myImage/[0-9]{13}+.jpg");
            Matcher m = p.matcher(content);
            while (m.find()) {
                if(m.start()>0){
                    contentText.append(content.substring(startindex,m.start()));
                }

                String mypath = m.group();
                Bitmap bitmap = BitmapFactory.decodeFile(mypath);
                Log.d("wuuuuuuuuuuuuu", mypath);
                //已经获取到了.....到底为啥解析不出来啊啊啊啊啊啊
                ImageSpan span = new ImageSpan(this, bitmap);
                span_str.setSpan(span, 0, mypath.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                contentText.append(span_str);
            }

            id =bundle.getInt("id");
            save.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    String title=titleText.getText().toString();
                    String content=contentText.getText().toString();
                    Log.d(TAG,content);
                    DiaryService diaryService=new DiaryService(AddDiaryActivity.this);
                    Diary diary=new Diary(id,title, content, fomate());
                    diaryService.update(diary);
                    Intent intent=new Intent();
                    intent.setClass(AddDiaryActivity.this, DiaryActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    public class TimeThread extends  Thread{
        @Override
        public void run() {
            super.run();
            do{
                try {
                    Thread.sleep(1000);
                    Message msg = new Message();
                    msg.what = msgKey1;
                    mHandler.sendMessage(msg);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }while (true);
        }
    }
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case msgKey1:
                    long time = System.currentTimeMillis();
                    Date date = new Date(time);
                    SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
                    time1.setText(format.format(date));
                    break;
                default:
                    break;
            }
        }
    };
    public String fomate(){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy年MM月dd日  hh时:mm分:ss秒");
        return simpleDateFormat.format(new Date());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_add_diary, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                Intent intent = new Intent();
                intent.setClass(AddDiaryActivity.this, DiaryActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //相机
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        ContentResolver resolver = getContentResolver();
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PHOTO_SUCCESS:
                    //获得图片的uri
                    Uri originalUri = intent.getData();
                    Bitmap bitmap = null;
                    try {
                        Bitmap originalBitmap = BitmapFactory.decodeStream(resolver.openInputStream(originalUri));
                        bitmap = resizeImage(originalBitmap, 500, 500);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    String name = Calendar.getInstance(Locale.CHINA).getTimeInMillis() + ".jpg";
                    FileOutputStream FOut = null;
                    File file = new File("/sdcard/myImage/");
                    file.mkdirs();// 创建文件夹
                    String fileName = "/sdcard/myImage/"+name;
                    //editText.setText(fileName);
                    try {
                        FOut = new FileOutputStream(fileName);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, FOut);// 把数据写入文件
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }finally {
                        try {
                            FOut.flush();
                            FOut.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    myPath=fileName;//接着根据存放的路径获取图片放到EditText中
                    Log.d("fileName",fileName);

                    Toast.makeText(this, myPath, Toast.LENGTH_SHORT).show();

                    SpannableString span_str = new SpannableString(myPath);
                    Bitmap my_bm=BitmapFactory.decodeFile(myPath);
//                    Bitmap my_rbm=resizeimg.resizeImage(my_bm, 300, 300);
                    ImageSpan span = new ImageSpan(this, my_bm);
                    span_str.setSpan(span, 0, myPath.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    Editable et = contentText.getText();// 先获取Edittext中的内容
                    int start = contentText.getSelectionStart();
                    et.insert(start, span_str);// 设置ss要添加的位置
                    contentText.setText((CharSequence)et);// 把et添加到Edittext中
                    contentText.setSelection(start + span_str.length());// 设置Edittext中光标在最后面显示
                    break;
                case CAMERA_SUCCESS:
                    Bundle extras = intent.getExtras();
                    Bitmap originalBitmap1 = (Bitmap) extras.get("data");
                    if(originalBitmap1 != null){
                        bitmap = resizeImage(originalBitmap1, 500, 500);
                        //根据Bitmap对象创建ImageSpan对象
                        ImageSpan imageSpan = new ImageSpan(AddDiaryActivity.this, bitmap);
                        //创建一个SpannableString对象，以便插入用ImageSpan对象封装的图像
                        SpannableString spannableString = new SpannableString("[local]"+1+"[/local]");
                        //  用ImageSpan对象替换face
                        spannableString.setSpan(imageSpan, 0, "[local]1[local]".length()+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        //将选择的图片追加到EditText中光标所在位置
                        int index = contentText.getSelectionStart(); //获取光标所在位置
                        Editable edit_text = contentText.getEditableText();
                        if(index <0 || index >= edit_text.length()){
                            edit_text.append(spannableString);
                        }else{
                            edit_text.insert(index, spannableString);
                        }
                    }else{
                        Toast.makeText(AddDiaryActivity.this, "获取图片失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }
        }
    }
    /**
     * 图片缩放
     * @param originalBitmap 原始的Bitmap
     * @param newWidth 自定义宽度
     * @return 缩放后的Bitmap
     */
    private Bitmap resizeImage(Bitmap originalBitmap, int newWidth, int newHeight){
        int width = originalBitmap.getWidth();
        int height = originalBitmap.getHeight();
        //定义欲转换成的宽、高
//            int newWidth = 200;
//            int newHeight = 200;
        //计算宽、高缩放率
        float scanleWidth = (float)newWidth/width;
        float scanleHeight = (float)newHeight/height;
        //创建操作图片用的matrix对象 Matrix
        Matrix matrix = new Matrix();
        // 缩放图片动作
        matrix.postScale(scanleWidth,scanleHeight);
        //旋转图片 动作
        //matrix.postRotate(45);
        // 创建新的图片Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(originalBitmap,0,0,width,height,matrix,true);
        return resizedBitmap;
    }
}


