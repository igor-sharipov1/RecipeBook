package com.example.recipebook.ui.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.example.recipebook.AfterGrid;
import com.example.recipebook.Dish;

import com.example.recipebook.ui.home.adapters.DishesByTimeListAdapter;
import com.example.recipebook.R;
import com.example.recipebook.ui.home.adapters.DishByTimeGridAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.transform.Pivot;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;


public class DishByTimeFragment extends Fragment  {

    private GridView gridView;
    private DiscreteScrollView listView;
    private DishByTimeGridAdapter mAdapter;
    private DishesByTimeListAdapter listAdapter;
    private DatabaseReference myDatabase;
    private String time; //Завтрак Обед Ужин
    private ArrayList<String> dishStringArray = new ArrayList<>();
    private ArrayList<Dish> dishArray = new ArrayList<>();

    public DishByTimeFragment(String time) {
        this.time = time;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("asd","asd");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_dish_by_time, container, false);
        myDatabase = FirebaseDatabase.getInstance().getReference();
        gridView = view.findViewById(R.id.otherTimeDishes);
        listView = view.findViewById(R.id.dishesPreview);
        listView.setItemTransformer(new ScaleTransformer.Builder()
                .setMaxScale(1.05f)
                .setMinScale(0.8f)
                .setPivotX(Pivot.X.CENTER) // CENTER is a default one
                .build());

        TextView header =  view.findViewById(R.id.timeName);
        header.setText(time);
        switch (time){
            case "Завтрак":
                fillArrayMorning();
                break;
            case "Обед":
                fillArrayAfternoon();
                break;
            case "Ужин":
                fillArrayEvening();
                break;
        }
        mAdapter = new DishByTimeGridAdapter(getContext(), dishStringArray, time, getParentFragmentManager());
        gridView.setAdapter(mAdapter);

        readDatabaseDishByTime();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                TextView categoryName = v.findViewById(R.id.categoryName);
                Fragment afterGrid = new AfterGrid(time, categoryName.getText().toString());
                getParentFragmentManager().beginTransaction().add(R.id.GridDishes, afterGrid).commit();
            }
        });

        return view;
    }


    private void fillArrayMorning(){
        dishStringArray.add("Салаты");
        dishStringArray.add("Каши");
        dishStringArray.add("Омлеты и глазуньи");
        dishStringArray.add("Сырники");
    }

    private void fillArrayAfternoon(){
        dishStringArray.add("Супы");
        dishStringArray.add("Запеканки");
        dishStringArray.add("Выпечка");
        dishStringArray.add("Блюда из рыбы");
    }

    private void fillArrayEvening(){
        dishStringArray.add("Блюда из свинины");
        dishStringArray.add("Блюда из курицы");
        dishStringArray.add("Блюда из говядины");
        dishStringArray.add("Гарниры");
    }

    private void readDatabaseDishByTime(){
        myDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {


                for (DataSnapshot dSnapshot : snapshot
                        .child("Рецепты").child(time).getChildren()){
                    int countElements = 0;
                    for(DataSnapshot ddSnapshot : dSnapshot.getChildren()) {
                        countElements++;
                        if(countElements > 3) break;
                        Dish dish = new Dish();
                        dish.imgUrl = ddSnapshot.child("imgUrl").getValue().toString();
                        dish.name = ddSnapshot.child("name").getValue().toString();
                        ArrayList<String> ingridients = new ArrayList<>();
                        dish.time = Integer.parseInt(ddSnapshot.child("time").getValue().toString());
                        dish.servings = Integer.parseInt(ddSnapshot.child("servings").getValue().toString());
                        for (DataSnapshot dShapshot2 : ddSnapshot.child("ingridients").getChildren()) {
                            ingridients.add(dShapshot2.getValue().toString());
                        }
                        ArrayList<String> instruction= new ArrayList<>();
                        for (DataSnapshot dShapshot2 : ddSnapshot.child("instruction").getChildren()) {
                            instruction.add(dShapshot2.getValue().toString());
                        }
                        dish.ingridients = ingridients;
                        dish.instruction = instruction;
                        dish.databasePath = "Рецепты->"+time+"->"+dSnapshot.getKey()+"->"+dish.name;
                        dishArray.add(dish);
                    }
                }
                Collections.shuffle(dishArray);
                listAdapter = new DishesByTimeListAdapter("main", getContext(), dishArray, getParentFragmentManager());

                listView.setAdapter(listAdapter);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }


}