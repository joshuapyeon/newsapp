package com.example.wntprototype.ui.wordmap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import com.example.wntprototype.MainActivity;
import com.example.wntprototype.R;
import com.example.wntprototype.databinding.FragmentWordMapBinding;
import com.example.wntprototype.ui.Shareable;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class WordMapFragment extends Fragment implements Shareable {
    private static Bitmap word_map_img = null;

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

                Executor executor = Executors.newSingleThreadExecutor();
                Handler handler = new Handler(Looper.getMainLooper());
                this.requireView().findViewById(R.id.progressBar).setVisibility(VISIBLE);
                this.requireView().findViewById(R.id.textView).setVisibility(VISIBLE);
                executor.execute(() -> {
                    word_map_img = WordCloudGenerator.generateWordCloud(0xFF000000, 0xFF000000, 50, 35, 2, 100, 2, BitmapFactory.decodeResource(WordMapFragment.this.requireContext().getResources(), R.drawable.mask_circle_300), null, WordMapFragment.this.requireContext().getFilesDir().getPath() + "/input.txt");
                    handler.post(() -> {
                        ((ImageView) MainActivity.currVisualizationFrag.requireView().findViewById(R.id.word_map_img)).setImageBitmap(word_map_img);
                        this.requireView().findViewById(R.id.progressBar).setVisibility(GONE);
                        this.requireView().findViewById(R.id.textView).setVisibility(GONE);
                        Toast.makeText(view.getContext(), "Finished!", Toast.LENGTH_LONG).show();
                    });
                });
                confirmButton.setVisibility(GONE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return root;
    }

    @Override
    public String getSharingType() {
        return "image/png";
    }

    @Override
    public File getSharingContent() {
        try {
            File parentDirectory = new File(this.requireContext().getFilesDir().getPath() + "/share");
            if (!parentDirectory.mkdir())
                System.out.println("Parent directory exists, skipping creation");
            File file = new File(parentDirectory, "/image.png");
            if (!file.createNewFile())
                System.out.println("File exists, skipping creation");
            PrintStream s = new PrintStream(file);
            if (file.setWritable(true)) {
                word_map_img.compress(Bitmap.CompressFormat.PNG, 100, s);
            } else
                throw new IOException("Failed to write to input file :(");
            s.close();
            return file;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}