package com.ravendmaster.linearmqttdashboard.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2017/9/26.
 */

public class FaceCompareJson {


    /**
     * message : OK
     * query_meta : null
     * results : [{"annotation":0,"born_year":1994,"face_image_id":3659174697238534,"face_image_id_str":"3659174697238534","face_image_uri":"normal://13/20170906/ykad2tKVUIg+cdfYcWeILQ==@1","gender":0,"is_writable":true,"name":"王硕","nation":0,"permission_map":{"0":2,"1":2,"101":2,"102":2,"400":2,"452":2,"501":2,"502":2,"503":2,"504":2,"505":2,"553":2},"person_id":"123","picture_uri":"normal://13/20170906/iLikEtOYvjQ1l7uQVDhqUA==@1","repository_id":13,"similarity":90.31517221764602,"timestamp":1504677997}]
     * retrieval_query_id : 1495
     * rtn : 0
     * total : 1
     */

    private String message;
    private Object query_meta;
    private int retrieval_query_id;
    private int rtn;
    private int total;
    private List<ResultsBean> results;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getQuery_meta() {
        return query_meta;
    }

    public void setQuery_meta(Object query_meta) {
        this.query_meta = query_meta;
    }

    public int getRetrieval_query_id() {
        return retrieval_query_id;
    }

    public void setRetrieval_query_id(int retrieval_query_id) {
        this.retrieval_query_id = retrieval_query_id;
    }

    public int getRtn() {
        return rtn;
    }

    public void setRtn(int rtn) {
        this.rtn = rtn;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<ResultsBean> getResults() {
        return results;
    }

    public void setResults(List<ResultsBean> results) {
        this.results = results;
    }

    public static class ResultsBean {
        /**
         * annotation : 0
         * born_year : 1994
         * face_image_id : 3659174697238534
         * face_image_id_str : 3659174697238534
         * face_image_uri : normal://13/20170906/ykad2tKVUIg+cdfYcWeILQ==@1
         * gender : 0 // 性别  0 男 ； 1 女
         * is_writable : true
         * name : 王硕 // 姓名
         * nation : 0
         * permission_map : {"0":2,"1":2,"101":2,"102":2,"400":2,"452":2,"501":2,"502":2,"503":2,"504":2,"505":2,"553":2}
         * person_id : 123  // 身份证号
         * picture_uri : normal://13/20170906/iLikEtOYvjQ1l7uQVDhqUA==@1
         * repository_id : 13
         * similarity : 90.31517221764602
         * timestamp : 1504677997
         */

        private int annotation;
        private int born_year;
        private long face_image_id;
        private String face_image_id_str;
        private String face_image_uri;
        private int gender;
        private boolean is_writable;
        private String name;
        private int nation;
        private PermissionMapBean permission_map;
        private String person_id;
        private String picture_uri;
        private int repository_id;
        private double similarity;
        private int timestamp;

        public int getAnnotation() {
            return annotation;
        }

        public void setAnnotation(int annotation) {
            this.annotation = annotation;
        }

        public int getBorn_year() {
            return born_year;
        }

        public void setBorn_year(int born_year) {
            this.born_year = born_year;
        }

        public long getFace_image_id() {
            return face_image_id;
        }

        public void setFace_image_id(long face_image_id) {
            this.face_image_id = face_image_id;
        }

        public String getFace_image_id_str() {
            return face_image_id_str;
        }

        public void setFace_image_id_str(String face_image_id_str) {
            this.face_image_id_str = face_image_id_str;
        }

        public String getFace_image_uri() {
            return face_image_uri;
        }

        public void setFace_image_uri(String face_image_uri) {
            this.face_image_uri = face_image_uri;
        }

        public int getGender() {
            return gender;
        }

        public void setGender(int gender) {
            this.gender = gender;
        }

        public boolean isIs_writable() {
            return is_writable;
        }

        public void setIs_writable(boolean is_writable) {
            this.is_writable = is_writable;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getNation() {
            return nation;
        }

        public void setNation(int nation) {
            this.nation = nation;
        }

        public PermissionMapBean getPermission_map() {
            return permission_map;
        }

        public void setPermission_map(PermissionMapBean permission_map) {
            this.permission_map = permission_map;
        }

        public String getPerson_id() {
            return person_id;
        }

        public void setPerson_id(String person_id) {
            this.person_id = person_id;
        }

        public String getPicture_uri() {
            return picture_uri;
        }

        public void setPicture_uri(String picture_uri) {
            this.picture_uri = picture_uri;
        }

        public int getRepository_id() {
            return repository_id;
        }

        public void setRepository_id(int repository_id) {
            this.repository_id = repository_id;
        }

        public double getSimilarity() {
            return similarity;
        }

        public void setSimilarity(double similarity) {
            this.similarity = similarity;
        }

        public int getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(int timestamp) {
            this.timestamp = timestamp;
        }

        public static class PermissionMapBean {
            /**
             * 0 : 2
             * 1 : 2
             * 101 : 2
             * 102 : 2
             * 400 : 2
             * 452 : 2
             * 501 : 2
             * 502 : 2
             * 503 : 2
             * 504 : 2
             * 505 : 2
             * 553 : 2
             */

            @SerializedName("0")
            private int _$0;
            @SerializedName("1")
            private int _$1;
            @SerializedName("101")
            private int _$101;
            @SerializedName("102")
            private int _$102;
            @SerializedName("400")
            private int _$400;
            @SerializedName("452")
            private int _$452;
            @SerializedName("501")
            private int _$501;
            @SerializedName("502")
            private int _$502;
            @SerializedName("503")
            private int _$503;
            @SerializedName("504")
            private int _$504;
            @SerializedName("505")
            private int _$505;
            @SerializedName("553")
            private int _$553;

            public int get_$0() {
                return _$0;
            }

            public void set_$0(int _$0) {
                this._$0 = _$0;
            }

            public int get_$1() {
                return _$1;
            }

            public void set_$1(int _$1) {
                this._$1 = _$1;
            }

            public int get_$101() {
                return _$101;
            }

            public void set_$101(int _$101) {
                this._$101 = _$101;
            }

            public int get_$102() {
                return _$102;
            }

            public void set_$102(int _$102) {
                this._$102 = _$102;
            }

            public int get_$400() {
                return _$400;
            }

            public void set_$400(int _$400) {
                this._$400 = _$400;
            }

            public int get_$452() {
                return _$452;
            }

            public void set_$452(int _$452) {
                this._$452 = _$452;
            }

            public int get_$501() {
                return _$501;
            }

            public void set_$501(int _$501) {
                this._$501 = _$501;
            }

            public int get_$502() {
                return _$502;
            }

            public void set_$502(int _$502) {
                this._$502 = _$502;
            }

            public int get_$503() {
                return _$503;
            }

            public void set_$503(int _$503) {
                this._$503 = _$503;
            }

            public int get_$504() {
                return _$504;
            }

            public void set_$504(int _$504) {
                this._$504 = _$504;
            }

            public int get_$505() {
                return _$505;
            }

            public void set_$505(int _$505) {
                this._$505 = _$505;
            }

            public int get_$553() {
                return _$553;
            }

            public void set_$553(int _$553) {
                this._$553 = _$553;
            }
        }
    }
}
