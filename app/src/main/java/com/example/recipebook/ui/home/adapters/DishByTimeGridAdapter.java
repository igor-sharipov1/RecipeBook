package com.example.recipebook.ui.home.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;

import com.example.recipebook.R;

import java.util.ArrayList;

public class DishByTimeGridAdapter extends BaseAdapter {

    private static ArrayList<String> myArray;
    private static ArrayList<String> mArray;
    private static ArrayList<String> aArray;
    private static ArrayList<String> eArray;
    ImageView mainImg;
    TextView dishName;
    String time; // Завтрак Обед Ужин
    Context mContext;
    FragmentManager fragmentManager;




    public DishByTimeGridAdapter(Context context, ArrayList<String> dishes, String time, FragmentManager fragmentManager) {
        if (time.equals("Завтрак")) {
            mArray = dishes;
        }
        else if(time.equals("Обед")){
            aArray = dishes;
        }
        else{
            eArray = dishes;
        }
        this.myArray = dishes;
        this.mContext = context;
        this.time = time;
        this.fragmentManager = fragmentManager;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (time.equals("Завтрак")) {
            myArray = mArray;
        }
        else if(time.equals("Обед")){
            myArray = aArray;
        }
        else{
            myArray = eArray;
        }
        String element = myArray.get(position);


        if (convertView == null) {
            convertView =  LayoutInflater.from(mContext).inflate(R.layout.grid_item, parent, false);
        }

        mainImg = convertView.findViewById(R.id.gridItemImage);
        dishName = convertView.findViewById(R.id.categoryName);

        switch(time){
            case "Завтрак":
                fillGridMorning(element);
                break;
            case "Обед":
                fillGridAfternoon(element);
                break;
            case "Ужин":
                fillGridEvening(element);
                break;
        }


        return convertView;
    }

    private void fillGridMorning(String element){
        switch (element){
            case "Салаты":
                mainImg.setImageResource(R.drawable.salad);
                dishName.setText(element);
                break;
            case "Каши":
                mainImg.setImageResource(R.drawable.porridge);
                dishName.setText(element);
                break;
            case "Омлеты и глазуньи":
                mainImg.setImageResource(R.drawable.omelette);
                dishName.setText(element);
                break;
            case "Сырники":
                mainImg.setImageResource(R.drawable.cheesecakes);
                dishName.setText(element);
                break;
        }
    }

    private void fillGridAfternoon(String element){
        switch (element){
            case "Супы":
                mainImg.setImageResource(R.drawable.soups);
                dishName.setText(element);
                break;
            case "Запеканки":
                mainImg.setImageResource(R.drawable.casseroles);
                dishName.setText(element);
                break;
            case "Выпечка":
                mainImg.setImageResource(R.drawable.bakery);
                dishName.setText(element);
                break;
            case "Блюда из рыбы":
                mainImg.setImageResource(R.drawable.fish);
                dishName.setText(element);
                break;
        }
    }

    private void fillGridEvening(String element){
        switch (element){
            case "Блюда из свинины":
                mainImg.setImageResource(R.drawable.pork);
                dishName.setText(element);
                break;
            case "Блюда из курицы":
                mainImg.setImageResource(R.drawable.chicken);
                dishName.setText(element);
                break;
            case "Блюда из говядины":
                mainImg.setImageResource(R.drawable.cow);
                dishName.setText(element);
                break;
            case "Гарниры":
                mainImg.setImageResource(R.drawable.garnish);
                dishName.setText(element);
                break;
        }
    }


    @Override
    public int getCount() {
        return myArray.size();
    }

    public String getItem(int position) {
        return myArray.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

}