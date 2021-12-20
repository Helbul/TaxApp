package com.harman.taxapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.harman.taxapp.datausers.User;

public class MainActivity extends AppCompatActivity {

    Button buttonSignIn, buttonRegister;
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference users;
    ConstraintLayout helloActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonSignIn = findViewById(R.id.buttonSignIn);
        buttonRegister = findViewById(R.id.buttonRegister);

        helloActivity = findViewById(R.id.hello_activity);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        users = database.getReference("Users");

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRegistarCard();
            }
        });

        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSignInCard();
            }
        });


    }

    private void showSignInCard() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Войти");
        dialog.setMessage("Введите данные для входа");

        LayoutInflater inflater = LayoutInflater.from(this);
        View signInCard = inflater.inflate(R.layout.sign_in_card, null);
        dialog.setView(signInCard);

        EditText email = signInCard.findViewById(R.id.editTextEmail);
        EditText password = signInCard.findViewById(R.id.editTextPassword);

        dialog.setNegativeButton("Отменить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
            }
        });

        dialog.setPositiveButton("Войти", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {

                if (TextUtils.isEmpty(email.getText().toString())) {
                    Snackbar.make(helloActivity, "Введите ваш email", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (password.getText().toString().length() < 8 ) {
                    Snackbar.make(helloActivity, "Неверные параметры входа! Повторите попытку.", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                //авторизация пользователя
                auth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            startActivity(new Intent(MainActivity.this, StatementActivity.class));
                            finish();
                            //Snackbar.make(helloActivity, auth.getCurrentUser().getUid(), Snackbar.LENGTH_LONG).show();
                            //TextView textView = findViewById(R.id.text_under_bottom);
                            //textView.setText(auth.getCurrentUser().getUid());
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Snackbar.make(helloActivity, "Ошибка авторизации. " + e.getMessage(), Snackbar.LENGTH_SHORT).show();
                        }
                });
            }
        });

        dialog.show();
    }

    private void showRegistarCard() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Регистрация");
        dialog.setMessage("Введите свои данные:");

        LayoutInflater inflater = LayoutInflater.from(this);
        View registerCard = inflater.inflate(R.layout.register_card, null);
        dialog.setView(registerCard);

        EditText name = registerCard.findViewById(R.id.editTextName);
        EditText email = registerCard.findViewById(R.id.editTextEmail);
        EditText phone = registerCard.findViewById(R.id.editTextPhone);
        EditText password = registerCard.findViewById(R.id.editTextPassword);

        dialog.setNegativeButton("Отменить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
            }
        });

        dialog.setPositiveButton("Зарегистрироваться", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                if (TextUtils.isEmpty(email.getText().toString())) {
                    Snackbar.make(helloActivity, "Введите ваш email", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(name.getText().toString())) {
                    Snackbar.make(helloActivity, "Введите ваше имя", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(phone.getText().toString())) {
                    Snackbar.make(helloActivity, "Введите ваш телефон", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (password.getText().toString().length() < 8 ) {
                    Snackbar.make(helloActivity, "Введите пароль, который больше 8 символов", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                //регистрация пользователя
                auth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                User user = new User();
                                user.setName(name.getText().toString());
                                user.setEmail(email.getText().toString());
                                user.setPhone(phone.getText().toString());
                                user.setPassword(password.getText().toString());

                                users.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(user)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Snackbar.make(helloActivity, "Пользователь добавлен!", Snackbar.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        });
            }
        });

        dialog.show();
    }

    /*private void showNewAccountCard (){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Создание счета");
        dialog.setMessage("Введите идентификационный номер вашего счета:");

        LayoutInflater inflater = LayoutInflater.from(this);
        View registerCard = inflater.inflate(R.layout.register_card, null);
        dialog.setView(registerCard);

        EditText accountName = registerCard.findViewById(R.id.editAccountName);

        dialog.setPositiveButton("Создать", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {

                if (TextUtils.isEmpty(accountName.getText().toString()) && !accountName.getText().toString().contains(" ")) {
                    Snackbar.make(helloActivity, "Введите имя счета (без пробелов)", Snackbar.LENGTH_SHORT).show();
                    //return;
                } else {

                //auth.


            }
        });

        dialog.show();


    }*/
}