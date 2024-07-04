package SpellCheckingAndAutoComplete;

import javax.swing.DefaultListModel;

public class MBK_ZNK_BinaryST<T extends Comparable<T>> {

    private Node root;

    public static class Node<T> {

        T word;
        Node<T> left;
        Node<T> right;

        Node(T word) {
            this.word = word;
            this.left = null;
            this.right = null;
        }

        public T getWord() {
            return word;
        }
    }

    public void addWord(T word) {
        root = addWord(root, word);
    }

    private Node<T> addWord(Node<T> node, T word) {
        if (node == null) {
            return new Node<>(word);
        }

        int comparison = word.compareTo(node.word);
        if (comparison < 0) {
            node.left = addWord(node.left, word);
        } else if (comparison > 0) {
            node.right = addWord(node.right, word);
        }
        return node;
    }

    public boolean searchWord(T word) {
        return searchWord(root, word);
    }

    private boolean searchWord(Node<T> node, T word) {
        if (node == null) {
            return false;
        }
        int comparison = word.compareTo(node.word);
        if (comparison < 0) {
            return searchWord(node.left, word);
        } else if (comparison > 0) {
            return searchWord(node.right, word);
        } else {
            return true;
        }
    }

    public Node getRoot() {
        return root;
    }

    public MBK_ZNK_BinaryST<T> suggestionTree(MBK_ZNK_BinaryST<T> wTree, String word) {
        MBK_ZNK_BinaryST<T> sTree = new MBK_ZNK_BinaryST<>();
        int a = distance((String) wTree.root.word, word);
        findNodesWithLengthDifference(sTree, wTree.root, word, a);
        return sTree;
    }

    public static int distance(String str1, String str2) {
        str1 = str1.toLowerCase();
        str2 = str2.toLowerCase();
        int[][] matrix = new int[str1.length() + 1][str2.length() + 1];
        for (int i = 0; i <= str1.length(); i++) {
            matrix[i][0] = i;
        }
        for (int j = 0; j <= str2.length(); j++) {
            matrix[0][j] = j;
        }
        for (int i = 1; i <= str1.length(); i++) {
            for (int j = 1; j <= str2.length(); j++) {
                int cost = (str1.charAt(i - 1) == str2.charAt(j - 1)) ? 0 : 1;
                matrix[i][j] = Math.min(matrix[i - 1][j] + 1, Math.min(matrix[i][j - 1] + 1, matrix[i - 1][j - 1] + cost));
            }
        }
        return matrix[str1.length()][str2.length()];
    }

    public void findNodesWithLengthDifference(MBK_ZNK_BinaryST<T> bts, Node current, String value, int lengthDiff) {
        if (current == null) {
            return;
        }
        lengthDiff = distance((String) current.word, value);
        String s = (String) current.word;
        if (Math.abs(s.length() - value.length()) == lengthDiff) {
            bts.addWord((T) current.word);
        }

        if (value.compareTo((String) current.word) < 0) {
            findNodesWithLengthDifference(bts, current.left, value, lengthDiff);
        } else if (value.compareTo((String) current.word) > 0) {
            findNodesWithLengthDifference(bts, current.right, value, lengthDiff);
        }
    }

    public void fillListModelInOrder(DefaultListModel<T> listModel) {
        fillListModelInOrder(root, listModel);
    }

    private void fillListModelInOrder(Node<T> node, DefaultListModel<T> listModel) {
        if (node != null) {
            fillListModelInOrder(node.left, listModel);
            listModel.addElement(node.word);
            fillListModelInOrder(node.right, listModel);
        }
    }
}
