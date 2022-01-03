package com.harman.taxapp.ui.register;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.harman.taxapp.R;
import com.harman.taxapp.usersdata.User;

import java.util.Objects;

public class RegisterFragment extends Fragment {

    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference users;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        users = database.getReference("Users");

        EditText name = view.findViewById(R.id.editTextName);
        EditText email = view.findViewById(R.id.editTextEmail);
        EditText phone = view.findViewById(R.id.editTextPhone);
        EditText password = view.findViewById(R.id.editTextPassword);

        Button buttonCancel = view.findViewById(R.id.button_new_user_cancel);
        buttonCancel.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_registerFragment_to_loginFragment));

        Button buttonRegister = view.findViewById(R.id.button_new_user_create);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(email.getText().toString())) { //добавить проверку e-mail, firebase не всегда регит
                    Snackbar.make(v, "Введите ваш email", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(name.getText().toString())) {
                    Snackbar.make(v, "Введите ваше имя", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(phone.getText().toString())) {
                    Snackbar.make(v, "Введите ваш телефон", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (password.getText().toString().length() < 8 ) {
                    Snackbar.make(v, "Введите пароль, который больше 8 символов", Snackbar.LENGTH_SHORT).show();
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
                                user.setAllAccounts("no");

                                users.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(user)
                                        //может поменять на addOnCompleteListener??
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Snackbar.make(v, "Пользователь добавлен!", Snackbar.LENGTH_SHORT).show();
                                                Navigation.findNavController(v).navigate(R.id.action_registerFragment_to_firstAccountFragment);
                                            }

                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Snackbar.make(v, Objects.requireNonNull(e.getMessage()), Snackbar.LENGTH_SHORT).show();
                                            }
                                        });
                            }


                        });
            }
        });

    }
}