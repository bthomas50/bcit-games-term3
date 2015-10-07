package entities;

public class BaseClientEntity {

	public int id;
	public String username;
	
	// Location
	public int x;
	public int y;
	
	public BaseClientEntity(int id) {
		super();
		this.id = id;
	}
	
	public BaseClientEntity(int id, String username) {
		super();
		this.id = id;
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
}
