
    public class Trie {
        // 1.addString
        // 2.removeString
        // 3.isContain(String)
        // 4.isContain(prefix)
        public static void main(String... args){
            System.out.println("HW");
            Trie trie=new Trie();
            String[] strings={"aab","aba","aaa","abab"};
            for(String s:strings){
                trie.add(s);
            }
            System.out.println(trie.isContainPerfect("aab"));

        }
        private static class Node {
            private int passCount = 0;// how many words pass this node.
            private int leafCount = 0;
            private final int len;
            private final Map<Character, Node> childs = new HashMap<>();
            private final Node parent;

            public Node(Node parent,int len) {
                this.parent = parent;
                this.len=len;
            }

            public boolean isLeafNode() {
                return leafCount > 0;
            }

            public void addPassNum() {
                this.passCount++;
            }

            public void minusPassNum() {
                if (passCount == 0)throw new IllegalArgumentException();
                this.passCount--;
            }

            public void addLeafNum() {
                this.leafCount++;
            }

            public void minusLeafNum() {
                if (leafCount == 0) throw new IllegalArgumentException();
                this.leafCount--;
            }

            public Node addChild(char c) {
                if (childs.containsKey(c)) return childs.get(c);
                Node child = new Node(this,this.len+1);
                childs.put(c, child);
                return child;
            }

            public Node searchChild(char c) {
                // nullable
                return childs.get(c);
            }
        }

        final private Node root = new Node(null,-1);

        public void add(String string) {
            Node currentNode = root;
            for (int i = 0; i < string.length(); i++) {
                currentNode = currentNode.addChild(string.charAt(i));
                currentNode.addPassNum();
            }
            currentNode.addLeafNum();
        }

        public boolean remove(String string) {
            Optional<Node> leafNode = searchNode(string);
            if (!leafNode.isPresent()) return false;
            Node node = leafNode.get();
            if (node.leafCount <= 0) return false;
            node.minusLeafNum();
            do {
                node.minusPassNum();
                node = node.parent;
            } while (node != root);
            return true;
        }

        private Optional<Node> searchNode(String string) {
            Optional<Node> currentNode = Optional.of(root);
            for(char c:string.toCharArray()){
                currentNode = currentNode.map(node -> node.searchChild(c));
            }
            return currentNode;
        }

        public boolean isContainPerfect(String string) {
            Optional<Node> currentNode = searchNode(string);
            return currentNode.isPresent() && currentNode.get().isLeafNode();
        }

        public boolean isContainPrefix(String string) {
            Optional<Node> currentNode = searchNode(string);
            return currentNode.isPresent() && currentNode.get().passCount > 0;
        }
    }