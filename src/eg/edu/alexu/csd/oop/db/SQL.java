/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eg.edu.alexu.csd.oop.db;
import static eg.edu.alexu.csd.oop.db.Controller.controller;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.stream.XMLStreamException;
import org.xml.sax.SAXException;

/**
 *
 * @author Mustafa
 */
public class SQL {
  private static SQL sql;
    final  Pattern createtable;
   final Pattern droptable ;
   final Pattern insertinto ;
   final Pattern deletefrom ;
   final Pattern select ;
     Pattern []patternarray = new Pattern[5] ;
    
   private SQL(){
        
        this.createtable = Pattern.compile("(?i)create\\s+table\\s+\\w+\\s*(.*);");
        this. droptable = Pattern.compile("(?i)drop\\s+table\\s+\\w+\\s*;");
        this.insertinto = Pattern.compile("(?i)insert\\s+into\\s+\\w+\\s*(.*)values\\s*(.*);");
        this.deletefrom = Pattern.compile("(?i)delete\\s+from\\s+\\w+\\s*where\\s*(.*);");
            this.select = Pattern.compile("(?i)select\\s*(.*)\\s*from\\s+\\w+\\s*(where\\s*(.*));");
        
      patternarray[0] = createtable;
      patternarray[1] = droptable;
      patternarray[2] = insertinto;
      patternarray[3] = deletefrom;
      patternarray[4] = select;

    }
     public static synchronized  SQL sql(){
       
        if (sql == null ){
            
            sql = new SQL();
        }
        
        return sql;
        
    }

    
 public int query (String query) throws XMLStreamException, FileNotFoundException{
      
     StringTokenizer token = new StringTokenizer(query);
     StringBuilder std = new StringBuilder();
     while (token.hasMoreTokens()){
         std.append(token.nextToken());
         std.append(" ");
         
     }
     
     for (int i = 0 ;i <patternarray.length;i++){
        // System.out.println(std.toString());
          Matcher m = patternarray[i].matcher(std.toString());
    
               if (m.find()){
                //   System.out.println(i);                   
// method i 
                return i;
               }
     }
      return 5000;
    
   
 }   
 
public Object CaptureTheQuery(int i, String query) throws XMLStreamException, FileNotFoundException, UnsupportedEncodingException, IOException, SAXException {

        switch (i) {
            case 0:
/*
                Matcher matcher = Pattern.compile("CREATE\\s+TABLE\\s+\"?(\\w+)").matcher(query);
                if (matcher.find()) {
                    String name = matcher.group(1); 
                    //System.out.print(name);
                }*/
                String name = null;
                Matcher matcher = Pattern.compile("(?i)\\s*CREATE\\s+TABLE?\\s+(\\w+)").matcher(query);
                if (matcher.find()) {
                     name = matcher.group(1); 
                 //  System.out.print(name + "\n");
                }
                StringBuilder a = new StringBuilder();
                boolean paren = false;
                for (char ch: query.toCharArray()) 
                {
                   if(ch == '(')
                   paren = true;
                   if(ch == ')')
                   paren = false;
                      if(paren == true&& ch !='(' && ch != '\n'  && ch!= '\t') 
                      {
                          a.append(ch);
                      }
                   
                   
                }
               // System.out.println(a.toString());
          //     String subject = "HELLO , THERE ,WORLD";
                String[] couple = a.toString().split(",");
               List<String> column_name = new ArrayList();

                List<String> data_types = new ArrayList(); 
               
               for(int z=0; z<couple.length; z++)
               {
                  //  System.out.print(couple[z]);
               Matcher iii = Pattern.compile("(?i)(\\w+)\\s*(\\w+)").matcher(couple[z]);
                // System.out.print(couple[z]);
                if (iii.find()) {
                   column_name.add(iii.group(1));
                   data_types.add(iii.group(2));
                }
              //  System.out.println(column_name[z]);
           //  column_name[z] = matcher.group(1);
             // data_types[z] = matcher.group(2);
               }
             //  System.out.println(Arrays.toString(data_types) +" "+ Arrays.toString(column_name)  );
               List<Map<String, String>> Data = new ArrayList();
            controller().Create(name, Data,column_name,data_types);
           //     System.out.println(name + " created successfully ");
                
                // creat table
                return true;
            case 1:
           matcher = Pattern.compile("(?i)\\s*DROP\\s+TABLE?\\s(\\w+);").matcher(query);

                 if (matcher.find()) {
                     name = matcher.group(1); 
                     Controller.controller().Drop(name);
                     Controller.controller().Dropsxd(name);
                 //   System.out.print(name+ " Dropped");
                } 
                 return true ;

            case 2:
                
                 
                // insert into table
                
                int parenth_lvl = 0;
                a = new StringBuilder();
                StringBuilder b =  new StringBuilder();
                 matcher = Pattern.compile("(?i)\\s*INSERT\\s*INTO\\s*(\\w+)\\s*").matcher(query);
              
                if (matcher.find()) {
                     name = matcher.group(1); 
                    for (char ch: query.toCharArray()) 
                {
                   if(ch == '(')
                   parenth_lvl++;
                   if(ch == ')')
                   {
                    parenth_lvl++;
                   }
                      if(parenth_lvl ==1&& ch !='(' && ch != '\n' && ch != ' '  && ch!= '\t') 
                      {
                          a.append(ch);
                      }
                        if(parenth_lvl ==3&& ch !='(' && ch != '\n' && ch != ' '  && ch!= '\t') 
                      {
                         b.append(ch);
                      }
                      
                }
                    String[] Columns = a.toString().split(",");
                    String[] Values = b.toString().split(",");
                    Map<String,String> new_data = new LinkedHashMap<String,String>();
                    for( i =0; i<Columns.length;i++ )
                    {
                        new_data.put(Columns[i], Values[i]);
                    }
                    
                           
                      Controller.controller().Insert(name, new_data);
                       return i;
                  //  System.out.println(Arrays.toString(Values) +" " + Arrays.toString(Columns));
                    
                }
                
                
                

                break;
            case 3:
                // delete from
                
                 matcher = Pattern.compile("(?i)\\s*DELETE\\s+FROM\\s*(\\w+)\\s*where\\s*(\\w+)\\s*(=|<|>)\\s*(\\w+)\\s*;").matcher(query);
                if (matcher.find()) {
                     name = matcher.group(1); 
                    String op = matcher.group(3);
                    String LHS = matcher.group(2);
                     String RHS = matcher.group(4);
                     
                     //Delete (String tableName ,char operator, String col, String val )
                     char[] operand = op.toCharArray();
                       System.out.print( name + " " +LHS + " "+op  + " " + RHS );
                    return  Controller.controller().Delete(name, operand[0], LHS, RHS);
                
                }
                

            case 4:
                // selsct from
                
                
            this.Select(query);
       
        }
      return null;

    }

public Object [][] Select(String query) throws XMLStreamException, FileNotFoundException{
      
    
    
   Matcher matcher = Pattern.compile("(?i)\\s*SELECT\\s*((\\w+)\\s*,\\s*|(\\w+))*\\s*FROM\\s*(\\w+)\\s*WHERE\\s*(\\w+)\\s*(=|<|>)\\s*(\\w+)\\s*;").matcher(query);
                if (matcher.find()) {
                    String name = null;
                     name = matcher.group(4); 
                    String op = matcher.group(6);
                    String LHS = matcher.group(5);
                     String RHS = matcher.group(7);
       //    System.out.print( name + " " +LHS + " "+op  + " " + RHS );
       //      System.out.print( Arrays.toString(Columns_names) );
       StringBuilder a = new StringBuilder();
       StringBuilder b = new StringBuilder();
                boolean flag = false;
                 for (char ch: query.toCharArray()) 
                {
                      a.append(ch);
                      if(flag == true && ch != ' ' )
                           b.append(ch);
                      if(a.toString().equalsIgnoreCase("select") )
                      {
                          flag = true;                     
                      }
                      
                      
                }
                  String[] Columns = b.toString().split(",");
                 matcher = Pattern.compile("(?i)\\s*(\\w+)\\s*FROM").matcher(Columns[Columns.length-1]);
                 if (matcher.find()) {
                    Columns[Columns.length-1] = matcher.group(1);
                }
                   
                    //(String tableName, String[] columns, char operator, String col, String val)
                  char[]  operand = op.toCharArray();
                //   System.out.print( Arrays.toString(Columns) +name + RHS +operand[0] + LHS+ " " );
                      
                      return Controller.controller().Select(name, Columns ,operand[0] , LHS , RHS );
                }
      return null;
    
    
    
}

}
