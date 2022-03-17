package com.example.wntprototype.ui.wordmap;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.wntprototype.R;
import com.example.wntprototype.databinding.FragmentWordMapBinding;


public class WordMapFragment extends Fragment {
    private FragmentWordMapBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        WordMapViewModel wordMapViewModel =
                new ViewModelProvider(this).get(WordMapViewModel.class);

        binding = FragmentWordMapBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textNotifications;
        wordMapViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        Button confirmButton = root.findViewById(R.id.button);
        EditText emailText = (EditText) root.findViewById(R.id.editTextTextEmailAddress3);

        //Log.d("storedEmail1", storedEmail);
        confirmButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){
                //Log.d("storedEmail", storedEmail);
                String storedEmail = emailText.getText().toString();
                Toast.makeText(view.getContext(), "You have now stored your the following email account to share news: " + storedEmail, Toast.LENGTH_LONG).show();
                //Snackbar.make(view, "You requested to receive an email to add a news source!", Snackbar.LENGTH_LONG)
                //  .setAction("Action", null).show();
           }
        });

        return root;


    }

   @Override
    public void onDestroyView() {
       super.onDestroyView();
        binding = null;
    }


}