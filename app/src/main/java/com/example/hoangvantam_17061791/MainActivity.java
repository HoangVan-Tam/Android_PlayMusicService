package com.example.hoangvantam_17061791;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private MyService myService;
    private boolean isBound = false;
    private ServiceConnection connection;
    private MediaPlayer mediaPlayer;
    private SeekBar playerSeekBar;
    private TextView textCurrentTime;
    int temp=0;
    Handler handler;
    Runnable runnable;
    private MyPlayer MyPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        SeekBar playerSeekBar=(SeekBar) findViewById(R.id.playerSeekBar);
        TextView textCurrentTime= (TextView) findViewById(R.id.textCurrentTime);
        final Button btOn = (Button) findViewById(R.id.btOn);
      //  final ImageView btStop = (ImageView) findViewById(R.id.btStop);
        final Button btOff = (Button) findViewById(R.id.btOff);
        final ImageView btFast = (ImageView) findViewById(R.id.btTua);


        playerSeekBar.setMax(100);
        // Khởi tạo ServiceConnection
        connection = new ServiceConnection() {

            // Phương thức này được hệ thống gọi khi kết nối tới service bị lỗi
            @Override
            public void onServiceDisconnected(ComponentName name) {

                isBound = false;
            }

            // Phương thức này được hệ thống gọi khi kết nối tới service thành công
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                MyService.MyBinder binder = (MyService.MyBinder) service;
                myService = binder.getService(); // lấy đối tượng MyService
                isBound = true;
            }
        };

        // Khởi tạo intent
        final Intent intent =
                new Intent(MainActivity.this,
                        MyService.class);




        btOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Bắt đầu một service sủ dụng bind
                bindService(intent, connection,
                        Context.BIND_AUTO_CREATE);
                // Đối thứ ba báo rằng Service sẽ tự động khởi tạo
            }
        });

        btOff.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Nếu Service đang hoạt động
                if (isBound) {
                    // Tắt Service
                    unbindService(connection);
                    isBound = false;
                }
            }
        });


 /*       findViewById(R.id.btStop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isBound) {
                    ImageView btPause= (ImageView)findViewById(R.id.btPause);
                    myService.stop();
                    btPause.setImageResource(R.drawable.ic_on);
                    temp=1;
                } else {
                    Toast.makeText(MainActivity.this,
                            "Service chưa hoạt động", Toast.LENGTH_SHORT).show();
                }
            }
        });*/

        findViewById(R.id.btPause).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageView btPause= (ImageView)findViewById(R.id.btPause);
                if (isBound) {
                    if(temp==0){
                        myService.Pause();
                        temp=1;
                        btPause.setImageResource(R.drawable.ic_on);
                    }
                    else{
                        myService.fastStart();
                        temp=0;
                        btPause.setImageResource(R.drawable.ic_pause);
                    }
                } else {
                    Toast.makeText(MainActivity.this,
                            "Service chưa hoạt động", Toast.LENGTH_SHORT).show();
                }
            }
        });


        btFast.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // nếu service đang hoạt động
                if (isBound) {
                    // tua bài hát
                    myService.fastForward();
                } else {
                    Toast.makeText(MainActivity.this,
                            "Service chưa hoạt động", Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.btTuanguoc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isBound) {
                    // tua bài hát
                    myService.rewind();
                } else {
                    Toast.makeText(MainActivity.this,
                            "Service chưa hoạt động", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private Runnable update=new Runnable() {
        @Override
        public void run() {
            updateSeekBar();
            String currentDuration="123";
            textCurrentTime=(TextView) findViewById(R.id.textCurrentTime);
            textCurrentTime.setText(currentDuration);
          //  textCurrentTime.setText(milisecondToTimer(currentDuration));
            //Toast.makeText(MainActivity.this,
              //      milisecondToTimer(currentDuration), Toast.LENGTH_SHORT).show();
        }
    };

    private  void  updateSeekBar(){
        if(mediaPlayer.isPlaying()){
            playerSeekBar.setProgress((int) (((float) mediaPlayer.getCurrentPosition() / mediaPlayer.getDuration()) *100 ));
        }
    }

    private String milisecondToTimer(long miliseconds){
        String timerString="";
        String secondString;

        int hour=(int) (miliseconds/(1000*60*60));
        int minutes=(int) (miliseconds %(1000*60*60))/(1000*60);
        int second=(int) ((miliseconds %(1000*60*60))% (1000*60)/1000);
        if(hour>0){
            timerString=hour+":";
        }
        if(second<10){
            secondString="0"+second;
        }
        else {
            secondString=""+second;
        }
        timerString=timerString+minutes+":"+secondString;
        return timerString;
    }
}

