package tfws.mobileapps.valoranthubupload;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class UploadActivity extends AppCompatActivity {
    private Button signout,upload_video;
    private FirebaseAuth firebaseAuth;
    private Spinner team,type,site;
    private EditText collection_name,video_title,video_url,image_url,credit;
    private String teamspinner = "";
    private String sitespinner = "";
    private String typespinner = "";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private long backPressedTime;
    private Toast backToast;
    public static final String SAVECRED = "SAVECRED";
    public static final String EmailShared = "Email";
    public static final String passShared = "Password";
    SharedPreferences sharedPreferences;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        team = (Spinner)findViewById(R.id.teamselect);
        type = (Spinner)findViewById(R.id.typeselect);
        site = (Spinner)findViewById(R.id.siteselect);
        signout = (Button)findViewById(R.id.signout);
        upload_video = (Button)findViewById(R.id.upload_video);
        collection_name = (EditText)findViewById(R.id.collection_name);
        video_title = (EditText)findViewById(R.id.video_title);
        video_url = (EditText)findViewById(R.id.video_url);
        image_url = (EditText)findViewById(R.id.image_link);
        credit = (EditText)findViewById(R.id.credit);


        ArrayAdapter<CharSequence> siteadapter = ArrayAdapter.createFromResource(UploadActivity.this,R.array.site,android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> teamadapter = ArrayAdapter.createFromResource(UploadActivity.this,R.array.team,android.R.layout.simple_spinner_item);
        final ArrayAdapter<CharSequence> typeadapter = ArrayAdapter.createFromResource(UploadActivity.this,R.array.type,android.R.layout.simple_spinner_item);
        siteadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        teamadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type.setAdapter(typeadapter);
        site.setAdapter(siteadapter);
        team.setAdapter(teamadapter);
        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i>0){
                    typespinner = adapterView.getItemAtPosition(i).toString();
                }
                else{
                    typespinner = "";
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        site.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i>0){
                    sitespinner = adapterView.getItemAtPosition(i).toString();

                }
                else{
                    sitespinner = "";
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        team.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i>0){
                    teamspinner = adapterView.getItemAtPosition(i).toString();
                }
                else{
                    teamspinner = "";
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        firebaseAuth = FirebaseAuth.getInstance();
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                SharedPreferences preferences =getSharedPreferences(SAVECRED, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });

        upload_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(collection_name.getText().toString().trim().equals("") || video_title.getText().toString().trim().equals("") ||
                        video_url.getText().toString().trim().equals("") || image_url.getText().toString().trim().equals("")){
                    Toast.makeText(UploadActivity.this, "All fields are compulsary", Toast.LENGTH_SHORT).show();
                }
                else{
                    final String coll_name = collection_name.getText().toString().toUpperCase();
                    final String vid_title = video_title.getText().toString();
                    String vid_url = video_url.getText().toString();
                    String img_url = image_url.getText().toString();
                    String cred = credit.getText().toString();
                    final Video video = new Video(vid_title,vid_url,img_url,cred,teamspinner,typespinner,sitespinner);
                    Log.e("Team",teamspinner);
                    Log.e("Type",typespinner);
                    Log.e("Site",sitespinner);
                    Random random = new Random();
                    int min = 1;
                    int max = 1000;
                    int range = max - min + 1;
                    int ranNum = random.nextInt(range) + min;
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yy");
                    Date date = new Date();
                    String currentDate = simpleDateFormat.format(date);
                   // Log.e("current",currentDate);
                    final Map<String, Object> data = new HashMap<>();
                    data.put(String.valueOf(ranNum),currentDate +" - " +vid_title);

                            db.collection(coll_name).add(video).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(UploadActivity.this, "Video uploaded successfully", Toast.LENGTH_LONG).show();
                                    video_title.setText(null);
                                    video_url.setText(null);
                                    collection_name.setText(null);
                                    credit.setText(null);
                                    image_url.setText(null);
                                    team.setSelection(0);
                                    site.setSelection(0);
                                    type.setSelection(0);

                                    db.collection("Recently").document("Recently").set(data, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(UploadActivity.this, "Failed to add video", Toast.LENGTH_SHORT).show();

                                }
                            });
                }
            }
        });

    }

    @Override
    public void onBackPressed() {

        if(backPressedTime + 2000 > System.currentTimeMillis()){
            backToast.cancel();
            super.onBackPressed();
            finishAffinity();
            finish();
            return;

        }else{
            backToast = Toast.makeText(UploadActivity.this, "Press back again to exit", Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();


    }
}
