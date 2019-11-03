package com.example.roomiefinder;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class mainActivity extends AppCompatActivity {

    private BottomNavigationView main_nav;
    private FrameLayout main_frame;
    private calendarFragment cf;
    private searchFragment sf;
    private profileFragment pf;
    private ChatFragment chf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        main_frame = (FrameLayout) findViewById(R.id.main_frame);
        main_nav = (BottomNavigationView) findViewById(R.id.nav_bar);

        cf = new calendarFragment();
        sf = new searchFragment();
        pf = new profileFragment();
        chf = new ChatFragment();

        setFragment(cf);

        main_nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_home:
                        setFragment(cf);
                        return true;

                    case R.id.nav_search:
                        setFragment(sf);
                        return true;

                    case R.id.nav_profile:
                        setFragment(pf);
                        return true;

                    case R.id.nav_chat:
                        setFragment(chf);
                        return true;

                    default:
                        return false;
                }
            }
        });
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.main_frame, fragment);
        ft.commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            // do something on back.
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if(user != null) {
                FirebaseAuth.getInstance().signOut();
            }
            Intent intent = new Intent(this,loginActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}