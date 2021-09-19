package com.myktmmotor.myktmmotor.ui.services;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.myktmmotor.myktmmotor.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ViewHolder> {

    private final ArrayList<ServiceModel> listService = new ArrayList<>();
    public void setData(ArrayList<ServiceModel> items) {
        listService.clear();
        listService.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_service, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.bind(listService.get(position));
    }

    @Override
    public int getItemCount() {
        return listService.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout cv;
        TextView name, merk, dateTime, kendala, status;
        View bgStatus;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.cv);
            name = itemView.findViewById(R.id.name);
            merk = itemView.findViewById(R.id.merk);
            dateTime = itemView.findViewById(R.id.dateTime);
            kendala = itemView.findViewById(R.id.kendala);
            status = itemView.findViewById(R.id.status);
            bgStatus = itemView.findViewById(R.id.bg_status);
        }

        public void bind(ServiceModel model) {

            name.setText(model.getName());
            merk.setText(model.getMerk());
            dateTime.setText("Diajukan Pada: " + model.getDateTime());
            kendala.setText(model.getKendala());
            status.setText(model.getStatus());

            if(model.getStatus().equals("Sudah Disetujui")) {
                bgStatus.setBackgroundTintList(ContextCompat.getColorStateList(itemView.getContext(), R.color.secondary));
            } else if(model.getStatus().equals("Selesai")) {
                bgStatus.setBackgroundTintList(ContextCompat.getColorStateList(itemView.getContext(), R.color.green));
            } else {
                bgStatus.setBackgroundTintList(ContextCompat.getColorStateList(itemView.getContext(), R.color.primary));
            }

            cv.setOnClickListener(view -> {
                Intent intent = new Intent(itemView.getContext(), ServiceDetailActivity.class);
                intent.putExtra(ServiceDetailActivity.EXTRA_SERVICE, model);
                itemView.getContext().startActivity(intent);
            });

        }
    }
}
