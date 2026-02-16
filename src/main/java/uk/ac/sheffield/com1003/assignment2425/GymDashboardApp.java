package uk.ac.sheffield.com1003.assignment2425;

import uk.ac.sheffield.com1003.assignment2425.codeprovided.*;
import uk.ac.sheffield.com1003.assignment2425.codeprovided.gui.AbstractGymDashboardPanel;
import uk.ac.sheffield.com1003.assignment2425.codeprovided.gui.GymDashboard;
import uk.ac.sheffield.com1003.assignment2425.gui.GymDashboardPanel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Iterator;



public class GymDashboardApp {
    private final AbstractEntryCatalog entryCatalog;
    private final List<Query> builtQueriesList;

    // do not change the constructor
    public GymDashboardApp(String entriesFileName, String queryFileName) {
        AbstractEntryCatalog abstractEntryCatalog = null;
        List<Query> builtQueriesList = null;
        try {
            abstractEntryCatalog = new EntryCatalog(entriesFileName);
            List<String> queryTokensFromFile = AbstractQueryParser.readQueryTokensFromFile(queryFileName);
            List<String> queryTokens = new ArrayList<>(queryTokensFromFile);
            try {
                QueryParser queryParser = new QueryParser();
                List<Query> queriesList = queryParser.buildQueries(queryTokens);
                builtQueriesList = new ArrayList<>(queriesList);
            } catch (IllegalArgumentException e) { // captures case of malformed queries in query file
                System.err.println(e);
                builtQueriesList = new ArrayList<>(); //this allows the program to resume, just skipping queries
            }
        } catch (IllegalArgumentException | IOException e) {
            System.err.println(e);
            System.exit(-1);
        }
        this.entryCatalog = abstractEntryCatalog;
        this.builtQueriesList = builtQueriesList;

    }

    // do not change this main method
    public static void main(String[] args) {
        if (args.length == 0) {
            args = new String[]{
                    "./src/main/resources/gym.csv",
                    "./src/main/resources/queries.txt"
            };
        }
        GymDashboardApp gymDashboardApp = new GymDashboardApp(args[0], args[1]);
        gymDashboardApp.startCLI();
        gymDashboardApp.startGUI();
    }

    // do not change this method
    public void startCLI() {
        // Basic catalogue information
        printQuestionAnswers();

        // Queries
        executeQueries();
    }

    // do not change this method
    private void executeQueries() {
        System.out.println("\n======================================");
        System.out.println("Executing queries...");
        printNumberQueries();

        int i = 1;
        for (Query query : builtQueriesList) {
            System.out.println("---> (" + i +") " + query.toString() + ":");
            List<Entry> queryResults = query.executeQuery(entryCatalog);
            System.out.println("-> Printing 5 out of " + queryResults.size() + " matching entries...\n");
            printFirstFiveEntriesInList(queryResults);
            System.out.println();
            i++;
        }
        System.out.println("\n======================================");
    }

    // do not change this method
    private void printNumberQueries() {
        System.out.println("In total, " + builtQueriesList.size() + " queries were found.");
        System.out.println();
    }

    // do not change this method
    private void printQuestionAnswers() {
        System.out.println("\n======================================");
        System.out.println("Printing question answers...\n");
        printTotalNumberOfUniqueAges();
        printAverageBmiForTallMembers();
        printAverageFatPercentage();
        printAverageBmiForAdvancedLevelMembers();
        printMembersCountWaterIntakeAbove3Liters();
        printPercentageAboveHealthyBmi();
        printNumberOfMembersWithYogaWorkoutType();
        System.out.println("\n======================================");

        printFirstFiveEntriesInCatalog();
    }

    /**
     * prints out the total number of unique ages in the entry catalog
     *
     */
    private void printTotalNumberOfUniqueAges() {
        List<Entry> entryList = entryCatalog.getEntriesList();
        Iterator<Entry> iterator = entryList.iterator();
        ArrayList<Double> ages = new ArrayList<>();
        Entry entry;
        //stores each unique age value and print out length of array
        while (iterator.hasNext()) {
            entry = iterator.next();
            if(!ages.contains(entry.getEntryProperty(EntryProperty.AGE))){
                ages.add(entry.getEntryProperty(EntryProperty.AGE));
            }
        }
        //if there are no values -1 is returned
        int numberOfUniqueAges = -1;
        if (!ages.isEmpty()) {
            numberOfUniqueAges = ages.size();
        }

        System.out.println("The total number of unique ages in the dataset is : " + numberOfUniqueAges);
    }

    /**
     * prints out the average BMI for members of the data set over 180cm
     */
    private void printAverageBmiForTallMembers() {
        List<Entry> entryList = entryCatalog.getEntriesList();
        entryList = EntryCatalog.getByPropertyMoreThan(EntryProperty.HEIGHT,entryList,1.8);
        double averageBmiForTallMembers = -1;
        if(!entryList.isEmpty()){
            averageBmiForTallMembers = entryCatalog.getAverageValue(EntryProperty.BMI,entryList);
        }
        //returns -1 if no individuals meet the criteria
        System.out.println("The average BMI of members with a height greater than 1.8 meters is : "
                + Double.parseDouble(String.format("%.2f",EntryCatalog.roundTo2DP(averageBmiForTallMembers))));
    }


    /**
     * prints average fat percentage for members who work out more than 4 days a week
     */
    private void printAverageFatPercentage() {
        List<Entry> entryList = entryCatalog.getEntriesList();
        //gets list of entry that workout more than 4 days a week
        entryList = EntryCatalog.getByPropertyMoreThan(EntryProperty.WORKOUT_FREQUENCY,entryList,4);
        double averageFatPercentage = -1;
        if(!entryList.isEmpty()){
            //gets average BFP of list
            averageFatPercentage = entryCatalog.getAverageValue(EntryProperty.BODY_FAT_PERCENTAGE, entryList);
        }
        //return -1 if no members meet the criteria
        System.out.println("The average fat percentage for members who workout more than 4 days a week is : "
                + Double.parseDouble(String.format("%.2f", EntryCatalog.roundTo2DP(averageFatPercentage))));
    }

    /**
     * prints the average BIM for all members of the entry catalog with an exeprience level above 2
     */
    private void printAverageBmiForAdvancedLevelMembers() {

        List<Entry> entryList = entryCatalog.getEntriesList();
        //gets list of entry with an experience level of 3 or higher
        entryList = EntryCatalog.getByPropertyMoreThan(EntryProperty.EXPERIENCE_LEVEL,entryList,2);
        double averageBmiForAdvancedLevelMembers = -1;
        if(!entryList.isEmpty()){
            averageBmiForAdvancedLevelMembers = entryCatalog.getAverageValue(EntryProperty.BMI,entryList);
        }
        //return -1 if no members meet the criteria
        System.out.println("The average BMI of members who have an Advanced level of experience is : "
                + Double.parseDouble(String.format("%.2f",
                EntryCatalog.roundTo2DP(averageBmiForAdvancedLevelMembers))));
    }

    /**
     * prints the number of members of the entry catalog set who drink more than 3 liters of water a day
     */
    private void printMembersCountWaterIntakeAbove3Liters() {

        List<Entry> entryList = entryCatalog.getEntriesList();
        //gets list of entry's with a water intake above 3 liters
        entryList = EntryCatalog.getByPropertyMoreThan(EntryProperty.WATER_INTAKE,entryList,3);
        int count = entryList.size();

        //return size of list
        System.out.println("The number of members with a water intake above 3 litres is : " + count);
    }

    /**
     * prints the percentage of members of the entry catalog with a BMI above 25
     */
    private void printPercentageAboveHealthyBmi() {
        List<Entry> entryList = entryCatalog.getEntriesList();
        //get list of entry's with BMI above 25
        List<Entry> filteredEntryList = EntryCatalog.getByPropertyMoreThan(EntryProperty.BMI,entryList,25);
        double percentageAboveHealthyBmi = -1;
        if (!entryList.isEmpty()) {
            percentageAboveHealthyBmi = (double) filteredEntryList.size()/entryList.size() * 100;
        }
        //if no individuals meet the criteria return -1
        System.out.println("The percentage of members with BMI above 25 is : " +
                EntryCatalog.roundTo2DP(percentageAboveHealthyBmi));
    }

    /**
     * prints number of members of the entry catalog that have the workout type YOGA
     */
    private void printNumberOfMembersWithYogaWorkoutType() {
        List<Entry> entryList = entryCatalog.getEntriesList();
        //gets entry's with workout type Yoga
        int count = entryCatalog.getEntriesListByEntryDetail(entryList,EntryDetail.WORKOUT_TYPE,"Yoga").size();

        //returns the size of the list
        System.out.println("The number of members with Yoga workout type is : " + count);
    }

    // do not change this method
    private void printFirstFiveEntriesInCatalog() {
        System.out.println("\n======================================");
        System.out.println("Printing first five gym entries in catalog...\n");
        printFirstFiveEntriesInList(entryCatalog.getFirstFiveEntries());
        System.out.println("\n======================================");
    }

    // do not change this method
    private void printFirstFiveEntriesInList(List<Entry> entriesList) {
        int count = 1;
        for (Entry e : entriesList){
            System.out.println(e);
            if (++count > 5)
                break;
        }
    }

    // do not change this method
    public void startGUI() {
        // Start GUI
        AbstractGymDashboardPanel gymDashboardPanel = new GymDashboardPanel(entryCatalog);
        GymDashboard eDashboard = new GymDashboard(gymDashboardPanel);
        eDashboard.setVisible(true);
    }


}