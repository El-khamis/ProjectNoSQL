import java.util.TreeMap;
import java.util.TreeSet;

public class Index {
    String name;
    TreeMap<Integer, TreeMap<Integer,TreeSet<Integer>>> index =new TreeMap<Integer,TreeMap<Integer,TreeSet<Integer>>>();
    TreeMap<Integer, TreeMap<Integer,Long>> statistique = new TreeMap<Integer, TreeMap<Integer,Long>>();

    Index(String name, TreeMap<Integer, TreeMap<Integer,TreeSet<Integer>>> index){
        this.name = name;
        this.index = index;
    }

        // <p, o, stat>
    public void calcStat(int taillemax){
      long stat;



/*
       for(int i=0 ;i<index.size();i++)
        {
            for (int j=0;j<index.get(i).size();j++)
            {
                stat=(index.get(i).get(j).size())/taillemax;

                if(statistique.containsKey(index.get(i)))
                {
                   statistique.get(index.get(i)).put(index.get(i).,stat);


                }
                else
                {



                }
*/




            }

        }




