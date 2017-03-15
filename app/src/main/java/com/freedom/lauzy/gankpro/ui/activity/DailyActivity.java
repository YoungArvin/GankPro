package com.freedom.lauzy.gankpro.ui.activity;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.Toast;

import com.freedom.lauzy.gankpro.R;
import com.freedom.lauzy.gankpro.app.GankApp;
import com.freedom.lauzy.gankpro.common.base.BaseToolbarActivity;
import com.freedom.lauzy.gankpro.common.utils.ScreenUtils;
import com.freedom.lauzy.gankpro.common.widget.ImageLoader;
import com.freedom.lauzy.gankpro.function.entity.ItemBean;
import com.freedom.lauzy.gankpro.function.utils.DateUtils;
import com.freedom.lauzy.gankpro.function.view.DailyItemDecoration;
import com.freedom.lauzy.gankpro.presenter.DailyPresenter;
import com.freedom.lauzy.gankpro.ui.adapter.DailyAdapter;
import com.freedom.lauzy.gankpro.view.DailyView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.freedom.lauzy.gankpro.function.ValueConstants.ImageValue.IMAGE_URL;
import static com.freedom.lauzy.gankpro.function.ValueConstants.ImageValue.PUBLISH_DATE;

public class DailyActivity extends BaseToolbarActivity {


    private static final String LYTAG = DailyActivity.class.getSimpleName();
    @BindView(R.id.img_title)
    ImageView mImgTitle;
    @BindView(R.id.toolbar_daily)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout mToolbarLayout;
    @BindView(R.id.rv_daily)
    RecyclerView mRvDaily;
    @BindView(R.id.srl_daily)
    SwipeRefreshLayout mSrlDaily;
    @BindView(R.id.fab_beauty)
    FloatingActionButton mFabBeauty;
    private Date mPublishDate;
    @BindView(R.id.stub_empty_view)
    ViewStub mEmptyViewStub;
    @BindView(R.id.app_bar)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.act_daily)
    View mContentView;
    private List<ItemBean> mItemBeen = new ArrayList<>();
    private DailyAdapter mAdapter;
    private DailyPresenter mDailyPresenter;

    @Override
    protected void loadData() {
        String imgUrl = getIntent().getStringExtra(IMAGE_URL);
//        Bitmap img_data = getIntent().getParcelableExtra("img_data");
        /*CollectionEntityDao entityDao = GankApp.getGankApp().getDaoSession().getCollectionEntityDao();
        CollectionEntity entity = entityDao.queryBuilder().where(CollectionEntityDao.Properties.Id.eq(img_id)).build().list().get(0);*/
        ImageLoader.loadImage(this, imgUrl, mImgTitle);
        if (mPublishDate == null) {
            Log.d(LYTAG, "loadData: date is null");
        }

        initPresenter();
        mDailyPresenter.initData();
    }

    private void initPresenter() {
        mDailyPresenter = new DailyPresenter(new DailyView() {
            @Override
            public void initRvData(List<ItemBean> data) {
                if (data == null) {
                    mEmptyViewStub.inflate();
                } else {
                    mItemBeen.addAll(data);
                    mAdapter.notifyDataSetChanged();
                }
                mSrlDaily.setRefreshing(false);
            }

            @Override
            public void refreshRvData(List<ItemBean> refreshData) {
                if (refreshData != null) {
                    mAdapter.removeAllData();
                    mAdapter.addData(refreshData);
                    mAdapter.notifyDataSetChanged();
                }
                mSrlDaily.setRefreshing(false);
            }

            @Override
            public void refreshError(Throwable e) {
                mSrlDaily.setRefreshing(false);
            }
        }, mPublishDate);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_daily;
    }

    @Override
    protected void initViews() {

        mPublishDate = (Date) getIntent().getSerializableExtra(PUBLISH_DATE);
        mToolbarLayout.setTitle(DateUtils.toDate(mPublishDate));
        ViewCompat.setTransitionName(mImgTitle, "transitionImg");
//        Log.i(LYTAG, "initViews: " + mToolbar.getLayoutParams().height);
        mToolbar.getLayoutParams().height += ScreenUtils.getStatusHeight(GankApp.getInstance());
        mToolbar.setPadding(0, ScreenUtils.getStatusHeight(GankApp.getInstance()), 0, 0);
//        Log.i(LYTAG, "initViews: " + ScreenUtils.getStatusHeight(GankApp.getInstance()));
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.mipmap.icon_arrow_white_back);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRvDaily.setLayoutManager(linearLayoutManager);

        mSrlDaily.setColorSchemeResources(R.color.color_style_gray);
        mSrlDaily.setRefreshing(true);
        mAdapter = new DailyAdapter(DailyActivity.this, mItemBeen);
        mRvDaily.setAdapter(mAdapter);

        mSrlDaily.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSrlDaily.setRefreshing(false);
                mDailyPresenter.refreshData();
            }
        });
        mRvDaily.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy <= 0) {
                    mFabBeauty.show();
                } else {
                    mFabBeauty.hide();
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });


        mRvDaily.addItemDecoration(new DailyItemDecoration(DailyActivity.this, mItemBeen));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.fab_beauty)
    public void onClick() {
        Toast.makeText(this, "hh", Toast.LENGTH_SHORT).show();
    }

}
