package com.example.dictionaryApplication;

import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

public class DictionaryGUIManagement {

    public static void insertDictGUIDataFromFile() throws IOException {
        try {
            //DictionaryGUI.dict.set(0,new Word("","",""));
            File file2 = new File("src/main/resources/anhviet109K.txt");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file2)));
            StringBuilder target = new StringBuilder().append("TU DIEN BY Q & N");
            StringBuilder res = new StringBuilder().append("20020224 Pham Viet Quang\n20020303 Chu Dang Nghia\nDictionary sources :  https://github.com/yenthanh132/avdict-database-sqlite-converter/blob/master/anhviet109K.txt");
            String spelling = "Hazel Team";
            int targetEndIndex = 0;
            while (bufferedReader.ready()) {
                String line = bufferedReader.readLine();
                if(!line.isEmpty()) { // Neu ko co se bi loi index out of bounds
                    if (line.charAt(0) == '@') {
                        Word word = new Word(target.toString().trim(),spelling,res.toString());
                        DictionaryGUI.dict.add(word);
                        res = new StringBuilder();
                        target = new StringBuilder();
                        spelling = "";
                        targetEndIndex = line.length();

                        for (int i = 1; i < line.length(); i++) {
                            if (line.charAt(i) != '/') {
                                target.append(line.charAt(i));
                            } else {
                                targetEndIndex = i;
                                break;
                            }
                        }
                        spelling = line.substring(targetEndIndex);
                        //System.out.println(spelling);
                    } else {
                        res.append(line);
                        res.append(System.getProperty("line.separator")); // them dong moi
                    }

                }
            }
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Please check file directory");
        }
    }

    public static void exportDictGUIDataToFile() throws IOException{
        File outfile = new File("src/main/resources/anhviet109K.txt");
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outfile)));
        ArrayList<Word> updatedDict = DictionaryGUI.dict;
        for (int i = 1; i < DictionaryGUI.dict.size(); i++) {
            bufferedWriter.write("@" + DictionaryGUI.dict.get(i).getWord_target());
            bufferedWriter.write(" " + DictionaryGUI.dict.get(i).getWord_spelling());
            bufferedWriter.newLine();
            bufferedWriter.write(DictionaryGUI.dict.get(i).getWord_explain());
            bufferedWriter.newLine();
        }
        bufferedWriter.close();
    }

    /**
     * So sanh 2 xau theo thu tu bang chu cai
     * @param string1 xau 1
     * @param string2 xau 2
     * @return -1 neu xau 1 < xau 2; 0 neu xau 1 = xau 2; 1 neu xau 1 > xau 2
     */
    private static int compare2String(String string1, String string2) {
        int index = 0;
        while (index < string1.length() && index < string2.length()) {

            if (string1.charAt(index) > string2.charAt(index)) {
                return 1;
            }
            if (string1.charAt(index) < string2.charAt(index)) {
                return -1;
            }
            index++;
        }
        return Integer.compare(string1.length(), string2.length());
    }

    /**
     * Add a word to dict arraylist
     * @param wordTarget wordTarget
     * @param wordSpelling wordSpelling
     * @param wordDef wordDef
     * @return true if success, false if fail
     */
    public static boolean addWord(String wordTarget, String wordSpelling, String wordDef) {
        Word word = new Word(wordTarget,wordSpelling,wordDef);
        for (int i = 1; i < DictionaryGUI.dict.size()-1; i++) {
            if (compare2String(DictionaryGUI.dict.get(i).getWord_target(),wordTarget) == 0) {
                return false;
            }

            if (compare2String(DictionaryGUI.dict.get(i).getWord_target(),wordTarget) < 0 && compare2String(DictionaryGUI.dict.get(i+1).getWord_target(),wordTarget) > 0) {
                DictionaryGUI.dict.add(i+1,word);
                return true;
            }
        }
        DictionaryGUI.dict.add(word);
        return true;
    }

    public static void removeWord(int index) {
        DictionaryGUI.dict.remove(index);
    }

    public static boolean editWord(Word word) {
        for (int i = 0; i < DictionaryGUI.dict.size(); i++) {
            if (Objects.equals(DictionaryGUI.dict.get(i).getWord_target(), word.getWord_target())) {
                DictionaryGUI.dict.set(i,word);
                return true;
            }
        }
        return false;
    }

    public static int DictionaryGUILookup(String lookUpWord) {
        for (int i = 1; i < DictionaryGUI.dict.size(); i++) {
            if (DictionaryGUI.dict.get(i).getWord_target().equals(lookUpWord)) {
                return i;
            }
        }
        return 0;
    }



}