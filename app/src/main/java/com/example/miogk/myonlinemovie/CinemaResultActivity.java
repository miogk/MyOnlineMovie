package com.example.miogk.myonlinemovie;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.route.BusPath;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CinemaResultActivity extends AppCompatActivity {
    Context context = this;
    Activity activity = this;
    @BindView(R.id.cinema_result_key_word)
    AutoCompleteTextView cinema_result_key_word;
    @BindView(R.id.cinema_result_map_view)
    MapView mapView;
    private AMap aMap;
    private static final String TAG = "CinemaResultActivity";
    @BindView(R.id.cinema_result_recycler_view)
    RecyclerView cinema_result_recycler_view;
    private MyAdapter adapter;
    private LatLonPoint latLonPoint;
    private android.app.AlertDialog alertDialog;
    @BindView(R.id.cinema_result_search)
    Button cinema_result_search;

    private void dingwei() {
        AMapLocationClient mLocationClient = new AMapLocationClient(this);
        AMapLocationClientOption mClientOption = new AMapLocationClientOption();
        mClientOption.setOnceLocationLatest(true);
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式
        //高精度定位模式：会同时使用网络定位和GPS定位，优先返回最高精度的定位结果，以及对应的地址描述信息。
        mClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation != null) {
                    if (aMapLocation.getErrorCode() == 0) {
                        latLonPoint = new LatLonPoint(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                    } else {
                        Log.e(TAG, "onLocationChanged: " + aMapLocation.getErrorCode() + ":" + aMapLocation.getErrorInfo());
                    }
                }
            }
        });
        mLocationClient.stopLocation();
        mLocationClient.startLocation();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cinema_result);
        ButterKnife.bind(this);
        if (getActionBar() != null) {
            getActionBar().hide();
        }
        dingwei();
        getWindow().setStatusBarColor(getResources().getColor(R.color.pink));
        doSearchQuery();
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        ArrayList<PoiItem> items;

        MyAdapter(ArrayList<PoiItem> items) {
            this.items = items;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(CinemaResultActivity.this).inflate(R.layout.cinema_result_recycler_view_layout, null);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
            final PoiItem item = items.get(i);
            final String s = item.getTitle() + item.getAdName() + item.getBusinessArea() + item.getSnippet();
            final int distance = item.getDistance();
            myViewHolder.cinema_result_recycler_view_layout_address.setText(s);
            myViewHolder.cinema_result_recycler_view_layout_distance.setText(item.getDistance() + "米");
            View root = myViewHolder.root;
            root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Intent intent = new Intent(CinemaResultActivity.this, CinemaItemResultActivity.class);
                    RouteSearch routeSearch = new RouteSearch(CinemaResultActivity.this);
                    RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(latLonPoint, item.getLatLonPoint());
                    Log.e(TAG, "onClick: " + latLonPoint + "|" + item.getLatLonPoint());
                    RouteSearch.WalkRouteQuery walkRouteQuery = new RouteSearch.WalkRouteQuery(fromAndTo);
                    routeSearch.calculateWalkRouteAsyn(walkRouteQuery);
                    RouteSearch.BusRouteQuery query = new RouteSearch.BusRouteQuery(fromAndTo, RouteSearch.BUS_DEFAULT, "上海市", 0);
                    routeSearch.calculateBusRouteAsyn(query);//异步请求开始规划路径
                    routeSearch.setRouteSearchListener(new RouteSearch.OnRouteSearchListener() {
                        @Override
                        public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {
                            if (i == 1000) {
                                List<BusPath> paths = busRouteResult.getPaths();
                                if (paths.size() > 0) {
                                    ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity);
                                    intent.putExtra("paths", (Serializable) paths);
                                    intent.putExtra("title", item.getTitle());
                                    ActivityCompat.startActivity(activity, intent, optionsCompat.toBundle());
                                } else {
                                    Toast.makeText(context, "距离太近，请用步行", Toast.LENGTH_SHORT).show();
                                }
//                                for (BusPath busPath : paths) {
//                                    Log.e(TAG, "BusPath: " + "公交距离:" + busPath.getBusDistance() + "米:价格" + busPath.getCost() + "元" +
//                                            ":步行距离" + busPath.getWalkDistance() + "米:总距离" + busPath.getDistance());
//                                    for (BusStep busStep : busPath.getSteps()) {
//                                        RouteBusWalkItem walkItem = busStep.getWalk();
//                                        List<WalkStep> walkSteps = walkItem.getSteps();
//                                        for (WalkStep walkStep : walkSteps) {
//                                            Log.e(TAG, "WalkStep: " + walkStep.getInstruction());
//                                        }
//                                        Log.e(TAG, "BusStep: " + busStep.getRailway() + ":" + walkItem.getDistance() + "米:" + walkItem.getDuration() + "秒" + busStep.getEntrance()
//                                                + ":" + busStep.getExit() + ":" + busStep.getTaxi());
//                                        for (RouteBusLineItem busLineItem : busStep.getBusLines()) {
//                                            Log.e(TAG, "RouteBusLineItem: " + busLineItem + ":" + busLineItem.getDepartureBusStation() + ":" +
//                                                    busLineItem.getArrivalBusStation() + ":" + busLineItem.getTotalPrice() + ":" + busLineItem.getDuration()
//                                                    + ":" + busLineItem.getPassStationNum());
//                                            for (BusStationItem busStationItem : busLineItem.getPassStations()) {
//                                                Log.e(TAG, "BusStationItem: " + busStationItem);
//                                            }
//                                        }
//                                    }
//                                }
//                                for (BusPath path : paths) {
//                                    for (BusStep busStep : path.getSteps()) {
//                                        List<RouteBusLineItem> busLineItems = busStep.getBusLines();
//                                        RouteBusWalkItem walkItem = busStep.getWalk();
//                                    }
//                                }
                            } else {
                                Log.e(TAG, "onBusRouteSearched: 公交规划出错");
                            }
                        }

                        @Override
                        public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {

                        }

                        @Override
                        public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {
                            if (i == 1000) {
                                Log.e(TAG, "onWalkRouteSearched: " + walkRouteResult);
                            }
                        }

                        @Override
                        public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

                        }
                    });
                }
            });
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.cinema_result_recycler_view_layout_address)
            TextView cinema_result_recycler_view_layout_address;
            @BindView(R.id.cinema_result_recycler_view_layout_distance)
            TextView cinema_result_recycler_view_layout_distance;
            View root;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                root = itemView;
                ButterKnife.bind(this, itemView);

            }
        }
    }

    private void searchPos(String keyWord) {
//aMap.getUiSettings().setMyLocationButtonEnabled(true);设置默认定位按钮是否显示，非必需设置。
//        aMap.setMyLocationEnabled(true);// 设置
        final PoiSearch.Query query = new PoiSearch.Query(keyWord, "", "上海市");
//keyWord表示搜索字符串，
//第二个参数表示POI搜索类型，二者选填其一，选用POI搜索类型时建议填写类型代码，码表可以参考下方（而非文字）
//cityCode表示POI搜索区域，可以是城市编码也可以是城市名称，也可以传空字符串，空字符串代表全国在全国范围内进行搜索
        query.setPageSize(100);// 设置每页最多返回多少条poiitem
        query.setPageNum(1);//设置查询页码
        final PoiSearch poiSearch = new PoiSearch(this, query);
        poiSearch.setBound(new PoiSearch.SearchBound(latLonPoint, 500000, true));//设置周边搜索的中心点以及半径
        poiSearch.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
            /**
             * 1）可以在回调中解析result，获取POI信息。
             *
             //             * 2）result.getPois()可以获取到PoiItem列表，Poi详细信息可参考PoiItem类。
             //             *
             //             * 3）若当前城市查询不到所需POI信息，可以通过result.getSearchSuggestionCitys()获取当前Poi搜索的建议城市。
             //             *
             //             * 4）如果搜索关键字明显为误输入，则可通过result.getSearchSuggestionKeywords()方法得到搜索关键词建议。
             //             *
             //             * 5）返回结果成功或者失败的响应码。1000为成功，其他为失败（详细信息参见网站开发指南-实用工具-错误码对照表）
             //             * @param poiResult
             //             * @param i
             //             */
            @Override
            public void onPoiSearched(PoiResult poiResult, int i) {
//                aMap.clear(true);
                if (i == 1000) {
                    adapter = new MyAdapter(poiResult.getPois());
                    cinema_result_recycler_view.setLayoutManager(new LinearLayoutManager(CinemaResultActivity.this));
                    cinema_result_recycler_view.setAdapter(adapter);
                    cinema_result_recycler_view.addItemDecoration(new DividerItemDecoration(CinemaResultActivity.this, DividerItemDecoration.VERTICAL));
                    cinema_result_recycler_view.setVisibility(View.VISIBLE);
                    alertDialog.cancel();
                } else {
                    Toast.makeText(CinemaResultActivity.this, "未查询到所需的信息...", Toast.LENGTH_SHORT).show();
                }
                //                ArrayList<PoiItem> items = poiResult.getPois();
//                for (PoiItem item : items) {
//                    String s = item.getTitle() + "\n" + item.getAdName() + item.getBusinessArea() + item.getSnippet() + item.getDistance();
//                    Log.e(TAG, "onPoiSearched: " + s);
//                    LatLonPoint latLonPoint = item.getLatLonPoint();
//                    LatLng latLng = new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude());
//                    Marker marker = aMap.addMarker(new MarkerOptions().position(latLng).snippet(s.trim()));
//                }
            }

            @Override
            public void onPoiItemSearched(PoiItem item, int i) {

            }
        });
        poiSearch.searchPOIAsyn();

//        //第二个参数传入null或者“”代表在全国进行检索，否则按照传入的city进行检索
//        InputtipsQuery inputquery = new InputtipsQuery("电影院", "上海");
//        inputquery.setCityLimit(true);//限制在当前城市
//        Inputtips inputTips = new Inputtips(CinemaResultActivity.this, inputquery);
//        inputTips.setInputtipsListener(new Inputtips.InputtipsListener() {
//            @Override
//            /**
//             * 1）在回调中解析 tipList，获取输入提示词的相关信息。
//             *
//             * 2）tipList 数组中的对象是 Tip ，Tip 类中包含 PoiID、Adcode、District、Name 等信息。
//             *
//             * 注意：
//             *
//             *     a 、由于提示中会出现相同的关键字，但是这些关键字所在区域不同，使用时可以通过 tipList.get(i).getDistrict() 获得区域，也可以在提示时在关键字后加上区域。
//             *
//             *     b、当 Tip 的 getPoiID() 返回空，并且 getPoint() 也返回空时，表示该提示词不是一个真实存在的 POI，这时区域、经纬度参数都是空的，此时可根据该提示词进行POI关键词搜索
//             *
//             *     c、当 Tip 的 getPoiID() 返回不为空，但 getPoint() 返回空时，表示该提示词是一个公交线路名称，此时用这个id进行公交线路查询。
//             *
//             *     d、当 Tip 的 getPoiID() 返回不为空，且 getPoint() 也不为空时，表示该提示词一个真实存在的POI，可直接显示在地图上。
//             */
//            public void onGetInputtips(List<Tip> list, int i) {
//
//            }
//        });
//        inputTips.requestInputtipsAsyn();
    }

    private void s(TextView v) {
        String keyWord = v.getText().toString().trim();
        if (!TextUtils.isEmpty(keyWord)) {
            //先清除数据
            cinema_result_recycler_view.setVisibility(View.GONE);
            searchPos(keyWord);
            ProgressDialog.Builder builder = new android.app.AlertDialog.Builder(CinemaResultActivity.this);
            builder.setMessage("正在搜索'" + keyWord + "'");
            alertDialog = builder.show();
        } else {
            ProgressDialog.Builder builder = new ProgressDialog.Builder(CinemaResultActivity.this)
                    .setMessage("关键词不能为空,请重新输入.");
            builder.setPositiveButton("确定", null);
            builder.setNegativeButton("取消", null);
            builder.show();
        }
    }


    protected void doSearchQuery() {
        cinema_result_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s(cinema_result_key_word);
            }
        });
        cinema_result_key_word.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    switch (event.getAction()) {
                        case KeyEvent.ACTION_UP:
                            s(v);
                            break;
                    }
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
//        mapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        mapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
//        mapView.onSaveInstanceState(outState);
    }
}
