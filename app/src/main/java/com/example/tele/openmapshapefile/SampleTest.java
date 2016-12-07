package com.example.tele.openmapshapefile;

/**
 * diewald_shapeFileReader.
 * <p>
 * a Java Library for reading ESRI-shapeFiles (*.shp, *.dfb, *.shx).
 * <p>
 * <p>
 * Copyright (c) 2012 Thomas Diewald
 * <p>
 * <p>
 * This source is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * <p>
 * This code is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * <p>
 * A copy of the GNU General Public License is available on the World
 * Wide Web at <http://www.gnu.org/copyleft/gpl.html>. You can also
 * obtain it by writing to the Free Software Foundation,
 * Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 */




import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import java.io.File;
import java.security.Permission;
import java.util.jar.Manifest;

import diewald_shapeFile.files.dbf.DBF_Field;
import diewald_shapeFile.files.dbf.DBF_File;
import diewald_shapeFile.files.shp.SHP_File;
import diewald_shapeFile.files.shp.shapeTypes.ShpPolygon;
import diewald_shapeFile.files.shp.shapeTypes.ShpShape;
import diewald_shapeFile.files.shx.SHX_File;
import diewald_shapeFile.shapeFile.ShapeFile;

public class SampleTest extends FragmentActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

            DBF_File.LOG_INFO = !false;
            DBF_File.LOG_ONLOAD_HEADER = false;
            DBF_File.LOG_ONLOAD_CONTENT = false;

            SHX_File.LOG_INFO = !false;
            SHX_File.LOG_ONLOAD_HEADER = false;
            SHX_File.LOG_ONLOAD_CONTENT = false;

            SHP_File.LOG_INFO = !false;
            SHP_File.LOG_ONLOAD_HEADER = false;
            SHP_File.LOG_ONLOAD_CONTENT = false;




        if(android.Manifest.permission.READ_EXTERNAL_STORAGE.equals(PackageManager.PERMISSION_GRANTED))
        {
            try {
                // GET DIRECTORY
                // String curDir = System.getProperty("user.dir");
                //  String folder = "/data/Gis Steiermark 2010/Bezirke/BezirkeUTM33N/";

                // LOAD SHAPE FILE (.shp, .shx, .dbf)
                ShapeFile shapefile = new ShapeFile(getfile(), "cb_2015_us_cd114_500k").READ();

                // TEST: printing some content
                ShpShape.Type shape_type = shapefile.getSHP_shapeType();
                System.out.println("\nshape_type = " + shape_type);

                int number_of_shapes = shapefile.getSHP_shapeCount();
                int number_of_fields = shapefile.getDBF_fieldCount();
                System.out.println("\nshape_type = " + number_of_shapes+"------>"+number_of_fields);

                for (int i = 0; i < number_of_shapes; i++) {

                    ShpPolygon shape = shapefile.getSHP_shape(i);
                    String[] shape_info = shapefile.getDBF_record(i);

                    ShpShape.Type type = shape.getShapeType();
                    int number_of_vertices = shape.getNumberOfPoints();
                    int number_of_polygons = shape.getNumberOfParts();
                    int record_number = shape.getRecordNumber();
                    //  shape.
                    System.out.println("Dataaaaaaaa @@@@"+String.valueOf(shape.getPoints()));

                    System.out.printf("\nSHAPE[%2d] - %s\n", i, type);
                    System.out.printf("  (shape-info) record_number = %3d; vertices = %6d; polygons = %2d\n", record_number, number_of_vertices, number_of_polygons);

                    for (int j = 0; j < number_of_fields; j++) {
                        String data = shape_info[j].trim();
                        DBF_Field field = shapefile.getDBF_field(j);
                        String field_name = field.getName();
                        System.out.printf("  (dbase-info) [%d] %s = %s", j, field_name, data);


                    }
                    System.out.printf("\n");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else
        {
            requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},200);
        }



    }

    public String getfile() {

        File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/cb_2015_us_cd114_500k");

        return folder.getPath();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        try {
            // GET DIRECTORY
            // String curDir = System.getProperty("user.dir");
            //  String folder = "/data/Gis Steiermark 2010/Bezirke/BezirkeUTM33N/";

            // LOAD SHAPE FILE (.shp, .shx, .dbf)
            ShapeFile shapefile = new ShapeFile(getfile(), "cb_2015_us_cd114_500k").READ();

            // TEST: printing some content
            ShpShape.Type shape_type = shapefile.getSHP_shapeType();
            System.out.println("\nshape_type = " + shape_type);

            int number_of_shapes = shapefile.getSHP_shapeCount();
            int number_of_fields = shapefile.getDBF_fieldCount();
            System.out.println("\nshape_type = " + number_of_shapes+"------>"+number_of_fields);

            for (int i = 0; i < number_of_shapes; i++) {

                ShpPolygon shape = shapefile.getSHP_shape(i);
                String[] shape_info = shapefile.getDBF_record(i);

                ShpShape.Type type = shape.getShapeType();
                int number_of_vertices = shape.getNumberOfPoints();
                int number_of_polygons = shape.getNumberOfParts();
                int record_number = shape.getRecordNumber();
                //  shape.
               // System.out.println("Dataaaaaaaa @@@@"+(shape.getPoints()));

               // System.out.printf("\nSHAPE[%2d] - %s\n", i, type);
               // System.out.printf("  (shape-info) record_number = %3d; vertices = %6d; polygons = %2d\n", record_number, number_of_vertices, number_of_polygons);

                for (int j = 0; j < number_of_fields; j++) {

                    String data = shape_info[j].trim();
                    DBF_Field field = shapefile.getDBF_field(j);

                    String field_name = field.getName();
                    System.out.println(" field.getName(); "+ field.getName());



                }
                System.out.printf("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}