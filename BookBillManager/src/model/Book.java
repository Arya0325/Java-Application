package model;

public class Book {
	private int no;
    private String name;
    private double rate;
    private int quantity;
    private double total;
	public Book(int no, String name, double rate, int quantity, double total) {
		super();
		this.no = no;
		this.name = name;
		this.rate = rate;
		this.quantity = quantity;
		this.total = total;
	}
	public int getNo() {
		return no;
	}
	public void setNo(int no) {
		this.no = no;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getRate() {
		return rate;
	}
	public void setRate(double rate) {
		this.rate = rate;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
    

}
