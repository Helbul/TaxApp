package com.harman.taxapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.SharedPreferences;

import com.harman.taxapp.R;

public class LoginActivity extends AppCompatActivity {
    public static SharedPreferences settings;//избавиться от статик!!!!!!!!!!

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        settings = getSharedPreferences(String.valueOf(R.string.PREF_FILE), MODE_PRIVATE); //избавиться от статика!!!!!!!!!!!!!












        /*

        settings = getSharedPreferences(PREF_FILE, MODE_PRIVATE);

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

        */
    }

//    private void showSignInCard() {
//        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
//        dialog.setTitle("Войти");
//        dialog.setMessage("Введите данные для входа");
//
//        LayoutInflater inflater = LayoutInflater.from(this);
//        View signInCard = inflater.inflate(R.layout.sign_in_card, null);
//        dialog.setView(signInCard);
//
//        EditText email = signInCard.findViewById(R.id.editTextEmail);
//        EditText password = signInCard.findViewById(R.id.editTextPassword);
//
//        dialog.setNegativeButton("Отменить", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int which) {
//                dialogInterface.dismiss();
//            }
//        });
//
//        dialog.setPositiveButton("Войти", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int which) {
//
//                if (TextUtils.isEmpty(email.getText().toString())) {
//                    Snackbar.make(helloActivity, "Введите ваш email", Snackbar.LENGTH_SHORT).show();
//                    return;
//                }
//
//                if (password.getText().toString().length() < 8 ) {
//                    Snackbar.make(helloActivity, "Неверные параметры входа! Повторите попытку.", Snackbar.LENGTH_SHORT).show();
//                    return;
//                }
//
//                //авторизация пользователя
//                String emailString = email.getText().toString();
//                auth.signInWithEmailAndPassword(emailString, password.getText().toString())
//                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
//                        @Override
//                        public void onSuccess(AuthResult authResult) {
//                            SharedPreferences.Editor preferenceEditor = settings.edit();
//                            //preferenceEditor.putString(PREF_NAME, users.child(id).child("name").); name сохраню в другой activity, чтобы не тратить ресурсы
//                            preferenceEditor.putString(PREF_EMAIL, emailString);
//                            String idUser = auth.getCurrentUser().getUid();
//                            preferenceEditor.putString(PREF_BASE_ID, idUser);
//                            preferenceEditor.apply();
//
//                            //startActivity(new Intent(LoginActivity.this, MainActivity.class));
//                            //finish();
//
//
//                            Snackbar.make(helloActivity, auth.getCurrentUser().getUid(), Snackbar.LENGTH_LONG).show();
//                            TextView textView = findViewById(R.id.text_under_bottom);
//                            textView.setText(auth.getCurrentUser().getUid());
//
//                            showSelectAccountCard(idUser);
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Snackbar.make(helloActivity, "Ошибка авторизации. " + e.getMessage(), Snackbar.LENGTH_SHORT).show();
//                        }
//                });
//            }
//        });
//
//        dialog.show();
//    }
//
//    private void showRegistarCard() {
//        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
//        dialog.setTitle("Регистрация");
//        dialog.setMessage("Введите свои данные:");
//
//        LayoutInflater inflater = LayoutInflater.from(this);
//        View registerCard = inflater.inflate(R.layout.register_card, null);
//        dialog.setView(registerCard);
//
//        EditText name = registerCard.findViewById(R.id.editTextName);
//        EditText email = registerCard.findViewById(R.id.editTextEmail);
//        EditText phone = registerCard.findViewById(R.id.editTextPhone);
//        EditText password = registerCard.findViewById(R.id.editTextPassword);
//
//        dialog.setNegativeButton("Отменить", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int which) {
//                dialogInterface.dismiss();
//            }
//        });
//
//        dialog.setPositiveButton("Зарегистрироваться", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int which) {
//                if (TextUtils.isEmpty(email.getText().toString())) {
//                    Snackbar.make(helloActivity, "Введите ваш email", Snackbar.LENGTH_SHORT).show();
//                    return;
//                }
//
//                if (TextUtils.isEmpty(name.getText().toString())) {
//                    Snackbar.make(helloActivity, "Введите ваше имя", Snackbar.LENGTH_SHORT).show();
//                    return;
//                }
//
//                if (TextUtils.isEmpty(phone.getText().toString())) {
//                    Snackbar.make(helloActivity, "Введите ваш телефон", Snackbar.LENGTH_SHORT).show();
//                    return;
//                }
//
//                if (password.getText().toString().length() < 8 ) {
//                    Snackbar.make(helloActivity, "Введите пароль, который больше 8 символов", Snackbar.LENGTH_SHORT).show();
//                    return;
//                }
//
//                //регистрация пользователя
//                auth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
//                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
//                        @Override
//                        public void onSuccess(AuthResult authResult) {
//                            User user = new User();
//                            user.setName(name.getText().toString());
//                            user.setEmail(email.getText().toString());
//                            user.setPhone(phone.getText().toString());
//                            user.setPassword(password.getText().toString());
//                            user.setAllAccounts("no");
//
//                            users.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                                    .setValue(user)
//                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                        @Override
//                                        public void onSuccess(Void unused) {
//                                            Snackbar.make(helloActivity, "Пользователь добавлен!", Snackbar.LENGTH_SHORT).show();
//                                        }
//                                    });
//                        }
//
//                    });
//            }
//        });
//
//        dialog.show();
//    }
//
//
//    private void showNewAccountCard (String idUser){
//        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
//        dialog.setTitle("Создание счета");
//        dialog.setMessage("Введите идентификационный номер вашего счета:");
//
//        LayoutInflater inflater = LayoutInflater.from(this);
//        View newAccountCard = inflater.inflate(R.layout.new_account_card, null);
//        dialog.setView(newAccountCard);
//
//        EditText editTextAccountName = newAccountCard.findViewById(R.id.editAccountName);
//        //String allAccounts = settings.getString(PREF_ALL_ACCOUNTS, "");
//
//        //обращение к Firebase, перенести
//        /*
//        users.child(idUser).child("accounts_names").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DataSnapshot> task) {
//                if (!task.isSuccessful()) {
//                    //Log.e("firebase", "Error getting data", task.getException());
//                }
//                else {
//                    //Log.d("firebase", String.valueOf(task.getResult().getValue()));
//
//                }
//            }
//        });
//
//         */
//
//        dialog.setPositiveButton("Создать", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int which) {
//                String accountName = editTextAccountName.getText().toString();
//                if (TextUtils.isEmpty(accountName) && accountName.contains(" ")) {
//                    Snackbar.make(helloActivity, "Введите имя счета (без пробелов)", Snackbar.LENGTH_SHORT).show();
//                    //return;
//                } else {
//                    SharedPreferences.Editor preferenceEditor = settings.edit();
//                    preferenceEditor.putString(PREF_ACCOUNT, accountName);
//                    //preferenceEditor.putString(PREF_ALL_ACCOUNTS, ???);
//                    preferenceEditor.apply();
//                    //auth.
//                    startActivity(new Intent(LoginActivity.this, MainActivity.class)); //потом перенести в select account
//                    finish(); //потом перенсти в select account
//
//                }
//            }
//        });
//        dialog.show();
//    }
//
//    private void showSelectAccountCard (String idUser) {
//        Log.i(TAG, "Вошли в SelectAccountCard");
//        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
//        dialog.setTitle("Выберете счет");
//        dialog.setMessage("из выпадающего списка:");
//
//        LayoutInflater inflater = LayoutInflater.from(this);
//        View selectAccountCard= inflater.inflate(R.layout.select_account_card, null);
//        dialog.setView(selectAccountCard);
//
//        //String allAccounts = "no";
//                //= settings.getString(PREF_ALL_ACCOUNTS, "");
//        SharedPreferences.Editor preferenceEditor = settings.edit();
//
//        Log.i(TAG, "А до сюда дойдем?");
//
//
//        users.child(idUser).child("account_names").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DataSnapshot> task) {
//                if (!task.isSuccessful()) {
//                    //Snackbar.make(helloActivity, "Ошибка  в selectAccount!", Snackbar.LENGTH_SHORT).show();
//                    //Log.e("firebase", "Error getting data", task.getException());
//                    Log.i(TAG, "Ошибка  в selectAccount!");                }
//                else {
//                    preferenceEditor.putString(PREF_ALL_ACCOUNTS, String.valueOf(task.getResult().getValue()));
//                    preferenceEditor.apply();
//                    Log.i(TAG, "Получили в ALL_ACCOUNTS: " + String.valueOf(task.getResult().getValue()));
//
//                    //Log.d("firebase", String.valueOf(task.getResult().getValue()));
//
//                }
//            }
//        });
//
//
//
//        //String[] arrayAllAccounts = settings.getString(PREF_ALL_ACCOUNTS, "").split(" ");
//        String[] arrayAllAccounts = {"acc1", "acc2"};
//
//
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayAllAccounts);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        Spinner spinner = findViewById(R.id.spinner_accounts);
//        spinner.setAdapter(adapter);
//
//
//        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                String item = (String)parent.getItemAtPosition(position);
//                preferenceEditor.putString(PREF_ACCOUNT, item);
//                preferenceEditor.apply();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        };
//        spinner.setOnItemClickListener((AdapterView.OnItemClickListener) itemSelectedListener);
//
//
//        dialog.setPositiveButton("Открыть", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int which) {
//                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
//                    finish();
//            }
//        });
//
//        dialog.show();
//    }

}