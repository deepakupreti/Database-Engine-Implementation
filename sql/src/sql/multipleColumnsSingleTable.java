package sql;



import java.util.ArrayList;
import java.util.HashMap;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author sambhav
 */
public class multipleColumnsSingleTable {
    static void fun(ArrayList<String> tables, ArrayList<String> columns, HashMap<String, HashMap<String, ArrayList<Integer>>> outerHm,ArrayList<String> desired_col){
//        System.out.println(tables);    
//        System.out.println(columns);
//        ArrayList<String> actualColInQuery = new ArrayList<String>(columns);
        //int colNameHaveDot = 0;
        for(int i = 0 ;i<columns.size() ;i++)
        {
            if(columns.get(i).contains("."))
            {
                columns.set( i, columns.get(i).substring(columns.get(i).indexOf(".")+1)) ;
            }
        }
        //System.out.println(columns);
        try
        {    
            int noOfRows = outerHm.get(tables.get(0)).get(columns.get(0)).size() ;
                for(int i = 0 ;i <columns.size(); i++){
                    System.out.print(tables.get(0)+"."+columns.get(i));
                    if(i+1 != columns.size())
                        System.out.print(",");

            System.out.println("");
            }
            //System.out.println(noOfRows);
            for(int i = 0 ; i<noOfRows ; i++){
                for(int j = 0 ; j < columns.size(); j++){
                    System.out.print( outerHm.get(tables.get(0)).get(columns.get(j)).get(i)  );
                    if(j+1 != columns.size())
                        System.out.print(",");
                }
                if(i+1 != noOfRows)
                    System.out.println("");
            }
    
        }
        catch(Exception e)
        {
            System.out.println("error");
            return;
        }

    }
}

