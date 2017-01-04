package com.example.juliuscassin.photoalbum;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Julius Cassin on 5/1/2016.
 */
public class AddAlbum extends AppCompatActivity {

    EditText albumName;

    private UserInfo userInfo;
    private int option;
    private String oldName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.albumnaming);

        albumName = (EditText)findViewById(R.id.album_name);

        //Check if anything was sent in bundle
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        option = bundle.getInt(Login.OPTION_KEY);
        if(option == Login.RENAME_ALBUM_CODE){
            String name = bundle.getString(Login.NAME_KEY);
            oldName = name;
            albumName.setText(name);
        }
    }

    // called when the user clicks the Save button
    public void save(View view) {
        //Name entered by user
        String name = albumName.getText().toString();
        //Name was not entered
        if (name == null || name.length() == 0) {
            Toast
                    .makeText(this, "Name is Required", Toast.LENGTH_SHORT)
                    .show();
            return;   // does not quit activity, just returns from method
        }
        //attempt to change backend data
        userInfo = UserInfo.getInstance();
        boolean result;
        //user wants to add album
        if(option == Login.ADD_ALBUM_CODE){
            result = userInfo.addAlbum(name);
        }
        //user wants to change album name
        else{
            result = userInfo.changeAlbumName(oldName,name);
        }
        //Name was duplicated
        if(result == false){
            Toast
                    .makeText(this, "Album Name Already in Use", Toast.LENGTH_SHORT)
                    .show();
            return;   // does not quit activity, just returns from method
        }

        Context context =getApplicationContext();
        userInfo.store(context);

        // successfully added
        setResult(RESULT_OK);
        finish(); // pops the activity from the call stack, returns to parent
    }

    public void cancel(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }
}
