package br.com.ifirewall.ui;

import javafx.application.Application;

/**
 * Ponto de entrada do fat JAR.
 *
 * <p>Esta classe NÃO estende {@link Application} de propósito: quando a main-class
 * de um JAR estende {@code Application}, a JVM exige que os módulos do JavaFX
 * estejam no module-path e falha com "JavaFX runtime components are missing".
 * Delegar para {@link Application#launch} a partir de uma classe comum evita essa
 * verificação e permite iniciar a aplicação via {@code java -jar}.</p>
 */
public class Launcher {

    public static void main(String[] args) {
        Application.launch(App.class, args);
    }
}
