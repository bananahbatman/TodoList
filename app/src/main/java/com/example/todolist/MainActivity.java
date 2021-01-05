package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import org.apache.commons.io.FileUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<String> items;
    Button addBtn;
    EditText entItem;
    RecyclerView curItems;
    ItemsAdapter itemsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadItems();

        addBtn = findViewById(R.id.addBtn);
        entItem = findViewById(R.id.entItem);
        curItems = findViewById(R.id.curItems);
        ItemsAdapter.OnLongClickListener onLongClickListener = new ItemsAdapter.OnLongClickListener() {
            @Override
            public void onItemLongClicked(int position)
            {
                items.remove(position);
                itemsAdapter.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(), "removed Item", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        };
        itemsAdapter = new ItemsAdapter(items, onLongClickListener);

        curItems.setAdapter(itemsAdapter);
        curItems.setLayoutManager(new LinearLayoutManager(this));
        addBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String todoItem = entItem.getText().toString();
                items.add(todoItem);
                itemsAdapter.notifyItemInserted(items.size()-1);
                entItem.setText("");
                Toast.makeText(getApplicationContext(), "added Item", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        });

    }
    private File getDataFile()
    {
        return new File(getFilesDir(), "data.txt");

    }
    private void loadItems()
    {
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e)
        {
            Log.e("MainActivity", "error reading items",e);
            items = new ArrayList<>();
        }
    }
    private void saveItems()
    {
        try{
            FileUtils.writeLines(getDataFile(), items);
            
        }
        catch (IOException e)
        {
            Log.e("MainActivity", "Error writing items");
        }
    }

}