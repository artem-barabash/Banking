package instruments;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import models.Person;
import securityblock.AESUtils;
import securityblock.CryptoControl;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;


public class CreatePDFfilesClass {

   public static void createSertificatOfUser(Person person, String pinCode) throws DocumentException, IOException, SQLException {
       var doc = new Document();
       String filePath = "F:/banking/sertificat/text" + generatePwd() + ".pdf";
        try {
            PdfWriter.getInstance(doc, new FileOutputStream(filePath));
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        doc.open();

        BaseFont bf = BaseFont.createFont("F:\\banking\\font\\times.ttf", "cp1251", BaseFont.EMBEDDED);
        Font caption = new Font(bf, 20, Font.BOLD, new CMYKColor(0, 255, 255,17));


        Paragraph title1 = new Paragraph("СЕРТИФІКАТ", caption);
        title1.setAlignment(Element.ALIGN_CENTER);


        Chapter chapter1 = new Chapter(title1, 1);
        chapter1.setNumberDepth(0);

        Font paragraphFont = new Font(bf, 14);
        Paragraph title11 = new Paragraph(getParagraphName(person, pinCode), paragraphFont);

        Section section1 = chapter1.addSection(title11);


        PdfPTable t = new PdfPTable(2);

        t.setSpacingBefore(25);

        t.setSpacingAfter(25);

        PdfPCell c1 = new PdfPCell(new Phrase(String.valueOf(new Paragraph("Телефон")), paragraphFont));
        t.addCell(c1);
        PdfPCell c2 = new PdfPCell(new Phrase(String.valueOf(new Paragraph(person.phone)), paragraphFont));
        t.addCell(c2);


        PdfPCell c3 = new PdfPCell(new Phrase(String.valueOf(new Paragraph("Дата народження")), paragraphFont));
        t.addCell(c3);
        PdfPCell c4 = new PdfPCell(new Phrase(String.valueOf(new Paragraph(String.valueOf(person.birthday))), paragraphFont));
        t.addCell(c4);

        PdfPCell c5 = new PdfPCell(new Phrase(String.valueOf(new Paragraph("Пароль")), paragraphFont));
        t.addCell(c5);
        //декодуемо пароль, щоб юзеру було зроміло в документі
        String decryptPassword = AESUtils.decrypt(person.password, CryptoControl.secretKey);
        PdfPCell c6 = new PdfPCell(new Phrase(String.valueOf(new Paragraph(decryptPassword)), paragraphFont));
        t.addCell(c6);



        section1.add(t);

        Paragraph footer = new Paragraph(getDataForFoooter(), paragraphFont);

        section1.add(footer);


        doc.add(chapter1);
        doc.close();
    }

    private static String getDataForFoooter() {
        SimpleDateFormat dnt = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();

        return dnt.format(date) +  "  Підпис _______";
    }

    private static String getParagraphName(Person person, String pinCode) throws SQLException {

        return "Цей сертифікат засвідчує, що громадянин " + person.surname + " " +  person.name + " " + person.fatherName +
                " відкрив  рахунок : " + person.getNumberCode() +" в банку 'Мій банк', та отримав картку з відповідним номером.Пін-код картки: " + pinCode
                + " (Не повідомляти нікому, навіть працівникам банку).";
    }



    private static String generatePwd() {
        String charsCaps = "abcdefghijklmnopqrstuvwxyz";
        String nums = "0123456789";
        String passSymbols = charsCaps + nums;
        Random rnd = new Random();

        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            sb.append(passSymbols.charAt(rnd.nextInt(passSymbols.length())));
        }
        return sb.toString();
    }


    public void createDocumentOfTransakcia(Person person1, Person person2, float summ) throws IOException, DocumentException {
        var doc = new Document();
        String filePath = "F:/banking/transactions/transaction" + generatePwd() + ".pdf";
        try {
            PdfWriter.getInstance(doc, new FileOutputStream(filePath));
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        doc.open();

        BaseFont bf = BaseFont.createFont("F:\\banking\\font\\times.ttf", "cp1251", BaseFont.EMBEDDED);
        Font caption = new Font(bf, 20, Font.BOLD, new CMYKColor(0, 255, 255,17));


        Paragraph title1 = new Paragraph("ТРАНЗАКЦІЯ", caption);
        title1.setAlignment(Element.ALIGN_CENTER);

        Chapter chapter1 = new Chapter(title1, 1);
        chapter1.setNumberDepth(0);

        Font paragraphFont = new Font(bf, 14);

        PdfPTable t = new PdfPTable(3);

        t.setSpacingBefore(25);
        t.setSpacingAfter(25);

        PdfPCell c1 = new PdfPCell(Phrase.getInstance(""));
        t.addCell(c1);
        PdfPCell c2 = new PdfPCell(new Phrase(String.valueOf(new Paragraph("ПІБ")), paragraphFont));
        t.addCell(c2);
        PdfPCell c3 = new PdfPCell(new Phrase(String.valueOf(new Paragraph("Номер рахунку")), paragraphFont));
        t.addCell(c3);
        t.addCell(new Phrase(String.valueOf(new Paragraph("Платник")), paragraphFont));
        t.addCell(new Phrase(String.valueOf(new Paragraph(person1.surname + " " + person1.name.substring(0, 1).toUpperCase() + "." +
                person1.fatherName.substring(0, 1).toUpperCase() + ".")), paragraphFont ));
        t.addCell(person1.getNumberCode());
        t.addCell(new Phrase(String.valueOf(new Paragraph("Отримувач")), paragraphFont));
        t.addCell( new Phrase(String.valueOf(new Paragraph(person2.surname + " " + person2.name.substring(0, 1).toUpperCase() + "." +
                person2.fatherName.substring(0, 1).toUpperCase() + ".")), paragraphFont));
        t.addCell(person2.getNumberCode());


        PdfPTable sum = new PdfPTable(2);

        sum.setSpacingBefore(25);
        sum.setSpacingAfter(25);

        PdfPCell t1 = new PdfPCell(new Phrase(String.valueOf(new Paragraph("Сума")), paragraphFont));
        sum.addCell(t1);
        PdfPCell t2 = new PdfPCell(new Phrase(String.valueOf(new Paragraph("Комісія")), paragraphFont));
        sum.addCell(t2);
        sum.addCell(new Phrase(String.valueOf(summ)));
        sum.addCell(new Phrase("0"));

        Paragraph date = new Paragraph(getDateInTransaction(), paragraphFont);

        Section section1 = chapter1.addSection("");
        section1.add(t);

        section1.add(sum);

        section1.add(date);
        doc.add(chapter1);

        doc.close();
    }

    public void createDocumnetOfServiceTransakcia(Person person1, String service, float summ) throws IOException, DocumentException {
        var doc = new Document();
        String filePath = "F:/banking/transactions/transaction" + generatePwd() + ".pdf";
        try {
            PdfWriter.getInstance(doc, new FileOutputStream(filePath));
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        doc.open();

        BaseFont bf = BaseFont.createFont("F:\\banking\\font\\times.ttf", "cp1251", BaseFont.EMBEDDED);
        Font caption = new Font(bf, 20, Font.BOLD, new CMYKColor(0, 255, 255,17));


        Paragraph title1 = new Paragraph("ТРАНЗАКЦІЯ", caption);
        title1.setAlignment(Element.ALIGN_CENTER);

        Chapter chapter1 = new Chapter(title1, 1);
        chapter1.setNumberDepth(0);

        Font paragraphFont = new Font(bf, 14);

        PdfPTable t = new PdfPTable(3);

        t.setSpacingBefore(25);
        t.setSpacingAfter(25);

        PdfPCell c1 = new PdfPCell(Phrase.getInstance(""));
        t.addCell(c1);
        PdfPCell c2 = new PdfPCell(new Phrase(String.valueOf(new Paragraph("ПІБ")), paragraphFont));
        t.addCell(c2);
        PdfPCell c3 = new PdfPCell(new Phrase(String.valueOf(new Paragraph("Номер рахунку")), paragraphFont));
        t.addCell(c3);
        t.addCell(new Phrase(String.valueOf(new Paragraph("Платник")), paragraphFont));
        t.addCell(new Phrase(String.valueOf(new Paragraph(person1.surname + " " +
                person1.name.substring(0, 1).toUpperCase() + "." + person1.fatherName.substring(0, 1).toUpperCase() + ".")), paragraphFont));
        t.addCell(person1.getNumberCode());

        Paragraph titleService = new Paragraph("Оплата " + service.toLowerCase() + ".", paragraphFont);


        PdfPTable sum = new PdfPTable(2);

        sum.setSpacingBefore(25);
        sum.setSpacingAfter(25);

        PdfPCell t1 = new PdfPCell(new Phrase(String.valueOf(new Paragraph("Сума")), paragraphFont));
        sum.addCell(t1);
        PdfPCell t2 = new PdfPCell(new Phrase(String.valueOf(new Paragraph("Комісія")), paragraphFont));
        sum.addCell(t2);
        sum.addCell(new Phrase(String.valueOf(summ)));
        sum.addCell(new Phrase("0"));

        Paragraph date = new Paragraph(getDateInTransaction(), paragraphFont);

        Section section1 = chapter1.addSection("");
        section1.add(t);

        section1.add(sum);
        section1.add(titleService);
        section1.add(date);
        doc.add(chapter1);

        doc.close();
    }

    private static String getDateInTransaction() {
        SimpleDateFormat dnt = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss  ");
        Date date = new Date();

        return dnt.format(date);
    }

}
