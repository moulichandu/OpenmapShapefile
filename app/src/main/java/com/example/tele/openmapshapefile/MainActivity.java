package com.example.tele.openmapshapefile;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.bbn.openmap.dataAccess.shape.ShapeUtils;
import com.bbn.openmap.layer.shape.ESRIPoly;
import com.bbn.openmap.layer.shape.ESRIPolygonRecord;
import com.bbn.openmap.layer.shape.ESRIRecord;
import com.bbn.openmap.layer.shape.ShapeFile;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import static com.example.tele.openmapshapefile.R.id.map;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    DbfInputStream2 dbfInputStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(map);
        if (isStoragePermissionGranted())
            mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        try {

            File file = new File(getfile("cb_2015_us_cd114_500k.shp"));

            if (file.exists()) {
                Toast.makeText(getApplicationContext(), "File exists",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "File not exists @@@@@",
                        Toast.LENGTH_LONG).show();
                return;
            }

            ShapeFile shapeFile = new ShapeFile(file);

            FileInputStream fileInputStream = new FileInputStream(new File(getfile("cb_2015_us_cd114_500k.dbf")));
            dbfInputStream = new DbfInputStream2(fileInputStream);

            //  Log.v("myapp","row 200, column 4 = "+ ((ArrayList)dbfInputStream.getRecords().get(1)).get(2));
            Log.v("myapp", "row 200, column 0 = " + ((ArrayList) dbfInputStream.getRecords().get(0)).get(0));
            Log.v("myapp", "row 200, column 1 = " + ((ArrayList) dbfInputStream.getRecords().get(0)).get(1));
            Log.v("myapp", "row 200, column 2 = " + ((ArrayList) dbfInputStream.getRecords().get(0)).get(2));
            Log.v("myapp", "row 200, column 3 = " + ((ArrayList) dbfInputStream.getRecords().get(0)).get(3));
            Log.v("myapp", "row 200, column 4 = " + ((ArrayList) dbfInputStream.getRecords().get(0)).get(4));
            Log.v("myapp", "row 200, column 5 = " + ((ArrayList) dbfInputStream.getRecords().get(0)).get(5));
            Log.v("myapp", "row 200, column 6 = " + ((ArrayList) dbfInputStream.getRecords().get(0)).get(6));
            Log.v("myapp", "row 200, column 7 = " + ((ArrayList) dbfInputStream.getRecords().get(0)).get(7));
          //  Log.v("myapp", "row 200, column 8 = " + ((ArrayList) dbfInputStream.getRecords().get(0)).get(8));
          //  Log.v("myapp", "row 200, column 9 = " + ((ArrayList) dbfInputStream.getRecords().get(0)).get(9));
          //  Log.v("myapp", "row 200, column 10 = " + ((ArrayList) dbfInputStream.getRecords().get(0)).get(10));
          //  Log.v("myapp", "row 200, column 11 = " + ((ArrayList) dbfInputStream.getRecords().get(0)).get(11));




            for(int k=0;k<((ArrayList) dbfInputStream.getRecords()).size();k++)
            {
                ESRIRecord esriRecord = shapeFile.getNextRecord();
                if (String.valueOf(((ArrayList) dbfInputStream.getRecords().get(k)).get(0)).equals("36")) {

                    ESRIPolygonRecord polyRec = (ESRIPolygonRecord) esriRecord;
                    //  Log.v("myapp", "number of polygon objects = " + polyRec.polygons.length);
                    for (int i = 0; i < polyRec.polygons.length; i++) {
                        // read for a few layers
                        ESRIPoly.ESRIFloatPoly poly = (ESRIPoly.ESRIFloatPoly) polyRec.polygons[i];
                        PolygonOptions polygonOptions = new PolygonOptions();
                        polygonOptions.strokeColor(Color.argb(150, 200, 0, 0));
                        polygonOptions.fillColor(Color.argb(150, 0, 0, 150));
                        polygonOptions.strokeWidth(2.0f);
                        // Log.v("myapp", "Points in the polygon = " + poly.nPoints);

                        for (int j = 0; j < poly.nPoints; j++) {
                            //Log.v("myapp",poly.getY(j) + "," + poly.getX(j));
                            polygonOptions.add(new LatLng(poly.getY(j), poly.getX(j)));
                        }
                        Log.v("myapp", "row 200, column 4 = "+" :"+ String.valueOf(((ArrayList) dbfInputStream.getRecords().get(esriRecord.getRecordNumber())).get(0)));
                        mMap.addPolygon(polygonOptions);
                        //  Log.v("myapp", "polygon added");
                    }
                }

            }




           /* int count=0;
            for (ESRIRecord esriRecord = shapeFile.getNextRecord(); esriRecord != null; esriRecord = shapeFile.getNextRecord()) {
                String shapeTypeStr = ShapeUtils.getStringForType(esriRecord.getShapeType());
                // Log.v("myapp", "shape type = " + esriRecord.getRecordNumber() + "-" + shapeTypeStr);

                // In QGIS, US is 208 but index starts from 0 so add 1



               // String str = String.valueOf(((ArrayList) dbfInputStream.getRecords().get(esriRecord.getRecordNumber())).get(0));
                // Log.v("myapp", "row 200, column 4 = " + str);
                if (shapeTypeStr.equals("POLYGON")) {

                    // cast type after checking the type
                    if (String.valueOf(((ArrayList) dbfInputStream.getRecords().get(esriRecord.getRecordNumber())).get(0)).equals("24")) {

                        ESRIPolygonRecord polyRec = (ESRIPolygonRecord) esriRecord;
                        //  Log.v("myapp", "number of polygon objects = " + polyRec.polygons.length);
                        for (int i = 0; i < polyRec.polygons.length; i++) {
                            // read for a few layers
                            ESRIPoly.ESRIFloatPoly poly = (ESRIPoly.ESRIFloatPoly) polyRec.polygons[i];
                            PolygonOptions polygonOptions = new PolygonOptions();
                            polygonOptions.strokeColor(Color.argb(150, 200, 0, 0));
                            polygonOptions.fillColor(Color.argb(150, 0, 0, 150));
                            polygonOptions.strokeWidth(2.0f);
                            // Log.v("myapp", "Points in the polygon = " + poly.nPoints);

                            for (int j = 0; j < poly.nPoints; j++) {
                                //Log.v("myapp",poly.getY(j) + "," + poly.getX(j));
                                polygonOptions.add(new LatLng(poly.getY(j), poly.getX(j)));
                            }
                            Log.v("myapp", "row 200, column 4 = "+ count+" :"+ String.valueOf(((ArrayList) dbfInputStream.getRecords().get(esriRecord.getRecordNumber())).get(0)));
                            mMap.addPolygon(polygonOptions);
                            //  Log.v("myapp", "polygon added");
                        }
                    }


                } else {
                    Log.v("myapp", "error polygon not found (type = " + esriRecord.getShapeType() + ")");
                }
                count+=count;
            }*/
            //  }


        } catch (Exception e) {
            e.printStackTrace();
            Log.v("myapp", "error=" + e);
        }


    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                //  Log.v(TAG, "Permission is granted");
                return true;
            } else {

                // Log.v(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            // Log.v(TAG, "Permission is granted");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Log.v(TAG, "Permission: " + permissions[0] + "was " + grantResults[0]);

            //resume tasks needing this permission
        }
    }

    public String getfile(String title) {

        File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/cb_2015_us_cd114_500k/" + title);

        return folder.getPath();

    }

    public boolean iswriteStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                //  Log.v(TAG, "Permission is granted");
                return true;
            } else {

                // Log.v(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            // Log.v(TAG, "Permission is granted");
            return true;
        }
    }


}