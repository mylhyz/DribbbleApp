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

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.lhyz.android.dribbble.login.AuthActivity;
import io.lhyz.android.dribbble.main.MainActivity;

/**
 * hello,android
 * Created by lhyz on 2016/8/8.
 * <p/>
 * 启动导航页,根据token是否已经获取进行跳转
 */
@AndroidEntryPoint
public class AppStart extends AppCompatActivity {

    @Inject
    AppPreference mAppPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

//        if (BuildConfig.DEBUG) {
//            AppPreference.getInstance().saveToken(BuildConfig.ACCESS_TOKEN);
//        }

        if (mAppPref.readToken() == null) {
            redirectToLogin();
        } else {
            redirectToMain();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode && AuthActivity.REQUEST_AUTH == requestCode) {
            String token = data.getData().toString();
            mAppPref.saveToken(token);
            redirectToMain();
        } else if (RESULT_CANCELED == resultCode) {
            finish();
        }
    }

    private void redirectToLogin() {
        Intent intent = new Intent(this, AuthActivity.class);
        startActivityForResult(intent, AuthActivity.REQUEST_AUTH);
    }

    private void redirectToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
