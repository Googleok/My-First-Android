package com.example.slideview.viewpager;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;

public class MainFragment extends Fragment {

    int[] images = new int[]{R.drawable.whenaman,R.drawable.maps_maroon5, R.drawable.shapeofyou};
    String[] names = new String[]{"Michael Bolton","Maroon5", "Ed sheeran"};
    String[] descriptions = new String[]{"When a man loves women","Maps", "Shape of you"};

    public static MainFragment newInstance() {

        Bundle args = new Bundle();

        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);

        return fragment;
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fourth_fragment, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final ListView lv = (ListView) view.findViewById(R.id.listView1);
        SearchView sv = (SearchView) view.findViewById(R.id.searchView1);

        final ListAdapter adapter2 = new ListAdapter(getContext(), getItems());
        lv.setAdapter(adapter2);


        // 아이템 클릭 시 실행
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {

                switch (pos){
                    case 0:
                        Intent intent1=new Intent(getContext(),WhenAmanLoveIsWomenActivity.class);
                        startActivity(intent1);
                        break;
                    case 1:
                        Intent intent2=new Intent(getContext(),SubActivity.class);
                        startActivity(intent2);
                        break;
                    case 2:
                        Intent intent3=new Intent(getContext(),ShapeofyouActivity.class);
                        startActivity(intent3);
                        break;
                    case 3:

                        break;
                    case 4:

                        break;
                    default:
                        break;
                }

            }
        });

        // 서치뷰
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter2.getFilter().filter(newText);
                return false;
            }
        });
    }

    // 아이템 가져오기
    private ArrayList<Item> getItems(){
        ArrayList<Item> items  = new ArrayList<>();
        Item t;

        for(int i=0; i < names.length; i++)
        {
            t = new Item(images[i],names[i],descriptions[i]);
            items.add(t);
        }
        return items;
    }

}
