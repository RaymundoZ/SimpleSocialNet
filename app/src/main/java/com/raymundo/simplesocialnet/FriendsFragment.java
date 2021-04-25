package com.raymundo.simplesocialnet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.raymundo.simplesocialnet.pojo.User;

import java.util.ArrayList;
import java.util.List;

public class FriendsFragment extends Fragment {

    private static final String TAG = "raymundo/simplesocialnet/FriendsFragment";

    private RecyclerView recyclerView;

    public static FriendsFragment newInstance() {
        Bundle bundle = new Bundle();
        FriendsFragment fragment = new FriendsFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.friends_fragment_layout, container, false);
        recyclerView = view.findViewById(R.id.recycler);
        List<User> list = new ArrayList<>();
        list.add(new User("Name", null, null, null, null, null));
        list.add(new User("Name", null, null, null, null, null));
        list.add(new User("Name", null, null, null, null, null));
        list.add(new User("Name", null, null, null, null, null));
        list.add(new User("Name", null, null, null, null, null));
        list.add(new User("Name", null, null, null, null, null));
        list.add(new User("Name", null, null, null, null, null));
        list.add(new User("Name", null, null, null, null, null));
        list.add(new User("Name", null, null, null, null, null));
        FriendsFragmentAdapter adapter = new FriendsFragmentAdapter(list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        return view;
    }


    private class FriendsFragmentViewHolder extends RecyclerView.ViewHolder {

        private MaterialTextView textView;
        private ShapeableImageView imageView;

        public FriendsFragmentViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.name);
            imageView = itemView.findViewById(R.id.image);
        }

        private void bind(User user) {
            textView.setText(user.getName());
        }
    }

    private class FriendsFragmentAdapter extends RecyclerView.Adapter<FriendsFragmentViewHolder> {

        private List<User> userList;

        public FriendsFragmentAdapter(List<User> userList) {
            this.userList = userList;
        }

        @NonNull
        @Override
        public FriendsFragmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.friend_item, parent, false);
            return new FriendsFragmentViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull FriendsFragmentViewHolder holder, int position) {
            User user = userList.get(position);
            holder.bind(user);
        }

        @Override
        public int getItemCount() {
            return userList.size();
        }
    }

    public static String getTAG() {
        return TAG;
    }
}
