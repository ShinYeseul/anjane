package com.test.anjane.post;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.test.anjane.R;

public class Photo extends AppCompatActivity {

    private static final String TAG = "Photo";

    FirebaseStorage storage = FirebaseStorage.getInstance("gs://anjane-821f9.appspot.com");
    StorageReference storageRef = storage.getReference();

    private ImageView allpohoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.photo);

        allpohoto = findViewById( R.id.allphoto);

        Intent intent = getIntent();
        String imagename = intent.getExtras().getString("iv");
        final StorageReference islandRef = storageRef.child("postimage/" + imagename);
        islandRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    // Glide 이용하여 이미지뷰에 로딩
                    Glide.with( Photo.this)
                            .load(task.getResult())
                            .into(allpohoto);
                } else {
                    // URL을 가져오지 못하면 토스트 메세지
                    Toast.makeText( Photo.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }

    //닫기 버튼
    public  void  onXButtonClicked(View v){
        finish();
    }
}