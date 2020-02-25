package com.delivery.main.util;


import org.slf4j.LoggerFactory;

public class Result<T> {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(Result.class);

    private static final long serialVersionUID = -1802122468331526708L;
    private int status;
    private String message;
    private T data ;

    public Result(){}

    public Result(int status) {
        this.status = status;
    }


    public Result(int status, String message){
        this.status = status;
        this.message = message;
    }

    public Result(int status,String message,T data){
        this.status = status;
        this.data = data;
        this.message = message;
    }
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

//    public void putData(String key, Object value) {
////        data.put(key, value);
////    }
////
////    public void removeData(String key) {
////        data.remove(key);
////    }

    @Override
    public String toString() {
        return "Result{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}

