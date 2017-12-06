package blackstorelongclass.personal_info_collector;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.provider.SyncStateContract;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;

public class MapsActivity extends AppCompatActivity implements View.OnClickListener {

    private LatLng position;
    public final static String EXTRA_MESSAGE = "blackstorelongclass.personal_info_collector.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);//设置对应的XML布局文件

        MapView mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        AMap aMap = mapView.getMap();

        Button submit = (Button)findViewById(R.id.submit);
        submit.setOnClickListener(this);


        LatLng latLng = new LatLng(39.906901,116.397972);
        final Marker marker = aMap.addMarker(new MarkerOptions().position(latLng).title("北京").snippet("DefaultMarker"));
        // 定义 Marker拖拽的监听
        marker.isDraggable();
        marker.setDraggable(true);
        Log.i("bslc","bslc_MapsActivity_OnCreate_markerisDraggable"+marker.isDraggable());
        AMap.OnMarkerDragListener markerDragListener = new AMap.OnMarkerDragListener() {

            // 当marker开始被拖动时回调此方法, 这个marker的位置可以通过getPosition()方法返回。
            // 这个位置可能与拖动的之前的marker位置不一样。
            // marker 被拖动的marker对象。
            @Override
            public void onMarkerDragStart(Marker arg0) {
                // TODO Auto-generated method stub

            }

            // 在marker拖动完成后回调此方法, 这个marker的位置可以通过getPosition()方法返回。
            // 这个位置可能与拖动的之前的marker位置不一样。
            // marker 被拖动的marker对象。
            @Override
            public void onMarkerDragEnd(Marker arg0) {
                // TODO Auto-generated method stub
                position = marker.getPosition();
                Log.i("bslc","bslc_MapsActivity_OnCreate_position is "+position.toString());
            }

            // 在marker拖动过程中回调此方法, 这个marker的位置可以通过getPosition()方法返回。
            // 这个位置可能与拖动的之前的marker位置不一样。
            // marker 被拖动的marker对象。
            @Override
            public void onMarkerDrag(Marker arg0) {
                // TODO Auto-generated method stub

            }
        };


// 绑定marker拖拽事件
        aMap.setOnMarkerDragListener(markerDragListener);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, fillList.class);
        intent.putExtra(EXTRA_MESSAGE, position.toString());
        startActivity(intent);
    }

}
