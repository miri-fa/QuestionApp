package com.example.question2;


import androidx.appcompat.app.AppCompatActivity;

public class HelpManager extends AppCompatActivity {
    private String content;

    public HelpManager(){
        content = "";
    }
//Help messages
    public String getContent(int number){
        switch (number){
            case 1: content = "Esta es la pantalla principal, aquí se muestran los cuestionarios que han completado sobre ti para que puedas ver las respuestas de tus compañeros.";
                    break;
            case 2: content = "En esta pantalla debes introducir el código de la encuesta que te ha proporcionado tu profesor y así podrás completar la encuesta sobre tus compañeros.";
                    break;
            case 3: content = "Aquí puedes navegar entre las preguntas de la encuesta y comparar las respuestas de tus compañeros";
                    break;
            case 4: content = "Esta es la pantalla principal, aquí podrás ver las encuestas que has creado y acceder a ellas para ver los resultados";
                    break;
            case 5: content = "Aquí puedes crear una encuesta nueva. Primero introduce un título y luego crea las preguntas a tu gusto";
                    break;
            case 6: content = "Aquí puedes navegar entre las preguntas de la encuesta y comprobar los resultados dados. Si eres profesor, puedes editar o exportar las respuestas";
                    break;
            case 7: content = "Edita las preguntas a tu gusto y navega entre ellas para ultimar los detalles";
                    break;
        }

        return content;
    }
}
