package UTILS;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

public class Validador {
    //Regex para E-mail
    private static final String EMAIL_REGEX = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
    
    //Regex para Placa (AAA-1234 ou AAA1B23)
    private static final String PLACA_REGEX = "[A-Z]{3}[0-9][0-9A-Z][0-9]{2}";
    
    //Regex para Telefone (XX) 9XXXX-XXXX, XX 9XXXXXXXX, ou telefone fixo
    private static final String TELEFONE_REGEX = "^\\(?\\d{2}\\)?\\s?(9?\\d{4})-?\\d{4}$";
    
    //Aceita XXXXX-XXX ou XXXXXXXX
    private static final String CEP_REGEX = "^\\d{5}-?\\d{3}$";

    public static boolean isVazio(String texto){
        return texto == null || texto.trim().isEmpty();
    }

    public static boolean isEmailValido(String email){
        if(isVazio(email)){
        	return false;
        }
        return Pattern.matches(EMAIL_REGEX, email);
    }

    public static boolean isCpfValido(String cpf){
        if(isVazio(cpf)){
        	return false;
        }
        String cpfLimpo = cpf.replaceAll("[^0-9]", "");
        return cpfLimpo.length() == 11;
    }

    public static boolean isPlacaValida(String placa){
        if(isVazio(placa)){
        	return false;
        }
        String placaLimpa = placa.replace("-", "").toUpperCase();
        return Pattern.matches(PLACA_REGEX, placaLimpa);
    }

    public static boolean isDataValida(String dataStr){
        if(isVazio(dataStr)){
        	return false;
        }
        try{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate.parse(dataStr, formatter);
            return true;
        }catch(DateTimeParseException e){
            return false;
        }
    }

    public static boolean isNumero(String str){
        if(isVazio(str)){
        	return false;
        }
        try{
            Double.parseDouble(str.replace(",", ".")); //Aceita '.' ou ','
            return true;
        }catch(NumberFormatException e){
            return false;
        }
    }
    
    public static boolean isTelefoneValido(String telefone){
        if(isVazio(telefone)) return false;
        return Pattern.matches(TELEFONE_REGEX, telefone);
    }
    
    public static boolean isCepValido(String cep){
        if(isVazio(cep)) return false;
        return Pattern.matches(CEP_REGEX, cep);
    }
    
}