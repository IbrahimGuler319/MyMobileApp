package msku.ceng.madlab.myapplication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

// This class implemented by Baran İşci 200709054
public class AdvertAdapter extends ArrayAdapter<HashMap<String, Object>> {
    private Context mContext;
    private int mResource;
    private String currentUsername;

    public AdvertAdapter(Context context, int resource, ArrayList<HashMap<String, Object>> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
        initCurrentUsername();
    }

    private void initCurrentUsername() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users").document(uid)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                currentUsername = documentSnapshot.getString("Username");
                            } else {
                                // Kullanıcı belirtilen alanda bulunamadı
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("AdvertAdapter", "Hata: " + e.getMessage());
                        }
                    });
        } else {
            Log.e("AdvertAdapter", "Oturum açmış bir kullanıcı yok.");
        }
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
        }


        HashMap<String, Object> advert = getItem(position);

        if (advert != null) {
            String ownerUsername = advert.get("Owner").toString();
            String content = advert.get("Content").toString();
            String time = advert.get("Time").toString();
            String address = advert.get("Address").toString();
            TextView usernameTextView = convertView.findViewById(R.id.usernameTextView);
            TextView contentTextView = convertView.findViewById(R.id.contentTextView);
            TextView timeTextView = convertView.findViewById(R.id.timeTextView);
            TextView addressTextView = convertView.findViewById(R.id.addressTextView);
            EditText newCommentEditText = convertView.findViewById(R.id.newCommentEditText);
            Button sendCommentButton = convertView.findViewById(R.id.sendCommentButton);
            TextView commentsTextView = convertView.findViewById(R.id.commentsTextView);

            String advertId = advert.get("ID").toString();
            Log.e("AdvertAdapter", advertId);
            usernameTextView.setText("Owner Advert: " + ownerUsername);
            contentTextView.setText(" Content: " + content);
            timeTextView.setText("Time: " + time);
            addressTextView.setText("Address" + address);


            ArrayList<String> comments = (ArrayList<String>) advert.get("Comments");


            StringBuilder commentsText = new StringBuilder();
            for (String comment : comments) {
                commentsText.append(comment).append("\n\n");
            }
            commentsTextView.setText(commentsText.toString());


            sendCommentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String newComment = newCommentEditText.getText().toString();
                    if (!newComment.isEmpty()) {

                        comments.add(currentUsername + ": " + newComment);


                        StringBuilder updatedCommentsText = new StringBuilder();
                        for (String comment : comments) {
                            updatedCommentsText.append(comment).append("\n\n");
                        }
                        commentsTextView.setText(updatedCommentsText.toString());

                        newCommentEditText.setText("");


                        updateAdvertInFirestore(advertId, comments);
                    }
                }
            });
        }

        return convertView;
    }

    private void updateAdvertInFirestore(String advertId, ArrayList<String> updatedComments) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();


        HashMap<String, Object> updatedData = new HashMap<>();
        updatedData.put("Comments", updatedComments);

        Log.e("AdvertAdapter", advertId);


        db.collection("adverts").document(advertId)
                .update(updatedData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.e("AdvertAdapter", "ilan güncellendi");

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("AdvertAdapter", "ilan güncellenemedi: " + e.getMessage());
                    }
                });
    }
}
