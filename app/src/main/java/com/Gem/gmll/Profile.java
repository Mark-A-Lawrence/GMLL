package com.Gem.gmll;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaCodec;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class Profile extends AppCompatActivity {



    public static final int PICK_IMAGE = 1;
    ImageView img;
    String filepath;
    String  key = "Suffering knows no words";
    ImageView selectedImage;
    Gallery gallery;
    Bitmap bi;
    PhotoViewAttacher mAttacher;
    int degree;
    FloatingActionButton fab;
    FloatingActionButton minus;
    PhotoView full_view ;
    FloatingActionButton export ;
    FloatingActionButton rotate ;
    FloatingActionButton delete ;
    FloatingActionButton grid;
    FloatingActionButton full_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

         fab = findViewById(R.id.fab);
         minus  = findViewById(R.id.minus);
         full_view = findViewById(R.id.full_view);
         export = findViewById(R.id.export);
         rotate = findViewById(R.id.rotate);
         delete = findViewById(R.id.delete);
         grid = findViewById(R.id.grid);
         full_btn = findViewById(R.id.full_btn);


        full_view.setVisibility(View.GONE);

        grid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

        full_btn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {
                try {


                    selectedImage.setVisibility(View.GONE);
                    gallery.setVisibility(View.GONE);
                    full_view.setVisibility(View.VISIBLE);
                    full_view.setImageBitmap(bi);
                    full_btn.setVisibility(View.GONE);
                    minus.setVisibility(View.VISIBLE);
                    fab.setVisibility(View.GONE);
                    export.setVisibility(View.VISIBLE);
                    rotate.setVisibility(View.VISIBLE);
                    delete.setVisibility(View.VISIBLE);

                }catch (Exception ex){
                    Toast.makeText(Profile.this, "Gallery is Empty", Toast.LENGTH_SHORT).show();
                }

            }
        });

        degree = 0;

        rotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                degree = degree +90;
                full_view.setRotation(degree);

            }
        });
        export.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {

                String decrypted_str = new File(filepath).getName().replace(".enc","");
                File decrypted = new File(Environment.getExternalStorageDirectory()+ File.separator+"GMLLex"+File.separator+decrypted_str);

                fileProcessor(Cipher.DECRYPT_MODE,key,new File(filepath),decrypted);
                Toast.makeText(Profile.this, filepath, Toast.LENGTH_SHORT).show();
                File del_file = new File(filepath);
                if(del_file.exists()) {
                     del_file.delete();
                     load();
                }

                selectedImage.setVisibility(View.VISIBLE);
                gallery.setVisibility(View.VISIBLE);
                full_view.setVisibility(View.GONE);
                full_view.setImageBitmap(bi);
                full_btn.setVisibility(View.VISIBLE);
                minus.setVisibility(View.GONE);
                fab.setVisibility(View.VISIBLE);
                export.setVisibility(View.GONE);
                rotate.setVisibility(View.GONE);
                delete.setVisibility(View.GONE);

                //deletefile(new File(filepath));

            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {
                selectedImage.setVisibility(View.VISIBLE);
                gallery.setVisibility(View.VISIBLE);
                full_view.setVisibility(View.GONE);
                full_view.setImageBitmap(bi);
                full_btn.setVisibility(View.VISIBLE);
                minus.setVisibility(View.GONE);
                fab.setVisibility(View.VISIBLE);
                export.setVisibility(View.GONE);
                rotate.setVisibility(View.GONE);
                delete.setVisibility(View.GONE);



            }
        });

        selectedImage=(ImageView)findViewById(R.id.imageView);

        load();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pickImage();

//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });

      //  load();


    }


    @SuppressLint("InlinedApi")
    public void pickImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }

    @SuppressLint("NewApi")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                //Display an error
                Toast.makeText(this, "it was empty", Toast.LENGTH_SHORT).show();
                return;
            } else {

                  ClipData clipData = data.getClipData();
                  if(clipData!=null){
                    for (int i=0;i<clipData.getItemCount();i++) {

                        Uri sec_data =Uri.parse(clipData.getItemAt(i).getUri().toString());

                        // Will return "image:x*"
                        String wholeID = DocumentsContract.getDocumentId(sec_data);

// Split at colon, use second item in the array
                        String id = wholeID.split(":")[1];

                        String[] column = { MediaStore.Images.Media.DATA };

// where id is equal to
                        String sel = MediaStore.Images.Media._ID + "=?";

                        Cursor cursor = getContentResolver().
                                query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                        column, sel, new String[]{ id }, null);

                        String filePat = "";

                        int columnIndex = cursor.getColumnIndex(column[0]);

                        if (cursor.moveToFirst()) {
                            filePat = cursor.getString(columnIndex);
                        }

                        cursor.close();
//............................................................................................. encrypt..................................................................
                        System.out.println(filePat);

                        try {
                            //InputStream inputStream = this.getContentResolver().openInputStream(data.getData());

                            File inputFile = new File(filePat);


                            // File inputFile = new File("text.txt");
                            File encryptedFile = new File(Environment.getExternalStorageDirectory() + File.separator + "GMLL" + File.separator + new File(filePat).getName() + ".enc");

                            try {


                                fileProcessor(Cipher.ENCRYPT_MODE, key, inputFile, encryptedFile);
                                load();
                                //  Bitmap bit = deccryptfile(Cipher.DECRYPT_MODE,key,new File(Environment.getExternalStorageDirectory()+ File.separator+"GMLL"+File.separator+"text.encrypted"));
                                // img.setImageBitmap(bit);


                                Toast.makeText(this, "Sucess", Toast.LENGTH_SHORT).show();
                            } catch (Exception ex) {
                                Toast.makeText(this, "Error"+ex.getMessage(), Toast.LENGTH_SHORT).show();
                                ex.printStackTrace();
                            }
                            // getRealPathFromURI(getApplicationContext(),data.getData());
                            Toast.makeText(this, getRealPathFromURI(getApplicationContext(), data.getData()), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                  }else {
                      System.out.println("alone");

             //  if(true){

                        try {
                            //InputStream inputStream = this.getContentResolver().openInputStream(data.getData());

                            File inputFile = new File(getRealPathFromURI(getApplicationContext(), data.getData()));


                            // File inputFile = new File("text.txt");
                            File encryptedFile = new File(Environment.getExternalStorageDirectory() + File.separator + "GMLL" + File.separator + new File(getRealPathFromURI(getApplicationContext(), data.getData())).getName() + ".enc");


                            File decryptedFile = new File(Environment.getExternalStorageDirectory() + File.separator + "decrypted.jpg");

                            try {


                                fileProcessor(Cipher.ENCRYPT_MODE, key, inputFile, encryptedFile);
                                load();
                                //  Bitmap bit = deccryptfile(Cipher.DECRYPT_MODE,key,new File(Environment.getExternalStorageDirectory()+ File.separator+"GMLL"+File.separator+"text.encrypted"));
                                // img.setImageBitmap(bit);


                                Toast.makeText(this, "Sucess", Toast.LENGTH_SHORT).show();
                            } catch (Exception ex) {
                                Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                                ex.printStackTrace();
                            }
                            // getRealPathFromURI(getApplicationContext(),data.getData());
                            Toast.makeText(this, getRealPathFromURI(getApplicationContext(), data.getData()), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
            } 

        }

    }
    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    void fileProcessor(int cipherMode,String key,File inputFile,File outputFile){
        try {
            Key secretKey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(cipherMode, secretKey);

            FileInputStream inputStream = new FileInputStream(inputFile);
            byte[] inputBytes = new byte[(int) inputFile.length()];
            inputStream.read(inputBytes);

            byte[] outputBytes = cipher.doFinal(inputBytes);

            FileOutputStream outputStream = new FileOutputStream(outputFile);
            outputStream.write(outputBytes);

            inputStream.close();
            outputStream.close();

            try{
            deletefile(inputFile);}catch (Exception y){
                Toast.makeText(this, y.getMessage(), Toast.LENGTH_SHORT).show();
            }

        } catch (NoSuchPaddingException | NoSuchAlgorithmException
                | InvalidKeyException | BadPaddingException
                | IllegalBlockSizeException | IOException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    Bitmap deccryptfile (int cipherMode,String key,File inputFile){
        Bitmap bitmap=null;
        try {
            Key secretKey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(cipherMode, secretKey);

            FileInputStream inputStream = new FileInputStream(inputFile);
            byte[] inputBytes = new byte[(int) inputFile.length()];
            inputStream.read(inputBytes);

            byte[] outputBytes = cipher.doFinal(inputBytes);

            bitmap = BitmapFactory.decodeByteArray(outputBytes, 0, outputBytes.length);
            inputStream.close();

        } catch (NoSuchPaddingException | NoSuchAlgorithmException
                | InvalidKeyException | BadPaddingException
                | IllegalBlockSizeException | IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }


    public void load(){
try {
    // getting sub dirs of folder
    final ArrayList<String> paths = new ArrayList<String>();
    String path = Environment.getExternalStorageDirectory().toString() + "/GMLL";
    Log.d("Files", "Path: " + path);
    File directory = new File(path);
    File[] files = directory.listFiles();
    Log.d("Files", "Size: " + files.length);

// adding sub directories to Arraylist paths
    for (int i = 0; i < files.length; i++) {
        Log.d("Files", "filename:" + files[i].getName());
        paths.add("" + files[i].getPath());
        //Log.d("Files", "FileName:" + files[i].getPath());
    }

    // convert strign paths to bitmaps
    final ArrayList<Bitmap> bits = new ArrayList<Bitmap>();

    for (int i = 0; i < paths.size(); i++) {
        Bitmap bi = deccryptfile(Cipher.DECRYPT_MODE, key, new File(paths.get(i)));
        bits.add(bi);
        // Log.d("Files", "FileName:" + paths.get(i));
    }
    selectedImage.setImageBitmap(bits.get(0));
    gallery = (Gallery) findViewById(R.id.gallery);
    gallery.setSpacing(2);
    final CustomAd galleryImageAdapter = new CustomAd(this, bits);
    gallery.setAdapter(galleryImageAdapter);
    bi = bits.get(0);
    filepath = paths.get(0);

    gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
              bi = bits.get(position);
//                // show the selected Image
            selectedImage.setImageBitmap(bits.get(position));
            filepath = paths.get(position);
//               // selectedImage.setImageResource();
        }
    });

}catch(Exception e){
    Toast.makeText(this, "Error: File is empty", Toast.LENGTH_SHORT).show();
}

    }


    public void deletefile(File file){
        try {

            String[] projection = {MediaStore.Images.Media._ID};

            String selection = MediaStore.Images.Media.DATA + "=?";
            String[] selectionArgs = new String[]{file.getAbsolutePath()};

            Uri queryUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            ContentResolver contentResolver = getContentResolver();
            Cursor c = contentResolver.query(queryUri, projection, selection, selectionArgs, null);

            if (c.moveToFirst()) {
                long id = c.getLong(c.getColumnIndexOrThrow(MediaStore.Images.Media._ID));
                Uri deleteUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
                contentResolver.delete(deleteUri, null, null);
            } else {
                Toast.makeText(this, "Error Moving File...", Toast.LENGTH_SHORT).show();
            }
            c.close();
        }catch(Exception ex){
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void showDialog(){
        LayoutInflater factory = LayoutInflater.from(this);
        final View deleteDialogView = factory.inflate(R.layout.sure_popup, null);
        final AlertDialog deleteDialog = new AlertDialog.Builder(this).create();
        deleteDialog.setView(deleteDialogView);
        deleteDialogView.findViewById(R.id.yes).setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {

                File del_file = new File(filepath);

                if(del_file.exists()) {
                    del_file.delete();
                    selectedImage.setVisibility(View.VISIBLE);
                    gallery.setVisibility(View.VISIBLE);
                    full_view.setVisibility(View.GONE);
                    full_view.setImageBitmap(bi);
                    full_btn.setVisibility(View.VISIBLE);
                    minus.setVisibility(View.GONE);
                    fab.setVisibility(View.VISIBLE);
                    export.setVisibility(View.GONE);
                    rotate.setVisibility(View.GONE);
                    delete.setVisibility(View.GONE);
                    load();
                }
                deleteDialog.dismiss();
            }
        });
        deleteDialogView.findViewById(R.id.no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDialog.dismiss();
            }
        });

        deleteDialog.show();

    }

    


}
