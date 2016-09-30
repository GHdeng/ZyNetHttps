package com.deng.netlibrary.netLib.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 邓鉴恒 on 16/9/14.
 */
public class RPCBaseModel<T> implements Parcelable {

    private String id;
    private RPCBaseResultModel<T> result;
    private RPCServerErrorModel error = null;
    private String jsonrpc;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public RPCBaseResultModel getResult() {
        return result;
    }

    public void setResult(RPCBaseResultModel result) {
        this.result = result;
    }

    public RPCServerErrorModel getError() {
        return error;
    }

    public void setError(RPCServerErrorModel error) {
        this.error = error;
    }

    public String getJsonrpc() {
        return jsonrpc;
    }

    public void setJsonrpc(String jsonrpc) {
        this.jsonrpc = jsonrpc;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeParcelable((Parcelable) this.result, flags);
        dest.writeParcelable((Parcelable) this.error, flags);
        dest.writeString(this.jsonrpc);
    }

    public RPCBaseModel() {
    }

    protected RPCBaseModel(Parcel in) {
        this.id = in.readString();
        String dataName = in.readString();
        try {
            this.result = in.readParcelable(Class.forName(dataName).getClassLoader());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        this.error = in.readParcelable(RPCServerErrorModel.class.getClassLoader());
        this.jsonrpc = in.readString();
    }

    public static final Creator<RPCBaseModel> CREATOR = new Creator<RPCBaseModel>() {
        @Override
        public RPCBaseModel createFromParcel(Parcel source) {
            return new RPCBaseModel(source);
        }

        @Override
        public RPCBaseModel[] newArray(int size) {
            return new RPCBaseModel[size];
        }
    };
}
