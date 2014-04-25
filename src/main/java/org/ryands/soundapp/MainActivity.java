package org.ryands.soundapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends Activity {
    public static final String TAG = MainActivity.class.getSimpleName();

    private Button install;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        install = (Button) findViewById(R.id.btn_install_sound);
        install.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                installSoundHelper();
            }
        });

    }

    private void installSoundHelper() {
        File dest = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_NOTIFICATIONS);

        InputStream input = getResources().openRawResource(R.raw.old_hangouts);
        File file = new File(dest, "old_hangouts.ogg");

        try {
            FileOutputStream out = new FileOutputStream(file);

            byte[] buffer = new byte[4096];
            int count;
            while ((count = input.read(buffer, 0, 4096)) != -1) {
                out.write(buffer, 0, count);
            }
            out.flush();
            out.close();

            Intent notify = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            notify.setData(Uri.fromFile(file));
            sendBroadcast(notify);

            Toast.makeText(this, "Installed old notification sound!", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Log.e(TAG, "Error writing file: ", e);
            Toast.makeText(this, "Error installing sound :(", Toast.LENGTH_LONG).show();
        }

    }
}
