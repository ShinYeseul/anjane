package com.test.anjane.post.fragment;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MyPostsFragment extends PostListMyFragment {

    public MyPostsFragment() {}

    @Override
    public Query getQuery(FirebaseFirestore databaseReference) {
        return databaseReference.collection("posts").whereEqualTo("uid", getUid()).orderBy("formatDate", Query.Direction.DESCENDING);
    }
}
