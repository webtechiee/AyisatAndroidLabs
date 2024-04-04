package algonquin.cst2335.ayisat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import algonquin.cst2335.ayisat.ChatMessage;
import algonquin.cst2335.ayisat.databinding.DetailsLayoutBinding;

public class MessageDetails extends Fragment {

    ChatMessage selected;

    public MessageDetails(ChatMessage m){
        selected = m;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        DetailsLayoutBinding binding = DetailsLayoutBinding.inflate(inflater);

        binding.messageText.setText(selected.getMessage());
        binding.timeText.setText(selected.getTimeSent());
        binding.sendReceive.setText(selected.getButton()?"Send":"Receive");
        binding.databaseText.setText("Id = " + selected.id);

        return binding.getRoot();
    }
}