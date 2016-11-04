package cs490.breakfastclub;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

import cs490.breakfastclub.Classes.Post;
import cs490.breakfastclub.Classes.User;

public class DisplayMessageActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        Intent intent = getIntent();
        //String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        final ArrayList<Post> messagePosts = new ArrayList<Post>();
        final int textSize = 30;
        final int messageTextColor = getResources().getColor(R.color.Purdue_White);
        final int senderTextColor = getResources().getColor(R.color.Gray);
        final int backGroundColor = getResources().getColor(R.color.Purdue_Black);
        final RelativeLayout.LayoutParams sendSide = new RelativeLayout.LayoutParams
                (RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        final RelativeLayout.LayoutParams receiveSide = new RelativeLayout.LayoutParams
                (RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);

        sendSide.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        receiveSide.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Display Message");

        // TODO: Figure out why this breaks the build...
        ListView messagesListView = (ListView) findViewById(R.id.messagesList);
        final SquadMessageAdapter messageListAdapter = new SquadMessageAdapter(this, R.layout.squad_message_list_item, messagePosts);
        messagesListView.setAdapter(messageListAdapter);

        final User currentUser = ((MyApplication) getApplication()).getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference messagesRef = mDatabase.child("Squads/" + currentUser.getSquad().getSquadID()).child("Messages");

        if (currentUser.isPartOfSquad()) {
            Button send = (Button) findViewById(R.id.btnSend);
            send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText sendText = (EditText) findViewById(R.id.txt_msg);
                    if (!sendText.getText().toString().trim().isEmpty())
                    {
                        Post newPost = new Post();
                        String message = sendText.getText().toString().trim();
                        String senderID = currentUser.getUserId();
                        String senderName = currentUser.getName();
                        Date date = new Date();
                        long time = date.getTime();
                        newPost.setMessage(message);
                        newPost.setSenderID(senderID);
                        newPost.setSenderName(senderName);
                        newPost.setDate(date);
                        messagePosts.add(newPost);
                        sendText.setText("");
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                        messageListAdapter.notifyDataSetChanged();

                        messagesRef.child(Long.toString(time)).child("message").setValue(message);
                        messagesRef.child(Long.toString(time)).child("sender").setValue(senderID);


                    }


                }
            });


            messagesRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        int i = 0;
                        for (DataSnapshot messageSnapShot : dataSnapshot.getChildren())
                        {

                            if (i < messagePosts.size())
                            {
                                i++;
                            }
                            else
                            {
                                final Post newPost = new Post();
                                Log.v("Snapshot", messageSnapShot.toString());
                                String message = (String) messageSnapShot.child("message").getValue();
                                String senderID = (String) messageSnapShot.child("sender").getValue();
                                Date date = new Date(Long.parseLong(messageSnapShot.getKey()));
                                newPost.setMessage(message);
                                newPost.setSenderID(senderID);
                                newPost.setDate(date);
                                /*if (senderID == currentUser.getUserId())
                                {
                                    String senderName = currentUser.getName();
                                    newPost.setSenderName(senderName);
                                    messagePosts.add(newPost);
                                    messageListAdapter.notifyDataSetChanged();
                                }
                                else */{
                                    final DatabaseReference userRef = mDatabase.child("Users/" + senderID);
                                    userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            Log.v("Snapshot", newPost.toString());
                                            String senderName = (String) dataSnapshot.child("name").getValue();
                                            newPost.setSenderName(senderName);
                                            messagePosts.add(newPost);
                                            Log.v("MessagePostsSnapshot", messagePosts.toString());
                                            messageListAdapter.notifyDataSetChanged();
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }

                            }
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
