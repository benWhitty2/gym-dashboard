package uk.ac.sheffield.com1003.assignment2425;

import uk.ac.sheffield.com1003.assignment2425.codeprovided.AbstractQueryParser;
import uk.ac.sheffield.com1003.assignment2425.codeprovided.Query;
import uk.ac.sheffield.com1003.assignment2425.codeprovided.EntryProperty;
import uk.ac.sheffield.com1003.assignment2425.codeprovided.SubQuery;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

// This class provides some default code, but it needs to be completely replaced



public class QueryParser extends AbstractQueryParser {
    @Override
    /**
     * takes list containing the tokens for multiple query's and returns them as a list of query objects
     *
     * @param queryTokens a list containing the tokens for multiple different query's
     * @return a list of query objects
     * @throws IllegalArgumentException if the list of queryTokens if empty or there is an error
     * with assembling the query then an Exception will be thrown
     *
     *
     *
     */
    public List<Query> buildQueries(List<String> queryTokens) throws IllegalArgumentException {
        if (queryTokens.isEmpty()) {
            throw new IllegalArgumentException("no tokens found in query file");
        }

        ArrayList<Query> queries = new ArrayList<>();
        Iterator<String> qTIterator = queryTokens.iterator();
        ArrayList<String> indvidualQueryTokens = new ArrayList<String>();
        String token = qTIterator.next();

        for (int i = 0; i < queryTokens.size()-1; i++) {
            indvidualQueryTokens.add(token);
            token = qTIterator.next();
            //if next token is the start of another query
            if(token.equalsIgnoreCase("select")){
                //query is built and tokens are cleared
                try{
                queries.add(buildQuerie(indvidualQueryTokens));
                }
                catch(IllegalArgumentException e){
                    throw e;
                }
                indvidualQueryTokens.clear();
            }
        }
        //builds final query
        indvidualQueryTokens.add(token);
        try{
        queries.add(buildQuerie(indvidualQueryTokens));
        }catch(IllegalArgumentException e){
            throw e;
        }

        return queries;
    }

    /**
     * receives the tokens for one singular query and returns a query object
     *
     * @param queryTokens a list of strings that make up a logical query
     * @return a new query object
     * @throws IllegalArgumentException if the query submitted is invalid then
     * an exception if thrown
     *
     */
    public Query buildQuerie(List<String> queryTokens) throws IllegalArgumentException {
        ArrayList<SubQuery> subQueryList = new ArrayList<SubQuery>();

        if((queryTokens.size()-2) % 4 ==0) {//checks if query is a valid size
            System.out.println(queryTokens.size());

            try{
            //iterates through sub-query's
            for (int i = 0; i < ((queryTokens.size() - 3) / 4) + 1; i++) {
                subQueryList.add(new SubQuery(
                        EntryProperty.valueOf(queryTokens.get(3 + (i * 4)).toUpperCase()),//property
                        queryTokens.get(4 + (i * 4)),//operator
                        Double.parseDouble(queryTokens.get(5 + (i * 4)))));//operand
            }
            return new Query(subQueryList);

            }catch(Exception e){//program will not crash if a valid query is given
                throw new IllegalArgumentException("invalid file format for query");
            }
        }else{
            throw new IllegalArgumentException("a query is not of a valid size");
        }

    }
}



