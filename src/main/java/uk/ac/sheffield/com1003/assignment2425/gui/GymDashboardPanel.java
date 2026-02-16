package uk.ac.sheffield.com1003.assignment2425.gui;

import uk.ac.sheffield.com1003.assignment2425.EntryCatalog;
import uk.ac.sheffield.com1003.assignment2425.codeprovided.*;
import uk.ac.sheffield.com1003.assignment2425.codeprovided.gui.AbstractGymDashboardPanel;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static java.lang.Math.round;

// This class provides some default code, but it needs to be completely replaced


public class GymDashboardPanel extends AbstractGymDashboardPanel {

    /**
     * will store listeners for button object
     */
    private final Listener listener;
    /**
     * will store listeners for slider object
     */
    private final ChangeListener changeListener;

    public GymDashboardPanel(AbstractEntryCatalog entryCatalog) {
        super(entryCatalog);
        listener = new Listener(this);
        changeListener = new ChangeListener();
        addListeners();
        executeQuery();

    }

    /**
     * assembles and executes a query to get the new filteredEntriesList
     * <p>
     * this is where the values of the range sliders are used
     * to filter the entryCatalog as well as any other sub Queries that the
     * user has entered
     * this method will call the histogram to be redrawn and repainted
     * <p/>
     */
    public void executeQuery() {
        ArrayList<SubQuery> subQueries = new ArrayList<>();
        subQueries.add(new SubQuery(EntryProperty.AGE,">=",ageRangeSlider.getLowerValue()));
        subQueries.add(new SubQuery(EntryProperty.AGE,"<=",ageRangeSlider.getUpperValue()));
        subQueries.add(new SubQuery(EntryProperty.HEIGHT,">=",heightRangeSlider.getLowerValue()));
        subQueries.add(new SubQuery(EntryProperty.HEIGHT,"<=",heightRangeSlider.getUpperValue()));
        subQueries.add(new SubQuery(EntryProperty.WEIGHT,">=",weightRangeSlider.getLowerValue()));
        subQueries.add(new SubQuery(EntryProperty.WEIGHT,"<=",weightRangeSlider.getUpperValue()));
        subQueries.addAll(subQueryList);
        Query myQuery = new Query(subQueries);
        filteredEntriesList = myQuery.executeQuery(entryCatalog);
        updateStatistics();
        histogram.updateHistogramContents(
                EntryProperty.valueOf(comboBoxPropertyNames.getSelectedItem().toString().toUpperCase()),
                filteredEntriesList);
        this.repaint();
    }

    /**
     * clears the list of custom filters the use has placed on the entryCatalog
     */
    public void clearFilters() {
        subQueryList.clear();
        updateSubQueryTextArea();
        executeQuery();
    }

    /**
     * adds the filter the user enters
     *
     * <p>
     * if the filter entered by the user has not yet been added into the
     * list of filters and is a valid filter it will be added to the list
     * </p>
     */
    public void addFilter() {
        if(!value.getText().isEmpty()) {
            SubQuery subQuery = new SubQuery(
                    EntryProperty.fromPropertyName((String) comboQueryProperties.getSelectedItem()),//property
                    (String) comboOperators.getSelectedItem(),//operator
                    Double.parseDouble((String) value.getText()));//value
            if (!subQueryList.contains(subQuery)) {
                subQueryList.add(subQuery);
                updateSubQueryTextArea();
                executeQuery();
            }
        }
    }

    /**
     * determines the property values that the histogram can display the frequency of
     */
    public void populateComboBoxes() {
        comboBoxPropertyNames.addItem("Age");
        comboBoxPropertyNames.addItem("Height");
        comboBoxPropertyNames.addItem("Weight");
    }

    /**
     * outlines the buttons and sliders that need to react when a user clicks on them
     */
    public void addListeners() {
        buttonAddFilter.addActionListener(listener);
        buttonClearFilters.addActionListener(listener);
        comboBoxPropertyNames.addActionListener(listener);
        ageRangeSlider.addChangeListener(changeListener);
        heightRangeSlider.addChangeListener(changeListener);
        weightRangeSlider.addChangeListener(changeListener);
    }

    /**
     * overrides the default method in super class
     *
     * @param g the <code>Graphics</code> object to protect
     */
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    /**
     * updates the text displayed in the statistics text box at the bottom of the page
     */
    public void updateStatistics() {
        StringBuilder names = new StringBuilder();
        StringBuilder minValues = new StringBuilder("Minimum");
        StringBuilder maxValues = new StringBuilder("Maximum");
        StringBuilder avgValues = new StringBuilder("Average");
        for (EntryProperty property : EntryProperty.values()) {
            names.append("-").append(property.getName());
            minValues.append("-").append(getMinimumValue(property,filteredEntriesList));
            maxValues.append("-").append(getMaximumValue(property,filteredEntriesList));
            avgValues.append("-").append(getAverageValue(property,filteredEntriesList));

        }
        statisticsTextArea.setText(names.toString() + "\n" +
                minValues.toString() + "\n" +
                maxValues.toString() + "\n" +
                avgValues.toString());
    }

    /**
     * returns the minimum value of a property in a list of entry's
     *
     * @param entryProperty the property to find the minimum of
     * @param filteredEntriesList the entry's to find the minimum in
     * @return the minimum value if no minimum value returns -1
     */
    public double getMinimumValue(EntryProperty entryProperty, List<Entry> filteredEntriesList) {
        try{
            return EntryCatalog.roundTo2DP(entryCatalog.getMinimumValue(entryProperty, filteredEntriesList));
        } catch (NoSuchElementException e) {
            return -1;
        }
    }

    /**
     * returns the maximum value of a property in a list of entry's
     *
     * @param entryProperty the property to find the maximum of
     * @param filteredEntriesList the entry's to find the maximum in
     * @return the maximum value if no maximum value returns -1
     */
    public double getMaximumValue(EntryProperty entryProperty, List<Entry> filteredEntriesList) {
        try{//rounds to 2 dp
            return EntryCatalog.roundTo2DP(entryCatalog.getMaximumValue(entryProperty, filteredEntriesList));
        } catch (NoSuchElementException e) {
            return -1;
        }
    }

    /**
     * get the average value of a property in a list of entry's
     *
     * @param entryProperty the property to find the average of
     * @param filteredEntriesList the list of entry's to find the average of
     * @return the average value in the list if there is no average -1 is returned
     */
    public double getAverageValue(EntryProperty entryProperty, List<Entry> filteredEntriesList) {
        try{
            return EntryCatalog.roundTo2DP(entryCatalog.getAverageValue(entryProperty, filteredEntriesList));
        }catch (NoSuchElementException e) {
            return -1;
        }
    }

    /**
     * updates the text area that displays the current filters applied to the entryCatalog
     */
    public void updateSubQueryTextArea(){
        StringBuilder text = new StringBuilder();
        for(SubQuery subQuery : subQueryList){
            text.append(subQuery.getEntryProperty()).append(" ").append(subQuery.getOperator()).
                    append(" ").append(subQuery.getValue()).append("; ");
        }

        subQueriesTextArea.setText(text.toString());
    }

    /**
     * a class that is used to listen for actionEvent's from the buttons
     */
    public class Listener implements ActionListener {
        private final GymDashboardPanel gymDashboardPanel;

        public Listener(GymDashboardPanel gymDashboardPanel) {
            this.gymDashboardPanel = gymDashboardPanel;
        }
        public void actionPerformed(ActionEvent e) {
            JComponent source =(JComponent) e.getSource();
            if (source == buttonAddFilter) {
                addFilter();
            }else if(source == buttonClearFilters){
                clearFilters();
            }else if(source == comboBoxPropertyNames){
                histogram.updateHistogramContents(
                        EntryProperty.valueOf(comboBoxPropertyNames.getSelectedItem().toString().toUpperCase()),
                        filteredEntriesList);
                gymDashboardPanel.repaint();
            }
        }
    }

    /**
     * a class used to listen for ChangeEvents from the sliders
     */
    public class ChangeListener implements javax.swing.event.ChangeListener {

        public ChangeListener(){}

        public void stateChanged(ChangeEvent e) {
            executeQuery();
        }
    }
}