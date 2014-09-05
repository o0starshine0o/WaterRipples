package test.starshine.com.demowaterripples.activity;

import android.os.Bundle;
import android.app.Activity;
import android.util.DisplayMetrics;

import test.starshine.com.demowaterripples.R;
import test.starshine.com.demowaterripples.view.WaterRipplesView;

public class MainActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        // All options in Configuration are optional. Use only those you really want to customize.
        ((WaterRipplesView) findViewById(R.id.wrv))
                .paintWidth(3)
                .duringTime(3000)
                .maxRadius(100)
                .minRadius(40)
                .canTouch(true)
                .x(dm.widthPixels / 2)
                .y(dm.heightPixels / 2)
                .multiply(2.0f)
                .newTime(1000)
                .beginTransparent(128)
                .backgroundResource(R.drawable.bg_galaxy)
                .refreshTime(50)
                .endTransparent(0)
                .transparentTransition(255)
                .transparentTransitionRadius(80)
                .start();
    }
}
