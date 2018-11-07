package com.example.slideview.viewpager;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.ErrorReason;
import com.google.android.youtube.player.YouTubePlayer.PlayerStateChangeListener;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.android.youtube.player.YouTubePlayer.PlaybackEventListener;

import static java.security.AccessController.getContext;


final class Tag
{
    public static final String _TAG="debug";
}

class Thr extends AsyncTask<Void, Void, Void> {

    private SubTitle st;  //SubTitle 클래스
    private ButtonClass bc;  // ButtonClass 클래스
    private SubActivity.YouTube mt;  // innerclass인 Youtube 클래스

    Thr(SubTitle st, ButtonClass bc, SubActivity.YouTube mt)
    {
        this.st = st;
        this.bc = bc;
        this.mt = mt;

    } // Thr 생성자


    @Override
    //perform an operation on a background thread
    protected Void doInBackground(Void... voids)
    {

        Log.v(Tag._TAG, "doInBackground");

        if (Looper.myLooper() == Looper.getMainLooper())
        {
            Log.v(Tag._TAG, "Main Thread running");

        }else{

            Log.v(Tag._TAG,"asyncTask Thread running");
        }

        while (true)
        {

            // 해당 시간(해쉬맵의 키)에 해당 가사가 나올 시간(유튜브 영상의 시간)이 되면
            if (st.hm.containsKey(mt.myYouTubePlayer.getCurrentTimeMillis() / 1000))
            {
                // 해당 시간에 맞는 가사 띄우기
                publishProgress();  // onProgressUpdate() 메소드 호출

            }// if 문

        }// while 문

    }

    @Override
    // 메인 메소드에게 UI 작업을 시킴 from publishProgress() in doInBackground()
    protected void onProgressUpdate(Void... values)
    {

        super.onProgressUpdate(values);
        bc.tv.setText(st.hm.get(mt.myYouTubePlayer.getCurrentTimeMillis() / 1000));

    }


} // Thr class

class ButtonClass {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private boolean flag=true;

    private Button phraseButton; // 해당 가사를 저장할 버튼
    private Button songButton;     // 재생할 영상(영상제목과 url)을 저장할 버튼

    public TextView tv;  // 가사를 띄울 TextView

    public String song;
    public String singer;
    int image;
    boolean flaglist;
    ButtonClass(final Activity myActivity, final SubActivity subActivity) {

        sharedPreferences = myActivity.getSharedPreferences("data",0);
        //데어터가 저장될 xml 이름 , 읽기 쓰기 가능 모드 0
        editor=sharedPreferences.edit(); // 에디터객체를 받아오기> 데이터저장하는 역할

        song="Maps";
        singer="Maroon5";
        image=R.drawable.maps_maroon5;

        phraseButton = myActivity.findViewById(R.id.button2);
        songButton = myActivity.findViewById(R.id.button3);
        tv = myActivity.findViewById(R.id.textView);

        phraseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editor.remove("song");
                editor.remove("singer");
                editor.remove("image");
                editor.commit();

                //mainy.playbackEventListener.onPaused();// 영상 멈추기

                tv.getText();// textView 에서 해당 가사 받아서 넘겨주기

                //Log.v("dd", tv.getText()+"");

                Toast.makeText(myActivity, "문장이 추가되었습니다.", Toast.LENGTH_LONG).show(); // 넘겨줬으면 확인 메세지 띄워주고

                //mainy.playbackEventListener.onPlaying(); // 영상 다시 시작


            }

        });

        songButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                flaglist=true;
                editor.putString("song",song);
                editor.putString("singer",singer);
                editor.putInt("image",image);
                editor.putBoolean("flaglist",flaglist);
                editor.commit();

                // 영상제목과 url 넘기기
                Toast.makeText(myActivity, "영상이 즐겨찾기에 추가되었습니다.", Toast.LENGTH_LONG).show();
                songButton.setBackgroundColor(Color.argb(0xff, 132, 132, 130)); // 버튼의 색깔 회색으로 바뀌고


                songButton.setClickable(false);  // 더 이상누를수없게

            }

        });


    } // ButtonClass 생성자


} // ButtonClass 클래스




class SubTitle {

    private Spinner sp;    // 스피너
    private String[] str; // 스피너 목록 문자열
    private ArrayAdapter<String> adapter; // 스피너 어댑터

    private String[] eng; // 영어가사 문자열 배열 리소스를 받을 문자열 배열
    private String[] kor; // 한국어가사 문자열 배열 리소스를 받을 문자열 배열
    private String[] sub;

    public HashMap<Integer, String> hm; // 가사가 나오는 시간(초)->key, 가사->value 를 담을 해쉬맵

    SubTitle(final Activity ac) {

        hm = new HashMap<>(); // 해쉬맵 생성

        eng = ac.getResources().getStringArray(R.array.map_eng); // 영어가사 문자열 배열 리소스 담기
        kor = ac.getResources().getStringArray(R.array.map_kor); // 한국어가사 문자열 배열 리소스 담기

        sp = ac.findViewById(R.id.spinner);
        str = new String[]{"영어", "한국어", "영어+한국어"}; // 스피너에 넣고 싶은 목록

        adapter = new ArrayAdapter<>(ac, android.R.layout.simple_spinner_item, str); // 스퍼너와 스피너 목록을 연결시켜 줄 어댑터
        sp.setAdapter(adapter); // 스피너에 만들어준 어댑터 채우기

        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {

                    case 0:
                        sub = eng;

                        break; // 영어 가사

                    case 1:
                        sub = kor;
                        break; // 한국어 가사

                    case 2:
                        break; // 영어와 한국어 같이

                    default:
                        break;

                } // switch

                hm.put(0, sub[0]);
                hm.put(4, sub[1]);
                hm.put(8, sub[2]);
                hm.put(12, sub[3]);
                hm.put(16, sub[4]);
                hm.put(20, sub[5]);
                hm.put(24, sub[6]);
                hm.put(28, sub[7]);
                hm.put(31, sub[8]);
                hm.put(35, sub[9]);
                hm.put(39, sub[10]);
                hm.put(42, sub[11]);
                hm.put(46, sub[12]);
                hm.put(47, sub[13]);
                hm.put(50, sub[14]);
                hm.put(53, sub[15]);
                hm.put(56, sub[16]);
                hm.put(59, sub[17]);
                hm.put(61, sub[18]);
                hm.put(62, sub[19]);
                hm.put(65, sub[20]);
                hm.put(66, sub[20]);
                hm.put(68, sub[20]);
                hm.put(70, sub[20]);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        }); // setOnItemSelected 함수 끝


    } // SubTitle constructor

}// SubTitle class


public class SubActivity extends YouTubeBaseActivity {

    private YouTube youTube = null;
    private ButtonClass bc = null;
    private SubTitle st = null;
    private Thr thr = null;

    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.subactivity);

        youTube = new YouTube(); // 유튜브 영상 클래스 ( innerClass 로 생성 )

        bc = new ButtonClass(this, this); // 레이아웃( 텍스트뷰와 버튼2개) 클래스
        st = new SubTitle(this); // 스피너, 자막 관리 클래스
        thr = new Thr(st, bc, youTube); // asynkTask


        //인텐트 처리
        Intent intent=new Intent(this.getIntent());

        button = findViewById(R.id.button4);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thr.cancel(true);
                finish();
            }
        });


    } // OnCreate 함수 끝


    // innerClass YouTube
    class YouTube implements YouTubePlayer.Provider {

        public static final String API_KEY = "AIzaSyCsaiAc4liJ9if0n1N_xAMUygpav0OB3YM ";
        public static final String VIDEO_ID = "aOJAWl12kII";    // 넣고자 하는 영상 주소

        public YouTubePlayerView youTubePlayerView;
        public YouTubePlayer myYouTubePlayer = null;

        boolean flag;

        public YouTube() {
            flag = false;

            youTubePlayerView = findViewById(R.id.youtube_player); // 영상 띄울 곳
            youTubePlayerView.initialize(API_KEY, new YouTubePlayer.OnInitializedListener() { // 영상 초기화
                @Override
                public void onInitializationSuccess(Provider provider, YouTubePlayer youTubePlayer, boolean b) {

                    youTubePlayer.setPlayerStateChangeListener(playerStateChangeListener);
                    youTubePlayer.setPlaybackEventListener(playbackEventListener);

                    if (!b) {

                        youTubePlayer.cueVideo(VIDEO_ID); // 넣고자 하는 영상 넣기
                    }

                    myYouTubePlayer = youTubePlayer; // YouTubePlayer interface 안에 우리가 쓰고자 하는 getCurrentTimeMills()가 있음, 메소드의
                    // 매개변수로 YouTubePlayer 인스턴스 생성


                }

                @Override
                public void onInitializationFailure(Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                    Log.d(Tag._TAG, "영상 초기화 실패");

                }

            });


        } // youTubePlayerView.initialize


        public PlaybackEventListener playbackEventListener = new PlaybackEventListener() {

            @Override
            public void onPlaying() {

                Log.v(Tag._TAG, "onPlaying");


            }

            @Override
            public void onPaused() {


                Log.v(Tag._TAG, "onPaused");

            }

            @Override
            public void onStopped() {

            }

            @Override
            public void onBuffering(boolean b) {

            }

            @Override
            public void onSeekTo(int i) {

            }


        }; // PlaybackEventListener interface


        private PlayerStateChangeListener playerStateChangeListener = new PlayerStateChangeListener() {

            @Override
            public void onLoading() {

            }

            @Override
            public void onLoaded(String s) {

            }

            @Override
            public void onAdStarted() {

            }

            @Override
            public void onVideoStarted() {

                if (!flag) {// execute 는 한번만 불러져야 한다

                    flag = true;

                    thr.execute();

                }

            }

            @Override
            public void onVideoEnded() {

                thr.cancel(true);
                //단점:AsyncTask를 execute한 Activity가 종료되었을 때 별도의 지시가 없다면 AsyncTask는 종료되지 않고 계속 돌아간다.
            }

            @Override
            public void onError(ErrorReason errorReason) {

            }

        }; //PlayerStateChangeListener interface


        @Override
        public void initialize(String s, YouTubePlayer.OnInitializedListener onInitializedListener) {

        }

    } // YouTube innerclass


} // MainActivity class
