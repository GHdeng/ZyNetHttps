package com.deng.netlibrary.netLib.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * rpc 请求格式model
 * Created by 邓鉴恒 on 2016/9/26.
 */

public class RPCRequestModel implements Parcelable {

    private String jsonrpc = "2.0";
    private String method;
    private Object params;
    private long id;

    public RPCRequestModel(String jsonrpc, String method, Object params, long id) {
        this.jsonrpc = jsonrpc;
        this.method = method;
        this.params = params;
        this.id = id;
    }

    public String getJsonrpc() {
        return jsonrpc;
    }

    public void setJsonrpc(String jsonrpc) {
        this.jsonrpc = jsonrpc;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Object getParams() {
        return params;
    }

    public void setParams(Object params) {
        this.params = params;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.jsonrpc);
        dest.writeString(this.method);
        dest.writeParcelable((Parcelable) this.params, flags);
        dest.writeLong(this.id);
    }

    public RPCRequestModel() {
    }

    protected RPCRequestModel(Parcel in) {
        this.jsonrpc = in.readString();
        this.method = in.readString();
        this.params = in.readParcelable(Object.class.getClassLoader());
        this.id = in.readLong();
    }

    public static final Creator<RPCRequestModel> CREATOR = new Creator<RPCRequestModel>() {
        @Override
        public RPCRequestModel createFromParcel(Parcel source) {
            return new RPCRequestModel(source);
        }

        @Override
        public RPCRequestModel[] newArray(int size) {
            return new RPCRequestModel[size];
        }
    };
}
