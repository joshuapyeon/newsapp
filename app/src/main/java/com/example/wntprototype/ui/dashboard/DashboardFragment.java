package com.example.wntprototype.ui.dashboard;

        import android.graphics.Color;
        import android.os.Bundle;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.ImageView;
        import android.widget.ListView;
        import android.widget.Spinner;
        import android.widget.TextView;
        import android.widget.Toast;

        import androidx.annotation.NonNull;
        import androidx.fragment.app.Fragment;
        import androidx.lifecycle.ViewModelProvider;

        import com.example.wntprototype.MainActivity;
        import com.example.wntprototype.R;
        import com.example.wntprototype.databinding.FragmentDashboardBinding;
        import com.google.android.material.snackbar.Snackbar;

        import org.w3c.dom.DOMStringList;

        import java.util.ArrayList;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //ListView Implementation AKA the list displayed
        ListView listView = (ListView) root.findViewById(R.id.DashboardList);
        ArrayList<String> toShow = new ArrayList<String>();

        //This is all temporary
        ArrayList<String> temp = new ArrayList<String>();
        temp.add("Advertainment");
        temp.add("Big Data");
        temp.add("Content Is King");
        temp.add("Customer Journey");
        temp.add("Deep Dive");
        temp.add("Growth Hacking");
        temp.add("Hyperlocal");
        temp.add("Low Hanging Fruit");
        temp.add("Jacking");
        temp.add("Move the Needle");
        temp.add("Retargeting");
        temp.add("Alignment");
        temp.add("Disruptor/Disruptive");
        temp.add("Freemium");
        temp.add("Leverage");
        temp.add("Quick Win");
        temp.add("Quota");
        temp.add("Value Add");
        temp.add("Wheelhouse");
        temp.add("Customer Acquisition");
        temp.add("Customer-Centric/Customer Centricity");
        temp.add("Customer Lifecycle");
        temp.add("Customer Retention");
        temp.add("Personalization");
        temp.add("Touchpoint");
        temp.add("Voice of the Customer");
        temp.add("Clickbait");
        temp.add("Earned Media");
        temp.add("Live Streaming");
        temp.add("Micro-Influencer");
        temp.add("User-Generated Content (UGC)");
        temp.add("FOMO");
        temp.add("Lit");
        for(int i = 0; i < temp.size(); i++){
            toShow.add("#" + (i+1) + ":  " + temp.get(i).toString());
        }

        //this is what actually shows the information and gives the given interactions
        ArrayAdapter arrayListAdapter = new ArrayAdapter(this.getContext(), android.R.layout.simple_list_item_1, toShow);
        listView.setAdapter(arrayListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Snackbar sb = Snackbar.make(root, "Rank #" + (i+1) + ": " + temp.get(i).toString(), Snackbar.LENGTH_SHORT);
                sb.setAction("More Info", new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(root.getContext(), "Here is More Info :)", Toast.LENGTH_SHORT).show();
                    }
                });
                sb.show();
            }
        });
        //List Done

        //the stuff for the Spinner AKA dropdown Menu
        Spinner spinner = root.findViewById(R.id.VisualizationOptions);
        spinner.setBackgroundColor(Color.LTGRAY);
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("View Type: List");
        arrayList.add("View Type: WordMap");
        arrayList.add("View Type: Graph");
        ArrayAdapter<String> arraySpinnerAdapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_spinner_item, arrayList);
        arraySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arraySpinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String tutorialsName = parent.getItemAtPosition(position).toString();
                Toast.makeText(parent.getContext(), "Selected: " + tutorialsName,          Toast.LENGTH_LONG).show();
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
            }
        });
        //Spinner Done

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}