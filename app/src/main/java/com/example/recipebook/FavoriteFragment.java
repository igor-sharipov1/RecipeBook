package com.example.recipebook;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.recipebook.ui.home.adapters.DishesByTimeListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Map;


public class FavoriteFragment extends Fragment {

    RecyclerView recyclerView;
    DishesByTimeListAdapter adapter;
    private DatabaseReference myDatabase;
    ArrayList<Dish> favoriteList = new ArrayList<>();
    private static FavoriteFragment instance;
    int currentCount = 0;
    private View view;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        instance = this;
        view = inflater.inflate(R.layout.fragment_favorite, container, false);
        recyclerView = view.findViewById(R.id.favoriteList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }

    private void fillList(){
        SharedPreferences sp = getActivity().getSharedPreferences("prefs", getContext().MODE_PRIVATE);
        Map<String, ?> allEntries = sp.getAll();
        int allCount = allEntries.size();
        
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            readDatabaseFavorite(entry, allCount);
        }

    }

    private void readDatabaseFavorite(Map.Entry<String, ?> entry, int allCount){
        String[] pathq = entry.getValue().toString().split("->");
        Query q = myDatabase.child(pathq[0]).child(pathq[1]).child(pathq[2]).child(pathq[3]);
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot data) {
                currentCount++;
                Dish dish = new Dish();
                dish.name = data.child("name").getValue().toString();
                dish.imgUrl = data.child("imgUrl").getValue().toString();
                ArrayList<String> ingridients = new ArrayList<>();
                for (DataSnapshot dShapshot2 : data.child("ingridients").getChildren()) {
                    ingridients.add(dShapshot2.getValue().toString());
                }
                dish.ingridients = ingridients;
                ArrayList<String> instruction = new ArrayList<>();
                for (DataSnapshot dShapshot2 : data.child("instruction").getChildren()) {
                    instruction.add(dShapshot2.getValue().toString());
                }
                dish.instruction = instruction;
                dish.servings = Integer.parseInt(data.child("servings").getValue().toString());
                dish.time = Integer.parseInt(data.child("time").getValue().toString());
                dish.name = data.child("name").getValue().toString();
                dish.databasePath = entry.getValue().toString();
                favoriteList.add(dish);
                if (currentCount == allCount){
                    adapter = new DishesByTimeListAdapter("third", getContext(), favoriteList, getParentFragmentManager());
                    recyclerView.setAdapter(adapter);
                    currentCount = 0;
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        recreateList(); // ????????????????????????????????
    }

    public static FavoriteFragment GetInstance()
    {
        return instance;
    }

    public  void  recreateList()
    {
        favoriteList.clear();
        fillList();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        onFavoriteDishChange(hidden);
    }

    private void onFavoriteDishChange(boolean hidden){
        if (hidden == true) return;
        DishFragment dishfragment = (DishFragment) getParentFragmentManager().findFragmentById(R.id.FavoriteDish);
        if (dishfragment != null){
            dishfragment.check();
        }
    }

}
