package com.example.ichatsocialmedaiapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.ichatsocialmedaiapp.Fragments.ChatsFragment;
import com.example.ichatsocialmedaiapp.Fragments.HomeFragment;
import com.example.ichatsocialmedaiapp.Fragments.NotificationFragment;
import com.example.ichatsocialmedaiapp.Fragments.ProfileFragment;
import com.example.ichatsocialmedaiapp.Fragments.ShortsFragment;
import com.example.ichatsocialmedaiapp.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();


        auth = FirebaseAuth.getInstance();


        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, new HomeFragment());
        transaction.commit();


        BottomNavigationView bnv = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        bnv.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                switch (item.getItemId()) {

                    case R.id.home:
//                        binding.toolbar2.setVisibility(View.GONE);
                        getSupportActionBar().hide();
                        transaction.replace(R.id.container, new HomeFragment());
                        Toast.makeText(getApplicationContext(), "Home", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.chatting:
//                        binding.toolbar2.setVisibility(View.GONE);
                        getSupportActionBar().hide();
                        transaction.replace(R.id.container, new ChatsFragment());
                        Toast.makeText(getApplicationContext(), "Chat", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.Shorts:
                        getSupportActionBar().hide();
                        transaction.replace(R.id.container,new ShortsFragment());
                        Toast.makeText(getApplicationContext(), "Add Post", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.notification:
//                        binding.toolbar2.setVisibility(View.GONE);
                        getSupportActionBar().hide();
                        transaction.replace(R.id.container, new NotificationFragment());
//                        Intent intent=new Intent(MainActivity.this,NotificationTabsActivity.class);
//                        startActivity(intent);
                        Toast.makeText(getApplicationContext(), "Notifications", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.profile:
//                        binding.toolbar2.setVisibility(View.VISIBLE);
                        getSupportActionBar().hide();
                        transaction.replace(R.id.container, new ProfileFragment());
                        Toast.makeText(getApplicationContext(), "Profile", Toast.LENGTH_SHORT).show();
                        break;

                }
                transaction.commit();
                return true;
            }
        });


    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater=getMenuInflater();
//        inflater.inflate(R.menu.signout_menu,menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
////        return super.onOptionsItemSelected(item);
//        switch(item.getItemId())
//        {
//            case R.id.signout:
//                Toast.makeText(MainActivity.this,"Sign Out",Toast.LENGTH_SHORT).show();
//                auth.signOut();
//                startActivity(new Intent(MainActivity.this, SignUp.class));
//        }
//        return super.onOptionsItemSelected(item);
//    }
}

