package com.example.juliuscassin.photoalbum;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

/*
IMPORTANT NOTICE

I forgot that in android you use the back button to navigate and screens are stored on a stack. I instead implemented buttons that navigate between the different parts of the app. Most of the testing I've done is using the buttons I created. I do not know if the app will malfunction if the back button is used for all cases.
The second thing is that I was not able to figure out how to save images in storage. The app saves stuff to a file but can not load things from file.
Everything else seems to work.

IMPORTANT NOTICE
*/

public class UserInfo {

    //single instance
    private static UserInfo userInfo= null;

    // holds albums in an array list
    private ArrayList<Album> albums;

    //constructor
    private UserInfo(){
        albums = new ArrayList<Album>();
    }

    //user info instance returned
    public static UserInfo getInstance(){
        if(userInfo == null){
            userInfo = new UserInfo();
        }
        return userInfo;
    }

    //user info clear and reload
    public static UserInfo clearAndGetInstance(){
        userInfo = new UserInfo();
        return userInfo;
    }

    //returns the list of all albums
    public ArrayList<Album> getAlbums(){
        return albums;
    }

    //adds an album if name is not already used. If the name already exists returns false.
    public boolean addAlbum(String name){
        for(Album album: albums){
            if(album.toString().equalsIgnoreCase(name)){
                //Album of that name already exists
                return false;
            }
        }
        //No duplicate add the album
        albums.add(new Album(name));
        return true;
    }
    //changes a name of an album given old album name
    public boolean changeAlbumName(String oldName, String newName){
        //check if name already exists
        for(Album album: albums){
            if(album.name.equalsIgnoreCase(newName)) {
                return false;
            }
        }
        //change album name
        for(Album target: albums){
            if(target.name.equalsIgnoreCase(oldName)) {
                target.name = newName;
            }
        }
        return true;
    }
    //deletes an album
    public void deleteAlbum(Album album){
        albums.remove(album);
    }

    //get an album you know name of
    public Album getAlbum(String name){
        for(Album album: albums){
            if(album.toString().equalsIgnoreCase(name)){
                //Album found
                return album;
            }
        }
        //Should never get here
       return null;
    }

    //Add Photo to Album. Returns false if photo already exists in that album
    public boolean addPhotoToAlbum(String pathName, Bitmap bitmap, String albumName){
        Album album = getAlbum(albumName);
        Photo newPhoto= new Photo(pathName, bitmap);
        //true returned if added and false returned if duplicate already exists
        boolean result = album.addPhoto(newPhoto);
        return result;
    }

    //delete a known photo from the album
    public void deletePhotoFromAlbum(String albumName, Photo photo){
        Album album = getAlbum(albumName);
        album.deletePhoto(photo);
    }

    //search by person
    public ArrayList<Photo> searchByPerson(String targetPerson){
        ArrayList<Photo> matchingPhotos = new ArrayList<Photo>();
        for(Album album: albums){
            for(Photo photo: album.photos){
                if(photo.person != null){
                    if (containsTag(photo.person, targetPerson)) {
                        matchingPhotos.add(photo);
                    }
                }
            }
        }
        return matchingPhotos;
    }

    //search by location
    public ArrayList<Photo> searchByLocation(String targetLocation){
        ArrayList<Photo> matchingPhotos = new ArrayList<Photo>();
        for(Album album: albums){
            for(Photo photo: album.photos){
                if(photo.location != null){
                    if (containsTag(photo.location, targetLocation)) {
                        matchingPhotos.add(photo);
                    }
                }
            }
        }
        return matchingPhotos;
    }

    //see if tag matches photo as indicated by directions
    public static boolean containsTag(String photoTag, String targetTag){
        if(photoTag == null || targetTag == null){
            return false;
        }
        String lowerPhotoTag = photoTag.toLowerCase();
        String lowerTargetTag = targetTag.toLowerCase();
        if(lowerPhotoTag.equals(lowerTargetTag)){
            return true;
        }
        else if(lowerPhotoTag.startsWith(lowerTargetTag + " ")){
            return true;
        }
        else if(lowerPhotoTag.endsWith(" " + lowerTargetTag)){
            return true;
        }
        else if(lowerPhotoTag.contains(" " + lowerTargetTag + " ")){
            return true;
        }
        else{
            return false;
        }
    }

    //store all data in a file for later boot up
    public void store(Context context){
        try {
            FileOutputStream fos = context.openFileOutput("DATA_FILE.dat", Context.MODE_PRIVATE);
            PrintWriter pw = new PrintWriter(fos);
            for (Album album : albums) {
                pw.println(album.getString());
                for (Photo photo : album.photos) {
                    pw.println(photo.getString());
                }
            }
            pw.close();
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }

    public void load(Context context, Login login) throws java.io.IOException{;
        try {
            FileInputStream fis = context.openFileInput("DATA_FILE.dat");
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            String info;
            Album currAlbum = null;
            while ((info = br.readLine()) != null) {
                if(info.equals("album")){
                    info = br.readLine();
                    Album albumToAdd = parseAlbum(info);
                    albums.add(albumToAdd);
                    currAlbum = albumToAdd;
                }
                /*
                else if(info.equals("photo")){
                    info = br.readLine();
                    Photo photoToAdd = parsePhoto(info, login);
                    if(photoToAdd != null){
                        currAlbum.addPhoto(photoToAdd);
                    }
                }
                */
            }
            fis.close();

        } catch (FileNotFoundException e) { // default to initial set
            e.printStackTrace();
        }
    }

    public Album parseAlbum(String info){
        return new Album(info);
    }


    /*
    public Photo parsePhoto(String info, Login login){
        String[] items = info.split("|");

        String path = items[0];

        Uri uri = Uri.fromFile(new File(path));
        try {
            InputStream inputStream = login.getContentResolver().openInputStream(uri);
            //gets bitmap from stream
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            String personTag = items[1];
            if(personTag.equals("null")){
                personTag = null;
            }

            String locationTag = items[2];
            if(locationTag.equals("null")){
                locationTag = null;
            }
            return new Photo(path, bitmap,personTag,locationTag);
        }
        catch(java.io.FileNotFoundException e){
            e.printStackTrace();
        }
        return null;
    }
    */
}