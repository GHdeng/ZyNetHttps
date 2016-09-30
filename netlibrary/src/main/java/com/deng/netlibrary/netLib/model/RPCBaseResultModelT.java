package com.deng.netlibrary.netLib.model;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * 带上泛型 <T> T  的model
 * Created by 邓鉴恒 on 16/9/14.
 */
public class RPCBaseResultModelT<T> implements Serializable {

    private BaseResultModel2<T> result;

    public BaseResultModel2<T> getResult() {
        return result;
    }

    public void setResult(BaseResultModel2<T> result) {
        this.result = result;
    }

    public class BaseResultModel2<T> {
        private Property Property;
        private Error Error;
        private T Obj;
        private LinkedList<T> List;

        public T getObj() {
            return Obj;
        }

        public void setObj(T obj) {
            Obj = obj;
        }

        public LinkedList<T> getList() {
            return List;
        }

        public void setList(LinkedList<T> list) {
            List = list;
        }

        public BaseResultModel2.Property getProperty() {
            return Property;
        }

        public void setProperty(BaseResultModel2.Property property) {
            Property = property;
        }

        public BaseResultModel2.Error getError() {
            return Error;
        }

        public void setError(BaseResultModel2.Error error) {
            Error = error;
        }

        public class Property {
            private String ObjName;
            private boolean IsNext;
            private int TotalSize;
            private int TotalIndex;
            private int PageIndex;
            private int PageSize;

            public String getObjName() {
                return ObjName;
            }

            public void setObjName(String objName) {
                ObjName = objName;
            }

            public boolean isNext() {
                return IsNext;
            }

            public void setIsNext(boolean isNext) {
                IsNext = isNext;
            }

            public int getTotalSize() {
                return TotalSize;
            }

            public void setTotalSize(int totalSize) {
                TotalSize = totalSize;
            }

            public int getTotalIndex() {
                return TotalIndex;
            }

            public void setTotalIndex(int totalIndex) {
                TotalIndex = totalIndex;
            }

            public int getPageIndex() {
                return PageIndex;
            }

            public void setPageIndex(int pageIndex) {
                PageIndex = pageIndex;
            }

            public int getPageSize() {
                return PageSize;
            }

            public void setPageSize(int pageSize) {
                PageSize = pageSize;
            }
        }

        public class Error {
            private String SubMsg;
            private int SubCode;

            public String getSubMsg() {
                return SubMsg;
            }

            public void setSubMsg(String subMsg) {
                SubMsg = subMsg;
            }

            public int getSubCode() {
                return SubCode;
            }

            public void setSubCode(int subCode) {
                SubCode = subCode;
            }
        }
    }

}
