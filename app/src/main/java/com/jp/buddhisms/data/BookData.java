package com.jp.buddhisms.data;

public class BookData {

	private String bookName;
	private int type;
	private int koFile;
	private int deFile;
	private int moonFile;

	public BookData(String bookName, int type) {
		super();
		this.bookName = bookName;
		this.type = type;

	}

	public BookData(String bookName, int type, int koFile) {
		super();
		this.bookName = bookName;
		this.type = type;
		this.koFile = koFile;

	}

	public BookData(String bookName, int type, int koFile, int moonFile) {
		super();
		this.bookName = bookName;
		this.type = type;
		this.koFile = koFile;
		this.moonFile = moonFile;

	}

	public BookData(String bookName, int type, int koFile, int moonFile,
			int deFile) {
		super();
		this.bookName = bookName;
		this.type = type;
		this.koFile = koFile;
		this.moonFile = moonFile;
		this.deFile = deFile;

	}

	public int getKoFile() {
		return koFile;
	}

	public void setKoFile(int koFile) {
		this.koFile = koFile;
	}

	public int getDeFile() {
		return deFile;
	}

	public void setDeFile(int deFile) {
		this.deFile = deFile;
	}

	public int getMoonFile() {
		return moonFile;
	}

	public void setMoonFile(int moonFile) {
		this.moonFile = moonFile;
	}

	public String getBookName() {
		return bookName;
	}

	public void setBookName(String bookName) {
		this.bookName = bookName;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
