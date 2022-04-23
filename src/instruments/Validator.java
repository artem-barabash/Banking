package instruments;

import conn.PersonalDataControl;
import controllers.list_enums.AlertRegistration;
import controllers.list_enums.Gender;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import models.OperationItem;
import models.Person;

import javax.swing.*;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {
    public Validator(){}

    //1номер телефона
    public  boolean methodValidPhoneNumber(String phoneNumber){
        //только в формате +380-XX-XXX-XX-XX +380 XX XXX XX XX
        String phoneRegex = "^\\+?3?8?(0[\\s\\.-]\\d{2}[\\s\\.-]\\d{3}[\\s\\.-]\\d{2}[\\s\\.-]\\d{2})$";

        Pattern pattern = Pattern.compile(phoneRegex);
        if (pattern.matcher(phoneNumber).matches()) {
            return true;
        } else {
            methodAlertErrorOnRegistration(AlertRegistration.phoneNumberValid);
            return false;
        }

    }
    //2email
    public boolean methodValidEmail(String emailAddress){
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +"[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);

        if (pattern.matcher(emailAddress).matches()){
            if(emailAddress.indexOf("mail.ru") != -1 || emailAddress.indexOf("yandex") != -1 || emailAddress.indexOf("rambler") != -1) {
                methodAlertErrorOnRegistration(AlertRegistration.emailBannedMailService);
                return false;
            }else return true;
       }else {
            methodAlertErrorOnRegistration(AlertRegistration.emailNumberValid);
            return false;
        }
    }
    //Alert об ошибке
    static void methodAlertErrorOnRegistration(String text){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Alert");
        alert.setContentText(text);

        alert.showAndWait();
    }


    /////
    //внутрений статический класс со статичсекими методами
    public static class ValidatorForPersonality{
        //4 пропервка ФИО на корректность
        public static boolean methodCheckPersonalityData(String text){
            //проверяем на кирилицу, большую первую букву, и маленькие осталные
            boolean isCorrectly = isCyrillic(text) && Character.isUpperCase(text.charAt(0)) && isStringLowerCase(text.substring(1));

            if (!isCorrectly) methodAlertErrorOnRegistration(AlertRegistration.firstAndLastNameValid);
            return isCorrectly;
        }



        //4.1проверка на маленькие буквы
        private static boolean isStringLowerCase(String substring) {
            for(int i = 0; i < substring.length(); i++){
                if(!Character.isLowerCase(substring.charAt(i))) return false;
            }
            return true;
        }

        //проверка кирилицы
        public static boolean isCyrillic(String s) {
            boolean result = true;
            for (char a : s.toCharArray()) {
                if (Character.UnicodeBlock.of(a) != Character.UnicodeBlock.CYRILLIC) {
                    result = false;
                    break;
                }
            }
            return result;
        }
    }
    /////

    //внутрений статический класс который нужно обьявить
    public static class PasswordValidator{
        public PasswordValidator(){}
        //подумать
        //3пароль проверяем
        public  boolean methodValidPassword(Person person){
            //не менше 8 символов
            //только латиница
            //и верхний и нижний регист
            //обязательно: одна с верхним регистром, один символ, и одна цифра
            int letter = 0;
            int upperCase = 0;
            int numbers = 0;
            int symbols = 0;


            String password = person.password;
            for(int i = 0; i < password.length(); i++){
                if(methodCheckTextStrings(String.valueOf(password.charAt(i)))) letter++;
                if(methodCheckTextNumbers(String.valueOf(password.charAt(i)))) numbers++;
                if(methodCheckTextSymbols(String.valueOf(password.charAt(i)))) symbols++;
                if(Character.isUpperCase(password.charAt(i))) upperCase++;
            }

            System.out.println("letter=" + letter + " upperCase=" + upperCase + " numbers=" + numbers + " symbols=" + symbols);

            boolean result = password.length() >= 8 && password.length() <= 20 && letter >= 3 && upperCase >= 1 &&
                    numbers >=1 && symbols >=1 && !isPasswordWithPersonalityData(person);

            //TODO проверка пароля на наличие в нем личных доступных даных: ФИО, дата рождения, телфеон, email.

            if(!result) methodAlertErrorOnRegistration(AlertRegistration.passwordValid);

            return result;
        }

        //3.1проверка на буквы
        private boolean methodCheckTextStrings(String s) {
            Pattern pattern = Pattern.compile("[a-zA-Z]");
            Matcher matcher = pattern.matcher(s);
            boolean result = matcher.find();

            return result;
        }


        //3.1проверка на числа
        private boolean methodCheckTextNumbers(String s) {
            Pattern pattern = Pattern.compile("[-]?[0-9]+(.[0-9]+)?");
            Matcher matcher = pattern.matcher(s);
            boolean result = matcher.find();

            return result;
        }

        //3.2проверка на символы
        private boolean methodCheckTextSymbols(String s) {
            String specialCharactersString = "!@#$%&*()'+,-./:;<=>?[]^_`{|}";

            boolean result = specialCharactersString.contains(s);

            return result;
        }

        //3.4 проверка пароля на наличие в нем личных доступных даных: ФИО, дата рождения, телфеон, email.
        private  boolean isPasswordWithPersonalityData(Person person) {
            //если есть личные даные true, в противном случае false

            String surName = transliterate(person.surname.toLowerCase());
            String name = transliterate(person.name.toLowerCase());
            String fatherName = transliterate(person.fatherName.toLowerCase());

            //ищет отывок из email в пароле
            String email = person.email.substring(0, person.email.indexOf('@')).toLowerCase();
            //System.out.println("email=" + email);

            //уменшаем до маленькой буквы вся строку
            String passwordToLoweCase = person.password.toLowerCase();

            return passwordToLoweCase.contains(surName) || passwordToLoweCase.contains(name)
                    || passwordToLoweCase.contains(fatherName) || passwordToLoweCase.contains(email) || isPhoneNumberInPassword(person.password, person.phone)
                    || passwordCommonKeys(person.password) || isBirthDayInPassword(person.password, person.birthday);
        }

        //3.4.1трасліт з української на латинь
        private  String transliterate(String message){
            char[] abcCyr =   {' ','а','б','в','г','ґ','д','е','є','ж','з','и','і', 'ї','й', 'к','л','м','н','о','п','р','с','т','у','ф','х', 'ц','ч', 'ш','щ', 'ь', 'ю','я','А','Б','В','Г', 'Ґ', 'Д','Е','Є', 'Ж','З','И', 'І', 'Ї','Й','К','Л','М','Н','О','П','Р','С','Т','У','Ф','Х', 'Ц', 'Ч','Ш', 'Щ', 'Ю','Я','a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
            String[] abcLat = {" ","a","b","v","h","g","d","e","ye","zh","z","y","i","yi","y","k","l","m","n","o","p","r","s","t","u","f","kh","ts","ch","sh","shch","","yu","ya","A","B","V", "H","G","D","E","Ye","Zh","Z","Y","I", "Yi", "Y", "K","L","M","N","O","P","R","S","T","U","F","Kh","Ts","Ch","Sh","Shch", "Yu", "Ya","a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < message.length(); i++) {
                for (int x = 0; x < abcCyr.length; x++ ) {
                    if (message.charAt(i) == abcCyr[x]) {
                        builder.append(abcLat[x]);
                    }
                }
            }
            return builder.toString();
        }

        //3.4.2 проверка на вписаный номер телефона пароле

        private  boolean isPhoneNumberInPassword(String password, String number){
            String phoneNumberText = returnNumberFromString(number);
            String numbersInPassword = returnNumberFromString(password);

            System.out.println("phoneNumberText=" + phoneNumberText);
            System.out.println("numbersInPassword=" + numbersInPassword);

            //TODO вернуть в условие
            return numbersInPassword.contains(phoneNumberText) || numbersInPassword.contains(phoneNumberText.substring(2));
        }

        //3.4.2.1 Возвращаем номер телфеона в String
        private String returnNumberFromString(String text){

            StringBuilder sb = new StringBuilder();

            for(int i = 0; i < text.length(); i++){
                if(methodCheckTextNumbers(String.valueOf(text.charAt(i)))){
                    sb.append(Integer.parseInt(String.valueOf(text.charAt(i))));
                }
            }

            return sb.toString();
        }

        //проверка на распространеные ключи
        private boolean passwordCommonKeys(String password) {
            String commonKeysValue[] = {
                    "password", "qwerty", "abc", "abcd"
            };

            for(String s : commonKeysValue){
                if(password.toLowerCase().contains(s)) return true;
            }

            return false;
        }

        //3.4.3
        private boolean isBirthDayInPassword(String password, LocalDate birthday) {
            //TODO проверка на наличие даты рождения и названия месяца
            String numbersInPasswordString = returnNumberFromString(password);
            System.out.println(numbersInPasswordString);
            //String numbersInDate = birthday.getDayOfMonth() +""+  birthday.getMonth().getValue()+""+ birthday.getYear();
            //System.out.println(numbersInDate);

            return numbersInPasswordString.contains(String.valueOf(birthday.getYear()));
        }

    }


}
