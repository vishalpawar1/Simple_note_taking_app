package com.example.notetakingapp;

import java.util.List;

import com.example.notetakingapp.data.NoteItem;
import com.example.notetakingapp.data.NotesDataSource;

import android.nfc.NfcAdapter.CreateNdefMessageCallback;
import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends ListActivity {

	private NotesDataSource datasource;
	List<NoteItem> notesList;
	private static final int MENU_DELETE_ID=1002;
	private int currentNodeId;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        registerForContextMenu(getListView());
        datasource = new NotesDataSource(MainActivity.this);
        
        refreshDisplay();
        
        
     
    }


    private void refreshDisplay() {
		notesList= datasource.findAll();
		
    	ArrayAdapter<NoteItem> adapter = new ArrayAdapter<NoteItem>(MainActivity.this, R.layout.list_item_layout,notesList);
    	setListAdapter(adapter);
		
	}


	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	
    	if(item.getItemId()==R.id.action_create_new){
    		createNote();
    		
    	}
    	
    	
    	return super.onOptionsItemSelected(item);
    }


	private void createNote() {
		NoteItem note= NoteItem.getNew();
		Intent i=new Intent(MainActivity.this,EditNoteActivity.class);
		i.putExtra("key", note.getKey());
		i.putExtra("text", note.getText());
		startActivityForResult(i, 1001);
		

	}
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		NoteItem note= notesList.get(position);
		Intent i=new Intent(MainActivity.this,EditNoteActivity.class);
		i.putExtra("key", note.getKey());
		i.putExtra("text", note.getText());
		startActivityForResult(i, 1001);
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode==1001 && resultCode==RESULT_OK){
			NoteItem note=NoteItem.getNew();
			note.setKey(data.getStringExtra("key"));
			note.setText(data.getStringExtra("text"));
			datasource.update(note);
			refreshDisplay();
			
			
			
		}
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		AdapterContextMenuInfo info= (AdapterContextMenuInfo) menuInfo;
		currentNodeId = (int) info.id;
		
		//first 0stand for group number, value1002 is store in MENU_DELETE_ID, 0
		menu.add(0, MENU_DELETE_ID, 0, "Delete");
	
	}
	@Override
	public boolean onContextItemSelected(MenuItem item) {

		if(item.getItemId()==MENU_DELETE_ID){
			NoteItem note=notesList.get(currentNodeId);
			datasource.remove(note);
			refreshDisplay();
			
		}
		return super.onContextItemSelected(item);
	}
	
}