package com.example.slideview.viewpager;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.security.AccessControlContext;
import java.util.ArrayList;

public class ListAdapter extends BaseAdapter implements Filterable {

    Context c;
    ArrayList<Item> items;
    CustomFilter filter;
    ArrayList<Item> filterList;

    private SparseBooleanArray mSelectedItemsIds;

    public ListAdapter(Context ctx, ArrayList<Item>items){

        mSelectedItemsIds = new SparseBooleanArray();
        this.c = ctx;
        this.items = items;
        this.filterList = items;
    }



    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int pos) {
        return items.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return items.indexOf(getItem(pos));
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(convertView == null){
            convertView=inflater.inflate(R.layout.model, null);
        }
        TextView nameTxt = (TextView) convertView.findViewById(R.id.textView_name);
        TextView discriptionTxt = (TextView) convertView.findViewById(R.id.textView_description);
        ImageView img = (ImageView) convertView.findViewById(R.id.imageView);

        //SET DATA TO THEM
        nameTxt.setText(items.get(pos).getName());
        discriptionTxt.setText(items.get(pos).getDescription());
        img.setImageResource(items.get(pos).getImage());

        return convertView;
    }

    @Override
    public Filter getFilter() {
        if(filter==null){
            filter=new CustomFilter();
        }
        return filter;
    }

    //INNER CLASS
    class CustomFilter extends Filter{

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults results = new FilterResults();

            if(constraint != null && constraint.length()>0 ){
                //CONSTRAINT TO UPPER
                constraint=constraint.toString().toUpperCase();

                ArrayList<Item> filters =new ArrayList<Item>();

                //get specific items
                for(int i=0; i<filterList.size();i++){
                    if(filterList.get(i).getName().toUpperCase().contains(constraint)||filterList.get(i).getDescription().toUpperCase().contains(constraint)){
                        Item t = new Item(filterList.get(i).getImage(),filterList.get(i).getName(),filterList.get(i).getDescription());

                        filters.add(t);
                    }
                }

                results.count =filters.size();
                results.values=filters;
            }else{
                results.count=filterList.size();
                results.values=filterList;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            items = (ArrayList<Item>) results.values;
            notifyDataSetChanged();
        }
    }

    public  void remove(Item object){
        items.remove(object);
        notifyDataSetChanged();
    }

    public ArrayList<Item> getitems(){
        return items;
    }

    public void toggleSelection(int position){
        selectView(position, !mSelectedItemsIds.get(position));
    }

    public void removeSelection(){
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    public void selectView(int position, boolean value){
        if(value){
            mSelectedItemsIds.put(position, value);
        }else{
            mSelectedItemsIds.delete(position);
        }
        notifyDataSetChanged();
    }

    public int getSelectedCount( ){
        return mSelectedItemsIds.size();
    }

    public SparseBooleanArray getSelectedIds(){
        return mSelectedItemsIds;
    }
}