package noahzu.github.io.axenotes.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import noahzu.github.io.axenotes.R;
import noahzu.github.io.axenotes.adapter.AxeAdapter;
import noahzu.github.io.axenotes.db.AxeDbHelper;
import noahzu.github.io.axenotes.entity.AxeNote;
import noahzu.github.io.axenotes.widget.RecycleViewDivider;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener,AxeAdapter.OnAxeItemLongClickListener,AxeAdapter.OnAxeItemClickListener {

    public static final String TYPE = "type";
    public static final int UPDATE_NOTE = 1;
    public static final int ADD_NOTE = 2;
    public static final String CURRENT_NOTE = "current_note";

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private Toolbar toolbar;

    private Menu menu;
    private boolean isRefreshing = false;

    private AxeAdapter adapter;
    private List<AxeNote> axeNoteList;

    public static final int REQUEST_CODE = 0;

    public boolean isSelecting = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
        initView();
    }

    private void initData() {
        axeNoteList = new ArrayList<AxeNote>();
        axeNoteList.addAll(AxeDbHelper.getInstance(this).getAllAxeNote());
        adapter = new AxeAdapter(this, axeNoteList);
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);//获取Toolbar
        setSupportActionBar(toolbar);//将Toolbar的对象设置进去替换actionBar

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swip_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setRefreshing(isRefreshing);
        //设置样式
        swipeRefreshLayout.setColorSchemeResources(android.R.color.white,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        recyclerView = (RecyclerView) findViewById(R.id.list_notes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.HORIZONTAL));
        recyclerView.setAdapter(adapter);
        adapter.setOnAxeItemLongClickListener(this);
        adapter.setOnAxeItemClickListener(this);
    }

    @Override
    public void onRefresh() {
        if (!isRefreshing) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(false);
                    // TODO: 2016/3/7 刷新数据
                    if (axeNoteList != null) {
                        axeNoteList.clear();
                        axeNoteList.addAll(AxeDbHelper.getInstance(MainActivity.this).getAllAxeNote());
                        adapter.notifyDataSetChanged();
                        Toast.makeText(MainActivity.this,"数据已经刷新",Toast.LENGTH_SHORT).show();
                    }
                    isRefreshing = false;
                }
            }, 1500);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                // TODO: 2016/3/7 添加便签 
                Intent intent = new Intent(MainActivity.this,EditActivity.class);
                startActivityForResult(intent,REQUEST_CODE);
                break;
            case R.id.action_edit:
                if(isSelecting){
                    final List<AxeNote> selectedNote = filterSelected();
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("警告").setMessage("您确认要删除这"+selectedNote.size()+"项吗？")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                   AxeDbHelper.getInstance(MainActivity.this).deleteAxeNotes(selectedNote);
                                    axeNoteList.removeAll(selectedNote);
                                    adapter.notifyDataSetChanged();
                                    menu.findItem(R.id.action_edit).setIcon(R.mipmap.note_edit);
                                    isSelecting = false;
                                }
                            })
                            .setNegativeButton("取消",null).create().show();
                }else{
                    Toast.makeText(this,"长按日记可以删除",Toast.LENGTH_SHORT).show();
                }
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 编辑完了note并返回了note
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            if(requestCode == REQUEST_CODE){
                if (resultCode == EditActivity.UPDATE_OK){
                    AxeNote axeNote = (AxeNote) data.getSerializableExtra(EditActivity.UPDATE_NOTE);
                    update(axeNote);
                }else  if(resultCode == EditActivity.ADD_OK){
                    AxeNote axeNote = (AxeNote) data.getSerializableExtra(EditActivity.ADD_NOTE);
                    axeNoteList.add(axeNote);
                    AxeDbHelper.getInstance(this).addAxeNote(axeNote);
                    adapter.notifyDataSetChanged();
                }
            }
    }

    private void update(AxeNote axeNote) {
        for (AxeNote axeNote1 : axeNoteList){
            if(axeNote1.onlyId == axeNote.onlyId){
                axeNote1.updateSelf(axeNote);
                adapter.notifyDataSetChanged();
                AxeDbHelper.getInstance(this).updateAxeNote(axeNote);
                break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menus, menu);
        this.menu = menu;
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                axeNoteList.clear();
                axeNoteList.addAll(AxeDbHelper.getInstance(MainActivity.this).query(query));
                adapter.notifyDataSetChanged();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    axeNoteList.clear();
                    axeNoteList.addAll(AxeDbHelper.getInstance(MainActivity.this).getAllAxeNote());
                    adapter.notifyDataSetChanged();
                }
            }
        });
        return true;
    }

    @Override
    public void onLongClick(View view,int position) {
        menu.findItem(R.id.action_edit).setIcon(R.mipmap.icon_delete);
        this.isSelecting = true;
        axeNoteList.get(position).isSelected = true;
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view,int position) {
        if(isSelecting){
            if (axeNoteList.get(position).isSelected){
                axeNoteList.get(position).isSelected = false;
                if(filterSelected().size() == 0){
                    menu.findItem(R.id.action_edit).setIcon(R.mipmap.note_edit);
                    isSelecting = false;
                }
            }else{
               axeNoteList.get(position).isSelected = true;
            }
            adapter.notifyDataSetChanged();
        }
        else{
            Intent intent = new Intent(MainActivity.this,EditActivity.class);
            intent.putExtra(TYPE,UPDATE_NOTE);
            intent.putExtra(CURRENT_NOTE, axeNoteList.get(position));
            startActivityForResult(intent,REQUEST_CODE);
        }
    }

    private List<AxeNote> filterSelected(){
        List<AxeNote> notes = new ArrayList<>();
        for (AxeNote axeNote : axeNoteList){
            if(axeNote.isSelected){
                notes.add(axeNote);
            }
        }
        return notes;
    }
}
