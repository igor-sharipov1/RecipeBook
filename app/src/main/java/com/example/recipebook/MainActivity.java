package com.example.recipebook;

import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


public class MainActivity extends AppCompatActivity {


    private FragmentManager fm = getSupportFragmentManager();
    private Fragment mainFragment = new HomeFragment();
    private BottomNavigationView navigation;
    private Fragment searchFragment = new SearchFragment();
    private Fragment favoriteFragment = new FavoriteFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setBottomNav();
        createAllWindows();
        attachWindowsToNavigation();

    }

    private void setBottomNav(){
        navigation = this.findViewById(R.id.navigation);


        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                Fragment main = getSupportFragmentManager().findFragmentByTag("main");
                Fragment search = getSupportFragmentManager().findFragmentByTag("search");
                Fragment third = getSupportFragmentManager().findFragmentByTag("third");

                if (main.isVisible() && search.isHidden() && third.isHidden()){
                    Fragment gridChild = getSupportFragmentManager().findFragmentById(R.id.GridDishes);
                    Fragment mainChild = getSupportFragmentManager().findFragmentById(R.id.HomeDish);
                    if(mainChild != null){
                        getSupportFragmentManager().beginTransaction().remove(mainChild).commit();
                    }
                    else if(gridChild != null){
                        getSupportFragmentManager().beginTransaction().remove(gridChild).commit();
                    }
                    else{
                        finish();
                    }
                }
                else if(main.isHidden() && search.isVisible() && third.isHidden()){
                    Fragment searchDish = getSupportFragmentManager().findFragmentById(R.id.SearchDish);
                    if(searchDish != null){
                        getSupportFragmentManager().beginTransaction().remove(searchDish).commit();
                    }
                    else{
                        finish();
                    }
                }
                else if(main.isHidden() && search.isHidden() && third.isVisible()){
                    Fragment favoriteDish = getSupportFragmentManager().findFragmentById(R.id.FavoriteDish);
                    if(favoriteDish != null){
                        getSupportFragmentManager().beginTransaction().remove(favoriteDish).commit();
                    }
                    else{
                        finish();
                    }
                }
            }
        };
        this.getOnBackPressedDispatcher().addCallback(this, callback);
    }

    private void createAllWindows(){
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.main_container, mainFragment, "main")
                .commit();

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.main_container, searchFragment, "search")
                .commit();

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.main_container, favoriteFragment, "third")
                .commit();

        getSupportFragmentManager()
                .beginTransaction().show(mainFragment)
                .hide(favoriteFragment).hide(searchFragment)
                .commit();
    }

    private void attachWindowsToNavigation(){
        navigation.setOnItemSelectedListener(
                item -> {
                    switch(item.getItemId()){
                        case R.id.navigation_home:
                            fm.beginTransaction().hide(favoriteFragment)
                                    .hide(searchFragment).show(mainFragment).commit();
                            break;
                        case R.id.navigation_search:
                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .hide(favoriteFragment).show(searchFragment).hide(mainFragment)
                                    .commit();
                            break;
                        case R.id.navigation_favorite:
                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .show(favoriteFragment).hide(searchFragment).hide(mainFragment)
                                    .commit();

                            break;
                    }


                    return true;
                });
    }
}