package com.example.mp4player;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jaiselrahman.filepicker.activity.FilePickerActivity;
import com.jaiselrahman.filepicker.config.Configurations;
import com.jaiselrahman.filepicker.model.MediaFile;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText et_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_url = findViewById(R.id.et_url);
    }


    public void video(View view){
        Intent intent = new Intent(MainActivity.this, FilePickerActivity.class);

        intent.putExtra(FilePickerActivity.CONFIGS, new Configurations.Builder()
                .setCheckPermission(true)
                .setShowImages(false)
                .setShowVideos(true)
                .enableVideoCapture(true)
                .setMaxSelection(1)
                .setSkipZeroSizeFiles(true)
                .build());

        startActivityForResult(intent, 104);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && data != null){

            ArrayList<MediaFile> mediaFiles = data.getParcelableArrayListExtra(FilePickerActivity.MEDIA_FILES);

            String path = mediaFiles.get(0).getPath();

            if (requestCode == 104) {
//                Toast.makeText(getApplicationContext(), "Video path: " + path, Toast.LENGTH_SHORT).show();

                Intent i = new Intent(MainActivity.this, player.class);
                i.putExtra("path", path);
                startActivity(i);
            }

        }
    }


    public void url(View view){
        String URL = et_url.getText().toString();
        if(URL.contains(".mp4") || URL.contains(".mkv"))
        {
            Intent i = new Intent(MainActivity.this, player.class);
            i.putExtra("url",URL);
            startActivity(i);
        }
        else if(et_url.getText().toString().trim().isEmpty()){
            et_url.setError("empty");
        }
        else {
            et_url.setText("");
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setCancelable(false);
            builder.setTitle(Html.fromHtml("<font color='#509324'>success</font>"));
            builder.setMessage("URL does not direct to a video");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });

            builder.show();
        }
    }

}