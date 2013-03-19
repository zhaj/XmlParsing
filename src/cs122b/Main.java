package cs122b;

import java.util.HashMap;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//paser xml
		System.out.println("************START*****************");
		final String fileName = args[0];
		long start = System.currentTimeMillis();
		
		SaxXmlParser sp = new SaxXmlParser();
		sp.run(fileName);
		long end = System.currentTimeMillis();
		System.out.println(">>>>>>>parsing costs: " + (end - start) + " milliseconds");
		
		//get book
		HashMap<String, Book> documents = sp.getDocuments();
		
		// store to db
		try {
			DBOperation dbOperation = new DBOperation();
			//insert tables; 

			dbOperation.InsertGenre(sp.getGenres());
			dbOperation.InsertBookTitle(sp.getBookTitels());
			dbOperation.InsertPeople(sp.getPeople());
			dbOperation.InsertPublisher(sp.getPublishers());
			//get ID
			HashMap<String,Integer> genre = dbOperation.getGenre();
			HashMap<String,Integer> bookTitle =  dbOperation.getBookTitle();
			HashMap<String,Integer> people = dbOperation.getPeople();
			HashMap<String,Integer> publisher = dbOperation.getPublisher();
			
			//insert  tbl_dblp_document 
			
			//HashMap<Integer, ArrayList<String>> AuthorDoc = new HashMap<Integer, ArrayList<String>>();
			dbOperation.InsertDBLPDocument(documents, genre, people, bookTitle, publisher);

			// insert 
			dbOperation.InsertAuthorDocMapping(documents, people);
			start = System.currentTimeMillis();
			dbOperation.close();
		} catch (Exception e) {
			start = System.currentTimeMillis();
			e.printStackTrace();
		}
		System.out.println(">>>>>>>inserting to db costs: " + (start - end) + " milliseconds");
		System.out.println("\n************DONE*****************");

	}

}
