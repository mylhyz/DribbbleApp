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
package io.lhyz.android.dribbble.detail;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.Collections;
import java.util.List;

import co.lujun.androidtagview.TagContainerLayout;
import io.viper.android.dribbble.R;
import io.lhyz.android.dribbble.base.app.BaseActivity;
import io.lhyz.android.dribbble.data.bean.Comment;
import io.lhyz.android.dribbble.data.bean.Shot;
import io.viper.android.dribbble.databinding.ActDetailShotBinding;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * hello,android
 * Created by lhyz on 2016/8/12.
 * <p>
 * 横竖屏动态模板代码
 */
public class DetailActivity extends BaseActivity implements DetailContract.View {
    public static final String EXTRA_PARAMS_SHOT = "EXTRA_PARAMS_SHOT";

    //必须控件
    FloatingActionButton fabLikes;
    SimpleDraweeView mImageView;

    //可选控件
    @Nullable
    TextView tvDescription;
    @Nullable
    TagContainerLayout mTagContainerLayout;
    @Nullable
    TextView tvCommentCount;
    @Nullable
    RecyclerView mRecyclerView;
    @Nullable
    SimpleDraweeView imgAuthor;
    @Nullable
    TextView tvUserName;
    @Nullable
    TextView tvUpdateTime;
    @Nullable
    View pbLoading;

    @Nullable
    EditText etComment;
    @Nullable
    ImageButton ibSend;

    CommentAdapter mAdapter;
    DetailContract.Presenter mPresenter;

    enum LikeState {
        LIKE,
        UNLIKE
    }

    LikeState state = LikeState.UNLIKE;

    @Override
    protected View getBindingLayout() {
        ActDetailShotBinding binding = ActDetailShotBinding.inflate(getLayoutInflater());
        fabLikes = binding.actionLikes ;
        mImageView = binding.imgShot;
        tvDescription = binding.tvDescription;
        mTagContainerLayout = binding.tagsView;
        tvCommentCount = binding.tvCommentsCount;
        mRecyclerView = binding.listComments;
        imgAuthor = binding.imgAuthor;
        tvUserName = binding.tvName;
        tvUpdateTime = binding.tvUpdateTime;
        pbLoading = binding.pbLoading;
        etComment = binding.etComment;
        ibSend = binding.ibSend;
        return binding.getRoot();
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.destroy();
    }

    private void initData() {
        Shot shot = (Shot) getIntent().getSerializableExtra(EXTRA_PARAMS_SHOT);
        mPresenter = new DetailPresenter(this, shot);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void showBasicInfo(Shot shot) {
        final Shot.Image image = shot.getImages();
        String url = image.getHidpi() == null ?
                (image.getNormal() == null ? image.getTeaser() : image.getNormal()) :
                image.getHidpi();

        GenericDraweeHierarchyBuilder builder =
                GenericDraweeHierarchyBuilder.newInstance(getResources());
        GenericDraweeHierarchy hierarchy =
                builder.setProgressBarImage(new ProgressBarDrawable())
                        .build();
        mImageView.setHierarchy(hierarchy);

        Uri uri = Uri.parse(url);
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(uri)
                .setAutoPlayAnimations(true)
                .build();
        mImageView.setController(controller);
        //默认不可用
        fabLikes.setEnabled(false);
        fabLikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //点击事件之后按钮不可用
                view.setEnabled(false);
                if (state == LikeState.LIKE) {
                    mPresenter.unlikeShot();
                } else {
                    mPresenter.likeShot();
                }
            }
        });

        //当竖屏时，下面的view都是null
        if (tvDescription == null ||
                mTagContainerLayout == null ||
                imgAuthor == null ||
                tvUserName == null ||
                tvUpdateTime == null ||
                etComment == null ||
                ibSend == null) {
            return;
        }

        //Toolbar设置
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        ActionBar actionBar = getSupportActionBar();
        checkNotNull(actionBar);
        actionBar.setTitle("");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        if (shot.getDescription() != null) {
            tvDescription.setText(Html.fromHtml(shot.getDescription()));
        } else {
            tvDescription.setText("No Description");
        }

        if (shot.getTags() != null && shot.getTags().size() > 0) {
            mTagContainerLayout.setTags(shot.getTags());
        } else {
            mTagContainerLayout.setTags(Collections.singletonList("No Tag"));
        }

        imgAuthor.setImageURI(Uri.parse(shot.getUser().getAvatarUrl()));
        tvUserName.setText(shot.getUser().getName());
        tvUpdateTime.setText(shot.getCreatedTime());

        ibSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(etComment.getText().toString())) {
                    mPresenter.postComment(etComment.getText().toString());
                    etComment.setText(null);
                    mPresenter.loadComments();
                }
            }
        });

        if (mRecyclerView == null) {
            return;
        }
        //横屏状态
        mAdapter = new CommentAdapter(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, getResources().getDisplayMetrics().density));
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showAllComments(List<Comment> comments) {
        if (tvCommentCount == null) {
            return;
        }
        if (comments.size() > 0) {
            tvCommentCount.setText(comments.size() + " Comments");
            mAdapter.setCommentList(comments);
        } else {
            tvCommentCount.setText("No Comments");
        }
    }

    @Override
    public void showLoadingComments() {
        if (pbLoading == null || mRecyclerView == null) {
            return;
        }
        pbLoading.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void hideLoadingComments() {
        if (pbLoading == null || mRecyclerView == null) {
            return;
        }
        pbLoading.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showNoComments() {
        if (tvCommentCount == null) {
            return;
        }
        tvCommentCount.setText("No Comments");
    }

    @Override
    public void showLikesState(boolean likes) {
        //更新了按钮状态之后解禁按钮
        fabLikes.setEnabled(true);
        if (likes) {
            state = LikeState.LIKE;
            fabLikes.setImageResource(R.drawable.ic_favorite_white_24dp);
        } else {
            state = LikeState.UNLIKE;
            fabLikes.setImageResource(R.drawable.ic_favorite_outline_white_24dp);
        }
    }

    @Override
    public void setPresenter(DetailContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public boolean isPortrait() {
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    @Override
    protected void setWindowFeature() {
        if (getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_PORTRAIT) {
            super.setWindowFeature();
        } else {
            /**
             * 在此activity中{@link #requestWindowFeature(int)} 不起作用,
             * AppCompatActivity中需要使用{@link #supportRequestWindowFeature(int)}
             */
            supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

}

