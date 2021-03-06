package com.example.luong.location.entities;

import java.io.Serializable;
import java.util.List;

public class ReturnObj<T> implements Serializable{
    public String ErrorCode = "0";
    public String ErrorMessage;
    public T Data;
    public List<T> ListData;
    public boolean hasData(){
        return Data != null;
    }
    public boolean hasListData(){
        return ListData != null;
    }
}
