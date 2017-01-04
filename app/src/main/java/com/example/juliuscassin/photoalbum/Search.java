package com.example.juliuscassin.photoalbum;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Julius Cassin on 5/3/2016.
 */
public class Search extends AppCompatActivity{

    //xml elements
    EditText editText_Person;
    EditText editText_Location;
    ListView search_listview;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        //get xml elements
        editText_Person = (EditText)findViewById(R.id.editText_Person);
        editText_Location = (EditText)findViewById(R.id.editText_Location);
        search_listview = (ListView)findViewById(R.id.search_listview);
        editText_Person.setImeOptions(EditorInfo.IME_ACTION_DONE);
        editText_Location.setImeOptions(EditorInfo.IME_ACTION_DONE);

    }

    //user clicked search for person
    public void person_search(View view){
        String person_tag = editText_Person.getText().toString();
        if(person_tag.length() == 0 || person_tag == null){
            Toast.makeText(this,"No Tag Entered",Toast.LENGTH_SHORT).show();
            return;
        }
        ArrayList<Photo> found = UserInfo.getInstance().searchByPerson(person_tag);
        if(found.isEmpty()){
            Toast.makeText(this,"No photos found.",Toast.LENGTH_SHORT).show();
        }
        setListView(found);
    }

    //user clicked search for location
    public void location_search(View view){
        String location_tag = editText_Location.getText().toString();
        if(location_tag.length() == 0 || location_tag == null){
            Toast.makeText(this,"No Tag Entered",Toast.LENGTH_SHORT).show();
            return;
        }
        ArrayList<Photo> found = UserInfo.getInstance().searchByLocation(location_tag);
        if(found.isEmpty()){
            Toast.makeText(this,"No photos found.",Toast.LENGTH_SHORT).show();
        }
        setListView(found);
    }

    //when user clicks back to albums
    public void backToAlbums(View view){
        Intent intent = new Intent(this,Login.class);
        startActivity(intent);
    }

    //set listview
    public void setListView(ArrayList<Photo> found){
        search_listview.setAdapter(new GridAdapter(this, R.layout.grid_item, found));
    }
}
