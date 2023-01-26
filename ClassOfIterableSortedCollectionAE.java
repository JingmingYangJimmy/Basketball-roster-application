// --== CS400 Fall 2022 File Header Information ==--
// Name: <Jingming Yang>
// Email: <jyang668@wisc.edu>
// Team: <AI>
// TA: <Karan>
// Lecturer: <Gary Dahl>
// Notes to Grader: <I use the successor to deal with the node with two children, 
//and I write the remove and removeYear algorithm. The removeYear method can remove all of
//the Players having the specific year>
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Stack;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.Assert.assertTrue;

/**
 * simulate the tree property
 * @author yangjingming
 *
 * @param <T>
 */
public class ClassOfIterableSortedCollectionAE  <T extends Comparable<T>>    {



  protected static class Node<T> {
    public T data;
    public Node<T> parent; // null for root node
    public Node<T> leftChild;
    public Node<T> rightChild;
    public int blackHeight;// track the black height only for the current node: 0 = red, 1 = black,
    //and 2 = double-black.



    public Node(T data) { this.data = data; }



  }


  protected Node<Player>  root; // reference to root node of tree, null when empty
  protected int size = 0; // the number of values in the tree

















  /**
   * Performs a naive insertion into a binary search tree: adding the input
   * data value to a new node in a leaf position within the tree. After  
   * this insertion, no attempt is made to restructure or balance the tree.
   * This tree will not hold null references, nor duplicate data values.
   * @param data to be added into this binary search tree
   * @return true if the value was inserted, false if not
   * @throws NullPointerException when the provided data argument is null
   * @throws IllegalArgumentException when the newNode and subtree contain
   *      equal data references
   */
  public boolean insert(Player data) throws NullPointerException, IllegalArgumentException {
    // null references cannot be stored within this tree
    if(data == null) throw new NullPointerException(
        "This RedBlackTree cannot store null references.");

    Node<Player> newNode = new Node<>(data);
    if(root == null) { root = newNode; size++; root.blackHeight=1; return true; } // add first 
    //node to an empty tree
    else{
      boolean returnValue = insertHelper(newNode,root); // recursively insert into subtree
      if (returnValue) {size++;     }
      else throw new IllegalArgumentException(
          "This RedBlackTree already contains that value.");
      root.blackHeight=1;
      return returnValue;
    }
  }

  /**
   * Recursive helper method to find the subtree with a null reference in the
   * position that the newNode should be inserted, and then extend this tree
   * by the newNode in that position.
   * @param newNode is the new node that is being added to this tree
   * @param subtree is the reference to a node within this tree which the 
   *      newNode should be inserted as a descenedent beneath
   * @return true is the value was inserted in subtree, false if not
   */
  private boolean insertHelper(Node<Player> newNode, Node<Player> subtree) {
    int compare = newNode.data.compareTo(subtree.data);
    // do not allow duplicate values to be stored within this tree
    if(compare == 0) return false;

    // store newNode within left subtree of subtree
    else if(compare < 0) {
      if(subtree.leftChild == null) { // left subtree empty, add here
        subtree.leftChild = newNode;
        newNode.parent = subtree;
        enforceRBTreePropertiesAfterInsert (newNode);
        return true;
        // otherwise continue recursive search for location to insert
      } else return insertHelper(newNode, subtree.leftChild);
    }

    // store newNode within the right subtree of subtree
    else {
      if(subtree.rightChild == null) { // right subtree empty, add here
        subtree.rightChild = newNode;
        newNode.parent = subtree;
        enforceRBTreePropertiesAfterInsert (newNode);
        return true;
        // otherwise continue recursive search for location to insert
      } else return insertHelper(newNode, subtree.rightChild);
    }
  }

  /**
   * Performs the rotation operation on the provided nodes within this tree.
   * When the provided child is a leftChild of the provided parent, this
   * method will perform a right rotation. When the provided child is a
   * rightChild of the provided parent, this method will perform a left rotation.
   * When the provided nodes are not related in one of these ways, this method
   * will throw an IllegalArgumentException.
   * @param child is the node being rotated from child to parent position
   *      (between these two node arguments)
   * @param parent is the node being rotated from parent to child position
   *      (between these two node arguments)
   * @throws IllegalArgumentException when the provided child and parent
   *      node references are not initially (pre-rotation) related that way
   */
  private void rotate(Node<Player> child, Node<Player> parent) throws IllegalArgumentException {
    if(child==null)
      throw new IllegalArgumentException("no element");
    if(parent==null)
      throw new IllegalArgumentException("no element");
    if(!contains(child.data))
      throw new IllegalArgumentException("no element");
    if(!contains(parent.data))
      throw new IllegalArgumentException("no element");

    if(parent.leftChild==child) { //right rotation

      if(parent.parent==null) { //the parent is root
        if(child.rightChild==null) { //the child do not have right child

          child.rightChild=parent;
          parent.leftChild=null;
          child.parent=null;
          root=child;
          root.parent=null;
          parent.parent=child;
          return;
        }
        Node<Player>container=new Node<Player>(null);
        container=child.rightChild;
        child.rightChild=parent;

        parent.leftChild=container;
        container.parent=parent;
        root=child;
        root.parent=null;
        parent.parent=child;
        return;
      }

      if(parent.parent.leftChild==parent) {  //parent is a left child xx
        Node<Player>pparent=parent.parent; //express the parent's parent
        pparent.leftChild=child;
        if(child.rightChild==null){//child do not have the right child
          child.rightChild=parent;
          parent.leftChild=null;

          child.parent=pparent;//
          parent.parent=child;//
          return;
        }
        Node<Player>container1=child.rightChild;
        child.rightChild=parent;
        parent.leftChild=container1;
        container1.parent=parent;
        parent.parent=child;
        child.parent=pparent;
        return;
      }

      if(parent.parent.rightChild==parent) { //parent is a right child
        Node<Player>pparent1=parent.parent; //express the parent's parent
        if(child.rightChild==null) {
          pparent1.rightChild=child;
          child.rightChild=parent;
          parent.leftChild=null;
          parent.parent=child;
          child.parent=pparent1;
          return;
        }
        Node<Player>container2=child.rightChild;
        pparent1.rightChild=child;
        child.rightChild=parent;
        parent.leftChild=container2;
        container2.parent=parent;
        parent.parent=child;
        child.parent=pparent1;
        return;
      }
    }

    else if(parent.rightChild==child) { //left rotation

      if(parent.parent==null) { //the parent is root

        if(child.leftChild==null){
          child.leftChild=parent;
          root=child;
          root.parent=null;
          parent.rightChild=null;
          parent.parent=child;/////////
          return;
        }
        Node<Player>container3=child.leftChild;
        child.leftChild=parent;
        parent.rightChild=container3;
        root=child;
        root.parent=null;
        container3.parent=parent;
        parent.parent=child;
        return;
      }

      if(parent.parent.rightChild==parent) { //parent is the right child
        Node<Player> pparent5=parent.parent;

        if(child.leftChild==null){
          pparent5.rightChild=child;
          child.leftChild=parent;
          parent.rightChild=null;
          parent.parent=child;
          child.parent=pparent5;
          return;
        }
        pparent5.rightChild=child;
        Node<Player>container4=child.leftChild;
        child.leftChild=parent;
        parent.rightChild=container4;
        container4.parent=parent;
        parent.parent=child;
        child.parent=pparent5;
        return;       
      }

      if(parent.parent.leftChild==parent) { //parent is the left child x
        Node<Player> pparent4=parent.parent;


        if(child.leftChild==null) {

          pparent4.leftChild=child;
          child.leftChild=parent;
          parent.rightChild=null;
          parent.parent=child;
          child.parent=pparent4;
          return;
        }
        Node<Player>container5=child.leftChild;

        pparent4.leftChild=child;
        child.leftChild=parent;
        parent.rightChild=container5;
        container5.parent=parent;
        parent.parent=child;
        child.parent=pparent4;
        return;
      }
    }
    throw  new IllegalArgumentException("not th right way");

  }

  /**
   * Get the size of the tree (its number of nodes).
   * @return the number of nodes in the tree
   */
  public int size() {
    return size;
  }

  /**
   * Method to check if the tree is empty (does not contain any node).
   * @return true of this.size() return 0, false if this.size() > 0
   */
  public boolean isEmpty() {
    return this.size() == 0;
  }

  /**
   * Checks whether the tree contains the value *data*.
   * @param data the data value to test for
   * @return true if *data* is in the tree, false if it is not in the tree
   */
  public boolean contains(Player data) {
    // null references will not be stored within this tree
    if(data == null) throw new NullPointerException(
        "This RedBlackTree cannot store null references.");
    return this.containsHelper(data, root);
  }

  /**
   * Recursive helper method that recurses through the tree and looks
   * for the value *data*.
   * @param data the data value to look for
   * @param subtree the subtree to search through
   * @return true of the value is in the subtree, false if not
   */
  private boolean containsHelper(Player data, Node<Player> subtree) {
    if (subtree == null) {
      // we are at a null child, value is not in tree
      return false;
    } else {
      int compare = data.compareTo(subtree.data);
      if (compare < 0) {
        // go left in the tree
        return containsHelper(data, subtree.leftChild);
      } else if (compare > 0) {
        // go right in the tree
        return containsHelper(data, subtree.rightChild);
      } else {
        // we found it :)
        return true;
      }
    }
  }


  /**
   * This method performs an inorder traversal of the tree. The string 
   * representations of each data value within this tree are assembled into a
   * comma separated string within brackets (similar to many implementations 
   * of java.util.Collection, like java.util.ArrayList, LinkedList, etc).
   * Note that this RedBlackTree class implementation of toString generates an
   * inorder traversal. The toString of the Node class class above
   * produces a level order traversal of the nodes / values of the tree.
   * @return string containing the ordered values of this tree (in-order traversal)
   */
  public String toInOrderString() {
    // generate a string of all values of the tree in (ordered) in-order
    // traversal sequence
    StringBuffer sb = new StringBuffer();
    sb.append("[ ");
    sb.append(toInOrderStringHelper("", this.root));
    if (this.root != null) {
      sb.setLength(sb.length() - 2);
    }
    sb.append(" ]");
    return sb.toString();
  }

  private String toInOrderStringHelper(String str, Node<Player> node){
    if (node == null) {
      return str;
    }
    str = toInOrderStringHelper(str, node.leftChild);
    str += (node.data.toString() + ", ");
    str = toInOrderStringHelper(str, node.rightChild);
    return str;
  }

  /**
   * This method performs a level order traversal of the tree rooted
   * at the current node. The string representations of each data value
   * within this tree are assembled into a comma separated string within
   * brackets (similar to many implementations of java.util.Collection).
   * Note that the Node's implementation of toString generates a level
   * order traversal. The toString of the RedBlackTree class below
   * produces an inorder traversal of the nodes / values of the tree.
   * This method will be helpful as a helper for the debugging and testing
   * of your rotation implementation.
   * @return string containing the values of this tree in level order
   */
  public String toLevelOrderString() {
    String output = "[ ";
    if (this.root != null) {
      LinkedList<Node<Player>> q = new LinkedList<>();
      q.add(this.root);
      while(!q.isEmpty()) {
        Node<Player> next = q.removeFirst();
        if(next.leftChild != null) q.add(next.leftChild);
        if(next.rightChild != null) q.add(next.rightChild);
        output += next.data.toString();
        if(!q.isEmpty()) output += ", ";
      }
    }
    return output + " ]";
  }

  public String toString() {

    return "level order: " + this.toLevelOrderString() +
        "\nin order: " + this.toInOrderString();
  }



  /**
   * This method will resolve the red-black tree violation caused by the newly added red node. 
   * Also this method can be called recursively. 
   * @param node the newly added red node
   */
  protected void enforceRBTreePropertiesAfterInsert (Node<Player> node) {

    if(node==null)
      return;
    if(node.parent==null) {
      node.blackHeight=1;
      return;}
    if(node.parent.blackHeight==1||node.parent.blackHeight==2) //do not need to adjust
      return;



    Node<Player> pparent=node.parent.parent;//express the node's parent's parent

    //case 3:
    //node's parent is left child:
    if(pparent.rightChild!=null) {
      if(node.parent.blackHeight==0&&pparent.blackHeight==1&&pparent.leftChild==node.parent
          &&pparent.rightChild.blackHeight==0) {
        node.parent.blackHeight=1;
        pparent.rightChild.blackHeight=1;
        if(pparent!=root) {
          pparent.blackHeight=0;
          if(pparent.parent.blackHeight==0) { //if it causes more violation
            enforceRBTreePropertiesAfterInsert(pparent);
            return;
          }
          return;
        }
        return;
      }
    }

    //node's parent is right child:
    if(pparent.leftChild!=null) {
      if(node.parent.blackHeight==0&&pparent.blackHeight==1&&pparent.rightChild==node.parent
          &&pparent.leftChild.blackHeight==0) {
        node.parent.blackHeight=1;
        pparent.leftChild.blackHeight=1;
        if(pparent!=root) {
          pparent.blackHeight=0;
          if(pparent.parent.blackHeight==0) { //if it causes more violation
            enforceRBTreePropertiesAfterInsert(pparent);
            return;
          }
          return;
        }
        return;
      }
    }

    //case1 (left):

    if(node.parent.blackHeight==0&&pparent.blackHeight==1&&pparent.leftChild==node.parent&&
        node.parent.leftChild==node&&pparent.rightChild==null) {// null case
      rotate(node.parent,pparent);
      node.parent.blackHeight=1;
      node.parent.rightChild.blackHeight=0;
      return;
    }

    if(node.parent.blackHeight==0&&pparent.blackHeight==1&&pparent.leftChild==node.parent&&
        node.parent.leftChild==node&&pparent.rightChild.blackHeight==1) {
      rotate(node.parent,pparent);
      node.parent.blackHeight=1;
      node.parent.rightChild.blackHeight=0;
      return;
    }


    //case1 (right):

    if(node.parent.blackHeight==0&&pparent.blackHeight==1&&pparent.rightChild==node.parent&&
        node.parent.rightChild==node&&pparent.leftChild==null) { //null case
      rotate(node.parent,pparent);
      node.parent.blackHeight=1;
      node.parent.leftChild.blackHeight=0;
      return;
    }


    if(node.parent.blackHeight==0&&pparent.blackHeight==1&&pparent.rightChild==node.parent&&
        node.parent.rightChild==node&&pparent.leftChild.blackHeight==1) { 
      rotate(node.parent,pparent);
      node.parent.blackHeight=1;
      node.parent.leftChild.blackHeight=0;
      return;
    }


    //case2 left:

    if(node.parent.blackHeight==0&&pparent.blackHeight==1&&pparent.leftChild==node.parent&&
        node.parent.rightChild==node&&
        pparent.rightChild==null) { //null case 
      rotate(node,node.parent);
      enforceRBTreePropertiesAfterInsert (node.leftChild);// recursive method
      return;
    }

    if(node.parent.blackHeight==0&&pparent.blackHeight==1&&pparent.leftChild==node.parent&&
        node.parent.rightChild==node&&
        pparent.rightChild.blackHeight==1) { 
      rotate(node,node.parent);
      enforceRBTreePropertiesAfterInsert (node.leftChild);// recursive method
      return;
    }


    //right:
    if(node.parent.blackHeight==0&&node.parent.leftChild==node&&pparent.blackHeight==1&&
        pparent.rightChild==node.parent&&pparent.leftChild==null) { //null case
      rotate(node,node.parent);
      enforceRBTreePropertiesAfterInsert (node.rightChild);
      return;
    }


    if(node.parent.blackHeight==0&&node.parent.leftChild==node&&pparent.blackHeight==1&&
        pparent.rightChild==node.parent&&pparent.leftChild.blackHeight==1) { 
      rotate(node,node.parent);
      enforceRBTreePropertiesAfterInsert (node.rightChild);
      return;
    }



  }

  /**
   * tree whose value is the greatest that is less than X
   * @param node
   */
  public Node<Player> predecessorHelper(Node<Player> node) {
    if(node.leftChild==null) {
      return node;
    }
    Node<Player> container=node.leftChild;
    while(container.rightChild !=null) {
      container=container.rightChild;
    }

    return container;
  }

  /**
   *  tree whose value is the least that is greater than X
   * @param node
   * @return
   */
  public Node<Player> successorsHelper(Node<Player> node){
    if(node.rightChild==null) {
      return node;}

    Node<Player> container=node.rightChild;
    while(container.leftChild!=null) {
      container=container.leftChild;
    }
    return container;    
  }







  /**
   * remove a code
   * @param node node to remove
   * @return return the node of player 
   */
  public Node<Player> remove(Node<Player> node) {

    if(node==null)
      return null;

    if(node==root&&node.leftChild==null&&node.rightChild==null&&node.parent==null) { //if the 
      //node is root
      root=null;
      size--;
      return node;
      //return player
    }

    //revise 

    if(node==root&&node.leftChild!=null&&node.rightChild==null&&node.parent==null) { //only has 
      //left child
      Node <Player>result=node;
      root=node.leftChild;
      root.parent=null;
      size--;
      return result;


    }

    if(node==root&&node.leftChild==null&&node.rightChild!=null&&node.parent==null) { 
      //only has right child
      Node <Player>result=node;
      root=node.rightChild;
      root.parent=null;
      size--;
      return result;
    }




    if(node==root&&node.leftChild!=null&&node.rightChild!=null&&node.parent==null&&
        node.rightChild.leftChild==null) { //successor is the right child(not the normal case)

      Node<Player> leftContainer=root.leftChild;
      Node<Player> rightContainer=root.rightChild;




      root=rightContainer;
      rightContainer.leftChild=leftContainer;
      leftContainer.parent=rightContainer;
      root.parent=null;
      size--;
      root.blackHeight=1;
      return node;



    }


    if(node==root&&node.leftChild!=null&&node.rightChild!=null&&node.parent==null&&node.rightChild.leftChild!=null) {
      //root has two children(normal cases)


      Node<Player> leftContainer=root.leftChild;
      Node<Player> rightContainer=root.rightChild;
      Node <Player>successor=successorsHelper(node);


      int count=0; //see the successor has the right child or not 
      if(successor.rightChild==null&&successor.parent.leftChild==successor) {
        successor.parent.leftChild=null;
        count=1;
      }
      if(count==0&&successor.parent.leftChild==successor&&
          successor.rightChild!=null) {
        Node<Player> contain=successor.rightChild;
        Node<Player>parent=successor.parent;
        parent.leftChild=contain;
        contain.parent=parent;
      }

      root=successor;
      successor.leftChild=leftContainer;
      successor.rightChild=rightContainer;
      leftContainer.parent=successor;
      rightContainer.parent=successor;
      successor.parent=null;
      size--;
      root.blackHeight=1;
      return node;





    }










    if(node.blackHeight==0&&node.leftChild==null&&node.rightChild==null) { // 0 child red

      if(node.parent.leftChild==node) {  
        node.parent.leftChild=null;
        size--;
        return node;
      }

      if(node.parent.rightChild==node) {
        node.parent.rightChild=null;
        size--;
        return node;
      }

    }


    if(node.leftChild!=null&&node.rightChild==null) { //node has 1 left child not root


      if(node.parent.leftChild==node) { //node is the left child

        Node<Player>left=node.leftChild;
        Node<Player>parent=node.parent;
        parent.leftChild=left;
        left.parent=parent;
        left.blackHeight=1;
        size--;
        return node;
      }



      if(node.parent.rightChild==node) { //node is the right child
        Node<Player>parent=node.parent;
        Node<Player>left=node.leftChild;
        parent.rightChild=left;
        left.parent=parent; 
        left.blackHeight=1;
        size--;
        return node;
      }
    }



    if(node.leftChild==null&&node.rightChild!=null) { //node has 1 right child not root


      if(node.parent.leftChild==node) { //node is the left child
        Node<Player>parent=node.parent;
        Node<Player>right=node.rightChild;
        parent.leftChild=right;
        right.parent=parent;
        right.blackHeight=1;
        size--;
        return node;
      }

      if(node.parent.rightChild==node) { //node is the right child
        Node<Player>parent=node.parent;
        Node<Player>right=node.rightChild;
        parent.rightChild=right;
        right.parent=parent;
        right.blackHeight=1;
        size--;
        return node;
      } 
    }



    if(node.leftChild!=null&&node.rightChild!=null) { //2 children and the node is not root
      //(using the successor)

      if(node.parent.leftChild==node) { //node is the left child

        Node<Player>result=node;
        Node<Player>successor=successorsHelper( node);

        if(node.rightChild.leftChild==null) { //do not have left child

          Node<Player>parent=node.parent; //save the node next to the node
          Node<Player>left=node.leftChild;
          successor.leftChild=left;
          successor.parent=parent;
          parent.leftChild=successor;
          left.parent=successor;
          size--;
          return result;
        }


        if(node.rightChild.leftChild!=null) { //have the left child
          Node<Player>parent=node.parent; //save the node next to the node
          Node<Player>left=node.leftChild;
          Node<Player>right=node.rightChild;

          if(successor.rightChild==null) {
            successor.parent.leftChild=null;}

          if(successor.rightChild!=null) {
            Node<Player>sparent=successor.parent;
            Node<Player>hold=successor.rightChild;
            sparent.leftChild=hold;
            hold.parent=sparent;

          }


          parent.leftChild=successor;
          successor.leftChild=left;
          successor.rightChild=right;
          successor.parent=parent;
          left.parent=successor;
          right.parent=successor;
          size--;
          return result;
        }
      }






      if(node.parent.rightChild==node) { //node is the right child

        Node<Player>result=node;
        Node<Player>parent=node.parent;
        Node<Player>left=node.leftChild;
        Node<Player>right=node.rightChild;
        Node<Player>successor=successorsHelper( node);


        if(node.rightChild.leftChild==null) {

          parent.rightChild=right;
          right.leftChild=left;
          left.parent=right;
          right.parent=parent;
          size--;
          return result;
        }


        if(successor.rightChild==null) {

          successor.parent.leftChild=null;
        }

        if(successor.rightChild!=null) {
          Node<Player>sparent=successor.parent;
          Node<Player>hold=successor.rightChild;
          sparent.leftChild=hold;
          hold.parent=sparent;

        }

        parent.rightChild=successor;
        successor.leftChild=left;
        successor.rightChild=right;
        left.parent=successor;
        right.parent=successor;
        successor.parent=parent;
        size--;
        return result;

      }
    }


    int count=0;
    Node<Player> remain=node;
    if(node.parent.leftChild==node&&count!=1) { //node is the left child
      node=node.parent;
      node.leftChild=null;
      size--;
      count=1;
    }

    if(count==0) { //then the node is the right child
      node=node.parent;
      node.rightChild=null;
      size--;
      count=1;
    }

    balance(node);

    return remain;

  }






  /**
   * balance the tree
   * @param node the node to delete 
   */
  public void balance(Node<Player> node) {

    //case 3:
    //left child
    if(node.rightChild!=null)
    {
      if(node.rightChild!=null&&
          node.rightChild.leftChild==null&&
          node.rightChild.rightChild==null&&node.rightChild.blackHeight==1) { //null null
        node.rightChild.blackHeight=0;
        if(node.blackHeight==0) {
          node.blackHeight=1;
          node.leftChild=null;
          return ;
        }
        if(node==root) {
          node.blackHeight=1;
          return;
        }
        balance(node);
      }

      if( node.rightChild!=null&&
          node.rightChild.blackHeight==1&&node.rightChild.leftChild==null&&
          node.rightChild.rightChild!=null&&
          node.rightChild.rightChild.blackHeight==1) { //null black
        node.rightChild.blackHeight=0;
        if(node.blackHeight==0) {
          node.blackHeight=1;
          node.leftChild=null;
          return ;
        }
        if(node==root) {
          node.blackHeight=1;
          return;
        }
        balance(node);
      }

      if(node.rightChild!=null&&
          node.rightChild.blackHeight==1&&node.rightChild.leftChild!=null&&
          node.rightChild.leftChild.blackHeight==1&&
          node.rightChild.rightChild!=null&&
          node.rightChild.rightChild.blackHeight==1) { // black black
        node.rightChild.blackHeight=0;
        if(node.blackHeight==0) {
          node.blackHeight=1;
          node.leftChild=null;
          return ;
        }
        if(node==root) {
          node.blackHeight=1;
          return;
        }
        balance(node);
      }


      if(node.rightChild!=null&&
          node.rightChild.blackHeight==1&& node.rightChild.leftChild!=null&&
          node.rightChild.leftChild.blackHeight==1&&
          node.rightChild.rightChild==null) { //black null
        node.rightChild.blackHeight=0;
        if(node.blackHeight==0) {
          node.blackHeight=1;
          node.leftChild=null;
          return ;
        }
        if(node==root) {
          node.blackHeight=1;
          return;
        }
        balance(node);
      }
    }


    if(node.leftChild!=null) {


      //right child
      if(node.leftChild!=null&&
          node.leftChild.blackHeight==1&&node.leftChild.leftChild==null
          &&node.leftChild.rightChild==null) { //null null

        node.leftChild.blackHeight=0;
        if(node.blackHeight==0) {
          node.blackHeight=1;
          node.rightChild=null;
          return ;
        }
        if(node==root) {
          node.blackHeight=1;
          return;
        }

        balance(node);
      }


      if( node.leftChild!=null&&
          node.leftChild.blackHeight==1&&node.leftChild.leftChild==null
          &&node.leftChild.rightChild!=null&&node.leftChild.rightChild.blackHeight==1) { //null black

        node.leftChild.blackHeight=0;
        if(node.blackHeight==0) {
          node.blackHeight=1;
          node.rightChild=null;
          return ;
        }
        if(node==root) {
          node.blackHeight=1;
          return;
        }
        balance(node);
      }

      if( node.leftChild!=null&&
          node.leftChild.blackHeight==1&&node.leftChild.leftChild!=null&&node.leftChild.leftChild.blackHeight==1
          &&node.leftChild.rightChild!=null&&node.leftChild.rightChild.blackHeight==1) { //black black

        node.leftChild.blackHeight=0;
        if(node.blackHeight==0) {
          node.blackHeight=1;
          node.rightChild=null;
          return ;
        }
        if(node==root) {
          node.blackHeight=1;
          return;
        }
        balance(node);
      }


      if( node.leftChild!=null&&
          node.leftChild.blackHeight==1&&node.leftChild.leftChild!=null&&node.leftChild.leftChild.blackHeight==1
          &&node.leftChild.rightChild==null) { //black null

        node.leftChild.blackHeight=0;
        if(node.blackHeight==0) {
          node.blackHeight=1;
          node.rightChild=null;
          return ;
        }
        if(node==root) {
          node.blackHeight=1;
          return;
        }
        balance(node);
      }
    }


    //case1.5:
    //left null
    if(node.rightChild!=null&&
        node.rightChild.blackHeight==1&&node.rightChild.leftChild!=null&&
        node.rightChild.leftChild.blackHeight==0&&
        node.rightChild.rightChild==null) {
      rotate(node.rightChild.leftChild,node.rightChild);
      node.rightChild.blackHeight=1;
      node.rightChild.rightChild.blackHeight=0;
      balance(node);

    }
    //left black
    if(node.rightChild!=null&&
        node.rightChild.blackHeight==1&&node.rightChild.leftChild!=null&&
        node.rightChild.leftChild.blackHeight==0&&
        node.rightChild.rightChild!=null&&
        node.rightChild.rightChild.blackHeight==1) {
      rotate(node.rightChild.leftChild,node.rightChild);
      node.rightChild.blackHeight=1;
      node.rightChild.rightChild.blackHeight=0;
      balance(node);
    }

    //right null
    if(node.leftChild!=null&&
        node.leftChild.blackHeight==1&&node.leftChild.rightChild!=null&&
        node.leftChild.rightChild.blackHeight==0&&

        node.leftChild.leftChild==null) {
      rotate(node.leftChild.rightChild,node.leftChild);
      node.leftChild.blackHeight=1;
      node.leftChild.leftChild.blackHeight=0;
      balance(node);
    }


    //right black
    if(node.leftChild!=null&&
        node.leftChild.blackHeight==1&&node.leftChild.rightChild!=null&&
        node.leftChild.rightChild.blackHeight==0&&
        node.leftChild.leftChild!=null&&
        node.leftChild.leftChild.blackHeight==1) {
      rotate(node.leftChild.rightChild,node.leftChild);
      node.leftChild.blackHeight=1;
      node.leftChild.leftChild.blackHeight=0;
      balance(node);
    }

    //case 1
    //left
    if(node.rightChild!=null&&
        node.rightChild.blackHeight==1&&node.rightChild.rightChild!=null&&
        node.rightChild.rightChild.blackHeight==0) {
      rotate(node.rightChild,node);
      int color=0;
      color=node.blackHeight;
      node.blackHeight=1;
      node.parent.blackHeight=color;
      node.parent.rightChild.blackHeight=1;
      node.leftChild=null;
      return ;
    }
    //revise!!!!!
    //right
    if(node.leftChild!=null&&
        node.leftChild.blackHeight==1&&
        node.leftChild.leftChild!=null&&
        node.leftChild.leftChild.blackHeight==0) {
      rotate(node.leftChild,node);
      int color=0;
      color=node.blackHeight;
      node.parent.blackHeight=color;
      node.blackHeight=1;

      node.parent.leftChild.blackHeight=1;



      // node.rightChild=null;
      return ;
    }


    //case2
    //left
    if(node.blackHeight==1&&node.rightChild!=null&&node.rightChild.blackHeight==0&&
        ( (node.rightChild.leftChild==null&&node.rightChild.rightChild==null)||
            (node.rightChild.leftChild!=null&&node.rightChild.leftChild.blackHeight==1&&node.rightChild.rightChild==null)||
            (node.rightChild.leftChild==null&&node.rightChild.rightChild!=null&&node.rightChild.rightChild.blackHeight==1)||
            (node.rightChild.leftChild.blackHeight==1&&node.rightChild.rightChild.blackHeight==1)
            )
        ){
      rotate(node.rightChild,node);
      node.blackHeight=0;
      node.parent.blackHeight=1;
      balance(node);
    }


    //right
    if(node.blackHeight==1&&node.leftChild!=null&&node.leftChild.blackHeight==0&&(
        (node.leftChild.leftChild==null&&node.leftChild.rightChild==null)||
        (node.leftChild.leftChild.blackHeight==1&&node.leftChild.rightChild==null)||
        (node.leftChild.leftChild==null&&node.leftChild.rightChild.blackHeight==1)||
        (node.leftChild.leftChild.blackHeight==1&&node.leftChild.rightChild.blackHeight==1))) {


      rotate(node.leftChild,node);
      node.blackHeight=0;
      node.parent.blackHeight=1;
      balance(node);
    }








    return ;


  }

  ClassOfIterableSortedCollectionAE <String> instance=null;
  //BeforeEach annotation makes a method invoked automatically
  //before each test
  @BeforeEach
  public void createInstane() {
    instance =new ClassOfIterableSortedCollectionAE<String>();
  }

  //a complete tree
  @Test
  public void test1() {



    //  ClassOfIterableSortedCollection<String> instance=new ClassOfIterableSortedCollection<String>();


    instance.insert(new Player(24, "Jimmy", "sophomore"));
    instance.insert(new Player(50, "Tonny", "freshman"));
    instance.insert(new Player(70, "Amy", "sophomore"));
    instance.insert(new Player(32, "Jack", "sophomore"));
    instance.insert(new Player(52, "Tom", "freshman"));
    instance.insert(new Player(62, "Jack", "Junior"));
    instance.insert(new Player(11, "Peter", "freshman"));

    instance.remove(instance.root.rightChild);
    instance.remove(instance.root.rightChild);

    String actualResultToLevelOrder=instance.toLevelOrderString();
    assertEquals(actualResultToLevelOrder,"[ 50 Tonny freshman, 24 Jimmy sophomore, "
        + "52 Tom freshman, 11 Peter freshman, 32 Jack sophomore ]");


    instance.remove(instance.root.leftChild.rightChild);


    String actualResultToLevelOrder2=instance.toLevelOrderString();


    assertEquals(actualResultToLevelOrder2,"[ 50 Tonny freshman, 24 Jimmy sophomore, "
        + "52 Tom freshman, 11 Peter freshman ]");

    instance.remove(instance.root.leftChild);

    String actualResultToLevelOrder3=instance.toLevelOrderString();

    assertEquals(actualResultToLevelOrder3,"[ 50 Tonny freshman, 11 Peter freshman,"
        + " 52 Tom freshman ]");




  }


  //Test node with 1 or 2 child
  @Test
  public void test2() {
    // ClassOfIterableSortedCollection<String> instance=new ClassOfIterableSortedCollection<String>();

    instance.insert(new Player(24, "Jimmy", "sophomore"));
    instance.insert(new Player(50, "Tonny", "freshman"));
    instance.insert(new Player(70, "Amy", "sophomore"));
    instance.insert(new Player(32, "Jack", "sophomore"));
    instance.insert(new Player(52, "Tom", "freshman"));
    instance.insert(new Player(62, "Jack", "Junior"));

    instance.remove(instance.root.leftChild);

    String actualResultToLevelOrder=instance.toLevelOrderString();
    assertEquals(actualResultToLevelOrder,"[ 50 Tonny freshman, 32 Jack sophomore, 6"
        + "2 Jack Junior, 52 Tom freshman, 70 Amy sophomore ]");

    instance.remove(instance.root.rightChild.rightChild);
    String actualResultToLevelOrder2=instance.toLevelOrderString();
    assertEquals(actualResultToLevelOrder2,"[ 50 Tonny freshman, 32 Jack sophomore, 62 Jack Junior, 52 Tom freshman ]");

    instance.remove(instance.root.rightChild);

    String actualResultToLevelOrder3=instance.toLevelOrderString();

    assertEquals(actualResultToLevelOrder3,"[ 50 Tonny freshman, 32 Jack sophomore, 52 Tom freshman ]");




  }



  //Test node with 0 child
  @Test
  public   void test3() {
    // ClassOfIterableSortedCollection<String> instance=new ClassOfIterableSortedCollection<String>();

    instance.insert(new Player(24, "Jimmy", "sophomore"));
    instance.insert(new Player(50, "Tonny", "freshman"));
    instance.insert(new Player(70, "Amy", "sophomore"));
    instance.insert(new Player(32, "Jack", "sophomore"));
    instance.insert(new Player(52, "Tom", "freshman"));
    instance.insert(new Player(62, "Jack", "Junior"));
    instance.insert(new Player(11, "Peter", "freshman"));

    instance.remove(instance.root.rightChild.rightChild);

    String actualResultToLevelOrder=instance.toLevelOrderString();
    assertEquals(actualResultToLevelOrder,"[ 50 Tonny freshman, 24 Jimmy sophomore, 62 Jack Junior, 11 Peter freshman, 32 Jack sophomore, 52 Tom freshman ]");

    instance.remove(instance.root.rightChild.leftChild);
    String actualResultToLevelOrder2=instance.toLevelOrderString();
    assertEquals(actualResultToLevelOrder2,"[ 50 Tonny freshman, 24 Jimmy sophomore, 62 Jack Junior, 11 Peter freshman, 32 Jack sophomore ]");

    instance.remove(instance.root.leftChild.rightChild);

    String actualResultToLevelOrder3=instance.toLevelOrderString();

    assertEquals(actualResultToLevelOrder3,"[ 50 Tonny freshman, 24 Jimmy sophomore, 62 Jack Junior, 11 Peter freshman ]");

    instance.remove(instance.root.leftChild.leftChild);

    String actualResultToLevelOrder4=instance.toLevelOrderString();

    assertEquals(actualResultToLevelOrder4,"[ 50 Tonny freshman, 24 Jimmy sophomore, 62 Jack Junior ]");




  }



  //Test some random nodes
  @Test
  public  void test4() {
    // ClassOfIterableSortedCollection<String> instance=new ClassOfIterableSortedCollection<String>();

    instance.insert(new Player(24, "Jimmy", "sophomore"));
    instance.insert(new Player(50, "Tonny", "freshman"));
    instance.insert(new Player(70, "Amy", "sophomore"));
    instance.insert(new Player(32, "Jack", "sophomore"));
    instance.insert(new Player(52, "Tom", "freshman"));
    instance.insert(new Player(62, "Jack", "Junior"));
    instance.insert(new Player(72, "Peter", "freshman"));

    instance.remove(instance.root.rightChild);

    String actualResultToLevelOrder=instance.toLevelOrderString();
    assertEquals(actualResultToLevelOrder,"[ 50 Tonny freshman, 24 Jimmy sophomore, 70 Amy sophomore, 32 Jack sophomore, 52 Tom freshman, 72 Peter freshman ]");

    instance.remove(instance.root.rightChild);
    String actualResultToLevelOrder2=instance.toLevelOrderString();
    assertEquals(actualResultToLevelOrder2,"[ 50 Tonny freshman, 24 Jimmy sophomore, 72 Peter freshman, 32 Jack sophomore, 52 Tom freshman ]");

    instance.remove(instance.root.rightChild);

    String actualResultToLevelOrder3=instance.toLevelOrderString();

    assertEquals(actualResultToLevelOrder3,"[ 50 Tonny freshman, 24 Jimmy sophomore, 52 Tom freshman, 32 Jack sophomore ]");

    instance.remove(instance.root.rightChild);

    String actualResultToLevelOrder4=instance.toLevelOrderString();

    assertEquals(actualResultToLevelOrder4,"[ 32 Jack sophomore, 24 Jimmy sophomore, 50 Tonny freshman ]");

    instance.remove(instance.root.rightChild);

    String actualResultToLevelOrder5=instance.toLevelOrderString();

    assertEquals(actualResultToLevelOrder5,"[ 32 Jack sophomore, 24 Jimmy sophomore ]");





  }


  //Test more nodes with the examples teached in the class
  @Test
  public void test5() {
    // ClassOfIterableSortedCollection<String> instance=new ClassOfIterableSortedCollection<String>();

    instance.insert(new Player(24, "Jimmy", "sophomore"));
    instance.insert(new Player(50, "Tonny", "freshman"));
    instance.insert(new Player(70, "Amy", "sophomore"));
    instance.insert(new Player(32, "Jack", "sophomore"));
    instance.insert(new Player(52, "Tom", "freshman"));
    instance.insert(new Player(77, "Jack", "Junior"));
    instance.insert(new Player(99, "Peter", "freshman"));

    instance.remove(instance.root.rightChild.rightChild.rightChild);

    String actualResultToLevelOrder=instance.toLevelOrderString();
    assertEquals(actualResultToLevelOrder,"[ 50 Tonny freshman, 24 Jimmy sophomore, 70 Amy sophomore, 32 Jack sophomore, 52 Tom freshman, 77 Jack Junior ]");


    instance.remove(instance.root.rightChild.rightChild);
    String actualResultToLevelOrder2=instance.toLevelOrderString();
    assertEquals(actualResultToLevelOrder2,"[ 50 Tonny freshman, 24 Jimmy sophomore, 70 Amy sophomore, 32 Jack sophomore, 52 Tom freshman ]");

    instance.remove(instance.root.rightChild.leftChild);

    String actualResultToLevelOrder3=instance.toLevelOrderString();

    assertEquals(actualResultToLevelOrder3,"[ 50 Tonny freshman, 24 Jimmy sophomore, 70 Amy sophomore, 32 Jack sophomore ]");

    instance.remove(instance.root.rightChild);

    String actualResultToLevelOrder4=instance.toLevelOrderString();

    assertEquals(actualResultToLevelOrder4,"[ 32 Jack sophomore, 24 Jimmy sophomore, 50 Tonny freshman ]");

    instance.remove(instance.root.leftChild);

    String actualResultToLevelOrder5=instance.toLevelOrderString();

    assertEquals(actualResultToLevelOrder5,"[ 32 Jack sophomore, 50 Tonny freshman ]");





  }


  Node<Player> node=root;
  int count=0;

  /**
   * we will remove all of the player having the specific year
   * @param year the year of the Player
   * @param node the node that you want to start with(remove all of the node meeting the requirement below
   * @return true if we remove successfully and vice versa
   */
  public  boolean removeYear(String year, Node<Player>node) {


    int count=size;
    if(node==null)
      return false;

    while(node!=null&&size>0&&node.data.year.equals(year)) { //condition is met, we should remove the node

      if(node==root&&node.leftChild==null&&node.rightChild==null) { //node is the root and do not have any children
        remove(node);
        if(root!=node) { //if I rotate the tree, then I will put the node into the modified root again to traverse the whole tree(If I do not rotate the tree, I will still do this line 
          //so that I can make sure everything has been removed)
          node=root;}
        else {
          return true;}
      }

      else  if(node==root&&node.leftChild!=null&&node.rightChild==null) { //node is the root and only has leftChild

        Node<Player>contain=node.leftChild;
        remove(node);
        if(root!=node) {
          node=root;}
        else {
          node=contain;}
        continue;
      }
      else if(node==root&&node.leftChild==null&&node.rightChild!=null) { //node is the root and only has rightChild
        Node<Player>contain=node.rightChild;
        remove(node);
        if(root!=node) {
          node=root;}
        else {
          node=contain;}
        continue;
      }
      else if(node==root&&node.leftChild!=null&&node.rightChild!=null) { //node is the root and have two children
        Node<Player>contain=successorsHelper(node);
        remove(node);
        if(root!=node) {
          node=root;}
        else {
          node=contain;}
        continue;
      }

      else if(node!=root&&node.leftChild==null&&node.rightChild==null) { // node is not root and node is leaf 
        remove(node);
        if(root!=node) {//if I rotate the tree, then I will put the node into the modified root again to traverse the whole tree(If I do not rotate the tree, I will still do this line 
          //so that I can make sure everything has been removed)
          node=root;}
        else {
          return true;}
      }

      else if(node!=root&&node.leftChild==null&&node.rightChild!=null) { //node is not root and node only has rightChild
        Node<Player>contain=node.rightChild;
        remove(node);
        if(root!=node) {
          node=root;}
        else {
          node=contain;}
        continue;
      }

      else if(node!=root&&node.leftChild!=null&&node.rightChild==null) { //node is not root and node only has leftChild
        Node<Player> contain=node.leftChild;
        remove(node);
        if(root!=node) {
          node=root;}
        else {
          node=contain;}
        continue;
      }

      else if(node!=root&&node.leftChild!=null&&node.rightChild!=null) { //node is not root and node have two children
        Node<Player>successor=successorsHelper(node);
        remove(node);
        if(root!=node) {
          node=root;}
        else {
          node=successor;}
        continue; 
      }

    }


    Node<Player>contain=node;

    if(size>0&&node!=null&&node.leftChild!=null) { //leftChild


      removeYear(year,contain.leftChild);
    }
    if(size>0&&contain!=null&&contain.rightChild!=null) { //rightChild


      removeYear(year,contain.rightChild);
    }

    if(count==size)
      return false;

    return true;
  }



  /**
   * This test will see the remove method of removing freshman and also check the result of DataWranger
   */
  @Test
  public void CodeReviewOfDataWranger1() {


    // ClassOfIterableSortedCollectionAE<String> node=new   ClassOfIterableSortedCollectionAE<String>();


    RosterLoader a=new  RosterLoader ();
    ArrayList<Player> books=null;

    try {
      books=  (ArrayList<Player>) a.loadPlayers( "uwBasketball.xml");
    } catch (FileNotFoundException e) {

      e.printStackTrace();
    }

    for(int i=0;i<books.size();i++) {
      instance.insert(books.get(i));
    }




    String beforetolevel=instance.toLevelOrderString();
    String beforeinorder=instance.toInOrderString();

    String estimatedbeforetolevel="[ 4 Kamari McGee sophomore, 2 Jordan Davis junior,"
        + " 12 Luke Haertle freshman, 0 Jahcobi Neath senior, 3 Connor Essegian freshman, "
        + "10 Isaac Lindsey sophomore, 21 Chris Hodges freshman, 5 Tyler Wahl senior, "
        + "11 Max Klesmit junior, 14 Carter Gilmore junior, 23 Chucky Hepburn sophomore, "
        + "13 Justin Taphorn junior, 15 Isaac Gard freshman, 22 Steven Crowl junior, "
        + "30 Ross Candelino freshman, 35 Markus Ilver sophomore ]";


    assertEquals(beforetolevel,estimatedbeforetolevel);

    String estimatedbeforeinorder="[ 0 Jahcobi Neath senior, 2 Jordan Davis junior, 3 Connor "
        + "Essegian freshman, 4 Kamari McGee sophomore, 5 Tyler Wahl senior, 10 Isaac Lindsey "
        + "sophomore, 11 Max Klesmit junior, 12 Luke Haertle freshman, 13 Justin Taphorn junior, "
        + "14 Carter Gilmore junior, 15 Isaac Gard freshman, 21 Chris Hodges freshman, 22 Steven "
        + "Crowl junior, 23 Chucky Hepburn sophomore, 30 Ross Candelino freshman, 35 Markus Ilver "
        + "sophomore ]";

    assertEquals(beforeinorder,estimatedbeforeinorder);

    assertTrue(beforetolevel.contains("freshman")); //it contains freshman originally
    assertTrue(beforeinorder.contains("freshman")); //it contains freshman originally

    instance.removeYear("freshman",instance.root);




    String aftertolevel=instance.toLevelOrderString();
    String afterinorder=instance.toInOrderString();

    assertTrue(!aftertolevel.contains("freshman")); //it does not contain freshman afterwards
    assertTrue(!afterinorder.contains("freshman")); //it does not contain freshman afterwards


    String estimatedaftertolevel="[ 4 Kamari McGee sophomore, 0 Jahcobi Neath senior, 13 Justin "
        + "Taphorn junior, 2 Jordan Davis junior, 10 Isaac Lindsey sophomore, 22 Steven Crowl "
        + "junior, 5 Tyler Wahl senior, 11 Max Klesmit junior, 14 Carter Gilmore junior, 23 Chucky"
        + " Hepburn sophomore, 35 Markus Ilver sophomore ]";

    String estimatedafterinorder="[ 0 Jahcobi Neath senior, 2 Jordan Davis junior, 4 Kamari McGee"
        + " sophomore, 5 Tyler Wahl senior, 10 Isaac Lindsey sophomore, 11 Max Klesmit junior, 13"
        + " Justin Taphorn junior, 14 Carter Gilmore junior, 22 Steven Crowl junior, 23 Chucky"
        + " Hepburn sophomore, 35 Markus Ilver sophomore ]";

    assertEquals(aftertolevel, estimatedaftertolevel);
    assertEquals(afterinorder,estimatedafterinorder);

  }


  /**
   * This test will see the remove method of removing sophomore and also check the result of DataWranger
   */
  @Test
  public void CodeReviewOfDataWranger2() {


    // ClassOfIterableSortedCollectionAE<String> instance=new   ClassOfIterableSortedCollectionAE<String>();


    RosterLoader a=new  RosterLoader ();
    ArrayList<Player> books=null;

    try {
      books=  (ArrayList<Player>) a.loadPlayers( "uwBasketball.xml");
    } catch (FileNotFoundException e) {

      e.printStackTrace();
    }

    for(int i=0;i<books.size();i++) {
      instance.insert(books.get(i));
    }

    String beforetolevel=instance.toLevelOrderString();
    String beforeinorder=instance.toInOrderString();



    String estimatedbeforetolevel="[ 4 Kamari McGee sophomore, 2 Jordan Davis junior, 12 Luke "
        + "Haertle freshman, 0 Jahcobi Neath senior, 3 Connor Essegian freshman, 10 Isaac Lindsey "
        + "sophomore, 21 Chris Hodges freshman, 5 Tyler Wahl senior, 11 Max Klesmit junior, 14 "
        + "Carter Gilmore junior, 23 Chucky Hepburn sophomore, 13 Justin Taphorn junior, 15 Isaac "
        + "Gard freshman, 22 Steven Crowl junior, 30 Ross Candelino freshman, 35 Markus Ilver "
        + "sophomore ]";

    String estimatedbeforeinorder="[ 0 Jahcobi Neath senior, 2 Jordan Davis junior, 3 Connor"
        + " Essegian freshman, 4 Kamari McGee sophomore, 5 Tyler Wahl senior, 10 Isaac Lindsey "
        + "sophomore, 11 Max Klesmit junior, 12 Luke Haertle freshman, 13 Justin Taphorn junior,"
        + " 14 Carter Gilmore junior, 15 Isaac Gard freshman, 21 Chris Hodges freshman, 22 Steven"
        + " Crowl junior, 23 Chucky Hepburn sophomore, 30 Ross Candelino freshman, 35 Markus Ilver "
        + "sophomore ]";

    assertEquals(beforetolevel,estimatedbeforetolevel);

    assertEquals(beforeinorder,estimatedbeforeinorder);



    assertTrue(beforetolevel.contains("sophomore"));  //it contains sophomore originally
    assertTrue(beforeinorder.contains("sophomore"));  //it contains sophomore originally


    instance.removeYear("sophomore",instance.root);



    String aftertolevel=instance.toLevelOrderString();
    String afterinorder=instance.toInOrderString();


    assertTrue(!aftertolevel.contains("sophomore")); //it does not contain sophomore afterwards
    assertTrue(!afterinorder.contains("sophomore")); //it does not contain sophomore afterwards


    String estimatedaftertolevel="[ 5 Tyler Wahl senior, 2 Jordan Davis junior, 12 Luke Haertle "
        + "freshman, 0 Jahcobi Neath senior, 3 Connor Essegian freshman, 11 Max Klesmit junior,"
        + " 21 Chris Hodges freshman, 14 Carter Gilmore junior, 30 Ross Candelino freshman, 13 "
        + "Justin Taphorn junior, 15 Isaac Gard freshman, 22 Steven Crowl junior ]";


    String estimatedafterinorder="[ 0 Jahcobi Neath senior, 2 Jordan Davis junior, 3 Connor"
        + " Essegian freshman, 5 Tyler Wahl senior, 11 Max Klesmit junior, 12 Luke Haertle "
        + "freshman, 13 Justin Taphorn junior, 14 Carter Gilmore junior, 15 Isaac Gard freshman,"
        + " 21 Chris Hodges freshman, 22 Steven Crowl junior, 30 Ross Candelino freshman ]";

    assertEquals(aftertolevel, estimatedaftertolevel);
    assertEquals(afterinorder,estimatedafterinorder);



  }


  /**
   * This test will see the remove method of removing junior and also check the result of DataWranger
   */
  @Test
  public   void  CodeReviewOfDataWranger3() {


    //ClassOfIterableSortedCollectionAE<String> instance=new   ClassOfIterableSortedCollectionAE<String>();


    RosterLoader a=new  RosterLoader ();
    ArrayList<Player> books=null;

    try {
      books=  (ArrayList<Player>) a.loadPlayers( "uwBasketball.xml");
    } catch (FileNotFoundException e) {

      e.printStackTrace();
    }

    for(int i=0;i<books.size();i++) {
      instance.insert(books.get(i));
    }


    String beforetolevel=instance.toLevelOrderString();
    String beforeinorder=instance.toInOrderString();




    String estimatedbeforetolevel="[ 4 Kamari McGee sophomore, 2 Jordan Davis junior, 12 Luke"
        + " Haertle freshman, 0 Jahcobi Neath senior, 3 Connor Essegian freshman, 10 Isaac"
        + " Lindsey sophomore, 21 Chris Hodges freshman, 5 Tyler Wahl senior, 11 Max Klesmit"
        + " junior, 14 Carter Gilmore junior, 23 Chucky Hepburn sophomore, 13 Justin Taphorn "
        + "junior, 15 Isaac Gard freshman, 22 Steven Crowl junior, 30 Ross Candelino freshman, "
        + "35 Markus Ilver sophomore ]";
    assertEquals(beforetolevel,estimatedbeforetolevel);



    String estimatedbeforeinorder="[ 0 Jahcobi Neath senior, 2 Jordan Davis junior, 3 Connor"
        + " Essegian freshman, 4 Kamari McGee sophomore, 5 Tyler Wahl senior, 10 Isaac Lindsey "
        + "sophomore, 11 Max Klesmit junior, 12 Luke Haertle freshman, 13 Justin Taphorn junior, "
        + "14 Carter Gilmore junior, 15 Isaac Gard freshman, 21 Chris Hodges freshman, 22 Steven "
        + "Crowl junior, 23 Chucky Hepburn sophomore, 30 Ross Candelino freshman, 35 Markus Ilver "
        + "sophomore ]";
    assertEquals(beforeinorder,estimatedbeforeinorder);



    assertTrue(beforetolevel.contains("junior")); //it contains junior originally
    assertTrue(beforeinorder.contains("junior")); //it contains junior originally





    instance.removeYear("junior",instance.root);




    String aftertolevel=instance.toLevelOrderString();
    String afterinorder=instance.toInOrderString();

    assertTrue(!aftertolevel.contains("junior")); //it does not contain junior afterwards
    assertTrue(!afterinorder.contains("junior")); //it does not contain junior afterwards




    String extimatedaftertolevel="[ 4 Kamari McGee sophomore, 3 Connor Essegian freshman, 12 Luke"
        + " Haertle freshman, 0 Jahcobi Neath senior, 5 Tyler Wahl senior, 21 Chris Hodges "
        + "freshman, 10 Isaac Lindsey sophomore, 15 Isaac Gard freshman, 30 Ross Candelino"
        + " freshman, 23 Chucky Hepburn sophomore, 35 Markus Ilver sophomore ]";

    assertEquals(aftertolevel,extimatedaftertolevel);

    String extimatedafterinorder="[ 0 Jahcobi Neath senior, 3 Connor Essegian freshman, "
        + "4 Kamari McGee sophomore, 5 Tyler Wahl senior, 10 Isaac Lindsey sophomore, 12 "
        + "Luke Haertle freshman, 15 Isaac Gard freshman, 21 Chris Hodges freshman, 23 Chucky"
        + " Hepburn sophomore, 30 Ross Candelino freshman, 35 Markus Ilver sophomore ]";

    assertEquals(afterinorder,extimatedafterinorder);

  }



  /**
   * This test will see the remove method of removing senior and also check the result of DataWranger
   */
  @Test
  public  void CodeReviewOfDataWranger4() {


    //ClassOfIterableSortedCollectionAE<String> instance=new   ClassOfIterableSortedCollectionAE<String>();


    RosterLoader a=new  RosterLoader ();
    ArrayList<Player> books=null;

    try {
      books=  (ArrayList<Player>) a.loadPlayers( "uwBasketball.xml");
    } catch (FileNotFoundException e) {

      e.printStackTrace();
    }

    for(int i=0;i<books.size();i++) {
      instance.insert(books.get(i));
    }




    String beforetolevel=instance.toLevelOrderString();
    String beforeinorder=instance.toInOrderString();




    String estimatedbeforetolevel="[ 4 Kamari McGee sophomore, 2 Jordan Davis junior, 12 Luke Haertle"
        + " freshman, 0 Jahcobi Neath senior, 3 Connor Essegian freshman, 10 Isaac Lindsey "
        + "sophomore, 21 Chris Hodges freshman, 5 Tyler Wahl senior, 11 Max Klesmit junior, "
        + "14 Carter Gilmore junior, 23 Chucky Hepburn sophomore, 13 Justin Taphorn junior,"
        + " 15 Isaac Gard freshman, 22 Steven Crowl junior, 30 Ross Candelino freshman, 35 "
        + "Markus Ilver sophomore ]";

    assertEquals(beforetolevel,estimatedbeforetolevel);


    String estimatedbeforeinorder="[ 0 Jahcobi Neath senior, 2 Jordan Davis junior, 3"
        + " Connor Essegian freshman, 4 Kamari McGee sophomore, 5 Tyler Wahl senior, "
        + "10 Isaac Lindsey sophomore, 11 Max Klesmit junior, 12 Luke Haertle freshman,"
        + " 13 Justin Taphorn junior, 14 Carter Gilmore junior, 15 Isaac Gard freshman, "
        + "21 Chris Hodges freshman, 22 Steven Crowl junior, 23 Chucky Hepburn sophomore, "
        + "30 Ross Candelino freshman, 35 Markus Ilver sophomore ]";

    assertEquals(beforeinorder,estimatedbeforeinorder);


    assertTrue(beforetolevel.contains("senior")); //it contains senior originally
    assertTrue(beforeinorder.contains("senior")); //it contains senior originally


    instance.removeYear("senior",instance.root);



    String aftertolevel=instance.toLevelOrderString();
    String afterinorder=instance.toInOrderString();

    assertTrue(!aftertolevel.contains("senior")); //it does not contain senior afterwards
    assertTrue(!afterinorder.contains("senior")); //it does not contain senior afterwards


    String estimatedaftertolevel="[ 4 Kamari McGee sophomore, 3 Connor Essegian freshman, 12 "
        + "Luke Haertle freshman, 2 Jordan Davis junior, 11 Max Klesmit junior, 21 Chris Hodges "
        + "freshman, 10 Isaac Lindsey sophomore, 14 Carter Gilmore junior, 23 Chucky Hepburn "
        + "sophomore, 13 Justin Taphorn junior, 15 Isaac Gard freshman, 22 Steven Crowl junior, "
        + "30 Ross Candelino freshman, 35 Markus Ilver sophomore ]";

    assertEquals(aftertolevel,estimatedaftertolevel);


    String estimatedafterinorder="[ 2 Jordan Davis junior, 3 Connor Essegian freshman, "
        + "4 Kamari McGee sophomore, 10 Isaac Lindsey sophomore, 11 Max Klesmit junior,"
        + " 12 Luke Haertle freshman, 13 Justin Taphorn junior, 14 Carter Gilmore junior, "
        + "15 Isaac Gard freshman, 21 Chris Hodges freshman, 22 Steven Crowl junior, 23 Chucky "
        + "Hepburn sophomore, 30 Ross Candelino freshman, 35 Markus Ilver sophomore ]";

    assertEquals(afterinorder,estimatedafterinorder);


  }


  public static void main(String[] args) {


  }





}
