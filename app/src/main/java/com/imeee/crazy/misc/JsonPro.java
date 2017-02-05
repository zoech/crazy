/**
 * not used any more, we use the ResHandle to handle the response json instead.
 *
 */




package com.imeee.crazy.misc;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/1/29.
 */

public class JsonPro {

    public static String proJson(String str){

        //  all null;
        String egx = emptyMatExg();
        // two array
        egx = egx + "|" + arrMatExg("rtUrls") +
                "|" + arrMatExg("ar") +
                "|" + arrMatExg("alia");
        // useless obj
        egx = egx + "|" + objMatExg("al") +
                "|" + objMatExg("l") +
                "|" + objMatExg("m") +
                "|" + objMatExg("h") +
                "|" + objMatExg("privilege") +
                "|" + objMatExg("a") +
                "|" + objMatExg("crbt") +
                "|" + objMatExg("rtUrl") +
                "|" + objMatExg("rurl") +
                "|" + objMatExg("pc");
        // two alpha
        egx = egx + "|" + twoAlMatExg();

        // count not match
        egx = egx + "|" + cntAlMatExg(1) +
                "|" + cntAlMatExg(3) ;//+
        //"|" + cntAlMatExg(5);

        egx = egx + "|" + atrMatExg("dt") +
                "|" + atrMatExg("cd") +
                "|" + atrMatExg("djId") +
                "|" + atrMatExg("crbt") +
                "|" + atrMatExg("ftype") +
                "|" + atrMatExg("rtype");

        Pattern pat = Pattern.compile(egx);
        Matcher mat = pat.matcher(str);
        String result = mat.replaceAll("");

        mat = pat.matcher(result);
        result = mat.replaceAll("");

                /*
                Pattern pat2 = Pattern.compile(twoAlMatExg());
                Matcher mat2 = pat2.matcher(result);
                result = mat2.replaceAll("");
                */

        return result;
    }

    public static String arrMatExg(String name){
        final String postNotEnd = "(?=\"[^,]*?\")";
        String arrEgxN = "\"" + name + "\"\\s*:\\s*\\[[^\\]]*?\\]\\s*," + postNotEnd;
        String arrEgxE = ",\\s*\"" + name + "\"\\s*:\\s*\\[[^\\]]*?\\]\\s*(?=\\})";
        return arrEgxN + "|" + arrEgxE;
    }

    public static String objMatExg(String name){
        final String postNotEnd = "(?=\"[^,]*?\")";
        String objEgxN = "\"" + name + "\"\\s*:\\s*\\{[^\\}]*?\\}\\s*," + postNotEnd;
        String objEgxE = ",\\s*\"" + name + "\"\\s*:\\s*\\{[^\\}]*?\\}\\s*(?=\\})";
        return objEgxN + "|" + objEgxE;
    }

    public static String emptyMatExg(){
        return nullMatExg() + "|" + empArrMatExg() + "|" + empStrMatExg();
    }

    public static String empArrMatExg(){
        return "\"\\w{1,}\"\\s*:\\s*\\[\\s*\\]\\s*," + "|" +
                ",\\s*\"\\w{1,}\"\\s*:\\s*\\[\\s*\\]\\s*(?=[\\]\\}])";
    }

    public static String empStrMatExg(){
        return "\"\\w{1,}\"\\s*:\\s*\"\\s*\"\\s*," +  "|" +
                ",\\s*\"\\w{1,}\"\\s*:\\s*\"\\s*\"\\s*(?=[\\]\\}])";
    }

    public static String nullMatExg(){
        return "\"\\w{1,}\"\\s*:\\s*null\\s*," + "|" +
                ",\\s*\"\\w{1,}\"\\s*:\\s*null\\s*(?=[\\]\\}])";
    }

    // delete two letter's attribute except "id", number or string attribute
    public static String twoAlMatExg(){
        return "\"[A-Za-ce-hj-z0-9_]{2}\"\\s*:\\s*(" + egxDigit +
                "|" + egxString +")\\s*,"

                + "|" +

                ",\\s*\"[A-Za-ce-hj-z0-9_]{2}\"\\s*:\\s*(" + egxDigit +
                "|" + egxString +")\\s*(?=[\\]\\}])";
    }

    // delete number or string attribute by count of letter of attibute's name
    public static String cntAlMatExg(int cnt){
        return "\"\\w{" + cnt + "}\"\\s*:\\s*(" + egxDigit +
                "|" + egxString +")\\s*,"

                + "|" +

                ",\\s*\"\\w{" + cnt + "}\"\\s*:\\s*(" + egxDigit +
                "|" + egxString +")\\s*(?=[\\]\\}])";
    }

    // delete number or string attribute by attribute's name
    public static String atrMatExg(String atr){
        return "\"" + atr + "\"\\s*:\\s*(" + egxDigit +
                "|" + egxString +")\\s*,"

                + "|" +

                ",\\s*\"" + atr + "\"\\s*:\\s*(" + egxDigit +
                "|" + egxString +")\\s*(?=[\\]\\}])";
    }


    // number
    public static String egxDigit = "[+-]?\\d*(\\.\\d*)?";

    // string, currently too simple but seems ok, the "\"" should not appear in the sting
    public static String egxString = "\"[^\"]*\"";




    public static String testMatExg(){
        return "\"[^\"]{" + 2 + "}\"\\s*:\\s*\"[^\"]*\"\\s*";
    }
}


