package com.ss.ftpserver.gui;

import androidx.recyclerview.widget.RecyclerView;

import android.app.AppOpsManager;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ss.ftpserver.databinding.UserListItemLayoutBinding;
import com.ss.ftpserver.ftpService.Settings;
import com.ss.ftpserver.ftpService.User;

import java.util.List;
import java.util.Set;

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
        holder.legal.setText(users.get(position).isLegal?"legal":"illegal");
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteUser(holder.getAbsoluteAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        public final TextView name;
        public final TextView pass;
        public final TextView legal;
        public final ImageButton deleteButton;
        public User user;

        public UserViewHolder(UserListItemLayoutBinding binding) {
            super(binding.getRoot());
            name = binding.userName;
            pass = binding.userPassword;
            legal = binding.userLegal;
            deleteButton = binding.userDeleteBtn;
        }
    }

    public void deleteUser(int position){
        users.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
        //保存sp中的user信息
        Settings.writeUsers(users);
    }

    public void addUser(User user){
        int position = users.size();
        users.add(position,user);
        //添加动画
        notifyItemInserted(position);
        //保存sp中的user信息
        Settings.writeUsers(users);
    }
}