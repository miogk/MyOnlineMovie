package com.example.miogk.myonlinemovie;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.services.busline.BusStationItem;
import com.amap.api.services.route.BusPath;
import com.amap.api.services.route.BusStep;
import com.amap.api.services.route.RouteBusLineItem;
import com.amap.api.services.route.RouteBusWalkItem;
import com.amap.api.services.route.WalkStep;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CinemaItemResultActivity extends AppCompatActivity {

    @BindView(R.id.cinema_item_result_recycler_view)
    RecyclerView cinema_item_result_recycler_view;
    @BindView(R.id.cinema_item_result_tool_bar)
    Toolbar cinema_item_result_tool_bar;
    @BindView(R.id.cinema_item_result_back)
    ImageView cinema_item_result_back;
    @BindView(R.id.cinema_item_result_tool_bar_title)
    TextView cinema_item_result_tool_bar_title;

    private static final String TAG = "CinemaItemResultActivit";
    private String title;

    /**
     * //for (BusPath busPath : paths) {
     * //                                    Log.e(TAG, "BusPath: " + "公交距离:" + busPath.getBusDistance() + "米:价格" + busPath.getCost() + "元" +
     * //                                            ":步行距离" + busPath.getWalkDistance() + "米:总距离" + busPath.getDistance());
     * //                                    for (BusStep busStep : busPath.getSteps()) {
     * //                                        RouteBusWalkItem walkItem = busStep.getWalk();
     * //                                        List<WalkStep> walkSteps = walkItem.getSteps();
     * //                                        for (WalkStep walkStep : walkSteps) {
     * //                                            Log.e(TAG, "WalkStep: " + walkStep.getInstruction());
     * //                                        }
     * //                                        Log.e(TAG, "BusStep: " + busStep.getRailway() + ":" + walkItem.getDistance() + "米:" + walkItem.getDuration() + "秒" + busStep.getEntrance()
     * //                                                + ":" + busStep.getExit() + ":" + busStep.getTaxi());
     * //                                        for (RouteBusLineItem busLineItem : busStep.getBusLines()) {
     * //                                            Log.e(TAG, "RouteBusLineItem: " + busLineItem + ":" + busLineItem.getDepartureBusStation() + ":" +
     * //                                                    busLineItem.getArrivalBusStation() + ":" + busLineItem.getTotalPrice() + ":" + busLineItem.getDuration()
     * //                                                    + ":" + busLineItem.getPassStationNum());
     * //                                            for (BusStationItem busStationItem : busLineItem.getPassStations()) {
     * //                                                Log.e(TAG, "BusStationItem: " + busStationItem);
     * //                                            }
     * //                                        }
     * //                                    }
     * //                                }
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cinema_item_result);
        ButterKnife.bind(this);
        if (getActionBar() != null) {
            getActionBar().hide();
        }
        cinema_item_result_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setSupportActionBar(cinema_item_result_tool_bar);
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        cinema_item_result_tool_bar_title.setText(title);
        List<BusPath> paths = (List<BusPath>) intent.getSerializableExtra("paths");
        MyAdapter myAdapter = new MyAdapter(paths);
        cinema_item_result_recycler_view.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        cinema_item_result_recycler_view.setLayoutManager(new LinearLayoutManager(this));
        cinema_item_result_recycler_view.setAdapter(myAdapter);
        getWindow().setStatusBarColor(getResources().getColor(R.color.pink));
//        BusPath busPath = paths.get(0);
//        float totalDistance = busPath.getBusDistance();
//        long totalDuration = busPath.getDuration();
//        float totalWalkDistance = busPath.getWalkDistance();
//        total_duration.setText(totalDuration / 60 + "分钟");
//        total_walk_distance.setText(totalWalkDistance + "米");
//        float cost = busPath.getCost();
//        total_cost.setText(cost + "元");
//        for (BusStep busStep : busPath.getSteps()) {
//            RouteBusWalkItem walkItem = busStep.getWalk();
//            float walkDistance = walkItem.getDistance();
//            float walkDuration = walkItem.getDuration();
//            Log.e(TAG, "walkItem: " + walkDistance + "米 -- " + walkDuration / 60f + "分钟");
//            for (WalkStep walkStep : walkItem.getSteps()) {
//                String instrction = walkStep.getInstruction();
//                Log.e(TAG, "walkStep: " + instrction);
//            }
//            RouteBusLineItem old = null;
//            for (RouteBusLineItem busLineItem : busStep.getBusLines()) {
//                String busLineName = busLineItem.getBusLineName();
//                //有大于第一条路线的话就比较
//                if (old != null) {
//                    if (busLineItem.equals(old)) {
//                        bus_line_item.setText(bus_line_item.getText().toString() + "/" + busLineItem.getBusLineName());
//                    }
//                }
//                //第一条路线的话先储存
//                if (old == null) {
//                    old = busLineItem;
//                    bus_line_item.setText(busLineItem.getBusLineName());
//                }
//                bus_line_item.setText(busLineName);
////                busLineItemTextView.setText(busLineName);
//                float duration = busLineItem.getDuration();
//                String from = busLineItem.getArrivalBusStation().getBusStationName();
//                String to = busLineItem.getDepartureBusStation().getBusStationName();
//                total_station_count.setText(busLineItem.getPassStationNum() + "站");
//                original_staton.setText("从" + from + "出发");
//                Log.e(TAG, "busLineItem: " + busLineName + " -- " + duration / 60 + "分钟 -- 从" + from + "上车 -- 到" + to + "下车" + " -- 一共"
//                        + busLineItem.getPassStationNum() + "站");
//                for (BusStationItem busStationItem : busLineItem.getPassStations()) {
//                    String stationName = busStationItem.getBusStationName();
//                    Log.e(TAG, "busStationItem: " + stationName);
//                }
//            }
//            Log.e(TAG, "onCreate: 总长" + totalDistance + "米,总耗时:" + totalDuration / 60 + "分钟:总价格" + cost);
//        }
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        List<BusPath> paths;

        MyAdapter(List<BusPath> paths) {
            this.paths = paths;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(CinemaItemResultActivity.this).inflate(R.layout.cinema_item_result_item_layout, null);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {
            final BusPath busPath = paths.get(i);
            myViewHolder.root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(CinemaItemResultActivity.this);
                    Intent intent = new Intent(CinemaItemResultActivity.this, CinemaItemResultActivityResultActivity.class);
                    intent.putExtra("title", title);
                    intent.putExtra("busPath", busPath);
                    ActivityCompat.startActivity(CinemaItemResultActivity.this, intent, optionsCompat.toBundle());
                }
            });
            float totalDistance = busPath.getBusDistance();
            long totalDuration = busPath.getDuration();
            float totalWalkDistance = busPath.getWalkDistance();
            myViewHolder.total_duration.setText(totalDuration / 60 + "分钟");
            myViewHolder.total_walk_distance.setText(totalWalkDistance + "米");
            float cost = busPath.getCost();
            myViewHolder.total_cost.setText(cost + "元");
            RouteBusLineItem old = null;
            int passStationNum = 0;
            for (BusStep busStep : busPath.getSteps()) {
                if (busStep.getWalk() != null) {
                    RouteBusWalkItem walkItem = busStep.getWalk();
                    float walkDistance = walkItem.getDistance();
                    float walkDuration = walkItem.getDuration();
                    Log.e(TAG, "walkItem: " + walkDistance + "米 -- " + walkDuration / 60f + "分钟");
                    if (walkItem.getSteps() != null) {
                        for (WalkStep walkStep : walkItem.getSteps()) {
                            String instrction = walkStep.getInstruction();
                            Log.e(TAG, "walkStep: " + instrction);
                        }
                    }
                }
                if (busStep.getBusLines() != null) {
                    for (RouteBusLineItem busLineItem : busStep.getBusLines()) {
                        String busLineName = busLineItem.getBusLineName();
                        //有大于第一条路线的话就比较
                        if (old != null) {
                            if (old.equals(busLineItem)) {
                                myViewHolder.bus_line_item.setText(myViewHolder.bus_line_item.getText().toString() + " | " + busLineItem.getBusLineName());
                            } else {
                                myViewHolder.bus_line_item.setText(myViewHolder.bus_line_item.getText().toString() + " > " + busLineItem.getBusLineName());
                            }
                        }
                        //第一条路线的话先储存
                        if (old == null) {
                            old = busLineItem;
                            myViewHolder.bus_line_item.setText(busLineItem.getBusLineName());
                        }
//                    myViewHolder.bus_line_item.setText(busLineName);
//                busLineItemTextView.setText(busLineName);
                        float duration = busLineItem.getDuration();
                        String from = busLineItem.getArrivalBusStation().getBusStationName();
                        String to = busLineItem.getDepartureBusStation().getBusStationName();
                        passStationNum += busLineItem.getPassStationNum();
                        myViewHolder.total_station_count.setText((passStationNum + 1) + "站");
                        myViewHolder.original_staton.setText("从" + from + "出发");
                        Log.e(TAG, "busLineItem: " + busLineName + " -- " + duration / 60 + "分钟 -- 从" + from + "上车 -- 到" + to + "下车" + " -- 一共"
                                + (busLineItem.getPassStationNum() + 1) + "站");
                        for (BusStationItem busStationItem : busLineItem.getPassStations()) {
                            String stationName = busStationItem.getBusStationName();
                            Log.e(TAG, "busStationItem: " + stationName);
                        }
                        Log.e(TAG, "onBindViewHolder: " + busLineItem.getBusLineType());
                    }
                }
            }
        }

        @Override
        public int getItemCount() {
            return paths.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.total_duration)
            TextView total_duration;
            @BindView(R.id.total_walk_distance)
            TextView total_walk_distance;
            @BindView(R.id.bus_line_item)
            TextView bus_line_item;
            @BindView(R.id.total_station_count)
            TextView total_station_count;
            @BindView(R.id.total_cost)
            TextView total_cost;
            @BindView(R.id.original_staton)
            TextView original_staton;
            View root;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                root = itemView;
                ButterKnife.bind(this, itemView);
            }
        }
    }
}