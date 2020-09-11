package com.test.anjane.firebase;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.test.anjane.dust.DustActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DustFirebase extends AppCompatActivity {
    private static final String TAG = "DustFirebase";
    private FirebaseFirestore db;
    ArrayList<HashMap<String, String>> result = new ArrayList<>();
    DustActivity dustActivity = new DustActivity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = FirebaseFirestore.getInstance();

        //Delete();
    }

    @Override
    public void onStart() {
        super.onStart();

        dustActivity.getItem();
        result = dustActivity.retItem();
        for(int i = 1; i < 18; i++) {
            Iterator<String> keys = result.get(i).keySet().iterator();
            final String address = keys.next();

            if(Double.parseDouble( result.get(i).get(address)) > 30.0) {
                Log.d(result.get(i).toString(), result.get(i).get(address));
                onAuthSuccess(address, result.get(i).get(address));
            }
        }
    }

    private void onAuthSuccess(String address, String value) {
        db.collection("dust").document(address).set(toMap(address, value));
    }

    @Exclude
    public Map<String, Object> toMap(String address, String content) {
        HashMap<String, Object> result = new HashMap<>();
        result.put("address", address);
        result.put("content", content);

        return result;
    }

    public void Delete() {
        db.collection("dust")
                //.whereEqualTo("capital", true)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                db.collection("dust").document(document.getId()).delete();
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void onDestroy() {
        super.onDestroy();
        finish();
    }
}