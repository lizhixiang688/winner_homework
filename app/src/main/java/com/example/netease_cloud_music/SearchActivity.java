package com.example.netease_cloud_music;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import NetRequest.MyJson;
import NetRequest.PostNetRequest;
import Search_bar.RecordsAdapter;
import Search_bar.RecordsDao;

public class SearchActivity extends AppCompatActivity {

    private List<String>recordlist=new ArrayList<>();
    private RecordsDao recordsDao;
    private RecordsAdapter recordsAdapter;
    private ListView listView;
    private EditText editText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        CoverActivity.activityList.add(this);
        View decorview =getWindow().getDecorView();
        decorview.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                |View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        listView=(ListView)findViewById(R.id.listview);
        editText=(EditText)findViewById(R.id.edit_search);
        recordsDao=new RecordsDao(this);
        recordlist=recordsDao.getRecordsList();
        reversedRecords();

        recordsAdapter=new RecordsAdapter(this,R.layout.item_listview,recordlist);
        listView.setAdapter(recordsAdapter);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String keyword = v.getText().toString().trim();
                    Intent intent=new Intent(SearchActivity.this,Search_result_Activity.class);
                    intent.putExtra("to_search",keyword);
                    startActivity(intent);
                    recordsDao.addRecords(keyword);
                    recordsAdapter.notifyDataSetChanged();
                    return true;
                }
                return false;

            }
        });
       listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               editText.setText(recordlist.get(position));
           }
       });
    }

    private void reversedRecords(){
        String temp = "";
        int size = (recordlist.size())/2;
        int foot = recordlist.size()-1;
        //下面的循环实现数组首尾置换
        for (int i=0;i<size;i++){
            foot = foot - i;
            temp = recordlist.get(i);
            recordlist.set(i,recordlist.get(foot));
            recordlist.set(foot,temp);
        }
    }

    public void clear(View view) {
        recordlist.clear();
        recordsDao.clearRecords();
        recordsAdapter.notifyDataSetChanged();

    }

    public void clear_edittext(View view) {
        if (editText.getText()!=null){
            editText.setText("");
        }
    }


}