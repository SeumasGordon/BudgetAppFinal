package com.Save.Save_App.Settings;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.Save.Save_App.R;
import com.Save.Save_App.Settings.ProfileFolder.ProfileInfo;

public class settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        TextView Profile = (TextView)findViewById(R.id.Profile);
        TextView TermsOfServices = (TextView)findViewById(R.id.Terms_of_Services);
        TextView Exit = (TextView)findViewById(R.id.ExitSettings);

        Exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(settings.this, ProfileInfo.class);
                startActivity(i);

            }
        });

        TermsOfServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(settings.this, TermsOfService.class);
                startActivity(i);

            }
        });
    }
}
