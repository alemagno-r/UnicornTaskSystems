package com.example.unicorntasksystem;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText editUser;
    private EditText editPassword;
    private TextView txtStatus;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        editUser = findViewById(R.id.editUser);
        editPassword = findViewById(R.id.editPassword);
        txtStatus = findViewById(R.id.txtStatus);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> authenticate());
    }

    private void authenticate() {

        String usuario = editUser.getText().toString().trim();
        String password = editPassword.getText().toString().trim();

        if (usuario.isEmpty() || password.isEmpty()) {

            txtStatus.setText("● SYSTEM STATUS: DATA REQUIRED");
            txtStatus.setTextColor(0xFF37B9FF);

            Toast.makeText(
                    this,
                    "Ingresa usuario y contraseña",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        if (usuario.equals("pilot") && password.equals("unicorn")) {

            txtStatus.setText("● SYSTEM STATUS: ACCESS GRANTED");
            txtStatus.setTextColor(0xFF37B9FF);

            Toast.makeText(
                    this,
                    "ACCESS GRANTED",
                    Toast.LENGTH_SHORT
            ).show();

            Intent intent = new Intent(
                    LoginActivity.this,
                    DashboardActivity.class
            );

            intent.putExtra("username", usuario);

            startActivity(intent);

            finish();

            Toast.makeText(
                    this,
                    "ACCESS GRANTED",
                    Toast.LENGTH_SHORT
            ).show();

        } else {

            txtStatus.setText("● SYSTEM STATUS: ACCESS DENIED");
            txtStatus.setTextColor(0xFFFF365C);

            Toast.makeText(
                    this,
                    "Credenciales incorrectas",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
}