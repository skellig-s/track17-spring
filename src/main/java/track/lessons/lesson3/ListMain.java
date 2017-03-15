package track.lessons.lesson3;

/**
 *
 */
public class ListMain {

    public static void main(String[] args) {
        MyArrayList list = new MyArrayList(5);
        System.out.println(list.size());
        list.add(10);
        list.add(11);
        list.add(12);
        System.out.println(list.size());

        list.remove(6);

        int[] array = { 1 , 2, 3, 4};
        int[] buff = new int[array.length - 1];
//        System.arraycopy(array, array.length, buff, 0, 1);
//        for (int i = 0; i < buff.length; i++){
//            System.out.println(buff[i]);
//        }
//        System.out.println("!!!!");
        for (int i = 0; i < list.size(); i++){
            System.out.println(list.get(i));
        }

    }
}
