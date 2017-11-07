package farmacia.stock.models.entity;

public class Counter {

	private int actualNumber;

	public Counter() {
		this.actualNumber = 0;
	}

	public int sumAndGet(int i) {
		this.actualNumber += i;
		return this.actualNumber;
	}
	
	public int getActualNumber() {
		return this.actualNumber;
	}
}
