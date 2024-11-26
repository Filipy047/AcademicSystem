package ucan.edu.academic.utils;

import com.github.javafaker.Faker;

import java.time.LocalDate;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class EstudanteUtils {
    private static final Faker faker = new Faker(new Locale("pt-BR"));

    // Método para gerar número de estudante no padrão 006152516LA042
    public static String gerarNumeroEstudante() {
        return String.format(
            "006%06dLA%03d",
            faker.number().numberBetween(100000, 999999),
            faker.number().numberBetween(1, 999)
        );
    }

    // Método para gerar uma data de nascimento entre 20 e 60 anos atrás
    public static LocalDate gerarDataNascimento() {
        return faker.date()
                .birthday(20, 60)
                .toInstant()
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDate();
    }

    // Método para gerar uma data de matrícula fictícia
    public static LocalDate gerarDataMatricula() {
        return faker.date()
                .past(2000, TimeUnit.DAYS)
                .toInstant()
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDate();
    }

    // Método para gerar o gênero (Masculino ou Feminino)
    public static String gerarGenero() {
        return faker.options().option("Masculino", "Feminino");
    }

    // Métodos para outros atributos
    public static String gerarNome() {
        return faker.name().fullName();
    }

    public static String gerarEmail() {
        return faker.internet().emailAddress();
    }

    public static String gerarTelefone() {
        return faker.phoneNumber().cellPhone();
    }

    public static String gerarEndereco() {
        return faker.address().streetAddress();
    }
}
