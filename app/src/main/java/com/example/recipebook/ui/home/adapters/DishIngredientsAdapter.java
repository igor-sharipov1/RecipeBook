package com.example.recipebook.ui.home.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipebook.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class DishIngredientsAdapter extends RecyclerView.Adapter<DishIngredientsAdapter.DishIngredientsHolder>{

    private Context context;
    private ArrayList<String> listIngredients;

    public DishIngredientsAdapter(Context context, ArrayList<String> listIngredients){
        this.context = context;
        this.listIngredients = listIngredients;
    }


    @NonNull
    @NotNull
    @Override
    public DishIngredientsHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.ingredient_item, parent, false);

        return new DishIngredientsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull DishIngredientsHolder holder, int position) {
        String ingredients = listIngredients.get(position);
        holder.typeIngredient.setText(ingredients.split("_")[0]);
        holder.countIngredient.setText(ingredients.split("_")[1]);
    }

    @Override
    public int getItemCount() {
        return listIngredients.size();
    }

    static class DishIngredientsHolder extends RecyclerView.ViewHolder {

        final TextView typeIngredient;
        final TextView countIngredient;


        public DishIngredientsHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            typeIngredient = itemView.findViewById(R.id.typeIngredient);
            countIngredient = itemView.findViewById(R.id.countIngredient);
        }
    }
}
