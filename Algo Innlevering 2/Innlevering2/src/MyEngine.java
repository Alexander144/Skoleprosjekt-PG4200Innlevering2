import java.util.HashMap;
import java.util.HashSet;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Iterator;



public class MyEngine implements SearchEngine {
	


    private static final int DEFAULT_SIZE = 5000;

    private int size = 0;
    private int max;
    private WebPageReader reader;
    private HashSet<String> indexSet;
    private HashSet<String> blackListSet;
    private HashMap<String, HashSet<String>> indexMap;
    private Scanner whiteListScanner;
    private Scanner blackListScanner;
    private Iterator<String> blackListIterator;

    public MyEngine(){ // DONE
        this(DEFAULT_SIZE);
    }

    public MyEngine(int theMax) { // DONE
        setMax(theMax);
    }

    public void setMax(int theMax){ // DONE
        max = theMax;
    }
    
    public boolean setBreadthFirst(){ // TODO
        return false;
    }

    public boolean setDepthFirst(){ // TODO
        return false;
    }
    
    

    public void crawlFrom(String webAdress) { // TODO
        
    	indexSet = new HashSet<String>();
    	blackListSet = new HashSet<String>();
    	indexMap = new HashMap<String, HashSet<String>>();
    	

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
        
        crawl(webAdress);
        
        for(Object objname: indexMap.keySet()) {
     	   System.out.print(objname + "\t");
     	   System.out.println(indexMap.get(objname));
     	 }
        
        
    }
    
    
    private void crawl (String webAdress) {
    	
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
