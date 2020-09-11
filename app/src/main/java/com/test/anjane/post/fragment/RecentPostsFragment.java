package com.test.anjane.post.fragment;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class RecentPostsFragment extends PostListFragment  {

    public RecentPostsFragment() {}

    @Override
    public Query getQuery(FirebaseFirestore databaseReference) {
        return databaseReference.collection("posts").orderBy("formatDate", Query.Direction.DESCENDING);
    }
}
