package com.example.recipebook;

import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.recipebook.ui.home.DishByTimeFragment;
import java.util.Date;


public class HomeFragment extends Fragment {



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        setElementsByTime();
        return root;

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        onFavoriteDishChange(hidden);
    }

    private void onFavoriteDishChange(boolean hidden){
        if (hidden == true) return;
        DishFragment dishfragment = (DishFragment) getParentFragmentManager().findFragmentById(R.id.HomeDish);
        if (dishfragment != null){
            dishfragment.check();
        }
    }

    private void setElementsByTime(){
        Fragment fragObj = new DishByTimeFragment("Завтрак");
        Fragment fragObj2 = new DishByTimeFragment("Обед");
        Fragment fragObj3 = new DishByTimeFragment("Ужин");

        timeChecker(fragObj, fragObj2, fragObj3);

    }

    private void timeChecker(Fragment f1, Fragment f2, Fragment f3){
        Date currentTime = Calendar.getInstance().getTime();
        int currentTimeString = Integer.parseInt(currentTime.toString().split(" ")[3].split(":")[0]);

        if (currentTimeString > 6 && currentTimeString < 12){
            getParentFragmentManager().beginTransaction().add(R.id.dishFrame, f1).commit();
            getParentFragmentManager().beginTransaction().add(R.id.dishFrame2, f2).commit();
            getParentFragmentManager().beginTransaction().add(R.id.dishFrame3, f3).commit();
        }
        else if (currentTimeString >= 12 && currentTimeString < 18){
            getParentFragmentManager().beginTransaction().add(R.id.dishFrame, f2).commit();
            getParentFragmentManager().beginTransaction().add(R.id.dishFrame2, f3).commit();
            getParentFragmentManager().beginTransaction().add(R.id.dishFrame3, f1).commit();
        }
        else {
            getParentFragmentManager().beginTransaction().add(R.id.dishFrame, f3).commit();
            getParentFragmentManager().beginTransaction().add(R.id.dishFrame2, f1).commit();
            getParentFragmentManager().beginTransaction().add(R.id.dishFrame3, f2).commit();
        }
    }

}