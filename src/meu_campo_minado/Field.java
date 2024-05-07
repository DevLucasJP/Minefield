package meu_campo_minado;

import java.util.Random;
import java.util.function.BinaryOperator;
import java.util.function.Predicate;

public class Field {

	private Square[][] field = new Square[9][9];
	private Square[][] hiddenField = new Square[9][9];

	private Predicate<Square> isMined = im -> im.getMine();
	private Predicate<Square> haveNumber = hn -> !hn.getMine() && !hn.getText().equals(" ");
	private Predicate<Square> blankSpace = bs -> bs.getText().equals(" ");

	public Field() {

		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {

				field[i][j] = new Square();
				hiddenField[i][j] = new Square();
			}
		}

		generateMines();
	}

	private void generateMines() {

		Random random = new Random();

		for (int i = 0; i < 10; i++) {

			int line = random.nextInt(9);
			int column = random.nextInt(9);

			while (isMined.test(hiddenField[line][column])) {
				line = random.nextInt(9);
				column = random.nextInt(9);
			}

			hiddenField[line][column].setText("*");
			hiddenField[line][column].setMine();
			generateWarnings(line, column);
		}
	}

	private void generateWarnings(int i, int j) {

		for (int x = 0; x < 9; x++) {
			for (int y = 0; y < 9; y++) {

				BinaryOperator<Integer> absolute = (a, b) -> Math.abs(a - b);
				int absLine = absolute.apply(i, x);
				int absColumn = absolute.apply(j, y);

				if (absLine == 1 && absColumn == 1 && !isMined.test(hiddenField[x][y])) {
					if (hiddenField[x][y].getText().equals("1")) {
						hiddenField[x][y].setText("2");
					} else if (hiddenField[x][y].getText().equals("2")) {
						hiddenField[x][y].setText("3");
					} else if (hiddenField[x][y].getText().equals("3")) {
						hiddenField[x][y].setText("4");
					} else {
						hiddenField[x][y].setText("1");
					}
				}

				if ((absLine + absColumn) == 1 && !isMined.test(hiddenField[x][y])) {
					if (hiddenField[x][y].getText().equals("1")) {
						hiddenField[x][y].setText("2");
					} else if (hiddenField[x][y].getText().equals("2")) {
						hiddenField[x][y].setText("3");
					} else if (hiddenField[x][y].getText().equals("3")) {
						hiddenField[x][y].setText("4");
					} else {
						hiddenField[x][y].setText("1");
					}
				}

				if (hiddenField[x][y].getText().equals("?")) {
					hiddenField[x][y].setText(" ");
				}
			}
		}
	}

	public boolean openField(int i, int j, String flag) {

		if (field[i][j].getOpen()) {
			System.out.println("O campo escolhido já está aberto!");

			return false;
		} else if (field[i][j].getFlag()) {
			field[i][j].flagOff();
			field[i][j].setText("?");

			return false;
		} else {
			if (flag.equalsIgnoreCase("s")) {
				field[i][j].flagOn();
				field[i][j].setText("F");
			} else {
				field[i][j].setOpen();

				if (!isMined.test(hiddenField[i][j])) {
					if (haveNumber.test(hiddenField[i][j])) {
						field[i][j].setText(hiddenField[i][j].getText());
						field[i][j].setOpen();
					} else if (blankSpace.test(hiddenField[i][j])) {
						field[i][j].setText(hiddenField[i][j].getText());
						field[i][j].setOpen();

						for (int x = 0; x < 9; x++) {
							for (int y = 0; y < 9; y++) {

								BinaryOperator<Integer> absolute = (a, b) -> Math.abs(a - b);
								int absLine = absolute.apply(i, x);
								int absColumn = absolute.apply(j, y);

								if (absLine == 1 && absColumn == 1 && blankSpace.test(hiddenField[x][y])
										&& !field[x][y].getOpen()) {
									field[x][y].setText(hiddenField[x][y].getText());
									field[x][y].setOpen();

									chain(x, y);
								} else if ((absLine + absColumn) == 1 && blankSpace.test(hiddenField[x][y])
										&& !field[x][y].getOpen()) {
									field[x][y].setText(hiddenField[x][y].getText());
									field[x][y].setOpen();

									chain(x, y);
								} else if ((absLine == 1 && absColumn == 1 && haveNumber.test(hiddenField[x][y]))
										&& !field[x][y].getOpen()) {
									field[x][y].setText(hiddenField[x][y].getText());
									field[x][y].setOpen();
								} else if ((absLine + absColumn) == 1 && haveNumber.test(hiddenField[x][y])
										&& !field[x][y].getOpen()) {
									field[x][y].setText(hiddenField[x][y].getText());
									field[x][y].setOpen();
								}
							}
						}
					}

					return false;
				} else {
					for (int x = 0; x < 9; x++) {
						for (int y = 0; y < 9; y++) {

							if (isMined.test(hiddenField[x][y])) {
								field[x][y].setText(hiddenField[x][y].getText());
								field[x][y].setOpen();
							}
						}
					}

					return true;
				}
			}

			if (winCondition()) {
				return true;
			} else {
				return false;
			}
		}
	}

	private void chain(int i, int j) {

		for (int x = 0; x < 9; x++) {
			for (int y = 0; y < 9; y++) {

				BinaryOperator<Integer> absolute = (a, b) -> Math.abs(a - b);
				int absLine = absolute.apply(i, x);
				int absColumn = absolute.apply(j, y);

				if (absLine == 1 && absColumn == 1 && blankSpace.test(hiddenField[x][y]) && !field[x][y].getOpen()) {
					field[x][y].setText(hiddenField[x][y].getText());
					field[x][y].setOpen();

					chain(x, y);
				} else if ((absLine + absColumn) == 1 && blankSpace.test(hiddenField[x][y]) && !field[x][y].getOpen()) {
					field[x][y].setText(hiddenField[x][y].getText());
					field[x][y].setOpen();

					chain(x, y);
				} else if ((absLine == 1 && absColumn == 1 && haveNumber.test(hiddenField[x][y]))
						&& !field[x][y].getOpen()) {
					field[x][y].setText(hiddenField[x][y].getText());
					field[x][y].setOpen();
				} else if ((absLine + absColumn) == 1 && haveNumber.test(hiddenField[x][y]) && !field[x][y].getOpen()) {
					field[x][y].setText(hiddenField[x][y].getText());
					field[x][y].setOpen();
				}

			}
		}
	}

	public boolean winCondition() {

		int winCon = 0;

		for (int x = 0; x < 9; x++) {
			for (int y = 0; y < 9; y++) {

				if (field[x][y].getOpen()) {
					winCon++;
				}
			}
		}

		if (winCon == 71) {
			return true;
		} else {
			return false;
		}
	}

	public String toString() {

		StringBuilder fieldView = new StringBuilder();

		for (int i = 0; i < 9; i++) {

			fieldView.append(i + " ");

			for (int j = 0; j < 9; j++) {
				fieldView.append(field[i][j]);

				if (j < 8) {
					fieldView.append(" ");
				}
			}

			if (i < 8) {
				fieldView.append("\n");
			} else {
				fieldView.append("\n  0 1 2 3 4 5 6 7 8");
			}
		}

		return fieldView.toString();
	}
}
