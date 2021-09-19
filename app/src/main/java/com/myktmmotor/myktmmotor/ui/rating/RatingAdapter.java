package com.myktmmotor.myktmmotor.ui.rating;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.myktmmotor.myktmmotor.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class RatingAdapter extends RecyclerView.Adapter<RatingAdapter.viewHolder> {

    private final ArrayList<RatingModel> listRating = new ArrayList<>();
    public void setData(ArrayList<RatingModel> items) {
        listRating.clear();
        listRating.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rating, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull viewHolder holder, int position) {
        holder.bind(listRating.get(position));
    }

    @Override
    public int getItemCount() {
        return listRating.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {

        TextView rating, comment;

        public viewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            rating = itemView.findViewById(R.id.rating);
            comment = itemView.findViewById(R.id.comment);
        }

        @SuppressLint("SetTextI18n")
        public void bind(RatingModel model) {

            rating.setText(model.getStars() + " Bintang");
            comment.setText(model.getComment());

        }
    }
}
