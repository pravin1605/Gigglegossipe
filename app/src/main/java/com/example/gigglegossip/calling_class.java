package com.example.gigglegossip;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.zegocloud.uikit.prebuilt.call.ZegoUIKitPrebuiltCallService;
import com.zegocloud.uikit.prebuilt.call.config.ZegoNotificationConfig;
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationConfig;
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationService;

public class calling_class extends AppCompatActivity {

    private EditText editText;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // Set the content view to the appropriate layout
        setContentView(R.layout.activity_calling_class);  // Make sure this is the correct layout file

        // Initialize the EditText and Button
        editText = findViewById(R.id.TextID);
        btn = findViewById(R.id.callLogin);

        // Set the OnClickListener for the button
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userId = editText.getText().toString().trim();

                if (userId.isEmpty()) {
                    Toast.makeText(calling_class.this, "Please enter a user ID", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Call the method to start the service
                startMyService(userId);

                // Start the profile activity
                Intent intent = new Intent(getApplicationContext(), profile.class);
                intent.putExtra("caller", userId);
                startActivity(intent);
            }
        });
    }

    // Method to start the ZegoUIKitPrebuiltCallService
    public void startMyService(String userId) {
        Application application = getApplication(); // Android's application context
        long appID = 1407614335;   // yourAppID
        String appSign = "e3e35a27cd0b6469f91e233e610a7a2ede21958d48b252e56630476ef0bb8679";  // yourAppSign
        String userName = userId;  // Use userId as userName

        ZegoUIKitPrebuiltCallInvitationConfig callInvitationConfig = new ZegoUIKitPrebuiltCallInvitationConfig();
        ZegoNotificationConfig notificationConfig = new ZegoNotificationConfig();
        notificationConfig.sound = "zego_uikit_sound_call";
        notificationConfig.channelID = "CallInvitation";
        notificationConfig.channelName = "CallInvitation";

        callInvitationConfig.notificationConfig = notificationConfig;

        // Initialize the Zego UIKit Prebuilt Call Service
        ZegoUIKitPrebuiltCallService.init(application, appID, appSign, userId, userName, callInvitationConfig);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Uninitialize the Zego UIKit Prebuilt Call Service
        ZegoUIKitPrebuiltCallInvitationService.unInit();
    }
}
