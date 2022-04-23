package controllers.list_enums;

public class AlertRegistration {

    public final static String allFieldsEmpty = "Заповніть форму реєстрації!";


    public final static String phoneNumberValid = "Телефон повинен бути вписаний в форматі +380-XX-XXX-XX-XX,\n" + "+380 XX XXX XX XX";
    public final static String emailNumberValid = "Некоректно вказаний email!";
    public final static String emailBannedMailService = "Поштові сервіси mail.ru, rambler та yandex заборонені на території України!";

    public final static String passwordValid = "Пароль в некоректному форматі. Мінімум:\n" +
            "8 символів, максимум 20, виключно латинські;\n" +
            "3 літери верхнього/нижнього регістру;\n" +
            "Одна літера верхнього регістру;\n" +
            "Один символ;\n" +
            "Одне число;\n" +
            "Також в паролі не повині фігурувати ваші ПІБ, дата нарождення, email, номер телефону, та легкі ключі\n"+
            "(qwerty, abcd, password, тощо).";

    public final static String firstAndLastNameValid = "Прізвище, ім'я та по-батькові повині бути прописані українською мовою," +
            " з великої літери. в родовому відмінку!";

    public final static String passwordsNotIdentical = "Паролі не співпадають!";

    public final static  String ageOfClientError = "Вік кліента може бути не молодше 16 років.";
    public final static  String personAlreadyInBase = "Особа з цими контактними даними вже відкрила рахунок в нашому банку!";

    public final static  String personAlreadyHavePhone = "За цим телефоном вже зареєстрований акаунт!";
    public final static  String personAlreadyHaveEmail = "За цим email вже зареєстрований акаунт!";

}
