package com.adamm.sharenet.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adamm.sharenet.Database.AppDatabase;
import com.adamm.sharenet.PostDetailDialog;
import com.adamm.sharenet.R;
import com.adamm.sharenet.ViewModel.PostViewModel;
import com.adamm.sharenet.entities.Post;

import java.util.List;


public abstract class PostListFragment extends Fragment {

    private static final String TAG = "PostListFragment";

    private AppDatabase mDatabase;

    public PostAdapter mAdapter;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;

    private PostViewModel postViewModel;

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
      //  LiveData<List<Post>> livePosts = getQuery(mDatabase);
      /*  livePosts.observe(this, new Observer<List<Post>>() {
            @Override
            public void onChanged(List<Post> posts) {
                mAdapter.setData(posts);
            }

        });

       */

        postViewModel = ViewModelProviders.of(this).get(PostViewModel.class);

        if(getWho() == 1) {
            postViewModel.getAllPosts().observe(getViewLifecycleOwner(), new Observer<List<Post>>() {
                @Override
                public void onChanged(List<Post> posts) {
                    mAdapter.setData(posts);
                }
            });

        }
        else {
            postViewModel.getMyPosts().observe(getViewLifecycleOwner(), new Observer<List<Post>>() {
                @Override
                public void onChanged(List<Post> posts) {
                    mAdapter.setData(posts);
                }
            });
        }

       // List<Post> posts = livePosts.getValue();
        mAdapter = new PostAdapter(postViewModel.getAllPosts().getValue());//Posts query list result
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
            if(mPosts == null)
                return 0;
            return mPosts.size();
        }

        public void setData(List<Post> newPosts){
            mPosts = newPosts;
            notifyDataSetChanged();
        }
    }

    public class PostViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

        public TextView titleView;
        public TextView authorView;
        public ImageView deleteView;
        public TextView bodyView;
        public Post posta;

        public PostViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_post, parent, false));
            titleView = itemView.findViewById(R.id.postTitle);
            authorView = itemView.findViewById(R.id.postAuthor);
            deleteView = itemView.findViewById(R.id.delete);
            deleteView.setImageResource(R.drawable.ic_delete_24px);
            bodyView = itemView.findViewById(R.id.postBody);
            itemView.setOnLongClickListener(this);
        }

        public void bindToPost(Post post) {//BUG NEEDS TO BE FIXED
            posta = post;
            titleView.setText(post.title);
            authorView.setText(post.author);
            if(AppDatabase.curr_user.uid != posta.uid)
                deleteView.setVisibility(View.INVISIBLE);
            else {
                deleteView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setMessage("Are you sure you want to delete this post?")
                                .setTitle("Delete post");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                postViewModel.deletePost(posta);
                            }
                        });
                        builder.setNegativeButton("Cancel", null);
                        builder.create().show();
                    }
                });
            }
            bodyView.setText(post.body);
        }

        @Override
        public boolean onLongClick(View view) {
            PostDetailDialog dialog = new PostDetailDialog(new Post(AppDatabase.curr_user.uid,authorView.getText().toString(),titleView.getText().toString(),bodyView.getText().toString()));
            dialog.show(getActivity().getSupportFragmentManager(), "Post " );
            return false;
        }
    }
    public abstract LiveData<List<Post>> getQuery(AppDatabase databaseReference);

    public abstract  int getWho() ;
}
