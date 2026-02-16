package uk.ac.sheffield.com1003.assignment2425.gui;

import uk.ac.sheffield.com1003.assignment2425.codeprovided.AbstractEntryCatalog;
import uk.ac.sheffield.com1003.assignment2425.codeprovided.Entry;
import uk.ac.sheffield.com1003.assignment2425.codeprovided.EntryProperty;
import uk.ac.sheffield.com1003.assignment2425.codeprovided.gui.AbstractHistogram;
import uk.ac.sheffield.com1003.assignment2425.codeprovided.gui.HistogramBin;

import java.util.*;

// This class provides some default code, but it needs to be completely replaced


public class Histogram extends AbstractHistogram {

    public Histogram(AbstractEntryCatalog catalog, List<Entry> filteredEntries, EntryProperty property) {
        super(catalog, filteredEntries, property);
        updateHistogramContents(property,filteredEntries);
    }

    /**
     * this changes the contents of the histogram
     *
     * @param property the property that the histogram needs to display the frequency of
     * @param filteredEntries the list of entry's that the histogram should display
     */
    public void updateHistogramContents(EntryProperty property, List<Entry> filteredEntries) {
        this.entryList = filteredEntries;
        this.property = property;
        this.minPropertyValue = getMinPropertyValue();
        this.maxPropertyValue = getMaxPropertyValue();
        this.entryCountsPerBin = countIntoBins();

    }

    /**
     *
     * @return the average value of the current property in the entryList
     * @throws NoSuchElementException if there are no values in the entry
     * list then an exception is thrown
     */
    public double getAveragePropertyValue() throws NoSuchElementException {
        return catalog.getAverageValue(property, entryList);
    }


    /**
     *
     * @return maximum value of the current property in the entryList
     */
    public double getMaxPropertyValue() {
        return catalog.getMaximumValue(property, entryList);
    }

    /**
     *
     * @return minimum value of the current property in the entryList
     */
    public double getMinPropertyValue() {
        return catalog.getMinimumValue(property, entryList);
    }


    /**
     * counts the values of the entryList into their individual bins
     *
     * @return a TreeMap with Histogram bins as the keys and the number
     * of entry's in each bin as the value
     */
    public TreeMap<HistogramBin,Integer> countIntoBins() {
        TreeMap<HistogramBin, Integer> tree = new TreeMap<>();
        List<HistogramBin> bins = getBinsInBoundaryOrder();
        for (Entry entry : entryList){
            //increments the bin containing the entry
            for (HistogramBin bin : bins){
                if(bin.valueInBin(entry.getEntryProperty(property))){
                    if(tree.get(bin)==null){
                        tree.put(bin,1);
                    }else{
                        tree.put(bin,tree.get(bin)+1);
                    }
                }
            }
        }
        return tree;
    }


    /**
     * creates histogram bins covering the range of entryList
     *
     * @return a list of histogram bins
     */
    public List<HistogramBin> getBinsInBoundaryOrder() {
        //creates and returns a list of histogram bins covering the range of values
        ArrayList<HistogramBin> bins = new ArrayList<>();
        //the range that one bin should cover
        double range = (maxPropertyValue - minPropertyValue) / NUMBER_BINS;
        for (int i = 0; i < NUMBER_BINS; i++) {
            bins.add(new HistogramBin(
                    (range * i)+minPropertyValue,
                    (range * (i + 1))+minPropertyValue,
                    i == (NUMBER_BINS - 1)));

        }
        return bins;
    }
}
