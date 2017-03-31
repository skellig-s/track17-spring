package track.lessons.lesson5generics;

import java.util.*;

import track.util.Util;

/**
 *
 */
public class Cypher {

    public static final int SYMBOL_DIST = 32;

    private Map<Character, Integer> readData(String data) {
        Map<Character, Integer> map = new HashMap<>();
        for (int i = 0; i < data.length(); i++) {
            char ch = data.charAt(i);
            if ((ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z')) {
                if (ch < 'Z') {
                    ch += SYMBOL_DIST;
                }
                // Если это буква, то собираем частотную информацию
                if (map.get(ch) != null) {
                    map.put(ch, map.get(ch) + 1);
                } else {
                    map.put(ch, 1);
                }


            }
        }
        return map;
    }

    /**
    На вход приходит текст
    1. Считываем readData() и получаем мапу {Символ -> Кол-во употреблений}
    2. Далее нам нужно отсортировать пары ключ-значение по значению
     (Называются{@code List<Map.Entry<Character, Integer>>})
     (то есть по частоте употребления). Для этого можно создать список этих пар и отсортировать список.
     У java.lang.List есть вспомогательный метод {@link List#sort(Comparator)}
     Где Comparator - это логика сравнения объектов.

     3. После того, как получен отсортированный список {@code List<Map.Entry<Character, Integer>>} нужно превратить его
        обратно в Map для того, чтобы иметь быстрый доступ get().

     */
    public Map<Character, Integer> buildHist(String data) {
        Map<Character, Integer> map = readData(data);

        List<Map.Entry<Character, Integer>> list = new ArrayList<>(map.entrySet());
        list.sort(((o1, o2) -> o2.getValue() - o1.getValue()));

        map = new LinkedHashMap<>();

        for (Map.Entry<Character, Integer> gg : list) {
            map.put(gg.getKey(), gg.getValue());
        }

        return map;
    }

    /**
     * Заменяем символы зашифрованного текста по таблицам частот
     *
     * @param in - отсортированный по частоте алфавит для основного текста
     * @param out - отсортированный по частоте алфавит для шифрованного текста
     * @param encrypted - зашифрованный текст
     * @return расшифрованный текст
     */
    public String merge(List<Character> in, List<Character> out, String encrypted) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < encrypted.length(); i++) {
            Character ch = encrypted.charAt(i);
            if (out.contains(ch)) {
                int current = out.indexOf(ch);
                builder.append(in.get(current));
            } else {
                builder.append(ch);
            }

        }
        return builder.toString();
    }

    public static void main(String[] args) {
        Cypher cypher = new Cypher();

        Map<Character, Integer> dataHist = cypher.buildHist(Util.readFile("data.txt"));

        String encryptedText = Util.encrypt(Util.readFile("toEncrypt.txt"));
        Map<Character, Integer> encryptedHist = cypher.buildHist(encryptedText);

        String result = cypher.merge(
                new LinkedList<>(dataHist.keySet()),
                new LinkedList<>(encryptedHist.keySet()),
                encryptedText);
        System.out.println(result);

    }

}



