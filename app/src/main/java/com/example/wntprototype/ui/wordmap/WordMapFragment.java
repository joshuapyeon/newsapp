package com.example.wntprototype.ui.wordmap;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.example.wntprototype.APIWrappers.APIData;
import com.example.wntprototype.DataCache;
import com.example.wntprototype.R;
import com.example.wntprototype.databinding.FragmentWordMapBinding;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;


public class WordMapFragment extends Fragment {
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentWordMapBinding binding = FragmentWordMapBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Button confirmButton = root.findViewById(R.id.button);

        confirmButton.setOnClickListener((view) -> {
            List<APIData> data = DataCache.getCache().getData();
            if (data == null || data.isEmpty()) {
                Toast.makeText(view.getContext(), "No data--try searching for something first!", Toast.LENGTH_LONG).show();
                return;
            }

            try {
                File file = new File(this.getContext().getFilesDir().getPath() + "/input.txt");
                file.createNewFile();
                if (file.setWritable(true)) {
                    PrintStream ps = new PrintStream(file);
                    for (APIData a : data)
                        ps.println(a.getToParse());
                    ps.close();
                } else
                    throw new IOException("Failed to write to input file :(");

                String drawablesPath = this.getContext().getDir("drawable", Context.MODE_PRIVATE).getPath();
                Toast.makeText(view.getContext(), "Generating Word map...", Toast.LENGTH_LONG).show();
                String[] cmd = { "res/word_cloud_res/wordcloudgenerator.jar", "-maskpath", drawablesPath + "/mask_circle.png", "-inputpath", this.getContext().getFilesDir().getPath() + "/input.txt", "-outputpath", this.getContext().getFilesDir().getPath() + "/output.png" };
                Process p = Runtime.getRuntime().exec(cmd);
                p.waitFor();

                ((ImageView) WordMapFragment.this.requireView().findViewById(R.id.word_map_img)).setImageURI(new Uri.Builder().path(this.getContext().getFilesDir().getPath() + "/output.png").build());
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });

        return root;
    }
}