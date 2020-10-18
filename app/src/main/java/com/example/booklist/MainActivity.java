package com.example.booklist;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class MainActivity extends AppCompatActivity implements BookAdapter.ItemClick {

    ImageView bookimage;
    TextView bookname, authorname;
    private ArrayList<Book> example;
    RecyclerView rec;
    RecyclerView.LayoutManager layoutManager;
    BookAdapter myadapter;
    SwipeRefreshLayout swipeRefreshLayout;
    static boolean isChecked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rec=findViewById(R.id.recbooks);
        bookname=findViewById(R.id.bookname);
        authorname=findViewById(R.id.authorname);
        bookimage=findViewById(R.id.bookimage);

        swipeRefreshLayout=findViewById(R.id.swipeRefreshLayout);

        example= ApplicationClass.list;
        myadapter=new BookAdapter(MainActivity.this, example);
        layoutManager=new LinearLayoutManager(this.getApplicationContext());

        rec.setLayoutManager(layoutManager);
        rec.setAdapter(myadapter);

        ActionBar actionBar=getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("List Of Books");
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        ItemTouchHelper touchHelper=new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(rec);

        swipeRefreshLayout.setColorSchemeColors(Color.GREEN, Color.RED, Color.BLUE);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                example.add(new Book("The Three-Body Problem", "Liu Cixin","sci fi"));
                example.add(new Book("Kindred", "Octavia", "sci fi"));
                example.add(new Book("Jurassic Park", "Michael Crichton", "sci fi"));

                myadapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    Book deletedBook=null;
    List<Book> archieveBooks=new ArrayList<>();

    ItemTouchHelper.SimpleCallback callback= new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN|
            ItemTouchHelper.START| ItemTouchHelper.END ,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT)
    {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

           int fromPosition= viewHolder.getAdapterPosition();
           int toPosition= target.getAdapterPosition();

           Collections.swap(example, fromPosition, toPosition);

           (recyclerView.getAdapter()).notifyItemMoved(fromPosition, toPosition);

            return false;
        }

        @Override
        public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
            super.onSelectedChanged(viewHolder, actionState);
            if (actionState == ItemTouchHelper.ACTION_STATE_DRAG){
                assert viewHolder != null;
                viewHolder.itemView.setBackgroundColor(Color.BLACK);
            }
        }

        @Override
        public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            viewHolder.itemView.setBackgroundColor(ContextCompat.getColor(viewHolder.itemView.getContext(),R.color.bookBg));
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            final int position= viewHolder.getAdapterPosition();

             switch (direction)
             {
                 case ItemTouchHelper.RIGHT:
                     deletedBook=example.get(position);
                     example.remove(position);
                     myadapter.notifyItemRemoved(position);
                     Snackbar.make(rec, deletedBook.getBookname()+" Deleted", Snackbar.LENGTH_LONG)
                         .setAction("Undo", new View.OnClickListener() {
                             @Override
                             public void onClick(View v) {
                                 example.add(position, deletedBook);
                                 myadapter.notifyItemInserted(position);
                                 deletedBook=null;
                             }
                         }).setActionTextColor(Color.RED)
                             .show();
                     break;

                 case ItemTouchHelper.LEFT:
                     archieveBooks.add(example.get(position));
                     example.remove(position);
                     myadapter.notifyItemRemoved(position);
                     Snackbar.make(rec, archieveBooks.get(archieveBooks.size()-1).getBookname()+" Archieved", Snackbar.LENGTH_LONG)
                             .setAction("Undo", new View.OnClickListener() {
                                 @Override
                                 public void onClick(View v) {
                                     example.add(position, archieveBooks.get(archieveBooks.size()-1));
                                     myadapter.notifyItemInserted(position);
                                     archieveBooks.remove(archieveBooks.size()-1);
                                 }
                             }).setActionTextColor(Color.GREEN)
                               .show();
                     break;
             }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(MainActivity.this, android.R.color.holo_red_dark))
                    .addSwipeRightActionIcon(R.drawable.ic_baseline_delete_24)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(MainActivity.this, android.R.color.holo_green_dark))
                    .addSwipeLeftActionIcon(R.drawable.ic_baseline_archive_24)
                    .create()
                    .decorate();
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem menuItem= menu.findItem(R.id.search);
        SearchView searchView= (SearchView) menuItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                myadapter.getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.addbookbtn:
            startActivityForResult(new Intent(MainActivity.this, DetailsActivity.class), 1);
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode==1 && resultCode==RESULT_OK)
        {
            myadapter.notifyDataSetChanged();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(int index) {
             String s= ApplicationClass.list.get(index).getBookname() + "-" + ApplicationClass.list.get(index).getAuthor();
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}
