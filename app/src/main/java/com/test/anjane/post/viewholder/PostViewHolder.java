package com.test.anjane.post.viewholder;

import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.test.anjane.R;
import com.test.anjane.post.models.mPost;

public class PostViewHolder extends RecyclerView.ViewHolder {

    public TextView titleView;
    public TextView authorView;
    public TextView addressView;
    public TextView titleContent;
    private TextView spin1View;
    private TextView bodyView;
    private ImageView postImage1;
    private TextView postDate;
    public View txtDelete;
    public View postView;

    public PostViewHolder(View itemView) {
        super(itemView);

        titleView = itemView.findViewById(R.id.post_title);
        authorView = itemView.findViewById(R.id.post_author);
        addressView = itemView.findViewById(R.id.post_address);
        spin1View = itemView.findViewById(R.id.post_category);
        bodyView = itemView.findViewById(R.id.post_body);
        postDate = itemView.findViewById(R.id.post_date);
        txtDelete = itemView.findViewById(R.id.txtDelete);
        postView = itemView.findViewById(R.id.postView);
        titleContent = itemView.findViewById(R.id.post_content);

        postImage1 = itemView.findViewById(R.id.itemimage);
    }


    public void bindToPost(mPost post, View.OnClickListener starClickListener) {
        titleView.setText(post.title);
        addressView.setText(post.address);
        spin1View.setText(post.spin1 + "/" + post.spin2);
        postDate.setText(post.date);
        bodyView.setVisibility(View.GONE);
        titleContent.setVisibility(View.GONE);
        getImageView1(post.image1);
    }

    FirebaseStorage storage = FirebaseStorage.getInstance();

    public void getImageView1(String name) {
        StorageReference storageRef = storage.getReferenceFromUrl("gs://anjane-821f9.appspot.com").child("postimage/" + name);

        storageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Glide.with(postImage1.getContext())
                            .load(task.getResult())
                            .into(postImage1);
                } else {
                    Toast.makeText( postImage1.getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) { }
        });
    }
}