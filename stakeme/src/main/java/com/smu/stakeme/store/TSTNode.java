package com.smu.stakeme.store;

/**
 * Created by jitheshrajan on 11/20/18.
 */

import java.util.Set;

/** class TSTNode **/
/**
 * Ensure T is a class which overrides the default functionality of equals and hashcode.
 * This will make sure you dont have duplicate objects stored in your tree!
 * */
class TSTNode<T>
{
    char data;
    boolean isEnd;
    TSTNode left, middle, right;
    Set<T> storedVal;

    /** Constructor **/
    public TSTNode(char data)
    {
        this.data = data;
        this.isEnd = false;
        this.storedVal = null;

        this.left = null;
        this.middle = null;
        this.right = null;
    }
}