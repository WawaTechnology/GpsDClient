package com.example.unsan.gpsdclient;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Unsan on 7/5/18.
 */

public class CustomerEngChinese {
String chinese;

    public CustomerEngChinese() {
    }

    String english;

    public String getChinese() {
        return chinese;
    }

    public CustomerEngChinese(String chinese, String english) {
        this.chinese = chinese;
        this.english = english;
    }

    public void setChinese(String chinese) {
        this.chinese = chinese;

    }

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }
}
