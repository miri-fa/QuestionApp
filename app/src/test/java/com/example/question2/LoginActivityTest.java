package com.example.question2;

import android.content.Intent;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class LoginActivityTest {

    @Mock
    private FirebaseAuth mAuth;

    @Mock
    private EditText emailView;

    @Mock
    private EditText passView;

    private LoginActivity loginActivity;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        loginActivity = new LoginActivity();
        loginActivity.setmAuth(mAuth);
        loginActivity.setEmailView(emailView);
        loginActivity.setPassView(passView);
    }

    @Test
    public void testUserLogin_Successful() {
        // Configurar el comportamiento esperado
        String email = "example@email.com";
        String password = "password";

        when(emailView.getText().toString()).thenReturn(email);
        when(passView.getText().toString()).thenReturn(password);

        Task<AuthResult> task = mock(Task.class);
        when(mAuth.signInWithEmailAndPassword(email, password)).thenReturn(task);
        when(task.isSuccessful()).thenReturn(true);

        // Llamar al método que se va a probar
        loginActivity.userLogin();

        // Verificar que se llamaron a los métodos esperados
        verify(emailView).getText().toString();
        verify(passView).getText().toString();
        verify(mAuth).signInWithEmailAndPassword(email, password);
        verify(task).addOnCompleteListener(any(OnCompleteListener.class));
        // Verificar que se inicia una actividad después del inicio de sesión exitoso
        verify(loginActivity).startActivity(any(Intent.class));
    }
}
