package com.example.fersonaapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FragmentReport extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // RecyclerView
    ArrayList<MonFaceListVO> data = new ArrayList<>();
    private RecyclerView wantedListRv;
    private MonRAdapter adapter;

    RequestQueue requestQueue;
    ScrollView scrollView;
    FragmentMypage fragmentMypage;
    LinearLayout mainLl, step1Ll, step2Ll, step3Ll, step4Ll, step5Ll;
    EditText monMakeEt, reportConEt, repAdrET;
    Button step1Btn, step2Btn, step3Btn, step4Btn, wantedviewBtn, infoViewBtn, step5Btn, submitBtn;
    ImageButton voiceBtn;
    ImageView noticeImg, userImg,wantedImg, wantResultImg, monResultImg, monMake1Img, monMake2Img, monMake3Img, monMake4Img;
    RadioButton rd1, rd2, rd3, rd4, rd5, rd6, rd7, rd8, rd9;
    Spinner wantedSpin;
    TextView dateTv, timeTv, nameTv, phoneTv, wantedcontentTv, reportGetTv, reportGetAdrTv;
    DatePicker repDate;
    TimePicker repTime;
    CheckBox wantedCk, infoCk;

    Intent intent;
    final int PERMISSION = 1;
    SpeechRecognizer mRecognizer;
    String reportCont = null;
    String reportWanted = null;
    public static String id, pw, name, date, city, dong, phone, rep_cate, rep_con, rep_date1, rep_date,rep_time1,rep_time, mem_id, rep_adr;
    public static String shared = "fersona";
    public static String mon_id = "00";
    public static String rep_pro = "????????????";
    public static String want_id = "want1";
    StringRequest request;
    String mon_char, url, monId1,monId2,monId3,monId4, monImg1,monImg2,monImg3,monImg4, wantImg1,wantImg2,wantImg3,wantImg4;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report, container, false);

        // viewFindViewById ???????????? ????????? ????????? ?????????????????????! ??? ?????? ????????????!??????
        viewFindViewById(view);

        noticeImg.setOnClickListener(this);
        userImg.setOnClickListener(this);
        step1Btn.setOnClickListener(this);
        step2Btn.setOnClickListener(this);
        step3Btn.setOnClickListener(this);
        step4Btn.setOnClickListener(this);
        step5Btn.setOnClickListener(this);
        voiceBtn.setOnClickListener(this);
        wantedviewBtn.setOnClickListener(this);
        infoViewBtn.setOnClickListener(this);
        submitBtn.setOnClickListener(this);

        // LoginActivity?????? ????????? ?????? ????????????
        loginContent();


        monMake1Img.setOnClickListener(this);
        monMake2Img.setOnClickListener(this);
        monMake3Img.setOnClickListener(this);
        monMake4Img.setOnClickListener(this);


        // RecyclerView
        for (int i = 0; i < 4; i++) {
            if (data != null) {
                addItemWanted("wantedimg", (i + 1));
                Log.d("FragmentReport", "item not null");
            } else {
                Log.d("FragmentReport", "item null");
            }
        }

        adapter = new MonRAdapter(data);
        wantedListRv.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), RecyclerView.HORIZONTAL, false));
        wantedListRv.setAdapter(adapter);

        // ??????????????? 6.0?????? ???????????? ???????????? ????????? ??????
        if (Build.VERSION.SDK_INT >= 23) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.INTERNET,
                    Manifest.permission.RECORD_AUDIO}, PERMISSION);
        }

        // RecognizerIntent ??????
        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getActivity().getPackageName()); // ????????? ???
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR"); // ?????? ??????

        // ???????????? ??????
        WantedCheck();

        // ???????????? ??????
        reportConEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                Log.d("FragmentReport", "???????????? ????????? ????????? ??????");
                rd2.setChecked(true);
                rd2.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.pointOrange)));
            }
        });

        ReportDate(); // ??????????????????
        ReportTime(); // ??????????????????
        // ?????????????????? ??????
        repAdrET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                rd5.setChecked(true);
                rd5.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.pointOrange)));
            }
        });

        if(requestQueue==null){
            requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        }

        submitBtn.setVisibility(View.GONE);

        return view;
    }

    // ????????? ????????? ?????? ????????? 4??? ?????? ??? wantImg ????????????
    public void selectMon4() {
        url = "http://121.147.52.96:5000/selectMon4";

        StringRequest request = new StringRequest(
                Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getActivity().getApplicationContext(), "????????? ?????? ??????????", Toast.LENGTH_SHORT).show();
                        try {
                            JSONArray array = new JSONArray(response);

                            monId1 = array.getJSONArray(0).getString(0);
                            monId2 = array.getJSONArray(1).getString(0);
                            monId3 = array.getJSONArray(2).getString(0);
                            monId4 = array.getJSONArray(3).getString(0);
                            monImg1 = array.getJSONArray(0).getString(5);
                            monImg2 = array.getJSONArray(1).getString(5);
                            monImg3 = array.getJSONArray(2).getString(5);
                            monImg4 = array.getJSONArray(3).getString(5);
                            wantImg1 = array.getJSONArray(0).getString(1);
                            wantImg2 = array.getJSONArray(0).getString(2);
                            wantImg3 = array.getJSONArray(0).getString(3);
                            wantImg4 = array.getJSONArray(0).getString(4);

                            Log.d("selectMon4", monId1 + " " + monId2 + " " + monId3 + " " + monId4);
                            Log.d("selectMon4", monImg1 + " " + monImg2 + " " + monImg3 + " " + monImg4);
                            Log.d("selectMon4", wantImg1 + " " + wantImg2 + " " + wantImg3 + " " + wantImg4);

                        } catch (JSONException e) {  e.printStackTrace(); }

                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(shared, Context.MODE_PRIVATE);    // test ????????? ???????????? ??????
                        SharedPreferences.Editor editor = sharedPreferences.edit(); //sharedPreferences??? ????????? editor??? ??????

                        editor.putString("monId1", monId1);
                        editor.putString("monId2", monId2);
                        editor.putString("monId3", monId3);
                        editor.putString("monId4", monId4);
                        editor.putString("monImg1", monImg1);
                        editor.putString("monImg2", monImg2);
                        editor.putString("monImg3", monImg3);
                        editor.putString("monImg4", monImg4);
                        editor.putString("wantId1", wantImg1);
                        editor.putString("wantId2", wantImg2);
                        editor.putString("wantId3", wantImg3);
                        editor.putString("wantId4", wantImg4);
                        editor.putString("mon_char", mon_char);

                        editor.commit();    //?????? ??????. ????????? ?????? ????????? ??????.

                        //monImg1 ?????? ?????????????????? ???????????? ??????????????? ??????
                        int monId1 = getResources().getIdentifier(monImg1, "drawable", getActivity().getPackageName());
                        int monId2 = getResources().getIdentifier(monImg2, "drawable", getActivity().getPackageName());
                        int monId3 = getResources().getIdentifier(monImg3, "drawable", getActivity().getPackageName());
                        int monId4 = getResources().getIdentifier(monImg4, "drawable", getActivity().getPackageName());
                        monMake1Img.setImageResource(monId1);
                        monMake2Img.setImageResource(monId2);
                        monMake3Img.setImageResource(monId3);
                        monMake4Img.setImageResource(monId4);

//                        monId1

                        int wanId1 = getResources().getIdentifier(wantImg1, "drawable", getActivity().getPackageName());
                        int wanId2 = getResources().getIdentifier(wantImg2, "drawable", getActivity().getPackageName());
                        int wanId3 = getResources().getIdentifier(wantImg3, "drawable", getActivity().getPackageName());
                        int wanId4 = getResources().getIdentifier(wantImg4, "drawable", getActivity().getPackageName());
//                        monMake1Img.setImageResource(monId1);
                        wantResultImg.setImageResource(R.drawable.wantedimg39);


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity().getApplicationContext(), "????????? ?????? ??????????", Toast.LENGTH_SHORT).show();
                    }
                }
        ){
            // ????????????????????? ?????? ?????????
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    String utf8String = new String(response.data, "UTF-8");
                    return Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    // log error
                    return Response.error(new ParseError(e));
                } catch (Exception e) {
                    // log error
                    return Response.error(new ParseError(e));
                }
            }

            // getParams ?????? ????????? ??????????????? : alt + insert
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // Map : dictionary, json??? ????????? key, value??? ???????????? ??????
                Map<String, String> params = new HashMap<>();
                params.put("mon_char", mon_char);
//                params.put("mem_pw", pw);

                return params;
            }
        };




        request.setShouldCache(false);
        requestQueue.add(request);
    }

    // LoginActivity?????? ????????? ?????? ????????????
    private void loginContent() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(shared, Context.MODE_PRIVATE);
        id = sharedPreferences.getString("id", "");
        pw = sharedPreferences.getString("pw", "");
        name = sharedPreferences.getString("name", "");
        date = sharedPreferences.getString("date", "");
        city = sharedPreferences.getString("city", "");
        dong = sharedPreferences.getString("dong", "");
        phone = sharedPreferences.getString("phone", "");
        nameTv.setText(name);
        phoneTv.setText(phone);
        mem_id = id;
    }


    public void addItemWanted(String imgName, int imgId) {
        int resId = getResources().getIdentifier(imgName + imgId, "drawable", getActivity().getPackageName());
        MonFaceListVO item = new MonFaceListVO("num", resId);
        if (item != null) {
            data.add(item);
        } else {
            Log.d("FragmentWanted", "item null");
        }
    }


    // ???????????? ??????
    private void WantedCheck() {
        wantedSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                Log.d("ReportActivity", "" + wantedSpin.getItemAtPosition(position));
                if (!wantedSpin.getItemAtPosition(position).toString().equals("??????")) {
                    rd1.setChecked(true);
                    rd1.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.pointOrange)));
                    reportWanted = (String) wantedSpin.getItemAtPosition(position);
                    rep_cate = reportWanted;
                    Log.d("ReportActivity", "???????????? ?????? = " + reportWanted);
                } else {
                    rd1.setChecked(false);
                    rd1.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.btnGray)));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    // ??????????????????
    private void ReportDate() {
        repDate.init(repDate.getYear(), repDate.getMonth(), repDate.getDayOfMonth(), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int month, int day) {
                // ?????? ??????????????? ????????? ?????? ?????? ????????????
                dateTv.setVisibility(View.VISIBLE);
                rd3.setChecked(true);
                rd3.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.pointOrange)));
                rep_date1 = String.format("%d/%d/%d", year, month + 1, day);
                dateTv.setText(rep_date1);

                String year1 = Integer.toString(year);
                String month1 = Integer.toString(month+1); // ????????? ???????????? ??????????????? ???!
                if(month1.length()==1){ month1 = "0"+month1; }
                String day1 = Integer.toString(day);
                if(day1.length()==1){ day1 = "0"+day1; }
                rep_date = year1 + month1 + day1;


            }
        });
    }

    // ??????????????????
    private void ReportTime() {
        repTime.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hour, int min) {
                timeTv.setVisibility(View.VISIBLE);
                rd4.setChecked(true);
                rd4.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.pointOrange)));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    hour = repTime.getHour();
                    min = repTime.getMinute();
                } else {
                    hour = repTime.getCurrentHour();
                    min = repTime.getCurrentMinute();
                }
                if (hour >= 12) {
                    rep_time1 = String.format("PM " + "%d : %d", hour, min);
                    rep_time = String.format("%d:%d", hour, min);
                } else {
                    rep_time1 =String.format("AM " + "%d : %d", hour, min);
                    rep_time = String.format("%d:%d", hour, min);
                }
                timeTv.setText(rep_time1);


            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.noticeImg:
                Log.d("FragmentReport","noticeImg");
                break;
            case R.id.userImg:
                Log.d("FragmentReport", "userImg");
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fl, new FragmentMypage()).commit();
                break;
            case R.id.voiceBtn:
                Log.d("FragmentReport", "????????????");
                mRecognizer = SpeechRecognizer.createSpeechRecognizer(getActivity().getApplicationContext()); // ??? SpeechRecognizer ??? ????????? ????????? ?????????
                mRecognizer.setRecognitionListener(listener); // ????????? ??????
                mRecognizer.startListening(intent); // ?????? ??????
                break;
            case R.id.step1Btn:
                Log.d("FragmentReport", "step1");
                step2Ll.setVisibility(View.GONE);
                step3Ll.setVisibility(View.GONE);
                step4Ll.setVisibility(View.GONE);
                step5Ll.setVisibility(View.GONE);
                if (step1Ll.getVisibility() == View.VISIBLE) {
                    step2Ll.setVisibility(View.GONE);
                    step3Ll.setVisibility(View.GONE);
                    step4Ll.setVisibility(View.GONE);
                    step5Ll.setVisibility(View.GONE);
                } else {
                    step1Ll.setVisibility(View.VISIBLE);
                    step5Btn.setVisibility(view.VISIBLE);
                }
                break;
            case R.id.step2Btn:
                Log.d("FragmentReport", "step2");
                mon_char = monMakeEt.getText().toString();

                // ????????? ????????? ?????? ????????? 4??? ?????? ??? wantImg ????????????
                selectMon4();

                Log.d("name", monId1 + " " + monId2 + " " + monId3 + " " + monId4);
                // ????????? 4??? ????????? ?????????
//                monMake1Img.setImageResource(R.drawable.montage1);
//                monMake2Img.setImageResource(R.drawable.montage2);
//                monMake3Img.setImageResource(R.drawable.montage3);
//                monMake4Img.setImageResource(R.drawable.montage4);

                Log.d("name", monImg1 +" ");
//
//                int resId1 = getResources().getIdentifier(monImg1, "drawable", getActivity().getPackageName());
//                int resId2 = getResources().getIdentifier(monImg2, "drawable", getActivity().getPackageName());
//                int resId3 = getResources().getIdentifier(monImg3, "drawable", getActivity().getPackageName());
//                int resId4 = getResources().getIdentifier(monImg4, "drawable", getActivity().getPackageName());
//                monMake1Img.setImageResource(resId1);
//                monMake2Img.setImageResource(resId2);
//                monMake3Img.setImageResource(resId3);
//                monMake4Img.setImageResource(resId4);

//                monMake1Img = "R.drawable." + monImg1;
//                monMake2Img.setImageResource();
//                monMake3Img.setImageResource();
//                monMake4Img.setImageResource();


                step1Ll.setVisibility(View.GONE);
                step3Ll.setVisibility(View.GONE);
                step4Ll.setVisibility(View.GONE);
                step5Ll.setVisibility(View.GONE);
                if (step2Ll.getVisibility() == View.VISIBLE) {
                    step1Ll.setVisibility(View.GONE);
                    step3Ll.setVisibility(View.GONE);
                    step4Ll.setVisibility(View.GONE);
                    step5Ll.setVisibility(View.GONE);
                } else {
                    step2Ll.setVisibility(View.VISIBLE);
                    step5Btn.setVisibility(view.VISIBLE);
                }
                // ????????? ????????? ??????
                montageClick();

                break;
            case R.id.step3Btn:
                Log.d("FragmentReport", "step3");
                step1Ll.setVisibility(View.GONE);
                step2Ll.setVisibility(View.GONE);
                step4Ll.setVisibility(View.GONE);
                step5Ll.setVisibility(View.GONE);
                if (step3Ll.getVisibility() == View.VISIBLE) {
                    step1Ll.setVisibility(View.GONE);
                    step2Ll.setVisibility(View.GONE);
                    step4Ll.setVisibility(View.GONE);
                    step5Ll.setVisibility(View.GONE);
                } else {
                    step3Ll.setVisibility(View.VISIBLE);
                    step5Btn.setVisibility(view.VISIBLE);
                }
                break;
            case R.id.step4Btn:
                Log.d("FragmentReport", "step4");
                step1Ll.setVisibility(View.GONE);
                step2Ll.setVisibility(View.GONE);
                step3Ll.setVisibility(View.GONE);
                step5Ll.setVisibility(View.GONE);
                if (step4Ll.getVisibility() == View.VISIBLE) {
                    step1Ll.setVisibility(View.GONE);
                    step2Ll.setVisibility(View.GONE);
                    step3Ll.setVisibility(View.GONE);
                    step5Ll.setVisibility(View.GONE);
                } else {
                    step4Ll.setVisibility(View.VISIBLE);
                    step5Btn.setVisibility(view.VISIBLE);
                }
                break;
            case R.id.step5Btn:
                Log.d("FragmentReport", "step5");
                step1Ll.setVisibility(View.GONE);
                step2Ll.setVisibility(View.GONE);
                step3Ll.setVisibility(View.GONE);
                step4Ll.setVisibility(View.GONE);
                if (step5Ll.getVisibility() == View.VISIBLE) {
                    step1Ll.setVisibility(View.GONE);
                    step2Ll.setVisibility(View.GONE);
                    step3Ll.setVisibility(View.GONE);
                    step4Ll.setVisibility(View.GONE);
                } else {
                    step5Ll.setVisibility(View.VISIBLE);
                    submitBtn.setVisibility(view.VISIBLE);
                    step5Btn.setVisibility(view.GONE);
                }

                // ????????????
                rep_con = reportConEt.getText().toString();
                reportGetTv.setText(rep_con);
                // ???????????? <- ???????????? ?????? ??????, ????????? ?????? ??????
                wantedcontentTv.setText(monMakeEt.getText().toString());
                // ???????????? ??????
                rep_adr = repAdrET.getText().toString();
                reportGetAdrTv.setText(rep_adr);
                // ?????????
                montageClick();

                break;
            case R.id.wantedviewBtn:
                Log.d("FragmentReport", "???????????? ????????????");
                Bundle shareargs = new Bundle();
                PopupDialog sharePopup = new PopupDialog("???????????? ????????????", R.string.share);
                sharePopup.setArguments(shareargs);
                sharePopup.show(getActivity().getSupportFragmentManager(), "????????????????????????");
                break;
            case R.id.infoViewBtn:
                Log.d("FragmentReport", "???????????? ????????????");
                Bundle infoargs = new Bundle();
                PopupDialog infoPopup = new PopupDialog("???????????? ????????????", R.string.info);
                infoPopup.setArguments(infoargs);
                infoPopup.show(getActivity().getSupportFragmentManager(), "???????????? ????????????");
                break;
            case R.id.submitBtn:
                reportCont = reportConEt.getText().toString();
                Log.d("ReportPage submitBtn", "" + reportWanted + " , " + reportCont + " , " + dateTv.getText() + " , " + timeTv.getText());
//                submitBtn.setBackgroundResource(R.color.subGray);

                // ????????????, ????????? ????????????, ???????????? ??????, ????????? ?????????, ???????????? ????????????, ???????????? ????????????
                if(reportGetTv.length()<5 || monMakeEt.length()<5 || repAdrET.length()<5 || infoCk.isChecked() == false || wantedCk.isChecked() == false){
                    //????????????
                    if (reportGetTv.length() < 5) {
                        rd2.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.red)));
                        Toast.makeText(getActivity().getApplicationContext(), "??????????????? ??????????????????????", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d("FragmentReport", "???????????? : " + reportCont);
                    }

                    // ????????? ????????????
                    if(monMakeEt.length()<5){
                        Toast.makeText(getActivity().getApplicationContext(), "Step1??? ??????????????? ????????? ?????????", Toast.LENGTH_SHORT).show();
                    }else{
                        Log.d("FragmentReport", "????????? ???????????? : " + monMakeEt);
                    }

                    // ???????????? ??????
                    if(repAdrET.length()<5 ){
                        Toast.makeText(getActivity().getApplicationContext(), "Step4??? ????????????????????? ??????????????????", Toast.LENGTH_SHORT).show();
                    }else{
                        Log.d("FragmentReport", "????????? ???????????? : " + monMakeEt);
                    }

                    //????????? ?????????

                    // ???????????? ??? ???????????? ?????? ?????? ??????
                    if (infoCk.isChecked() == true && wantedCk.isChecked() == true) {
                        Log.d("FragmentReport", "check success");
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), "??????????????? ?????? ??? ??????????????????", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    submitBtn.setBackgroundResource(R.color.pointOrange);

                    String url = "http://121.147.52.96:5000/report";

                    // ?????? ?????????
                    request = new StringRequest(
                            Request.Method.POST,
                            url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Toast.makeText(getActivity().getApplicationContext(), "???????????? ?????? ??????????", Toast.LENGTH_SHORT).show();
                                    Log.d("??????", response);
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getActivity().getApplicationContext(), "???????????? ?????? ??????????", Toast.LENGTH_SHORT).show();
                                    Log.d("??????", "??????");
                                }
                            }
                    ){ // getParams ?????? ????????? ??????????????? : alt + insert
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            Log.d("??????", rep_cate);
                            params.put("rep_cate", rep_cate);
                            params.put("rep_con", rep_con);
                            params.put("rep_date", rep_date);
                            params.put("rep_time", rep_time);
                            params.put("mem_id", mem_id);
                            params.put("rep_adr", rep_adr);
                            params.put("mon_id", mon_id);
                            params.put("want_id", want_id);
                            params.put("rep_pro", rep_pro);
                            return params;
                        }
                    };
                    Toast.makeText(getActivity().getApplicationContext(), "??????????", Toast.LENGTH_SHORT).show();

                    request.setShouldCache(false);
                    requestQueue.add(request);
//                    finish();


                }

                break;

//                ????????? ????????? ??????
            case R.id.monMake1Img:
                Log.d("????????? ?????????", "1??? Click " + monMake1Img.isClickable());
                Toast.makeText(getActivity().getApplicationContext(), "????????? 1?????? ?????????????????????.", Toast.LENGTH_SHORT).show();
                monMake1Img.setClickable(true);
                montageClick();
                break;
            case R.id.monMake2Img:
                Log.d("????????? ?????????", "2??? Click " + monMake2Img.isClickable());
                if(monMake2Img.isClickable()==false){
                    monMake2Img.setClickable(true);
                    Toast.makeText(getActivity().getApplicationContext(), "????????? 2?????? ?????????????????????.", Toast.LENGTH_SHORT).show();
                    montageClick();
                }
                break;
            case R.id.monMake3Img:
                Log.d("????????? ?????????", "3??? Click " + monMake3Img.isClickable());
                if(monMake3Img.isClickable()==false){
                    monMake3Img.setClickable(true);
                    Toast.makeText(getActivity().getApplicationContext(), "????????? 3?????? ?????????????????????.", Toast.LENGTH_SHORT).show();
                    montageClick();
                }
                break;
            case R.id.monMake4Img:
                Log.d("????????? ?????????", "4??? Click " + monMake4Img.isClickable());
                if(monMake4Img.isClickable()==false){
                    monMake4Img.setClickable(true);
                    Toast.makeText(getActivity().getApplicationContext(), "????????? 4?????? ?????????????????????.", Toast.LENGTH_SHORT).show();
                    montageClick();
                }
                break;

        }

    }

    private void montageClick() {
        Log.d("?????????", "montageClick ?????????");
        if (monMake1Img.isClickable() == true) {
            // ????????? 1?????? ????????? ????????????
            Log.d("????????? ?????????", "1Img " + monMake1Img.isClickable());
//                    mon_id = monMake1Img.toString();
            wantedImg.setImageResource(R.drawable.mon_516);
            monResultImg.setImageResource(R.drawable.mon_516);
            wantResultImg.setImageResource(R.drawable.wantedimg39);
        } else if (monMake2Img.isClickable() == true) {
            // ????????? 2?????? ????????? ????????????
            Log.d("????????? ?????????", "2Img" + monMake2Img.isClickable());
//                    mon_id = monMake2Img.toString();
            wantedImg.setImageResource(R.drawable.montage2);
            monResultImg.setImageResource(R.drawable.montage2);
        } else if (monMake3Img.isClickable() == true) {
            // ????????? 3?????? ????????? ????????????
            Log.d("????????? ?????????", "3Img" + monMake3Img.isClickable());
//                    mon_id = monMake3Img.toString();
            wantedImg.setImageResource(R.drawable.montage3);
            monResultImg.setImageResource(R.drawable.montage3);
        } else if (monMake4Img.isClickable() == true) {
            // ????????? 4?????? ????????? ????????????
            Log.d("????????? ?????????", "4Img" + monMake4Img.isClickable());
//                    mon_id = monMake4Img.toString();
            wantedImg.setImageResource(R.drawable.montage4);
            monResultImg.setImageResource(R.drawable.montage4);
        }
    }

    // ?????? ??????
    private RecognitionListener listener = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle params) {
            // ????????? ????????? ??????????????? ??????
//            Toast.makeText(getApplicationContext(), "???????????? ??????", Toast.LENGTH_SHORT).show();
            Toast.makeText(getActivity().getApplicationContext(), "???????????? ??????", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onBeginningOfSpeech() { // ????????? ???????????? ??? ??????
        }

        @Override
        public void onRmsChanged(float rmsdB) {
            // ???????????? ????????? ????????? ?????????
        }

        @Override
        public void onBufferReceived(byte[] buffer) {
            // ?????? ???????????? ????????? ??? ????????? buffer??? ??????
        }

        @Override
        public void onEndOfSpeech() {
            // ???????????? ???????????? ??????
        }

        @Override
        public void onError(int error) {
            // ???????????? ?????? ?????? ????????? ???????????? ??? ??????
            String message;

            switch (error) {
                case SpeechRecognizer.ERROR_AUDIO:
                    message = "????????? ??????";
                    break;
                case SpeechRecognizer.ERROR_CLIENT:
                    message = "??????????????? ??????";
                    break;
                case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                    message = "????????? ??????";
                    break;
                case SpeechRecognizer.ERROR_NETWORK:
                    message = "???????????? ??????";
                    break;
                case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                    message = "????????? ????????????";
                    break;
                case SpeechRecognizer.ERROR_NO_MATCH:
                    message = "?????? ??? ??????";
                    break;
                case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                    message = "RECOGNIZER ??? ??????";
                    break;
                case SpeechRecognizer.ERROR_SERVER:
                    message = "????????? ?????????";
                    break;
                case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                    message = "????????? ????????????";
                    break;
                default:
                    message = "??? ??? ?????? ?????????";
                    break;
            }

            Toast.makeText(getActivity().getApplicationContext(), "?????? ?????? : " + message, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onResults(Bundle results) {
            // ?????? ????????? ???????????? ??????
            // ?????? ?????? ArrayList??? ????????? ?????? textView??? ????????? ?????????
            ArrayList<String> matches =
                    results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

            for (int i = 0; i < matches.size(); i++) {
//                textView.setText(matches.get(i));
                monMakeEt.setText(matches.get(i));
            }
        }

        @Override
        public void onPartialResults(Bundle partialResults) {
            // ?????? ?????? ????????? ????????? ??? ?????? ??? ??????
        }

        @Override
        public void onEvent(int eventType, Bundle params) {
            // ?????? ???????????? ???????????? ?????? ??????
        }
    };

    // ????????? ?????? ??????
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //request????????? 0?????? ok??? ???????????? data??? ????????? ???????????????
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            Log.d("ReportPage", "uri:" + String.valueOf(uri));
            try {
                //Uri????????? Bitmap?????? ???????????? ImageView??? ?????? ?????????.
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                wantedImg.setImageBitmap(bitmap);
            } catch (IOException e) {
                Toast.makeText(getActivity().getApplicationContext(), "????????? ????????? ????????????????", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "?????? ???????????????.????", Toast.LENGTH_SHORT).show();
        }
    }

    // viewFindViewById ???????????? ????????? ????????? ?????????????????????!
    private void viewFindViewById(View view) {
        scrollView = view.findViewById(R.id.scrollView);
        wantedListRv = view.findViewById(R.id.wantedListRv); // ????????? 4??? ?????????

        mainLl = view.findViewById(R.id.mainLl);
        step1Ll = view.findViewById(R.id.step1Ll);
        step2Ll = view.findViewById(R.id.step2Ll);
        step3Ll = view.findViewById(R.id.step3Ll);
        step4Ll = view.findViewById(R.id.step4Ll);
        step5Ll = view.findViewById(R.id.step5Ll);

        monMakeEt = view.findViewById(R.id.monMakeEt); // ????????? ?????? ?????? ??????
        reportConEt = view.findViewById(R.id.reportConEt); // ???????????? ??????
        repAdrET = view.findViewById(R.id.repAdrET); // ?????????????????? ??????

        step1Btn = view.findViewById(R.id.step1Btn);
        step2Btn = view.findViewById(R.id.step2Btn);
        step3Btn = view.findViewById(R.id.step3Btn);
        step4Btn = view.findViewById(R.id.step4Btn);
        wantedviewBtn = view.findViewById(R.id.wantedviewBtn);
        infoViewBtn = view.findViewById(R.id.infoViewBtn);
        step5Btn = view.findViewById(R.id.step5Btn);
        submitBtn = view.findViewById(R.id.submitBtn);

        voiceBtn = view.findViewById(R.id.voiceBtn); // ???????????? ??????

        // ????????? 4??? ?????????
        monMake1Img = view.findViewById(R.id.monMake1Img);
        monMake2Img = view.findViewById(R.id.monMake2Img);
        monMake3Img = view.findViewById(R.id.monMake3Img);
        monMake4Img = view.findViewById(R.id.monMake4Img);

        wantedImg = view.findViewById(R.id.wantedImg); // step3 > ????????? ?????????
        monResultImg = view.findViewById(R.id.monResultImg);
        wantResultImg = view.findViewById(R.id.wantResultImg);
        userImg = view.findViewById(R.id.userImg);
        noticeImg = view.findViewById(R.id.noticeImg);

        rd1 = view.findViewById(R.id.rd1);
        rd2 = view.findViewById(R.id.rd2);
        rd3 = view.findViewById(R.id.rd3);
        rd4 = view.findViewById(R.id.rd4);
        rd5 = view.findViewById(R.id.rd5);
        rd6 = view.findViewById(R.id.rd6);
        rd7 = view.findViewById(R.id.rd7);
        rd8 = view.findViewById(R.id.rd8);
        rd9 = view.findViewById(R.id.rd9);

        wantedSpin = view.findViewById(R.id.wantedSpin); // ????????????

        dateTv = view.findViewById(R.id.dateTv);
        timeTv = view.findViewById(R.id.timeTv);
        nameTv = view.findViewById(R.id.nameTv);
        phoneTv = view.findViewById(R.id.phoneTv);
        wantedcontentTv = view.findViewById(R.id.wantedcontentTv);
        reportGetTv = view.findViewById(R.id.reportGetTv);
        reportGetAdrTv = view.findViewById(R.id.reportGetAdrTv);

        repDate = view.findViewById(R.id.repDate); // ??????????????????

        repTime = view.findViewById(R.id.repTime); // ??????????????????

        wantedCk = view.findViewById(R.id.wantedCk);
        infoCk = view.findViewById(R.id.infoCk);
    }

}