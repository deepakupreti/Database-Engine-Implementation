package sql;


import gudusoft.gsqlparser.*;
import gudusoft.gsqlparser.nodes.TJoin;
import gudusoft.gsqlparser.nodes.TJoinItem;
import gudusoft.gsqlparser.nodes.TResultColumn;
import gudusoft.gsqlparser.stmt.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
public class SqlParser {
    static int distinct_flag = 0 , whereFlag = 0 , hasDotFlag =0;
    public static void main(String args[]){
        int have_aggregate = 0 , normal_flag =0;
        TGSqlParser sqlparser = new TGSqlParser(EDbVendor.dbvoracle);
        //Scanner scan = new Scanner(System.in);
        sqlparser.sqltext=""+args[0]+"";
        //sqlparser.sqltext = scan.nextLine();
        //sqlparser.sqltext = sqlparser.sqltext.toLowerCase();
        
        if(sqlparser.sqltext.contains("DISTINCT") || sqlparser.sqltext.contains("distinct")){
            distinct_flag= 1;
        }
        if(sqlparser.sqltext.contains(".") || sqlparser.sqltext.contains(".")){
            hasDotFlag= 1;
        }
        int ret=-1 ;
        try{
        ret = sqlparser.parse();
        }
        catch (NullPointerException e){
            System.out.println("Enter a valid query.....");         
        }
        if (ret == 0){
            for(int i=0;i<sqlparser.sqlstatements.size();i++){
                analyzeStmt(sqlparser.sqlstatements.get(i));
            }
        }else{
      
           System.out.println(sqlparser.getErrormessage());
           
        }
    }

    protected static void analyzeStmt(TCustomSqlStatement stmt){
        switch(stmt.sqlstatementtype){
            case sstselect:
                analyzeSelectStmt((TSelectSqlStatement)stmt);
                break;
            case sstupdate:
                break;
            case sstcreatetable:
                break;
            case sstaltertable:
                break;
            case sstcreateview:
                break;
            default:
                System.out.println(stmt.sqlstatementtype.toString());
        }
    }

    protected static void analyzeSelectStmt(TSelectSqlStatement pStmt){
       // System.out.println("\nSelect statement:");
        ArrayList<String> tables= new ArrayList<>();
        ArrayList<String> columns = new ArrayList<>();
        String whereCon = "";
        
        
        
        if (pStmt.isCombinedQuery()){
            String setstr="";
            switch (pStmt.getSetOperator()){
                case 1: setstr = "union";break;
                case 2: setstr = "union all";break;
                case 3: setstr = "intersect";break;
                case 4: setstr = "intersect all";break;
                case 5: setstr = "minus";break;
                case 6: setstr = "minus all";break;
                case 7: setstr = "except";break;
                case 8: setstr = "except all";break;
            }
            System.out.printf("set type: %s\n",setstr);
            System.out.println("left select:");
            analyzeSelectStmt(pStmt.getLeftStmt());
            System.out.println("right select:");
            analyzeSelectStmt(pStmt.getRightStmt());
            if (pStmt.getOrderbyClause() != null){
                System.out.printf("order by clause %s\n",pStmt.getOrderbyClause().toString());
            }
        }else{
            //select list
            for(int i=0; i < pStmt.getResultColumnList().size();i++){
                TResultColumn resultColumn = pStmt.getResultColumnList().getResultColumn(i);
                columns.add(resultColumn.getExpr().toString());
               // System.out.printf("\tColumn: %s, Alias: %s\n",resultColumn.getExpr().toString(), (resultColumn.getAliasClause() == null)?"":resultColumn.getAliasClause().toString());
            }

            //from clause, check this document for detailed information
           
            for(int i=0; i<pStmt.joins.size();i++){
                TJoin join = pStmt.joins.getJoin(i);
                switch (join.getKind()){
                    case TBaseType.join_source_fake:
                        tables.add(join.getTable().toString());
                       // System.out.printf("\ntable: \n\t%s, alias: %s\n",join.getTable().toString(),(join.getTable().getAliasClause() !=null)?join.getTable().getAliasClause().toString():"");
                        //System.out.printf("\ntablezxcvsdf: \n\t%s, alias: %s\n",join.getTable().toString(),(join.getTable().getAliasClause() !=null)?join.getTable().getAliasClause().toString():"");
                        break;
                    case TBaseType.join_source_table:
                        System.out.printf("\ntable: \n\t%s, alias: %s\n",join.getTable().toString(),(join.getTable().getAliasClause() !=null)?join.getTable().getAliasClause().toString():"");
                        for(int j=0;j<join.getJoinItems().size();j++){
                            TJoinItem joinItem = join.getJoinItems().getJoinItem(j);
                            System.out.printf("Join type: %s\n",joinItem.getJoinType().toString());
                            System.out.printf("table3: %s, alias: %s\n",joinItem.getTable().toString(),(joinItem.getTable().getAliasClause() !=null)?joinItem.getTable().getAliasClause().toString():"");
                            if (joinItem.getOnCondition() != null){
                                System.out.printf("On: %s\n",joinItem.getOnCondition().toString());
                            }else  if (joinItem.getUsingColumns() != null){
                                System.out.printf("using: %s\n",joinItem.getUsingColumns().toString());
                            }
                        }
                        break;
                    case TBaseType.join_source_join:
                        TJoin source_join = join.getJoin();
                        //System.out.printf("\ntable: \n\t%s, alias: %s\n",source_join.getTable().toString(),(source_join.getTable().getAliasClause() !=null)?source_join.getTable().getAliasClause().toString():"");

                        for(int j=0;j<source_join.getJoinItems().size();j++){
                            TJoinItem joinItem = source_join.getJoinItems().getJoinItem(j);
                            System.out.printf("source_join type: %s\n",joinItem.getJoinType().toString());
                            System.out.printf("table: %s, alias: %s\n",joinItem.getTable().toString(),(joinItem.getTable().getAliasClause() !=null)?joinItem.getTable().getAliasClause().toString():"");
                            if (joinItem.getOnCondition() != null){
                                System.out.printf("On: %s\n",joinItem.getOnCondition().toString());
                            }else  if (joinItem.getUsingColumns() != null){
                                System.out.printf("using: %s\n",joinItem.getUsingColumns().toString());
                            }
                        }

                        for(int j=0;j<join.getJoinItems().size();j++){
                            TJoinItem joinItem = join.getJoinItems().getJoinItem(j);
                            System.out.printf("Join type: %s\n",joinItem.getJoinType().toString());
                            System.out.printf("table: %s, alias: %s\n",joinItem.getTable().toString(),(joinItem.getTable().getAliasClause() !=null)?joinItem.getTable().getAliasClause().toString():"");
                            if (joinItem.getOnCondition() != null){
                                System.out.printf("On: %s\n",joinItem.getOnCondition().toString());
                            }else  if (joinItem.getUsingColumns() != null){
                                System.out.printf("using: %s\n",joinItem.getUsingColumns().toString());
                            }
                        }

                        break;
                    default:
                        System.out.println("unknown type in join!");
                        break;
                }
            }

            //where clause
            if (pStmt.getWhereClause() != null){
                //System.out.printf("\nwhere clause: \n\t%s\n", pStmt.getWhereClause().getCondition().toString());
                whereCon = pStmt.getWhereClause().getCondition().toString() ;
                whereFlag = 1;
            }

            // group by
            if (pStmt.getGroupByClause() != null){
                System.out.printf("\ngroup by: \n\t%s\n",pStmt.getGroupByClause().toString());
            }

            // order by
            if (pStmt.getOrderbyClause() != null){
              System.out.printf("\norder by:");
                for(int i=0;i<pStmt.getOrderbyClause().getItems().size();i++){
                    System.out.printf("\n\t%s",pStmt.getOrderbyClause().getItems().getOrderByItem(i).toString());

                }
            }

            // for update
            if (pStmt.getForUpdateClause() != null){
                System.out.printf("for update: \n%s\n",pStmt.getForUpdateClause().toString());
            }

            // top clause
            if (pStmt.getTopClause() != null){
                System.out.printf("top clause: \n%s\n",pStmt.getTopClause().toString());
            }

            // limit clause
            if (pStmt.getLimitClause() != null){
                System.out.printf("top clause: \n%s\n",pStmt.getLimitClause().toString());
            }
        }
        
        //code to form hashmap 
        HashMap<String , HashMap<String,ArrayList<Integer>>> outerHm = new HashMap<>();
        HashMap<String, ArrayList> innerHm = new HashMap<>(); //to store column name and its data
        ArrayList<Integer> al= new ArrayList<>();
        String csvFile = "metadata.txt";
	BufferedReader br = null;
	String line = "";
        ArrayList<String> desired_col= new ArrayList<>() ;
        int flag = 0;
	try {
		br = new BufferedReader(new FileReader(csvFile)) ;
		while ((line = br.readLine()) != null) {
                        String table_Name = "" ;
                        if( "<begin_table>".equals(line) )
                        {
                            ArrayList <String> actual_col_order = new ArrayList<>();
                            HashMap <String,ArrayList<Integer>> temp = new HashMap<>();
                            table_Name = br.readLine();
                            if(tables.size() == 1)
                                if(table_Name.equals(tables.get(0)) ){
                                    flag = 1;
                                }
                            if(tables.size()==2)
                                if(table_Name.equals(tables.get(0)) || table_Name.equals(tables.get(1))){
                                    flag = 1;
                                }
                            //String s = br.readLine();
                            while(true){
                                String s;
                                s = br.readLine();
                                if("<end_table>".equals(s) && flag == 1 && tables.size() == 2)
                                {
                                    desired_col.add("end_table");
                                    break;
                                }
                                if("<end_table>".equals(s))
                                     break;
                                ArrayList <Integer> tempa =new ArrayList<>();
                                actual_col_order.add(s);
                                if(flag == 1)
                                desired_col.add(s);
                                temp.put(s,tempa);
                            }
                            outerHm.put(table_Name,temp);
                            //System.out.println("actual column order : "+actual_col_order);
//Now the outer empty structure of hashmap is made. Its time to fill values in it by reading .csv file
                            
                            





        //String csv = "/home/sambhav/Desktop/testSuite/";
        String csv = table_Name + ".csv";

                
                
            BufferedReader csvbr = null;
            String csvline = "";
            String cvsSplitBy = ",";
            try{
                    csvbr = new BufferedReader(new FileReader(csv));
                    while ((csvline = csvbr.readLine()) != null) {
                        String[] row = csvline.split(cvsSplitBy);
                       // System.out.println("row length : "+row.length);
                        for(int i=0;i<row.length;i++){
                            //System.out.println(row[i]);
                           row[i] = row[i].replace("\"" ,"");
                            int value = Integer.parseInt(row[i]);
                         //   System.out.println(value);
                            //System.out.println(actual_col_order);
                            outerHm.get(table_Name).get(actual_col_order.get(i)).add(value);
                        }
                    }
            }catch (FileNotFoundException e) {
                    e.printStackTrace();
            } catch (IOException e) {
                    e.printStackTrace();
            } finally {
                    if (csvbr != null) {
                            try {
                                    csvbr.close();
                            } catch (IOException e) {
                                    e.printStackTrace();
                            }
                    }
                }
                }
                flag = 0;
           }
         }catch (FileNotFoundException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	} finally {
		if (br != null) {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
        //System.out.println(columns);
       // System.out.println(desired_col);
        //if(hasDotFlag == 0)
        
        //System.out.println(desired_col) ;
        int errorFlag = 0;
        for(int i=0 ;i<tables.size() ; i++)
        {
            String str = tables.get(i);
            if( !(outerHm.containsKey(str)) )
            {
                System.out.println("error");
                errorFlag = 1;
            }
        }
        
        if(errorFlag == 0)
        {
            if(tables.size() ==1 )
                result.print_result(tables,columns,whereCon,outerHm,desired_col);
            if(tables.size() == 2)
               doubleTableResult.fun(tables,columns,whereCon, outerHm,desired_col);
        }

    }
}
