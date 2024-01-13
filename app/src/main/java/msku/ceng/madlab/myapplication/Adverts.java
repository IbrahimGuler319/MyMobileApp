package msku.ceng.madlab.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Adverts extends AppCompatActivity {

    private TextView advertsMenu;
    private Button givingAdvert, lookingAdvert, yourAdverts, referencedAdverts;
    private LinearLayout topBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adverts);
        advertsMenu = findViewById(R.id.advertsMenu);
        givingAdvert = findViewById(R.id.givingAdvert);
        lookingAdvert = findViewById(R.id.lookingAdvert);
        yourAdverts = findViewById(R.id.givingAdvert);
        referencedAdverts = findViewById(R.id.referencedAdverts);
        topBar = findViewById(R.id.topBar);
    }
}
