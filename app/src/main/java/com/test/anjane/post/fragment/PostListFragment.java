package com.test.anjane.post.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.test.anjane.R;
import com.test.anjane.post.Post;
import com.test.anjane.post.PostDetailActivity;
import com.test.anjane.post.adapter.FirestoreAdapter;
import com.test.anjane.post.models.mPost;
import com.test.anjane.post.viewholder.PostViewHolder;

public abstract class PostListFragment extends Fragment {

    private static final String TAG = "PostListFragment";

    private FirebaseFirestore db;
    private PostAdapter mAdapter;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;

    public PostListFragment() {}

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_all_posts, container, false);

        db = FirebaseFirestore.getInstance();
        mRecycler = rootView.findViewById(R.id.messages_list);
        mRecycler.setHasFixedSize(true);

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
                    .inflate(R.layout.item_post, parent, false));
        }

        @Override
        public void onBindViewHolder(PostViewHolder viewHolder, int position) {
            DocumentSnapshot documentSnapshot = getSnapshot(position);
            mPost post = documentSnapshot.toObject( mPost.class);

            final String postKey = documentSnapshot.getId();

            viewHolder.postView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), PostDetailActivity.class);
                    intent.putExtra( PostDetailActivity.EXTRA_POST_KEY, postKey);
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
