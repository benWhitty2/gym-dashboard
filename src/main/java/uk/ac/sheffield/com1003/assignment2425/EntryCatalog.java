package uk.ac.sheffield.com1003.assignment2425;

import uk.ac.sheffield.com1003.assignment2425.codeprovided.*;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

// This class provides some default code, but it needs to be completely replaced


public class EntryCatalog extends AbstractEntryCatalog {

    public EntryCatalog(String entryFile)
            throws IllegalArgumentException, IOException {
        super(entryFile);
    }

    /**
     *
     * takes the information for an entry and constructs its entry property map
     *
     * @param line all the information for one entry
     * @return a populated entry property map
     * @throws IllegalArgumentException if the line does not contain the correct
     * number of elements
     * @throws NumberFormatException if there is a error when inserting a value
     * into the entry property map
     *
     *
     */
    public EntryPropertyMap parseEntryLine(String line) throws IllegalArgumentException {
        EntryPropertyMap entryPropertyMap = new EntryPropertyMap();
        String[] properties = line.split(",");

        if (properties.length != 15) {
            throw new IllegalArgumentException();
        }

        int pCount = 0;
        int ePCount = 0;
        boolean eD = true;
        while(pCount < properties.length) {
            try{
            if(pCount != 1 && pCount != 9){//checks if a property or detail needs to be added
                entryPropertyMap.putProperty(EntryProperty.values()[ePCount],Double.parseDouble(properties[pCount]));
                pCount++;
                ePCount++;
            }else{
                if (eD){
                    entryPropertyMap.putDetail(EntryDetail.GENDER,properties[pCount]);
                    eD = false;
                }else{
                    entryPropertyMap.putDetail(EntryDetail.WORKOUT_TYPE,properties[pCount]);
                }
                pCount++;
            }
            }catch(Exception e){
                throw new NumberFormatException();
            }
        }
        return entryPropertyMap;
    }

    /**
     * filters the list given to it down to entry's with a matching entry detail
     *
     * @param filteredEntriesList the list of entry's to be filtered
     * @param entryDetail the entry detail we want to filter for(WORKOUT-TYPE or GENDER)
     * @param value the value that the entry detail should match to
     * @return the filtered list of entry's
     */
    public List<Entry> getEntriesListByEntryDetail(List<Entry> filteredEntriesList,
                                                   EntryDetail entryDetail, String value) {
        List<Entry> list = new ArrayList<>();
        //add matching entry's to new list
        for (Entry entry : filteredEntriesList) {
            if (entry.getEntryDetail(entryDetail).equals(value)) {
                list.add(entry);
            }
        }
        return list;
    }

    /**
     * gets the minimum value of an entry property in a list of entry's
     *
     * @param entryProperty the property to find the minimum of
     * @param entriesList the list to be searched for the minimum
     * @return the minimum value
     * @throws NoSuchElementException if the list is empty or all values
     * are the larges possible value an error will be thrown
     */
    public double getMinimumValue(EntryProperty entryProperty, List<Entry> entriesList)
            throws NoSuchElementException {
        double min = Double.MAX_VALUE;
        for (Entry entry : entriesList) {
            if(entry.getEntryProperty(entryProperty) < min){
                min = entry.getEntryProperty(entryProperty);
            }
        }
        //if there are no values or all values are of maximum possible size then error is thrown
        if (min == Double.MAX_VALUE) {
            throw new NoSuchElementException();
        }else{
            return min;
        }
    }

    /**
     * gets the maximum value of an entry property in a list of entry's
     *
     * @param entryProperty the property to find the maximum of
     * @param entriesList the list to be searched for the maximum
     * @return the maximum value
     * @throws NoSuchElementException if the list is empty or all values
     * are the smallest possible value an error will be thrown
     */
    public double getMaximumValue(EntryProperty entryProperty, List<Entry> entriesList)
            throws NoSuchElementException {
        double max = Double.MIN_VALUE;
        for (Entry entry : entriesList) {
            if(entry.getEntryProperty(entryProperty) > max){
                max = entry.getEntryProperty(entryProperty);
            }
        }
        //if there are no values or all values are of minimum possible size then error is thrown
        if (max == Double.MIN_VALUE) {
            throw new NoSuchElementException();
        }else{
            return max;
        }
    }

    /**
     * gets the average value in of an entry property in a list of entry's
     *
     * @param entryProperty the property to find the average of
     * @param entriesList the indexes to have their average calculated
     * @return the average value of the entry property's
     * @throws NoSuchElementException if the there are no entry property's
     * an error is thrown
     */
    public double getAverageValue(EntryProperty entryProperty, List<Entry> entriesList)
            throws NoSuchElementException {

        if (entriesList.isEmpty()) {
            throw new NoSuchElementException();
        }
        double average = 0;
        for (Entry entry : entriesList) {
            average += entry.getEntryProperty(entryProperty);
        }
        average = average / entriesList.size();
        return average;
    }

    /**
     *
     * @return the first five entries in a list
     */
    public List<Entry> getFirstFiveEntries() {
        ArrayList<Entry> firstFive = new ArrayList<>();
        Iterator<Entry> iterator = entriesList.iterator();
        for(int i = 0; i < 5; i++) {
            firstFive.add(iterator.next());
        }
        return firstFive;
    }


    /**
     * round a decimal to two decimal places
     *
     * @param number number to be rounded
     * @return rounded number
     */
    public static double roundTo2DP(double number) {
        return Math.round(number*100.0)/100.0;
    }

    /**
     * returns all entry's with a property more than the value specified
     *
     * @param entryProperty property to be compared to value
     * @param entriesList list of entry's to be filtered
     * @param value value for property to checked against
     * @return filtered list of entry's
     */
    public static List<Entry> getByPropertyMoreThan(EntryProperty entryProperty, List<Entry> entriesList, double value){
        ArrayList<Entry> filteredList = new ArrayList<>();
        for(Entry entry : entriesList) {
            if(entry.getEntryProperty(entryProperty) > value) {
                filteredList.add(entry);
            }
        }
        return filteredList;
    }
}
