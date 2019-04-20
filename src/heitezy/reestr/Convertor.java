package heitezy.reestr;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.opencsv.CSVReader;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;

import static com.itextpdf.text.Font.NORMAL;


class Convertor {

    static void convert(String inputpath, String outputpath) throws IOException, DocumentException {
        File inputfiles = new File(inputpath);
        ArrayList<File> listoffiles = new ArrayList<>(Arrays.asList(Objects.requireNonNull(inputfiles.listFiles((file, filterstring) -> {
            if (filterstring.lastIndexOf('.') > 0) {
                int lastIndex = filterstring.lastIndexOf('.');
                String extension = filterstring.substring(lastIndex);
                if (extension.equals(".xls")) {
                    return true;
                } else return extension.equals(".csv");
            }
            return false;
        }))));
        HSSFWorkbook[] outputfiles = new HSSFWorkbook[listoffiles.size()];
        batchProcess(listoffiles, outputfiles, outputpath);
    }

    private static void batchProcess(ArrayList<File> filesToProcess, HSSFWorkbook[] wbToProcess, String outputpath) throws IOException, DocumentException {

        String[] wbname = new String[wbToProcess.length];

        for (int i = 0; i < wbToProcess.length; i++) {
            String XlsOrCSV = filesToProcess.get(i).toString();
            int lastIndex = XlsOrCSV.lastIndexOf('.');
            String extension = XlsOrCSV.substring(lastIndex);

            if (extension.equals(".csv")) {
                wbToProcess[i] = convertCsvToXls(filesToProcess.get(i));
            } else {
                wbToProcess[i] = readWorkbook(filesToProcess.get(i).toString());
            }

            String filename = filesToProcess.get(i).toString();
            wbname [i] = outputpath + filename.substring(filename.lastIndexOf(File.separator));
        }
        magic(wbToProcess, wbname);
    }

    private static HSSFWorkbook readWorkbook(String filename) {
        try {
            POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(filename));
            return new HSSFWorkbook(fs.getRoot(), true);
        } catch (Exception e) {
            return null;
        }
    }

    private static void magic(HSSFWorkbook[] wbToProcess, String[] wbname) throws IOException, DocumentException {

        for (int i = 0; i < wbToProcess.length; i++) {
            HSSFWorkbook wbToProcessSingle = wbToProcess[i];

            String dateOfDocument = null;
            int reestr_type = 0;

            HSSFWorkbook templatewb = new HSSFWorkbook();
            templatewb.createSheet("TempSheet");
            HSSFSheet sheetSingle = wbToProcessSingle.getSheetAt(0);
            HSSFSheet templateSheet = templatewb.getSheetAt(0);

            try {
                if (sheetSingle.getRow(4).getCell(1).toString().contains("ВЕНТА. ЛТД")) {

                    String datecell = sheetSingle.getRow(4).getCell(2).toString();
                    int predate = datecell.lastIndexOf(' ');
                    dateOfDocument = datecell.substring(predate + 1);
                    reestr_type = 1;

                    for (int j = 4; j < sheetSingle.getPhysicalNumberOfRows() - 2; j++) {

                        HSSFRow rowSingle = sheetSingle.getRow(j);
                        HSSFRow rowTemplate = templateSheet.createRow(j - 4);

                        Iterator<Cell> cellIterator = rowSingle.cellIterator();

                        while (cellIterator.hasNext()) {
                            Cell cellIn = cellIterator.next();
                            Cell cellOut = rowTemplate.createCell(cellIn.getColumnIndex(), cellIn.getCellType());

                            switch (cellIn.getCellType()) {
                                case BLANK:
                                    break;

                                case BOOLEAN:
                                    cellOut.setCellValue(cellIn.getBooleanCellValue());
                                    break;

                                case ERROR:
                                    cellOut.setCellValue(cellIn.getErrorCellValue());
                                    break;

                                case FORMULA:
                                    cellOut.setCellFormula(cellIn.getCellFormula());
                                    break;

                                case NUMERIC:
                                    cellOut.setCellValue(cellIn.getNumericCellValue());
                                    break;

                                case STRING:
                                    cellOut.setCellValue(cellIn.getStringCellValue());
                                    break;
                            }
                        }
                    }
                } else if (sheetSingle.getRow(8).getCell(0).toString().contains("БаДМ")) {

                    String datecell = sheetSingle.getRow(8).getCell(1).toString();
                    int predate = datecell.lastIndexOf(' ');
                    dateOfDocument = datecell.substring(predate + 1);
                    reestr_type = 2;

                    for (int j = 8; j < sheetSingle.getPhysicalNumberOfRows(); j++) {

                        HSSFRow rowSingle = sheetSingle.getRow(j);
                        HSSFRow rowTemplate = templateSheet.createRow(j - 8);

                        Iterator<Cell> cellIterator = rowSingle.cellIterator();

                        while (cellIterator.hasNext()) {
                            Cell cellIn = cellIterator.next();
                            Cell cellOut = rowTemplate.createCell(cellIn.getColumnIndex(), cellIn.getCellType());

                            switch (cellIn.getCellType()) {
                                case BLANK:
                                    break;

                                case BOOLEAN:
                                    cellOut.setCellValue(cellIn.getBooleanCellValue());
                                    break;

                                case ERROR:
                                    cellOut.setCellValue(cellIn.getErrorCellValue());
                                    break;

                                case FORMULA:
                                    cellOut.setCellFormula(cellIn.getCellFormula());
                                    break;

                                case NUMERIC:
                                    cellOut.setCellValue(cellIn.getNumericCellValue());
                                    break;

                                case STRING:
                                    cellOut.setCellValue(cellIn.getStringCellValue());
                                    break;
                            }
                        }
                    }
                } else if (sheetSingle.getRow(8).getCell(1).toString().contains("Оптiма-Фарм")) {

                    String datecell = sheetSingle.getRow(9).getCell(2).toString();
                    int predate = datecell.lastIndexOf(' ');
                    dateOfDocument = datecell.substring(predate + 1);
                    reestr_type = 3;

                    for (int j = 8; j < sheetSingle.getPhysicalNumberOfRows() - 3; j++) {

                        HSSFRow rowSingle = sheetSingle.getRow(j);
                        HSSFRow rowTemplate = templateSheet.createRow(j - 8);

                        Iterator<Cell> cellIterator = rowSingle.cellIterator();

                        while (cellIterator.hasNext()) {
                            Cell cellIn = cellIterator.next();
                            Cell cellOut = rowTemplate.createCell(cellIn.getColumnIndex(), cellIn.getCellType());

                            switch (cellIn.getCellType()) {
                                case BLANK:
                                    break;

                                case BOOLEAN:
                                    cellOut.setCellValue(cellIn.getBooleanCellValue());
                                    break;

                                case ERROR:
                                    cellOut.setCellValue(cellIn.getErrorCellValue());
                                    break;

                                case FORMULA:
                                    cellOut.setCellFormula(cellIn.getCellFormula());
                                    break;

                                case NUMERIC:
                                    cellOut.setCellValue(cellIn.getNumericCellValue());
                                    break;

                                case STRING:
                                    cellOut.setCellValue(cellIn.getStringCellValue());
                                    break;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Неизвестный поставщик");
            }
            convertToPdf(templatewb, wbname[i], dateOfDocument, reestr_type);
        }
    }

    private static HSSFWorkbook convertCsvToXls(File csvFile) throws IOException {
        //todo Convertion to xls
        HSSFWorkbook tempwb = new HSSFWorkbook();
        HSSFSheet tempst = tempwb.createSheet("TempCsvSheet");

        CSVReader reader = new CSVReader(new InputStreamReader(new FileInputStream(csvFile), "Cp1251"));
        String[] nextLine;
        int rowNum = 0;

        while ((nextLine = reader.readNext()) != null) {

            HSSFRow currentRow = tempst.createRow(rowNum++);
            for (int i = 0; i < nextLine.length; i++) {
                currentRow.createCell(i).setCellType(CellType.STRING);
                currentRow.getCell(i).setCellValue(nextLine[i]);
            }
            if (currentRow.getRowNum() == 4) {
                currentRow.createCell(1).setCellValue("");
            }
        }
        try {
            System.out.println(tempst.getRow(8).getCell(0).toString());
        } catch (Exception e) {
            System.out.println("Это не Бадм");
        }
        return tempwb;
    }

    private static void convertToPdf(HSSFWorkbook wb, String wbname, String dateOfDocument, int reestr_type) throws DocumentException, IOException {
        HSSFSheet wsheet = Objects.requireNonNull(wb).getSheetAt(0);
        Iterator<Row> rowIterator = wsheet.iterator();
        Document xls_2_pdf = new Document(PageSize.A4.rotate());

        int lastIndex = wbname.lastIndexOf('.');
        String extension = wbname.substring(lastIndex);
        if (extension.equals(".csv")) {
            PdfWriter.getInstance(xls_2_pdf, new FileOutputStream(wbname.replace(".csv", ".pdf")));
        } else {
            PdfWriter.getInstance(xls_2_pdf, new FileOutputStream(wbname.replace(".xls", ".pdf")));
        }
        xls_2_pdf.open();

        float[] columnWidths;
        switch (reestr_type) {
            case (3):
                columnWidths = new float[]{(float) 1.5, 5, 4, 7, 5, 3, 4, 4, 4, 5, 0};
                break;
            case (1):
                columnWidths = new float[]{(float) 1.5, 5, 4, 7, 5, 3, 4, 4, 4, 5};
                break;
            default:
                columnWidths = new float[]{(float) 1.5, 5, 4, 7, 5, 3, 4, 4, 4, 5};
        }

        PdfPTable table = new PdfPTable(columnWidths);
        table.setWidthPercentage(90);
        BaseFont arial = BaseFont.createFont("ArialMT.ttf", BaseFont.IDENTITY_H, true);
        Font font = new Font(arial, 10, NORMAL, GrayColor.GRAYBLACK);

        PdfPCell header_cell = new PdfPCell(new Phrase("Реєстр\nлікарських засобів, " +
                "які надійшли до суб'єкта господарювання\n" + mainUI.organizationtext +"\n ", font));
        switch (reestr_type) {
            case (3):
                header_cell.setColspan(11);
                break;
            case (1):
                header_cell.setColspan(10);
                break;
            default:
                header_cell.setColspan(10);
        }
        header_cell.setVerticalAlignment(Element.ALIGN_CENTER);
        header_cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(header_cell);

        String[] columns = {"№ з/п", "Назва постачальника та номер ліцензії", "Номер та дата накладної",
                "Назва лікарського засобу та його лікарська форма, дата реєстрації та номер реєстраційного посвідчення",
                "Назва виробника", "Номер серії", "Номер і дата сертифіката якості виробника", "Кількість одержаних упаковок",
                "Термін придатності лікарського засобу", "Результат контролю уповноваженою особою"};
        switch (reestr_type) {
            case (3):
                for (int i=0; i<10; i++) {
                    PdfPCell column_cell = new PdfPCell(new Phrase(columns[i], font));
                    column_cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    if (i==9) {
                        column_cell.setColspan(2);
                    }
                    table.addCell(column_cell);
                }
                break;
            case (1):
                for (int i=0; i<10; i++) {
                    PdfPCell column_cell = new PdfPCell(new Phrase(columns[i], font));
                    column_cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    table.addCell(column_cell);
                }
                break;
            default:
                for (int i=0; i<10; i++) {
                    PdfPCell column_cell = new PdfPCell(new Phrase(columns[i], font));
                    column_cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    table.addCell(column_cell);
                }
        }

        PdfPCell table_cell;

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                table_cell = new PdfPCell(new Phrase(String.valueOf(cell.getRichStringCellValue()), font));
                table_cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(table_cell);
            }
        }

        PdfPCell footer_cell = new PdfPCell(new Phrase("\nРезультат вхідного контролю якості лікарських засобів " +
                "здійснив — уповноважена особа " + mainUI.personname + "\n" + dateOfDocument, font));
        switch (reestr_type) {
            case (3):
                footer_cell.setColspan(11);
                break;
            case (1):
                footer_cell.setColspan(10);
                break;
            default:
                footer_cell.setColspan(10);
        }
        footer_cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(footer_cell);

        Image image = Image.getInstance("sign.jpg");
        PdfPCell image_cell = new PdfPCell(image);
        switch (reestr_type) {
            case (3):
                image_cell.setColspan(11);
                break;
            case (1):
                image_cell.setColspan(10);
                break;
            default:
                image_cell.setColspan(10);
        }
        image_cell.setColspan(11);
        image_cell.setPaddingLeft((float)50);
        image_cell.setFixedHeight((float)100);
        image_cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(image_cell);

        xls_2_pdf.add(table);
        xls_2_pdf.close();
    }
}
