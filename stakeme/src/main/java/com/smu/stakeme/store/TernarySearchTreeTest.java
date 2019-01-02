package com.smu.stakeme.store;

/**
 * Created by jitheshrajan on 11/20/18.
 ** Java Program to Implement Ternary Search Tree
 **/

import java.util.Scanner;


/** class TernarySearchTree **/
public class TernarySearchTreeTest
{
    public static void main(String[] args)
    {
        Scanner scan = new Scanner(System.in);

        /* Creating object of TernarySearchTree */
        TernarySearchTree<String> tst = new TernarySearchTree<String>();
        System.out.println("Ternary Search Tree Test\n");

        char ch;
        /*  Perform tree operations  */
        do
        {
            System.out.println("\nTernary Search Tree Operations\n");
            System.out.println("1. insert word");
            System.out.println("2. search word");
            System.out.println("3. delete word");
            System.out.println("4. check empty");
            System.out.println("5. make empty");

            int choice = scan.nextInt();
            switch (choice)
            {
                case 1 :
                    System.out.println("Enter word to insert");
                    String word = scan.next();
                    for(String splitString : word.split(",")){
                        tst.insert(splitString.trim(), splitString.trim());
                    }
//                    tst.insert(  );
                    break;
                case 2 :
                    System.out.println("Enter word to search");
                    String s = scan.next();
                    System.out.println("Search result : "+ tst.search( s ));
//                    tst.searchAndPrint(s);
                    for( String matchedString : tst.searchAndReturnObjects(s)){
                        System.out.println("Matched 1 : "+matchedString);
                    }
                    break;
                case 3 :
                    System.out.println("Enter word to delete");
                    String textToDelete = scan.next();
                    tst.delete( textToDelete , textToDelete);
                    break;
                case 4 :
                    System.out.println("Empty Status : "+ tst.isEmpty() );
                    break;
                case 5 :
                    System.out.println("Ternary Search Tree cleared");
                    tst.makeEmpty();
                    break;
                default :
                    System.out.println("Wrong Entry \n ");
                    break;
            }
            System.out.println(tst);

            System.out.println("\nDo you want to continue (Type y or n) \n");
            ch = scan.next().charAt(0);
        } while (ch == 'Y'|| ch == 'y');
    }
}
