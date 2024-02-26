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
package io.lhyz.android.dribbble.di.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import io.lhyz.android.dribbble.AppPreference;
import io.lhyz.android.dribbble.data.DribbbleService;
import io.lhyz.android.dribbble.net.AuthorizationInterceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * hello,android
 * Created by lhyz on 2016/8/10.
 */
@Module
@InstallIn(SingletonComponent.class)
public class NetModule {

    private static final String DRIBBBLE_API = "https://api.dribbble.com/v1/";


    @Provides
    @Singleton
    public static AuthorizationInterceptor provideInterceptor(AppPreference perf) {
        return new AuthorizationInterceptor(perf);
    }

    @Provides
    @Singleton
    public static Retrofit provideRetrofit(AuthorizationInterceptor interceptor) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();
        return new Retrofit.Builder()
                .baseUrl(DRIBBBLE_API)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
    }

    @Provides
    @Singleton
    public static DribbbleService provideServiceAPI(Retrofit retrofit) {
        return retrofit.create(DribbbleService.class);
    }
}
