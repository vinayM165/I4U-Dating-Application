package com.example.i4u_.Util;

public class Calculateuid {

    public String setOneToOneChat(String uid1, String uid2)
    {
        int ret = uid1.compareTo(uid2);
        if(ret > 0){
            return uid1+uid2;
        }
        else if(ret < 0){
            return uid2+uid1;
        }else
            return  "error";

    }
}
