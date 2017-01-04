package com.example.juliuscassin.photoalbum;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Julius Cassin on 5/3/2016.
 */
public class Display extends AppCompatActivity {

    //backend
    private String albumName;
    private Photo photo;

    //get xml elements
    private ImageView imageView;
    private TextView personTextView;
    private TextView locationTextView;

    //keys For tag edit
    public static final String TAG_KEY="TAG_KEY";
    public static final String WHICH_TAG_KEY="WHICH_TAG_KEY";
    public static final int PERSON_OPTION=1;
    public static final int LOCATION_OPTION=2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set what user sees
        setContentView(R.layout.display);

        //unpack bundle to get albumname and photo
        Bundle bundle = getIntent().getExtras();
        albumName = bundle.getString(Login.ALBUM_NAME_KEY);
        String photoIndentifier = bundle.getString(OpenAlbum.PHOTO_IDENTIFIER_KEY);
        Album album = UserInfo.getInstance().getAlbum(albumName);
        photo = album.getPhoto(photoIndentifier);
        //xml element stuff
        //get imageview
        imageView = (ImageView)findViewById(R.id.imageView);
        //set imageView
        imageView.setImageBitmap(photo.bitMap);
        //get textviews for tags
        personTextView = (TextView)findViewById(R.id.personTextView);
        locationTextView = (TextView)findViewById(R.id.locationTextView);
        //set text views
        if(photo.person!=null){
            personTextView.setText(photo.person);
        }
        if(photo.location!=null){
            locationTextView.setText(photo.location);
        }
    }
    //when user clicks add for person tag
    public void editPersonTag(View view){
        //Send info to tag edit
        Intent intent= new Intent(this, TagEdit.class);
        Bundle bundle = new Bundle();
        bundle.putString(OpenAlbum.PHOTO_IDENTIFIER_KEY,photo.pathName);
        bundle.putString(Login.ALBUM_NAME_KEY,albumName);
        if(photo.person != null){
            bundle.putString(TAG_KEY, photo.person);
        }
        bundle.putInt(WHICH_TAG_KEY,PERSON_OPTION);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    //when user clicks delete for person tag
    public void deletePersonTag(View view){
        photo.person = null;
        Context context =getApplicationContext();
        UserInfo.getInstance().store(context);
        personTextView.setText("No Tag");
    }

    //when user clicks add for location tag
    public void editLocationTag(View view){
        //Send info to tag edit
        Intent intent= new Intent(this, TagEdit.class);
        Bundle bundle = new Bundle();
        bundle.putString(OpenAlbum.PHOTO_IDENTIFIER_KEY,photo.pathName);
        bundle.putString(Login.ALBUM_NAME_KEY,albumName);
        if(photo.person != null){
            bundle.putString(TAG_KEY, photo.location);
        }
        bundle.putInt(WHICH_TAG_KEY,LOCATION_OPTION);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    //when user clicks delete for location tag
    public void deleteLocationTag(View view){
        photo.location = null;
        Context context =getApplicationContext();
        UserInfo.getInstance().store(context);
        personTextView.setText("No Tag");
    }

    //when user clicks go to album
    public void goToAlbum(View view){
        Intent intent = new Intent(this, OpenAlbum.class);
        Bundle bundle = new Bundle();
        bundle.putString(Login.ALBUM_NAME_KEY,albumName);
        intent.putExtras(bundle);
        startActivity(intent);
    }

}
