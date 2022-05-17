package com.example.recipebook.ui.home.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipebook.R;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class DishInstructionAdapter extends RecyclerView.Adapter<DishInstructionAdapter.DishInstructionHolder>{

    private Context context;
    private ArrayList<String> listInstruction;

    public DishInstructionAdapter(Context context, ArrayList<String> listIngredients){
        this.context = context;
        this.listInstruction = listIngredients;
    }


    @NonNull
    @NotNull
    @Override
    public DishInstructionHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.instruction_item, parent, false);

        return new DishInstructionHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull DishInstructionHolder holder, int position) {
        String ingredients = listInstruction.get(position);
        holder.stepNumber.setText("Шаг "+ (position + 1));
        Picasso.with(context).load(ingredients.split("___")[0]).into(holder.instructionImage);
        holder.instruction.setText(ingredients.split("___")[1]);
    }

    @Override
    public int getItemCount() {
        return listInstruction.size();
    }

    static class DishInstructionHolder extends RecyclerView.ViewHolder {

        final TextView stepNumber;
        final TextView instruction;
        final ImageView instructionImage;


        public DishInstructionHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            stepNumber = itemView.findViewById(R.id.stepNumber);
            instruction = itemView.findViewById(R.id.instruction);
            instructionImage = itemView.findViewById(R.id.instructionImage);
        }
    }
}
