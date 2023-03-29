package com.hnhy.trash.contract;

import static com.hnhy.trash.utils.Constant.API_KEY;
import static com.hnhy.trash.utils.Constant.API_SECRET;
import static com.hnhy.trash.utils.Constant.GRANT_TYPE;

import android.annotation.SuppressLint;

import com.hnhy.trash.api.ApiService;
import com.hnhy.trash.model.GetDiscernResultResponse;
import com.hnhy.trash.model.GetTokenResponse;
import com.hnhy.trash.model.TrashResponse;
import com.llw.mvplibrary.base.BasePresenter;
import com.llw.mvplibrary.base.BaseView;
import com.llw.mvplibrary.network.NetworkApi;
import com.llw.mvplibrary.network.observer.BaseObserver;

public class ImageContract {

    public static class ImagePresenter extends BasePresenter<ImageView>{

        /**
         * 获取鉴权Token
         */
        @SuppressLint("CheckResult")
        public void getToken() {
            ApiService service = NetworkApi.createService(ApiService.class, 1);
            service.getToken(GRANT_TYPE, API_KEY, API_SECRET)
                    .compose(NetworkApi.applySchedulers(new BaseObserver<GetTokenResponse>() {
                        @Override
                        public void onSuccess(GetTokenResponse getTokenResponse) {
                            if (getView() != null) {
                                getView().getTokenResponse(getTokenResponse);
                            }
                        }

                        @Override
                        public void onFailure(Throwable e) {
                            if (getView() != null) {
                                getView().getTokenFailed(e);
                            }
                        }
                    }));
        }


        /**
         * 获取图像识别结果
         *
         * @param token 鉴权Token
         * @param image 图片base64
         * @param url   网络图片url
         */
        @SuppressLint("CheckResult")
        public void getDiscernResult(String token, String image, String url) {
            ApiService service = NetworkApi.createService(ApiService.class, 1);
            service.getDiscernResult(token, image, url)
                    .compose(NetworkApi.applySchedulers(new BaseObserver<GetDiscernResultResponse>() {
                        @Override
                        public void onSuccess(GetDiscernResultResponse getTokenResponse) {
                            if (getView() != null) {
                                getView().getDiscernResultResponse(getTokenResponse);
                            }
                        }

                        @Override
                        public void onFailure(Throwable e) {
                            if (getView() != null) {
                                getView().getDiscernResultFailed(e);
                            }
                        }
                    }));
        }


        /**
         * 搜索物品
         *
         * @param word 物品名
         */
        @SuppressLint("CheckResult")
        public void searchGoods(String word) {
            ApiService service = NetworkApi.createService(ApiService.class, 0);
            service.searchGoods(word).compose(NetworkApi.applySchedulers(new BaseObserver<TrashResponse>() {
                @Override
                public void onSuccess(TrashResponse groupResponse) {
                    if (getView() != null) {
                        getView().getSearchResponse(groupResponse);
                    }
                }

                @Override
                public void onFailure(Throwable e) {
                    if (getView() != null) {
                        getView().getSearchResponseFailed(e);
                    }
                }
            }));
        }
    }

    public interface ImageView extends BaseView{
        /**
         * 获取鉴权Token
         *
         * @param response GetTokenResponse
         */
        void getTokenResponse(GetTokenResponse response);

        /**
         * 获取鉴权Token异常返回
         *
         * @param throwable 异常
         */
        void getTokenFailed(Throwable throwable);

        /**
         * 获取图像识别结果
         *
         * @param response GetDiscernResultResponse
         */
        void getDiscernResultResponse(GetDiscernResultResponse response);

        /**
         * 获取图像识别结果失败
         *
         * @param throwable 异常
         */
        void getDiscernResultFailed(Throwable throwable);

        /**
         * 搜索物品返回
         *
         * @param response TrashResponse
         */
        void getSearchResponse(TrashResponse response);

        /**
         * 搜索物品异常返回
         *
         * @param throwable 异常
         */
        void getSearchResponseFailed(Throwable throwable);
    }
}
