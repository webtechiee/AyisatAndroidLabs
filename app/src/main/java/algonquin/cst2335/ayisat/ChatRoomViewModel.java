package algonquin.cst2335.ayisat;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import algonquin.cst2335.ayisat.ChatMessage;

public class ChatRoomViewModel  extends ViewModel
{
    public MutableLiveData<ArrayList<ChatMessage>> messages = new MutableLiveData< >();
}