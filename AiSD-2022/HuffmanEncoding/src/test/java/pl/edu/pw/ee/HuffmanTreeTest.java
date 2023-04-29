package pl.edu.pw.ee;

import org.junit.Test;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;

public class HuffmanTreeTest {
    private HuffmanTree tree;
    private static final Charset CHARSET = StandardCharsets.ISO_8859_1;


    @Test
    public void buildsTreeFromFileCorrectly() throws IOException {
        // given
        tree = new HuffmanTree();
        File treeEncoded = new File("src/test/resources/HuffmanTreeTest/treeEncoded.txt");
        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(treeEncoded), CHARSET);
        writer.write((char) 8);
        writer.write((char) 176);
        writer.write((char) 218);
        writer.write((char) 192);
        writer.close();

        //when
        tree.buildTreeFromFileHeader("src/test/resources/HuffmanTreeTest/treeEncoded.txt", CHARSET);
        char leftChar = tree.getRoot().getLeft().getCharacter();
        char rightChar = tree.getRoot().getRight().getCharacter();

        //then
        assertEquals('a', leftChar);
        assertEquals('k', rightChar);
    }

    @Test
    public void shouldEncodeTreeProperly() {
        //given
        Node root = new Node(7);
        root.setLeft(new Node('a', 3));
        root.setRight(new Node('k', 4));
        int[] expectedCodes = {8, 176, 218, 192};

        //when
        tree = new HuffmanTree(root, 8);
        String result = tree.encodeTree();

        //then
        int i = 0;
        for (int code : expectedCodes) {
            assertEquals(code, (int) result.charAt(i++));
        }
    }

    @Test
    public void buildsTreeFromAddedNodesAndAssignsCorrectCodes() {
        //given
        tree = new HuffmanTree(8);
        char[] chars = {'a', 'b', 'e', 'f'};
        String[] expectedCodes = {"0", "101", "11", "100"};
        Node[] nodes = {new Node('a', 35), new Node('b', 18), new Node('e', 21), new Node('f', 2)};
        Dictionary codes;

        //when
        for (Node node : nodes) {
            tree.addNodeToQueue(node);
        }
        tree.buildTreeFromQueue();
        codes = tree.getCodes();

        //then
        for (int i = 0; i < chars.length; i++) {
            String expected = expectedCodes[i];
            String result = codes.getCodeAsString(chars[i]);
            assertEquals(expected, result);
        }
    }

    @Test(expected = RuntimeException.class)
    public void throwsCorrectExceptionWhenTreeIsNotEncodedProperlyInFile() {
        //given
        tree = new HuffmanTree(8);

        //when
        tree.buildTreeFromFileHeader("src/test/resources/HuffmanTreeTest/corruptedTree.brrt", CHARSET);

        //then
        assert (false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwsExceptionWhenCodeNotExistInDictionary() {
        //given
        tree = new HuffmanTree(8);
        Node[] nodes = {new Node('a', 35), new Node('b', 18), new Node('e', 21), new Node('f', 2)};
        Dictionary codes;

        //when
        for (Node node : nodes) {
            tree.addNodeToQueue(node);
        }
        tree.buildTreeFromQueue();
        codes = tree.getCodes();
        codes.getCode('y');

        //then
        assert (false);
    }

}
