package com.myktmmotor.myktmmotor.ui.home;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;


import java.util.ArrayList;

public class MotorViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<MotorModel>> listMotor = new MutableLiveData<>();
    final ArrayList<MotorModel> motorModelArrayList = new ArrayList<>();
    private static final String TAG = MotorViewModel.class.getSimpleName();

    public void setListMotor() {

        motorModelArrayList.clear();

        try {
            FirebaseFirestore
                    .getInstance()
                    .collection("motor")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                MotorModel motor = new MotorModel();
                                motor.setColor("" + document.get("color"));
                                motor.setDp("" + document.get("dp"));
                                motor.setModel("" + document.get("model"));
                                motor.setName("" + document.get("name"));
                                motor.setPrice("" + document.get("price"));
                                motor.setSpec("" + document.get("spec"));
                                motor.setTopSpeed("" + document.get("topSpeed"));
                                motor.setUid("" + document.get("uid"));
                                motor.setYear("" + document.get("year"));



                                motorModelArrayList.add(motor);
                            }
                            listMotor.postValue(motorModelArrayList);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setListMotorByQuery(String query) {

        motorModelArrayList.clear();

        try {
            FirebaseFirestore
                    .getInstance()
                    .collection("motor")
                    .whereGreaterThanOrEqualTo("nameTemp", query)
                    .whereLessThanOrEqualTo("nameTemp", query + '\uf8ff')
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                MotorModel motor = new MotorModel();
                                motor.setColor("" + document.get("color"));
                                motor.setDp("" + document.get("dp"));
                                motor.setModel("" + document.get("model"));
                                motor.setName("" + document.get("name"));
                                motor.setPrice("" + document.get("price"));
                                motor.setSpec("" + document.get("spec"));
                                motor.setTopSpeed("" + document.get("topSpeed"));
                                motor.setUid("" + document.get("uid"));
                                motor.setYear("" + document.get("year"));



                                motorModelArrayList.add(motor);
                            }
                            listMotor.postValue(motorModelArrayList);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public LiveData<ArrayList<MotorModel>> getMotorList() {
        return listMotor;
    }

}
