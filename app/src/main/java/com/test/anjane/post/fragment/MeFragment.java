package com.test.anjane.post.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.test.anjane.R;
import com.test.anjane.login.login;
import com.test.anjane.post.MyPostList;


public class MeFragment extends Fragment  {

    private TextView textviewUserEmail;
    private Button buttonLogout;
    private Button buttonlist;
    private Context context;
    private FirebaseAuth firebaseAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate( R.layout.fragment_me_post, container, false);

        //이메일 텍스트뷰에 밑줄
        textviewUserEmail = (TextView) v.findViewById(R.id.textviewUserEmail);
        SpannableString content = new SpannableString("Content");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        textviewUserEmail.setText(content);

        //이메일 가져오기
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        textviewUserEmail.setText(user.getEmail());

        buttonLogout = (Button) v.findViewById(R.id.buttonLogout);
        buttonlist = (Button) v.findViewById(R.id.buttonlist);

        firebaseAuth = FirebaseAuth.getInstance();

        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context = container.getContext();
                Toast.makeText( context, "정상적으로 로그아웃되었습니다. ", Toast.LENGTH_SHORT ).show();
                firebaseAuth.signOut();
                Intent intent = new Intent(getActivity(), login.class);
                startActivity(intent);
            }
        });
        buttonlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyPostList.class);
                startActivity(intent);
            }
        });

        return v;
    }
}