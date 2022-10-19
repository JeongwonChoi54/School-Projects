/**
 * Name: Jeongwon Choi
 * Matric. No: A0219728U
 */

import java.util.*;

class AVLTree<Key, Value> {
  private Node root;
  public HashMap<Value, Key> hash;

  private class Node {
    private final Key key;
    private Value val;
    private int height;
    private int size;
    private Node left;
    private Node right;

    public Node(Key key, Value val, int height, int size) {
      this.key = key;
      this.val = val;
      this.size = size;
      this.height = height;
    }
  }

  class Key{
    public long point;
    public int index;

    public Key(long point, int index){
      this.point = point;
      this.index = index;
    }
  }

  public long compare(Key k1, Key k2){
    if (k1.point != k2.point){
      return k1.point - k2.point;
    }
    else{
      return k2.index-k1.index;
    }
  }

  public void query(Value val){
    if (get(val).point <= 0){
      System.out.println("Team " + val + " is ELIMINATED");
    }
    else{
      int rank = rank(root, val);
      System.out.println("Team " + val + ": " + get(val).point + " points, rank " + rank);
    }
  }

  public int rank(Node x, Value val){
    if (x == null){
      return 0;
    }
    long cmp = compare(x.key, get(val));
    if (cmp >= 0){
      if (x.left == null){
        return 1 + rank(x.right, val);
      }
      else{
        return 1 + x.left.size + rank(x.right, val);
      }
    }
    else{
      return rank(x.left, val);
    }
  }

  public int size() {
    return size(root);
  }

  private int size(Node x) {
    if (x == null)
      return 0;
    return x.size;
  }

  private int height(Node x) {
    if (x == null)
      return -1;
    return x.height;
  }

  public Key get(Value val){
    return hash.get(val);
  }

  public Value get(Key key) {
    Node x = get(root, key);
    if (x == null)
      return null;
    return x.val;
  }

  private Node get(Node x, Key key) {
    if (x == null)
      return null;
    long cmp = compare(x.key, key);
    if (cmp < 0)
      return get(x.left, key);
    else if (cmp > 0)
      return get(x.right, key);
    else
      return x;
  }

  public boolean contains(Key key) {
    return get(key) != null;
  }

  public void put(long point, int index, Value val) {
    Key key = new Key(point, index);
    if (val == null) {
      delete(key);
      return;
    }
    root = put(root, key, val);
    if (hash == null){
      hash = new HashMap<Value, Key>();
    }
    hash.put(val, key);
  }

  private Node put(Node x, Key key, Value val) {
    if (x == null)
      return new Node(key, val, 0, 1);
    long cmp = compare(x.key, key);
    if (cmp < 0) {
      x.left = put(x.left, key, val);
    } else if (cmp > 0) {
      x.right = put(x.right, key, val);
    } else {
      x.val = val;
      return x;
    }
    x.size = 1 + size(x.left) + size(x.right);
    x.height = 1 + Math.max(height(x.left), height(x.right));
    return balance(x);
  }

  private Node balance(Node x) {
    if (balanceFactor(x) < -1) {
      if (balanceFactor(x.right) > 0) {
        x.right = rotateRight(x.right);
      }
      x = rotateLeft(x);
    } else if (balanceFactor(x) > 1) {
      if (balanceFactor(x.left) < 0) {
        x.left = rotateLeft(x.left);
      }
      x = rotateRight(x);
    }
    return x;
  }

  private int balanceFactor(Node x) {
    return height(x.left) - height(x.right);
  }

  private Node rotateRight(Node x) {
    Node y = x.left;
    x.left = y.right;
    y.right = x;
    y.size = x.size;
    x.size = 1 + size(x.left) + size(x.right);
    x.height = 1 + Math.max(height(x.left), height(x.right));
    y.height = 1 + Math.max(height(y.left), height(y.right));
    return y;
  }

  private Node rotateLeft(Node x) {
    Node y = x.right;
    x.right = y.left;
    y.left = x;
    y.size = x.size;
    x.size = 1 + size(x.left) + size(x.right);
    x.height = 1 + Math.max(height(x.left), height(x.right));
    y.height = 1 + Math.max(height(y.left), height(y.right));
    return y;
  }

  public void delete(Key key) {
    if (!contains(key))
      return;
    root = delete(root, key);
  }

  private Node delete(Node x, Key key) {
    long cmp = compare(x.key, key);
    if (cmp < 0) {
      x.left = delete(x.left, key);
    } else if (cmp > 0) {
      x.right = delete(x.right, key);
    } else {
      if (x.left == null) {
        return x.right;
      } else if (x.right == null) {
        return x.left;
      } else {
        Node y = x;
        x = min(y.right);
        x.right = deleteMin(y.right);
        x.left = y.left;
      }
    }
    x.size = 1 + size(x.left) + size(x.right);
    x.height = 1 + Math.max(height(x.left), height(x.right));
    return balance(x);
  }

  private Node deleteMin(Node x) {
    if (x.left == null)
      return x.right;
    x.left = deleteMin(x.left);
    x.size = 1 + size(x.left) + size(x.right);
    x.height = 1 + Math.max(height(x.left), height(x.right));
    return balance(x);
  }

  private Node min(Node x) {
    if (x.left == null)
      return x;
    return min(x.left);
  }

  public void match(Value x, Value y, long a, long b){
    if (a != b){
      Key x_key = hash.get(x);
      Key y_key = hash.get(y);

      root = delete(root, x_key);
      root = delete(root, y_key);

      x_key.point = x_key.point + (a-b);
      y_key.point = y_key.point - (a-b);

      if (x_key.point > 0){
        root = put(root, x_key, x);
      }
      hash.put(x, x_key);

      if (y_key.point > 0){
        root = put(root, y_key, y);
      }
      hash.put(y, y_key);
    }
  }
}



public class Soccer {
	public static void main(String args[]) {
    Kattio io = new Kattio(System.in);
		AVLTree teams = new AVLTree();
    int command = io.getInt();
    int index = 0;

    for (int i = 0; i < command; i++){
      String c = io.getWord();

      if (c.equals("ADD")){
        String teamname = io.getWord();
        Long point = io.getLong();
        teams.put(point, index, teamname);
        index ++;
      }
      else if (c.equals("MATCH")){
        teams.match(io.getWord(), io.getWord(), io.getLong(), io.getLong());
      }
      else{
        teams.query(io.getWord());
      }
    }
  }
}
