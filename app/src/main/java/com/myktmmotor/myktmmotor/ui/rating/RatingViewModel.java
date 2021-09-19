package com.myktmmotor.myktmmotor.ui.rating;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;

public class RatingViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<RatingModel>> listRating = new MutableLiveData<>();
    final ArrayList<RatingModel> ratingModelArrayList = new ArrayList<>();
    private static final String TAG = RatingViewModel.class.getSimpleName();

    public void setListRating() {
        ratingModelArrayList.clear();

        try {
            FirebaseFirestore
                    .getInstance()
                    .collection("rating")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                RatingModel motor = new RatingModel();
                                motor.setUid("" + document.get("uid"));
                                motor.setComment("" + document.get("comment"));
                                if(("" + document.get("stars")).equals("null")) {
                                    motor.setStars(0.0);
                                } else {
                                    motor.setStars(document.getDouble("stars"));
                                }

                                ratingModelArrayList.add(motor);
                            }
                            listRating.postValue(ratingModelArrayList);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public LiveData<ArrayList<RatingModel>> getListRating() {
        return listRating;
    }

}
