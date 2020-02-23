package com.adamm.sharenet.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adamm.sharenet.Database.AppDatabase;
import com.adamm.sharenet.PostDetailActivity;
import com.adamm.sharenet.R;
import com.adamm.sharenet.entities.Post;


public abstract class PostListFragment extends Fragment {

    private static final String TAG = "PostListFragment";

    private AppDatabase mDatabase;

    private RecyclerView.Adapter mAdapter;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;

    public PostListFragment() {}

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_all_posts, container, false);

        mDatabase = AppDatabase.getAppDatabase(getContext());//get database reference

        mRecycler = rootView.findViewById(R.id.postsList);
        mRecycler.setHasFixedSize(true);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Set up Layout Manager, reverse layout
        mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);

        // Set up FirebaseRecyclerAdapter with the Query
        //Query postsQuery = getQuery(mDatabase);

        mAdapter = new FirebaseRecyclerAdapter<Post, PostViewHolder>(options) {

            @Override
            public PostViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                return new PostViewHolder(inflater.inflate(R.layout.item_post, viewGroup, false));//Create a PostViewHolder for each post row
            }

            @Override
            protected void onBindViewHolder(PostViewHolder viewHolder, int position, final Post post) {
                final DatabaseReference postRef = getRef(position);
                // Set click listener for the whole post view
                final String postKey = postRef.getKey();
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Launch PostDetailActivity
                        Intent intent = new Intent(getActivity(), PostDetailActivity.class);
                        intent.putExtra(PostDetailActivity.EXTRA_POST_KEY, postKey);
                        startActivity(intent);
                    }
                });

                // Determine if the current user has liked this post and set UI accordingly
                if (post.stars.containsKey(getUid())) {
                    viewHolder.starView.setImageResource(R.drawable.ic_toggle_star_24);
                } else {
                    viewHolder.starView.setImageResource(R.drawable.ic_toggle_star_outline_24);
                }
                viewHolder.bindToPost(post, postRef);// Bind Post to ViewHolder
            }
        };
        mRecycler.setAdapter(mAdapter);
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {

        public TextView titleView;
        public TextView authorView;
        public ImageView starView;
        public TextView numStarsView;
        public TextView bodyView;

        public PostViewHolder(View itemView) {
            super(itemView);

            titleView = itemView.findViewById(R.id.postTitle);
            authorView = itemView.findViewById(R.id.postAuthor);
            starView = itemView.findViewById(R.id.star);
            numStarsView = itemView.findViewById(R.id.postNumStars);
            bodyView = itemView.findViewById(R.id.postBody);
        }

        public void bindToPost(Post post, DatabaseReference postRef) {
            final Post posta = post;
            final DatabaseReference postRefa = postRef;
            titleView.setText(post.title);
            authorView.setText(post.author);
            numStarsView.setText(String.valueOf(post.starCount));
            bodyView.setText(post.body);

            //Setting onClickListener for star button
            starView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View starView) {
                    // Need to write to both places the post is stored
                    DatabaseReference globalPostRef = mDatabase.child("posts").child(postRefa.getKey());
                    DatabaseReference userPostRef = mDatabase.child("user-posts").child(posta.uid).child(postRefa.getKey());

                    // Run two transactions
                    onStarClicked(globalPostRef);
                    onStarClicked(userPostRef);
                }
            });
        }
    }


    //Should we remove??
    private void onStarClicked(DatabaseReference postRef) {
        postRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Post p = mutableData.getValue(Post.class);
                if (p == null) {
                    return Transaction.success(mutableData);
                }

                if (p.stars.containsKey(getUid())) {
                    // Unstar the post and remove self from stars
                    p.starCount = p.starCount - 1;
                    p.stars.remove(getUid());
                } else {
                    // Star the post and add self to stars
                    p.starCount = p.starCount + 1;
                    p.stars.put(getUid(), true);
                }

                // Set value and report transaction success
                mutableData.setValue(p);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                // Transaction completed
                Log.d(TAG, "postTransaction:onComplete:" + databaseError);
            }
        });
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

    public abstract Query getQuery(DatabaseReference databaseReference);
}
