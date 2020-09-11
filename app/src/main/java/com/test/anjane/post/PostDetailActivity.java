package com.test.anjane.post;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.test.anjane.R;
import com.test.anjane.post.models.Comment;
import com.test.anjane.post.models.User;
import com.test.anjane.post.models.mPost;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class PostDetailActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "PostDetailActivity";
    public static final String EXTRA_POST_KEY = "post_key";

    private DocumentReference mPostReference;
    private String mPostKey;
    private CommentAdapter mAdapter;
    private TextView mAddressView;
    private TextView mTitleView;
    private TextView mBodyView;
    private TextView mDateView;
    private TextView mAuthorView;
    private EditText mCommentField;
    private Button mCommentButton;
    private RecyclerView mCommentsRecycler;
    FirebaseStorage storage = FirebaseStorage.getInstance("gs://anjane-821f9.appspot.com");
    StorageReference storageRef = storage.getReference();
    private ImageView iv1;
    private ImageView iv2;
    private ImageView iv3;
    private TextView mspin1View;
    private String imagemane1;
    private String imagemane2;
    private String imagemane3;
    private MapView mMapView;
    private MapPOIItem mDefaultMarker;
    final Geocoder geocoder = new Geocoder(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        iv1 = findViewById(R.id.postImage1);
        iv2 = findViewById(R.id.postImage2);
        iv3 = findViewById(R.id.postImage3);

        mPostKey = getIntent().getStringExtra(EXTRA_POST_KEY);
        if (mPostKey == null) {
            throw new IllegalArgumentException("Must pass EXTRA_POST_KEY");
        }

        mPostReference = FirebaseFirestore.getInstance().collection("posts").document(mPostKey);

        mAddressView = findViewById(R.id.post_address);
        mTitleView = findViewById(R.id.post_title);
        mBodyView = findViewById(R.id.post_body);
        mDateView = findViewById(R.id.post_date);
        mAuthorView = findViewById(R.id.post_author);
        mCommentField = findViewById(R.id.field_comment_text);
        mCommentButton = findViewById(R.id.button_post_comment);
        mCommentsRecycler = findViewById(R.id.recycler_comments);
        mspin1View = findViewById( R.id.post_category );

        mCommentButton.setOnClickListener(this);
        mCommentsRecycler.setLayoutManager(new LinearLayoutManager(this));

        iv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PostDetailActivity.this, Photo.class);
                i.putExtra("iv", imagemane1);
                startActivity(i);
            }
        });
        iv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PostDetailActivity.this, Photo.class);
                i.putExtra("iv", imagemane2.toString());
                startActivity(i);
            }
        });
        iv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PostDetailActivity.this, Photo.class);
                i.putExtra("iv", imagemane3.toString());
                startActivity(i);
            }
        });
        mMapView = (MapView) findViewById(R.id.map_view);
        mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff);
        mMapView.setShowCurrentLocationMarker(false);
        mMapView.setZoomLevel(2,true);
    }

    public void getImage1() {
        final StorageReference islandRef = storageRef.child("postimage/" + imagemane1);
        islandRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    // Glide 이용하여 이미지뷰에 로딩
                    Glide.with(PostDetailActivity.this)
                            .load(task.getResult())
                            .into(iv1);
                } else {
                    // URL을 가져오지 못하면 토스트 메세지
                    Toast.makeText(PostDetailActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }

    public void getImage2() {
        final StorageReference islandRef = storageRef.child("postimage/" + imagemane2);
        islandRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    // Glide 이용하여 이미지뷰에 로딩
                    Glide.with(PostDetailActivity.this)
                            .load(task.getResult())
                            .into(iv2);
                } else {
                    // URL을 가져오지 못하면 토스트 메세지
                    Toast.makeText(PostDetailActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }

    public void getImage3() {
        final StorageReference islandRef = storageRef.child("postimage/" + imagemane3);
        islandRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    // Glide 이용하여 이미지뷰에 로딩
                    Glide.with(PostDetailActivity.this)
                            .load(task.getResult())
                            .into(iv3);
                } else {
                    // URL을 가져오지 못하면 토스트 메세지
                    Toast.makeText(PostDetailActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mPostReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                if(e != null) {
                    Log.w(TAG, "Listen failed", e);
                    return;
                }
                mPost post = snapshot.toObject(mPost.class);
                mAddressView.setText(post.address);
                List<Address> list = null;
                String str = mAddressView.getText().toString();
                try {
                    list = geocoder.getFromLocationName
                            (str, // 지역 이름
                                    10); // 읽을 개수
                } catch (IOException ex) {
                    ex.printStackTrace();
                    Log.e("test","입출력 오류 - 서버에서 주소변환시 에러발생");
                }

                if (list != null) {
                    if (list.size() == 0) {
                    } else {
                        // 해당되는 주소로 인텐트 날리기
                        Address addr = list.get( 0 );
                        double lat = addr.getLatitude();
                        double lon = addr.getLongitude();

                        MapPoint DEFAULT_MARKER_POINT = MapPoint.mapPointWithGeoCoord(lat, lon);

                        mDefaultMarker = new MapPOIItem();
                        mDefaultMarker.setTag( 0 );
                        mDefaultMarker.setItemName(post.address);
                        mDefaultMarker.setMapPoint( DEFAULT_MARKER_POINT );
                        mDefaultMarker.setMarkerType( MapPOIItem.MarkerType.BluePin );
                        mDefaultMarker.setSelectedMarkerType( MapPOIItem.MarkerType.RedPin );

                        mMapView.addPOIItem( mDefaultMarker );
                        mMapView.selectPOIItem( mDefaultMarker, true );
                        mMapView.setMapCenterPoint( DEFAULT_MARKER_POINT, false );
                    }
                }

                mTitleView.setText(post.title);
                mBodyView.setText(post.body);
                mDateView.setText(post.date);
                mAuthorView.setText("신고자: " + post.author);
                imagemane1 = post.image1;
                if(post.image2 != null) imagemane2 = post.image2;
                if(post.image3 != null) imagemane3 = post.image3;
                mspin1View.setText(post.spin1 + "/" + post.spin2);
                getImage1();
                if(imagemane2 != null) getImage2();
                if(imagemane3 != null) getImage3();
            }

        });
        mAdapter = new CommentAdapter(mPostReference.collection("post-comments"));
        mCommentsRecycler.setAdapter(mAdapter);
    }

    @Override
    public void onStop() {
        super.onStop();
        if(mAdapter != null) {
            mAdapter.cleanupListener();
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if(i == R.id.button_post_comment) {
            postComment();
        }
    }

    private void postComment() {
        final String uid = getUid();
        DocumentReference docRef = FirebaseFirestore.getInstance().collection("users").document(uid);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);
                if(user == null) {
                    Log.e(TAG, "User " + uid + " is unexpectedly null");
                    Toast.makeText( PostDetailActivity.this, "Error: could not fetch user.", Toast.LENGTH_SHORT).show();
                } else {
                    Comment comment = new Comment(uid, user.username, mCommentField.getText().toString());
                    mPostReference.collection("post-comments").document().set(comment);
                    mCommentField.setText(null);
                }
            }
        });
    }

    private static class CommentViewHolder extends RecyclerView.ViewHolder {

        public TextView authorView;
        public TextView bodyView;

        CommentViewHolder(View itemView) {
            super(itemView);

            authorView = itemView.findViewById( R.id.comment_author);
            bodyView = itemView.findViewById( R.id.comment_body);
        }

    }

    private static class CommentAdapter extends RecyclerView.Adapter<CommentViewHolder> {
        private List<String> mCommentIds = new ArrayList<>();
        private List<Comment> mComments = new ArrayList<>();

        private ListenerRegistration listenerRegistration;

        public CommentAdapter(Query query) {
            //하위 이벤트 수신기 만들기
            EventListener childEventListener = new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                    if (e != null) return;
                    String commentKey;
                    int commentIndex;
                    Comment comment;

                    for (DocumentChange dc : snapshots.getDocumentChanges()) {
                        switch (dc.getType()) {
                            case ADDED:
                                comment = dc.getDocument().toObject( Comment.class);
                                mCommentIds.add(dc.getDocument().getId());
                                mComments.add(comment);
                                notifyItemInserted(mComments.size() - 1);
                                break;
                            case MODIFIED:
                                comment = dc.getDocument().toObject( Comment.class);
                                commentKey = dc.getDocument().getId();
                                commentIndex = mCommentIds.indexOf(commentKey);
                                if (commentIndex > -1) {
                                    mComments.set(commentIndex, comment);
                                    notifyItemChanged(commentIndex);
                                } else {
                                    Log.w(TAG, "onChildChanged:unknown_child: " + commentKey);
                                }
                                break;
                            case REMOVED:
                                commentKey = dc.getDocument().getId();
                                commentIndex = mCommentIds.indexOf(commentKey);
                                if (commentIndex > -1) {
                                    mCommentIds.remove(commentIndex);
                                    mComments.remove(commentIndex);

                                    notifyItemRemoved(commentIndex);
                                } else {
                                    Log.w(TAG, "onChildRemoved:unknown_child: " + commentKey);
                                }
                                break;
                        }
                    }
                }
            };
            listenerRegistration = query.addSnapshotListener(childEventListener);
        }

        @Override
        public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate( R.layout.item_comment, parent, false);
            return new CommentViewHolder(view);
        }

        @Override
        public void onBindViewHolder(CommentViewHolder holder, int position) {
            Comment comment = mComments.get(position);
            holder.authorView.setText(comment.author);
            holder.bodyView.setText(comment.text);
        }

        @Override
        public int getItemCount() { return mComments.size(); }

        public void cleanupListener() { listenerRegistration.remove(); }
    }
}