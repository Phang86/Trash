package com.hnhy.trash.model;

import java.util.List;

/**
 * 垃圾分类返回数据
 */
public class TrashResponse {

    private int code;
    private String msg;
    private ResultBean result;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        private List<ListBean> list;

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            private String name;
            private int type;
            private int aipre;
            private String explain;
            private String contain;
            private String tip;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public int getAipre() {
                return aipre;
            }

            public void setAipre(int aipre) {
                this.aipre = aipre;
            }

            public String getExplain() {
                return explain;
            }

            public void setExplain(String explain) {
                this.explain = explain;
            }

            public String getContain() {
                return contain;
            }

            public void setContain(String contain) {
                this.contain = contain;
            }

            public String getTip() {
                return tip;
            }

            public void setTip(String tip) {
                this.tip = tip;
            }

            @Override
            public String toString() {
                return "ListBean{" +
                        "name='" + name + '\'' +
                        ", type=" + type +
                        ", aipre=" + aipre +
                        ", explain='" + explain + '\'' +
                        ", contain='" + contain + '\'' +
                        ", tip='" + tip + '\'' +
                        '}';
            }
        }
    }

    @Override
    public String toString() {
        return "TrashResponse{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", result=" + result +
                '}';
    }
}
