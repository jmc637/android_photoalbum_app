package com.example.juliuscassin.photoalbum;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Julius Cassin on 5/3/2016.
 */
public class MovePhoto extends AppCompatActivity {

    //xml attributes
    ListView listView;

    //backend
    private Photo photoSelected;
    private Album album;
    private UserInfo userInfo;

    //album user choose
    private Album albumToMoveTo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set what user sees
        setContentView(R.layout.movephoto);
        //get xml attribute
        listView = (ListView) findViewById(R.id.list_view);

        //get backend info
        userInfo = UserInfo.getInstance();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        String albumName = bundle.getString(Login.ALBUM_NAME_KEY);
        album = userInfo.getAlbum(albumName);

        String photoIdentifier = bundle.getString(OpenAlbum.PHOTO_IDENTIFIER_KEY);
        photoSelected = album.getPhoto(photoIdentifier);

        //initialize listview to all albums except the album the picture is already from
        ArrayList<Album> newArrayList = new ArrayList<Album>(userInfo.getAlbums());
        //remove album from new list
        for(Album currentAlbum: newArrayList){
            if(currentAlbum.name.equals(album.name)){
                newArrayList.remove(currentAlbum);
                break;
            }
        }
        listView.setAdapter(new ArrayAdapter<Album>(this, R.layout.album, newArrayList));
        //set listener to get users choice
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
                view.setSelected(true);
                albumToMoveTo = (Album)parent.getItemAtPosition(position);
            }
        });
    }

    public void move(View view){
        //check if selected album is null
        if(albumToMoveTo == null){
            Toast.makeText(this,"Need to select an album.",Toast.LENGTH_SHORT).show();
            return;
        }
        //First try adding the photo to other album
        boolean result = albumToMoveTo.addPhoto(photoSelected);
        //successfully added now remove from other
        if(result == true){
            album.deletePhoto(photoSelected);
            Context context =getApplicationContext();
            UserInfo.getInstance().store(context);
            setResult(RESULT_OK);
            finish();
        }
        //couldn't move
        else{
            Toast.makeText(this,"Photo already exists in that album.",Toast.LENGTH_SHORT).show();
            return;
        }
    }

    public void cancelMove(View view){
        setResult(RESULT_CANCELED);
        finish();
    }
}
