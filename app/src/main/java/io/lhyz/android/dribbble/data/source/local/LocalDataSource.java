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
package io.lhyz.android.dribbble.data.source.local;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.orhanobut.logger.Logger;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import io.lhyz.android.boilerplate.util.TagHelper;
import io.lhyz.android.dribbble.data.bean.Comment;
import io.lhyz.android.dribbble.data.bean.Like;
import io.lhyz.android.dribbble.data.bean.Shot;
import io.lhyz.android.dribbble.data.mapper.CommentMapper;
import io.lhyz.android.dribbble.data.mapper.CommentModelMapper;
import io.lhyz.android.dribbble.data.mapper.ShotMapper;
import io.lhyz.android.dribbble.data.mapper.ShotModelMapper;
import io.lhyz.android.dribbble.data.model.CommentModel;
import io.lhyz.android.dribbble.data.model.ShotModel;
import io.lhyz.android.dribbble.data.source.DataSource;
import rx.Observable;
import rx.Subscriber;

/**
 * hello,android
 * Created by lhyz on 2016/8/14.
 */
public class LocalDataSource implements DataSource {
    private static final String TAG = TagHelper.from(LocalDataSource.class);

    private static LocalDataSource INSTANCE;

    Dao<CommentModel, Long> mCommentDao;
    Dao<ShotModel, Long> mShotDao;

    public LocalDataSource(Context context) {
        CommentDBHelper commentDBHelper = new CommentDBHelper(context);
        ShotDBHelper shotDBHelper = new ShotDBHelper(context);
        try {
            mCommentDao = commentDBHelper.getDao();
            mShotDao = shotDBHelper.getDao();
        } catch (SQLException e) {
            Logger.e(TAG, e.getMessage());
        }
    }

    public static LocalDataSource getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new LocalDataSource(context);
        }
        return INSTANCE;
    }

    /**
     * 根据shot列表类型获取列表
     */
    @Override
    public Observable<List<Shot>> getShotList(final int type) {
        return Observable.create(new Observable.OnSubscribe<List<Shot>>() {
            @Override
            public void call(Subscriber<? super List<Shot>> subscriber) {
                try {
                    QueryBuilder<ShotModel, Long> queryBuilder = mShotDao.queryBuilder();
                    queryBuilder.where().eq("type", type);
                    PreparedQuery<ShotModel> preparedQuery = queryBuilder.prepare();
                    List<ShotModel> shotModels = mShotDao.query(preparedQuery);
                    subscriber.onNext(ShotModelMapper.getInstance().transform(shotModels));
                    subscriber.onCompleted();
                } catch (SQLException e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    /**
     * 保存一种类型的shot列表
     */
    @Override
    public void saveShotList(int type, List<Shot> shots) {
        Collection<ShotModel> shotModels = ShotMapper.getInstance().transform(shots);
        try {
            mShotDao.create(shotModels);
        } catch (SQLException e) {
            Logger.e(TAG, e.getMessage());
        }
    }

    @Override
    public void refreshShots(int type) {
        //Not Need For Local DataSource
    }

    /**
     * 获取某个shot的所有comment
     */
    @Override
    public Observable<List<Comment>> getComments(final long shotId) {
        return Observable.create(new Observable.OnSubscribe<List<Comment>>() {
            @Override
            public void call(Subscriber<? super List<Comment>> subscriber) {
                try {
                    QueryBuilder<CommentModel, Long> queryBuilder = mCommentDao.queryBuilder();
                    queryBuilder.where().eq("shotId", shotId);
                    PreparedQuery<CommentModel> preparedQuery = queryBuilder.prepare();
                    List<CommentModel> commentModels = mCommentDao.query(preparedQuery);
                    subscriber.onNext(CommentModelMapper.getInstance().transform(commentModels));
                    subscriber.onCompleted();
                } catch (SQLException e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    /**
     * 本地添加一条comment（远程添加完毕之后再调用本地保存）
     */
    @Override
    public Observable<Comment> addComment(final long shotId, final Comment comment) {
        CommentModel commentModel = CommentMapper.getInstance().transform(comment);
        commentModel.setShotId(shotId);
        try {
            mCommentDao.create(commentModel);
        } catch (SQLException e) {
            Logger.e(TAG, e.getMessage());
        }
        return Observable.empty();
    }

    /**
     * 保存一条shot所有comment列表
     */
    @Override
    public void saveCommentList(long shotId, List<Comment> comments) {
        Collection<CommentModel> commentModels = CommentMapper.getInstance().transform(comments);
        try {
            mCommentDao.create(commentModels);
        } catch (SQLException e) {
            Logger.e(TAG, e.getMessage());
        }
    }

    @Override
    public void refreshComment() {
        //Not Need For Local DataSource
    }

    @Override
    public Observable<Like> isLike(long shotId) {
        //本地无法检测
        return null;
    }

    @Override
    public Observable<Like> likeShot(long shotId) {
        //本地无法操作
        return null;
    }

    @Override
    public Observable<Like> unlikeShot(long shotId) {
        //本地无法操作
        return null;
    }
}