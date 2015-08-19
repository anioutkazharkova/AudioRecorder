package com.azharkova.writemesound.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Toast;

import com.azharkova.writemesound.R;
import com.azharkova.writemesound.Utility;

public class SortingActivity extends AppCompatActivity {
SharedPreferences preferences;
    RadioButton rbName,rbDate,rbCollection;

    private int sortOrder=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sorting);
        preferences=getSharedPreferences(Utility.PREFERENCES, Context.MODE_PRIVATE);

        rbName=(RadioButton)findViewById(R.id.rbNameSort);
        rbDate=(RadioButton)findViewById(R.id.rbDateSort);
        rbCollection=(RadioButton)findViewById(R.id.rbCollectionNameSort);

        rbName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()) {
                    sortOrder = 0;
                }

            }
        });
        rbDate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()) {
                    sortOrder = 1;
                }
            }
        });
        rbCollection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()) {
                    sortOrder = 2;
                }
            }
        });

        loadPreferences();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sorting, menu);
        return true;
    }

    private void loadPreferences()
    {
        sortOrder=preferences.getInt(Utility.SORT_PREFERENCES,0);
      //  Toast.makeText(this,sortOrder+"",Toast.LENGTH_LONG).show();
        switch(sortOrder)
        {
            case 0:
                rbName.setChecked(true);
                rbCollection.setChecked(false);
                rbDate.setChecked(false);
                break;
            case 1:
                rbDate.setChecked(true);
                rbName.setChecked(false);
                rbCollection.setChecked(false);
                break;
            case 2:
                rbDate.setChecked(false);
                rbName.setChecked(false);
                rbCollection.setChecked(true);
                break;
        }
    }

    public void savePreferences()
    {
        SharedPreferences.Editor edit=preferences.edit();
        edit.putInt(Utility.SORT_PREFERENCES,sortOrder);

        edit.apply();
       // Toast.makeText(this,sortOrder+"",Toast.LENGTH_LONG).show();

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {

            finish();
            return true;
        }
        if (id==R.id.itemDone)
        {
            savePreferences();
            Intent intent=new Intent();


            setResult(RESULT_OK,intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
