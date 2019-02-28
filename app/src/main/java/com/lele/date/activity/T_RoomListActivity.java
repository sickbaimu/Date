package com.lele.date.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lele.date.R;
import com.lele.date.entity.MeetingRoom;
import com.lele.date.entity.ReserverInfo;
import com.lele.date.faker.Client;
import com.lele.date.faker.Server;
import com.lele.date.fragment.MyDialogFragment;

import java.util.List;

public class T_RoomListActivity extends AppCompatActivity {
    /**
     * 按时间选择预约模式下的会议室选择页面
     */
    static ReserverInfo meeting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_list);
        //从intent获取meeting对象
        meeting = (ReserverInfo)getIntent().getSerializableExtra("meeting");
        //设置适会议室配器
        RecyclerView recyclerView =  findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        RoomAdapter roomAdapter = new RoomAdapter(Server.getMeetingRoomsByOrgId(Client.getOrgId()));
        recyclerView.setAdapter(roomAdapter);
    }
    class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.ViewHolder> {
        private List<MeetingRoom> roomList;
        class ViewHolder extends RecyclerView.ViewHolder{
            View view;//整个控件组
            ImageView imageView;//显示图片
            TextView textView;//显示会议室名
            TextView id;//会议室的id（隐藏）
            TextView info;//会议室的最大容量（目前先只显示这个信息）

            ViewHolder(View view) {
                super(view);
                this.view = view;
                imageView = view.findViewById(R.id.room_image);
                textView = view.findViewById(R.id.room_name);
                id = view.findViewById(R.id.room_id);
                info = view.findViewById(R.id.room_info);
            }
        }

        public RoomAdapter(List<MeetingRoom> roomList)
        {
            this.roomList = roomList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
            final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_room,parent,false);
            ViewHolder holder = new ViewHolder(view);
            holder.view.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    //从CradView获取到会议室的id，访问服务器获取会议室对象
                    CardView cardView =(CardView)view;
                    TextView textView = cardView.findViewById(R.id.room_id);
                    String roomid = textView.getText().toString();
                    MeetingRoom room = Server.getMeetingRoomByIdAndOrgId(Integer.valueOf(roomid), Client.getOrgId());
                    meeting.setReserverId(room.getTrans_id());//为会议设置会议室
                    MyDialogFragment myDialogFragment = new MyDialogFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("meeting", meeting);
                    myDialogFragment.setArguments(bundle);
                    myDialogFragment.show(getFragmentManager(),"Dialog");
                }
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position){
            MeetingRoom room = roomList.get(position);
            holder.imageView.setImageResource(R.drawable.img_room_free);
            holder.textView.setText(room.getRoomName());
            holder.id.setText(String.valueOf(room.getTrans_id()));
            holder.info.setText("最大容量".concat(String.valueOf(room.getCon())));
        }

        @Override
        public int getItemCount(){
            return roomList.size();
        }
    }

}
