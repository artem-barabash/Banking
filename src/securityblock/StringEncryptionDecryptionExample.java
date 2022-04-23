package securityblock;

public class StringEncryptionDecryptionExample {
    public static void main(String[] args) {
        final String secretKey = "JHKLXABYZC";

        String originalString = "Kurwa@1234";
        System.out.println("originalString.length()=" +originalString.length());
        String encryptedString = AESUtils.encrypt(originalString, secretKey) ;
        String decryptedString = AESUtils.decrypt(encryptedString, secretKey) ;

        System.out.println("String Before Encryption is :");
        System.out.println(originalString);
        System.out.println("String After Encryption is :");
        System.out.println(encryptedString);
        System.out.println("encryptedString.length()=" + encryptedString.length());
        System.out.println("String After Decryption is:");
        System.out.println(decryptedString);
        System.out.println("decryptedString.length()=" +decryptedString.length());

        System.out.println("password=" + AESUtils.encrypt("1567", CryptoControl.secretKeyForPinCode));
    }
}
