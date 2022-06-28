package com.example.recipebook;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.example.recipebook.ui.home.adapters.DishesByTimeListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SearchFragment extends Fragment {

    EditText editSearch;
    private DatabaseReference myDatabase;
    private DishesByTimeListAdapter listAdapter;
    RecyclerView listView;
    ArrayList<Dish> list = new ArrayList<>();
    String currentString = "";
    public String path;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        setSearchWindowElements(view);
        return view;
    }

    private void setSearchWindowElements(View view){

        editSearch = view.findViewById(R.id.editTextSearch);

        editSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == editSearch.getId())
                {
                    editSearch.setCursorVisible(true);
                }
            }
        });

        editSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                return enterSearchButton(keyCode, view);
            }
        });
    }

    private boolean enterSearchButton(int keyCode, View view){
        if ( keyCode != KeyEvent.KEYCODE_ENTER) return false;
        if (currentString.equals(editSearch.getText().toString()))
            return false;
        else list.clear();
        currentString = editSearch.getText().toString();
        if (currentString.length() > 0) {
            readDatabaseSearch();
        }
        listView = view.findViewById(R.id.SearchList);
        listView.setLayoutManager(new LinearLayoutManager(getContext()));
        listAdapter = new DishesByTimeListAdapter("search", getContext(), list, getParentFragmentManager());
        listAdapter.notifyDataSetChanged();
        listView.setAdapter(listAdapter);
        listAdapter.notifyItemInserted(list.size());
        hideKeyboardFrom(getContext(), view);
        return true;
    }

    private void readDatabaseSearch(){
        myDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot dSnapshot : snapshot
                        .child("Рецепты").getChildren()) {
                    String time = dSnapshot.getKey();
                    for (DataSnapshot ddSnapshot : dSnapshot.getChildren()) {
                        path = "Рецепты->"+dSnapshot.getKey()+"->";
                        String category = ddSnapshot.getKey();
                        SearchThread searchThread = new SearchThread(time, category, ddSnapshot, currentString);
                        searchThread.run();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
            }
        });
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        hideKeyboardFrom(getContext(), getView());
        if (hidden == true) return;
        DishFragment dishfragment = (DishFragment) getParentFragmentManager().findFragmentById(R.id.SearchDish);
        if (dishfragment != null){
            dishfragment.check();
        }
    }

    class SearchThread extends Thread
    {
        String time;
        String category;
        DataSnapshot ddShanpshot;
        String currentString;
        String databaseString = "";



        SearchThread(String time, String category, DataSnapshot ddSnapshot, String currentString){
            this.time = time;
            this.category = category;
            this.ddShanpshot = ddSnapshot;
            this.currentString = currentString;
        }

        @Override
        public void run()
        {
            for(DataSnapshot s : ddShanpshot.getChildren()){
                databaseString = s.child("name").getValue().toString();
                for (DataSnapshot dShapshot2 : s.child("ingridients").getChildren()) {
                    databaseString += (" "+dShapshot2.getValue().toString().split("_")[0]);
                }
                if (getSimilarDish())
                    createDish(s);
            }
        }


        private void createDish(DataSnapshot dishSnapshot){
            Dish dish = new Dish();
            dish.imgUrl = dishSnapshot.child("imgUrl").getValue().toString();
            dish.name = dishSnapshot.child("name").getValue().toString();
            ArrayList<String> ingridients = new ArrayList<>();
            dish.time = Integer.parseInt(dishSnapshot.child("time").getValue().toString());
            dish.servings = Integer.parseInt(dishSnapshot.child("servings").getValue().toString());
            for (DataSnapshot dShapshot2 : dishSnapshot.child("ingridients").getChildren()) {
                ingridients.add(dShapshot2.getValue().toString());
            }
            ArrayList<String> instruction= new ArrayList<>();
            for (DataSnapshot dShapshot2 : dishSnapshot.child("instruction").getChildren()) {
                instruction.add(dShapshot2.getValue().toString());
            }
            dish.ingridients = ingridients;
            dish.instruction = instruction;
            dish.databasePath = path+category+"->"+dish.name;
            list.add(dish);
        }

        private boolean getSimilarDish(){
            for (String string : currentString.split(" ")){
                if (string.length() == 1 && currentString.length() != 1) continue;
                boolean flag = false;
                for (String string2 : databaseString.split(" ")){
                    int end = string.length() > 3 ? (int) Math.round(string.length()/1.6) : string.length();
                    boolean check = string2.toLowerCase().startsWith(string.toLowerCase().substring(0, end));
                    if (check) {
                        flag = true;
                        break;
                    }
                }
                if (!flag) return false;
            }
            return true;
        }


    }
}

