package cs122b;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import java.util.HashMap;
import java.util.HashSet;

public class DBOperation {
	protected Connection conn;

	private PreparedStatement insertGenreStmt;
	private PreparedStatement insertPeopleStmt;
	private PreparedStatement insertBookTitleStmt;
	private PreparedStatement insertPublisherStmt;
	private PreparedStatement insertDblpDocStmt;
	private PreparedStatement insertAuthorDocMapStmt;
	private PreparedStatement selectDblpDocStmt;
	
	public DBOperation() throws Exception{
		getConnection();
		PrepareStatements();
	}
	
	public void getConnection() throws Exception{
		// Incorporate mySQL driver
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		conn =  DriverManager.getConnection("jdbc:mysql:///bookdb","root", "root"); 
	}
	public void close() throws SQLException{
		conn.close();
	}

	public int InsertGenre(HashSet<String> genres) throws SQLException{
		//Set auto-commit to false
		conn.setAutoCommit(false);
		insertGenreStmt = conn.prepareStatement("insert into tbl_genres (genre_name) values(?);");
		for(String g : genres){
			insertGenreStmt.setString(1, g);
			
			// Add it to the batch
			insertGenreStmt.addBatch();
		}
		//Create an int[] to hold returned values
		insertGenreStmt.executeBatch();

		//Explicitly commit statements to apply changes
		conn.commit();
		return 0;
	}
	
	public int InsertPeople(HashSet<String> people) throws SQLException{
		//Set auto-commit to false
		conn.setAutoCommit(false);
		insertPeopleStmt = conn.prepareStatement("insert into tbl_people (name) values(?);");

		for(String p : people){
			insertPeopleStmt.setString(1, p);
			insertPeopleStmt.addBatch();
		}
		//Create an int[] to hold returned values
		insertPeopleStmt.executeBatch();

		//Explicitly commit statements to apply changes
		conn.commit();
		return 0;
	}
	
	public int InsertBookTitle(HashSet<String> title) throws SQLException{
		//Set auto-commit to false
		conn.setAutoCommit(false);
		insertBookTitleStmt = conn.prepareStatement("insert into tbl_booktitle (title) values(?);");

		for(String t : title){
			insertBookTitleStmt.setString(1,t);
			insertBookTitleStmt.addBatch();
		}
		//Create an int[] to hold returned values
		insertBookTitleStmt.executeBatch();

		//Explicitly commit statements to apply changes
		conn.commit();
		return 0;
	}
	
	public int InsertPublisher(HashSet<String> publishers) throws SQLException{
		//Set auto-commit to false
		conn.setAutoCommit(false);
		insertPublisherStmt = conn.prepareStatement("insert into tbl_publisher (publisher_name) values(?);");
		for(String p : publishers){
			insertPublisherStmt.setString(1, p);
			insertPublisherStmt.addBatch();
		}
		//Create an int[] to hold returned values
		insertPublisherStmt.executeBatch();

		//Explicitly commit statements to apply changes
		conn.commit();
		return 0;
	}
	
	public void InsertDBLPDocument(HashMap<String,Book> documents, HashMap<String,Integer> genre,HashMap<String,Integer> people, 
						HashMap<String,Integer> bookTitle, HashMap<String,Integer> publisher) throws SQLException{
		conn.setAutoCommit(false);
		for (Book  book: documents.values()){	
			Integer peopleId = people.get(book.getEditor());
			Integer publisherId = publisher.get(book.getPublisher());
			Integer genreId = genre.get(book.getGenre());
			Integer bookTitleId = bookTitle.get(book.getBookTitle());
		
			String title = book.title;

			insertDblpDocStmt.setString(1,title);
			
			if(book.start_page == -1)
				insertDblpDocStmt.setNull(2, Types.INTEGER);
			else
				insertDblpDocStmt.setInt(2, book.start_page);
			
			if(book.end_page == -1)
				insertDblpDocStmt.setNull(3, Types.INTEGER);
			else
				insertDblpDocStmt.setInt(3, book.end_page);
		
			if(book.year == -1)
				insertDblpDocStmt.setNull(4, Types.INTEGER);
			else
				insertDblpDocStmt.setInt(4, book.year);
			
			if(book.volume == -1)
				insertDblpDocStmt.setNull(5, Types.INTEGER);
			else
				insertDblpDocStmt.setInt(5, book.volume);
			
			if(book.number == -1)
				insertDblpDocStmt.setNull(6, Types.INTEGER);
			else
				insertDblpDocStmt.setInt(6, book.number);
			
			insertDblpDocStmt.setString(7, book.url);
			insertDblpDocStmt.setString(8, book.ee);
			insertDblpDocStmt.setString(9, book.cdrom);
			insertDblpDocStmt.setString(10, book.cite);
			insertDblpDocStmt.setString(11, book.crossref);
			insertDblpDocStmt.setString(12, book.isbn);
			insertDblpDocStmt.setString(13, book.series);
			
			if(peopleId == null)
				insertDblpDocStmt.setNull(14, Types.INTEGER);
			else
				insertDblpDocStmt.setInt(14, peopleId);
	
			if(bookTitleId == null)
				insertDblpDocStmt.setNull(15, Types.INTEGER);
			else
				insertDblpDocStmt.setInt(15, bookTitleId);
			if(genreId == null)
				insertDblpDocStmt.setNull(16, Types.INTEGER);
			else
				insertDblpDocStmt.setInt(16, genreId);
			if(publisherId == null)
				insertDblpDocStmt.setNull(17, Types.INTEGER);
			else
				insertDblpDocStmt.setInt(17, publisherId);
		
			insertDblpDocStmt.addBatch();
		}
		insertDblpDocStmt.executeBatch();

		//Explicitly commit statements to apply changes
		conn.commit();
		
		//get id
		ResultSet rs;
		rs = selectDblpDocStmt.executeQuery();
		while(rs.next()){
			documents.get((rs.getString(2))).setDocID(rs.getInt(1));
		}
		selectDblpDocStmt.close();
		rs.close();

	}
	
	public int InsertAuthorDocMapping(HashMap<String,Book> documents, HashMap<String,Integer> people) throws SQLException{
		int result = 1;
		for (Book book : documents.values()){
			int docID = book.getDocID();
			conn.setAutoCommit(false);
			insertAuthorDocMapStmt.setInt(1, docID);
			if(book.getAuthors().size() ==0)
				continue;
			for(String a : book.getAuthors()){
				if((people.get(a)) != null){
					insertAuthorDocMapStmt.setInt(2, people.get(a));
				}
				else{ 
					//update tbl_people and then insert author_doc_mapping table
					int authorID = insertPeopleOneAuthor(a);
					if(authorID == -1){
						System.out.println("wrong author ID for author: "+a);
						continue;
					}
					insertAuthorDocMapStmt.setInt(2, authorID);		
				}
				insertAuthorDocMapStmt.addBatch();
			}
			//Create an int[] to hold returned values
			insertAuthorDocMapStmt.executeBatch();

			//Explicitly commit statements to apply changes
			conn.commit();			
		}
		return result;
	}
	
	public  HashMap<String,Integer> getGenre() throws SQLException{
		ResultSet rs = getGenreResults();
		HashMap<String,Integer> map = new HashMap<String,Integer>();
		while(rs.next()){
			map.put(rs.getString(2), rs.getInt(1));
		}
		
		rs.close();
		return map;
	}
	
	public  HashMap<String,Integer> getPeople() throws SQLException{
		ResultSet rs = getPeopleResults();
		HashMap<String,Integer> map = new HashMap<String,Integer>();
		while(rs.next()){
			map.put(rs.getString(2), rs.getInt(1));
		}
		rs.close();
		return map;
	}
	
	public  HashMap<String,Integer> getPublisher() throws SQLException{
		ResultSet rs = getPublisherResults();
		HashMap<String,Integer> map = new HashMap<String,Integer>();
		while(rs.next()){
			map.put(rs.getString(2), rs.getInt(1));
		}
		rs.close();
		return map;
	}
	
	public  HashMap<String,Integer> getBookTitle() throws SQLException{
		ResultSet rs = getBookTitleResults();
		HashMap<String,Integer> map = new HashMap<String,Integer>();
		while(rs.next()){
			map.put(rs.getString(2), rs.getInt(1));
		}
		rs.close();
		return map;
	}
	
	private void PrepareStatements() throws SQLException{
		
		
		insertDblpDocStmt = conn.prepareStatement("insert into tbl_dblp_document " +
							"(title,start_page,end_page,year,volume,number,url,ee,cdrom,cite,crossref,isbn," +
							"series,editor_id,booktitle_id,genre_id,publisher_id) " +
							"values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);");
		insertAuthorDocMapStmt = conn.prepareStatement("insert into tbl_author_document_mapping (doc_id,author_id) values(?,?);");
		
		selectDblpDocStmt = conn.prepareStatement("select id, title from tbl_dblp_document;");
	}
	
	private ResultSet getGenreResults() throws SQLException{
		String sql = "select id, genre_name from tbl_genres;";
		Statement stmt = conn.createStatement();
		return stmt.executeQuery(sql);
	}	
	
	private ResultSet getPeopleResults() throws SQLException{
		String sql = "select id, name from tbl_people;";
		Statement stmt = conn.createStatement();
		return stmt.executeQuery(sql);
	}
	
	private int insertPeopleOneAuthor(String author) throws SQLException{
		String sql = "select id from tbl_people where name='"+author+"';";
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		int id = -1;
		if(rs.next()){
			id = rs.getInt(1);
		}
		else{
			insertPeopleStmt.setString(1, author);
			insertPeopleStmt.executeUpdate();
			rs.close();
			rs = stmt.executeQuery(sql);
			if(rs.next()){
				id = rs.getInt(1);
			}
		}
		rs.close();
		return id;
	}
	
	private ResultSet getBookTitleResults() throws SQLException{
		String sql = "select id, title from tbl_booktitle;";
		Statement stmt = conn.createStatement();
		return stmt.executeQuery(sql);
	}
	
	private ResultSet getPublisherResults() throws SQLException{
		String sql = "select id, publisher_name from tbl_publisher;";
		Statement stmt = conn.createStatement();
		return stmt.executeQuery(sql);
	}
	
}
