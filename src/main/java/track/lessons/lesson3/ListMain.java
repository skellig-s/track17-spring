package track.lessons.lesson3;

/**
 *
 */
public class ListMain {

    public static void main(String[] args) {
        List list = new MyLinkedList();
        list.add(1);
        list.add(2);
        list.add(3);

        System.out.println( list.size());

        System.out.println( list.get(0));
        System.out.println( list.get(1));
        System.out.println( list.get(2));

        list.remove(1);//
        System.out.println("!!!" + list.size());
        System.out.println( list.get(1));
        System.out.println( list.get(0));

        list.remove(1);
        list.remove(0);


        System.out.println(list.size() == 0);
    }
}