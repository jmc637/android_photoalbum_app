package com.example.juliuscassin.photoalbum;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;


/**
 * Created by Julius Cassin on 5/1/2016.
 */
public class OpenAlbum extends AppCompatActivity {

    //Keys to identify activities
    public final static int ADD_PHOTO_CODE = 3;
    public final static int MOVE_PHOTO_CODE=4;

    //xml elements
    private GridView gridView = null;

    //bakend
    private String albumName;
    private UserInfo userInfo = null;
    private Photo photoSelected = null;

    //keys for data to be used in display
    public static final String PHOTO_IDENTIFIER_KEY = "PHOTO_INDENTIFIER_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set what user sees
        setContentView(R.layout.openalbum);
        //get xml attribute
        gridView = (GridView) findViewById(R.id.gridView);
        //get things passed from login
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        albumName = bundle.getString(Login.ALBUM_NAME_KEY);
        //Get backend data reference
        userInfo = UserInfo.getInstance();
        //initialize gridview
        resetGridView();
        //Set listener if grid view is clicked
        //When item in listview is clicked light up and also keep track of selected item
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
                view.setSelected(true);
                photoSelected = (Photo) parent.getItemAtPosition(position);
            }
        });
    }

    //When add pic is clicked
    public void addPicture(View view){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        //Where to get data
        File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String pictureDirectoryPath = pictureDirectory.getPath();
        //uri representation
        Uri data = Uri.parse(pictureDirectoryPath);

        //set data and type
        intent.setDataAndType(data,"image/*");
        startActivityForResult(intent, ADD_PHOTO_CODE);
    }

    //When delete picture is clicked
    public void deletePicture(View view){
        if(photoSelected==null){
            photoSelectToast();
            return;
        }
        userInfo.deletePhotoFromAlbum(albumName,photoSelected);
        photoSelected = null;
        resetGridView();
    }

    //When go to albums is clicked
    public void goToAlbums(View view){
        Intent intent= new Intent(this, Login.class);
        startActivity(intent);
    }

    //When display is clicked
    public void display(View view){
        if(photoSelected == null){
            photoSelectToast();
            return;
        }
        //Need to send albumname and filepath of photo to retrieve those from backend
        Intent intent = new Intent(this, Display.class);
        Bundle bundle = new Bundle();
        bundle.putString(Login.ALBUM_NAME_KEY,albumName);
        bundle.putString(PHOTO_IDENTIFIER_KEY,photoSelected.pathName);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    //When move photo is clicked
    public void movePhoto(View view){
        //no photo selected
        if(photoSelected == null){
            photoSelectToast();
            return;
        }
        //only one album so can't move
        if(UserInfo.getInstance().getAlbums().size() == 1){
            Toast.makeText(this, "Can't move. Only one album.", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this, MovePhoto.class);
        //need to send photo identifier and also albumname
        Bundle bundle = new Bundle();
        bundle.putString(PHOTO_IDENTIFIER_KEY, photoSelected.pathName);
        bundle.putString(Login.ALBUM_NAME_KEY, albumName);
        intent.putExtras(bundle);
        startActivityForResult(intent,MOVE_PHOTO_CODE);
    }

    //Process when activities return and send back data
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode != RESULT_OK){
            return;
        }
        else if(requestCode == ADD_PHOTO_CODE){
            //Address of the image on the SD card
            Uri imageUri = intent.getData();
            //Reads data from URI
            try {
                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                //gets bitmap from stream
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                //store bitmap and uri in a photo to album
                boolean result = userInfo.addPhotoToAlbum(imageUri.getPath(), bitmap, albumName);
                //Photo does not exists update gridview
                if(result == true){
                    Context context =getApplicationContext();
                    UserInfo.getInstance().store(context);
                    resetGridView();
                    photoSelected=null;
                }
                //Photo already exists in album
                else{
                    Toast.makeText(this, "Photo already in album.", Toast.LENGTH_SHORT).show();
                    photoSelected=null;
                }
            }
            catch(FileNotFoundException e){
                e.printStackTrace();
                Toast.makeText(this,"Can't open image.",Toast.LENGTH_SHORT).show();
                photoSelected=null;
            }

        }
        else if(requestCode == MOVE_PHOTO_CODE){
            resetGridView();
            photoSelected=null;
        }
    }

    //Resets grid with photos in album
    public void resetGridView(){
        gridView.setAdapter(new GridAdapter(this, R.layout.grid_item, userInfo.getAlbum(albumName).photos));
    }
    //Toast to tell user they must select a photo
    public void photoSelectToast(){
        Toast
                .makeText(this, "Need to Select a Photo", Toast.LENGTH_SHORT)
                .show();
    }
}
