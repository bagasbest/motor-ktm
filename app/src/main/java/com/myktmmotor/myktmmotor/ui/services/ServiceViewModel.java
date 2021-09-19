package com.myktmmotor.myktmmotor.ui.services;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;

public class ServiceViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<ServiceModel>> listService = new MutableLiveData<>();
    final ArrayList<ServiceModel> serviceModelArrayList = new ArrayList<>();
    private static final String TAG = ServiceViewModel.class.getSimpleName();

    public void setAllService(String status) {
        serviceModelArrayList.clear();

        try {
            FirebaseFirestore
                    .getInstance()
                    .collection("service")
                    .whereEqualTo("status", status)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ServiceModel service = new ServiceModel();
                                service.setAddress("" + document.get("address"));
                                service.setDateTime("" + document.get("dateTime"));
                                service.setKendala("" + document.get("kendala"));
                                service.setMerk("" + document.get("merk"));
                                service.setName("" + document.get("name"));
                                service.setPhone("" + document.get("phone"));
                                service.setServiceId("" + document.get("serviceId"));
                                service.setStatus("" + document.get("status"));
                                service.setServiceDate("" + document.get("serviceDate"));
                                service.setUid("" + document.get("uid"));

                                serviceModelArrayList.add(service);
                            }
                            listService.postValue(serviceModelArrayList);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setAllServiceByUid(String uid, String status) {
        serviceModelArrayList.clear();

        try {
            FirebaseFirestore
                    .getInstance()
                    .collection("service")
                    .whereEqualTo("uid", uid)
                    .whereEqualTo("status", status)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ServiceModel service = new ServiceModel();
                                service.setAddress("" + document.get("address"));
                                service.setDateTime("" + document.get("dateTime"));
                                service.setKendala("" + document.get("kendala"));
                                service.setMerk("" + document.get("merk"));
                                service.setName("" + document.get("name"));
                                service.setPhone("" + document.get("phone"));
                                service.setServiceId("" + document.get("serviceId"));
                                service.setStatus("" + document.get("status"));
                                service.setServiceDate("" + document.get("serviceDate"));
                                service.setUid("" + document.get("uid"));

                                serviceModelArrayList.add(service);
                            }
                            listService.postValue(serviceModelArrayList);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public LiveData<ArrayList<ServiceModel>> getServiceList() {
        return listService;
    }
}
