package cs122b;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class SaxXmlParser extends DefaultHandler {
	private String tempVal;
	
	//to maintain context
	private Book tempBook;
	//private final String dtdFile = "dblp-data-small/dblp.dtd";
	//
	private String genreList = "article|inproceedings|proceedings|book|incollection|phdthesis|mastersthesis|www";
	private HashSet<String> BookTitle;
	private HashMap<String,Book> Document;
	private HashSet<String> Genres;
	private HashSet<String> People;
	private HashSet<String> Publisher;

	public SaxXmlParser(){
		BookTitle = new HashSet<String>();
		Genres = new HashSet<String>();
		People = new HashSet<String>();
		Publisher = new HashSet<String>();
		Document = new HashMap<String,Book>();	
		setGenres();
	}
	
	private void setGenres(){
		String[] tokens = genreList.split("\\|");
		for(String s : tokens){
			Genres.add(s);
		}
	}
	
	private int validateInt(String value){
		int intValue = -1;
		try{
			intValue = Integer.parseInt(value);
		}catch(NumberFormatException e){
			System.out.println("wrong number fromat:"+value);
		}
		return intValue;
	}
	
	public void parseDocument(String fileName){
		
		//get a factory
		SAXParserFactory spf = SAXParserFactory.newInstance();
		try {
		
			//get a new instance of parser
			SAXParser parser = spf.newSAXParser();

			//parse the file and also register this class for call backs
			parser.parse(fileName, this);
			
		}catch(SAXException se) {
			se.printStackTrace();
		}catch(ParserConfigurationException pce) {
			pce.printStackTrace();
		}catch (IOException ie) {
			ie.printStackTrace();
		}
	}
	//Event Handlers
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		//reset
		tempVal = "";
		if(Genres.contains(qName.toLowerCase())){
			tempBook = new Book();
		}
	}
	
	public void characters(char[] ch, int start, int length) throws SAXException {
			String val = new String(ch,start,length);
			if(val.equals("\n"))
			 	return;

			tempVal = val.replace("\n", " ").trim();
			//System.out.println(tempVal);
	}
	
	public void endElement(String uri, String localName, String qName) throws SAXException {
		String attr = qName.toLowerCase();	

		if(tempVal.length() == 0)
			return;

		if(Genres.contains(attr)){
			tempBook.setGenre(attr);
			Document.put(tempBook.title,tempBook);
		}
		else if(attr.equals("editor")) {
			if(tempVal.length() != 0){
				People.add(tempVal);
				tempBook.setEditor(tempVal);
			}
		}
		else if(attr.equals("author")) {
				People.add(tempVal);
				tempBook.setAuthor(tempVal);
		}
		else if (attr.equals("booktitle")){
				BookTitle.add(tempVal);
				tempBook.setBookTitle(tempVal);
		}
		else if (attr.equals("publisher")){
				Publisher.add(tempVal);
				tempBook.setPublisher(tempVal);
		}
		else if (attr.equals("title")){
			tempBook.setTitle(tempVal);
		}
		else if (attr.equals("year")){
			int value = validateInt(tempVal);
			if(value != -1)
				tempBook.setYear(value);
		}
		else if (attr.equals("volume")){
			int value = validateInt(tempVal);
			if(value != -1)
				tempBook.setVolume(value);
		}
		else if (attr.equals("number")){
			int value = validateInt(tempVal);
			if(value != -1)
				tempBook.setNumber(value);
		}
		else if (attr.equals("url")){
			tempBook.setUrl(tempVal);
		}
		else if (attr.equals("ee")){
			tempBook.setEe(tempVal);
		}
		else if (attr.equals("cdrom")){
			tempBook.setCdrom(tempVal);
		}
		else if (attr.equals("cite")){
			tempBook.setCite(tempVal);
		}
		else if (attr.equals("crossref")){
			tempBook.setCrossref(tempVal);
		}
		else if (attr.equals("isbn")){
			tempBook.setIsbn(tempVal);
		}
		else if (attr.equals("series")){
			tempBook.setSeries(tempVal);
		}
		else if (attr.equals("pages")){
			tempBook.setPages(tempVal);
		}
	}
	
	public HashMap<String,Book> getDocuments(){
		return Document;
	}
	
	public HashSet<String> getGenres(){
		return Genres;
	}
	
	public HashSet<String> getBookTitels(){
		return BookTitle;
	}
	
	public HashSet<String> getPublishers(){
		return Publisher;
	}
	
	public HashSet<String> getPeople(){
		return People;
	}
	
	public void run(String fileName){
		parseDocument(fileName);
	}
}
