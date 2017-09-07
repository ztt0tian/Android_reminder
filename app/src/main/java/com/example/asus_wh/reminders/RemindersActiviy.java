package com.example.asus_wh.reminders;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class RemindersActiviy extends ActionBarActivity {
    private ListView mListView;
    private RemindersDbAdapter remindersDbAdapter;
    private ReminderSimpleCursorAdapter reminderSimpleCursorAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminders_activiy);
        mListView = (ListView) findViewById(R.id.reminders_list_view);
        mListView.setDivider(null);
        remindersDbAdapter=new RemindersDbAdapter(this);
        remindersDbAdapter.open();
        Cursor cursor=remindersDbAdapter.ftchAllReminders();
        String[] form=new String[]{
                RemindersDbAdapter.COL_CONTENT
        };
        int[] to=new int[]{
                R.id.row_text
        };
        reminderSimpleCursorAdapter=new ReminderSimpleCursorAdapter(
                RemindersActiviy.this,R.layout.reminders_row,cursor,form,to,0
        );
        mListView.setAdapter(reminderSimpleCursorAdapter);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this, R.layout.reminders_row, R.id.row_text, new String[]{
                "first record", "second record", "third record"
        }
        );
        mListView.setAdapter(arrayAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_reminders, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_new:
                Log.d(getLocalClassName(),"create new Reminder");
                return true;
            case R.id.action_exit:
                finish();
                return  true;
            default:
                return false;
        }
    }
}
