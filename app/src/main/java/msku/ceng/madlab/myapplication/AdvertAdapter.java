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

        // Her bir ilana ait verileri al
        HashMap<String, Object> advert = getItem(position);

        if (advert != null) {
            String ownerUsername = advert.get("Owner").toString();
            String content = advert.get("Content").toString();
            TextView usernameTextView = convertView.findViewById(R.id.usernameTextView);
            TextView contentTextView = convertView.findViewById(R.id.contentTextView);
            EditText newCommentEditText = convertView.findViewById(R.id.newCommentEditText);
            Button sendCommentButton = convertView.findViewById(R.id.sendCommentButton);
            TextView commentsTextView = convertView.findViewById(R.id.commentsTextView);

            // İlanın benzersiz kimliğini alın
            String advertId = advert.get("ID").toString();
            Log.e("AdvertAdapter", advertId);
            usernameTextView.setText(ownerUsername);
            contentTextView.setText(content);

            // İlanın yorumlarını al
            ArrayList<String> comments = (ArrayList<String>) advert.get("Comments");

            // Yorumları göstermek için CommentAdapter kullan
            StringBuilder commentsText = new StringBuilder();
            for (String comment : comments) {
                commentsText.append(comment).append("\n\n");
            }
            commentsTextView.setText(commentsText.toString());

            // Yorum gönderme butonuna tıklanınca
            sendCommentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String newComment = newCommentEditText.getText().toString();
                    if (!newComment.isEmpty()) {
                        // Yeni yorumu ilgili ilanın yorum listesine ekle
                        comments.add(currentUsername + ": " + newComment);

                        // Yorumları güncelle
                        StringBuilder updatedCommentsText = new StringBuilder();
                        for (String comment : comments) {
                            updatedCommentsText.append(comment).append("\n\n");
                        }
                        commentsTextView.setText(updatedCommentsText.toString());

                        newCommentEditText.setText("");  // Yorum giriş alanını temizle

                        // İlanın ID'sini kullanarak Firestore'da güncelleme yap
                        updateAdvertInFirestore(advertId, comments);
                    }
                }
            });
        }

        return convertView;
    }

    private void updateAdvertInFirestore(String advertId, ArrayList<String> updatedComments) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Güncellenecek veriyi oluşturun
        HashMap<String, Object> updatedData = new HashMap<>();
        updatedData.put("Comments", updatedComments);

        Log.e("AdvertAdapter", advertId);

        // Firestore'da ilanı güncelleyin
        db.collection("adverts").document(advertId)
                .update(updatedData)  // update kullanarak belgeyi güncelleyin
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.e("AdvertAdapter", "ilan güncellendi");
                        // İlan başarıyla güncellendi
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("AdvertAdapter", "ilan güncellenemedi: " + e.getMessage());
                        // İlan güncelleme hatası
                    }
                });
    }
}
