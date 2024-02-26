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
package io.lhyz.android.dribbble.main.following;

import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import io.viper.android.dribbble.R;
import io.lhyz.android.dribbble.base.app.BaseFragment;
import io.lhyz.android.dribbble.data.bean.Shot;
import io.lhyz.android.dribbble.main.OnShotClickListener;
import io.lhyz.android.dribbble.main.ShotAdapter;
import io.lhyz.android.dribbble.navigation.Navigator;
import io.viper.android.dribbble.databinding.FragListBinding;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * hello,android
 * Created by lhyz on 2016/8/16.
 */
public class FollowingFragment extends BaseFragment implements FollowingContract.View {
    SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerView mRecyclerView;
    View mEmptyView;

    FollowingContract.Presenter mPresenter;
    ShotAdapter mAdapter;

    public static FollowingFragment newInstance() {
        return new FollowingFragment();
    }

    @Override
    protected View getBindingLayout(LayoutInflater inflater, @Nullable ViewGroup container) {
        FragListBinding binding = FragListBinding.inflate(inflater,container,false);
        mSwipeRefreshLayout = binding.refreshLayout;
        mRecyclerView = binding.recyclerList;
        mEmptyView = binding.noItems;
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSwipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getContext(), R.color.color_pink),
                ContextCompat.getColor(getContext(), R.color.color_pro),
                ContextCompat.getColor(getContext(), R.color.color_teams));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.loadFollowing(true);
            }
        });

        mEmptyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.loadFollowing(true);
            }
        });

        mAdapter = new ShotAdapter(getContext(), mOnShotClickListener);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.pause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.destroy();
    }

    @Override
    public void showFollowing(List<Shot> shots) {
        mEmptyView.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);

        mAdapter.setShotList(shots);
    }

    @Override
    public void showLoading() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showEmptyView() {
        mEmptyView.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);

        TextView description = (TextView) mEmptyView.findViewById(R.id.description);
        description.setText("No Followings");
    }

    @Override
    public void showError(String message) {
        Snackbar.make(mEmptyView, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void setPresenter(FollowingContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    private final OnShotClickListener mOnShotClickListener = new OnShotClickListener() {
        @Override
        public void onShotClick(Shot shot) {
            Navigator.startShotDetailsActivity(getActivity(), shot);
        }
    };
}
