
class KeyValuePair{
    private String key;
    private String value;

    public KeyValuePair(String key, String value){
        this.key = key;
        this.value = value;
    }

    public String getKey(){
        return key;
    }

    public String getValue(){
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

class SplayTree{
    private class Node{
        KeyValuePair data;
        Node left, right, parent;

        Node(KeyValuePair data){
            this.data = data;
            left = right = parent = null;
        }
    }

    private Node root;

    public int compare(KeyValuePair a, KeyValuePair b){
        return a.getKey().compareTo(b.getKey());
    }

    private void rightRotate(Node x){
        Node y = x.left;
        if (y != null){
            x.left = y.right;
            if (y.right != null){
                y.right.parent = x;
            }
            y.parent = x.parent;
        }
        if (x.parent == null) {
            root = y;
        } else if (x == x.parent.left) {
            x.parent.left = y;
        }
        else{
            x.parent.right = y;
        }
        if(y != null){
            y.right = x;
            x.parent = y;
        }
    }

    private void leftRotate(Node x){
        Node y = x.right;
        if(y != null){
            x.right = y.left;
            if (y.left != null) {
                y.left.parent = x;
            }
            y.parent = x.parent;
        }
        if (x.parent == null){
            root = y;
        }
        else if (x == x.parent.left) {
            x.parent.left = y;
        }
        else{
            x.parent.right = y;
        }
        if (y != null) {
            y.left = x;
            x.parent = y;
        }
    }

    private void splay(Node x){
        while(x.parent != null){
            if (x.parent.parent == null){
                if (x == x.parent.left){
                    rightRotate(x.parent);
                }
                else{
                    leftRotate(x.parent);
                }
            } else if (x == x.parent.left && x.parent == x.parent.parent.left) {
                rightRotate(x.parent.parent);
                rightRotate(x.parent);
            }
            else if(x == x.parent.right && x.parent == x.parent.parent.right){
                leftRotate(x.parent.parent);
                leftRotate(x.parent);
            }
            else if (x == x.parent.right && x.parent == x.parent.parent.left){
                leftRotate(x.parent);
                rightRotate(x.parent);
            }
            else{
                rightRotate(x.parent);
                leftRotate(x.parent);
            }
        }
    }

    public void insert(KeyValuePair item){
        Node currentNode = root, parent = null;
        while (currentNode != null){
            parent = currentNode;
            int cmp = compare(item, currentNode.data);
            if (cmp < 0){
                currentNode = currentNode.left;
            } else if (cmp > 0) {
                currentNode = currentNode.right;
            }
            else{
                return;
            }
        }
        Node newNode = new Node(item);
        newNode.parent = parent;
        if (parent == null){
            root = newNode;
        }
        else if(compare(item, parent.data) < 0){
            parent.left = newNode;
        }
        else{
            parent.right = newNode;
        }
        splay(newNode);
    }
    public KeyValuePair get(KeyValuePair item){
        Node currentNode = root;
        while (currentNode != null) {
            int cmp = compare(item, currentNode.data);
            if (cmp == 0){
                splay(currentNode);
                return currentNode.data;
            } else if (cmp < 0) {
                currentNode = currentNode.left;
            }
            else{
                currentNode = currentNode.right;
            }
        }
        return null;
    }

    public void delete(KeyValuePair item){
        KeyValuePair toDelete = get(item);
        if (toDelete == null){
            return;
        }

        Node currentNode = root;

        if (currentNode.left == null){
            root = currentNode.right;
            if (root != null){
                root.parent = null;
            }
        } else if (currentNode.right == null) {
            root = currentNode.left;
            root.parent = null;
        }
        else {
            Node leftSubtree = currentNode.left;
            leftSubtree.parent = null;

            Node minNode = currentNode.right;
            while (minNode.left != null) {
                minNode = minNode.left;
            }

            splay(minNode);

            minNode.left = leftSubtree;
            if (leftSubtree != null) {
                leftSubtree.parent = minNode;
            }
            root = minNode;
        }
    }
}

class TreeMap{
    private SplayTree tree;

    TreeMap() {
        tree = new SplayTree();
    }
    public void insert(String key, String value){
        KeyValuePair pair = new KeyValuePair(key, value);
        tree.insert(pair);
    }

    public String get(String key){
        KeyValuePair searchPair = new KeyValuePair(key, "");
        KeyValuePair foundPair = tree.get(searchPair);
        if (foundPair != null){
            return foundPair.getValue();
        }
        return "";
    }
    public String delete(String key){
        KeyValuePair deletePair = new KeyValuePair(key, "");
        KeyValuePair found = tree.get(deletePair);
        if(found == null){
            return "";
        }
        tree.delete(deletePair);
        return found.getValue();
    }
}

class Main{
    public static void main(String[] args){
        TreeMap map = new TreeMap();

        map.insert("keyOne", "valueOne");
        map.insert("keyTwo", "valueTwo");
        map.insert("keyThree", "valueThree");

        System.out.println(map.get("keyOne"));
        System.out.println(map.get("keyThree"));
        System.out.println(map.get("keyDoesNotExist"));

        System.out.println(map.delete("keyOne"));
    }
}



