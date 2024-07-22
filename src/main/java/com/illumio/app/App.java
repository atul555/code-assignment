package com.illumio.app;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Code Assignment: Parse an input file and look for keys specified in a predefined file.
 * Inputs: A file with path for the predefined words to be counted in the input file.
 * An input file where to look for the frequency of those words.
 * Output: A file containing the keys and count of it's frequency in the input file.
 *
 */
public class App 
{
    Map<String, Integer> word_count;
    String output_file = "output.txt";
    private static final Logger logger =  Logger.getLogger(App.class.getName());
    public App() {
        word_count = new HashMap();
    }

    /**
     * Entrypoint. Processes the steps in sequence.
     * @param key_file : The predefined file with words to match
     * @param words_file : The input file to be parsed for word count.
     * @throws ApplicationException
     */
    public void process(String key_file, String words_file, String output) throws ApplicationException {
        word_count.clear();
        output_file = output;
        validate(key_file, words_file);
        loadKeys(key_file);
        loadAndParse(words_file);
        output();
    }

    /**
     * Validation for the file names
     * @param key_file : The predefined file with words to match
     * @param words_file : The input file to be parsed for word count.
     * @throws ApplicationException
     */
    private void validate(String key_file, String words_file) throws ApplicationException {
        if(key_file == null || key_file.equals("")) {
            logger.log(Level.SEVERE, "Filename : " + key_file +" not correct");
            throw new ApplicationException("Key file not correct");
        }
        if(words_file == null || words_file.equals("")) {
            logger.log(Level.SEVERE, "Filename : " + words_file +" not correct");
            throw new ApplicationException("Word file not correct");
        }
    }

    /**
     * Loads the words to be counted by frequency in the input file into a hashmap data structure.
     * @param key_file : The predefined file with words to match
     * @throws ApplicationException
     */
    private void loadKeys(String key_file) throws ApplicationException {
        try (Scanner file = new Scanner(new File(key_file))) {
            while (file.hasNextLine()) {
                String nextKey = file.nextLine();
                // convert to lowercase and put in the map.
                word_count.put(nextKey.toLowerCase(), 0);
            }
        } catch (FileNotFoundException e) {
            logger.log(Level.SEVERE, "Filename : " + key_file +" not found");
            throw new ApplicationException("Filename : " + key_file +" not found");
        }
    }

    /**
     * Loads the input file and counts the words by frequency.
     * @param words_file
     * @throws ApplicationException
     */
    private void loadAndParse(String words_file) throws ApplicationException {
        try (Scanner file = new Scanner(new File(words_file))) {
            while (file.hasNextLine()) {
                String nextLine = file.nextLine();
                parse(nextLine);
            }
        } catch (FileNotFoundException e) {
            logger.log(Level.SEVERE, "Filename : " + words_file +" not found");
            throw new ApplicationException("Filename : " + words_file +" not found");
        }
    }

    /**
     * Parses the line from the file to get the count of words in the word_count map.
     * @param line : line string read from the input file.
     */
    private void parse(String line) {
        String[] parts = line.split("\\W+");
        for(String key: parts) {
            key = key.toLowerCase();
            if(word_count.containsKey(key)) {
                logger.info("Key: "+key+" found in line: "+line);
                word_count.put(key, word_count.get(key)+1);
            }
        }
    }

    /**
     * Outputs the final count of the keys in an output file.
     * @throws ApplicationException
     */
    private void output() throws ApplicationException {
        // Write it out to output file:
        final String header = keyFormat("Predefined word")+"\t\tMatch count\n";
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(output_file));
            // Write the header
            writer.write(header);
            // using for-each loop for iteration over Map.entrySet()
            for (Map.Entry<String,Integer> entry : word_count.entrySet()) {
                logger.info("Key = " + entry.getKey() +
                        ", Value = " + entry.getValue());
                String line = keyFormat(entry.getKey())+"\t\t"+entry.getValue()+"\n";
                writer.write(line);
            }
            writer.close();
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            throw new ApplicationException(e.getMessage());
        }
    }
    private String keyFormat(String key) {
        int length = -50;
        return String.format("%1$"+length+ "s", key);
    }

    public static void main( String[] args ) {
        App app = new App();
        try {
            app.process("./predefined.txt", "./input.txt", "result.txt");
        } catch(Exception e) {
            logger.info("Exiting with error: "+e.getMessage());
        }
    }
}
