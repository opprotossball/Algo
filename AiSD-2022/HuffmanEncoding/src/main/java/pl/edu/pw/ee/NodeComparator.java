package pl.edu.pw.ee;

import java.util.Comparator;

public class NodeComparator implements Comparator<Node> {

    @Override
    public int compare(Node n1, Node n2) {
        int diff = n1.getCount() - n2.getCount();
        if (diff == 0) {
            if (n1.isLeaf() && n2.isLeaf()) {
                return 0;
            }
            return n1.isLeaf() ? -1 : 1;
        } else {
            return diff;
        }
    }

}
