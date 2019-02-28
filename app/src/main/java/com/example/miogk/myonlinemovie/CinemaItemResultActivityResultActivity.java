package com.example.miogk.myonlinemovie;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.services.busline.BusStationItem;
import com.amap.api.services.route.BusPath;
import com.amap.api.services.route.BusStep;
import com.amap.api.services.route.RouteBusLineItem;
import com.amap.api.services.route.RouteBusWalkItem;
import com.amap.api.services.route.WalkStep;

import org.w3c.dom.Text;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CinemaItemResultActivityResultActivity extends AppCompatActivity {
    @BindView(R.id.cinema_item_result_result_instruction)
    ViewGroup cinema_item_result_result_instruction;
    @BindView(R.id.cinema_item_result_result_bus_station)
    ViewGroup cinema_item_result_result_bus_station;
    @BindView(R.id.cinema_item_result_result_tool_bar_title)
    TextView cinema_item_result_result_tool_bar_title;
    @BindView(R.id.cinema_item_result_result_back)
    ImageView cinema_item_result_result_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cinema_item_result_result);
        ButterKnife.bind(this);
        if (getActionBar() != null) {
            getActionBar().hide();
        }
        cinema_item_result_result_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getWindow().setStatusBarColor(getResources().getColor(R.color.pink));
        Intent intent = getIntent();
        BusPath busPath = intent.getParcelableExtra("busPath");
        String title = intent.getStringExtra("title");
        cinema_item_result_result_tool_bar_title.setText(title);
        List<BusStep> busSteps = busPath.getSteps();
        for (BusStep busStep : busSteps) {
            RouteBusWalkItem routeBusWalkItem = busStep.getWalk();
            if (routeBusWalkItem != null) {
                List<WalkStep> walkSteps = routeBusWalkItem.getSteps();
                for (WalkStep walkStep : walkSteps) {
                    String instruction = walkStep.getInstruction();
                    TextView textView = new TextView(this);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams.bottomMargin = 15;
                    layoutParams.gravity = Gravity.CENTER;
                    textView.setLayoutParams(layoutParams);
                    textView.setTextColor(getResources().getColor(R.color.black));
                    textView.setText(instruction);
                    ImageView imageView = new ImageView(this);
                    imageView.setImageResource(R.drawable.down);
                    imageView.setLayoutParams(layoutParams);
                    cinema_item_result_result_instruction.addView(textView);
                    cinema_item_result_result_instruction.addView(imageView);
                }
            }
            List<RouteBusLineItem> routeBusLineItems = busStep.getBusLines();
            for (RouteBusLineItem routeBusLineItem : routeBusLineItems) {
                TextView busLineName = new TextView(this);
                busLineName.setTextColor(getResources().getColor(R.color.pink));
                busLineName.setText(routeBusLineItem.getBusLineName());
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.bottomMargin = 15;
                layoutParams.gravity = Gravity.CENTER;
                busLineName.setLayoutParams(layoutParams);
                ImageView imageView = new ImageView(this);
                imageView.setImageResource(R.drawable.down);
                imageView.setLayoutParams(layoutParams);
                cinema_item_result_result_instruction.addView(busLineName);
                cinema_item_result_result_instruction.addView(imageView);
                List<BusStationItem> busStationItems = routeBusLineItem.getPassStations();

                //上车站
                TextView departureBusStation = new TextView(this);
                departureBusStation.setTextColor(getResources().getColor(R.color.pink));
                departureBusStation.setText(routeBusLineItem.getDepartureBusStation().getBusStationName());
                departureBusStation.setLayoutParams(layoutParams);
                cinema_item_result_result_instruction.addView(departureBusStation);
                ImageView imageView1 = new ImageView(this);
                imageView1.setImageResource(R.drawable.down);
                imageView1.setLayoutParams(layoutParams);
                cinema_item_result_result_instruction.addView(imageView1);
                for (BusStationItem busStationItem : busStationItems) {
                    TextView textView = new TextView(this);
                    textView.setText(busStationItem.getBusStationName());
                    textView.setTextColor(getResources().getColor(R.color.pink));
                    ImageView imageView2 = new ImageView(this);
                    imageView2.setImageResource(R.drawable.down);
                    imageView2.setLayoutParams(layoutParams);
                    textView.setLayoutParams(layoutParams);
                    cinema_item_result_result_instruction.addView(textView);
                    cinema_item_result_result_instruction.addView(imageView2);
                }
                //下车站
                TextView arrivalBusStation = new TextView(this);
                arrivalBusStation.setTextColor(getResources().getColor(R.color.pink));
                arrivalBusStation.setText(routeBusLineItem.getArrivalBusStation().getBusStationName());
                arrivalBusStation.setLayoutParams(layoutParams);
                cinema_item_result_result_instruction.addView(arrivalBusStation);
                ImageView imageView3 = new ImageView(this);
                imageView3.setImageResource(R.drawable.down);
                imageView3.setLayoutParams(layoutParams);
                cinema_item_result_result_instruction.addView(imageView3);
            }

        }
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.bottomMargin = 15;
        layoutParams.gravity = Gravity.CENTER;
        TextView textView = new TextView(this);
        textView.setTextColor(getResources().getColor(R.color.black));
        textView.setText("到达目的地");
        textView.setLayoutParams(layoutParams);
        cinema_item_result_result_instruction.addView(textView);
    }
}
