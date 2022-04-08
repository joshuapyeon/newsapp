package com.example.wntprototype.ui.wordmap;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.wntprototype.APIWrappers.TrendingContent;
import com.example.wntprototype.DataCache;
import com.example.wntprototype.R;
import com.example.wntprototype.databinding.FragmentWordMapBinding;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;


public class WordMapFragment extends Fragment {
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentWordMapBinding binding = FragmentWordMapBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Button confirmButton = root.findViewById(R.id.generate_button);

        confirmButton.setOnClickListener((view) -> {
            if (!DataCache.getCache().hasData()) {
                Toast.makeText(view.getContext(), "No data--try searching for something first!", Toast.LENGTH_LONG).show();
                return;
            }
            List<TrendingContent> data = DataCache.getCache().getData();

            try {
                File file = new File(this.requireContext().getFilesDir().getPath() + "/input.txt");
                if (!file.createNewFile())
                    System.out.println("File exists, skipping creation");
                if (file.setWritable(true)) {
                    PrintStream ps = new PrintStream(file);
                    for (TrendingContent a : data)
                        ps.println(a.getPhrase());
                    ps.close();
                } else
                    throw new IOException("Failed to write to input file :(");

                Toast.makeText(view.getContext(), "Generating Word map...", Toast.LENGTH_LONG).show();
                Bitmap b = WordCloudGenerator.generateWordCloud(new String[]{"-inputpath", this.requireContext().getFilesDir().getPath() + "/input.txt", "-maxfsize", "100", "-minfsize", "40", "-fstep", "2"});
                ((ImageView) WordMapFragment.this.requireView().findViewById(R.id.word_map_img)).setImageBitmap(b);
                confirmButton.setVisibility(View.GONE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        return root;
    }
}