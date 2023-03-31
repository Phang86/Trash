package com.hnhy.trash.model;

import java.util.List;

public class PhotoResponse {

    private Integer code;
    private String msg;
    private List<DataBean> data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private String imageUrl;
        private String imageSize;
        private Integer imageFileLength;

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getImageSize() {
            return imageSize;
        }

        public void setImageSize(String imageSize) {
            this.imageSize = imageSize;
        }

        public Integer getImageFileLength() {
            return imageFileLength;
        }

        public void setImageFileLength(Integer imageFileLength) {
            this.imageFileLength = imageFileLength;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "imageUrl='" + imageUrl + '\'' +
                    ", imageSize='" + imageSize + '\'' +
                    ", imageFileLength=" + imageFileLength +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "PhotoResponse{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
