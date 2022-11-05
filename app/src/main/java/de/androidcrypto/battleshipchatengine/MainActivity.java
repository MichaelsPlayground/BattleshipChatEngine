package de.androidcrypto.battleshipchatengine;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button placeAShot;
    com.google.android.material.textfield.TextInputEditText chatInput;
    com.google.android.material.textfield.TextInputEditText chatLog;

    final static String COMMAND_PLACE_SHOT = "shot";
    final static String COMMAND_PLACE_SHOT_RESPONSE = "shotresponse";
    final static String RESULT_PLACE_SHOT = "shotresult";
    final static String RESULT_PLACE_SHOT_RESPONSE = "shotresultresp";
    final static String SHOT_HIT = "hit";
    final static String SHOT_SUNK = "sunk";
    final static String SHOT_NO_HIT = "nohit";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chatInput = findViewById(R.id.etMainChatInput);
        placeAShot = findViewById(R.id.btnMainPlaceAShot);
        chatLog = findViewById(R.id.etMainChatLog);

        placeAShot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int field = Integer.valueOf(chatInput.getText().toString());
                placeShot(field);
                //String data = "shot:" + chatInput.getText().toString();
                //chatEngine(data);
            }
        });
    }

    private void chatEngine (String input) {
        addChatLog("chatEngine new command: " + input);
        boolean result = false;
        String[] parts = input.split(":", 0);
        String command = parts[0];
        String data = "";
        if (parts.length > 1) {
            data = parts[1];
        }
        String data2 = "";
        if (parts.length > 2) {
            data2 = parts[2];
        }
        switch (command) {
            case COMMAND_PLACE_SHOT: {
                addChatLog("found command: " + command + " data: " + data);
                int field = Integer.valueOf(data);
                receiveShot(field);
                break;
            }
            case COMMAND_PLACE_SHOT_RESPONSE: {
                addChatLog("found command: " + command + " data: " + data);
                int field = Integer.valueOf(data);
                receiveShotResponse(field);
                break;
            }
            case RESULT_PLACE_SHOT: {
                addChatLog("found command: " + command + " result: " + data + " field: " + data2);
                int field = Integer.valueOf(data);
                resultShot(field, data2);
                break;
            }

        }

    }

    private void placeShot (int field) {
        /**
         * workflow
         * 1 mark this field as temporary shot
         * 2 send the shot as chat string to other party
         */
        addChatLog("placeShot field " + field);
        // mark this field as temporary shot
        String command = COMMAND_PLACE_SHOT + ":" + String.valueOf(field);
        chatEngine(command);
    }

    private void receiveShot(int field) {
        /**
         * workflow:
         * 1 respond the shot
         * 1 check if field if empty or not
         * 2 mark field as shot
         * 3 response hit or not
         * 4 if hit mark field as hit shot
         * 5 if hit and sunk mark field as hit-sunk
         */
        String fieldString = String.valueOf(field);
        addChatLog("receiveShot field " + fieldString);
        String respondString = COMMAND_PLACE_SHOT_RESPONSE + fieldString;
        chatEngine(respondString);
        // check if shot is a hit or not and sunk or not
        String cmd = RESULT_PLACE_SHOT + ":" + fieldString + ":";
        boolean hit = false;
        // todo this is a dummy check, fields < 10 are marked as hit
        if (field < 10) hit = true;
        if (hit) {
            // check if sunk or not
            if (field > 7) {
                // ship is sunk
                cmd += SHOT_SUNK;
                chatEngine(cmd);
            } else {
                cmd += SHOT_HIT;
                chatEngine(cmd);
            }
        } else {
            cmd +=  SHOT_NO_HIT;
            chatEngine(cmd);
        }
        addChatLog("receiveShot field " + String.valueOf(field) + " result: " + cmd);
    }

    private void receiveShotResponse(int field) {
        /**
         * workflow:
         * 1 mark the field as shot
         * 2 wait for shotResult
         */
        addChatLog("receiveShotResponse field " + field);
    }

    private void resultShot(int field, String resultString) {
        /**
         * workflow
         * 1 mark the field as shot if NO_SHOT
         * 2 mark the field as hit if HIT
         * 3 mark the field as SUNK if SUNK
         */
        addChatLog("resultShot field " + field + " result: " + resultString);
        switch (resultString) {
            case SHOT_NO_HIT: {
                // mark the field simply as shot

                break;
            }
            case SHOT_HIT: {
                // mark the field as hit

                break;
            }
            case SHOT_SUNK: {
                // mark the field as SUNK

                break;
            }
        }
    }



    private void addChatLog (String message) {
        String messageNew = message + "\n" +
                chatLog.getText().toString();
        chatLog.setText(messageNew);
    }
}