package com.example.juliuscassin.photoalbum;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Julius Cassin on 5/3/2016.
 */
public class TagEdit extends AppCompatActivity{

    //xml elements
    private TextView tagTextView;

    //backend
    private Photo photo;
    private String albumName;

    //to identify if a person or location tag is to be edited
    private int TAG_SELECTED=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addtag);

        //initalize xml elements
        tagTextView = (TextView)findViewById(R.id.tag_textview);

        //Check if anything was sent in bundle
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        //set up backend photo
        albumName = bundle.getString(Login.ALBUM_NAME_KEY);
        String photoIdentifier = bundle.getString(OpenAlbum.PHOTO_IDENTIFIER_KEY);
        photo = UserInfo.getInstance().getAlbum(albumName).getPhoto(photoIdentifier);
        String tag = bundle.getString(Display.TAG_KEY);
        //set tag text to tag if it exists
        if(tag != null){
            tagTextView.setText(tag);
        }
        TAG_SELECTED = bundle.getInt(Display.WHICH_TAG_KEY);
    }

    public void saveTag(View view){
        String tagEntered = tagTextView.getText().toString();
        //User didn't enter a tag
        if(tagEntered == null || tagEntered.length() == 0){
            Toast.makeText(this, "Tag name is required.", Toast.LENGTH_SHORT).show();
            return;
        }
        //figure out which tag is being edited
        if(TAG_SELECTED == Display.PERSON_OPTION) {
            photo.setPersonTag(tagEntered);
        }
        else if(TAG_SELECTED == Display.LOCATION_OPTION){
            photo.setLocationTag(tagEntered);
        }
        Context context =getApplicationContext();
        UserInfo.getInstance().store(context);
        //put information needed to start displa in bundle and exit
        Intent intent = new Intent(this,Display.class);
        Bundle bundle = new Bundle();
        bundle.putString(Login.ALBUM_NAME_KEY,albumName);
        bundle.putString(OpenAlbum.PHOTO_IDENTIFIER_KEY,photo.pathName);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    //when user presses cancel
    public void cancelTag(View view){
        Intent intent = new Intent(this,Display.class);
        Bundle bundle = new Bundle();
        bundle.putString(Login.ALBUM_NAME_KEY,albumName);
        bundle.putString(OpenAlbum.PHOTO_IDENTIFIER_KEY,photo.pathName);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
