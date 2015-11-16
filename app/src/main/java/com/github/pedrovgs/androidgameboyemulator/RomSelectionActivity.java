package com.github.pedrovgs.androidgameboyemulator;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class RomSelectionActivity extends Activity {


    ListView LV;
    private ArrayAdapter<String> adapter;
    ArrayList<String> roms;
    File file = new File(Environment.getExternalStorageDirectory(), "Download");

    @Override
    protected void onCreate(Bundle savedInstanceState) { 
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rom_selection);
        LV = (ListView)findViewById(R.id.listView);
        if(file!=null) {
            File fileArray[] = file.listFiles();
            if(fileArray!=null) {
                Log.d("Files", "Size: " + fileArray.length);

                roms = new ArrayList<String>();
                for (int i = 0; i < fileArray.length; i++) {
                    Log.d("Files", "FileName:" + fileArray[i].getName());
                    if(fileArray[i].getName().contains(".gb")){
                        roms.add(fileArray[i].getName());
                    }
                }
                adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.listitemlayout, roms);
                LV.setAdapter(adapter);
            }
        }
        LV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Intent intent=new Intent();
                intent.putExtra("filepath",file.getAbsolutePath()+"/"+roms.get(position));
                setResult(2,intent);
                finish();
            }
        });
    }
}
