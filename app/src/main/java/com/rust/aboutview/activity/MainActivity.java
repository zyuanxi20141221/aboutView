package com.rust.aboutview.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.rust.aboutview.AboutViewConfig;
import com.rust.aboutview.R;
import com.rust.aboutview.contactview.PeopleMainActivity;
import com.rust.aboutview.service.FloatingBarService;
import com.rust.aboutview.widget.DividerLine;
import com.rust.aboutview.widget.PageItemDecoration;
import com.rust.aboutview.widget.PageListAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final String FAN_DEMO_ACT = "fan_view_demo_act";
    private static final String SURFACE_VIEW_DEMO_ACT = "surface_view_demo_act";
    private static final String BULB_VIEW_ACT = "bulb_view_act";
    private static final String DASHBOARD_ACT = "dashboard_view_act";
    private static final String IMAGE_PROCESSING = "image_processing";
    private static final String CIRCLE_VIEW = "circle_view";
    private static final String CONTACT_PAGE = "contact_page";
    private static final String FLOAT_WINDOW = "floating_window";
    private static final String FOLLOW_CURSOR = "follow_cursor";
    private static final String COLOR_BOARD_ACTIVITY = "color_board_act";
    private static final String LINE_CHART_ACT = "line_chart_act";

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.pagesReView)
    RecyclerView mPagesView;
    @BindView(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout mCollapsingToolbarLayout;

    SharedPreferences mConfigs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        initConfigs();

    }

    private void initUI() {
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mCollapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);
        mCollapsingToolbarLayout.setCollapsedTitleTextColor(Color.YELLOW);
        initPageList();
    }

    private void initConfigs() {
        mConfigs = getSharedPreferences(AboutViewConfig.APP_CONFIG, MODE_PRIVATE);
        SharedPreferences.Editor editor = mConfigs.edit();
        editor.putBoolean(AboutViewConfig.SHOW_FLOAT_BAR, false);
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initPageList() {
        final ArrayList<PageListAdapter.DeviceItemViewEntity> pageItemViewEntities = new ArrayList<>();
        pageItemViewEntities.add(new PageListAdapter.DeviceItemViewEntity(FAN_DEMO_ACT, "风力发电机视图"));
        pageItemViewEntities.add(new PageListAdapter.DeviceItemViewEntity(LINE_CHART_ACT, "折线图和饼图"));
        pageItemViewEntities.add(new PageListAdapter.DeviceItemViewEntity(SURFACE_VIEW_DEMO_ACT, "SurfaceView可伸缩图表"));
        pageItemViewEntities.add(new PageListAdapter.DeviceItemViewEntity(BULB_VIEW_ACT, "Bulb view"));
        pageItemViewEntities.add(new PageListAdapter.DeviceItemViewEntity(DASHBOARD_ACT, "Dashboard view"));
        pageItemViewEntities.add(new PageListAdapter.DeviceItemViewEntity(COLOR_BOARD_ACTIVITY, "颜色选择板"));
        pageItemViewEntities.add(new PageListAdapter.DeviceItemViewEntity(IMAGE_PROCESSING, getString(R.string.image_process)));
        pageItemViewEntities.add(new PageListAdapter.DeviceItemViewEntity(CIRCLE_VIEW, getString(R.string.circle_activity)));
        pageItemViewEntities.add(new PageListAdapter.DeviceItemViewEntity(FLOAT_WINDOW, getString(R.string.float_bar_activity)));
        pageItemViewEntities.add(new PageListAdapter.DeviceItemViewEntity(FOLLOW_CURSOR, getString(R.string.drawer_line_activity)));
        pageItemViewEntities.add(new PageListAdapter.DeviceItemViewEntity(CONTACT_PAGE, getString(R.string.contact_activity)));

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mPagesView.setLayoutManager(gridLayoutManager);
        DividerLine dividerLine = new DividerLine(DividerLine.VERTICAL);
        dividerLine.setSize(1);
        dividerLine.setColor(0xFFDDDDDD);
        mPagesView.addItemDecoration(dividerLine);

        PageListAdapter pageListAdapter = new PageListAdapter(pageItemViewEntities);
        pageListAdapter.setOnItemClickListener(new PageListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String id = pageItemViewEntities.get(position).itemID;
                Intent i = new Intent();
                switch (id) {
                    case LINE_CHART_ACT:
                        i.setClass(getApplicationContext(), LineChartAndPieViewActivity.class);
                        startActivity(i);
                        break;
                    case IMAGE_PROCESSING:
                        i.setClass(getApplicationContext(), ImageProcessingActivity.class);
                        startActivity(i);
                        break;
                    case CIRCLE_VIEW:
                        i.setClass(getApplicationContext(), CirclesActivity.class);
                        startActivity(i);
                        break;
                    case CONTACT_PAGE:
                        i.setClass(getApplicationContext(), PeopleMainActivity.class);
                        startActivity(i);
                        break;
                    case FLOAT_WINDOW:
                        boolean mFloatBarShow =
                                !mConfigs.getBoolean(AboutViewConfig.SHOW_FLOAT_BAR, false);
                        SharedPreferences.Editor editor = mConfigs.edit();
                        editor.putBoolean(AboutViewConfig.SHOW_FLOAT_BAR, mFloatBarShow);
                        editor.apply();
                        if (mConfigs.getBoolean(AboutViewConfig.SHOW_FLOAT_BAR, false)) {
                            Intent floatBarIntent =
                                    new Intent(MainActivity.this, FloatingBarService.class);
                            startService(floatBarIntent);
                        } else {
                            Intent floatBarIntent =
                                    new Intent(MainActivity.this, FloatingBarService.class);
                            stopService(floatBarIntent);
                        }
                        break;
                    case FOLLOW_CURSOR:
                        i.setClass(getApplicationContext(), DrawLineActivity.class);
                        startActivity(i);
                        break;
                    case COLOR_BOARD_ACTIVITY:
                        i.setClass(getApplicationContext(), ColorBoardActivity.class);
                        startActivity(i);
                        break;
                    case DASHBOARD_ACT:
                        startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                        break;
                    case BULB_VIEW_ACT:
                        startActivity(new Intent(getApplicationContext(), BulbViewActivity.class));
                        break;
                    case SURFACE_VIEW_DEMO_ACT:
                        startActivity(new Intent(getApplicationContext(), SurfaceViewDemoActivity.class));
                        break;
                    case FAN_DEMO_ACT:
                        startActivity(new Intent(getApplicationContext(), FanActivity.class));
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        mPagesView.addItemDecoration(new PageItemDecoration(getApplicationContext()));
        mPagesView.setAdapter(pageListAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}