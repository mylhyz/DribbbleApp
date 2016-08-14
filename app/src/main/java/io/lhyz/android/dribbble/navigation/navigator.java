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
package io.lhyz.android.dribbble.navigation;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import io.lhyz.android.dribbble.data.model.Shot;
import io.lhyz.android.dribbble.detail.ShotDetailActivity;

/**
 * hello,android
 * Created by lhyz on 2016/8/14.
 */
public class Navigator {

    public static void startShotDetailsActivity(@NonNull Context context, @NonNull Shot shot) {
        Intent intent = new Intent(context, ShotDetailActivity.class);
        intent.putExtra(ShotDetailActivity.EXTRA_PARAMS_SHOT, shot);
        context.startActivity(intent);
    }
}
