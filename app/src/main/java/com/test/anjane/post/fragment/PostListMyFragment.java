package com.test.anjane.post.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.test.anjane.R;
import com.test.anjane.post.Post;
import com.test.anjane.post.PostDetailActivity;
import com.test.anjane.post.adapter.FirestoreAdapter;
import com.test.anjane.post.models.mPost;
import com.test.anjane.post.viewholder.PostViewHolder;

public abstract class PostListMyFragment extends Fragment {

    private static final String TAG = "PostListFragment";

    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private PostAdapter mAdapter;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;

    public PostListMyFragment() {}

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.mypost_list, container, false);

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        mRecycler = rootView.findViewById(R.id.messages_list1);
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecycler.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);

        Query postsQuery = getQuery(db);

        mAdapter = new PostAdapter(postsQuery);
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecycler.setAdapter(mAdapter);
    }

    class PostAdapter extends FirestoreAdapter<PostViewHolder> {
        PostAdapter(Query query) {
            super(query);
        }

        @Override
        public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new PostViewHolder( LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_post_my, parent, false));
        }

        @Override
        public void onBindViewHolder(PostViewHolder viewHolder, final int position) {
            DocumentSnapshot documentSnapshot = getSnapshot(position);
            mPost post = documentSnapshot.toObject( mPost.class);
            final String postKey = documentSnapshot.getId();

            viewHolder.postView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Launch PostDetailActivity
                    Intent intent = new Intent(getActivity(), PostDetailActivity.class);
                    intent.putExtra(PostDetailActivity.EXTRA_POST_KEY, postKey);
                    startActivity(intent);
                }
            });

            viewHolder.bindToPost(post, new View.OnClickListener() {
                @Override
                public void onClick(View starView) {
                    db.collection("posts").document(postKey).get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    Post post = documentSnapshot.toObject(Post.class);
                                    documentSnapshot.getReference().set(post);
                                }
                            });
                }
            });

            viewHolder.txtDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    db.collection("posts").document(postKey).get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    mPost post = documentSnapshot.toObject(mPost.class);
                                    documentSnapshot.getReference().set(post);
                                    if(post.image2 != null) delete_Image(post.image1, null, null);
                                    else if(post.image3 != null) delete_Image(post.image1, post.image2, null);
                                    else delete_Image(post.image1, post.image2, post.image3);

                                    db.collection("posts").document(postKey).delete();
                                }
                            });
                }
            });
        }

        private void delete_Image(final String imageName1, final String imageName2, final String imageName3) {
            if(imageName2 == null) {
                storage.getReference().child("postimage").child(imageName1).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getActivity(), "사진 삭제완료", Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "사진 삭제실패", Toast.LENGTH_LONG).show();
                    }
                });
            } else if (imageName3 == null) {
                storage.getReference().child("postimage").child(imageName1).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
                storage.getReference().child("postimage").child(imageName2).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getActivity(), "사진 삭제완료", Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "사진 삭제실패", Toast.LENGTH_LONG).show();
                    }
                });
            }
            else {
                storage.getReference().child("postimage").child(imageName1).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
                storage.getReference().child("postimage").child(imageName2).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
                storage.getReference().child("postimage").child(imageName3).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getActivity(), "사진 삭제완료", Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "사진 삭제실패", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mAdapter != null) {
            mAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAdapter != null) {
            mAdapter.stopListening();
        }
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public abstract Query getQuery(FirebaseFirestore databaseReference);
}