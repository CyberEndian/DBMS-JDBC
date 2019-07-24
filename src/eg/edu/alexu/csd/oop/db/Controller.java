/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eg.edu.alexu.csd.oop.db;

import static eg.edu.alexu.csd.oop.db.Controller.controller;
import static eg.edu.alexu.csd.oop.db.xmlManager.managergetter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.xml.stream.XMLStreamException;
import org.xml.sax.SAXException;

/**
 *
 * @author Manny
 */
class Controller {

    private static Controller controller;

    private Controller() {

    }

    public static synchronized Controller controller() {

        if (controller == null) {

            controller = new Controller();
        }

        return controller;

    }

    public void Create(String tableName, List<Map<String, String>> Data, List<String> names, List<String> types) throws FileNotFoundException, UnsupportedEncodingException {
        managergetter().create(Data, tableName);
        managergetter().CreateXSD(tableName, names, types);

    }

    public void Drop(String tableName) throws IOException {

        Path path = Paths.get("Data//" + tableName + ".xml");

        Files.delete(path);

    }

    public void Dropsxd(String tableName) throws IOException {

        Path path2 = Paths.get("Data//" + tableName + ".xsd");

        Files.delete(path2);

    }

    public void Select(String tableName, String[] columns) throws XMLStreamException, FileNotFoundException {

        List<  Map<String, String>> x = managergetter().read(tableName);
        // System.out.print(x.get(0).get("name"));
        for (int i = 0; i < x.size(); i++) {
            for (Map.Entry<String, String> entry : x.get(i).entrySet()) {
                String key = entry.getKey();
                String tab = entry.getValue();
                if (Arrays.asList(columns).contains(key)) {
                    //  System.out.print(x.get(i).get(key) + " " ); //<<<<
                }
            }
        }
        System.gc();
    }

    public String [][] Select(String tableName, String[] columns, char operator, String col, String val) throws XMLStreamException, FileNotFoundException {

        try {

            List<  Map<String, String>> x = managergetter().read(tableName);
            String[][] Data = new String[columns.length][x.size()];
          List<List<String>> listOfLists = new ArrayList<List<String>>();
                    for(int i=0; i<columns.length;i++)
                    {
                        listOfLists.add(new ArrayList<String>());
                    }
            
            
            // System.out.print(x.get(0).get("name"));
            for (int i = 0; i < x.size(); i++) {
                for (Map.Entry<String, String> entry : x.get(i).entrySet()) {
                    String key = entry.getKey();
                    String tab = entry.getValue();
                    switch (operator) {
                        case '=':
                            if (Arrays.asList(columns).contains(key) && val.equalsIgnoreCase(x.get(i).get(col))) {
                                listOfLists.get(Arrays.asList(columns).indexOf(entry.getKey())).add(tab);
                            //   System.out.print(" " +  x.get(i).get(key) + "\n");//<<<<
                            }
                            break;
                        case '>':
                            if (Arrays.asList(columns).contains(key) && Integer.parseInt(x.get(i).get(col)) > Integer.parseInt(val)) {
                                 listOfLists.get(Arrays.asList(columns).indexOf(entry.getKey())).add(tab);
                             //  System.out.print(" " +  x.get(i).get(key) + "\n");//<<<<
                            }
                            break;
                        case '<':
                            if (Arrays.asList(columns).contains(key) && Integer.parseInt(x.get(i).get(col)) < Integer.parseInt(val)) {
                                 listOfLists.get(Arrays.asList(columns).indexOf(entry.getKey())).add(tab);
                              // System.out.print(" " +  x.get(i).get(key) + "\n");//<<<<
                            }
                            break;

                      //  System.out.print(x.get(i).get(key));    
                    }
                     

                    
                    
                    //System.gc();
                }
                 
            }
            
            
                String[][] array = new String[listOfLists.size()][];
                String[] blankArray = new String[0];
                for(int i=0; i < listOfLists.size(); i++) {
                    array[i] = listOfLists.get(i).toArray(blankArray);
                }
                    
                    System.out.println(Arrays.deepToString(array));
                return array;

     
        } catch (NumberFormatException e) {
        return null;
        }

    }

       public  void Insert (String tableName , Map<String, String> Data ) throws FileNotFoundException, XMLStreamException, SAXException, IOException
    {
      
         List<  Map<String, String>> x = managergetter().read(tableName);
         List<  Map<String, String>> y = new ArrayList();
         for (int i=0 ; i < x.size();i++)
             y.add(x.get(i));
             
             
         x.add(Data);
         
         managergetter().create(x, tableName);
         System.out.print(y);
         System.out.print("here");
         System.out.print(x);
         
            if (managergetter().validation(tableName) == true){
            }else {
                
              managergetter().create(y, tableName);
             System.out.println("File not valid");
         }
    
    }
       public int Delete(String tableName, char operator, String col, String val) throws FileNotFoundException, XMLStreamException {
        //    try {

        List<  Map<String, String>> x = managergetter().read(tableName);
        List<  Map<String, String>> deleting = new ArrayList<Map<String, String>>();
        // System.out.print(x.get(0).get("name"));
        for (int i = 0; i < x.size(); i++) {
            for (Map.Entry<String, String> entry : x.get(i).entrySet()) {
                String key = entry.getKey();
                String tab = entry.getValue();
                switch (operator) {
                    case '=':
                        if (val.equalsIgnoreCase(x.get(i).get(col))) {
                            System.out.print(" " + x.get(i).get(key) + " ");//<<<<
                            deleting.add(x.get(i));
                        }

                        break;
                    case '>':
                        if (Integer.parseInt(x.get(i).get(col)) > Integer.parseInt(val)) {
                            System.out.print(" " + x.get(i).get(key) + " ");//<<<<
                            deleting.add(x.get(i));
                        }
                        break;
                    case '<':
                        if (Integer.parseInt(x.get(i).get(col)) < Integer.parseInt(val)) {
                            System.out.print(" " + x.get(i).get(key) + " "); //<<<<
                            deleting.add(x.get(i));
                        }
                        break;

                    // System.out.print(x.get(i).get(key));    
                    }
            }
        }
        for (int z = 0; z < deleting.size(); z++) {
            x.remove(deleting.get(z));
        }
        managergetter().create(x, tableName);

        return deleting.size();

        //   System.gc();
    }

}