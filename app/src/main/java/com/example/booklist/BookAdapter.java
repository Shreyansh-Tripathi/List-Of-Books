package com.example.booklist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> implements Filterable
{
    ArrayList<Book> list;
    ItemClick activity;
    ArrayList<Book> editList;

    public interface ItemClick
    {
        void onClick(int index);
    }

    public BookAdapter(Context context, ArrayList<Book> list) {
        this.list = list;
        editList=new ArrayList<>(list);
        activity=(ItemClick)context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView logo;
        TextView bname,bauthor;
        CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            logo=itemView.findViewById(R.id.bookimage);
            bname=itemView.findViewById(R.id.bookname);
            bauthor=itemView.findViewById(R.id.authorname);
            checkBox=itemView.findViewById(R.id.checkBox);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                         activity.onClick(list.indexOf((Book)v.getTag()));
                }
            });
        }
    }

    @NonNull
    @Override
    public BookAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.book_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookAdapter.ViewHolder holder, int position) {

        holder.itemView.setTag(list.get(position));
        holder.bname.setText(list.get(position).getBookname());
        holder.bauthor.setText(list.get(position).getAuthor());
        if(list.get(position).getBookgenre().equals("love"))
            holder.logo.setImageResource(R.drawable.love);

        else if(list.get(position).getBookgenre().equals("horror"))
            holder.logo.setImageResource(R.drawable.ghost);

        else
            holder.logo.setImageResource(R.drawable.scifi);

        holder.checkBox.setChecked(false);
        }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter= new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Book> filteredList= new ArrayList<>();

            if (constraint==null || constraint.length()==0)
                filteredList.addAll(editList);

            else
            {
                String filtering= constraint.toString().toLowerCase().trim();

                for (Book book : editList){
                    if (book.getAuthor().toLowerCase().contains(filtering) || book.getBookname().toLowerCase().contains(filtering))
                    {
                        filteredList.add(book);
                    }
                }
            }
            FilterResults results= new FilterResults();
            results.values=filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
               list.clear();
               list.addAll((Collection<? extends Book>) results.values);
               notifyDataSetChanged();
        }
    };
}
