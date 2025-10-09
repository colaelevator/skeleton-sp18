public class Palindrome {
    public Deque<Character> wordToDeque(String word) {
        LinkedListDeque<Character> d = new LinkedListDeque<>();
        for(int i = 0;i < word.length(); i++) {
            d.addLast(word.charAt(i));
        }
        return d;
    }

    public boolean isPalindrome(String word) {
        if(word.isEmpty() || word.length() == 1) {
            return true;
        }
        for (int i = 0; i <= word.length()/2; i++) {
            if (word.charAt(i) != word.charAt(word.length()-1-i)) {
                return false;
            }
        }
        return true;
    }

    public boolean isPalindrome(String word, CharacterComparator cc) {
        if(word.isEmpty() || word.length() == 1) {
            return true;
        }
        for (int i = 0; i < word.length()/2; i++) {
            if (!cc.equalChars(word.charAt(i),word.charAt(word.length()-1-i))) {
                return false;
            }
        }
        return true;
    }
}
