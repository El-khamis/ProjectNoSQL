import java.util.ArrayList;

public class Outils {

    String maVariable;

    public void parse_requete(String s) {
        s = s.replaceAll("\t", "");
        String[] corps = s.split("\\{");
        String[] entete = corps[0].split(" ");
        String[] bodysplit;
        String body;
        ArrayList<String> predicats = new ArrayList<>();
        ArrayList<String> objets = new ArrayList<>();


        for (String s1 : entete) {
            if (s1.contains("?")) {
                maVariable = s1;
            }
        }

        for (String s1 : corps[1].split(" ")) {
            if (s1.contains("?") && !s1.equals(maVariable)) {
                System.out.println("La requete n'est pas une requete étoile\n");
                return;
            }
        }
        body = corps[1];
        bodysplit = body.replace("}","").split(" \\. ");

        String temp[];
        for(String s1: bodysplit ) {
            temp = s1.split(" ");

            if(temp[0].isEmpty())
            {
                objets.add(temp[3]);

                predicats.add(temp[2]);

            }
            else {
            objets.add(temp[2]);

            predicats.add(temp[1]);
            }

        }
            System.out.println("La requete est une requete étoile dont la variable est "+maVariable+" et dont les prédicats sont :"+ predicats.toString()+"\n");
        System.out.println("La requete est une requete étoile dont la variable est "+maVariable+" et dont les objets sont :"+ objets.toString()+"\n");
            predicats.clear();
        }


    }
