/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eg.edu.alexu.csd.oop.db;

import static eg.edu.alexu.csd.oop.db.SQL.sql;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.stream.XMLStreamException;
import org.xml.sax.SAXException;

/**
 *
 * @author Mustafa
 */
public class databasemanager implements Database {
        
    public void dataparser(String query) throws XMLStreamException, FileNotFoundException, SQLException{
   int x= sql().query(query);
        
   if (x == 0 || x == 1){
       
       this.executeStructureQuery(query);
   }else if(x ==2 ||x == 3  ){
       this.executeUpdateQuery(query);
   }else if (x== 4){
       
       this.executeRetrievalQuery(query);
   }
    
   
    }

    @Override
    public boolean executeStructureQuery(String query) throws SQLException {
        try{
           
        int x = sql().query(query);
  
             
        return  (boolean) sql().CaptureTheQuery(x, query) ;
        
        }catch (FileNotFoundException | UnsupportedEncodingException | XMLStreamException e){
            return false;
        } catch (IOException ex) {
            return false;
        } catch (SAXException ex) {
            Logger.getLogger(databasemanager.class.getName()).log(Level.SEVERE, null, ex);
              return false;
        }
      
        
       
    }

    @Override
    public Object[][] executeRetrievalQuery(String query) throws SQLException {
            int x = 111;
      
  
             
        try {
            
            Object [][] f = sql().Select(query);
           // System.out.print(f.toString());
            return  f;
        } catch (XMLStreamException | FileNotFoundException ex) {
            Logger.getLogger(databasemanager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(databasemanager.class.getName()).log(Level.SEVERE, null, ex);
        }
    return null;
    }

    @Override
    public int executeUpdateQuery(String query) throws SQLException {
          int x = 55;
        try {
            x = sql().query(query);
        } catch (XMLStreamException | FileNotFoundException ex) {
            Logger.getLogger(databasemanager.class.getName()).log(Level.SEVERE, null, ex);
        }
  
             
        try {
            return  (int) sql().CaptureTheQuery(x, query) ;
        } catch (XMLStreamException | FileNotFoundException | UnsupportedEncodingException ex) {
            Logger.getLogger(databasemanager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(databasemanager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(databasemanager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
}
    
}
