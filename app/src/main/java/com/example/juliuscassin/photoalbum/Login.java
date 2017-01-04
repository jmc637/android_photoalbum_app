package com.example.juliuscassin.photoalbum;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/*
IMPORTANT NOTICE

I forgot that in android you use the back button to navigate and screens are stored on a stack. I instead implemented buttons that navigate between the different parts of the app. Most of the testing I've done is using the buttons I created. I do not know if the app will malfunction if the back button is used for all cases.
The second thing is that I was not able to figure out how to save images in storage. The app saves stuff to a file but can not load things from file.
Everything else seems to work.

IMPORTANT NOTICE
*/



public class Login extends AppCompatActivity {

    //album selected by user is stored here
    private Album albumSelected = null;
    private ListView listView = null;
    private UserInfo userInfo = null;

    public static final String NAME_KEY = "NAME";
    public static final String OPTION_KEY= "OPTION_KEY";
    public static final String ALBUM_NAME_KEY="ALBUM_NAME_KEY";
    public static final int ADD_ALBUM_CODE=1;
    public static final int RENAME_ALBUM_CODE=2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set what user sees
        setContentView(R.layout.login);

        userInfo = UserInfo.getInstance();

        /*
        userInfo = UserInfo.clearAndGetInstance();
        //load stuff
        Context context = getApplicationContext();

        Couldn't get load to work properly
        try {
            userInfo.load(context,this);
        }
        catch(java.io.IOException e){
            Toast.makeText(this,"Could not properly load data.",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        */

        //get list view
        listView = (ListView) findViewById(R.id.listView);
        //put sutff in listview
        listView.setAdapter(new ArrayAdapter<Album>(this, R.layout.album, userInfo.getAlbums()));
        //When item in listview is clicked light up and also keep track of selected item
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
                view.setSelected(true);
                albumSelected = (Album)parent.getItemAtPosition(position);
            }
        });
    }

    //User clicked add album
    public void add(View view){
        Intent intent = new Intent(this, AddAlbum.class);
        Bundle bundle = new Bundle();
        bundle.putInt(OPTION_KEY,ADD_ALBUM_CODE);
        intent.putExtras(bundle);
        startActivityForResult(intent, ADD_ALBUM_CODE);
    }

    //User clicked rename
    public void rename(View view){
        //check if user selected an album
        if(albumSelected==null){
            albumSelectToast();
            return;
        }

        //Send album string name to add album
        Intent intent= new Intent(this, AddAlbum.class);
        Bundle bundle = new Bundle();
        bundle.putString(NAME_KEY, albumSelected.toString());
        bundle.putInt(OPTION_KEY,RENAME_ALBUM_CODE);
        intent.putExtras(bundle);
        startActivityForResult(intent, RENAME_ALBUM_CODE);
    }

    //User clicked delete
    public void delete(View view){
        //check if user selected an album
        if(albumSelected==null){
            albumSelectToast();
            return;
        }
        userInfo.deleteAlbum(albumSelected);
        Context context =getApplicationContext();
        UserInfo.getInstance().store(context);
        listView.setAdapter(new ArrayAdapter<Album>(this, R.layout.album, userInfo.getAlbums()));
        albumSelected = null;
    }

    //USer clicked open
    public void open(View view){
        //check if user selected an album
        if(albumSelected==null){
            albumSelectToast();
            return;
        }

        Intent intent= new Intent(this, OpenAlbum.class);
        Bundle bundle = new Bundle();
        bundle.putString(Login.ALBUM_NAME_KEY, albumSelected.toString());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    //User clicked search photos
    public void searchPhotos(View view){
        Intent intent = new Intent(this,Search.class);
        startActivity(intent);
    }

    //Things to be done when an activity returns
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode != RESULT_OK) { return; }
        listView.setAdapter(new ArrayAdapter<Album>(this, R.layout.album, userInfo.getAlbums()));
        albumSelected = null;
    }

    //test to display when user doesn't pick an album
    public void albumSelectToast(){
        Toast
                .makeText(this, "Need to Select an Album", Toast.LENGTH_SHORT)
                .show();
    }
}
