package util;

// Metodos simples para validar o que o usuario digita nas telas
public class Validador {

    // Retorna true se o texto for nulo ou so tiver espacos
    public static boolean campoVazio(String texto) {
        return texto == null || texto.trim().isEmpty();
    }

    // Retorna true se o texto puder virar um numero inteiro (ex: idade)
    public static boolean ehInteiro(String texto) {
        try {
            Integer.parseInt(texto.trim());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Retorna true se o texto puder virar um numero com virgula ou ponto (ex: 150,00)
    public static boolean ehDecimal(String texto) {
        try {
            Double.parseDouble(texto.trim().replace(",", "."));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Converte "150,50" para 150.50
    public static double paraDecimal(String texto) {
        return Double.parseDouble(texto.trim().replace(",", "."));
    }

    // Verifica se o e-mail tem pelo menos um @ e um ponto depois dele
    public static boolean emailValido(String email) {
        if (campoVazio(email)) {
            return false;
        }
        int arroba = email.indexOf('@');
        return arroba > 0 && email.indexOf('.', arroba) > arroba + 1 && !email.endsWith(".");
    }
}
