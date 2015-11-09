import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.NoSuchElementException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Iterator;



public class MyEngine implements SearchEngine {
	


    private static final int DEFAULT_SIZE = 5000;

    private int size = 0;
    private int max;
    private boolean searchChoice = false;
    private static WebPageReader reader;
    private static HashSet<String> indexSet;
    private static HashSet<String> blackListSet;
    private static HashMap<String, HashSet<String>> indexMap;
    private static Scanner whiteListScanner;
    private static Scanner blackListScanner;
    private static Iterator<String> blackListIterator;
    private static LinkedHashSet<String> linksToVisit;
    private static HashSet<String> linksVisited;
    private static Scanner userInput;
    private static String choice;


    public MyEngine(){ // DONE
        this(DEFAULT_SIZE);
    }

    public MyEngine(int theMax) { // DONE
        setMax(theMax);
        initialiseVariables();
    }
    
    public void initialiseVariables () {
    	indexSet = new HashSet<String>();
    	blackListSet = new HashSet<String>();
    	indexMap = new HashMap<String, HashSet<String>>();
    	linksToVisit = new LinkedHashSet<String>();
    	linksVisited = new HashSet<String>();

    	
    }

    public void setMax(int theMax){ // DONE
        max = theMax;
    }
    

    
    public boolean setBreadthFirst(){ // TODO
        return true;
       
    }

    public boolean setDepthFirst(){ // TODO
        return true;
    }
    
    public static String getSearchChoice() {
    	
        userInput = new Scanner(System.in);
        System.out.println("Type in your prefered searching method, \"depth\" or \"breadth\":");
        String inputString = userInput.next();
        boolean continueAsking = true;
        try {
        	
        	while (continueAsking) {
            	if (inputString.equals("depth") || inputString.equals("breadth")) {
            		continueAsking = false;
            		userInput.close();
            		return inputString;
            	} else {
            		System.out.println("You have not entered \"depth\" or \"breadth\", please try again:");
            		inputString = userInput.next();
            	}
        	}
        	userInput.close();
        	return null;
            
        } catch (NoSuchElementException e) {
            return null;
        }
    }
    
    

    public void crawlFrom(String webAdress) { // TODO
        

    	

    	try {
    		whiteListScanner = new Scanner(new File("words.txt"));
    	} catch (FileNotFoundException e) {
    		System.out.println("Caught FileNotFoundException " + e.getMessage());
    	}
    	
    	while (whiteListScanner.hasNext()) {
    		indexSet.add(whiteListScanner.next());
    	}
    	
    	whiteListScanner.close();
    	
    	
    	try {
    		blackListScanner = new Scanner(new File("stopwords.txt"));
    	} catch (FileNotFoundException e) {
    		System.out.println("Caught FileNotFoundException " + e.getMessage());
    	}
    	
    	while (blackListScanner.hasNext()) {
    		blackListSet.add(blackListScanner.next());
    	}
    	
    	blackListScanner.close();
    	
        blackListIterator = blackListSet.iterator();
        	
        	
        while (blackListIterator.hasNext()) {
       	String blackListWord = blackListIterator.next();
        	for (String whiteListWord : indexSet) {
        		if (whiteListWord.equals(blackListWord)) {
        			indexSet.remove(blackListWord);
        			break;
        		}
        	}
    	}
        
        for (String finalIndexWord : indexSet) {
        	indexMap.put(finalIndexWord, new HashSet<String>());
        }
        

            	
    	reader = new WebPageReader(webAdress);
        reader.run();
        
        
        
        choice = getSearchChoice();
        
        
        crawl(webAdress);
        
        for(Object objname: indexMap.keySet()) {
     	   System.out.print(objname + "\t");
     	   System.out.println(indexMap.get(objname));
     	 }
        
        
    }
    
    
    private void crawl (String webAdress) {
    	
    	while (size < max) {
            for (String wordInIndex : indexMap.keySet()) {
            	for (String wordOnWebsite : reader.getWords()) {
            		if (wordInIndex.equalsIgnoreCase(wordOnWebsite)) {
            			indexMap.get(wordInIndex).add(webAdress);
            			reader.getWords().remove(wordOnWebsite);
            			size++;
            			break;
            		}
            	}
            }
            
            if (choice.equals("depth")) {
                LinkedHashSet<String> tempLinkStorage = new LinkedHashSet<String>();
                        for (String previousLink : linksToVisit) {
                        	tempLinkStorage.add(previousLink);
                        }
                        linksToVisit = new LinkedHashSet<String>();
                        for (String linkOnPage : reader.getLinks()) {
                    		linksToVisit.add(linkOnPage);
                    	}
                        for (String linkInTemp : tempLinkStorage) {
                        	linksToVisit.add(linkInTemp);
                        }
                        tempLinkStorage = null;
                        
            } else {
            	
            }
            
           
            linksVisited.add(webAdress);
            
            for (String element : linksVisited) {
            	System.out.println(element);
            }
            
            System.out.println(linksToVisit.iterator().next() + "HALLA");
            crawl(linksToVisit.iterator().next());
            

    	}

        

    }

    public String[] searchHits(String target){ // TODO
        String[] out = new String[reader.getWords().size()];
        out[0] = reader.toString();
        		
        // reader.getLinks().toArray(new String[reader.getLinks().size()]);
        return out;
    }

    public int size(){ // DONE
        return size;
    }

    
    /*
     * Simple test code
     */
    public static void main(String[] args){
        String AFTEN = "https://snl.no";
        String TARGET = "alalalalalalalalallalalalall";

        MyEngine engine = new MyEngine();
        System.out.print("Searching, start....");
        engine.crawlFrom(AFTEN);
        System.out.printf("finish. Size of index = %d%n",engine.size());

        System.out.printf("Occurences of \"%s\":%n",TARGET);
        String[] results = engine.searchHits(TARGET);
        for (String s: results)
            System.out.println(s);
    }
}
