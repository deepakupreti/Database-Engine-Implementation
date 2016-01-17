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
    public class whereClause 
    {
    static void whereResult(ArrayList<String> tables, ArrayList<String> columns,String whereCon, HashMap<String, HashMap<String, ArrayList<Integer>>> outerHm,ArrayList<String> desired_col){
        try
        {
        if(tables.size() == 1)
        {
            int singleCon = 0 , doubleCon = 0 , andFlag = 0 , orFlag = 0;
            if( whereCon.toLowerCase().contains(" and ")    )
            {
                doubleCon = 1 ;
                andFlag = 1 ;
            }
            else if( whereCon.toLowerCase().contains(" or ")   )
            {
                doubleCon = 1;
                orFlag = 1;
            }
            else
            {
                singleCon = 1;
            }
            
            if(singleCon == 1)
            {
                for(int i = 0 ;i<columns.size() ;i++)
                {
                    if(columns.get(i).contains("."))
                    {
                        columns.set( i, columns.get(i).substring(columns.get(i).indexOf(".")+1)) ;
                    }
                }
                int gt =0 , lt = 0 , lte = 0, gte=0, eq =0 ;
                String [] colAndCondition = null;
                if( whereCon.contains(">=") )
                {
                    gte = 1;
                    colAndCondition = whereCon.split(">=");
                }
                if(whereCon.contains("<=") )
                {
                    lte = 1;
                    colAndCondition = whereCon.split("<=");
                }
                
                if(whereCon.contains("=") && gte == 0 && lte ==0){
                    eq = 1;
                    colAndCondition = whereCon.split("=");
                }
                if( whereCon.contains(">")  && gte != 1){
                    gt = 1;
                    colAndCondition = whereCon.split(">");
                }
                if( whereCon.contains("<")  && lte != 1){
                    lt = 1;
                    colAndCondition = whereCon.split("<");
                }
                //System.out.println(">= , <= , = , > , < "+gte+lte+eq+lt+gt);
                int secondOperandIsString = 0;
                String columnName = colAndCondition[0].trim();
                Integer value = null ;
                columnName = columnName.substring(columnName.indexOf(".")+1);
                String secondColumnName = null ;
                //System.out.println(columnName);
                try
                {    
                    value = Integer.parseInt(colAndCondition[1].trim() );
                }
                catch(NumberFormatException e)
                {
                    secondColumnName = colAndCondition[1].trim() ;
                    secondOperandIsString =1;
                }
                
                if(secondOperandIsString == 0)
                {    
                String tableName  = tables.get(0);
                int rows =0;
                if(columns.get(0).equals("*"))
                {
                    //System.out.println("test1");
                    rows = outerHm.get(tableName).get(desired_col.get(0)).size();
                }
                else
                    rows = outerHm.get(tableName).get(columns.get(0)).size();
                
                int cols = outerHm.get(tableName).keySet().size();
               // System.out.println("rows are :"+rows);
                //prepare new empty hashMap
                HashMap<String , HashMap<String,ArrayList<Integer>>> tempOuterHm = new HashMap<>();
                HashMap<String, ArrayList<Integer>> tempInnerHm = new HashMap<>();
                for(int i = 0 ;i<cols ; i++)
                {
                    String s = desired_col.get(i);
                    ArrayList<Integer> temp= new ArrayList<>();
                    tempInnerHm.put(s, temp);
                }
                tempOuterHm.put(tableName, tempInnerHm);
                
                for (int i = 0 ; i<rows ; i++)
                {
                    if(gte ==1){
                        if( outerHm.get(tableName).get(columnName).get(i) >= value  )
                        {
                      //      System.out.println( outerHm.get(tableName).get(columnName).get(i) );
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    
                    if(eq ==1){
                        if( outerHm.get(tableName).get(columnName).get(i).equals(value)  )
                        {
                            //System.out.println(value);
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    
                    if(lt ==1){
                        if( outerHm.get(tableName).get(columnName).get(i) < value  )
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(gt ==1){
                        if( outerHm.get(tableName).get(columnName).get(i) > value  )
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                }
                if(  "*".equals (columns.get(0) ) )
                {
                    //code to print all columns
                    for(int i = 0 ;i<desired_col.size();i++)
                    {
                        System.out.print(tables.get(0)+"."+desired_col.get(i) );
                        if( i+1 != desired_col.size() )
                            System.out.print(",");
                    }
                    System.out.println("");
                    
                   int resultRows = tempOuterHm.get(tableName).get(desired_col.get(0)).size();
                    //System.out.println(resultRows);
                    for(int m = 0 ; m<resultRows ;m++){
                        for(int n = 0 ; n < desired_col.size() ; n++){
                            System.out.print(tempOuterHm.get(tableName).get(desired_col.get(n)).get(m) );
                            if(n+1 != desired_col.size())
                                System.out.print(",");
                        }
                        if( m+1 != resultRows )
                            System.out.println("");
                    }
                }
                else
                {
                    for(int i = 0 ;i<columns.size();i++)
                    {
                        System.out.print(tables.get(0)+"."+columns.get(i) );
                        if(i+1!=columns.size())
                             System.out.print(",");
                    }
                    System.out.println("");
                    //System.out.println( tempOuterHm.get(tableName) );
                    int resultRows = tempOuterHm.get(tableName).get(columns.get(0)).size();
                    for(int m = 0 ; m<resultRows ;m++){
                        for(int n = 0 ; n < columns.size() ; n++){
                            System.out.print(tempOuterHm.get(tableName).get(columns.get(n)).get(m) );
                            if( n+1!=columns.size() )
                                System.out.print(",");
                        }
                        if(m+1!=resultRows)
                            System.out.println("");
                    }
                }
            }
                else
                {
                    //System.out.println(secondColumnName);
                    secondColumnName = secondColumnName.substring(secondColumnName.indexOf(".")+1);
                    //System.out.println(secondColumnName);
                    
                    /////////////////////////////////////////////////////////////////////////////////////////
                String tableName  = tables.get(0);
                int rows =0;
                if(columns.get(0).equals("*")){
                    //System.out.println("test1");
                    rows = outerHm.get(tableName).get(desired_col.get(0)).size();
                }
                else
                    rows = outerHm.get(tableName).get(columns.get(0)).size();
                
                int cols = outerHm.get(tableName).keySet().size() ;
                //prepare new empty hashMap
                HashMap<String , HashMap<String,ArrayList<Integer>>> tempOuterHm = new HashMap<>();
                HashMap<String, ArrayList<Integer>> tempInnerHm = new HashMap<>();
                for(int i = 0 ;i<cols ; i++)
                {
                    String s = desired_col.get(i) ;
                    
                    ArrayList<Integer> temp= new ArrayList<>() ;
                    tempInnerHm.put(s, temp);
                }
                tempOuterHm.put(tableName, tempInnerHm);
                
                for (int i = 0 ; i<rows ; i++)
                {
                    if(gte ==1){
                        if( outerHm.get(tableName).get(columnName).get(i) >=  outerHm.get(tableName).get(secondColumnName).get(i) )
                        {
                      //      System.out.println( outerHm.get(tableName).get(columnName).get(i) );
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    
                    if(eq ==1){
                        if( outerHm.get(tableName).get(columnName).get(i).equals(outerHm.get(tableName).get(secondColumnName).get(i))  ) 
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    
                    if(lt ==1){
                        if( outerHm.get(tableName).get(columnName).get(i) < outerHm.get(tableName).get(secondColumnName).get(i)  )
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(gt ==1){
                        if( outerHm.get(tableName).get(columnName).get(i) > outerHm.get(tableName).get(secondColumnName).get(i)  )
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                }
                if(  "*".equals (columns.get(0) ) )
                {
                    //code to print all columns
                    for(int i = 0 ;i<desired_col.size();i++)
                    {
                        System.out.print(tables.get(0)+"."+desired_col.get(i) );
                        if( i+1 != desired_col.size() )
                            System.out.print(",");
                    }
                    System.out.println("");
                    
                   int resultRows = tempOuterHm.get(tableName).get(desired_col.get(0)).size();
                    //System.out.println(resultRows);
                    for(int m = 0 ; m<resultRows ;m++){
                        for(int n = 0 ; n < desired_col.size() ; n++){
                            System.out.print(tempOuterHm.get(tableName).get(desired_col.get(n)).get(m) );
                            if(n+1 != desired_col.size())
                                System.out.print(",");
                        }
                        if( m+1 != resultRows )
                            System.out.println("");
                    }
                }
                else
                {
                    for(int i = 0 ;i<columns.size();i++)
                    {
                        System.out.print(tables.get(0)+"."+columns.get(i) );
                        if(i+1!=columns.size())
                             System.out.print(",");
                    }
                    System.out.println("");
                    //System.out.println( tempOuterHm.get(tableName) );
                    int resultRows = tempOuterHm.get(tableName).get(columns.get(0)).size();
                    for(int m = 0 ; m<resultRows ;m++){
                        for(int n = 0 ; n < columns.size() ; n++){
                            System.out.print(tempOuterHm.get(tableName).get(columns.get(n)).get(m) );
                            if(n+1!=columns.size())
                                System.out.print(",");
                        }
                        if(m+1!=resultRows)
                            System.out.println("");
                    }
                }
                    
                    
                    
                }
            }
            
            if( doubleCon==1 )
            {
                String [] predicate = null ;
                if(andFlag == 1 )
                {
                    if(whereCon.contains("AND"))
                        predicate = whereCon.split("AND") ;
                    if(whereCon.contains("and"))
                        predicate = whereCon.split("and") ;
                    if(whereCon.contains("And"))
                        predicate = whereCon.split("And") ;
                    predicate[0] = predicate[0].trim() ;
                    predicate[1] = predicate[1].trim() ;
                for(int i = 0 ;i<columns.size() ;i++)
                {
                    if(columns.get(i).contains("."))
                    {
                        columns.set( i, columns.get(i).substring(columns.get(i).indexOf(".")+1)) ;
                    }
                }
               // System.out.println("Predicate 0 is : "+predicate[0]);
                //System.out.println(predicate[1]);
                int fgte=0,flte=0,flt=0,fgt=0,feq=0;
                int sgte=0,slte=0,slt=0,sgt=0,seq=0;
                String [] fcolAndCondition = null;
                String [] scolAndCondition = null;
                
                if( predicate[0].contains(">=") )
                {
                    fgte = 1;
                    fcolAndCondition = predicate[0].split(">=");
                    //System.out.println("in >=");
                }
                if(predicate[0].contains("<=") )
                {
                    flte = 1;
                    fcolAndCondition = predicate[0].split("<=");
                }
                
                if(predicate[0].contains("=") && flte == 0 && fgte ==0 ){
                    feq = 1;
                    fcolAndCondition = predicate[0].split("=");
                }
                if( predicate[0].contains(">")  && fgte == 0){
                   // System.out.println("in >");
                    fgt = 1;
                    fcolAndCondition = predicate[0].split(">");
                }
                if( predicate[0].contains("<")  && flte == 0){
                    flt = 1;
                    fcolAndCondition = predicate[0].split("<");
                }
                
                String fcolumnName = fcolAndCondition[0].trim();
                fcolumnName = fcolumnName.substring(fcolumnName.indexOf(".") +1);
                int fSecondOperandIsString = 0;
                int sSecondOperandIsString = 0;
                Integer fvalue = null;
                String fSecondOperand = null ;
                try
                {
                    fvalue = Integer.parseInt(fcolAndCondition[1].trim() );
                //System.out.println(fcolumnName+"  "+"  "+fvalue);
                }
                catch(NumberFormatException e)
                {
                    fSecondOperand = fcolAndCondition[1].trim();
                    fSecondOperandIsString =1;
                }
                
                if( predicate[1].contains(">=") ){
                    sgte = 1;
                    scolAndCondition = predicate[1].split(">=");
                    //System.out.println("in >=");
                }
                if(predicate[1].contains("<=") ){
                    slte = 1;
                    scolAndCondition = predicate[1].split("<=");
                }
                
                if(predicate[1].contains("=") && slte == 0 && sgte ==0 ){
                    seq = 1;
                    scolAndCondition = predicate[1].split("=");
                }
                if( predicate[1].contains(">")  && sgte == 0){
                   // System.out.println("in >");
                    sgt = 1;
                    scolAndCondition = predicate[1].split(">");
                }
                if( predicate[1].contains("<")  && slte == 0){
                    slt = 1;
                    scolAndCondition = predicate[1].split("<");
                }
                Integer svalue = null;
                String sSecondOperand = null ;
                String scolumnName = scolAndCondition[0].trim();
                scolumnName = scolumnName.substring(scolumnName.indexOf(".") + 1);
                try
                {
                svalue = Integer.parseInt(scolAndCondition[1].trim() );
                //System.out.println(scolumnName+"  "+"  "+svalue);
                }
                catch(NumberFormatException e)
                {
                    sSecondOperand = scolAndCondition[1].trim();
                    sSecondOperandIsString =1;
                }
                //System.out.println(fcolumnName +" "+ scolumnName + " "+fSecondOperand + " "+ sSecondOperand );
                String tableName  = tables.get(0);
                int rows =0;
                if(columns.get(0).equals("*")){
                    //System.out.println("test1");
                    rows = outerHm.get(tableName).get(desired_col.get(0)).size();
                }
                else
                    rows = outerHm.get(tableName).get(columns.get(0)).size();
                
                int cols = outerHm.get(tableName).keySet().size();
                
                HashMap<String , HashMap<String,ArrayList<Integer>>> tempOuterHm = new HashMap<>();
                HashMap<String, ArrayList<Integer>> tempInnerHm = new HashMap<>();
                for(int i = 0 ;i<cols ; i++)
                {
                    String s = desired_col.get(i);
                    ArrayList<Integer> temp= new ArrayList<>();
                    tempInnerHm.put(s, temp);
                }
                tempOuterHm.put(tableName, tempInnerHm);
                   // System.out.println(fcolumnName +" "+ scolumnName + " "+ tableName);
                if( fSecondOperandIsString == 0 && sSecondOperandIsString == 0 )
                {
                for (int i = 0 ; i<rows ; i++){
                    // for first ***********************************************************************************
                    if(fgte ==1 && sgte == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) >= fvalue && outerHm.get(tableName).get(scolumnName).get(i) >= svalue)
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(fgte ==1 && slte == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) >= fvalue && outerHm.get(tableName).get(scolumnName).get(i) <= svalue)
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(fgte ==1 && sgt == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) >= fvalue && outerHm.get(tableName).get(scolumnName).get(i) > svalue)
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(fgte ==1 && slt == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) >= fvalue && outerHm.get(tableName).get(scolumnName).get(i) < svalue)
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(fgte ==1 && seq == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) >= fvalue && outerHm.get(tableName).get(scolumnName).get(i).equals(svalue))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    
                    // for second
                    if(flte ==1 && sgte == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) <= fvalue && outerHm.get(tableName).get(scolumnName).get(i) >= svalue)
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(flte ==1 && slte == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) <= fvalue && outerHm.get(tableName).get(scolumnName).get(i) <= svalue)
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(flte ==1 && sgt == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) <= fvalue && outerHm.get(tableName).get(scolumnName).get(i) > svalue)
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(flte ==1 && slt == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) <= fvalue && outerHm.get(tableName).get(scolumnName).get(i) < svalue)
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(flte ==1 && seq == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) <= fvalue && outerHm.get(tableName).get(scolumnName).get(i).equals(svalue))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    
                    // for third 
                    
                    if(flt ==1 && sgte == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) < fvalue && outerHm.get(tableName).get(scolumnName).get(i) >= svalue)
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if( flt ==1 && slte == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) < fvalue && outerHm.get(tableName).get(scolumnName).get(i) <= svalue)
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(flt ==1 && sgt == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) < fvalue && outerHm.get(tableName).get(scolumnName).get(i) > svalue)
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(flt ==1 && slt == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) < fvalue && outerHm.get(tableName).get(scolumnName).get(i) < svalue)
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(flt ==1 && seq == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) < fvalue && outerHm.get(tableName).get(scolumnName).get(i).equals(svalue))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    //for forth
                    if(fgt ==1 && sgte == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) > fvalue && outerHm.get(tableName).get(scolumnName).get(i) >= svalue)
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if( fgt ==1 && slte == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) > fvalue && outerHm.get(tableName).get(scolumnName).get(i) <= svalue)
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(fgt ==1 && sgt == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) > fvalue && outerHm.get(tableName).get(scolumnName).get(i) > svalue)
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(fgt ==1 && slt == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) > fvalue && outerHm.get(tableName).get(scolumnName).get(i) < svalue)
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(fgt ==1 && seq == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) > fvalue && outerHm.get(tableName).get(scolumnName).get(i).equals(svalue))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    //for fifth
                    if(feq ==1 && sgte == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i).equals(fvalue) && outerHm.get(tableName).get(scolumnName).get(i) >= svalue)
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if( feq ==1 && slte == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i).equals(fvalue) && outerHm.get(tableName).get(scolumnName).get(i) <= svalue)
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(feq ==1 && sgt == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i).equals(fvalue) && outerHm.get(tableName).get(scolumnName).get(i) > svalue)
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(feq ==1 && slt == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i).equals(fvalue) && outerHm.get(tableName).get(scolumnName).get(i) < svalue)
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(feq ==1 && seq == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i).equals(fvalue) && outerHm.get(tableName).get(scolumnName).get(i).equals(svalue))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    
                }
                if(  "*".equals (columns.get(0) ) )
                {
                    //code to print all columns
                    for(int i = 0 ;i<desired_col.size();i++)
                    {
                        System.out.print(tables.get(0)+"."+desired_col.get(i) );
                        if( i+1 != desired_col.size() )
                            System.out.print(",");
                    }
                    System.out.println("");
                    
                   int resultRows = tempOuterHm.get(tableName).get(desired_col.get(0)).size();
                    //System.out.println(resultRows);
                    for(int m = 0 ; m<resultRows ;m++){
                        for(int n = 0 ; n < desired_col.size() ; n++){
                            System.out.print(tempOuterHm.get(tableName).get(desired_col.get(n)).get(m) );
                            if(n+1 != desired_col.size())
                                System.out.print(",");
                        }
                        if(m+1 != resultRows)
                            System.out.println("");
                    }
                }
                else
                {
                    for(int i = 0 ;i<columns.size();i++)
                    {
                        System.out.print(tables.get(0)+"."+columns.get(i) );
                        if(i+1!=columns.size())
                             System.out.print(",");
                    }
                    System.out.println("");
                    int resultRows = tempOuterHm.get(tableName).get(columns.get(0)).size();
                    for(int m = 0 ; m<resultRows ;m++){
                        for(int n = 0 ; n < columns.size() ; n++){
                            System.out.print(tempOuterHm.get(tableName).get(columns.get(n)).get(m) );
                            if(n+1!=columns.size())
                                System.out.print(",");
                        }
                        if(m+1!=resultRows)
                            System.out.println("");
                    }
                }
                }
                //////////////////////////////////////////////////////////////////////////////////////////////////
                if( fSecondOperandIsString == 1 && sSecondOperandIsString == 0 )
                {
                    //System.out.println( fSecondOperand );
                    fSecondOperand = fSecondOperand.substring(fSecondOperand.indexOf(".")+1);
                   // System.out.println(fSecondOperand);
                for (int i = 0 ; i<rows ; i++){
                    // for first ***********************************************************************************
                    if(fgte ==1 && sgte == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) >= outerHm.get(tableName).get(fSecondOperand).get(i) && outerHm.get(tableName).get(scolumnName).get(i) >= svalue)
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(fgte ==1 && slte == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) >= outerHm.get(tableName).get(fSecondOperand).get(i) && outerHm.get(tableName).get(scolumnName).get(i) <= svalue)
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(fgte ==1 && sgt == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) >= outerHm.get(tableName).get(fSecondOperand).get(i) && outerHm.get(tableName).get(scolumnName).get(i) > svalue)
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(fgte ==1 && slt == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) >= outerHm.get(tableName).get(fSecondOperand).get(i) && outerHm.get(tableName).get(scolumnName).get(i) < svalue)
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(fgte ==1 && seq == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) >= outerHm.get(tableName).get(fSecondOperand).get(i) && outerHm.get(tableName).get(scolumnName).get(i).equals(svalue))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    
                    // for second
                    if(flte ==1 && sgte == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) <= outerHm.get(tableName).get(fSecondOperand).get(i) && outerHm.get(tableName).get(scolumnName).get(i) >= svalue)
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(flte ==1 && slte == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) <= outerHm.get(tableName).get(fSecondOperand).get(i) && outerHm.get(tableName).get(scolumnName).get(i) <= svalue)
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(flte ==1 && sgt == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) <= outerHm.get(tableName).get(fSecondOperand).get(i) && outerHm.get(tableName).get(scolumnName).get(i) > svalue)
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(flte ==1 && slt == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) <= outerHm.get(tableName).get(fSecondOperand).get(i) && outerHm.get(tableName).get(scolumnName).get(i) < svalue)
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(flte ==1 && seq == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) <= outerHm.get(tableName).get(fSecondOperand).get(i) && outerHm.get(tableName).get(scolumnName).get(i).equals(svalue))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    
                    // for third 
                    
                    if(flt ==1 && sgte == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) < outerHm.get(tableName).get(fSecondOperand).get(i) && outerHm.get(tableName).get(scolumnName).get(i) >= svalue)
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if( flt ==1 && slte == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) < outerHm.get(tableName).get(fSecondOperand).get(i) && outerHm.get(tableName).get(scolumnName).get(i) <= svalue)
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(flt ==1 && sgt == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) < outerHm.get(tableName).get(fSecondOperand).get(i) && outerHm.get(tableName).get(scolumnName).get(i) > svalue)
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(flt ==1 && slt == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) < outerHm.get(tableName).get(fSecondOperand).get(i) && outerHm.get(tableName).get(scolumnName).get(i) < svalue)
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(flt ==1 && seq == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) < outerHm.get(tableName).get(fSecondOperand).get(i) && outerHm.get(tableName).get(scolumnName).get(i).equals(svalue))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    //for forth
                    if(fgt ==1 && sgte == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) > outerHm.get(tableName).get(fSecondOperand).get(i) && outerHm.get(tableName).get(scolumnName).get(i) >= svalue)
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if( fgt ==1 && slte == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) > outerHm.get(tableName).get(fSecondOperand).get(i) && outerHm.get(tableName).get(scolumnName).get(i) <= svalue)
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(fgt ==1 && sgt == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) > outerHm.get(tableName).get(fSecondOperand).get(i) && outerHm.get(tableName).get(scolumnName).get(i) > svalue)
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(fgt ==1 && slt == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) > outerHm.get(tableName).get(fSecondOperand).get(i) && outerHm.get(tableName).get(scolumnName).get(i) < svalue)
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(fgt ==1 && seq == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) > outerHm.get(tableName).get(fSecondOperand).get(i) && outerHm.get(tableName).get(scolumnName).get(i).equals(svalue))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    //for fifth
                    if(feq ==1 && sgte == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i).equals(outerHm.get(tableName).get(fSecondOperand).get(i) ) && outerHm.get(tableName).get(scolumnName).get(i) >= svalue)
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if( feq ==1 && slte == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i).equals(outerHm.get(tableName).get(fSecondOperand).get(i)) && outerHm.get(tableName).get(scolumnName).get(i) <= svalue)
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(feq ==1 && sgt == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i).equals(outerHm.get(tableName).get(fSecondOperand).get(i)) && outerHm.get(tableName).get(scolumnName).get(i) > svalue)
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(feq ==1 && slt == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i).equals(outerHm.get(tableName).get(fSecondOperand).get(i)) && outerHm.get(tableName).get(scolumnName).get(i) < svalue)
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(feq ==1 && seq == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i).equals(outerHm.get(tableName).get(fSecondOperand).get(i)) && outerHm.get(tableName).get(scolumnName).get(i).equals(svalue))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    
                }
                if(  "*".equals (columns.get(0) ) )
                {
                    //code to print all columns
                    for(int i = 0 ;i<desired_col.size();i++)
                    {
                        System.out.print(tables.get(0)+"."+desired_col.get(i) );
                        if( i+1 != desired_col.size() )
                            System.out.print(",");
                    }
                    System.out.println("");
                    
                   int resultRows = tempOuterHm.get(tableName).get(desired_col.get(0)).size();
                    //System.out.println(resultRows);
                    for(int m = 0 ; m<resultRows ;m++){
                        for(int n = 0 ; n < desired_col.size() ; n++){
                            System.out.print(tempOuterHm.get(tableName).get(desired_col.get(n)).get(m) );
                            if(n+1 != desired_col.size())
                                System.out.print(",");
                        }
                        if(m+1 != resultRows)
                            System.out.println("");
                    }
                }
                else
                {
                    for(int i = 0 ;i<columns.size();i++)
                    {
                        System.out.print(tables.get(0)+"."+columns.get(i) );
                        if(i+1!=columns.size())
                             System.out.print(",");
                    }
                    System.out.println("");
                    int resultRows = tempOuterHm.get(tableName).get(columns.get(0)).size();
                    for(int m = 0 ; m<resultRows ;m++){
                        for(int n = 0 ; n < columns.size() ; n++){
                            System.out.print(tempOuterHm.get(tableName).get(columns.get(n)).get(m) );
                            if(n+1!=columns.size())
                                System.out.print(",");
                        }
                        if(m+1!=resultRows)
                            System.out.println("");
                    }
                }
                }
                ////////////////////////////////////////////////////////////////////////////////////////////////
                if( fSecondOperandIsString == 0 && sSecondOperandIsString == 1 )
                {
                    //System.out.println( sSecondOperand );
                   sSecondOperand = sSecondOperand.substring(sSecondOperand.indexOf(".")+1);
                for (int i = 0 ; i<rows ; i++){
                    // for first ***********************************************************************************
                    if(fgte ==1 && sgte == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) >= fvalue && outerHm.get(tableName).get(scolumnName).get(i) >= outerHm.get(tableName).get(sSecondOperand).get(i))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(fgte ==1 && slte == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) >= fvalue && outerHm.get(tableName).get(scolumnName).get(i) <= outerHm.get(tableName).get(sSecondOperand).get(i))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(fgte ==1 && sgt == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) >= fvalue && outerHm.get(tableName).get(scolumnName).get(i) > outerHm.get(tableName).get(sSecondOperand).get(i))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(fgte ==1 && slt == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) >= fvalue && outerHm.get(tableName).get(scolumnName).get(i) < outerHm.get(tableName).get(sSecondOperand).get(i))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(fgte ==1 && seq == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) >= fvalue && outerHm.get(tableName).get(scolumnName).get(i).equals(outerHm.get(tableName).get(sSecondOperand).get(i)))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    
                    // for second
                    if(flte ==1 && sgte == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) <= fvalue && outerHm.get(tableName).get(scolumnName).get(i) >= outerHm.get(tableName).get(sSecondOperand).get(i))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(flte ==1 && slte == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) <= fvalue && outerHm.get(tableName).get(scolumnName).get(i) <= outerHm.get(tableName).get(sSecondOperand).get(i))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(flte ==1 && sgt == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) <= fvalue && outerHm.get(tableName).get(scolumnName).get(i) > outerHm.get(tableName).get(sSecondOperand).get(i))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(flte ==1 && slt == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) <= fvalue && outerHm.get(tableName).get(scolumnName).get(i) < outerHm.get(tableName).get(sSecondOperand).get(i))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(flte ==1 && seq == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) <= fvalue && outerHm.get(tableName).get(scolumnName).get(i).equals(outerHm.get(tableName).get(sSecondOperand).get(i)))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    
                    // for third 
                    
                    if(flt ==1 && sgte == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) < fvalue && outerHm.get(tableName).get(scolumnName).get(i) >= outerHm.get(tableName).get(sSecondOperand).get(i))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if( flt ==1 && slte == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) < fvalue && outerHm.get(tableName).get(scolumnName).get(i) <= outerHm.get(tableName).get(sSecondOperand).get(i))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(flt ==1 && sgt == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) < fvalue && outerHm.get(tableName).get(scolumnName).get(i) > outerHm.get(tableName).get(sSecondOperand).get(i))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(flt ==1 && slt == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) < fvalue && outerHm.get(tableName).get(scolumnName).get(i) < outerHm.get(tableName).get(sSecondOperand).get(i))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(flt ==1 && seq == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) < fvalue && outerHm.get(tableName).get(scolumnName).get(i).equals(outerHm.get(tableName).get(sSecondOperand).get(i)))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    //for forth
                    if(fgt ==1 && sgte == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) > fvalue && outerHm.get(tableName).get(scolumnName).get(i) >= outerHm.get(tableName).get(sSecondOperand).get(i))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if( fgt ==1 && slte == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) > fvalue && outerHm.get(tableName).get(scolumnName).get(i) <= outerHm.get(tableName).get(sSecondOperand).get(i))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(fgt ==1 && sgt == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) > fvalue && outerHm.get(tableName).get(scolumnName).get(i) > outerHm.get(tableName).get(sSecondOperand).get(i))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(fgt ==1 && slt == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) > fvalue && outerHm.get(tableName).get(scolumnName).get(i) < outerHm.get(tableName).get(sSecondOperand).get(i))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(fgt ==1 && seq == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) > fvalue && outerHm.get(tableName).get(scolumnName).get(i).equals(outerHm.get(tableName).get(sSecondOperand).get(i)))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    //for fifth
                    if(feq ==1 && sgte == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i).equals(fvalue) && outerHm.get(tableName).get(scolumnName).get(i) >= outerHm.get(tableName).get(sSecondOperand).get(i))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if( feq ==1 && slte == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i).equals(fvalue) && outerHm.get(tableName).get(scolumnName).get(i) <= outerHm.get(tableName).get(sSecondOperand).get(i))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(feq ==1 && sgt == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i).equals(fvalue) && outerHm.get(tableName).get(scolumnName).get(i) > outerHm.get(tableName).get(sSecondOperand).get(i))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(feq ==1 && slt == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i).equals(fvalue) && outerHm.get(tableName).get(scolumnName).get(i) < outerHm.get(tableName).get(sSecondOperand).get(i))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(feq ==1 && seq == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i).equals(fvalue) && outerHm.get(tableName).get(scolumnName).get(i).equals(outerHm.get(tableName).get(sSecondOperand).get(i)))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    
                }
                if(  "*".equals (columns.get(0) ) )
                {
                    //code to print all columns
                    for(int i = 0 ;i<desired_col.size();i++)
                    {
                        System.out.print(tables.get(0)+"."+desired_col.get(i) );
                        if( i+1 != desired_col.size() )
                            System.out.print(",");
                    }
                    System.out.println("");
                    
                   int resultRows = tempOuterHm.get(tableName).get(desired_col.get(0)).size();
                    //System.out.println(resultRows);
                    for(int m = 0 ; m<resultRows ;m++){
                        for(int n = 0 ; n < desired_col.size() ; n++){
                            System.out.print(tempOuterHm.get(tableName).get(desired_col.get(n)).get(m) );
                            if(n+1 != desired_col.size())
                                System.out.print(",");
                        }
                        if(m+1 != resultRows)
                            System.out.println("");
                    }
                }
                else
                {
                    for(int i = 0 ;i<columns.size();i++)
                    {
                        System.out.print(tables.get(0)+"."+columns.get(i) );
                        if(i+1!=columns.size())
                             System.out.print(",");
                    }
                    System.out.println("");
                    int resultRows = tempOuterHm.get(tableName).get(columns.get(0)).size();
                    for(int m = 0 ; m<resultRows ;m++){
                        for(int n = 0 ; n < columns.size() ; n++){
                            System.out.print(tempOuterHm.get(tableName).get(columns.get(n)).get(m) );
                            if(n+1!=columns.size())
                                System.out.print(",");
                        }
                        if(m+1!=resultRows)
                            System.out.println("");
                    }
                }
                }
                //////////////////////////////////////////////////////////////////////////////////////////////
                if( fSecondOperandIsString == 1 && sSecondOperandIsString == 1 )
                {
                    //System.out.println( sSecondOperand );
                   sSecondOperand = sSecondOperand.substring(sSecondOperand.indexOf(".")+1);
                   fSecondOperand = fSecondOperand.substring(fSecondOperand.indexOf(".")+1);
                for (int i = 0 ; i<rows ; i++){
                    // for first ***********************************************************************************
                    if(fgte ==1 && sgte == 1){
                        if( outerHm.get(tableName).get( fcolumnName).get(i) >= outerHm.get(tableName).get(fSecondOperand).get(i) && outerHm.get(tableName).get(scolumnName).get(i) >= outerHm.get(tableName).get(sSecondOperand).get(i))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(fgte ==1 && slte == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) >= outerHm.get(tableName).get(fSecondOperand).get(i) && outerHm.get(tableName).get(scolumnName).get(i) <= outerHm.get(tableName).get(sSecondOperand).get(i))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(fgte ==1 && sgt == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) >= outerHm.get(tableName).get(fSecondOperand).get(i) && outerHm.get(tableName).get(scolumnName).get(i) > outerHm.get(tableName).get(sSecondOperand).get(i))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(fgte ==1 && slt == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) >= outerHm.get(tableName).get(fSecondOperand).get(i) && outerHm.get(tableName).get(scolumnName).get(i) < outerHm.get(tableName).get(sSecondOperand).get(i))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(fgte ==1 && seq == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) >= outerHm.get(tableName).get(fSecondOperand).get(i) && outerHm.get(tableName).get(scolumnName).get(i).equals(outerHm.get(tableName).get(sSecondOperand).get(i)))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    
                    // for second
                    if(flte ==1 && sgte == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) <= outerHm.get(tableName).get(fSecondOperand).get(i) && outerHm.get(tableName).get(scolumnName).get(i) >= outerHm.get(tableName).get(sSecondOperand).get(i))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(flte ==1 && slte == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) <= outerHm.get(tableName).get(fSecondOperand).get(i) && outerHm.get(tableName).get(scolumnName).get(i) <= outerHm.get(tableName).get(sSecondOperand).get(i))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(flte ==1 && sgt == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) <= outerHm.get(tableName).get(fSecondOperand).get(i) && outerHm.get(tableName).get(scolumnName).get(i) > outerHm.get(tableName).get(sSecondOperand).get(i))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(flte ==1 && slt == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) <= outerHm.get(tableName).get(fSecondOperand).get(i) && outerHm.get(tableName).get(scolumnName).get(i) < outerHm.get(tableName).get(sSecondOperand).get(i))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(flte ==1 && seq == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) <= outerHm.get(tableName).get(fSecondOperand).get(i) && outerHm.get(tableName).get(scolumnName).get(i).equals(outerHm.get(tableName).get(sSecondOperand).get(i)))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    
                    // for third 
                    
                    if(flt ==1 && sgte == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) < outerHm.get(tableName).get(fSecondOperand).get(i) && outerHm.get(tableName).get(scolumnName).get(i) >= outerHm.get(tableName).get(sSecondOperand).get(i))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if( flt ==1 && slte == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) < outerHm.get(tableName).get(fSecondOperand).get(i) && outerHm.get(tableName).get(scolumnName).get(i) <= outerHm.get(tableName).get(sSecondOperand).get(i))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(flt ==1 && sgt == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) < outerHm.get(tableName).get(fSecondOperand).get(i) && outerHm.get(tableName).get(scolumnName).get(i) > outerHm.get(tableName).get(sSecondOperand).get(i))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(flt ==1 && slt == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) < outerHm.get(tableName).get(fSecondOperand).get(i) && outerHm.get(tableName).get(scolumnName).get(i) < outerHm.get(tableName).get(sSecondOperand).get(i))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(flt ==1 && seq == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) < outerHm.get(tableName).get(fSecondOperand).get(i) && outerHm.get(tableName).get(scolumnName).get(i).equals(outerHm.get(tableName).get(sSecondOperand).get(i)))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    //for forth
                    if(fgt ==1 && sgte == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) > outerHm.get(tableName).get(fSecondOperand).get(i) && outerHm.get(tableName).get(scolumnName).get(i) >= outerHm.get(tableName).get(sSecondOperand).get(i))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if( fgt ==1 && slte == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) > outerHm.get(tableName).get(fSecondOperand).get(i) && outerHm.get(tableName).get(scolumnName).get(i) <= outerHm.get(tableName).get(sSecondOperand).get(i))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(fgt ==1 && sgt == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) > outerHm.get(tableName).get(fSecondOperand).get(i) && outerHm.get(tableName).get(scolumnName).get(i) > outerHm.get(tableName).get(sSecondOperand).get(i))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(fgt ==1 && slt == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) > outerHm.get(tableName).get(fSecondOperand).get(i) && outerHm.get(tableName).get(scolumnName).get(i) < outerHm.get(tableName).get(sSecondOperand).get(i))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(fgt ==1 && seq == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) > outerHm.get(tableName).get(fSecondOperand).get(i) && outerHm.get(tableName).get(scolumnName).get(i).equals(outerHm.get(tableName).get(sSecondOperand).get(i)))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    //for fifth
                    if(feq ==1 && sgte == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i).equals(outerHm.get(tableName).get(fSecondOperand).get(i)) && outerHm.get(tableName).get(scolumnName).get(i) >= outerHm.get(tableName).get(sSecondOperand).get(i))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if( feq ==1 && slte == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i).equals(outerHm.get(tableName).get(fSecondOperand).get(i)) && outerHm.get(tableName).get(scolumnName).get(i) <= outerHm.get(tableName).get(sSecondOperand).get(i))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(feq ==1 && sgt == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i).equals(outerHm.get(tableName).get(fSecondOperand).get(i)) && outerHm.get(tableName).get(scolumnName).get(i) > outerHm.get(tableName).get(sSecondOperand).get(i))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(feq ==1 && slt == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i).equals(outerHm.get(tableName).get(fSecondOperand).get(i)) && outerHm.get(tableName).get(scolumnName).get(i) < outerHm.get(tableName).get(sSecondOperand).get(i))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(feq ==1 && seq == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i).equals(outerHm.get(tableName).get(fSecondOperand).get(i)) && outerHm.get(tableName).get(scolumnName).get(i).equals(outerHm.get(tableName).get(sSecondOperand).get(i)))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    
                }
                if(  "*".equals (columns.get(0) ) )
                {
                    //code to print all columns
                    for(int i = 0 ;i<desired_col.size();i++)
                    {
                        System.out.print(tables.get(0)+"."+desired_col.get(i) );
                        if( i+1 != desired_col.size() )
                            System.out.print(",");
                    }
                    System.out.println("");
                    
                   int resultRows = tempOuterHm.get(tableName).get(desired_col.get(0)).size();
                    //System.out.println(resultRows);
                    for(int m = 0 ; m<resultRows ;m++){
                        for(int n = 0 ; n < desired_col.size() ; n++){
                            System.out.print(tempOuterHm.get(tableName).get(desired_col.get(n)).get(m) );
                            if(n+1 != desired_col.size())
                                System.out.print(",");
                        }
                        if(m+1 != resultRows)
                            System.out.println("");
                    }
                }
                else
                {
                    for(int i = 0 ;i<columns.size();i++)
                    {
                        System.out.print(tables.get(0)+"."+columns.get(i) );
                        if(i+1!=columns.size())
                             System.out.print(",");
                    }
                    System.out.println("");
                    int resultRows = tempOuterHm.get(tableName).get(columns.get(0)).size();
                    for(int m = 0 ; m<resultRows ;m++){
                        for(int n = 0 ; n < columns.size() ; n++){
                            System.out.print(tempOuterHm.get(tableName).get(columns.get(n)).get(m) );
                            if(n+1!=columns.size())
                                System.out.print(",");
                        }
                        if(m+1!=resultRows)
                            System.out.println("");
                    }
                }
                }
            }
            if(orFlag == 1 )
                {
                    if(whereCon.contains("OR"))
                        predicate = whereCon.split("OR") ;
                    if(whereCon.contains("or"))
                        predicate = whereCon.split("or") ;
                    if(whereCon.contains("Or"))
                        predicate = whereCon.split("Or") ;
                    predicate[0] = predicate[0].trim() ;
                    predicate[1] = predicate[1].trim() ;
                for(int i = 0 ;i<columns.size() ;i++)
                {
                    if(columns.get(i).contains("."))
                    {
                        columns.set( i, columns.get(i).substring(columns.get(i).indexOf(".")+1)) ;
                    }
                }
               // System.out.println("Predicate 0 is : "+predicate[0]);
                //System.out.println(predicate[1]);
                int fgte=0,flte=0,flt=0,fgt=0,feq=0;
                int sgte=0,slte=0,slt=0,sgt=0,seq=0;
                String [] fcolAndCondition = null;
                String [] scolAndCondition = null;
                
                if( predicate[0].contains(">=") )
                {
                    fgte = 1;
                    fcolAndCondition = predicate[0].split(">=");
                    //System.out.println("in >=");
                }
                if(predicate[0].contains("<=") )
                {
                    flte = 1;
                    fcolAndCondition = predicate[0].split("<=");
                }
                
                if(predicate[0].contains("=") && flte == 0 && fgte ==0 ){
                    feq = 1;
                    fcolAndCondition = predicate[0].split("=");
                }
                if( predicate[0].contains(">")  && fgte == 0){
                   // System.out.println("in >");
                    fgt = 1;
                    fcolAndCondition = predicate[0].split(">");
                }
                if( predicate[0].contains("<")  && flte == 0){
                    flt = 1;
                    fcolAndCondition = predicate[0].split("<");
                }
                
                String fcolumnName = fcolAndCondition[0].trim();
                fcolumnName = fcolumnName.substring(fcolumnName.indexOf(".") +1);
                int fSecondOperandIsString = 0;
                int sSecondOperandIsString = 0;
                Integer fvalue = null;
                String fSecondOperand = null ;
                try
                {
                    fvalue = Integer.parseInt(fcolAndCondition[1].trim() );
                //System.out.println(fcolumnName+"  "+"  "+fvalue);
                }
                catch(NumberFormatException e)
                {
                    fSecondOperand = fcolAndCondition[1].trim();
                    fSecondOperandIsString =1;
                }
                
                if( predicate[1].contains(">=") ){
                    sgte = 1;
                    scolAndCondition = predicate[1].split(">=");
                    //System.out.println("in >=");
                }
                if(predicate[1].contains("<=") ){
                    slte = 1;
                    scolAndCondition = predicate[1].split("<=");
                }
                
                if(predicate[1].contains("=") && slte == 0 && sgte ==0 ){
                    seq = 1;
                    scolAndCondition = predicate[1].split("=");
                }
                if( predicate[1].contains(">")  && sgte == 0){
                   // System.out.println("in >");
                    sgt = 1;
                    scolAndCondition = predicate[1].split(">");
                }
                if( predicate[1].contains("<")  && slte == 0){
                    slt = 1;
                    scolAndCondition = predicate[1].split("<");
                }
                Integer svalue = null;
                String sSecondOperand = null ;
                String scolumnName = scolAndCondition[0].trim();
                scolumnName = scolumnName.substring(scolumnName.indexOf(".") + 1);
                try
                {
                svalue = Integer.parseInt(scolAndCondition[1].trim() );
                //System.out.println(scolumnName+"  "+"  "+svalue);
                }
                catch(NumberFormatException e)
                {
                    sSecondOperand = scolAndCondition[1].trim();
                    sSecondOperandIsString =1;
                }
                //System.out.println(fcolumnName +" "+ scolumnName + " "+fSecondOperand + " "+ sSecondOperand );
                String tableName  = tables.get(0);
                int rows =0;
                if(columns.get(0).equals("*")){
                    //System.out.println("test1");
                    rows = outerHm.get(tableName).get(desired_col.get(0)).size();
                }
                else
                    rows = outerHm.get(tableName).get(columns.get(0)).size();
                
                int cols = outerHm.get(tableName).keySet().size();
                
                HashMap<String , HashMap<String,ArrayList<Integer>>> tempOuterHm = new HashMap<>();
                HashMap<String, ArrayList<Integer>> tempInnerHm = new HashMap<>();
                for(int i = 0 ;i<cols ; i++)
                {
                    String s = desired_col.get(i);
                    ArrayList<Integer> temp= new ArrayList<>();
                    tempInnerHm.put(s, temp);
                }
                tempOuterHm.put(tableName, tempInnerHm);
                   // System.out.println(fcolumnName +" "+ scolumnName + " "+ tableName);
                if( fSecondOperandIsString == 0 && sSecondOperandIsString == 0 )
                {
                for (int i = 0 ; i<rows ; i++){
                    // for first ***********************************************************************************
                    if(fgte ==1 && sgte == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) >= fvalue || outerHm.get(tableName).get(scolumnName).get(i) >= svalue)
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(fgte ==1 && slte == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) >= fvalue || outerHm.get(tableName).get(scolumnName).get(i) <= svalue)
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(fgte ==1 && sgt == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) >= fvalue || outerHm.get(tableName).get(scolumnName).get(i) > svalue)
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(fgte ==1 && slt == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) >= fvalue || outerHm.get(tableName).get(scolumnName).get(i) < svalue)
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(fgte ==1 && seq == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) >= fvalue || outerHm.get(tableName).get(scolumnName).get(i).equals(svalue))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    
                    // for second
                    if(flte ==1 && sgte == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) <= fvalue || outerHm.get(tableName).get(scolumnName).get(i) >= svalue)
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(flte ==1 && slte == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) <= fvalue || outerHm.get(tableName).get(scolumnName).get(i) <= svalue)
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(flte ==1 && sgt == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) <= fvalue || outerHm.get(tableName).get(scolumnName).get(i) > svalue)
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(flte ==1 && slt == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) <= fvalue || outerHm.get(tableName).get(scolumnName).get(i) < svalue)
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(flte ==1 && seq == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) <= fvalue || outerHm.get(tableName).get(scolumnName).get(i).equals(svalue))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    
                    // for third 
                    
                    if(flt ==1 && sgte == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) < fvalue || outerHm.get(tableName).get(scolumnName).get(i) >= svalue)
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if( flt ==1 && slte == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) < fvalue || outerHm.get(tableName).get(scolumnName).get(i) <= svalue)
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(flt ==1 && sgt == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) < fvalue || outerHm.get(tableName).get(scolumnName).get(i) > svalue)
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(flt ==1 && slt == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) < fvalue || outerHm.get(tableName).get(scolumnName).get(i) < svalue)
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(flt ==1 && seq == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) < fvalue || outerHm.get(tableName).get(scolumnName).get(i).equals(svalue))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    //for forth
                    if(fgt ==1 && sgte == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) > fvalue || outerHm.get(tableName).get(scolumnName).get(i) >= svalue)
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if( fgt ==1 && slte == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) > fvalue || outerHm.get(tableName).get(scolumnName).get(i) <= svalue)
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(fgt ==1 && sgt == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) > fvalue || outerHm.get(tableName).get(scolumnName).get(i) > svalue)
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(fgt ==1 && slt == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) > fvalue || outerHm.get(tableName).get(scolumnName).get(i) < svalue)
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(fgt ==1 && seq == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) > fvalue || outerHm.get(tableName).get(scolumnName).get(i).equals(svalue))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    //for fifth
                    if(feq ==1 && sgte == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i).equals(fvalue) || outerHm.get(tableName).get(scolumnName).get(i) >= svalue)
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if( feq ==1 && slte == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i).equals(fvalue) || outerHm.get(tableName).get(scolumnName).get(i) <= svalue)
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(feq ==1 && sgt == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i).equals(fvalue) || outerHm.get(tableName).get(scolumnName).get(i) > svalue)
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(feq ==1 && slt == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i).equals(fvalue) || outerHm.get(tableName).get(scolumnName).get(i) < svalue)
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(feq ==1 && seq == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i).equals(fvalue) || outerHm.get(tableName).get(scolumnName).get(i).equals(svalue))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    
                }
                if(  "*".equals (columns.get(0) ) )
                {
                    //code to print all columns
                    for(int i = 0 ;i<desired_col.size();i++)
                    {
                        System.out.print(tables.get(0)+"."+desired_col.get(i) );
                        if( i+1 != desired_col.size() )
                            System.out.print(",");
                    }
                    System.out.println("");
                    
                   int resultRows = tempOuterHm.get(tableName).get(desired_col.get(0)).size();
                    //System.out.println(resultRows);
                    for(int m = 0 ; m<resultRows ;m++){
                        for(int n = 0 ; n < desired_col.size() ; n++){
                            System.out.print(tempOuterHm.get(tableName).get(desired_col.get(n)).get(m) );
                            if(n+1 != desired_col.size())
                                System.out.print(",");
                        }
                        if(m+1 != resultRows)
                            System.out.println("");
                    }
                }
                else
                {
                    for(int i = 0 ;i<columns.size();i++)
                    {
                        System.out.print(tables.get(0)+"."+columns.get(i) );
                        if(i+1!=columns.size())
                             System.out.print(",");
                    }
                    System.out.println("");
                    int resultRows = tempOuterHm.get(tableName).get(columns.get(0)).size();
                    for(int m = 0 ; m<resultRows ;m++){
                        for(int n = 0 ; n < columns.size() ; n++){
                            System.out.print(tempOuterHm.get(tableName).get(columns.get(n)).get(m) );
                            if(n+1!=columns.size())
                                System.out.print(",");
                        }
                        if(m+1!=resultRows)
                            System.out.println("");
                    }
                }
                }
                //////////////////////////////////////////////////////////////////////////////////////////////////
                if( fSecondOperandIsString == 1 && sSecondOperandIsString == 0 )
                {
                    //System.out.println( fSecondOperand );
                    fSecondOperand = fSecondOperand.substring(fSecondOperand.indexOf(".")+1);
                   // System.out.println(fSecondOperand);
                for (int i = 0 ; i<rows ; i++){
                    // for first ***********************************************************************************
                    if(fgte ==1 && sgte == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) >= outerHm.get(tableName).get(fSecondOperand).get(i) || outerHm.get(tableName).get(scolumnName).get(i) >= svalue)
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(fgte ==1 && slte == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) >= outerHm.get(tableName).get(fSecondOperand).get(i) || outerHm.get(tableName).get(scolumnName).get(i) <= svalue)
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(fgte ==1 && sgt == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) >= outerHm.get(tableName).get(fSecondOperand).get(i) || outerHm.get(tableName).get(scolumnName).get(i) > svalue)
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(fgte ==1 && slt == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) >= outerHm.get(tableName).get(fSecondOperand).get(i) || outerHm.get(tableName).get(scolumnName).get(i) < svalue)
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(fgte ==1 && seq == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) >= outerHm.get(tableName).get(fSecondOperand).get(i) || outerHm.get(tableName).get(scolumnName).get(i).equals(svalue))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    
                    // for second
                    if(flte ==1 && sgte == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) <= outerHm.get(tableName).get(fSecondOperand).get(i) || outerHm.get(tableName).get(scolumnName).get(i) >= svalue)
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(flte ==1 && slte == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) <= outerHm.get(tableName).get(fSecondOperand).get(i) || outerHm.get(tableName).get(scolumnName).get(i) <= svalue)
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(flte ==1 && sgt == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) <= outerHm.get(tableName).get(fSecondOperand).get(i) || outerHm.get(tableName).get(scolumnName).get(i) > svalue)
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(flte ==1 && slt == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) <= outerHm.get(tableName).get(fSecondOperand).get(i) || outerHm.get(tableName).get(scolumnName).get(i) < svalue)
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(flte ==1 && seq == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) <= outerHm.get(tableName).get(fSecondOperand).get(i) || outerHm.get(tableName).get(scolumnName).get(i).equals(svalue))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    
                    // for third 
                    
                    if(flt ==1 && sgte == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) < outerHm.get(tableName).get(fSecondOperand).get(i) || outerHm.get(tableName).get(scolumnName).get(i) >= svalue)
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if( flt ==1 && slte == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) < outerHm.get(tableName).get(fSecondOperand).get(i) || outerHm.get(tableName).get(scolumnName).get(i) <= svalue)
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(flt ==1 && sgt == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) < outerHm.get(tableName).get(fSecondOperand).get(i) || outerHm.get(tableName).get(scolumnName).get(i) > svalue)
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(flt ==1 && slt == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) < outerHm.get(tableName).get(fSecondOperand).get(i) || outerHm.get(tableName).get(scolumnName).get(i) < svalue)
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(flt ==1 && seq == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) < outerHm.get(tableName).get(fSecondOperand).get(i) || outerHm.get(tableName).get(scolumnName).get(i).equals(svalue))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    //for forth
                    if(fgt ==1 && sgte == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) > outerHm.get(tableName).get(fSecondOperand).get(i) || outerHm.get(tableName).get(scolumnName).get(i) >= svalue)
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if( fgt ==1 && slte == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) > outerHm.get(tableName).get(fSecondOperand).get(i) || outerHm.get(tableName).get(scolumnName).get(i) <= svalue)
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(fgt ==1 && sgt == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) > outerHm.get(tableName).get(fSecondOperand).get(i) || outerHm.get(tableName).get(scolumnName).get(i) > svalue)
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(fgt ==1 && slt == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) > outerHm.get(tableName).get(fSecondOperand).get(i) || outerHm.get(tableName).get(scolumnName).get(i) < svalue)
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(fgt ==1 && seq == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) > outerHm.get(tableName).get(fSecondOperand).get(i) || outerHm.get(tableName).get(scolumnName).get(i).equals(svalue))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    //for fifth
                    if(feq ==1 && sgte == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i).equals(outerHm.get(tableName).get(fSecondOperand).get(i)) || outerHm.get(tableName).get(scolumnName).get(i) >= svalue)
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if( feq ==1 && slte == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i).equals(outerHm.get(tableName).get(fSecondOperand).get(i)) || outerHm.get(tableName).get(scolumnName).get(i) <= svalue)
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(feq ==1 && sgt == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i).equals(outerHm.get(tableName).get(fSecondOperand).get(i)) || outerHm.get(tableName).get(scolumnName).get(i) > svalue)
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(feq ==1 && slt == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i).equals(outerHm.get(tableName).get(fSecondOperand).get(i)) || outerHm.get(tableName).get(scolumnName).get(i) < svalue)
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(feq ==1 && seq == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i).equals(outerHm.get(tableName).get(fSecondOperand).get(i)) || outerHm.get(tableName).get(scolumnName).get(i).equals(svalue))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    
                }
                if(  "*".equals (columns.get(0) ) )
                {
                    //code to print all columns
                    for(int i = 0 ;i<desired_col.size();i++)
                    {
                        System.out.print(tables.get(0)+"."+desired_col.get(i) );
                        if( i+1 != desired_col.size() )
                            System.out.print(",");
                    }
                    System.out.println("");
                    
                   int resultRows = tempOuterHm.get(tableName).get(desired_col.get(0)).size();
                    //System.out.println(resultRows);
                    for(int m = 0 ; m<resultRows ;m++){
                        for(int n = 0 ; n < desired_col.size() ; n++){
                            System.out.print(tempOuterHm.get(tableName).get(desired_col.get(n)).get(m) );
                            if(n+1 != desired_col.size())
                                System.out.print(",");
                        }
                        if(m+1 != resultRows)
                            System.out.println("");
                    }
                }
                else
                {
                    for(int i = 0 ;i<columns.size();i++)
                    {
                        System.out.print(tables.get(0)+"."+columns.get(i) );
                        if(i+1!=columns.size())
                             System.out.print(",");
                    }
                    System.out.println("");
                    int resultRows = tempOuterHm.get(tableName).get(columns.get(0)).size();
                    for(int m = 0 ; m<resultRows ;m++){
                        for(int n = 0 ; n < columns.size() ; n++){
                            System.out.print(tempOuterHm.get(tableName).get(columns.get(n)).get(m) );
                            if(n+1!=columns.size())
                                System.out.print(",");
                        }
                        if(m+1!=resultRows)
                            System.out.println("");
                    }
                }
                }
                ////////////////////////////////////////////////////////////////////////////////////////////////
                if( fSecondOperandIsString == 0 && sSecondOperandIsString == 1 )
                {
                    //System.out.println( sSecondOperand );
                   sSecondOperand = sSecondOperand.substring(sSecondOperand.indexOf(".")+1);
                for (int i = 0 ; i<rows ; i++){
                    // for first ***********************************************************************************
                    if(fgte ==1 && sgte == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) >= fvalue || outerHm.get(tableName).get(scolumnName).get(i) >= outerHm.get(tableName).get(sSecondOperand).get(i))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(fgte ==1 && slte == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) >= fvalue || outerHm.get(tableName).get(scolumnName).get(i) <= outerHm.get(tableName).get(sSecondOperand).get(i))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(fgte ==1 && sgt == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) >= fvalue || outerHm.get(tableName).get(scolumnName).get(i) > outerHm.get(tableName).get(sSecondOperand).get(i))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(fgte ==1 && slt == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) >= fvalue || outerHm.get(tableName).get(scolumnName).get(i) < outerHm.get(tableName).get(sSecondOperand).get(i))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(fgte ==1 && seq == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) >= fvalue || outerHm.get(tableName).get(scolumnName).get(i).equals(outerHm.get(tableName).get(sSecondOperand).get(i)))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    
                    // for second
                    if(flte ==1 && sgte == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) <= fvalue || outerHm.get(tableName).get(scolumnName).get(i) >= outerHm.get(tableName).get(sSecondOperand).get(i))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(flte ==1 && slte == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) <= fvalue || outerHm.get(tableName).get(scolumnName).get(i) <= outerHm.get(tableName).get(sSecondOperand).get(i))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(flte ==1 && sgt == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) <= fvalue || outerHm.get(tableName).get(scolumnName).get(i) > outerHm.get(tableName).get(sSecondOperand).get(i))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(flte ==1 && slt == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) <= fvalue || outerHm.get(tableName).get(scolumnName).get(i) < outerHm.get(tableName).get(sSecondOperand).get(i))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(flte ==1 && seq == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) <= fvalue || outerHm.get(tableName).get(scolumnName).get(i).equals(outerHm.get(tableName).get(sSecondOperand).get(i)))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    
                    // for third 
                    
                    if(flt ==1 && sgte == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) < fvalue || outerHm.get(tableName).get(scolumnName).get(i) >= outerHm.get(tableName).get(sSecondOperand).get(i))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if( flt ==1 && slte == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) < fvalue || outerHm.get(tableName).get(scolumnName).get(i) <= outerHm.get(tableName).get(sSecondOperand).get(i))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(flt ==1 && sgt == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) < fvalue || outerHm.get(tableName).get(scolumnName).get(i) > outerHm.get(tableName).get(sSecondOperand).get(i))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(flt ==1 && slt == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) < fvalue || outerHm.get(tableName).get(scolumnName).get(i) < outerHm.get(tableName).get(sSecondOperand).get(i))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(flt ==1 && seq == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) < fvalue || outerHm.get(tableName).get(scolumnName).get(i).equals(outerHm.get(tableName).get(sSecondOperand).get(i)))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    //for forth
                    if(fgt ==1 && sgte == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) > fvalue || outerHm.get(tableName).get(scolumnName).get(i) >= outerHm.get(tableName).get(sSecondOperand).get(i))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if( fgt ==1 && slte == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) > fvalue || outerHm.get(tableName).get(scolumnName).get(i) <= outerHm.get(tableName).get(sSecondOperand).get(i))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(fgt ==1 && sgt == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) > fvalue || outerHm.get(tableName).get(scolumnName).get(i) > outerHm.get(tableName).get(sSecondOperand).get(i))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(fgt ==1 && slt == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) > fvalue || outerHm.get(tableName).get(scolumnName).get(i) < outerHm.get(tableName).get(sSecondOperand).get(i))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(fgt ==1 && seq == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) > fvalue || outerHm.get(tableName).get(scolumnName).get(i).equals(outerHm.get(tableName).get(sSecondOperand).get(i)))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    //for fifth
                    if(feq ==1 && sgte == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i).equals(fvalue) || outerHm.get(tableName).get(scolumnName).get(i) >= outerHm.get(tableName).get(sSecondOperand).get(i))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if( feq ==1 && slte == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i).equals(fvalue) || outerHm.get(tableName).get(scolumnName).get(i) <= outerHm.get(tableName).get(sSecondOperand).get(i))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(feq ==1 && sgt == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i).equals(fvalue) || outerHm.get(tableName).get(scolumnName).get(i) > outerHm.get(tableName).get(sSecondOperand).get(i))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(feq ==1 && slt == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i).equals(fvalue) || outerHm.get(tableName).get(scolumnName).get(i) < outerHm.get(tableName).get(sSecondOperand).get(i))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(feq ==1 && seq == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i).equals(fvalue) || outerHm.get(tableName).get(scolumnName).get(i).equals(outerHm.get(tableName).get(sSecondOperand).get(i)))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    
                }
                if(  "*".equals (columns.get(0) ) )
                {
                    //code to print all columns
                    for(int i = 0 ;i<desired_col.size();i++)
                    {
                        System.out.print(tables.get(0)+"."+desired_col.get(i) );
                        if( i+1 != desired_col.size() )
                            System.out.print(",");
                    }
                    System.out.println("");
                    
                   int resultRows = tempOuterHm.get(tableName).get(desired_col.get(0)).size();
                    //System.out.println(resultRows);
                    for(int m = 0 ; m<resultRows ;m++){
                        for(int n = 0 ; n < desired_col.size() ; n++){
                            System.out.print(tempOuterHm.get(tableName).get(desired_col.get(n)).get(m) );
                            if(n+1 != desired_col.size())
                                System.out.print(",");
                        }
                        if(m+1 != resultRows)
                            System.out.println("");
                    }
                }
                else
                {
                    for(int i = 0 ;i<columns.size();i++)
                    {
                        System.out.print(tables.get(0)+"."+columns.get(i) );
                        if(i+1!=columns.size())
                             System.out.print(",");
                    }
                    System.out.println("");
                    int resultRows = tempOuterHm.get(tableName).get(columns.get(0)).size();
                    for(int m = 0 ; m<resultRows ;m++){
                        for(int n = 0 ; n < columns.size() ; n++){
                            System.out.print(tempOuterHm.get(tableName).get(columns.get(n)).get(m) );
                            if(n+1!=columns.size())
                                System.out.print(",");
                        }
                        if(m+1!=resultRows)
                            System.out.println("");
                    }
                }
                }
                //////////////////////////////////////////////////////////////////////////////////////////////
                if( fSecondOperandIsString == 1 && sSecondOperandIsString == 1 )
                {
                    //System.out.println( sSecondOperand );
                   sSecondOperand = sSecondOperand.substring(sSecondOperand.indexOf(".")+1);
                   fSecondOperand = fSecondOperand.substring(fSecondOperand.indexOf(".")+1);
                for (int i = 0 ; i<rows ; i++){
                    // for first ***********************************************************************************
                    if(fgte ==1 && sgte == 1){
                        if( outerHm.get(tableName).get( fcolumnName).get(i) >= outerHm.get(tableName).get(fSecondOperand).get(i) || outerHm.get(tableName).get(scolumnName).get(i) >= outerHm.get(tableName).get(sSecondOperand).get(i))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(fgte ==1 && slte == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) >= outerHm.get(tableName).get(fSecondOperand).get(i) || outerHm.get(tableName).get(scolumnName).get(i) <= outerHm.get(tableName).get(sSecondOperand).get(i))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(fgte ==1 && sgt == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) >= outerHm.get(tableName).get(fSecondOperand).get(i) || outerHm.get(tableName).get(scolumnName).get(i) > outerHm.get(tableName).get(sSecondOperand).get(i))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(fgte ==1 && slt == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) >= outerHm.get(tableName).get(fSecondOperand).get(i) || outerHm.get(tableName).get(scolumnName).get(i) < outerHm.get(tableName).get(sSecondOperand).get(i))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(fgte ==1 && seq == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) >= outerHm.get(tableName).get(fSecondOperand).get(i) || outerHm.get(tableName).get(scolumnName).get(i).equals(outerHm.get(tableName).get(sSecondOperand).get(i)))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    
                    // for second
                    if(flte ==1 && sgte == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) <= outerHm.get(tableName).get(fSecondOperand).get(i) || outerHm.get(tableName).get(scolumnName).get(i) >= outerHm.get(tableName).get(sSecondOperand).get(i))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(flte ==1 && slte == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) <= outerHm.get(tableName).get(fSecondOperand).get(i) || outerHm.get(tableName).get(scolumnName).get(i) <= outerHm.get(tableName).get(sSecondOperand).get(i))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(flte ==1 && sgt == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) <= outerHm.get(tableName).get(fSecondOperand).get(i) || outerHm.get(tableName).get(scolumnName).get(i) > outerHm.get(tableName).get(sSecondOperand).get(i))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(flte ==1 && slt == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) <= outerHm.get(tableName).get(fSecondOperand).get(i) || outerHm.get(tableName).get(scolumnName).get(i) < outerHm.get(tableName).get(sSecondOperand).get(i))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(flte ==1 && seq == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) <= outerHm.get(tableName).get(fSecondOperand).get(i) || outerHm.get(tableName).get(scolumnName).get(i).equals(outerHm.get(tableName).get(sSecondOperand).get(i)))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    
                    // for third 
                    
                    if(flt ==1 && sgte == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) < outerHm.get(tableName).get(fSecondOperand).get(i) || outerHm.get(tableName).get(scolumnName).get(i) >= outerHm.get(tableName).get(sSecondOperand).get(i))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if( flt ==1 && slte == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) < outerHm.get(tableName).get(fSecondOperand).get(i) || outerHm.get(tableName).get(scolumnName).get(i) <= outerHm.get(tableName).get(sSecondOperand).get(i))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(flt ==1 && sgt == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) < outerHm.get(tableName).get(fSecondOperand).get(i) || outerHm.get(tableName).get(scolumnName).get(i) > outerHm.get(tableName).get(sSecondOperand).get(i))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(flt ==1 && slt == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) < outerHm.get(tableName).get(fSecondOperand).get(i) || outerHm.get(tableName).get(scolumnName).get(i) < outerHm.get(tableName).get(sSecondOperand).get(i))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(flt ==1 && seq == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) < outerHm.get(tableName).get(fSecondOperand).get(i) || outerHm.get(tableName).get(scolumnName).get(i).equals(outerHm.get(tableName).get(sSecondOperand).get(i)))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    //for forth
                    if(fgt ==1 && sgte == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) > outerHm.get(tableName).get(fSecondOperand).get(i) || outerHm.get(tableName).get(scolumnName).get(i) >= outerHm.get(tableName).get(sSecondOperand).get(i))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if( fgt ==1 && slte == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) > outerHm.get(tableName).get(fSecondOperand).get(i) || outerHm.get(tableName).get(scolumnName).get(i) <= outerHm.get(tableName).get(sSecondOperand).get(i))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(fgt ==1 && sgt == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) > outerHm.get(tableName).get(fSecondOperand).get(i) || outerHm.get(tableName).get(scolumnName).get(i) > outerHm.get(tableName).get(sSecondOperand).get(i))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(fgt ==1 && slt == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) > outerHm.get(tableName).get(fSecondOperand).get(i) || outerHm.get(tableName).get(scolumnName).get(i) < outerHm.get(tableName).get(sSecondOperand).get(i))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(fgt ==1 && seq == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i) > outerHm.get(tableName).get(fSecondOperand).get(i) || outerHm.get(tableName).get(scolumnName).get(i).equals(outerHm.get(tableName).get(sSecondOperand).get(i)))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    //for fifth
                    if(feq ==1 && sgte == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i).equals(outerHm.get(tableName).get(fSecondOperand).get(i)) || outerHm.get(tableName).get(scolumnName).get(i) >= outerHm.get(tableName).get(sSecondOperand).get(i))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if( feq ==1 && slte == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i).equals(outerHm.get(tableName).get(fSecondOperand).get(i)) || outerHm.get(tableName).get(scolumnName).get(i) <= outerHm.get(tableName).get(sSecondOperand).get(i))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(feq ==1 && sgt == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i).equals(outerHm.get(tableName).get(fSecondOperand).get(i)) || outerHm.get(tableName).get(scolumnName).get(i) > outerHm.get(tableName).get(sSecondOperand).get(i))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(feq ==1 && slt == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i).equals(outerHm.get(tableName).get(fSecondOperand).get(i)) || outerHm.get(tableName).get(scolumnName).get(i) < outerHm.get(tableName).get(sSecondOperand).get(i))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    if(feq ==1 && seq == 1){
                        if( outerHm.get(tableName).get(fcolumnName).get(i).equals(outerHm.get(tableName).get(fSecondOperand).get(i)) || outerHm.get(tableName).get(scolumnName).get(i).equals(outerHm.get(tableName).get(sSecondOperand).get(i)))
                        {
                            for(int j = 0 ; j<desired_col.size() ;j++)
                            {    
                                tempOuterHm.get(tableName).get(desired_col.get(j)).add( outerHm.get(tableName).get(desired_col.get(j)).get(i) );
                            }
                        }
                    }
                    
                }
                if(  "*".equals (columns.get(0) ) )
                {
                    //code to print all columns
                    for(int i = 0 ;i<desired_col.size();i++)
                    {
                        System.out.print(tables.get(0)+"."+desired_col.get(i) );
                        if( i+1 != desired_col.size() )
                            System.out.print(",");
                    }
                    System.out.println("");
                    
                   int resultRows = tempOuterHm.get(tableName).get(desired_col.get(0)).size();
                    //System.out.println(resultRows);
                    for(int m = 0 ; m<resultRows ;m++){
                        for(int n = 0 ; n < desired_col.size() ; n++){
                            System.out.print(tempOuterHm.get(tableName).get(desired_col.get(n)).get(m) );
                            if(n+1 != desired_col.size())
                                System.out.print(",");
                        }
                        if(m+1 != resultRows)
                            System.out.println("");
                    }
                }
                else
                {
                    for(int i = 0 ;i<columns.size();i++)
                    {
                        System.out.print(tables.get(0)+"."+columns.get(i) );
                        if(i+1!=columns.size())
                             System.out.print(",");
                    }
                    System.out.println("");
                    int resultRows = tempOuterHm.get(tableName).get(columns.get(0)).size();
                    for(int m = 0 ; m<resultRows ;m++){
                        for(int n = 0 ; n < columns.size() ; n++){
                            System.out.print(tempOuterHm.get(tableName).get(columns.get(n)).get(m) );
                            if(n+1!=columns.size())
                                System.out.print(",");
                        }
                        if(m+1!=resultRows)
                            System.out.println("");
                    }
                }
                }
            }    
        }
        }
        }
        catch(Exception e)
        {
            System.out.println("error");
        }
    }
}

