package com.hnhy.trash.api;

import com.hnhy.trash.model.GetDiscernResultResponse;
import com.hnhy.trash.model.GetTokenResponse;
import com.hnhy.trash.model.TrashNewsResponse;
import com.hnhy.trash.model.TrashResponse;
import com.hnhy.trash.model.WallPaperResponse;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

import static com.hnhy.trash.utils.Constant.KEY;

/**
 * API接口
 *
 * @author llw
 * @date 2021/3/30 15:13
 */
public interface ApiService {

    /**
     * 垃圾分类
     * @param word 物品名
     * @return TrashResponse 结果实体
     */
    @GET("/lajifenlei/index?key=" + KEY)
    Observable<TrashResponse> searchGoods(@Query("word") String word);

    /**
     * 获取鉴权认证Token
     * @param grant_type 类型
     * @param client_id API Key
     * @param client_secret Secret Key
     * @return GetTokenResponse
     */
    @FormUrlEncoded
    @POST("/oauth/2.0/token")
    Observable<GetTokenResponse> getToken(@Field("grant_type") String grant_type,
                                          @Field("client_id") String client_id,
                                          @Field("client_secret") String client_secret);

    /**
     * 获取图像识别结果
     * @param accessToken 获取鉴权认证Token
     * @param image 图片base64
     * @param url 网络图片Url
     * @return JsonObject
     */
    @FormUrlEncoded
    @POST("/rest/2.0/image-classify/v2/advanced_general")
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    Observable<GetDiscernResultResponse> getDiscernResult(@Field("access_token") String accessToken,
                                                          @Field("image") String image,
                                                          @Field("url") String url);

    //  /lajifenleinews/index?key=
    /**
     * 垃圾分类新闻
     * @param num 数量
     * @return TrashNewsResponse 结果实体
     */
    @GET("/lajifenleinews/index?key=" + KEY)
    Observable<TrashNewsResponse> getTrashNews(@Query("num") Integer num);

    /**
     * 手机壁纸API
     *
     * @return WallPaperResponse 网络壁纸数据返回
     */
    @GET("/v1/vertical/vertical?limit=30&skip=180&adult=false&first=0&order=hot")
    Observable<WallPaperResponse> getWallPaper();

}
