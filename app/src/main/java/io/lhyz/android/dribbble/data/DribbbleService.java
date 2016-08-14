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

import io.lhyz.android.dribbble.data.model.Comment;
import io.lhyz.android.dribbble.data.model.Like;
import io.lhyz.android.dribbble.data.model.Shot;
import io.lhyz.android.dribbble.data.model.User;
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

    @GET("user")
    Observable<User> getUser();

    //默认是popular
    @GET("shots")
    Observable<List<Shot>> getPopular();

    //参数是sort=recent
    @GET("shots?sort=recent")
    Observable<List<Shot>> getRecent();

    //参数list=debuts
    @GET("shots?list=debuts")
    Observable<List<Shot>> getDebuts();

    //参数是sort=recent
    @GET("shots?list=teams")
    Observable<List<Shot>> getTeams();

    //参数是sort=recent
    @GET("shots?list=playoffs")
    Observable<List<Shot>> getPlayoffs();

    @GET("shots/{shot}/comments")
    Observable<List<Comment>> getComments(@Path("shot") int shotId);

    @GET("shots/{shot}/like")
    Observable<Like> isLikes(@Path("shot") int shotId);

    @POST("shots/{shot}/like")
    Observable<Like> likeShot(@Path("shot") int shotId);

    @DELETE("shots/{shot}/like")
    Observable<Like> unlikeShot(@Path("shot") int shotId);
}
