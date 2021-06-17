package com.example.resultmap;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.CustomViewHolder> {

    private ArrayList<com.example.resultmap.RoomData> arrayList;

    public RoomAdapter(ArrayList<com.example.resultmap.RoomData> arrayList)
    {
        this.arrayList = arrayList;
    }


    @NonNull
    @Override
    public com.example.resultmap.RoomAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.romm_list,parent,false);
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull com.example.resultmap.RoomAdapter.CustomViewHolder holder, int position) {
        if(holder.roomTitle != null)
         holder.roomTitle.setText("방이름 : " + arrayList.get(position).getName());
        if(holder.roomLeftUser != null)
            holder.roomLeftUser.setText(arrayList.get(position).getUserLimit()+"/4");
        if(holder.roomGender != null)
            holder.roomGender.setText("성별 : " + arrayList.get(position).getGender());
        if (holder.roomTime != null)
         holder.roomTime.setText("모이는 시간 : " + arrayList.get(position).getStartAt());
        if(holder.roomPlace != null)
         holder.roomPlace.setText("위치 : " + arrayList.get(position).getPlace());

        holder.roomList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("**************눌렸어******************");
//               채팅방 누르면 반응하는곳
//                여기에 arrayList.get(position).getId() 로 방아이디를 불러옴
//                intent 로 chatting.java 파일로 방 아이디를 넘겨주면서 넘어감
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        protected TextView roomTitle;
        protected TextView roomLeftUser;
        protected TextView roomGender;
        protected TextView roomTime;
        protected TextView roomPlace;
        protected LinearLayout roomList;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.roomTitle = (TextView) itemView.findViewById(R.id.roomTitle);
            this.roomLeftUser = (TextView) itemView.findViewById(R.id.roomLeftUser);
            this.roomGender = (TextView) itemView.findViewById(R.id.roomGender);
            this.roomTime = (TextView) itemView.findViewById(R.id.roomTime);
            this.roomPlace = (TextView) itemView.findViewById(R.id.roomPlace);
            this.roomList = (LinearLayout) itemView.findViewById(R.id.roomList);

        }
    }
}
