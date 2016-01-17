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
public class doubleTableResult {
    static void fun(ArrayList<String> tables, ArrayList<String> columns,String whereCon, HashMap<String, HashMap<String, ArrayList<Integer>>> outerHm,ArrayList<String> desired_col)
    {
           
        ArrayList<String> desiredColOfFirst = new ArrayList<>();
        ArrayList<String> desiredColOfSecond = new ArrayList<>();
        //System.out.println(columns);
       try
        { 
        for(int i=0;i<desired_col.size(); i++)
        {
             while(true)
             {
                 if("end_table".equals(desired_col.get(i)))
                 {
                     i++;
                     break;
                 }
                 else
                 {
                     desiredColOfFirst.add(desired_col.get(i));
                     i++;
                 }
             }
             while(true)
             {
                 if("end_table".equals(desired_col.get(i)))
                 {
                     i++;
                     break;
                 }
                 else
                 {
                     desiredColOfSecond.add(desired_col.get(i));
                     i++;
                 }
             }
        }
//        System.out.println(desiredColOfFirst);
//        System.out.println(desiredColOfSecond);
//        System.out.println(tables);
//        System.out.println(columns);
        //HashMap<String , String> tableCols = new HashMap<>();
        String tempTableName = tables.get(0);
        
//        for(int i = 0 ;i<columns.size() ;i++)
//        {
//           if(columns.get(i).contains("."))
//           {
//               String tempTable = columns.get(i).substring(0, columns.get(i).indexOf("."));
//               String tempCol = columns.get(i).substring(columns.get(i).indexOf(".") +1  ) ;
//               tempTable = tempTable.trim();
//               tempCol = tempCol.trim();
//           }
//        }
        HashMap<  String ,   HashMap<String , Integer>> hmForTableAndColumn = new HashMap<>();
        //HashMap<String ,   HashMap<String , Integer>  > hmForSecondTable = new HashMap<>();
        //hmForTableAndColumn.put(tables.get(0), null);
        //hmForTableAndColumn.put(tables.get(1), null);
        
//        System.out.println(hmForFirstTable);
//        System.out.println(hmForSecondTable);
//        System.out.println("//////////////////////////////////////");
        Integer index =0;
        HashMap <String , Integer> tempHashMap = new HashMap<>();
        for(int i=0 ;i<desiredColOfFirst.size();i++)
        {
            String tempColumnName = desiredColOfFirst.get(i);
            tempHashMap.put(tempColumnName, i);
            index++;
        }
        
        hmForTableAndColumn.put(tables.get(0), tempHashMap);
        //tempHashMap.clear();
         HashMap <String , Integer> tempHashMap1 = new HashMap<>();
        for(int j=0 ;j<desiredColOfSecond.size();j++)
        {
            String tempColumnName = desiredColOfSecond.get(j);
            tempHashMap1.put(tempColumnName , index);
            index++;
        }
        hmForTableAndColumn.put(tables.get(1), tempHashMap1);
//        System.out.println(hmForTableAndColumn);
//        System.out.println("//////////////////////////////////////////////////////");
        
        String tableFirst = tables.get(0);
        String tableSecond = tables.get(1);
        int columnInFirstTable = outerHm.get(tableFirst).size();
        int columnInSecondTable = outerHm.get(tableSecond).size();
        int rowInFirstTable = outerHm.get(tableFirst).get(desiredColOfFirst.get(0)).size();
        int rowInSecondTable = outerHm.get(tableSecond).get(desiredColOfSecond.get(0)).size();
        Integer [][] crossProduct = new Integer[ rowInFirstTable * rowInSecondTable+10][columnInFirstTable+columnInSecondTable+10];
        int rowIndex =0 , columnIndex =0;
        for(int p = 0 ; p<rowInFirstTable ;p++)
        {
            columnIndex =0;
            for(int k = 0 ; k < rowInSecondTable ; k++)
            {
                columnIndex = 0;
                for(int i =0 ; i<desiredColOfFirst.size();i++)
                {
                    crossProduct[rowIndex][columnIndex] = outerHm.get(tableFirst).get(desiredColOfFirst.get(i) ).get(p);
                    columnIndex++;
                }
                for(int i =0 ; i<desiredColOfSecond.size();i++)
                {
                    crossProduct[rowIndex][columnIndex] = outerHm.get(tableSecond).get(desiredColOfSecond.get(i)).get(k);
                    columnIndex++;
                }
                rowIndex++;
            }
        }
        if(SqlParser.whereFlag == 1)
        {
            //System.out.println("asdfadsfadsf");
            int singleCon = 0 , doubleCon = 0 , andFlag = 0 , orFlag = 0;
            if(whereCon.toLowerCase().contains(" and ")    )
            {
                doubleCon = 1;
                andFlag = 1;
            }
            else if( whereCon.toLowerCase().contains(" OR ")    )
            {
                doubleCon = 1;
                andFlag = 1;
            }
            else
            {
                singleCon = 1;
            }
            if(singleCon == 1)
            {
                   // System.out.println("asdfadsfads");
                int gt =0 , lt = 0 , lte = 0, gte=0, eq =0;
                String [] colAndCondition = null;
                if( whereCon.contains(">=") )
                {
                    gte = 1;
                    colAndCondition = whereCon.split(">=");
                }
                else if( whereCon.contains("<=") )
                {
                    lte = 1;
                    colAndCondition = whereCon.split("<=");
                }

                else if(whereCon.contains("=") && gte == 0 && lte ==0)
                {
                    eq = 1;
                    colAndCondition = whereCon.split("=");
                }
                else if( whereCon.contains(">")  && gte != 1)
                {
                        //System.out.println("asdfadsfas");
                    gt = 1;
                    colAndCondition = whereCon.split(">");
                }
                else if( whereCon.contains("<")  && lte != 1)
                {
                    lt = 1;
                    colAndCondition = whereCon.split("<");
                }
                String columnName = colAndCondition[0].trim();
//                System.out.println(columnName);
                String whereTable = null ;
                String whereColumn ;
                if(columnName.contains("."))
                {
                    whereTable = columnName.substring(0 , columnName.indexOf("."));
                    //columns.get(i).substring(columns.get(i).indexOf(".")+1)
                    whereColumn = columnName.substring(columnName.indexOf(".") +1);
//                    System.out.println(whereTable);
//                    System.out.println(whereColumn);
                }
                else
                {
                    if(desiredColOfFirst.contains(columnName) )
                        whereTable = tables.get(0) ;
                    if(desiredColOfSecond.contains(columnName))
                        whereTable = tables.get(1);
                    whereColumn = columnName;
                }
                int secondOperandIsString = 0;
                Integer value = null ;
                try
                {
                    value = Integer.parseInt(colAndCondition[1].trim() );
                }
                catch(Exception e){
                    secondOperandIsString = 1;
                    //System.out.println("Incatch " + colAndCondition[1]);
                }
                if(secondOperandIsString == 0)
                {
                    index = hmForTableAndColumn.get(whereTable).get(whereColumn);
//                    System.out.println(index);
                    Integer result[][] = new Integer[ rowInFirstTable * rowInSecondTable+10][columnInFirstTable+columnInSecondTable+10];
                    int resultColIndex = 0 ;
                    for (int i = 0 ; i< rowInFirstTable * rowInSecondTable ; i++)
                    {
                        if(gte ==1){
                            if( crossProduct[i][index] >= value  )
                            {
                                for(int j = 0 ; j<columnInFirstTable + columnInSecondTable  ;j++)
                                {
                                    result[resultColIndex][j] = crossProduct[i][j];
                                }
                                resultColIndex++;
                            }
                        }
                        

                        if(eq ==1){
                            if( crossProduct[i][index] == value  )
                            {
                                for(int j = 0 ; j<columnInFirstTable + columnInSecondTable  ;j++)
                                {
                                    result[resultColIndex][j] = crossProduct[i][j];
                                }
                                resultColIndex++;
                            }
                        }

                        if(lt ==1){
                            if( crossProduct[i][index] < value  )
                            {
                                for(int j = 0 ; j<columnInFirstTable + columnInSecondTable  ;j++)
                                {
                                    result[resultColIndex][j] = crossProduct[i][j];
                                }
                                resultColIndex++;
                            }
                        }
                        if(gt ==1){
                            if( crossProduct[i][index] > value  )
                            {
                                for(int j = 0 ; j<columnInFirstTable + columnInSecondTable  ;j++)
                                {
                                    result[resultColIndex][j] = crossProduct[i][j];
                                }
                                resultColIndex++;
                            }
                        }
                        if(lte ==1){
                            if( crossProduct[i][index] <= value  )
                            {
                                for(int j = 0 ; j<columnInFirstTable + columnInSecondTable  ;j++)
                                {
                                    result[resultColIndex][j] = crossProduct[i][j];
                                }
                                resultColIndex++;
                            }
                        }
                    }
                    if("*".equals(columns.get(0)))
                    {
                          for(int i= 0 ;i<desiredColOfFirst.size() ;i++)
                {
                    System.out.print(tableFirst + "."+ desiredColOfFirst.get(i));
                    System.out.print(",");
                }
                for(int i= 0 ;i<desiredColOfSecond.size() ;i++)
                {
                    System.out.print(tableSecond + "."+ desiredColOfSecond.get(i));
                    if(i+1 != desiredColOfSecond.size())
                        System.out.print(",");
                }
                System.out.println("");
                for(int i=0 ;i<resultColIndex;i++)
                {
                    for(int j=0 ; j< columnInFirstTable+columnInSecondTable ; j++)
                    {
                        System.out.print(result[i][j] );
                        if(j+1 != columnInFirstTable+columnInSecondTable)
                            System.out.print(",");
                    }
                    if(i+1 != rowInFirstTable * rowInSecondTable)
                        System.out.println("");
                }
                    }
                    else
                    {
                        HashMap <String , String> selectTableAndColumn = new HashMap<>();
                        for(int i = 0 ; i < columns.size() ;i++)
                        {
                            String tempTableNameInColumns = "";
                            String tempColumnNameInColumns = "";
                            if( columns.get(i).contains(".") )
                            {
                                tempTableNameInColumns = columns.get(i).substring(0, columns.get(i).indexOf("."));
                                tempColumnNameInColumns = columns.get(i).substring(columns.get(i).indexOf(".") +1  ) ;
                                tempTableNameInColumns = tempTableNameInColumns.trim();
                                tempColumnNameInColumns =tempColumnNameInColumns.trim();
                            }
                            else
                            {
                                if(desiredColOfFirst.contains(columns.get(i)) )
                                    tempTableNameInColumns = tables.get(0) ;
                                if(desiredColOfSecond.contains(columns.get(i)))
                                    tempTableNameInColumns = tables.get(1);
                                tempTableNameInColumns = tempTableNameInColumns.trim();
                                tempColumnNameInColumns = columns.get(i);
                            }
                            selectTableAndColumn.put(tempTableNameInColumns , tempColumnNameInColumns);
                            System.out.print(tempTableNameInColumns+"."+tempColumnNameInColumns);
                            if(i+1!= columns.size())
                                System.out.print(",");
                        }
                        
                        System.out.println("");
                        //System.out.println(columns);
                        for(int i = 0 ;i<resultColIndex ;i++)
                        {
                            for(int j = 0 ;j<columns.size() ;j++)
                            {
                                    //System.out.println("asdfadsf");
                                /////////////////////////////////////////////////
                                String tempTableNameInColumns = "";
                            String tempColumnNameInColumns = "";
                            if( columns.get(j).contains(".") )
                            {
                                tempTableNameInColumns = columns.get(j).substring(0, columns.get(j).indexOf("."));
                                tempColumnNameInColumns = columns.get(j).substring(columns.get(j).indexOf(".") +1  ) ;
                                tempTableNameInColumns = tempTableNameInColumns.trim();
                                tempColumnNameInColumns =tempColumnNameInColumns.trim();
                            }
                            else
                            {
                                if(desiredColOfFirst.contains(columns.get(j)) )
                                    tempTableNameInColumns = tables.get(0) ;
                                if(desiredColOfSecond.contains(columns.get(j)))
                                    tempTableNameInColumns = tables.get(1);
                                tempTableNameInColumns.trim();
                                tempColumnNameInColumns = columns.get(j);
                            }
                            
                                /////////////////////////////////////////////////
                                //System.out.println("---------"+tempTableNameInColumns+" "+tempColumnNameInColumns);
                                int indexOfColumn = hmForTableAndColumn.get(tempTableNameInColumns).get(tempColumnNameInColumns);
                                

                                System.out.print(result[i][indexOfColumn]);
                                if(j+1 != columns.size())
                                    System.out.print(",");
                            }
                            if(i+1!=resultColIndex)
                                System.out.println("");
                        }

                    }
                    
                    
                    
                    /////////////////////////////////////////////////////////////////////////////
                }
                if(secondOperandIsString == 1)
                {
                    //System.out.println("asdf"+colAndCondition[1].trim());
                    String secondOperand = colAndCondition[1].trim();
                    //System.out.println(whereTable);
                    //System.out.println(whereColumn);
                    String secondOperandTable = null;
                    String secondOperandColumn = null;
                    if(secondOperand.contains("."))
                {
                    secondOperandTable = secondOperand.substring(0 , secondOperand.indexOf("."));
                    //columns.get(i).substring(columns.get(i).indexOf(".")+1)
                    secondOperandColumn = secondOperand.substring(secondOperand.indexOf(".") +1);
                    secondOperandColumn = secondOperandColumn.trim();
                    secondOperandTable = secondOperandTable.trim();
//                    System.out.println(whereTable);
//                    System.out.println(whereColumn);
                }
                else
                {
                    if(desiredColOfFirst.contains(secondOperand) )
                        secondOperandTable = tables.get(0) ;
                    if(desiredColOfSecond.contains(secondOperand))
                        secondOperandTable = tables.get(1);
                    secondOperandColumn = secondOperand;
                }
                    //System.out.println("table and Column "+secondOperandTable + " "+ secondOperandColumn);
                 // whereTable whereColumn secondOperandTable secondOperandColumn
                if(whereColumn.equals(secondOperandColumn) && eq == 1)
                {
                   // System.out.println("it is join condition handle it and return");
                    
                    index = hmForTableAndColumn.get(whereTable).get(whereColumn);
                    int indexOfSecond = hmForTableAndColumn.get(secondOperandTable).get(secondOperandColumn);
//                    System.out.println(index);
                    Integer result[][] = new Integer[ rowInFirstTable * rowInSecondTable+10][columnInFirstTable+columnInSecondTable+10];
                    int resultColIndex = 0 ;
                    for (int i = 0 ; i< rowInFirstTable * rowInSecondTable ; i++)
                    {
                        if( crossProduct[i][index] == crossProduct[i][indexOfSecond]  )
                        {
                            for(int j = 0 ; j<columnInFirstTable + columnInSecondTable  ;j++)
                            {
                                result[resultColIndex][j] = crossProduct[i][j];
                            }
                            resultColIndex++;
                        }
                    }
                    if("*".equals(columns.get(0)))
                    {
                          for(int i= 0 ;i<desiredColOfFirst.size() ;i++)
                            {
                                System.out.print(tableFirst + "."+ desiredColOfFirst.get(i));
                                System.out.print(",");
                            }
                for(int i= 0 ;i<desiredColOfSecond.size() ;i++)
                {
                    if( !(desiredColOfSecond.get(i).equals(whereColumn)))
                    {
                    System.out.print(tableSecond + "."+ desiredColOfSecond.get(i));
                    if(i+1 != desiredColOfSecond.size())
                        System.out.print(",");
                
                    }
                }    
                System.out.println("");
                int indexToSkip = hmForTableAndColumn.get(secondOperandTable).get(secondOperandColumn);
                for(int i=0 ;i<resultColIndex;i++)
                {
                    for(int j=0 ; j< columnInFirstTable+columnInSecondTable ; j++)
                    {
                        if(j!=indexToSkip)
                        {    
                        System.out.print(result[i][j] );
                        if(j+1 != columnInFirstTable+columnInSecondTable)
                            System.out.print(",");
                    
                        }
                    }    
                    if(i+1 != rowInFirstTable * rowInSecondTable)
                        System.out.println("");
                }
                    }
                    else
                    {
                        HashMap <String , String> selectTableAndColumn = new HashMap<>();
                        int count =0;
                        for(int i = 0 ; i < columns.size() ;i++)
                        {
                            String tempTableNameInColumns = "";
                            String tempColumnNameInColumns = "";
                            if( columns.get(i).contains(".") )
                            {
                                tempTableNameInColumns = columns.get(i).substring(0, columns.get(i).indexOf("."));
                                tempColumnNameInColumns = columns.get(i).substring(columns.get(i).indexOf(".") +1  ) ;
                                tempTableNameInColumns = tempTableNameInColumns.trim();
                                tempColumnNameInColumns =tempColumnNameInColumns.trim();
                            }
                            else
                            {
                                if(desiredColOfFirst.contains(columns.get(i)) )
                                    tempTableNameInColumns = tables.get(0) ;
                                if(desiredColOfSecond.contains(columns.get(i)))
                                    tempTableNameInColumns = tables.get(1);
                                tempTableNameInColumns = tempTableNameInColumns.trim();
                                tempColumnNameInColumns = columns.get(i);
                            }
                            
                            selectTableAndColumn.put(tempTableNameInColumns , tempColumnNameInColumns);
                            
                            
                            if(tempColumnNameInColumns.equals(whereColumn))
                            {
                                //System.out.println("count++");
                                count++;
                                if(count == 1)
                                    continue;
                            }
                            
                            System.out.print(tempTableNameInColumns+"."+tempColumnNameInColumns);
                            if(i+1!= columns.size())
                                System.out.print(",");
                            
                            
                        }
                        
                        System.out.println("");
//                        if(tempColumnNameInColumns.equals(whereColumn))
//                            {
//                                System.out.println("count++");
//                                count++;
//                            }
                        for(int i = 0 ;i<resultColIndex ;i++)
                        {
                            count = 0;
                            for(int j = 0 ;j<columns.size() ;j++)
                            {
                                /////////////////////////////////////////////////
                                
                            String tempTableNameInColumns = "";
                            String tempColumnNameInColumns = "";
                            if( columns.get(j).contains(".") )
                            {
                                tempTableNameInColumns = columns.get(j).substring(0, columns.get(j).indexOf("."));
                                tempColumnNameInColumns = columns.get(j).substring(columns.get(j).indexOf(".") +1  ) ;
                                tempTableNameInColumns = tempTableNameInColumns.trim();
                                tempColumnNameInColumns =tempColumnNameInColumns.trim();
                            }
                            else
                            {
                                if(desiredColOfFirst.contains(columns.get(j)) )
                                    tempTableNameInColumns = tables.get(0) ;
                                if(desiredColOfSecond.contains(columns.get(j)))
                                    tempTableNameInColumns = tables.get(1);
                                tempTableNameInColumns.trim();
                                tempColumnNameInColumns = columns.get(j);
                            }
                            if(tempColumnNameInColumns.equals(whereColumn))
                            {
                                //System.out.println("count++");
                                count++;
                                if(count == 1)
                                    continue;
                            }
                                /////////////////////////////////////////////////
                                //System.out.println("---------"+tempTableNameInColumns+" "+tempColumnNameInColumns);
                                int indexOfColumn = hmForTableAndColumn.get(tempTableNameInColumns).get(tempColumnNameInColumns);
                                    
                                System.out.print(result[i][indexOfColumn]);
                                if(j+1 != columns.size())
                                    System.out.print(",");
                                }
                            //}
                            if(i+1!=resultColIndex)
                                System.out.println("");
                        }

                    }
                    return;
                }
                else
                {
                    index = hmForTableAndColumn.get(whereTable).get(whereColumn);
                    int indexOfSecond = hmForTableAndColumn.get(secondOperandTable).get(secondOperandColumn);
//                    System.out.println(index);
                    Integer result[][] = new Integer[ rowInFirstTable * rowInSecondTable+10][columnInFirstTable+columnInSecondTable+10];
                    int resultColIndex = 0 ;
                    for (int i = 0 ; i< rowInFirstTable * rowInSecondTable ; i++)
                    {
                        if(gte ==1){
                            if( crossProduct[i][index] >= crossProduct[i][indexOfSecond]  )
                            {
                                for(int j = 0 ; j<columnInFirstTable + columnInSecondTable  ;j++)
                                {
                                    result[resultColIndex][j] = crossProduct[i][j];
                                }
                                resultColIndex++;
                            }
                        }

//                        if(eq ==1){
//                            if( crossProduct[i][index] == value  )
//                            {
//                                for(int j = 0 ; j<columnInFirstTable + columnInSecondTable  ;j++)
//                                {
//                                    result[resultColIndex][j] = crossProduct[i][j];
//                                }
//                                resultColIndex++;
//                            }
//                        }

                        if(lt ==1){
                            if( crossProduct[i][index] < crossProduct[i][indexOfSecond]  )
                            {
                                for(int j = 0 ; j<columnInFirstTable + columnInSecondTable  ;j++)
                                {
                                    result[resultColIndex][j] = crossProduct[i][j];
                                }
                                resultColIndex++;
                            }
                        }
                        if(gt ==1){
                            if( crossProduct[i][index] > crossProduct[i][indexOfSecond]  )
                            {
                                for(int j = 0 ; j<columnInFirstTable + columnInSecondTable  ;j++)
                                {
                                    result[resultColIndex][j] = crossProduct[i][j];
                                }
                                resultColIndex++;
                            }
                        }
                        if(lte ==1){
                            if( crossProduct[i][index] <= crossProduct[i][indexOfSecond]  )
                            {
                                for(int j = 0 ; j<columnInFirstTable + columnInSecondTable  ;j++)
                                {
                                    result[resultColIndex][j] = crossProduct[i][j];
                                }
                                resultColIndex++;
                            }
                        }
                    }
                    if("*".equals(columns.get(0)))
                    {
                          for(int i= 0 ;i<desiredColOfFirst.size() ;i++)
                {
                    System.out.print(tableFirst + "."+ desiredColOfFirst.get(i));
                    System.out.print(",");
                }
                for(int i= 0 ;i<desiredColOfSecond.size() ;i++)
                {
                    System.out.print(tableSecond + "."+ desiredColOfSecond.get(i));
                    if(i+1 != desiredColOfSecond.size())
                        System.out.print(",");
                }
                System.out.println("");
                for(int i=0 ;i<resultColIndex;i++)
                {
                    for(int j=0 ; j< columnInFirstTable+columnInSecondTable ; j++)
                    {
                        System.out.print(result[i][j] );
                        if(j+1 != columnInFirstTable+columnInSecondTable)
                            System.out.print(",");
                    }
                    if(i+1 != rowInFirstTable * rowInSecondTable)
                        System.out.println("");
                }
                    }
                    else
                    {
                        HashMap <String , String> selectTableAndColumn = new HashMap<>();
                        for(int i = 0 ; i < columns.size() ;i++)
                        {
                            String tempTableNameInColumns = "";
                            String tempColumnNameInColumns = "";
                            if( columns.get(i).contains(".") )
                            {
                                tempTableNameInColumns = columns.get(i).substring(0, columns.get(i).indexOf("."));
                                tempColumnNameInColumns = columns.get(i).substring(columns.get(i).indexOf(".") +1  ) ;
                                tempTableNameInColumns = tempTableNameInColumns.trim();
                                tempColumnNameInColumns =tempColumnNameInColumns.trim();
                            }
                            else
                            {
                                if(desiredColOfFirst.contains(columns.get(i)) )
                                    tempTableNameInColumns = tables.get(0) ;
                                if(desiredColOfSecond.contains(columns.get(i)))
                                    tempTableNameInColumns = tables.get(1);
                                tempTableNameInColumns = tempTableNameInColumns.trim();
                                tempColumnNameInColumns = columns.get(i);
                            }
                            selectTableAndColumn.put(tempTableNameInColumns , tempColumnNameInColumns);
                            System.out.print(tempTableNameInColumns+"."+tempColumnNameInColumns);
                            if(i+1!= columns.size())
                                System.out.print(",");
                        }
                        
                        System.out.println("");
                        
                        for(int i = 0 ;i<resultColIndex ;i++)
                        {
                            for(int j = 0 ;j<columns.size() ;j++)
                            {
                                /////////////////////////////////////////////////
                                String tempTableNameInColumns = "";
                            String tempColumnNameInColumns = "";
                            if( columns.get(j).contains(".") )
                            {
                                tempTableNameInColumns = columns.get(j).substring(0, columns.get(j).indexOf("."));
                                tempColumnNameInColumns = columns.get(j).substring(columns.get(j).indexOf(".") +1  ) ;
                                tempTableNameInColumns = tempTableNameInColumns.trim();
                                tempColumnNameInColumns =tempColumnNameInColumns.trim();
                            }
                            else
                            {
                                if(desiredColOfFirst.contains(columns.get(j)) )
                                    tempTableNameInColumns = tables.get(0) ;
                                if(desiredColOfSecond.contains(columns.get(j)))
                                    tempTableNameInColumns = tables.get(1);
                                tempTableNameInColumns.trim();
                                tempColumnNameInColumns = columns.get(j);
                            }
                            
                                /////////////////////////////////////////////////
                                //System.out.println("---------"+tempTableNameInColumns+" "+tempColumnNameInColumns);
                                int indexOfColumn = hmForTableAndColumn.get(tempTableNameInColumns).get(tempColumnNameInColumns);
                                

                                System.out.print(result[i][indexOfColumn]);
                                if(j+1 != columns.size())
                                    System.out.print(",");
                            }
                            if(i+1!=resultColIndex)
                                System.out.println("");
                        }

                    }
                }
                }
                return ;
            }
            if(doubleCon==1)
            {
                return;
            }
        }
        if("*".equals(columns.get(0)))
                    {
                          for(int i= 0 ;i<desiredColOfFirst.size() ;i++)
                {
                    System.out.print(tableFirst + "."+ desiredColOfFirst.get(i));
                    System.out.print(",");
                }
                for(int i= 0 ;i<desiredColOfSecond.size() ;i++)
                {
                    System.out.print(tableSecond + "."+ desiredColOfSecond.get(i));
                    if(i+1 != desiredColOfSecond.size())
                        System.out.print(",");
                }
                System.out.println("");
                for(int i=0 ;i<rowInFirstTable * rowInSecondTable;i++)
                {
                    for(int j=0 ; j< columnInFirstTable+columnInSecondTable ; j++)
                    {
                        System.out.print(crossProduct[i][j] );
                        if(j+1 != columnInFirstTable+columnInSecondTable)
                            System.out.print(",");
                    }
                    if(i+1 != rowInFirstTable * rowInSecondTable)
                        System.out.println("");
                }
                    }
                    else
                    {
                        HashMap <String , String> selectTableAndColumn = new HashMap<>();
                        for(int i = 0 ; i < columns.size() ;i++)
                        {
                            String tempTableNameInColumns = "";
                            String tempColumnNameInColumns = "";
                            if( columns.get(i).contains(".") )
                            {
                                tempTableNameInColumns = columns.get(i).substring(0, columns.get(i).indexOf("."));
                                tempColumnNameInColumns = columns.get(i).substring(columns.get(i).indexOf(".") +1  ) ;
                                tempTableNameInColumns = tempTableNameInColumns.trim();
                                tempColumnNameInColumns =tempColumnNameInColumns.trim();
                            }
                            else
                            {
                                if(desiredColOfFirst.contains(columns.get(i)) )
                                    tempTableNameInColumns = tables.get(0) ;
                                if(desiredColOfSecond.contains(columns.get(i)))
                                    tempTableNameInColumns = tables.get(1);
                                tempTableNameInColumns = tempTableNameInColumns.trim();
                                tempColumnNameInColumns = columns.get(i);
                            }
                            selectTableAndColumn.put(tempTableNameInColumns , tempColumnNameInColumns);
                            System.out.print(tempTableNameInColumns+"."+tempColumnNameInColumns);
                            if(i+1!= columns.size())
                                System.out.print(",");
                        }
                        
                        System.out.println("");
                        
                        for(int i = 0 ;i<rowInFirstTable * rowInSecondTable ;i++)
                        {
                            for(int j = 0 ;j<columns.size() ;j++)
                            {
                                /////////////////////////////////////////////////
                                String tempTableNameInColumns = "";
                            String tempColumnNameInColumns = "";
                            if( columns.get(j).contains(".") )
                            {
                                tempTableNameInColumns = columns.get(j).substring(0, columns.get(j).indexOf("."));
                                tempColumnNameInColumns = columns.get(j).substring(columns.get(j).indexOf(".") +1  ) ;
                                tempTableNameInColumns = tempTableNameInColumns.trim();
                                tempColumnNameInColumns =tempColumnNameInColumns.trim();
                            }
                            else
                            {
                                if(desiredColOfFirst.contains(columns.get(j)) )
                                    tempTableNameInColumns = tables.get(0) ;
                                if(desiredColOfSecond.contains(columns.get(j)))
                                    tempTableNameInColumns = tables.get(1);
                                tempTableNameInColumns.trim();
                                tempColumnNameInColumns = columns.get(j);
                            }
                            
                                /////////////////////////////////////////////////
                                //System.out.println("---------"+tempTableNameInColumns+" "+tempColumnNameInColumns);
                                int indexOfColumn = hmForTableAndColumn.get(tempTableNameInColumns).get(tempColumnNameInColumns);
                                

                                System.out.print(crossProduct[i][indexOfColumn]);   
                                
                                if(j+1 != columns.size())
                                    System.out.print(",");
                            }
                            if(i+1!=rowInFirstTable * rowInSecondTable)
                                System.out.println("");
                        }

                    }
    }
    catch (Exception E)
    {
        System.out.println("Error");
        return;
    }
    }
}
 

