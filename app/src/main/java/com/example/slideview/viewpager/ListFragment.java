package com.example.slideview.viewpager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.slideview.viewpager.ListAdapter;
import com.example.slideview.viewpager.R;
import com.example.slideview.viewpager.ShapeofyouActivity;
import com.example.slideview.viewpager.SubActivity;

import java.util.ArrayList;

public class ListFragment extends Fragment {

    private String getSong[];
    private String getSinger[];
    private int image[];

    SharedPreferences sharedPreferences;
    boolean flag1;
    boolean flag2;

    public static ListFragment newInstance() {


        Bundle args = new Bundle();
        ListFragment fragment = new ListFragment();
        fragment.setArguments(args);

        return fragment;


    }

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        getSong=new String[5];
        getSinger=new String [5];
        image= new int[5];
        flag1 = true;
        flag2 = false;

        sharedPreferences=getActivity().getSharedPreferences("data",0);

        flag2 = sharedPreferences.getBoolean("flaglist",flag2); //true로 받음

        if(flag1==flag2) // flag2가 true 즉 새로 업데이트 될 데이터가 있을 때
        {

            for(int i=0;i<5;i++)
            {
                getSong[i]=sharedPreferences.getString("song"+i+"","");
                getSinger[i]=sharedPreferences.getString("singer"+i+"","");
                image[i]=sharedPreferences.getInt("image"+i+"",0);

            }
        }




    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.third_fragment, container, false);


        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        final ListView lv = view.findViewById(R.id.listView1);
        SearchView sv =  view.findViewById(R.id.searchView1);

        final ListAdapter adapter2 = new ListAdapter(getContext(), getItems());
        lv.setAdapter(adapter2);

        // 아이템 삭제 기능
        lv.setChoiceMode(lv.CHOICE_MODE_MULTIPLE_MODAL);
        lv.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                final int checkedCount = lv.getCheckedItemCount();
                mode.setTitle(checkedCount + "Selected");
                adapter2.toggleSelection(position);
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.main, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.delete:
                        SparseBooleanArray selected = adapter2.getSelectedIds();

                        for (int i = (selected.size() - 1); i >= 0; i--) {
                            if (selected.valueAt(i)) {
                                Item selecteditem = (Item) adapter2.getItem(selected.keyAt(i));
                                adapter2.remove(selecteditem);
                            }
                        }
                        mode.finish();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                adapter2.removeSelection();
            }
        });


        // 아이템 클릭 시 실행
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {


                switch (pos) {

                    case 0:
                        Intent intent=new Intent(getContext(),SubActivity.class);
                        startActivity(intent);
                        break;

                    case 1:
                        Intent intent1=new Intent(getContext(),ShapeofyouActivity.class);
                        startActivity(intent1);

                        break;

                    case 2:
                        break;

                    default:
                        break;

                } // switch


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
    private ArrayList<Item> getItems()
    {


        Log.v("dd","Arraylist<Item>");
        ArrayList<Item> items  = new ArrayList<>();
        Item t;
        for(int i=0; i < getSong.length; i++)
        {
            t = new Item(image[i],getSong[i],getSinger[i]);
            items.add(t);
        }

        return items;
    }


}