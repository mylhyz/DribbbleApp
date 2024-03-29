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
package io.lhyz.android.dribbble;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.hilt.android.qualifiers.ApplicationContext;
import io.lhyz.android.dribbble.data.bean.User;

/**
 * hello,android
 * Created by lhyz on 2016/8/7.
 */
@Singleton
public class AppPreference {
    private static final String SHARED_NAME = "dribbble_app_conf";

    private static final String KEY_ACCESS_TOKEN = "access_token";
    private static final String KEY_FIRST_START = "first_start";
    private static final String KEY_TAB_POSITION = "tab_position";

    private static final String _ID = "user_cached_id";
    private static final String _NAME = "user_cached_name";
    private static final String _AVATAR = "user_cached_avatar";
    private static final String _HOST = "user_cached_host";

    private final SharedPreferences mSharedPreferences;

    private OnTokenUpdateListener mOnTokenUpdateListener;

    @Inject
    public AppPreference(@ApplicationContext Context context) {
        mSharedPreferences = context.getSharedPreferences(SHARED_NAME, Context.MODE_PRIVATE);
    }

    public void saveToken(String token) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(KEY_ACCESS_TOKEN, token);
        editor.apply();
        if (mOnTokenUpdateListener != null) {
            mOnTokenUpdateListener.onTokenUpdated(token);
        }
    }

    public String readToken() {
        return mSharedPreferences.getString(KEY_ACCESS_TOKEN, null);
    }

    public void setOnTokenUpdateListener(OnTokenUpdateListener listener) {
        mOnTokenUpdateListener = listener;
    }

    public boolean isFirstStart() {
        boolean isFirstStart = mSharedPreferences.getBoolean(KEY_FIRST_START, true);
        if (isFirstStart) {
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putBoolean(KEY_FIRST_START, false);
            editor.apply();
        }
        return isFirstStart;
    }


    public void saveTabPosition(int pos) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(KEY_TAB_POSITION, pos);
        editor.apply();
    }

    public int readTabPosition() {
        return mSharedPreferences.getInt(KEY_TAB_POSITION, 0);
    }


    public void saveUser(User user) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putLong(_ID, user.getId());
        editor.putString(_NAME, user.getName());
        editor.putString(_AVATAR, user.getAvatarUrl());
        editor.putString(_HOST, user.getHost());
        editor.apply();
    }

    public User readUser() {
        long id = mSharedPreferences.getLong(_ID, 0L);
        if (id == 0) {
            return null;
        }
        String name = mSharedPreferences.getString(_NAME, null);
        String avatar_url = mSharedPreferences.getString(_AVATAR, null);
        String host = mSharedPreferences.getString(_HOST, null);
        return new User(id, name, avatar_url, host);
    }

    public interface OnTokenUpdateListener {
        void onTokenUpdated(String token);
    }
}
