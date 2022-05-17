package com.example.recipebook.ui.home.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipebook.Dish;
import com.example.recipebook.DishFragment;
import com.example.recipebook.R;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class DishesByTimeListAdapter extends RecyclerView.Adapter<DishesByTimeListAdapter.DishViewHolder>  {

    Context context;
    ArrayList<Dish> dishList;
    FragmentManager fragmentManager;
    String FRAGMENT_TAG;

    public DishesByTimeListAdapter(String FRAGMENT_TAG,Context context, ArrayList<Dish> dishList, FragmentManager fragmentManager){
        this.FRAGMENT_TAG = FRAGMENT_TAG;
        this.context = context;
        this.dishList = dishList;
        this.fragmentManager = fragmentManager;
    }



    @NonNull
    @NotNull
    @Override
    public DishViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.menu_dish_item, parent, false);

        return new DishViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull DishViewHolder holder, int position) {
        Dish dish = dishList.get(position);
        Picasso.with(context).load(dish.imgUrl).into(holder.mainImg);
        holder.dishName.setText(dish.name);

        holder.time.setText(dish.time+" мин");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DishFragment fragObj = new DishFragment(FRAGMENT_TAG, dish);
                switch (FRAGMENT_TAG){
                    case "main":
                        fragmentManager.beginTransaction().add(R.id.HomeDish, fragObj, "mainChild").commit();
                        break;
                    case "search":
                        fragmentManager.beginTransaction().add(R.id.SearchDish, fragObj, "searchChild").commit();
                        break;
                    case "third":
                        fragmentManager.beginTransaction().add(R.id.FavoriteDish, fragObj, "favoriteChild").commit();
                        break;
                }
            }
        });

    }



    @Override
    public int getItemCount() {
        return dishList.size();
    }


    static class DishViewHolder extends RecyclerView.ViewHolder{

        final ImageView mainImg;
        final TextView dishName;
        final TextView time;

        public DishViewHolder(@NonNull @NotNull View convertView) {
            super(convertView);
            mainImg = convertView.findViewById(R.id.menuDishImage);
            dishName = convertView.findViewById(R.id.menuDishName);
            time = convertView.findViewById(R.id.cookingTime);
        }


    }


}
