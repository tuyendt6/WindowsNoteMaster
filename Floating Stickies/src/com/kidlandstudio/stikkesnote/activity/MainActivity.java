package com.kidlandstudio.stikkesnote.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.kidlandstudio.stikkesnote.R;
import com.kidlandstudio.stikkesnote.adapter.NotesAdapter;
import com.kidlandstudio.stikkesnote.customview.FloatingActionButton;
import com.kidlandstudio.stikkesnote.customview.ShowHideOnScroll;
import com.kidlandstudio.stikkesnote.object.Note;
import com.kidlandstudio.stikkesnote.service.FloatingSticky;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import wei.mark.standout.StandOutWindow;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private ListView mListView;
    private NotesAdapter mAdapterNote;
    private List<NotesAdapter.NoteViewWrapper> mDataNotes = new ArrayList<>();
    public static float density, width, height;


    private ArrayList<Integer> selectedPositions;

    private ActionMode.Callback actionModeCallback;
    private ActionMode actionMode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        density = getResources().getDisplayMetrics().density;
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        width = displaymetrics.widthPixels;
        height = displaymetrics.heightPixels;

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setColor(getResources().getColor(R.color.colorPrimary));
        if (fab != null)
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent serviceIntent = new Intent(getBaseContext(), StandOutWindow.class);
                    stopService(serviceIntent);
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            Map<String, ?> keys = prefs.getAll();

                            boolean none = true;
                            for (Map.Entry<String, ?> entry : keys.entrySet()) {
                                try {
                                    if (entry.getKey().contains("_id") && !entry.getValue().equals("")) {
                                        none = false;
                                        Log.d("startup", entry.getKey());
                                        StandOutWindow.show(MainActivity.this, FloatingSticky.class, Integer.parseInt(entry.getKey().replace("_id", "")));
                                        Thread.sleep(1500);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            if (none) {
                                StandOutWindow.show(MainActivity.this, FloatingSticky.class, StandOutWindow.DEFAULT_ID);
                            }
                        }

                    }).start();
                    finish();
                }
            });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        if (drawer != null)
            drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null)
            navigationView.setNavigationItemSelectedListener(this);

        mDataNotes.add(new NotesAdapter.NoteViewWrapper(new Note((long) 1, "hoc 1", "luc 9h", new Date(11111), new Date(11111))));
        mDataNotes.add(new NotesAdapter.NoteViewWrapper(new Note((long) 1, "hoc 2", "luc 9h", new Date(11111), new Date(11111))));
        mDataNotes.add(new NotesAdapter.NoteViewWrapper(new Note((long) 1, "hoc 3", "luc 9h", new Date(11111), new Date(11111))));
        mDataNotes.add(new NotesAdapter.NoteViewWrapper(new Note((long) 1, "hoc 4", "luc 9h", new Date(11111), new Date(11111))));
        mDataNotes.add(new NotesAdapter.NoteViewWrapper(new Note((long) 1, "hoc 5", "luc 9h", new Date(11111), new Date(11111))));
        mDataNotes.add(new NotesAdapter.NoteViewWrapper(new Note((long) 1, "hoc 6", "luc 9h", new Date(11111), new Date(11111))));
        mDataNotes.add(new NotesAdapter.NoteViewWrapper(new Note((long) 1, "hoc 7", "luc 9h", new Date(11111), new Date(11111))));
        mDataNotes.add(new NotesAdapter.NoteViewWrapper(new Note((long) 1, "hoc 8", "luc 9h", new Date(11111), new Date(11111))));
        mDataNotes.add(new NotesAdapter.NoteViewWrapper(new Note((long) 1, "hoc 9", "luc 9h", new Date(11111), new Date(11111))));
        mDataNotes.add(new NotesAdapter.NoteViewWrapper(new Note((long) 1, "hoc 10", "luc 9h", new Date(11111), new Date(11111))));


        mListView = (ListView) findViewById(R.id.listView);
        mListView.setOnTouchListener(new ShowHideOnScroll(fab, getSupportActionBar()));
        mAdapterNote = new NotesAdapter(mDataNotes);
        mListView.setAdapter(mAdapterNote);
        selectedPositions = new ArrayList<>();

        setupActionModeCallback();
        setListOnItemClickListenersWhenNoActionMode();
        updateView();




    }

    /** Actualiza la vista de esta actividad cuando hay notas o no hay notas. */
    private void updateView() {
        if (mDataNotes.isEmpty()) { // Mostrar mensaje
            mListView.setVisibility(View.GONE);
      //      emptyListTextView.setVisibility(View.VISIBLE);
        } else { // Mostrar lista
            mListView.setVisibility(View.VISIBLE);
     //       emptyListTextView.setVisibility(View.GONE);
        }
    }


    private void setListOnItemClickListenersWhenActionMode() {
        mListView.setOnItemLongClickListener(null);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Agregar items a la lista de seleccionados y cambiarles el fondo.
                // Si se deseleccionan todos los items, se acaba el modo contextual
                if (selectedPositions.contains(position)) {
                    selectedPositions.remove((Object)position); // no quiero el índice sino el objeto
                    if (selectedPositions.isEmpty()) actionMode.finish();
                    else {
                        actionMode.setTitle(String.valueOf(selectedPositions.size()));
                        mDataNotes.get(position).setSelected(false);
                        mAdapterNote.notifyDataSetChanged();
                    }
                } else {
                    mDataNotes.get(position).setSelected(true);
                    mAdapterNote.notifyDataSetChanged();
                    selectedPositions.add(position);
                    actionMode.setTitle(String.valueOf(selectedPositions.size()));
                }
            }
        });
    }

    private void setListOnItemClickListenersWhenNoActionMode() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Ver la nota al hacer click
         //       startActivityForResult(ViewNoteActivity.buildIntent(MainActivity.this, notesData.get(position).getNote()), EDIT_NOTE_RESULT_CODE);
            }
        });
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // Iniciar modo contextual para selección de items
                mDataNotes.get(position).setSelected(true);
                mAdapterNote.notifyDataSetChanged();
                selectedPositions.add(position);
                actionMode = startSupportActionMode(actionModeCallback);
                actionMode.setTitle(String.valueOf(selectedPositions.size()));
                return true;
            }
        });
    }



    /** Crea la llamada al modo contextual. */
    private void setupActionModeCallback() {
        actionModeCallback = new ActionMode.Callback() {

            /** {@inheritDoc} */
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                setListOnItemClickListenersWhenActionMode();
                // inflar menu contextual
                mode.getMenuInflater().inflate(R.menu.context_note, menu);
                return true;
            }

            /** {@inheritDoc} */
            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                // Nada
                return false;
            }

            /** {@inheritDoc} */
            @Override
            public boolean onActionItemClicked(final ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    // borrar notas solo si hay notas a borrar; sino se acaba el modo contextual.
                    case R.id.action_delete:
                        if (!selectedPositions.isEmpty()) {
                            new AlertDialog.Builder(MainActivity.this)
                                    .setMessage(getString(R.string.delete_notes_alert, selectedPositions.size()))
                                    .setNegativeButton(android.R.string.no, null)
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                          //  deleteNotes(selectedPositions);
                                            mode.finish();
                                        }
                                    })
                                    .show();
                        } else mode.finish();
                        return true;
                    default:
                        return false;
                }
            }

            /** {@inheritDoc} */
            @Override
            public void onDestroyActionMode(ActionMode mode) {
                // Regresar al modo normal
                setListOnItemClickListenersWhenNoActionMode();
                resetSelectedListItems();
            }
        };
    }

    private void resetSelectedListItems() {
        for (NotesAdapter.NoteViewWrapper noteViewWrapper : mDataNotes) noteViewWrapper.setSelected(false);
        selectedPositions.clear();
        mAdapterNote.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null)
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null)
            drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
