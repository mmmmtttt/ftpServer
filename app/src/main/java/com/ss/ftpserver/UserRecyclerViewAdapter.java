package com.ss.ftpserver;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ss.ftpserver.databinding.UserListItemLayoutBinding;

import java.util.List;

public class UserRecyclerViewAdapter extends RecyclerView.Adapter<UserRecyclerViewAdapter.UserViewHolder> {

    private final List<User> users;

    public UserRecyclerViewAdapter(List<User> items) {
        users = items;
    }

    //创建一个ViewHolder，可以根据viewType的不同而创建不同的ViewHolder实现列表页各种不一样的item
    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new UserViewHolder(UserListItemLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    //在ViewHolder中绑定数据
    @Override
    public void onBindViewHolder(final UserViewHolder holder, int position) {
        holder.user = users.get(position);
        holder.name.setText(users.get(position).name);
        holder.pass.setText(users.get(position).pass);
        holder.path.setText(users.get(position).path);
        holder.legal.setText(users.get(position).isLegal?"legal":"illegal");
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        public final TextView name;
        public final TextView pass;
        public final TextView path;
        public final TextView legal;
        public User user;

        public UserViewHolder(UserListItemLayoutBinding binding) {
            super(binding.getRoot());
            name = binding.userName;
            pass = binding.userPassword;
            path = binding.userPath;
            legal = binding.userLegal;
        }
    }

}