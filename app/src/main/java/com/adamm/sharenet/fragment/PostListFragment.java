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

import java.util.List;


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
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        List<Post> posts = getQuery(mDatabase);
        mAdapter = new PostAdapter(posts);//Posts query list result
        mRecycler.setAdapter(mAdapter);
    }

    private class PostAdapter extends RecyclerView.Adapter<PostViewHolder> {
        private List<Post> mPosts;

        public List<Post> getmPosts() {
            return mPosts;
        }

        public PostAdapter(List<Post> posts) {
            mPosts = posts;
        }

        @Override
        public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {// This is called by RecyclerView whenever it needs a new holder
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            return new PostViewHolder(inflater, parent);//Create a PostViewHolder for each post row
        }

        @Override
        public void onBindViewHolder(PostViewHolder holder, int position) {//Get list node via ID to view
            Post post = mPosts.get(position);
            holder.bindToPost(post);// Bind Post to ViewHolder
        }

        @Override
        public int getItemCount() {
            return mPosts.size();
        }
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {

        public TextView titleView;
        public TextView authorView;
        public ImageView starView;
        public TextView numStarsView;
        public TextView bodyView;

        public PostViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_post, parent, false));
            titleView = itemView.findViewById(R.id.postTitle);
            authorView = itemView.findViewById(R.id.postAuthor);
            starView = itemView.findViewById(R.id.star);
            numStarsView = itemView.findViewById(R.id.postNumStars);
            bodyView = itemView.findViewById(R.id.postBody);
        }

        public void bindToPost(Post post) {
            final Post posta = post;
            titleView.setText(post.title);
            authorView.setText(post.author);
            numStarsView.setText(String.valueOf(post.starCount));
            bodyView.setText(post.body);
        }
    }
    public abstract List<Post> getQuery(AppDatabase databaseReference);
}
