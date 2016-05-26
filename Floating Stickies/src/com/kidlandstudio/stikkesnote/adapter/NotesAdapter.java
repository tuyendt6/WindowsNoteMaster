package com.kidlandstudio.stikkesnote.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kidlandstudio.stikkesnote.R;
import com.kidlandstudio.stikkesnote.object.Note;

import java.text.DateFormat;
import java.util.List;


public class NotesAdapter extends BaseAdapter {

    public static class NoteViewWrapper {

        private final Note note;
        private boolean isSelected;

        public NoteViewWrapper(Note note) {
            this.note = note;
        }

        public Note getNote() {
            return note;
        }

        public void setSelected(boolean isSelected) {
            this.isSelected = isSelected;
        }
    }

    private static final DateFormat DATETIME_FORMAT = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);

    private final List<NoteViewWrapper> data;

    public NotesAdapter(List<NoteViewWrapper> data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public NoteViewWrapper getItem(int position) {
        return data.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_row, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else holder = (ViewHolder) convertView.getTag();
        NoteViewWrapper noteViewWrapper = data.get(position);
        holder.noteIdText.setText(String.valueOf(noteViewWrapper.note.getId()));
        holder.noteTitleText.setText(noteViewWrapper.note.getTitle());
        holder.noteContentText.setText(noteViewWrapper.note.getContent().length() >= 80 ? noteViewWrapper.note.getContent().substring(0, 80).concat("...") : noteViewWrapper.note.getContent());
        holder.noteDateText.setText(DATETIME_FORMAT.format(noteViewWrapper.note.getUpdatedAt()));
        if (noteViewWrapper.isSelected)
            holder.parent.setBackgroundColor(parent.getContext().getResources().getColor(R.color.selected_note));
        else
            holder.parent.setBackgroundColor(parent.getContext().getResources().getColor(android.R.color.transparent));
        return convertView;
    }

    private static class ViewHolder {

        private TextView noteIdText;
        private TextView noteTitleText;
        private TextView noteContentText;
        private TextView noteDateText;

        private View parent;

        private ViewHolder(View parent) {
            this.parent = parent;
            noteIdText = (TextView) parent.findViewById(R.id.note_id);
            noteTitleText = (TextView) parent.findViewById(R.id.note_title);
            noteContentText = (TextView) parent.findViewById(R.id.note_content);
            noteDateText = (TextView) parent.findViewById(R.id.note_date);
        }
    }
}