package com.example.wntprototype;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.wntprototype.APIWrappers.APISearch;
import com.example.wntprototype.APIWrappers.DataManager;
import com.example.wntprototype.APIWrappers.DataSource;
import com.example.wntprototype.databinding.ActivityMainBinding;
import com.example.wntprototype.ui.HomeFragment;
import com.example.wntprototype.ui.Shareable;
import com.example.wntprototype.ui.graph.GraphFragment;
import com.example.wntprototype.ui.list.ListFragment;
import com.example.wntprototype.ui.wordmap.WordMapFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    /**
     * The different view options
     */
    private final String[] visualizations = {"List", "Graph", "Word Map"};

    /**
     * The list of different data sources that the application can pull from
     */
    private final EnumSet<DataSource> sources = EnumSet.allOf(DataSource.class);

    /**
     * Lets the Strings convert back into Enums
     */
    private final Map<String, DataSource> stringToEnum = new HashMap<>();

    /**
     * Gives easy access to the currently selected data source
     */
    private DataSource currentDataSource = null;

    /**
     * Gives easy access to the current Keyword
     */
    private String currentKeyword = "";

    /**
     * The variable that identifies the fragment we want
     */
    private int currVisualization = 0;
    /**
     * The current fragment that we are seeing
     */
    public static Fragment currVisualizationFrag = new ListFragment();

    /**
     * the global cache
     */
    private final DataCache cache = DataCache.getCache();

    /**
     * The Main Activity xml Binding
     */
    private ActivityMainBinding binding;


    /**
     * Creates the user starting user interface
     * @param savedInstanceState The saved state of the application
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Initializes the different options for the Data from the APIs
        String[] dataSources = new String[sources.size()];
        int i = 0;
        for (DataSource temp : sources) {
            dataSources[i] = temp.toString();
            stringToEnum.put(temp.toString(), temp);
            i++;
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this.getBaseContext(), R.layout.dropdown_item, dataSources);
        binding.dataSearchText.setAdapter(arrayAdapter);

        //Hides the Search Drop Down
        findViewById(R.id.filter_view).setVisibility(View.GONE);

        //Tells the search dropdown button to make the filters visible
        findViewById(R.id.search_dropdown_button).setOnClickListener((u1) ->
                findViewById(R.id.filter_view).setVisibility(toggleVisibility()));

        //This tells the search button to execute the search
        findViewById(R.id.button_search).setOnClickListener((u1) -> executeSearch());

        //This tells the close button to close the search drop down
        findViewById(R.id.close_search).setOnClickListener((u1) ->
                findViewById(R.id.filter_view).setVisibility(View.GONE));

        //Sets the onClickListener for the
        findViewById(R.id.visualize_button).setOnClickListener((u1) -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(binding.getRoot().getContext());
            builder.setTitle("Choose Visualization");
            builder.setCancelable(false);
            builder.setSingleChoiceItems(visualizations, currVisualization, (v1, v2) -> currVisualization = v2);
            builder.setPositiveButton("OK", (v1, v2) -> replaceFragment((cache.hasData() ? getCurrVisualization(): new HomeFragment())));
            builder.show();
        });

        //Sets up the different listeners for the keyword and the data source
        ((AutoCompleteTextView) findViewById(R.id.data_search_text)).addTextChangedListener(new onTextEdit(false));
        ((TextInputEditText) findViewById(R.id.keyword_text)).addTextChangedListener(new onTextEdit(true));

        //Sets the onClickListener for the Share button
        findViewById(R.id.share_button).setOnClickListener((u1) -> {
            if (currVisualizationFrag instanceof Shareable) {
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                Shareable s = (Shareable)currVisualizationFrag;

                // This will be a string for ListView, PNG image for others.
                sharingIntent.setType(s.getSharingType());

                // Body of the content; maybe different types later
                String shareSubject = currentKeyword + " " + visualizations[currVisualization];
                if (sharingIntent.getType().equals("text/plain"))
                    sharingIntent.putExtra(Intent.EXTRA_TEXT, (String) s.getSharingContent());
                else {
                    Uri uri = FileProvider.getUriForFile(this, "com.example.fileprovider", ((File) s.getSharingContent()));
                    sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
                }
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, shareSubject);
                startActivity(Intent.createChooser(sharingIntent, "Share using"));
            }
        });
        replaceFragment(new HomeFragment());
    }

    /**
     * Toggles the visibility of the search tab
     *
     * @return the value of the Gone or Visible tag
     */
    private int toggleVisibility() {
        if (findViewById(R.id.filter_view).getVisibility() == View.GONE) {
            return View.VISIBLE;
        } else {
            return View.GONE;
        }
    }

    /**
     * This replaces the current fragment displayed on the main activity
     *
     * @param fragment the fragment to be replaced to
     */
    private void replaceFragment(Fragment fragment) {
        try {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout, fragment);
            fragmentTransaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * The Body of code that runs when the user presses the search button
     */
    private void executeSearch() {
        //Building the Search object for the search
        APISearch search = new APISearch();
        search.setSource(currentDataSource);
        search.setQuery(currentKeyword);

        //Populates the Cache
        new DataManager().buildData(search);

        //Lets the user know if the search was successful
        if (!cache.hasData()) {
            Toast.makeText(this.getBaseContext(), "Search failed", Toast.LENGTH_LONG).show();
            replaceFragment(new HomeFragment());
        } else {
            ((TextInputEditText) findViewById(R.id.keyword_text)).setText("");
            Toast.makeText(this.getBaseContext(), "Search Succeeded", Toast.LENGTH_SHORT).show();
            replaceFragment(getCurrVisualization());
        }

        //Hides the Search View and updates the current fragment
        this.findViewById(R.id.filter_view).setVisibility(View.GONE);

        //If we're in the word map view, we need to clear the old one and put the button back where it belongs.
        if (this.findViewById(R.id.generate_button) != null)
            this.findViewById(R.id.generate_button).setVisibility(View.VISIBLE);
        if (this.findViewById(R.id.word_map_img) != null)
            ((ImageView) this.findViewById(R.id.word_map_img)).setImageBitmap(null);
    }

    /**
     * Gets the current Fragment
     *
     * @return The Fragment of the current visualization
     */
    private Fragment getCurrVisualization() {
        /*
         * The current display.
         */
        Fragment dest;
        switch (currVisualization) {
            case 0:
                dest = new ListFragment();
                break;
            case 1:
                dest = new GraphFragment();
                break;
            default:
                dest = new WordMapFragment();
                break;
        }
        currVisualizationFrag = dest;
        return dest;
    }

    /**
     * This class is responsible for the real time accessibility of different buttons
     */
    private class onTextEdit implements TextWatcher {
        /**
         * Determines if the word is an acceptable keyword
         */
        private final boolean isKeyword;

        /**
         * Constructor.
         * @param isKeyword Determines if the user-entered keyword is acceptable or not.
         */
        public onTextEdit(boolean isKeyword) {
            this.isKeyword = isKeyword;
        }


        /**
         * Action before the keyword is changed. Not used.
         * @param charSequence The user-entered keyword
         * @param i UNIMPORTANT VARIABLE: USED ONLY BETWEEN ANOTHER OBJECT NOT IN OUR CONTROL
         * @param i1 UNIMPORTANT VARIABLE: USED ONLY BETWEEN ANOTHER OBJECT NOT IN OUR CONTROL
         * @param i2 UNIMPORTANT VARIABLE: USED ONLY BETWEEN ANOTHER OBJECT NOT IN OUR CONTROL
         */
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        /**
         * Changes the current keyword to the keyword that the user has entered.
         * @param charSequence The keyword that the user entered.
         * @param i UNIMPORTANT VARIABLE: USED ONLY BETWEEN ANOTHER OBJECT NOT IN OUR CONTROL
         * @param i1 UNIMPORTANT VARIABLE: USED ONLY BETWEEN ANOTHER OBJECT NOT IN OUR CONTROL
         * @param i2 UNIMPORTANT VARIABLE: USED ONLY BETWEEN ANOTHER OBJECT NOT IN OUR CONTROL
         */
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (isKeyword) {
                currentKeyword = charSequence.toString();
            } else {
                currentDataSource = stringToEnum.get(charSequence.toString());
            }
        }

        /**
         * Takes the current keyword and sets the permissions for the search button
         * @param editable Mutable object to attach information to. NOT USED
         */
        @Override
        public void afterTextChanged(Editable editable) {
            if (currentDataSource != null && currentDataSource.hasKeyword()) {
                ((TextInputLayout) findViewById(R.id.search_bar)).setEnabled(true);
                ((Button) findViewById(R.id.button_search)).setEnabled(!currentDataSource.requiresKeyword() || !currentKeyword.equals(""));
            } else {
                ((TextInputLayout) findViewById(R.id.search_bar)).setEnabled(false);
                ((Button) findViewById(R.id.button_search)).setEnabled(true);
            }
        }
    }

}