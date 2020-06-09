package com.example.everydayis.activity;
/**
 * DESC:
 * Author:七月无雨
 * Data:日记列表
 **/
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.example.everydayis.R;

public class DiaryActivity extends AppCompatActivity{
    private List<Map<String, ?>> diary;
    private ListView diaryList;
    private  ImageButton del;
    private Button add, delete;
    DiaryService diaryService;
    AlertDialog deleteDiaryAlert;
    private Button item_btn;
    SimpleAdapter simpleAdapter;
    int id;

    private int num = 0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);
        onRestart();

    }
    @Override
    protected void onRestart() {
        super.onRestart();
        diaryList = (ListView) this.findViewById(R.id.diary);
        add = (Button) this.findViewById(R.id.add);
        diary = getDiary();
        simpleAdapter = new SimpleAdapter(this, diary, R.layout.list_item,
                new String[]{"title", "pubdate"}, new int[]{R.id.title, R.id.pubdate});
        diaryList.setAdapter(simpleAdapter);
        //获取删除时需要的id
        diaryList.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                id = (Integer) diary.get(position).get("id");
            }
        });
        //长时间按该日志，可以跳转至修改页面
        diaryList.setOnItemLongClickListener(new OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,final int  position, long arg3) {
                final int position1  = position + 1;
                AlertDialog alertDialog = new AlertDialog.Builder(DiaryActivity.this).create();
                alertDialog.setMessage("你想干啥？");
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "查看", new android.content.DialogInterface .OnClickListener() {
                    @Override
                    public void onClick(android.content.DialogInterface dialogInterface, int i) {
                        id = (Integer) diary.get(position).get("id");
                        String title = (String) diary.get(position).get("title");
                        String content=(String) diary.get(position).get("content");
                        Log.d("ssssssssssss", content);
                        Diary d = new Diary(id, title, content);
                        Intent intent = new Intent();
                        intent.setClass(DiaryActivity.this, AddDiaryActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("id", id);
                        bundle.putString("title", title);
                        bundle.putString("content", content);
                        intent.putExtra("diary", bundle);
                        startActivity(intent);

                    }

                });
                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteDiaryDialog();
                        deleteDiaryAlert.show();
                    }
                });
                alertDialog.show();
                return false;
            }
        });
    }

    private List<Map<String, ?>> getDiary() {
        List<Map<String, ?>> data = new ArrayList<Map<String, ?>>();
        Map<String, Object> item = null;
        diaryService = new DiaryService(DiaryActivity.this);
        List<Diary> list = diaryService.getAllDiary();
        for (int i = 0; i < list.size(); i++) {
            Diary d = list.get(i);
            item = new HashMap<String, Object>();
            item.put("id", d.getId());// ID
            item.put("title", d.getTitle());// 标题
            item.put("content",d.getContent());
            item.put("pubdate", d.getPubdate());// 出版日期
            data.add(item);
        }
        return data;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        // 创建菜单，并设置图表
        menu.add(0, R.id.add, 0, "编辑新日志").setIcon(
                android.R.drawable.ic_input_add);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                Intent intent = new Intent();
                intent.setClass(DiaryActivity.this, AddDiaryActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    // 删除日志
    private void deleteDiaryDialog(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("请确定是否删除日记");
        alertDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // System.out.print("id:"+id);
                        diaryService.delete(id);
                        onRestart();
                    }
                });
        alertDialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        deleteDiaryAlert = alertDialog.create();

    }
}
