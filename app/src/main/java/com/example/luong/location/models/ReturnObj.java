package com.example.luong.location.models;

import java.io.Serializable;
import java.util.List;

public class ReturnObj<T> implements Serializable{
    public String ErrorCode = "0";
    public String Message;
    public T Data;
    public List<T> ListData;
    public boolean CheckHasData(){
        return Data != null;
    }
    public boolean CheckHasListData(){
        return ListData != null;
    }
}
