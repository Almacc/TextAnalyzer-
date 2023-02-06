
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


public class TextReader {

    public static void main(String [] args) {

        Map<String, Integer> map = new HashMap<String, Integer>();
        Scanner scan = null;
        //	List <Map.Entry<String, Integer>> list = new ArrayList(map.entrySet());

        try {
            scan = new Scanner(new FileReader("theraven.txt"));
            while(scan.hasNext()) {
                String des = scan.next().toLowerCase().replaceAll("[^a-zA-Z]", "");
                if(map.containsKey(des)) {

                    map.put(des, map.get(des)+1);
                }else {
                    map.put(des, 1);
                }


            }
        }catch(IOException e) {
            e.printStackTrace();
            System.out.println("File not found");
        }finally {
            if (scan != null) {
                scan.close();
            }
        }

        Set<Entry<String, Integer>> entrySet = map.entrySet();

        List<Entry<String, Integer>> list = new ArrayList<>(entrySet);


        Collections.sort(list, new Comparator<Entry<String, Integer>>(){

            @Override
            public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
                // TODO Auto-generated method stub
                return o2.getValue().compareTo(o1.getValue());
            }

        });

        list.forEach(s->{
            System.out.println(s.getKey() + " = " + s.getValue());
        });


    }


}
