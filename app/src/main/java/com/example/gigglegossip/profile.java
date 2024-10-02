package com.example.gigglegossip;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.zegocloud.uikit.prebuilt.call.invite.widget.ZegoSendCallInvitationButton;
import com.zegocloud.uikit.service.defines.ZegoUIKitUser;

import java.util.Collections;

public class profile extends AppCompatActivity {

    TextInputLayout callerLayout;
    EditText targetuser;
    ZegoSendCallInvitationButton callbtn;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        // Find the TextInputLayout and EditText
        callerLayout = findViewById(R.id.textVview);
        targetuser = findViewById(R.id.editId);
        callbtn = findViewById(R.id.callBtn);

        // Set the hint or the text of the EditText inside TextInputLayout
        EditText callerEditText = callerLayout.getEditText();
        if (callerEditText != null) {
            callerEditText.setText("you are : " + getIntent().getStringExtra("caller"));
        }

        // Add a TextWatcher to the targetuser EditText
        targetuser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                startvideocall(targetuser.getText().toString().trim());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    public void startvideocall(String targetuserid) {
        callbtn.setIsVideoCall(true);
        callbtn.setResourceID("zego_uikit_call"); // Please fill in the resource ID name that has been configured in the ZEGOCLOUD's console here.
        callbtn.setInvitees(Collections.singletonList(new ZegoUIKitUser(targetuserid, targetuserid)));
    }
}
