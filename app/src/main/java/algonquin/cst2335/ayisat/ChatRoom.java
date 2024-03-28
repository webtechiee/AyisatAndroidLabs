package algonquin.cst2335.ayisat;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.ayisat.databinding.ReceiveMessageBinding;
import algonquin.cst2335.ayisat.databinding.RoomChatBinding;
import algonquin.cst2335.ayisat.databinding.SentMessageBinding;

public class ChatRoom extends AppCompatActivity {
    RoomChatBinding binding;
    ChatRoomViewModel chatModel ;
    ArrayList<ChatMessage> messages = new ArrayList<>();

    RecyclerView.Adapter<MyRowHolder> myAdpter;
    ChatMessageDAO mDAO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.room_chat);

        binding= RoomChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        chatModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);

        MessageDatabase db = Room.databaseBuilder(getApplicationContext(), MessageDatabase.class,
                "Messages").build();

        mDAO = db.cmDAO();


        messages = chatModel.messages.getValue();


        if(messages == null)
        {
            chatModel.messages.setValue(messages = new ArrayList<>());
            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() ->
            {
                messages.addAll( mDAO.getAllMessages() ); //Once you get the data from database
                runOnUiThread( () -> binding.recyclerView.setAdapter( myAdpter )); //You can then load the RecyclerView
            });
        }




        binding.recyclerView.setAdapter(myAdpter=new RecyclerView.Adapter<MyRowHolder>(){

            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
                if (viewType==0)
                {
                    SentMessageBinding binding= SentMessageBinding.inflate(getLayoutInflater());
                    return new MyRowHolder(binding.getRoot());}
                else {
                    ReceiveMessageBinding binding= ReceiveMessageBinding.inflate(getLayoutInflater());
                    return new MyRowHolder(binding.getRoot());
                }

            }
            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position){
                ChatMessage obj = messages.get(position);
                holder.messageText.setText(obj.getMessage());
                holder.timeText.setText(obj.getTimeSent());

            }
            @Override
            public int getItemCount(){
                return messages.size();

            }
            @Override
            public  int getItemViewType(int position){
                ChatMessage obj = messages.get(position);
                if (obj.isSentButton()==true)

                    return 0; // sender message
                else
                    return 1;// receiver message
            }



        });
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        binding.sendButton.setOnClickListener(click->{
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
            String currentDateandTime = sdf.format(new Date());
            ChatMessage obj=new ChatMessage(binding.inputText.getText().toString(),currentDateandTime,true);
            messages.add(obj);
            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() ->
            {
                mDAO.insertMessage(obj);});
            myAdpter.notifyItemInserted(messages.size()-1);
            binding.inputText.setText("");



        });
        binding.receiveButton.setOnClickListener(click->{
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
            String currentDateandTime = sdf.format(new Date());
            ChatMessage obj=new ChatMessage(binding.inputText.getText().toString(),currentDateandTime,false);
            messages.add(obj);
            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() ->
            {
                mDAO.insertMessage(obj);});
            myAdpter.notifyItemInserted(messages.size()-1);

            binding.inputText.setText("");

        });

    }

    class MyRowHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        TextView timeText;



        public MyRowHolder(@NonNull View itemView) {
            super(itemView);
            AlertDialog.Builder builder = new AlertDialog.Builder( ChatRoom.this );
            itemView.setOnClickListener(e->{
                int position=getAbsoluteAdapterPosition();
                ChatMessage m=messages.get(position);

                builder.setMessage("Do you want to delete the message:" +messageText.getText());
                builder.setTitle("Question:");
                builder.setNegativeButton("NO", (dialog, cl)->{});
                builder.setPositiveButton( "Yes", (dialog, cl)->{
                    Executor thread = Executors.newSingleThreadExecutor();
                    thread.execute(() ->
                    {
                        mDAO.deleteMessage(m);});

                    messages. remove(position);
                    myAdpter.notifyItemRemoved(position);
                    Snackbar.make(e,"You deleted message #" , Snackbar.LENGTH_LONG)
                            .setAction (  "Undo", clk -> {

                                messages.add (position, m); myAdpter.notifyItemInserted(position);})
                            .show();
                });
                builder.create().show();





            });

            messageText=itemView.findViewById(R.id.message);
            timeText=itemView.findViewById(R.id.time);

        }
    }
}
