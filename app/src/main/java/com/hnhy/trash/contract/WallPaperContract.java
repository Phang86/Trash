package com.hnhy.trash.contract;

import android.annotation.SuppressLint;

import com.hnhy.trash.api.ApiService;
import com.hnhy.trash.model.TrashResponse;
import com.hnhy.trash.model.WallPaperResponse;
import com.llw.mvplibrary.base.BasePresenter;
import com.llw.mvplibrary.base.BaseView;
import com.llw.mvplibrary.network.NetworkApi;
import com.llw.mvplibrary.network.observer.BaseObserver;

import retrofit2.Response;

public class WallPaperContract {
    public static class WallPaperPresenter extends BasePresenter<WallPaperView> {
        @SuppressLint("CheckResult")
        public void getWallPaper() {
            ApiService service = NetworkApi.createService(ApiService.class,2);
            service.getWallPaper().compose(NetworkApi.applySchedulers(new BaseObserver<WallPaperResponse>() {
                @Override
                public void onSuccess(WallPaperResponse wallPaperResponse) {
                    if (getView() != null){
                        getView().getWallPaperResult(wallPaperResponse);
                    }
                }

                @Override
                public void onFailure(Throwable e) {
                    if (getView() != null){
                        getView().getDataFailed(e.getMessage());
                    }
                }
            }));
        }
    }

    public interface WallPaperView extends BaseView {
//        /**
//         * 获取必应每日一图返回
//         * @param response BiYingImgResponse
//         */
//        void getBiYingResult(Response<BiYingImgResponse> response);

        /**
         * 壁纸数据返回
         * @param response WallPaperResponse
         */
        void getWallPaperResult(WallPaperResponse response);

        /**
         * 错误返回
         */
        void getDataFailed(String e);
    }
}
