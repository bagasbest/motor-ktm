package com.myktmmotor.myktmmotor.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.myktmmotor.myktmmotor.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MotorAdapter extends RecyclerView.Adapter<MotorAdapter.ViewHolder> {

    private final ArrayList<MotorModel> listMotor = new ArrayList<>();
    public void setData(ArrayList<MotorModel> items) {
        listMotor.clear();
        listMotor.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_motor, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.bind(listMotor.get(position));
    }

    @Override
    public int getItemCount() {
        return listMotor.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout cv;
        TextView title;
        ImageView dp;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.cv);
            title = itemView.findViewById(R.id.title);
            dp = itemView.findViewById(R.id.dp);
        }

        public void bind(MotorModel model) {
            Glide.with(itemView.getContext())
                    .load(model.getDp())
                    .into(dp);

            title.setText(model.getName());

            cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
    }
}
