package algonquin.cst2335.ayisat;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {
    // MutableLiveData to track the boolean value and notify observers
    public MutableLiveData<Boolean> isSomethingEnabled = new MutableLiveData<>();

    // Constructor (if needed)
    public MainViewModel() {
        // Initialize the MutableLiveData if needed
        isSomethingEnabled.setValue(false);
    }
}
