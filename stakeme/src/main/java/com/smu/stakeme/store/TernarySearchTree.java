package com.smu.stakeme.store;

/**
 * Created by jitheshrajan on 11/20/18.
 */

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/** class TernarySearchTree **/
class TernarySearchTree<T>
{
    private TSTNode<T> root;
    private ArrayList<String> al;

    /** Constructor **/
    public TernarySearchTree()
    {
        root = null;
    }
    /** function to check if empty **/
    public boolean isEmpty()
    {
        return root == null;
    }
    /** function to clear **/
    public void makeEmpty()
    {
        root = null;
    }
    /** function to insert for a word **/
    public void insert(String word, T storedVal)
    {
        root = insert(root, word.toCharArray(), 0, storedVal);
    }
    /** function to insert for a word **/
    public TSTNode<T> insert(TSTNode<T> r, char[] word, int ptr, T storedVal)
    {
        if (r == null)
            r = new TSTNode(word[ptr]);

        if (word[ptr] < r.data)
            r.left = insert(r.left, word, ptr, storedVal);
        else if (word[ptr] > r.data)
            r.right = insert(r.right, word, ptr, storedVal);
        else
        {
            if (ptr + 1 < word.length)
                r.middle = insert(r.middle, word, ptr + 1, storedVal);
            else{
                r.isEnd = true;
                // add to node!
                if (r.storedVal == null) {
                    r.storedVal = new HashSet<T>();
                    r.storedVal.add(storedVal);
                }
            }
        }
        return r;
    }

    /** function to delete a word **/
    public void delete(String word, T storedVal)
    {
        delete(root, word.toCharArray(), 0, storedVal);
    }
    /** function to delete a word **/
    private void delete(TSTNode r, char[] word, int ptr, T storedVal)
    {
        if (r == null)
            return;

        if (word[ptr] < r.data)
            delete(r.left, word, ptr, storedVal);
        else if (word[ptr] > r.data)
            delete(r.right, word, ptr, storedVal);
        else
        {
            /** to delete a word just make isEnd false **/
            if (r.isEnd && ptr == word.length - 1){
                r.isEnd = false;
                if(r.storedVal != null){
                    r.storedVal.remove(storedVal); // is this efficient?
                }
            }

            else if (ptr + 1 < word.length)
                delete(r.middle, word, ptr + 1,storedVal);
        }
    }

    /** function to search for a word **/
    public boolean search(String word)
    {
        return search(root, word.toCharArray(), 0);
    }

    /** function to search for a word **/
    public void searchAndPrint(String word)
    {

        TSTNode tstNode = searchAndReturnNode(root, word.toCharArray(), 0, null);
        if(tstNode!=null){
            List<String> strings = printTree(tstNode);
            strings.stream().forEach(s -> System.out.println(s.equalsIgnoreCase(word) ? s : word + s));
        }
    }

    /** function to search for a word **/
    public List<String> searchAndReturn(String word)
    {
        List<String> returnList = new ArrayList<>();

        TSTNode newRoot = null;
        TSTNode tstNode = searchAndReturnNode(root, word.toCharArray(), 0, newRoot);
        if(tstNode!=null){
            List<String> strings = printTree(tstNode);
            strings.stream().forEach(s -> returnList.add(s.equalsIgnoreCase(word) ? s : word + s));
        }

//        if(newRoot!=null){
//            List<String> strings = printTree(tstNode);
//            strings.stream().forEach(s -> returnList.add(s.equalsIgnoreCase(word) ? s : word + s));
//        }

        return returnList;
    }

    public List<T> searchAndReturnObjects(String word)
    {
        List<T> returnList = new ArrayList<>();
        TSTNode newRoot = null;
        TSTNode tstNode = searchAndReturnNode(root, word.toCharArray(), 0, newRoot);
        if(tstNode!=null){
//            return getStoredObjectsFromTree(tstNode);
            return getStoredObjectsFromTree(tstNode);

//            strings.stream().forEach(s -> returnList.add(s.equalsIgnoreCase(word) ? s : word + s));
        }

        return returnList;
    }


    /** function to search for a word **/
    private boolean search(TSTNode r, char[] word, int ptr)
    {
        if (r == null)
            return false;

        if (word[ptr] < r.data)
            return search(r.left, word, ptr);
        else if (word[ptr] > r.data)
            return search(r.right, word, ptr);
        else
        {
            if (r.isEnd && ptr == word.length - 1)
                return true;
            else if (ptr == word.length - 1)
                return false;
            else
                return search(r.middle, word, ptr + 1);
        }
    }

    /** function to search for a word **/
    private TSTNode searchAndReturnNode(TSTNode r, char[] word, int ptr, TSTNode newRoot)
    {
        if (r == null)
            return r;

        if (word[ptr] < r.data)
            return searchAndReturnNode(r.left, word, ptr, newRoot);
        else if (word[ptr] > r.data)
            return searchAndReturnNode(r.right, word, ptr, newRoot);
        else
        {
            if (r.isEnd && ptr == word.length - 1){
                System.out.println("Exact match found!"+word);
//                return true;
                return r;
            }
            else if (ptr == word.length - 1){
                System.out.println("Searched word is a prefix");
                newRoot = r;
                return newRoot.middle;
            }
            else{
                return searchAndReturnNode(r.middle, word, ptr + 1, newRoot);
            }
        }
    }

    /** function to print tree **/
    public String toString()
    {
        List<String> tempList = new ArrayList<>();
        traverseAndRetrieveKeys(root, "", tempList);
        return "\nTernary Search Tree : "+ tempList;
    }
    /** function to traverse tree **/
    private void traverse(TSTNode r, String str)
    {

        if (r != null)
        {
            System.out.println("Traversing : "+r.data);
            traverse(r.left, str);

            str = str + r.data;
            if (r.isEnd)
                al.add(str);

            traverse(r.middle, str);
            str = str.substring(0, str.length() - 1);

            traverse(r.right, str);
        }
    }

    /** function to traverse tree **/
    private void traverseAndRetrieveKeys(TSTNode r, String str, List<String> strings)
    {

        if (r != null)
        {
            System.out.println("Traversing : "+r.data);
            traverseAndRetrieveKeys(r.left, str, strings);

            str = str + r.data;
            if (r.isEnd)
                strings.add(str);

            traverseAndRetrieveKeys(r.middle, str, strings);
            str = str.substring(0, str.length() - 1);

            traverseAndRetrieveKeys(r.right, str, strings);
        }
    }

    /** function to traverse tree **/
    private void traverseAndRetrieveObjects(TSTNode r, String str, List<T> objects)
    {

        if (r != null)
        {
            System.out.println("Traversing : "+r.data);
            traverseAndRetrieveObjects(r.left, str, objects);

            str = str + r.data;
            if (r.isEnd){
                System.out.println(str);
                objects.addAll(r.storedVal);
            }

            traverseAndRetrieveObjects(r.middle, str, objects);
            str = str.substring(0, str.length() - 1);

            traverseAndRetrieveObjects(r.right, str, objects);
        }
    }


    public List<String> printTree(TSTNode newRoot){
        System.out.println("Printing Tree");
        List<String> temp = new ArrayList<>();
        traverseAndRetrieveKeys(newRoot, "",temp);
        return temp;
    }

    public List<T> getStoredObjectsFromTree(TSTNode node){
        System.out.println("Fetching objects");
        List<T> temp = new ArrayList<>();
        traverseAndRetrieveObjects(node,"", temp);
        return temp;
    }


}
