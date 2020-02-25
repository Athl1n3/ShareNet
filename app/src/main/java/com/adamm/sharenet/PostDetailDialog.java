package com.adamm.sharenet;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.adamm.sharenet.R;
import com.adamm.sharenet.entities.Post;

import org.w3c.dom.Text;

public class PostDetailDialog extends DialogFragment {

    TextView bodytxt;
    TextView authortxt;
    TextView titleTxt;


    public PostDetailDialog(Post post) {//Get current bar progress from host activity
        Bundle b = new Bundle();
        b.putString("username",post.author );
        b.putString("body",post.body );
        b.putString("title",post.title );
        setArguments(b);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View v = requireActivity().getLayoutInflater().inflate(R.layout.activity_post_detail, null);
        bodytxt = v.findViewById(R.id.postBody);
        authortxt = v.findViewById(R.id.postAuthor);
        titleTxt = v.findViewById(R.id.postTitle);

        bodytxt.setText(getArguments().getString("body"));
        authortxt.setText(getArguments().getString("username"));
        builder.setTitle(getArguments().getString("title")).setView(v);

        return builder.create();
    }
}
