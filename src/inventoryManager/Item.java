package inventoryManager;

public class Item {
	
	private String name;
	private String category;
	private int price;

	Item(String name, String category, int price){
		this.setName(name);
		this.setCategory(category);
		this.setPrice(price);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}
	
	
}
