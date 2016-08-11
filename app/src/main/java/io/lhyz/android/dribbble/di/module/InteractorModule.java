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

import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.lhyz.android.boilerplate.executor.PostThreadExecutor;
import io.lhyz.android.boilerplate.executor.ThreadExecutor;
import io.lhyz.android.boilerplate.interactor.Interactor;
import io.lhyz.android.dribbble.data.model.Shot;
import io.lhyz.android.dribbble.di.annotation.Popular;
import io.lhyz.android.dribbble.main.popular.PopularInteractor;

/**
 * hello,android
 * Created by lhyz on 2016/8/8.
 */
@Module
public class InteractorModule {

    @Popular
    @Singleton
    @Provides
    Interactor<List<Shot>> providePopularInteractor(PostThreadExecutor postThreadExecutor,
                                                    ThreadExecutor threadExecutor) {
        return new PopularInteractor(threadExecutor, postThreadExecutor);
    }
}