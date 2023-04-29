package pl.edu.pw.ee;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

public class HuffmanTree {
    private Node root;
    private PriorityQueue<Node> queue;
    private Dictionary codes;
    private Stack<Node> stack;
    private byte encodingSize = 8;

    public HuffmanTree() {
        initializeQueue();
    }

    public HuffmanTree(int encodingSize) {
        this.encodingSize = (byte) encodingSize;
        initializeQueue();
    }

    public HuffmanTree(Node root, int encodingSize) {
        this.encodingSize = (byte) encodingSize;
        this.root = root;
        initializeQueue();
    }

    public Node getRoot() {
        return root;
    }

    public void buildTreeFromQueue() {
        if (queue.size() == 0) {
            throw new InvalidFileContentException("At least 1 character must occur in file");
        }
        if (queue.size() == 1) {
            char c = queue.peek().getCharacter();
            if (c > 0) {
                queue.add(new Node((char) (c - 1), 0));
            } else {
                queue.add(new Node((char) (c + 1), 0));
            }
        }
        mergeNodes();
    }

    public int buildTreeFromFileHeader(String pathName, Charset charset) {
        stack = new Stack<Node>();
        try {
            return buildTreeFromFile(pathName, charset);
        } catch (IOException e) {
            throw new InvalidFileContentException("Cannot decode tree from file " + pathName);
        }
    }

    public Dictionary getCodes() {
        codes = new Dictionary(256);
        getCode(root, new ArrayList<Boolean>());
        return codes;
    }

    public void addNodeToQueue(Node n) {
        if (queue == null) {
            throw new IllegalStateException("Queue must be initialized first");
        }
        queue.add(n);
    }

    public String encodeTree() {
        String encoded = "";
        encoded += (char) (encodingSize);

        List<Boolean> encodedAsBooleans = new ArrayList<Boolean>();
        traversePostOrder(root, encodedAsBooleans);
        encodedAsBooleans.add(Boolean.FALSE);

        HuffByte encoding = new HuffByte();
        int bitIndex = 0;

        for (Boolean b : encodedAsBooleans) {
            if (bitIndex == 8) {
                encoded += encoding.toChar();
                encoding = new HuffByte();
                bitIndex = 0;
            }
            if (b) {
                encoding.setBit(bitIndex);
            }
            bitIndex++;
        }
        encoded += encoding.toChar();
        return encoded;
    }


    private int buildTreeFromFile(String pathName, Charset charset) throws IOException {
        File file = new File(pathName);
        List<Boolean> encodedAsBooleans = new ArrayList<Boolean>();
        InputStreamReader reader = new InputStreamReader(new FileInputStream(file), charset);
        int characterCode;
        int needToRead = 1;
        int index = 0;
        boolean readingCharacter = false;
        encodingSize = (byte) reader.read();
        int bytesRead = 1;

        while (true) {
            for (int i = 0; i < Math.ceil((float) needToRead / 8); i++) {
                if ((characterCode = reader.read()) == -1) {
                    throw new InvalidFileContentException("Tree is not encoded properly in the file");
                }
                HuffByte huffByte = new HuffByte((char) characterCode);
                bytesRead++;
                huffByte.addToListAsBooleans(encodedAsBooleans);
            }
            if (readingCharacter) {
                Character c = readCharFromBooleanList(encodedAsBooleans, index);
                stack.push(new Node(c, 0));
                index += encodingSize;
                needToRead = index == encodedAsBooleans.size() ? 1 : 0;
                readingCharacter = false;
            } else if (encodedAsBooleans.get(index++)) {
                needToRead = Math.max(encodingSize - encodedAsBooleans.size() + index, 0);
                readingCharacter = true;
            } else {
                if (stack.size() == 1) {
                    root = stack.pop();
                    reader.close();
                    return bytesRead;
                }
                Node newNode = new Node(0);
                Node rightChild = stack.pop();
                Node leftChild = stack.pop();
                newNode.setLeft(leftChild);
                newNode.setRight(rightChild);
                stack.push(newNode);
                needToRead = index == encodedAsBooleans.size() ? 1 : 0;
            }
        }
    }

    private Character readCharFromBooleanList(List<Boolean> encodedAsBooleans, int index) {
        int code = 0;
        for (int i = index; i < index + encodingSize; i++) {
            if (encodedAsBooleans.get(i)) {
                code += Math.pow((double) 2, (double) (encodingSize - 1 + index - i));
            }
        }
        return (char) code;
    }

    private void traversePostOrder(Node node, List<Boolean> resultList) {
        if (node == null) {
            return;
        }
        traversePostOrder(node.getLeft(), resultList);
        traversePostOrder(node.getRight(), resultList);
        if (!node.isLeaf()) {
            resultList.add(Boolean.FALSE);
        } else {
            resultList.add(Boolean.TRUE);
            char character = node.getCharacter();
            char characterCopy = character;
            List<Boolean> characterBitsReversed = new ArrayList<Boolean>();

            for (int i = 0; i < encodingSize; i++) {
                if ((characterCopy & 1) == 1) {
                    characterBitsReversed.add(Boolean.TRUE);
                } else {
                    characterBitsReversed.add(Boolean.FALSE);
                }
                characterCopy = (char) (characterCopy >> 1);
            }
            if (characterCopy != 0) {
                throw new IllegalStateException(String.format("Character with code %d cannot encoded with encoding length %d", (int) character, this.encodingSize));

            }
            Collections.reverse(characterBitsReversed);
            resultList.addAll(characterBitsReversed);
        }
    }

    private void initializeQueue() {
        NodeComparator comparator = new NodeComparator();
        queue = new PriorityQueue<>(comparator);
    }

    private void mergeNodes() {
        while (queue.size() >= 2) {
            Node n1 = queue.poll();
            Node n2 = queue.poll();
            int countSum = n1.getCount() + n2.getCount();
            Node newNode = new Node(countSum);
            newNode.setLeft(n1);
            newNode.setRight(n2);
            queue.add(newNode);
        }
        root = queue.poll();
    }

    private void getCode(Node node, ArrayList<Boolean> code) {
        if (node == null) {
            return;
        } else if (node.isLeaf()) {
            for (Boolean b : code) {
                codes.addToCode(node.getCharacter(), b);
            }
            return;
        }
        ArrayList<Boolean> newCode = new ArrayList<Boolean>(code);
        newCode.add(Boolean.FALSE);
        getCode(node.getLeft(), newCode);

        newCode = new ArrayList<Boolean>(code);
        newCode.add(Boolean.TRUE);
        getCode(node.getRight(), newCode);
    }

}