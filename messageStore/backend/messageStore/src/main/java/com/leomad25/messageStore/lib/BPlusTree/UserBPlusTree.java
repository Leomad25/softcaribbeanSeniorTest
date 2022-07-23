package com.leomad25.messageStore.lib.BPlusTree;

import com.leomad25.messageStore.models.UserModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class UserBPlusTree {
    int m;
    InternalNode root;
    LeafNode firstLeaf;

    private int binarySearch(DictionaryPair[] dps, int numPairs, long t) {
        Comparator<DictionaryPair> c = new Comparator<DictionaryPair>() {
            @Override
            public int compare(DictionaryPair o1, DictionaryPair o2) {
                Long a = Long.valueOf(o1.key);
                Long b = Long.valueOf(o2.key);
                return a.compareTo(b);
            }
        };
        return Arrays.binarySearch(dps, 0, numPairs, new DictionaryPair(t, new UserModel()), c);
    }

    private LeafNode findLeafNode(long key) {

        Long[] keys = this.root.keys;
        int i;

        for (i = 0; i < this.root.degree - 1; i++) {
            if (key < keys[i]) { break; }
        }

        Node child = this.root.childPointers[i];
        if (child instanceof LeafNode) {
            return (LeafNode)child;
        } else {
            return findLeafNode((InternalNode)child, key);
        }
    }

    private LeafNode findLeafNode(InternalNode node, long key) {

        Long[] keys = node.keys;
        int i;

        for (i = 0; i < node.degree - 1; i++) {
            if (key < keys[i]) { break; }
        }

        Node childNode = node.childPointers[i];
        if (childNode instanceof LeafNode) {
            return (LeafNode)childNode;
        } else {
            return findLeafNode((InternalNode)node.childPointers[i], key);
        }
    }

    private int findIndexOfPointer(Node[] pointers, LeafNode node) {
        int i;
        for (i = 0; i < pointers.length; i++) {
            if (pointers[i] == node) { break; }
        }
        return i;
    }

    private long getMidpoint() {
        return (long)Math.ceil((this.m + 1) / 2.0) - 1;
    }

    private void handleDeficiency(InternalNode in) {

        InternalNode sibling;
        InternalNode parent = in.parent;

        if (this.root == in) {
            for (int i = 0; i < in.childPointers.length; i++) {
                if (in.childPointers[i] != null) {
                    if (in.childPointers[i] instanceof InternalNode) {
                        this.root = (InternalNode)in.childPointers[i];
                        this.root.parent = null;
                    } else if (in.childPointers[i] instanceof LeafNode) {
                        this.root = null;
                    }
                }
            }
        }

        else if (in.leftSibling != null && in.leftSibling.isLendable()) {
            sibling = in.leftSibling;
        } else if (in.rightSibling != null && in.rightSibling.isLendable()) {
            sibling = in.rightSibling;

            long borrowedKey = sibling.keys[0];
            Node pointer = sibling.childPointers[0];

            in.keys[in.degree - 1] = parent.keys[0];
            in.childPointers[in.degree] = pointer;

            parent.keys[0] = borrowedKey;

            sibling.removePointer(0);
            Arrays.sort(sibling.keys);
            sibling.removePointer(0);
            shiftDown(in.childPointers, 1);
        }

        else if (in.leftSibling != null && in.leftSibling.isMergeable()) {

        } else if (in.rightSibling != null && in.rightSibling.isMergeable()) {
            sibling = in.rightSibling;

            sibling.keys[sibling.degree - 1] = parent.keys[parent.degree - 2];
            Arrays.sort(sibling.keys, 0, sibling.degree);
            parent.keys[parent.degree - 2] = null;

            for (int i = 0; i < in.childPointers.length; i++) {
                if (in.childPointers[i] != null) {
                    sibling.prependChildPointer(in.childPointers[i]);
                    in.childPointers[i].parent = sibling;
                    in.removePointer(i);
                }
            }

            parent.removePointer(in);

            sibling.leftSibling = in.leftSibling;
        }

        if (parent != null && parent.isDeficient()) {
            handleDeficiency(parent);
        }
    }

    private boolean isEmpty() {
        return firstLeaf == null;
    }


    private int linearNullSearch(DictionaryPair[] dps) {
        for (int i = 0; i <  dps.length; i++) {
            if (dps[i] == null) { return i; }
        }
        return -1;
    }

    private int linearNullSearch(Node[] pointers) {
        for (int i = 0; i <  pointers.length; i++) {
            if (pointers[i] == null) { return i; }
        }
        return -1;
    }

    private void shiftDown(Node[] pointers, int amount) {
        Node[] newPointers = new Node[this.m + 1];
        for (int i = amount; i < pointers.length; i++) {
            newPointers[i - amount] = pointers[i];
        }
        pointers = newPointers;
    }

    private void sortDictionary(DictionaryPair[] dictionary) {
        Arrays.sort(dictionary, new Comparator<DictionaryPair>() {
            @Override
            public int compare(DictionaryPair o1, DictionaryPair o2) {
                if (o1 == null && o2 == null) { return 0; }
                if (o1 == null) { return 1; }
                if (o2 == null) { return -1; }
                return o1.compareTo(o2);
            }
        });
    }

    private Node[] splitChildPointers(InternalNode in, long split) {

        Node[] pointers = in.childPointers;
        Node[] halfPointers = new Node[this.m + 1];

        for (int i = (int) (split + 1); i < pointers.length; i++) {
            halfPointers[(int) (i - split - 1)] = pointers[i];
            in.removePointer(i);
        }

        return halfPointers;
    }

    private DictionaryPair[] splitDictionary(LeafNode ln, long split) {

        DictionaryPair[] dictionary = ln.dictionary;

        DictionaryPair[] halfDict = new DictionaryPair[this.m];

        for (long i = split; i < dictionary.length; i++) {
            halfDict[(int) (i - split)] = dictionary[(int) i];
            ln.delete(i);
        }

        return halfDict;
    }

    private void splitInternalNode(InternalNode in) {

        InternalNode parent = in.parent;

        long midpoint = getMidpoint();
        long newParentKey = in.keys[(int) midpoint];
        Long[] halfKeys = splitKeys(in.keys, midpoint);
        Node[] halfPointers = splitChildPointers(in, midpoint);

        in.degree = linearNullSearch(in.childPointers);

        InternalNode sibling = new InternalNode(this.m, halfKeys, halfPointers);
        for (Node pointer : halfPointers) {
            if (pointer != null) { pointer.parent = sibling; }
        }

        sibling.rightSibling = in.rightSibling;
        if (sibling.rightSibling != null) {
            sibling.rightSibling.leftSibling = sibling;
        }
        in.rightSibling = sibling;
        sibling.leftSibling = in;

        if (parent == null) {

            // Create new root node and add midpoint key and pointers
            Long[] keys = new Long[this.m];
            keys[0] = newParentKey;
            InternalNode newRoot = new InternalNode(this.m, keys);
            newRoot.appendChildPointer(in);
            newRoot.appendChildPointer(sibling);
            this.root = newRoot;

            in.parent = newRoot;
            sibling.parent = newRoot;

        } else {

            parent.keys[parent.degree - 1] = newParentKey;
            Arrays.sort(parent.keys, 0, parent.degree);

            int pointerIndex = parent.findIndexOfPointer(in) + 1;
            parent.insertChildPointer(sibling, pointerIndex);
            sibling.parent = parent;
        }
    }

    private Long[] splitKeys(Long[] keys, long split) {

        Long[] halfKeys = new Long[this.m];

        keys[(int) split] = null;

        for (long i = split + 1; i < keys.length; i++) {
            halfKeys[(int) (i - split - 1)] = keys[(int) i];
            keys[(int) i] = null;
        }

        return halfKeys;
    }

    /*~~~~~~~~~~~~~~~~ API: DELETE, INSERT, SEARCH ~~~~~~~~~~~~~~~~*/

    public void delete(long key) {
        if (isEmpty()) {

            System.err.println("Invalid Delete: The B+ tree is currently empty.");

        } else {

            LeafNode ln = (this.root == null) ? this.firstLeaf : findLeafNode(key);
            int dpIndex = binarySearch(ln.dictionary, ln.numPairs, key);


            if (dpIndex < 0) {


                System.err.println("Invalid Delete: Key unable to be found.");

            } else {

                ln.delete(dpIndex);

                if (ln.isDeficient()) {

                    LeafNode sibling;
                    InternalNode parent = ln.parent;

                    if (ln.leftSibling != null &&
                            ln.leftSibling.parent == ln.parent &&
                            ln.leftSibling.isLendable()) {

                        sibling = ln.leftSibling;
                        DictionaryPair borrowedDP = sibling.dictionary[sibling.numPairs - 1];

                        ln.insert(borrowedDP);
                        sortDictionary(ln.dictionary);
                        sibling.delete(sibling.numPairs - 1);


                        int pointerIndex = findIndexOfPointer(parent.childPointers, ln);
                        if (!(borrowedDP.key >= parent.keys[pointerIndex - 1])) {
                            parent.keys[pointerIndex - 1] = ln.dictionary[0].key;
                        }

                    } else if (ln.rightSibling != null &&
                            ln.rightSibling.parent == ln.parent &&
                            ln.rightSibling.isLendable()) {

                        sibling = ln.rightSibling;
                        DictionaryPair borrowedDP = sibling.dictionary[0];

                        ln.insert(borrowedDP);
                        sibling.delete(0);
                        sortDictionary(sibling.dictionary);

                        int pointerIndex = findIndexOfPointer(parent.childPointers, ln);
                        if (!(borrowedDP.key < parent.keys[pointerIndex])) {
                            parent.keys[pointerIndex] = sibling.dictionary[0].key;
                        }

                    }

                    else if (ln.leftSibling != null &&
                            ln.leftSibling.parent == ln.parent &&
                            ln.leftSibling.isMergeable()) {

                        sibling = ln.leftSibling;
                        int pointerIndex = findIndexOfPointer(parent.childPointers, ln);

                        parent.removeKey(pointerIndex - 1);
                        parent.removePointer(ln);

                        sibling.rightSibling = ln.rightSibling;

                        if (parent.isDeficient()) {
                            handleDeficiency(parent);
                        }

                    } else if (ln.rightSibling != null &&
                            ln.rightSibling.parent == ln.parent &&
                            ln.rightSibling.isMergeable()) {

                        sibling = ln.rightSibling;
                        int pointerIndex = findIndexOfPointer(parent.childPointers, ln);

                        parent.removeKey(pointerIndex);
                        parent.removePointer(pointerIndex);

                        sibling.leftSibling = ln.leftSibling;
                        if (sibling.leftSibling == null) {
                            firstLeaf = sibling;
                        }

                        if (parent.isDeficient()) {
                            handleDeficiency(parent);
                        }
                    }

                } else if (this.root == null && this.firstLeaf.numPairs == 0) {

                    this.firstLeaf = null;

                } else {

                    sortDictionary(ln.dictionary);

                }
            }
        }
    }

    public void insert(long key, UserModel value){
        if (isEmpty()) {

            LeafNode ln = new LeafNode(this.m, new DictionaryPair(key, value));

            this.firstLeaf = ln;

        } else {

            LeafNode ln = (this.root == null) ? this.firstLeaf :
                    findLeafNode(key);

            if (!ln.insert(new DictionaryPair(key, value))) {

                ln.dictionary[ln.numPairs] = new DictionaryPair(key, value);
                ln.numPairs++;
                sortDictionary(ln.dictionary);

                long midpoint = getMidpoint();
                DictionaryPair[] halfDict = splitDictionary(ln, midpoint);

                if (ln.parent == null) {

                    Long[] parent_keys = new Long[this.m];
                    parent_keys[0] = halfDict[0].key;
                    InternalNode parent = new InternalNode(this.m, parent_keys);
                    ln.parent = parent;
                    parent.appendChildPointer(ln);

                } else {

                    long newParentKey = halfDict[0].key;
                    ln.parent.keys[ln.parent.degree - 1] = newParentKey;
                    Arrays.sort(ln.parent.keys, 0, ln.parent.degree);
                }

                LeafNode newLeafNode = new LeafNode(this.m, halfDict, ln.parent);

                int pointerIndex = ln.parent.findIndexOfPointer(ln) + 1;
                ln.parent.insertChildPointer(newLeafNode, pointerIndex);

                newLeafNode.rightSibling = ln.rightSibling;
                if (newLeafNode.rightSibling != null) {
                    newLeafNode.rightSibling.leftSibling = newLeafNode;
                }
                ln.rightSibling = newLeafNode;
                newLeafNode.leftSibling = ln;

                if (this.root == null) {

                    this.root = ln.parent;

                } else {

                    InternalNode in = ln.parent;
                    while (in != null) {
                        if (in.isOverfull()) {
                            splitInternalNode(in);
                        } else {
                            break;
                        }
                        in = in.parent;
                    }
                }
            }
        }
    }

    public UserModel search(long key) {

        if (isEmpty()) { return null; }

        LeafNode ln = (this.root == null) ? this.firstLeaf : findLeafNode(key);

        DictionaryPair[] dps = ln.dictionary;
        int index = binarySearch(dps, ln.numPairs, key);

        if (index < 0) {
            return null;
        } else {
            return dps[index].value;
        }
    }

    public ArrayList<UserModel> search(long lowerBound, long upperBound) {

        ArrayList<UserModel> values = new ArrayList<>();

        LeafNode currNode = this.firstLeaf;
        while (currNode != null) {

            DictionaryPair dps[] = currNode.dictionary;
            for (DictionaryPair dp : dps) {

                if (dp == null) { break; }

                if (lowerBound <= dp.key && dp.key <= upperBound) {
                    values.add(dp.value);
                }
            }

            currNode = currNode.rightSibling;

        }

        return values;
    }

    public UserBPlusTree(int m) {
        this.m = m;
        this.root = null;
    }

    public class Node {
        InternalNode parent;
    }

    private class InternalNode extends Node {
        int maxDegree;
        int minDegree;
        int degree;
        InternalNode leftSibling;
        InternalNode rightSibling;
        Long[] keys;
        Node[] childPointers;

        private void appendChildPointer(Node pointer) {
            this.childPointers[degree] = pointer;
            this.degree++;
        }

        private int findIndexOfPointer(Node pointer) {
            for (int i = 0; i < childPointers.length; i++) {
                if (childPointers[i] == pointer) { return i; }
            }
            return -1;
        }

        private void insertChildPointer(Node pointer, long index) {
            for (int i = degree - 1; i >= index ;i--) {
                childPointers[i + 1] = childPointers[i];
            }
            this.childPointers[(int) index] = pointer;
            this.degree++;
        }

        private boolean isDeficient() {
            return this.degree < this.minDegree;
        }

        private boolean isLendable() { return this.degree > this.minDegree; }

        private boolean isMergeable() { return this.degree == this.minDegree; }

        private boolean isOverfull() {
            return this.degree == maxDegree + 1;
        }

        private void prependChildPointer(Node pointer) {
            for (int i = degree - 1; i >= 0 ;i--) {
                childPointers[i + 1] = childPointers[i];
            }
            this.childPointers[0] = pointer;
            this.degree++;
        }

        private void removeKey(long index) { this.keys[(int) index] = null; }

        private void removePointer(long index) {
            this.childPointers[(int) index] = null;
            this.degree--;
        }

        private void removePointer(Node pointer) {
            for (int i = 0; i < childPointers.length; i++) {
                if (childPointers[i] == pointer) { this.childPointers[i] = null; }
            }
            this.degree--;
        }

        private InternalNode(int m, Long[] keys) {
            this.maxDegree = m;
            this.minDegree = (int)Math.ceil(m/2.0);
            this.degree = 0;
            this.keys = keys;
            this.childPointers = new Node[this.maxDegree+1];
        }

        private InternalNode(int m, Long[] keys, Node[] pointers) {
            this.maxDegree = m;
            this.minDegree = (int)Math.ceil(m/2.0);
            this.degree = linearNullSearch(pointers);
            this.keys = keys;
            this.childPointers = pointers;
        }
    }

    public class LeafNode extends Node {
        int maxNumPairs;
        int minNumPairs;
        int numPairs;
        LeafNode leftSibling;
        LeafNode rightSibling;
        DictionaryPair[] dictionary;

        public void delete(long index) {

            // Delete dictionary pair from leaf
            this.dictionary[(int) index] = null;

            // Decrement numPairs
            numPairs--;
        }

        public boolean insert(DictionaryPair dp) {
            if (this.isFull()) {

                /* Flow of execution goes here when numPairs == maxNumPairs */

                return false;
            } else {

                // Insert dictionary pair, increment numPairs, sort dictionary
                this.dictionary[numPairs] = dp;
                numPairs++;
                Arrays.sort(this.dictionary, 0, numPairs);

                return true;
            }
        }

        public boolean isDeficient() { return numPairs < minNumPairs; }

        public boolean isFull() { return numPairs == maxNumPairs; }

        public boolean isLendable() { return numPairs > minNumPairs; }

        public boolean isMergeable() {
            return numPairs == minNumPairs;
        }

        public LeafNode(int m, DictionaryPair dp) {
            this.maxNumPairs = m - 1;
            this.minNumPairs = (int)(Math.ceil(m/2) - 1);
            this.dictionary = new DictionaryPair[m];
            this.numPairs = 0;
            this.insert(dp);
        }

        public LeafNode(int m, DictionaryPair[] dps, InternalNode parent) {
            this.maxNumPairs = m - 1;
            this.minNumPairs = (int)(Math.ceil(m/2) - 1);
            this.dictionary = dps;
            this.numPairs = linearNullSearch(dps);
            this.parent = parent;
        }
    }

    public class DictionaryPair implements Comparable<DictionaryPair> {
        long key;
        UserModel value;

        public DictionaryPair(long key, UserModel value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public int compareTo(DictionaryPair o) {
            if (key == o.key) { return 0; }
            else if (key > o.key) { return 1; }
            else { return -1; }
        }
    }
}