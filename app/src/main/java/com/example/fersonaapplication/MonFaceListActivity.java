package com.example.fersonaapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MonFaceListActivity extends AppCompatActivity implements View.OnClickListener {

    // RecyclerView
    ArrayList<MonFaceListVO> data;
    private RecyclerView wantedList_rv;
    private MonRAdapter adapter;

    ImageView logoImg, watnedImg;
    TextView voiceTv;
    Button reportMoveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mon_face_list);

        // 메뉴
        final DrawerLayout drawerLayout = findViewById(R.id.drawlayout);

        findViewById(R.id.menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.END);
            }
        });

        data = new ArrayList<>();
        wantedList_rv = findViewById(R.id.wantedList_rv);

        logoImg = findViewById(R.id.logoImg);
        watnedImg = findViewById(R.id.wantedImg);

        voiceTv = findViewById(R.id.voice_tv);

        reportMoveBtn = findViewById(R.id.reportMove_btn);

        logoImg.setOnClickListener(this);
        reportMoveBtn.setOnClickListener(this);


        // RecyclerView
        for(int i=0;i<4;i++){
            addItem("img");
        }

        adapter = new MonRAdapter(data);
        wantedList_rv.setAdapter(adapter);
        wantedList_rv.setLayoutManager(new LinearLayoutManager(this,RecyclerView.HORIZONTAL, false));

    }

    public void addItem(String imgName){
        MonFaceListVO item = new MonFaceListVO();

        item.setMonList(imgName);

        data.add(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.logoImg:
                Log.d("MonFaceActivity","로고클릭");
                Intent logoIntent = new Intent(MonFaceListActivity.this, MainActivity.class);
                startActivity(logoIntent);
                finish();
                break;
        }

    }
}