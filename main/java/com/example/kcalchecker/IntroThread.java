package com.example.kcalchecker;

import android.os.Message;
import android.os.Handler;


public class IntroThread extends Thread{
    private Handler handler;

    public IntroThread(Handler handler){
        this.handler = handler;
    }
    @Override
    public void run() {
        //전달할 메세지 객체
        Message msg = new Message();
        try {//3초뒤에 화면 전환
            Thread.sleep(3000);
            msg.what = 1;//메세지 1을 전달
            handler.sendEmptyMessage(msg.what);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
