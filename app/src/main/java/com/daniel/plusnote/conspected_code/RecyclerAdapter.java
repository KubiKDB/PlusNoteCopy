package com.daniel.plusnote.conspected_code;//package com.example.plusnote;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import java.util.List;
//
//public class RecyclerAdapter extends
//        RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
//    // Provide a direct reference to each of the views within a data item
//    // Used to cache the views within the item layout for fast access
//    public class ViewHolder extends RecyclerView.ViewHolder {
//        // Your holder should contain a member variable
//        // for any view that will be set as you render a row
//        public TextView nameTextView;
//        public Button messageButton;
//
//        public ViewHolder(View itemView) {
//
//            super(itemView);
//            EditText inputList1 = itemView.findViewById(R.id.inputList1);
//        }
//
//        private List<EditText> mContacts;
//
//        public ViewHolder(List<EditText> contacts) {
//            super((View) contacts);
//            this.mContacts = contacts;
//        }
//    }
//        @Override
//        public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            Context context = parent.getContext();
//            LayoutInflater inflater = LayoutInflater.from(context);
//
//            // Inflate the custom layout
//            View contactView = inflater.inflate(R.layout., parent, false);
//
//            // Return a new holder instance
//            ViewHolder viewHolder = new ViewHolder(contactView);
//            return viewHolder;
//        }
//
//        // Involves populating data into the item through holder
//        @Override
//        public void onBindViewHolder(RecyclerAdapter.ViewHolder holder, int position) {
//            EditText contact = mContacts.get(position);
//        }
//        @Override
//        public int getItemCount() {
//            return mContacts.size();
//        }
//    }
//}
