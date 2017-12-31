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

    ArrayList<Pair<String,Pair<Double,Double>>> pairList = new ArrayList<Pair<String,Pair<Double,Double>>>();
    private String positionstr="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);//设置对应的XML布局文件

        MapView mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        AMap aMap = mapView.getMap();


        Intent intent = getIntent();
        positionstr = intent.getStringExtra(detailsoftopic.EXTRA_MESSAGE);
        if(positionstr != null){
            Double firststr = Double.parseDouble(positionstr.split(",")[0]);
            Double secondstr = Double.parseDouble(positionstr.split(",")[1]);
            Pair<Double, Double> p = new Pair<>(firststr, secondstr);
            Pair<String , Pair<Double,Double>> p1 = new Pair<>("",p);
            pairList.add(p1);
        }
        else{
            listHandler listhandler = new listHandler("1");
            pairList = listhandler.getPositionWithTitle();
        }

//        Pair<Double,Double> q1 = new Pair<>(40.224956,116.588508);
//        Pair<Double,Double> q2 = new Pair<>(43.224956,113.588508);
//
//        Pair<String , Pair<Double,Double>> p1 = new Pair<>("eating",q1);
//        Pair<String , Pair<Double,Double>> p2 = new Pair<>("sleeping",q2);
//
//        pairList.add(p1);
//        pairList.add(p2);
//

        for(Pair<String,Pair<Double,Double>> p : pairList){
            LatLng latLng = new LatLng(p.second.first,p.second.second);
            final Marker marker = aMap.addMarker(new MarkerOptions().position(latLng).title(p.first));
        }

        if(positionstr==null) {
            LatLng latLng = new LatLng(39.906901, 116.397972);
            final Marker marker = aMap.addMarker(new MarkerOptions().position(latLng).title("北京"));
            // 定义 Marker拖拽的监听
            marker.isDraggable();
            marker.setDraggable(true);
            Log.i("bslc", "bslc_MapsActivity_OnCreate_markerisDraggable" + marker.isDraggable());
        }


    }
}
