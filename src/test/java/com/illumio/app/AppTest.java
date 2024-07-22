package com.illumio.app;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for simple App.
 */
public class AppTest
{

    static App app;

    static boolean CLEAN_OUTPUT = true;

    @BeforeAll
    public static void initalize() {
        app = new App();
    }

    @Test
    public void testApp()
    {
        assertTrue( true );
    }
    @Test
    public void testNoName() {
        ApplicationException exception = assertThrows(ApplicationException.class, () ->
                app.process("", "inp.txt", "testNoName.txt"));

        assertEquals("Key file not correct", exception.getMessage());

        exception = assertThrows(ApplicationException.class, () ->
                app.process("word.txt", "", "testNoName.txt"));

        assertEquals("Word file not correct", exception.getMessage());
    }
    @Test
    public void testNoFiles() {
        ApplicationException exception = assertThrows(ApplicationException.class, () ->
                app.process("doesnotexist.txt", "inp.txt", "testNoFiles.txt"));

        assertEquals("Filename : doesnotexist.txt not found", exception.getMessage());

        String test_pred = getFqdn("test_predefined.txt");
        exception = assertThrows(ApplicationException.class, () ->
                app.process(test_pred, "doesnotexist.txt", "testNoFiles.txt"));

        assertEquals("Filename : doesnotexist.txt not found", exception.getMessage());
    }

    private String getFqdn(String fileName) {
        return Thread.currentThread().getContextClassLoader().getResource(fileName).getFile();
    }

    @Test
    public void processFiles() {
        try {
            String pred_file = getFqdn("test_predefined.txt");
            String input_file = getFqdn("input1.txt");
            String output_file = "test_output_processFiles.txt";

            app.process(pred_file, input_file, output_file);
            Path path = Paths.get(output_file);
            assertTrue(Files.exists(path));

            // Check contents of the file
            try (Scanner file = new Scanner(new File(output_file))) {
                // Discard the header line
                String nextLine = "";
                if (file.hasNextLine()) {
                    nextLine = file.nextLine();
                } else fail("No header line");
                // 2nd line
                String line = keyFormat("name")+"\t\t2";
                if (file.hasNextLine()) {
                    nextLine = file.nextLine();
                    assertEquals(line, nextLine);
                } else fail("No 1st line");
                // 3rd line
                line = keyFormat("ai")+"\t\t1";
                if (file.hasNextLine()) {
                    nextLine = file.nextLine();
                    assertEquals(line, nextLine);
                } else fail("No 2nd line");
                // 4th line
                line = keyFormat("detect")+"\t\t0";
                if (file.hasNextLine()) {
                    nextLine = file.nextLine();
                    assertEquals(line, nextLine);
                } else fail("No 3rd line");
            } catch (FileNotFoundException e) {
                fail("File not found");
            }
        } catch (ApplicationException ae) {
            fail(ae.getMessage());
        }
    }

    @Test
    public void processFilesSet2() {
        try {
            String pred_file = getFqdn("test_predefined.txt");
            String input_file = getFqdn("input2.txt");
            String output_file = "test_output_processFilesSet2.txt";

            app.process(pred_file, input_file, output_file);
            Path path = Paths.get(output_file);
            assertTrue(Files.exists(path));
            // Check contents of the file
            try (Scanner file = new Scanner(new File(output_file))) {
                // Discard the header line
                String nextLine = "";
                if (file.hasNextLine()) {
                    nextLine = file.nextLine();
                } else fail("No header line");
                // 2nd line
                String line = keyFormat("name")+"\t\t2";
                if (file.hasNextLine()) {
                    nextLine = file.nextLine();
                    assertEquals(line, nextLine);
                } else fail("No 1st line");
                // 3rd line
                line = keyFormat("ai")+"\t\t5";
                if (file.hasNextLine()) {
                    nextLine = file.nextLine();
                    assertEquals(line, nextLine);
                } else fail("No 2nd line");
                // 4th line
                line = keyFormat("detect")+"\t\t1";
                if (file.hasNextLine()) {
                    nextLine = file.nextLine();
                    assertEquals(line, nextLine);
                } else fail("No 3rd line");
            } catch (FileNotFoundException e) {
                fail("File not found");
            }
        } catch (ApplicationException ae) {
            fail(ae.getMessage());
        }
    }

    @AfterAll
    public static void cleanUp(){
        if(CLEAN_OUTPUT) {
            final File outputDirectory = new File(".");
            final File[] files = outputDirectory.listFiles((dir, name) -> name.matches("test_output_.*?"));
            Arrays.asList(files).stream().forEach(File::delete);
        }
        System.out.println("After All cleanUp() method called");
    }

    private String keyFormat(String key) {
        int length = -50;
        return String.format("%1$"+length+ "s", key);
    }
}
