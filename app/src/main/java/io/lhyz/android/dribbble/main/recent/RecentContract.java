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
package io.lhyz.android.dribbble.main.recent;

import java.util.List;

import io.lhyz.android.dribbble.base.mvp.BasePresenter;
import io.lhyz.android.dribbble.data.bean.Shot;
import io.lhyz.android.dribbble.main.LoadView;

/**
 * hello,android
 * Created by lhyz on 2016/8/11.
 */
public class RecentContract {
    interface View extends LoadView<Presenter> {
        void showRecent(List<Shot> shots);
    }

    interface Presenter extends BasePresenter {
        void loadRecent(boolean force);
    }
}
