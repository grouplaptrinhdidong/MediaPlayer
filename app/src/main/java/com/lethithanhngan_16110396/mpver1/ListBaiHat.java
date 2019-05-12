package com.lethithanhngan_16110396.mpver1;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class ListBaiHat extends AppCompatActivity {
    ListView myListViewForSongs;
    String[] items;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_bai_hat);
        myListViewForSongs = (ListView) findViewById(R.id.mySongListView);
        display();
        //runtimePermission();
    }

    public ArrayList<File> findSong(File file){

        ArrayList<File> arrayList = new ArrayList<>();

        File[] files = file.listFiles();

        for(File singleFile: files){

            if (singleFile.isDirectory() && !singleFile.isHidden()){
                arrayList.addAll(findSong(singleFile));
            }
            else{
                if (singleFile.getName().endsWith(".mp3")){
                    arrayList.add(singleFile);
                }
            }
        }

        return arrayList;
    }

    void display(){

        final ArrayList<File> mySongs = findSong(Environment.getExternalStorageDirectory());
        items = new String[mySongs.size()];

        for (int i=0; i<mySongs.size(); i++){
            items[i] = mySongs.get(i).getName().toString().replace(".mp3","");

        }

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, items);
        myListViewForSongs.setAdapter(myAdapter);

        myListViewForSongs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String songName =  myListViewForSongs.getItemAtPosition(position).toString();

                startActivity(new Intent(getApplicationContext(), PlayerActivity.class).putExtra("songs",mySongs)
                        .putExtra("songname",songName)
                        .putExtra("pos", position));
            }
        });
    }
}
