package blackstorelongclass.personal_info_collector;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.provider.SyncStateContract;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;

import java.util.ArrayList;

import blackstorelongclass.personal_info_collector.DataHandler.listHandler;

public class MapShowPage extends AppCompatActivity {

    ArrayList<Pair> pairList = new ArrayList<Pair>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);//设置对应的XML布局文件

        MapView mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        AMap aMap = mapView.getMap();

        listHandler listhandler = new listHandler("1");
        pairList = listhandler.getPositionWithTitle();

//        Pair<Long , Pair<Long,Long>> p1 = new Pair<>("eating",(40.224956,116.588508));
//        Pair<Long , String> p2 = new Pair<>(1511451210000L,"eating");
//
//        pairList.add(p1);
//        pairList.add(p2);


        for(Pair<String,Pair<Long,Long>> p : pairList){
            LatLng latLng = new LatLng(p.second.first,p.second.second);
            final Marker marker = aMap.addMarker(new MarkerOptions().position(latLng).title(p.first));
        }

        LatLng latLng = new LatLng(39.906901, 116.397972);
        final Marker marker = aMap.addMarker(new MarkerOptions().position(latLng).title("北京"));
        // 定义 Marker拖拽的监听
        marker.isDraggable();
        marker.setDraggable(true);
        Log.i("bslc", "bslc_MapsActivity_OnCreate_markerisDraggable" + marker.isDraggable());

    }
}
