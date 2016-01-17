package sql;

import java.text.DecimalFormat;
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
public class aggregateResult {
    static void aggregateFunction(ArrayList<String> tables, ArrayList<String> columns, HashMap<String, HashMap<String, ArrayList<Integer>>> outerHm,ArrayList<String> desired_col){
        int max = 0 , min = 0 , avg = 0 , sum = 0 , count = 0;
        int avg_var=0;
        HashMap<String , Integer> result= new HashMap<>();
        
        
        try{
            if( columns.get(0).toLowerCase().contains("sum") )
            {
                String xyz = columns.get(0).substring(columns.get(0).indexOf("(")+1) ;
                xyz = xyz.replace(")","");
                xyz.trim();
                
                sum = 1;
                xyz = xyz.substring(xyz.indexOf(".")+1);
                xyz.trim();
                
                ArrayList <Integer> temp = new ArrayList<>();
                temp =  outerHm.get(tables.get(0)).get(xyz)  ;  
                int colsum = 0;
                for(int j= 0 ; j< temp.size(); j++)
                {
                    colsum = colsum + temp.get(j) ;
                }
                System.out.println("sum("+tables.get(0)+"."+xyz+")");
                System.out.print(colsum);
            }
            if(columns.get(0).toLowerCase().contains("min")){
                min = 1;
                String xyz = columns.get(0).substring(columns.get(0).indexOf("(")+1) ;
                xyz = xyz.replace(")","");
                xyz.trim();
                xyz = xyz.substring(xyz.indexOf(".")+1);
                xyz.trim();
                
                ArrayList <Integer> temp = new ArrayList<>();
                temp =  outerHm.get(tables.get(0)).get(xyz) ;
                int mincol = temp.get(0);
                for(int j= 1 ; j< temp.size(); j++)
                {
                    if( temp.get(j) < mincol) 
                         mincol = temp.get(j);
                }
                System.out.println("min("+tables.get(0)+"."+xyz+")");
                System.out.print(mincol);
                
                //result.put(columns.get(i),mincol);
            }
            
            
            if(columns.get(0).toLowerCase().contains("max")){
                max = 1;
                String xyz = columns.get(0).substring(columns.get(0).indexOf("(")+1) ;
                xyz = xyz.replace(")","");
                xyz.trim();
                xyz = xyz.substring(xyz.indexOf(".")+1);
                xyz.trim();
                //System.out.println(s);
                //System.out.println(tables);
                //System.out.println(outerHm.get(tables.get(0)));
                ArrayList <Integer> temp = new ArrayList<>();
                temp =  outerHm.get(tables.get(0)).get(xyz) ;
                int maxcol = temp.get(0);
                for(int j= 1 ; j< temp.size(); j++)
                {
                    if( temp.get(j) > maxcol) 
                         maxcol = temp.get(j);
                }
                System.out.println("max("+tables.get(0)+"."+xyz+")");
                System.out.print(maxcol);
                //System.out.println("min");
                result.put(columns.get(0),maxcol);

            }
            
            if(columns.get(0).toLowerCase().contains("avg")){
                avg = 1;
                String xyz = columns.get(0).substring(columns.get(0).indexOf("(")+1) ;
                xyz = xyz.replace(")","");
                xyz.trim();
                xyz = xyz.substring(xyz.indexOf(".")+1);
                xyz.trim();
                ArrayList <Integer> temp = new ArrayList<>();
                
                temp =  outerHm.get(tables.get(0)).get(xyz)  ;  
//                System.out.println(temp.size());
                int colsum = 0;
                int size = temp.size();
                for(int j= 0 ; j< size; j++)
                {
                    colsum = colsum + temp.get(j) ;
                }
                avg_var = colsum/size;
                //DecimalFormat df = new DecimalFormat("#.0000");
                
                System.out.println("avg("+tables.get(0)+"."+xyz+")");
                System.out.print(avg_var);
              //  System.out.println(colsum/size) ;
                //System.out.println("avg");
                
//                result.put(columns.get(i),avg_var);
                
            }
            
            if(columns.get(0).toLowerCase().contains("count")){
                count = 1;
                String xyz = columns.get(0).substring(columns.get(0).indexOf("(")+1) ;
                xyz = xyz.replace(")","");
                xyz.trim();
                xyz = xyz.substring(xyz.indexOf(".")+1);
                xyz.trim();
                int count_var;
                if("*".equals(xyz)){
                    count_var = outerHm.get(tables.get(0)).get(xyz).size() ;
                }
                else{
                ArrayList <Integer> temp = new ArrayList<>();
                temp =  outerHm.get(tables.get(0)).get( xyz )  ;
                count_var = temp.size();
                }
                //result.put(columns.get(i),count_var);
                System.out.println("count("+tables.get(0)+"."+xyz+")");
                System.out.print(count_var);
            }
        }
        catch(Exception e)
        {
            System.out.println("error");
        }
    }
}
