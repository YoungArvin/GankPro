package com.freedom.lauzy.gankpro.function.net;

import com.freedom.lauzy.gankpro.function.entity.GankData;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

import static com.freedom.lauzy.gankpro.function.net.NetConstants.*;

/**
 * Api
 * Created by Lauzy on 2017/1/18.
 */

public interface ApiService {

    @GET(GANK_URL + "{type}/" + DefaultSize + "/{page}")
    Observable<GankData> getGankData(@Path("type") String type, @Path("page") int page);
}