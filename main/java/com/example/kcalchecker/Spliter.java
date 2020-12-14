package com.example.kcalchecker;

public class Spliter {

    public String daySpliter(int title) {
        //일자 추출 스플리터
        String s = Integer.toString(title);
        String month = s.substring(4, 6);
        String day = s.substring(6);
        String result = month + "월 " + day + "일";
        return result;
    }

    public int noSpliter(String str) {
        //fNO 추출 스플리터
        String no = str.substring(3, 6);
        String nostr = no.replaceAll(" ", "");
        int result = Integer.parseInt(nostr);
        return result;
    }

}
