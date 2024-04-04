package algonquin.cst2335.ayisat;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.ayisat.databinding.ActivityChatRoomBinding;
import algonquin.cst2335.ayisat.databinding.SentMessageBinding;



public class ChatRoom extends AppCompatActivity {

    ActivityChatRoomBinding binding;
    ChatRoomViewModel chatModel;

    ArrayList<ChatMessage> messages = new ArrayList<>();
    RecyclerView.Adapter<MyRowHolder> myAdapter;

    ChatMessageDAO mDAO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        chatModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);

        /*
         * We made the object to store the messages in a database
         */
        MessageDatabase db = Room.databaseBuilder(getApplicationContext(), MessageDatabase.class, "Messages").build();
        mDAO = db.cmDAO();

        // Get the value of the messages from the model
        messages = chatModel.messages.getValue();

        if(messages == null)
        {
            chatModel.messages.setValue(messages = new ArrayList<>());
            /*
             * Used executor to get all the messages which will be stored in the database
             */
            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() ->
            {
                messages.addAll( mDAO.getAllMessages() ); //Once you get the data from database
                runOnUiThread( () -> binding.recycleView.setAdapter( myAdapter )); //You can then load the RecyclerView
            });
        }

        chatModel.selectedMessage.observe(this, (newMessageValue) -> {

                MessageDetails chatFragment = new MessageDetails( newMessageValue );
                FragmentManager fMgr = getSupportFragmentManager();
                FragmentTransaction tx = fMgr.beginTransaction();
                tx.add(R.id.fragmentLocation, chatFragment);
                tx.commit();
        });




        binding.sendButton.setOnClickListener(click -> {
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
            String currentDateandTime = sdf.format(new Date());

            ChatMessage msg = new ChatMessage (binding.textInput.getText().toString(), currentDateandTime, true);
            messages.add(msg);

            /*
             * Used executor to insert a message in the database while sending it
             */
            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() ->
            {
                mDAO.insertMessage(msg);
            });

            myAdapter.notifyItemInserted(messages.size()-1);

            binding.textInput.setText("");
        });

        binding.receiveButton.setOnClickListener(click -> {

            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
            String currentDateandTime = sdf.format(new Date());

            ChatMessage msg = new ChatMessage (binding.textInput.getText().toString(), currentDateandTime, false);
            messages.add(msg);

            /*
             * Used executor to insert a message in the database while receiving it
             */
            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() ->
            {
                mDAO.insertMessage(msg);
            });

            //  myAdapter.notifyItemInserted(messages.size()-1);
            myAdapter.notifyDataSetChanged();

            binding.textInput.setText("");
        });


        binding.recycleView.setAdapter(myAdapter =new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                if(viewType == 0) {
                    SentMessageBinding binding = SentMessageBinding.inflate(getLayoutInflater());
                    return new MyRowHolder(binding.getRoot());
                }
                else {
                    ReceivedMessageBinding binding = ReceivedMessageBinding.inflate(getLayoutInflater());
                    return new MyRowHolder(binding.getRoot());
                }
            }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                ChatMessage obj = messages.get(position);
                holder.messageText.setText(obj.getMessage());
                holder.timeText.setText(obj.getTimeSent());
            }

            @Override
            public int getItemCount() {
                return messages.size();
            }
            public int getItemViewType(int position) {
                ChatMessage object = messages.get(position);
                if(object.getButton() ){
                    return 0;
                }
                else{
                    return 1;
                }
            }
        });
        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));
    }
    class MyRowHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        TextView timeText;
        public MyRowHolder(@NonNull View itemView) {
            super(itemView);

            /*
             * Added the listener for the working of the view when we click on the messages
             */
            AlertDialog.Builder builder = new AlertDialog.Builder( ChatRoom.this );
            itemView.setOnClickListener( e->{
               /* // Find the absolute position and store it in the object we created now
                int position = getAbsoluteAdapterPosition();
                ChatMessage selected = messages.get(position);


                builder.setMessage("Do you want to delete the message: " + messageText.getText())
                    .setTitle("Question: ")
                    .setNegativeButton("No", ((dialog, cl) -> { }))
                    .setPositiveButton("Yes", ((dialog, cl) -> {


                        ChatMessage m = messages.get(position);

                        ChatMessage removedMessage = messages.get(position);


                        Executor thread = Executors.newSingleThreadExecutor();
                        thread.execute(() ->
                        {
                            mDAO.deleteMessage(m);
                        });

                        // Removes the message and notify the adapter that the message is deleted
                        messages.remove(position);
                        myAdapter.notifyItemRemoved(position);


                        Snackbar.make(messageText, "You deleted message #" + position, Snackbar.LENGTH_LONG)
                                .setAction("Undo", clc -> {
                                    messages.add(position, removedMessage);
                                    myAdapter.notifyItemInserted(position);
                                })
                                .show();
                    }))
                    .create().show();*/

                int position = getAbsoluteAdapterPosition();
                ChatMessage selected = messages.get(position);
                chatModel.selectedMessage.postValue(selected);
            });



            messageText = itemView.findViewById(R.id.message);
            timeText = itemView.findViewById(R.id.time);
        }
    }
}