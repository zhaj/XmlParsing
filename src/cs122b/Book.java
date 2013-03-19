package cs122b;

import java.util.HashSet;

public class Book {
	private final int TITLE_LENGTH = 300;
	
	String genre;
	
	String editor;
	
	String bookTitle;
	
	String publisher;
	
	//dblp document
	String title;
	//parsing pages to get start_page and end_page
	int start_page = -1;
	int end_page = -1;
	
	int year = -1;
	int volume = -1;
	int number = -1;
	String url;
	String ee;
	String cdrom;
	String cite;
	String crossref;
	String isbn;
	String series;
	
	HashSet<String> authors;
	int docID = -1;
	
	public void setDocID(int id) {
		this.docID = id;
	}
	public int getDocID() {
		return docID;
	}
	public Book(){
		authors = new HashSet<String>();
	}
	
	public void setAuthor(String name) {
		this.authors.add(name);
	}
	public HashSet<String> getAuthors(){
		return authors;
	}
	
	public String getGenre() {
		return genre;
	}
	public void setGenre(String genre) {
		this.genre = genre;
	}
	public String getEditor() {
		return editor;
	}
	public void setEditor(String editor) {
		this.editor = editor;
	}
	public String getBookTitle() {
		return bookTitle;
	}
	public void setBookTitle(String bookTitle) {
		this.bookTitle = bookTitle;
	}
	public String getPublisher() {
		return publisher;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public int getVolume() {
		return volume;
	}
	public int getNumber() {
		return number;
	}
	public String getEe() {
		return ee;
	}
	public String getCdrom() {
		return cdrom;
	}
	public String getCite() {
		return cite;
	}
	public String getCrossref() {
		return crossref;
	}
	public String getSeries() {
		return series;
	}
	
	public String getTitle() {
		return title;
	}
	public int getYear() {
		return year;
	}
	public String getIsbn() {
		return isbn;
	}
	public String getUrl() {
		return url;
	}
	
	public void setVolume(int volume) {
		this.volume = volume;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public void setEe(String ee) {
		this.ee = ee;
	}
	public void setCdrom(String cdrom) {
		this.cdrom = cdrom;
	}
	public void setCite(String cite) {
		this.cite = cite;
	}
	public void setCrossref(String crossref) {
		this.crossref = crossref;
	}
	public void setSeries(String series) {
		this.series = series;
	}

	public void setTitle(String title) {
		if(title.length() > TITLE_LENGTH)
			this.title = title.substring(0, TITLE_LENGTH - 1);
		else
			this.title = title;
	}

	public void setYear(int year) {
		this.year = year;
	}
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	public void setPages(String page){
		String[] pages = page.split("-");
		try{
			if(pages.length == 2)
			{
				start_page = Integer.parseInt(pages[0]);
				end_page = Integer.parseInt(pages[1]);
			}
			else if(pages.length == 1){
				start_page = Integer.parseInt(pages[0]);
			}
			else
				System.out.println("wrong page fromat:"+page);
		}catch(NumberFormatException e){
			System.out.println("wrong page fromat:"+page);
		}
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("Title: " + getTitle() + "\n");
		sb.append("pages: " + start_page + "-" + end_page + "\n");
		sb.append("Year: " + getYear() + "\n");
		sb.append("Volume: " + getVolume() + "\n");
		sb.append("Number: " + getNumber() + "\n");
		sb.append("Url: " + getUrl() + "\n");
		sb.append("ee: " + getEe() + "\n");
		sb.append("Cdrom: " + getCdrom() + "\n");
		sb.append("Cite: " + getCite() + "\n");
		sb.append("Crossref: " + getCrossref() + "\n");
		sb.append("Isbn: " + getIsbn() + "\n");
		sb.append("Series: " + getSeries() + "\n");
		sb.append("Editor: " + getEditor() + "\n");
		sb.append("Authors: ");
		for(String a : getAuthors()){
			sb.append(a+",");
		}
		
		sb.append("\nEditor: " + getEditor() + "\n");
		sb.append("Booktitle: " + getBookTitle() + "\n");
		sb.append("Genre: " + getGenre() + "\n");
		sb.append("Publisher: " + getPublisher() + "\n");
		return sb.toString();		
	}

}
