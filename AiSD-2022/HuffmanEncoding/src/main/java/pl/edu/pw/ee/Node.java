package pl.edu.pw.ee;

public class Node {
    private Node left, right;
    private int count;
    private Character character;
    private boolean isLeaf;

    public Node(Character character, int count) {
        this.character = character;
        this.count = count;
        this.isLeaf = true;
    }

    public Node(int count) {
        this.count = count;
        this.isLeaf = false;
    }

    public Node getLeft() {
        return left;
    }

    public Node getRight() {
        return right;
    }

    public int getCount() {
        return count;
    }

    public Character getCharacter() {
        return character;
    }

    public boolean isLeaf() {
        return this.isLeaf;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public void setRight(Node right) {
        this.right = right;
    }

}