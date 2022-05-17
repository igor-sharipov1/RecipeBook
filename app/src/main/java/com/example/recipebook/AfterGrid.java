package com.example.recipebook;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.recipebook.ui.home.adapters.DishIngredientsAdapter;
import com.example.recipebook.ui.home.adapters.DishesByTimeListAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yarolegovich.discretescrollview.transform.Pivot;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;


public class AfterGrid extends Fragment {

    private ArrayList<Dish> dishArray = new ArrayList<>();
    private DishesByTimeListAdapter listAdapter;
    private RecyclerView listView;
    private DatabaseReference myDatabase;
    private String time;
    String category;
    public AfterGrid(String time, String category) {
       this.time = time;
       this.category = category;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_after_grid, container, false);

        TextView header = view.findViewById(R.id.afterGridName);
        header.setText(category);

        listView = view.findViewById(R.id.gridList);
        myDatabase = FirebaseDatabase.getInstance().getReference();
        readDatabaseCategory();
        setBackButton(view);

        return view;
    }

    private void setBackButton(View view){
        Fragment fragment = this;
        FloatingActionButton back = view.findViewById(R.id.backAfterGrid);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction().remove(fragment).commit();
            }
        });
    }

    private void readDatabaseCategory(){
        myDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot dSnapshot : snapshot
                        .child("Рецепты").child(time).child(category).getChildren()) {
                    Dish dish = new Dish();
                    dish.imgUrl = dSnapshot.child("imgUrl").getValue().toString();
                    dish.name = dSnapshot.child("name").getValue().toString();
                    ArrayList<String> ingridients = new ArrayList<>();
                    dish.time = Integer.parseInt(dSnapshot.child("time").getValue().toString());
                    dish.servings = Integer.parseInt(dSnapshot.child("servings").getValue().toString());
                    for (DataSnapshot dShapshot2 : dSnapshot.child("ingridients").getChildren()) {
                        ingridients.add(dShapshot2.getValue().toString());
                    }
                    ArrayList<String> instruction= new ArrayList<>();
                    for (DataSnapshot dShapshot2 : dSnapshot.child("instruction").getChildren()) {
                        instruction.add(dShapshot2.getValue().toString());
                    }
                    dish.ingridients = ingridients;
                    dish.instruction = instruction;
                    dish.databasePath = "Рецепты->"+time+"->"+category+"->"+dish.name;
                    dishArray.add(dish);
                }
                listView.setLayoutManager(new LinearLayoutManager(getContext()));
                listAdapter = new DishesByTimeListAdapter("main", getContext(), dishArray, getParentFragmentManager());
                listView.setAdapter(listAdapter);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
            }
        });
    }
}