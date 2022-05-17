package com.example.recipebook;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.recipebook.ui.home.adapters.DishIngredientsAdapter;
import com.example.recipebook.ui.home.adapters.DishInstructionAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rhexgomez.typer.roboto.TyperRoboto;
import com.squareup.picasso.Picasso;
import net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout;


public class DishFragment extends Fragment {

    private Dish currentDish;
    private RecyclerView listIngredients;
    private RecyclerView listInstructions;
    FloatingActionButton favorite;
    private DishIngredientsAdapter ingredientsAdapter;
    private DishInstructionAdapter instructionAdapter;
    private Fragment fragment = this;
    String FRAGMENT_TAG;
    boolean flag = false;
    static DishFragment instance;

    public DishFragment(String FRAGMENT_TAG, Dish dish){
        this.FRAGMENT_TAG = FRAGMENT_TAG;
        this.currentDish = dish;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_dish_view, container, false);

        instance = this;
        setEveryBackgroundElement(view);
        favorite = view.findViewById(R.id.favorite);
        check();
        setFavoriteButton();
        return view;
    }


    public void check(){
        SharedPreferences sp = null;
        try{
            sp = getActivity().getSharedPreferences("prefs",getContext().MODE_PRIVATE);
        }
        catch(NullPointerException e){

        }
        if (sp != null) {
            String base = sp.getString(currentDish.name, "");
            if (base.length() != 0) {
                favorite.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_baseline_favorite));
                flag = true;
            } else {
                favorite.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_baseline_favorite_border));
                flag = false;
            }
        }
    }



    private void setEveryBackgroundElement(View view){
        CollapsingToolbarLayout collapsingToolbarLayout = view.findViewById(R.id.main_collapsing);
        collapsingToolbarLayout.setTitle(currentDish.name);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.AppBarExpanded);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.AppBarCollapsed);
        collapsingToolbarLayout.setCollapsedTitleTypeface(TyperRoboto.ROBOTO_BOLD());
        collapsingToolbarLayout.setExpandedTitleTypeface(TyperRoboto.ROBOTO_BOLD());

        ImageView mainImg = view.findViewById(R.id.mainImageDish);
        Picasso.with(getContext()).load(currentDish.imgUrl).into(mainImg);

        listIngredients = view.findViewById(R.id.ingredients);
        listIngredients.setLayoutManager(new LinearLayoutManager(getContext()));
        ingredientsAdapter = new DishIngredientsAdapter(getContext(), currentDish.ingridients);
        listIngredients.setAdapter(ingredientsAdapter);

        listInstructions = view.findViewById(R.id.stepByStepInstruction);
        listInstructions.setLayoutManager(new LinearLayoutManager(getContext()));
        instructionAdapter = new DishInstructionAdapter(getContext(), currentDish.instruction);
        listInstructions.setAdapter(instructionAdapter);

        TextView servings = view.findViewById(R.id.servantsCount);
        if (1 < currentDish.servings && 5 > currentDish.servings) servings.setText(currentDish.servings+" порции");
        else if(1 == currentDish.servings) servings.setText(currentDish.servings+" порция");
        else servings.setText(currentDish.servings+" порций");

        TextView time = view.findViewById(R.id.timeCooking);
        time.setText(currentDish.time+" минут");


        FloatingActionButton back = view.findViewById(R.id.backAfterGrid);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction().remove(fragment).commit();
            }
        });
    }

    private void setFavoriteButton(){
        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag){
                    SharedPreferences sp = getActivity().getSharedPreferences("prefs", getContext().MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    String base = sp.getString(currentDish.name, "");
                    editor.remove(base.split("->")[3]).apply();
                    FavoriteFragment frag = FavoriteFragment.GetInstance();
                    frag.recreateList();
                    favorite.setImageDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.ic_baseline_favorite_border));
                    flag = false;
                    Toast.makeText(getContext(), "Удалено", Toast.LENGTH_SHORT).show();
                }
                else{
                    SharedPreferences sp = getActivity().getSharedPreferences("prefs", getContext().MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString(currentDish.name, currentDish.databasePath).apply();
                    FavoriteFragment frag = FavoriteFragment.GetInstance();
                    frag.recreateList();
                    favorite.setImageDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.ic_baseline_favorite));
                    flag = true;
                    Toast.makeText(getContext(), "Добавлено", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

}