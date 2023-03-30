package com.hnhy.trash.model;

import java.util.List;

public class WallPaperResponse {

    private String msg;
    private ResBean res;
    private Integer code;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ResBean getRes() {
        return res;
    }

    public void setRes(ResBean res) {
        this.res = res;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public static class ResBean {
        private List<VerticalBean> vertical;

        public List<VerticalBean> getVertical() {
            return vertical;
        }

        public void setVertical(List<VerticalBean> vertical) {
            this.vertical = vertical;
        }

        @Override
        public String toString() {
            return "ResBean{" +
                    "vertical=" + vertical +
                    '}';
        }

        public static class VerticalBean {
            private String preview;
            private String thumb;
            private String img;
            private Integer views;
            private List<String> cid;
            private String rule;
            private Integer ncos;
            private Integer rank;
            private String source_type;
            private List<?> tag;
            private List<?> url;
            private String wp;
            private Boolean xr;
            private Boolean cr;
            private Integer favs;
            private Double atime;
            private String id;
            private String store;
            private String desc;

            public String getPreview() {
                return preview;
            }

            public void setPreview(String preview) {
                this.preview = preview;
            }

            public String getThumb() {
                return thumb;
            }

            public void setThumb(String thumb) {
                this.thumb = thumb;
            }

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }

            public Integer getViews() {
                return views;
            }

            public void setViews(Integer views) {
                this.views = views;
            }

            public List<String> getCid() {
                return cid;
            }

            public void setCid(List<String> cid) {
                this.cid = cid;
            }

            public String getRule() {
                return rule;
            }

            public void setRule(String rule) {
                this.rule = rule;
            }

            public Integer getNcos() {
                return ncos;
            }

            public void setNcos(Integer ncos) {
                this.ncos = ncos;
            }

            public Integer getRank() {
                return rank;
            }

            public void setRank(Integer rank) {
                this.rank = rank;
            }

            public String getSource_type() {
                return source_type;
            }

            public void setSource_type(String source_type) {
                this.source_type = source_type;
            }

            public List<?> getTag() {
                return tag;
            }

            public void setTag(List<?> tag) {
                this.tag = tag;
            }

            public List<?> getUrl() {
                return url;
            }

            public void setUrl(List<?> url) {
                this.url = url;
            }

            public String getWp() {
                return wp;
            }

            public void setWp(String wp) {
                this.wp = wp;
            }

            public Boolean getXr() {
                return xr;
            }

            public void setXr(Boolean xr) {
                this.xr = xr;
            }

            public Boolean getCr() {
                return cr;
            }

            public void setCr(Boolean cr) {
                this.cr = cr;
            }

            public Integer getFavs() {
                return favs;
            }

            public void setFavs(Integer favs) {
                this.favs = favs;
            }

            public Double getAtime() {
                return atime;
            }

            public void setAtime(Double atime) {
                this.atime = atime;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getStore() {
                return store;
            }

            public void setStore(String store) {
                this.store = store;
            }

            public String getDesc() {
                return desc;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }

            @Override
            public String toString() {
                return "VerticalBean{" +
                        "preview='" + preview + '\'' +
                        ", thumb='" + thumb + '\'' +
                        ", img='" + img + '\'' +
                        ", views=" + views +
                        ", cid=" + cid +
                        ", rule='" + rule + '\'' +
                        ", ncos=" + ncos +
                        ", rank=" + rank +
                        ", source_type='" + source_type + '\'' +
                        ", tag=" + tag +
                        ", url=" + url +
                        ", wp='" + wp + '\'' +
                        ", xr=" + xr +
                        ", cr=" + cr +
                        ", favs=" + favs +
                        ", atime=" + atime +
                        ", id='" + id + '\'' +
                        ", store='" + store + '\'' +
                        ", desc='" + desc + '\'' +
                        '}';
            }
        }
    }

    @Override
    public String toString() {
        return "WallPaperResponse{" +
                "msg='" + msg + '\'' +
                ", res=" + res +
                ", code=" + code +
                '}';
    }
}
