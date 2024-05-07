package meu_campo_minado;

public class Square {

	private String text;

	private boolean open;
	private boolean mine;
	private boolean flag;

	public Square() {
		setText("?");
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getText() {
		return this.text;
	}

	public void setMine() {
		mine = true;
	}

	public boolean getMine() {
		return mine;
	}

	public void setOpen() {
		open = true;
	}

	public boolean getOpen() {
		return open;
	}

	public void flagOn() {
		flag = true;
	}
	
	public void flagOff() {
		flag = false;
	}
	
	public boolean getFlag() {
		return flag;
	}

	public String toString() {
		return getText();
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((text == null) ? 0 : text.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Square other = (Square) obj;
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		return true;
	}
}
