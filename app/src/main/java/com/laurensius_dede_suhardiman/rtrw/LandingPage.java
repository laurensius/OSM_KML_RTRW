package com.laurensius_dede_suhardiman.rtrw;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.kml.KmlDocument;
import org.osmdroid.bonuspack.kml.KmlFeature;
import org.osmdroid.bonuspack.kml.KmlLineString;
import org.osmdroid.bonuspack.kml.KmlPlacemark;
import org.osmdroid.bonuspack.kml.KmlPoint;
import org.osmdroid.bonuspack.kml.KmlPolygon;
import org.osmdroid.bonuspack.kml.KmlTrack;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.FolderOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.Polygon;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;

import java.io.InputStream;

public class LandingPage extends AppCompatActivity {

    private MapView mvPeta = null;
    private ScaleBarOverlay mScaleBarOverlay;
    private CompassOverlay mCompassOverlay;
    private IMapController mapController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context context = getApplicationContext();
        Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context));
        setContentView(R.layout.activity_landing_page);
        mvPeta = (MapView)findViewById(R.id.mv_peta);
        mvPeta.setTileSource(TileSourceFactory.MAPNIK);
        mvPeta.setBuiltInZoomControls(true);
        mvPeta.setMultiTouchControls(true);
        //Zoom dan Center
        mapController = mvPeta.getController();
        mapController.setZoom(9);
        GeoPoint startPoint = new GeoPoint(6.8754, -9.6434);
        mapController.setCenter(startPoint);

        //Kontrol Skala
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        mScaleBarOverlay = new ScaleBarOverlay(mvPeta);
        mScaleBarOverlay.setCentred(true);
        mScaleBarOverlay.setScaleBarOffset(width / 2, 10);
        mvPeta.getOverlays().add(mScaleBarOverlay);
        //Kompas
        mCompassOverlay = new CompassOverlay(context, new InternalCompassOrientationProvider(context), mvPeta);
        mCompassOverlay.enableCompass();
        mvPeta.getOverlays().add(mCompassOverlay);
        mvPeta.invalidate();
        //MarkerOverlay
//        Marker startMarker = new Marker(mvPeta);
//        startMarker.setPosition(startPoint);
//        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
//        mvPeta.getOverlays().add(startMarker);
//        mvPeta.invalidate();
        //
        KmlDocument kmlDocument = new KmlDocument();
        AssetManager assetManager = getAssets();
        InputStream isReadKmlBWK;
        InputStream isReadKml;
        try {
//            streamDeKml = assetManager.open("raw/bagian_wilayah_kota.kml");
            isReadKmlBWK = getResources().openRawResource(R.raw.bagian_wilayah_kota);
            kmlDocument.parseKMLStream(isReadKmlBWK, null);
            isReadKmlBWK.close();

            BoundingBox bb = kmlDocument.mKmlRoot.getBoundingBox();
            mvPeta.zoomToBoundingBox(bb,true);
//            mvPeta.getController().setCenter(bb.getCenter());
            mapController.setCenter(bb.getCenter());
//            KmlFeature.Styler styler = new MyKmlStyler();
//            FolderOverlay kmlOverlay = (FolderOverlay)kmlDocument.mKmlRoot.buildOverlay(mvPeta, null, styler, kmlDocument);
            FolderOverlay kmlOverlay = (FolderOverlay)kmlDocument.mKmlRoot.buildOverlay(mvPeta, null, null, kmlDocument);
            mvPeta.getOverlays().add(kmlOverlay);
            mvPeta.invalidate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        //mvPeta.onResume();

    }

    @Override
    public void onPause(){
        super.onPause();
//        mvPeta.onPause();
    }


//    class MyKmlStyler implements KmlFeature.Styler {
//        @Override
//        public void onPoint(Marker marker, KmlPlacemark kmlPlacemark, KmlPoint kmlPoint) {}
//
//        @Override
//        public void onLineString(Polyline polyline, KmlPlacemark kmlPlacemark, KmlLineString kmlLineString){}
//
//        @Override
//        public void onFeature(Overlay overlay, KmlFeature kmlFeature) {}
//
//        @Override
//        public void onTrack(Polyline polyline, KmlPlacemark kmlPlacemark, KmlTrack kmlTrack) {}
//
//        @Override
//        public void onPolygon(Polygon polygone, KmlPlacemark kmlPlacemark, KmlPolygon kmlPolygon) {
//            Log.d("TESTING_1 :",kmlPlacemark.getExtendedDataAsText());
//            Log.d("TESTING_2 :",kmlPlacemark.getExtendedData("BWK").toString());
//            if(kmlPlacemark.getExtendedData("BWK").equals("BWK I")){
//                polygone.setFillColor(Color.parseColor("#CC110000"));
//
//            }
//        }
//    }
}
