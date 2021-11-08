package com.demo.prac;

import com.opencsv.CSVParser;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.*;
import java.util.*;

public class MyAssignMain {
    public static void main(String[] args) {
        /* Based on instructions, we don't want to read the file line by line,
        Instead we want to read the csv file into array in java  */

        System.out.println("Assignment cp2406");

        /* Map is just dictionary, Map would map the name of the column into the row.- hunch
        We tried searching for "how to import source from open csv into Java Project",
        we find the JAR, we copy it, and we add it as a library in our project. and that
        is the easiest way to do it. "opencsv-3.3 - Google Search"*/

        /* 1. googled read csv in java
        2. found opencsv - library
        3. Credible source - Apache - Famous product that they have is webserver,
        HTML Webservers
         4. downloaded the opencsv-4.1jar from
        "https://jar-download.com/download-handling.php" as this website has bundled
         all the jars' together.
         5. Note : The proper way to do it is Maven. Maven is a
         Package Management Tool. But did this instead for learning.*/


        Reader reader = null;
        try {
//            reader = new FileReader("MountSheridanStationCNS.csv");
            // commented line 36 to change root directory in line 38
            reader = new FileReader(
                    "./src/MountSheridanStationCNS.csv"); // changed it to ./src
            // based the help from StackOverflow, search
            // " how to reference file path from intelliJ code"
            // .(dot) refers to the root directory
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
//        CSVReader csv = new CSVReader(reader); // ran it and the program crashed. We dropped
        // the JAR files into the directory and Java doesn't know where it is.
        // Error we got is "File not Found, next step is, " how to reference a file within Java".

        /* The next commented block was initially written, commented out after we found
        the http://opencsv.sourceforge.net/apidocs/index.html to post the API*/

//        CSVReader csv = new CSVReaderHeaderAware(reader); Available FROM version 4.2 only,
//        Since we downloaded 4.1 Jar files;we didn't have the CSVReaderHeaderAware class.
//        Here we decided to download the Header manually.

        CSVReader csv = new CSVReader(reader);
        List<String[]> data = null;
        try {
            data = csv.readAll();
            System.out.println("data.size =" + data.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
//        try {
//            Map<String, String> values = new CSVReaderHeaderAware( // this "CSVReaderHeaderAware had an error
//                    new FileReader("MountSheridanStationCNS.csv")).readMap();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }

/* Note: Explanation for code - line 78 to 84 - What we done- we know the data is
"list of strings array- List<String[]> data = null", therefore, we removed the first one
as it's a 'Header', as we couldn't use the "CSVReaderHeaderAware" as it is available
for version 4.2 onwards, we downloaded jars for  4.1, so we took out the header, and when
we iterate through the array, we know what we should be getting is a primitive array
 of strings, as that's what the data is.*/

//        Object next = null;
//        String next = null; didn't put the array brackets and got error in line 79
        String[] next = null; // put the square brackets in line 75, error gone
        String[] header = data.remove(0);
        System.out.println("header" + header[0]);
        System.out.println(Arrays.toString(header));

        System.out.println("----------------PART B----------------------------------");

//Question - Part B : Calculate monthly rainfall totals along with
// minimum daily and maximum daily rainfall during
//each month, across the 10+ years’ worth of data.

        int MONTH_IDX = 3; // 3rd index is the month
        int DAY_IDX = 3;
        int RAIN_IDX = 5;
//      Index  Line 90 to line 97 is what we have inside the data.
    //   0     Product code,
    //   1    Bureau of Meteorology station number,
    //   3     Year,
    //   4     Month,
    //   5     Day,
    //   6     Rainfall amount (millimetres),
    //   7     Period over which rainfall was measured (days),
    //   8     Quality]

//  Hashtable<String, String> my_dict -from "https://www.educative.io/edpresso/how-to-create-a-dictionary-in-java"
// mapping- what to what- we are mapping string(months) to a value total(double), changed in line 107
        Hashtable<String, Double> monthStats = new Hashtable<>(); // New improvements in java, specify the first types
        // and the second type is already taken from the beginning.
        Hashtable<String, Double> minDayStats = new Hashtable<>(); // minimum daily
        Hashtable<String, Double> maxDayStats = new Hashtable<>();// maximum daily

        for (Iterator iterator = data.iterator(); iterator.hasNext(); ) {
            next = (String[])iterator.next();
//           System.out.println(next[0].toString()); //commented out this to get just the first line.
 // Util class arrays has a helper function that prints arrays.
            String rainStr = next[RAIN_IDX];
            String monthStr = next[MONTH_IDX];
            String dayStr = next[DAY_IDX];
            System.out.println(Arrays.toString(next));
            System.out.println("day = " + dayStr);
            System.out.println("month=" + monthStr); // not converting to number as we don't need to add it as a number
            System.out.println("rain=" + rainStr); //

            // now converting rain to number
            double rain = 0; // Here the try catch means - the rain is zero, if it is not passed

            try { // it stays zero
                rain = Double.parseDouble(rainStr);
            } catch (Exception e) { // Here the exception is not IOException, instead it's Exception
            }

//            double rain = Double.parseDouble(rainStr); // parseDouble coz we are getting a string
            System.out.println("rain = " + rain);
            // at this stage we ran the program, we got an error,
            // "Exception in thread "main" java.lang.NumberFormatException: empty String"
            if (monthStats.contains(monthStr)) {
                double currTotal = monthStats.get(monthStr);
                monthStats.put(monthStr,currTotal + rain);
            } else {
                monthStats.put(monthStr,rain);
            }

            String monthDayStr = monthStr + "-day=" + dayStr; // Keeping fancy keys to not get confused between
            if (minDayStats.containsKey(monthDayStr)) { // two dictionaries
                double currMin = minDayStats.get(monthDayStr);
                minDayStats.put(monthDayStr,Math.min(currMin, rain));
            } else {
                minDayStats.put(monthDayStr,rain);
            }

            if (maxDayStats.containsKey(monthDayStr)) { // two dictionaries
                double currMax = maxDayStats.get(monthDayStr);
                maxDayStats.put(monthDayStr,Math.max(currMax, rain));
            } else {
                maxDayStats.put(monthDayStr,rain);
            }
        }
//        Below two lines are incorrect implementation, changed it later.
//        for (Iterator<String[]> iterator = monthStats.iterator(); iterator.hasNext(); ) {
//            String[] strings =  iterator.next();
        System.out.println(monthStats.toString());
        System.out.println(minDayStats.toString());
        System.out.println(maxDayStats.toString());

        String outPath = "./src/MyMonthMap.csv";
        header = new String[]{"Month", "Total Rain"}; // Adding new String[],
//        if not used with initialization, it will throw an error.
        myWriteToCSV(monthStats, outPath, header);

        outPath = "./src/MyMinDay.csv";// This will add another.csv file- MyMinDay.csv
        header = new String[]{"Month-day", "Min Rain"};
        myWriteToCSV(monthStats, outPath, header);// will show min rain - month & day

        outPath = "./src/MyMaxDay.csv";// this will add another .csv file - MyMaxDay.csv
        header = new String[]{"Month-day", "Max Rain"};// will show max rain - month & day
        myWriteToCSV(monthStats, outPath, header);

    }

    private static void myWriteToCSV(Hashtable<String, Double> map,
                                     String outPath,
                                     String[] header) {
        CSVWriter csv = null;
        try {
            FileWriter writer = new FileWriter(outPath);
            csv = new CSVWriter(writer);
//            String[] header = {"Month", "Total Rain"};
            csv.writeNext(header);

            for (Map.Entry<String, Double> entry: map.entrySet()) { // this for loop will iterate through each element
                // in this map.
                String key = entry.getKey(); // get the key from the map
                Double v = entry.getValue(); // get the value from the map
                String[] row = {key,v.toString()}; // it needs to have an array of string to write
                System.out.println(Arrays.toString(row));
                csv.writeNext(row);

                //At this state, ran the program, it created the file "MyMonthMap.csv",
                //but the file was empty. May be because we didn't "writer.close();
            }
            csv.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
        }
    }
}
