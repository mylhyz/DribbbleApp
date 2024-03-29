/*
 * Copyright (c) 2016 lhyz Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.lhyz.android.dribbble.data;

import java.util.List;

import io.lhyz.android.dribbble.data.bean.Comment;
import io.lhyz.android.dribbble.data.bean.Like;
import io.lhyz.android.dribbble.data.bean.Shot;
import io.lhyz.android.dribbble.data.bean.User;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * hello,android
 * Created by lhyz on 2016/8/7.
 */
public interface DribbbleService {

    /**
     * User info
     *
     * @return User
     */
    @GET("user")
    Observable<User> getUser();

    /**
     * Shot接口
     */
    //默认是popular
    @GET("user/shots")
    Observable<List<Shot>> getPopularList();

    //参数是sort=recent
    @GET("shots?sort=recent")
    Observable<List<Shot>> getRecentList();

    //参数list=debuts
    @GET("shots?list=debuts")
    Observable<List<Shot>> getDebutList();

    //参数是sort=recent
    @GET("shots?list=teams")
    Observable<List<Shot>> getTeamList();

    //参数是sort=recent
    @GET("shots?list=playoffs")
    Observable<List<Shot>> getPlayoffList();

    @GET("user/following/shots")
    Observable<List<Shot>> getFollowingShotList();

    /**
     * 评论接口
     */
    @GET("shots/{shot}/comments")
    Observable<List<Comment>> getComments(@Path("shot") long shotId);

    /**
     * 我总是得到403 显示操作禁止（但是我获取access_token收取的时候确实获取了对Comment操作的权限）
     */
    @POST("shots/{shot}/comments")
    Observable<Comment> postComment(@Path("shot") long shotId, @Body String body);

    /**
     * like接口
     */
    @GET("shots/{shot}/like")
    Observable<Like> isLike(@Path("shot") long shotId);

    @POST("shots/{shot}/like")
    Observable<Like> likeShot(@Path("shot") long shotId);

    @DELETE("shots/{shot}/like")
    Observable<Like> unlikeShot(@Path("shot") long shotId);
}
