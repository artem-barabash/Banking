package conn;

import com.itextpdf.text.DocumentException;
import conn.data_for_connect.ConnectDB;
import instruments.CreatePDFfilesClass;
import models.Person;
import securityblock.AESUtils;
import securityblock.CryptoControl;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Random;

public class CardIssuanceForUser {


    public static void insertCardForUser(Person person) throws FileNotFoundException, DocumentException {
        try {
            String sql = "INSERT INTO card VALUES (?, ?)";

            PreparedStatement pstmt = ConnectDB.getConnection().prepareStatement(sql);
            String pinCode = generatePinCode();
            //кодим пин-код
            String encryptPinCode = AESUtils.encrypt(pinCode, CryptoControl.secretKeyForPinCode);
            pstmt.setString(1, person.getNumberCode());
            pstmt.setString(2, encryptPinCode);
            createCardForPerson(person.getNumberCode());

            CreatePDFfilesClass.createSertificatOfUser(person,pinCode);
            pstmt.executeUpdate();
        }catch (SQLException | IOException e) {
            e.printStackTrace();
            return;
        }
    }

    private static void createCardForPerson(String numberCode) {
        String fileName = generatePwd() + ".txt";

        try(FileWriter writer = new FileWriter("F:/banking/cards/" + fileName, false))
        {
            writer.write(numberCode);
            writer.flush();
        }
        catch(IOException ex){

            System.out.println(ex.getMessage());
        }
    }

    private static String generatePwd() {
        String charsCaps = "abcdefghijklmnopqrstuvwxyz";
        String nums = "0123456789";
        String passSymbols = charsCaps + nums;
        Random rnd = new Random();

        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            sb.append(passSymbols.charAt(rnd.nextInt(passSymbols.length())));
        }
        return sb.toString();
    }



    public static String generatePinCode() throws SQLException {
        String current = null;
        ArrayList<String> currentArray = checkForPinCode();
        for (String num : currentArray){
            String code = generatePinCodeNumber();
            if(!code.equals(num)){
                current = code;
            }
        }

        return current;
    }

    private static String generatePinCodeNumber() {
        String code = "";
        int[] array = new int[4];
        for (int i = 0; i < array.length; i++) {
            array[i] = (int) Math.round((Math.random() * 9) - 0);
            code += array[i];
        }
        return code;
    }


    public static ArrayList <String> checkForPinCode() throws SQLException {
        ArrayList <String> array = new ArrayList<>();
        Statement stmt = ConnectDB.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM card");
        while (rs.next()) {
            String current = rs.getString("pincode");
            array.add(current);
        }
        return  array;
    }


}
