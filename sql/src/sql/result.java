package sql;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author sambhav
 */
public class result {

    static void print_result(ArrayList<String> tables, ArrayList<String> columns,String whereCon, HashMap<String, HashMap<String, ArrayList<Integer>>> outerHm,ArrayList<String> desired_col) {   
       // System.out.println(tables);
       // System.out.println(columns);
        //if(tables.size() == 1)
        
        if(SqlParser.whereFlag == 1)
        {
            whereClause.whereResult(tables,columns,whereCon, outerHm,desired_col);
            return ;
        }
        
        if("*".equals(columns.get(0)) ){
            if(tables.size() == 1){
                String table_name = tables.get(0);
                HashMap<String, ArrayList<Integer>> innerHm = new HashMap<>();

                innerHm = outerHm.get(table_name);
                //System.out.println(desired_col) ;
                int noOfRows = innerHm.get(desired_col.get(0)).size() ;
               // System.out.println("No of row : "+noOfRows);
                //System.out.println(desired_col);
                for(int i = 0 ;i< desired_col.size() ; i++)
                {
                    System.out.print(table_name+"."+desired_col.get(i));
                    if( i+1 != desired_col.size() ){
                            System.out.print(",");
                        }
                }
                System.out.println("");
                for(int i = 0; i<noOfRows ;i++)
                {
                    for(int j= 0 ;j<desired_col.size() ; j++)
                    {
                        System.out.print( innerHm.get(desired_col.get(j)).get(i)  );
                        if( j+1 != desired_col.size() ){
                            System.out.print(",");
                        }
                    }
                    if(i+1 != noOfRows)
                        System.out.print("\n");
                }
                return;
            }
            
        }
        // handing when aggregate function occurs
        int have_aggregate = 0;
        for( int i = 0 ; i<columns.size() ;i++ ){
            if( columns.get(i).toLowerCase().contains("max") ){
                have_aggregate = 1;
               // System.out.println("max");
            }
            if(columns.get(i).toLowerCase().contains("min")){
                have_aggregate = 1;
               // System.out.println("min");
            }
            if(columns.get(i).toLowerCase().contains("avg") ){
                have_aggregate = 1;
               // System.out.println("avg");
            }
            if(columns.get(i).toLowerCase().contains("sum")){
                have_aggregate = 1;
              //  System.out.println("sum");
            }
            if(columns.get(i).toLowerCase().contains("count")){
                have_aggregate = 1;
              //  System.out.println("count");
            }
        }
        if(have_aggregate == 1){
            aggregateResult.aggregateFunction(tables, columns, outerHm,desired_col);
            return ;
        }
        
        
        
//            for(int i = 0 ;i<columns.size() ;i++)
//            {
//                if(columns.get(i).contains("("))
//                {
//                    String str = columns.get(i).replace("(","");
//                    str = str.replace(")","");
//                    columns.set( i, str) ;
//                }
//            }
        
        
        
            if(SqlParser.distinct_flag == 1) 
           {
            String column_name = columns.get(0);
            column_name = column_name.replace("(","");
            column_name = column_name.replace(")","");
            System.out.println("distinct("+tables.get(0)+"."+column_name+")");
            HashSet s =new HashSet(outerHm.get(tables.get(0)).get(column_name) );
            for (Iterator it = s.iterator(); it.hasNext();) {
                System.out.println(it.next());
            }
            return ;
            }    
        
        //handline of where clause
        
        
        
        // handling of multiple columns in single table like "select eid , age from emp";
        if(tables.size() == 1 ) 
            multipleColumnsSingleTable.fun(tables, columns,outerHm,desired_col);
        
    }
}
