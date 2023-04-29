package pl.edu.pw.ee;

class LongestCommonSubsequence {
    private Node[][] matrix;
    private String topStr;
    private String leftStr;
    private int nColumns;
    private int nRows;
    private String result;

    public LongestCommonSubsequence(String topStr, String leftStr) {
        if (topStr == null || leftStr == null) {
            throw new IllegalArgumentException("Given string cannot be null");
        }
        this.topStr = topStr;
        this.leftStr = leftStr;
        nColumns = topStr.length() + 1;
        nRows = leftStr.length() + 1;
        initializeMatrices();
    }

    public String findLCS() {
        traverse();
        return concatenateString();
    }

    public void display() {
        if (result == null) {
            throw new IllegalStateException("findLCS method must be used to display result");
        }
        System.out.println(buildBorderLine());
        System.out.println(buildTopLine(0));
        System.out.println(buildHeaderMiddleLine());
        System.out.println(buildBottomLine());
        System.out.println(buildBorderLine());
        for (int row = 0; row < nRows; row++) {
            System.out.println(buildTopLine(row));
            System.out.println(buildMiddleLine(row));
            System.out.println(buildBottomLine());
            System.out.println(buildBorderLine());
        }
    }

    private void initializeMatrices() {
        matrix = new Node[nRows][nColumns];
        for (int i = 0; i < nRows; i++) {
            for (int j = 0; j < nColumns; j++) {
                matrix[i][j] = new Node(0);
            }
        }
    }

    private void traverse() {
        for (int row = 1; row < nRows; row++) {
            for (int column = 1; column < nColumns; column++) {
                Node node = matrix[row][column];
                if (topStr.charAt(column - 1) == leftStr.charAt(row - 1)) {
                    node.setValue(matrix[row - 1][column - 1].getValue() + 1);
                    node.setDirection(Direction.DIAGONALLY);
                } else if (matrix[row - 1][column].getValue() >= matrix[row][column - 1].getValue()) {
                    node.setValue(matrix[row - 1][column].getValue());
                    node.setDirection(Direction.UP);
                } else {
                    node.setValue(matrix[row][column - 1].getValue());
                    node.setDirection(Direction.LEFT);
                }
            }
        }
    }

    private String concatenateString() {
        StringBuilder stringBuilder = new StringBuilder();
        int row = nRows - 1;
        int column = nColumns - 1;
        while (row > 0 || column > 0) {
            Node node = matrix[row][column];
            node.setOnPath(true);
            if (node.getDirection() == Direction.DIAGONALLY) {
                stringBuilder.append(leftStr.charAt(row - 1));
                row--;
                column--;
            } else if (node.getDirection() == Direction.UP) {
                row--;
            } else if (node.getDirection() == Direction.LEFT) {
                column--;
            } else {
                break;
            }
        }
        stringBuilder.reverse();
        String result = stringBuilder.toString();
        this.result = result;
        return result;
    }

    private void repeatAppend(StringBuilder stringBuilder, String pattern, int count) {
        for (int i = 0; i < count; i++) {
            stringBuilder.append(pattern);
        }
    }

    private String buildBorderLine() {
        StringBuilder stringBuilder = new StringBuilder("+");
        repeatAppend(stringBuilder, "-", 7);
        stringBuilder.append("+");
        repeatAppend(stringBuilder, "-----+", nColumns);
        return stringBuilder.toString();
    }

    private String buildTopLine(int row) {
        StringBuilder stringBuilder = new StringBuilder("|");
        repeatAppend(stringBuilder, " ", 7);
        stringBuilder.append("|");
        for (int i = 0; i < nColumns; i++) {
            Node node = matrix[row][i];
            if (!node.isOnPath()) {
                stringBuilder.append("     ");
            } else if (node.getDirection() == Direction.DIAGONALLY) {
                stringBuilder.append("\\    ");
            } else if (node.getDirection() == Direction.UP) {
                stringBuilder.append("  ^  ");
            } else {
                stringBuilder.append("     ");
            }
            stringBuilder.append("|");
        }
        return stringBuilder.toString();
    }

    private String buildHeaderMiddleLine() {
        StringBuilder stringBuilder = new StringBuilder("|       |     |");
        for (int i = 1; i < nColumns; i++) {
            stringBuilder.append("  ");
            String symbol = SpecialCharacterReplacer.getReplacement(topStr.charAt(i - 1));
            stringBuilder.append(symbol);
            repeatAppend(stringBuilder, " ", 3 - symbol.length());
            stringBuilder.append("|");
        }
        return stringBuilder.toString();
    }

    private String buildMiddleLine(int row) {
        StringBuilder stringBuilder = new StringBuilder();
        if (row == 0) {
            stringBuilder.append("|       |");
        } else {
            stringBuilder.append("|   ");
            String symbol = SpecialCharacterReplacer.getReplacement(leftStr.charAt(row - 1));
            stringBuilder.append(symbol);
            repeatAppend(stringBuilder, " ", 4 - symbol.length());
            stringBuilder.append("|");
        }
        for (int i = 0; i < nColumns; i++) {
            Node node = matrix[row][i];
            if (node.getDirection() == Direction.LEFT && node.isOnPath()) {
                stringBuilder.append("< ");
            } else {
                stringBuilder.append("  ");
            }
            String value = String.valueOf(node.getValue());
            stringBuilder.append(value);
            repeatAppend(stringBuilder, " ", 3 - value.length());
            stringBuilder.append("|");
        }
        return stringBuilder.toString();
    }

    private String buildBottomLine() {
        StringBuilder stringBuilder = new StringBuilder("|       |");
        repeatAppend(stringBuilder, "     |", nColumns);
        return stringBuilder.toString();
    }

}
