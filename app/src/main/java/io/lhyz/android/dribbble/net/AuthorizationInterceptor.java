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
package io.lhyz.android.dribbble.net;

import java.io.IOException;

import io.lhyz.android.dribbble.AppPreference;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * hello,android
 * Created by lhyz on 2016/8/7.
 * <p/>
 * 构造请求header，并入OkHttpClient
 * <p/>
 * 专用于Authorization头部添加
 */
public class AuthorizationInterceptor implements Interceptor {

    private String mCurrentAccessToken;

    public AuthorizationInterceptor(AppPreference perf) {
        this.mCurrentAccessToken = perf.readToken();
        perf.setOnTokenUpdateListener(new AppPreference.OnTokenUpdateListener() {
            @Override
            public void onTokenUpdated(String token) {
                AuthorizationInterceptor.this.mCurrentAccessToken = token;
            }
        });
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        request = request.newBuilder().addHeader("Authorization", "Bearer " + mCurrentAccessToken).build();
        return chain.proceed(request);
    }
}
