package util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.util.*;
import java.sql.ResultSetMetaData;
/**
 *	@Author: jx
 *  @Date: Dec 21, 2016 9:44:24 PM
 * 	@Description: MySQL数据库操作工具类
 *  @Version: v2.0
 */

//@TODO
//考虑多线程情况下，多个线程同时使用DBUtil的影响
//close()函数什么时候调用？
public class DBUtil {

	private static final String url = PropertyHandler.getProperty("dbUrl") +
            PropertyHandler.getProperty("dbName") +
            PropertyHandler.getProperty("dbParameters");
	private static final String driver = PropertyHandler.getProperty("dbDriver");
	private static final String user = PropertyHandler.getProperty("dbUser");
	private static final String password = PropertyHandler.getProperty("dbPassword");

	private Connection conn = null;
	private Statement stmt = null;
	private ResultSet rs = null;

	/**
     * 默认构造函数
     * 在不使用批量写入数据库时构造
	* */
	public DBUtil(){
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url,user,password);
			stmt = conn.createStatement();
		} catch(Exception e){
			e.printStackTrace();
			close();
		}
	}

	/**
     * 设置isAutoCommit = false则关闭自动提交
     * 用于在使用批量写入操作时构造
	* */
	public DBUtil(boolean isAutoCommit){
		if(!isAutoCommit){
			System.out.println("WARN:[sql batch autoCommit not close, speed will limited.]");
		}
		try{
			Class.forName(driver);
			conn = DriverManager.getConnection(url,user,password);
			conn.setAutoCommit(isAutoCommit);
			stmt = conn.createStatement();
		} catch(Exception e){
			e.printStackTrace();
			close();
		}
	}

    /**
     * 返回数据库查询的ResultSet对象
     * 使用方式不安全
     * 弃用
    * */
    @Deprecated
	public ResultSet rawquery(String sql){
		Statement stmt = this.stmt;
		try{
			rs = stmt.executeQuery(sql);
		} catch(Exception e){
			e.printStackTrace();
			close();
		}
		return rs;
	}

	/**
     * @Description：单sql语句查询
     * @Parameter：sql 查询语句"select [column] from [table] <where [condition] = [param]>
     * @Return：List<HashMap> 查询结果集合，HashMap<(Object)column,(Object)value>
     * @Example: TODO
	* */
	public ArrayList<HashMap> select(String sql){
		LinkedList resultList = new LinkedList();
		Statement stmt = this.stmt;
		try{
			rs = stmt.executeQuery(sql);
			ResultSetMetaData rsmd = rs.getMetaData();
			while(null != rs && rs.next()){
				HashMap metaMap = new HashMap();
				for(int i = 1; i <= rsmd.getColumnCount(); i++){
					metaMap.put(rsmd.getColumnLabel(i),rs.getObject(rsmd.getColumnLabel(i)));
				}
				resultList.add(metaMap);
			}
		} catch(Exception e){
			e.printStackTrace();
			close();
		}
		return new ArrayList<HashMap>(resultList);
	}

    /**
     * @Description: 带参数的sql查询，更加安全，防止注入攻击
     * @Parameter: sql-查询语句 parameters-查询参数
     * @Return: List<HashMap> 查询结果集合，HashMap<(Object)column,(Object)value>
     * @Example:
     *  util.DBUtil db = new util.DBUtil();
     *  String sql = "select id, post_time from weibo where uid = 1730077315";
     *  List param = new ArrayList();
     *  List result = db.query(sql);
     *  for(Object m : result){
     *  Map r = (Map)m;
     *  System.out.println(r.get("id"));
     *  System.out.println(r.get("post_time"));
     *  }
     *  db.close();
     * */
	public ArrayList<HashMap> select(String sql, List<?> parameters){
		LinkedList resultList = new LinkedList();
		Connection conn = this.conn;
		PreparedStatement prestmt = null;
		try{
			prestmt = conn.prepareStatement(sql);
			int index = 0;
			for(Object p : parameters){
				prestmt.setObject(++ index, p);
			}
			rs = prestmt.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			while(null != rs && rs.next()){
				HashMap metaMap = new HashMap();
				for(int i = 1; i <= rsmd.getColumnCount(); i++){
					metaMap.put(rsmd.getColumnLabel(i),rs.getObject(rsmd.getColumnLabel(i)));
				}
				resultList.add(metaMap);
			}
		} catch(Exception e){
			e.printStackTrace();
			close();
		}
		return new ArrayList<HashMap>(resultList);
	}

    /**
     * @Description: 简单查询，只返回一列的结果
     * @Parameters:sql 查询语句"select [column] from [table] <where [condition] = [param]>
     * @Return:List<String> 查询列的集合
    * */
	public ArrayList<String> selectOneColumn(String sql){
		LinkedList<String> resultList = new LinkedList<String>();
		Statement stmt = this.stmt;
		try{
			rs = stmt.executeQuery(sql);
			while(null != rs && rs.next()){
				resultList.add(rs.getString(1));
			}
		} catch(Exception e){
			e.printStackTrace();
			close();
		}
		return new ArrayList<String>(resultList);
	}


	/**
     * @Description: 带参数的更新操作，实际更新执行函数
     * @Parameters: sql-[update/insert/delete] [column] from [table] ...
     * parameters-sql参数
     * @Return: 受影响的列数
	* */
	private int upDate(String sql, List<?> parameters){
		int rows = 0;
		Connection conn = this.conn;
		PreparedStatement prestmt = null;
		try{
			prestmt = conn.prepareStatement(sql);
			int i = 0;
			for(Object p : parameters){
				prestmt.setObject(++ i, p);
				rows ++;
			}
			System.out.println("[util.DBUtil]: <sql Params>:" + parameters);
			prestmt.executeUpdate();
		} catch(Exception e){
			e.printStackTrace();
			close();
		}
		return rows;
	}

	/**
	 * @Description: 删除一行
	 *
	 * */
	public int delete(String deleteSql, List<?> parameters){
		return upDate(deleteSql, parameters);
	}

	/**
	 * @Description： 更新方法函数接口
	 * */
	public int update(String updateSql, List<?> parameters){
		return upDate(updateSql, parameters);
	}


	/**
	 * @Description: 批量更新
	* */
	public void batchUpdate(String updateSql, List<List> listParameters){
		Connection conn = this.conn;
		PreparedStatement prestmt = null;
		try {
			conn.setAutoCommit(false);
			stmt = conn.createStatement();
			prestmt = conn.prepareStatement(updateSql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			close();
		}

		int batchCount = 0;
		for(List<?> item : listParameters){
			batchCount ++;
			try {
				for(int i = 1; i <= item.size(); i++) {
					prestmt.setObject(i, item.get(i - 1));
				}
				prestmt.addBatch();
				if(0 == batchCount % Integer.parseInt(PropertyHandler.getProperty("dbBatchCount"))){
					prestmt.executeBatch();
					prestmt.clearBatch();
					conn.commit();
					batchCount = 0;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				close();
			}
		}

		try{
			prestmt.executeBatch();
			prestmt.clearBatch();
			conn.commit();
		} catch(SQLException e){
			e.printStackTrace();
		} finally{
			close();
		}

	}

	/**
	 * @Description: 根据表名与列名生成sql语句
	 * @Parameters: table- 插入表
	 * columns- 插入列的列表
	 * @Return: sql
	* */
	private String generateInsertSql(String table, List<String> columns){
		final StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append("insert into ");
		sqlBuffer.append(table);
		sqlBuffer.append("(");
		sqlBuffer.append(columns.get(0));
		for(int i = 1; i < columns.size(); i++){
			sqlBuffer.append("," + columns.get(i));
		}
		sqlBuffer.append(") ");
		sqlBuffer.append("values(");
		sqlBuffer.append("?");
		for(int i = 1; i < columns.size(); i++){
			sqlBuffer.append(",?");
		}
		sqlBuffer.append(")");
		return sqlBuffer.toString();
	}

	/**
     * @Description： 插入单行
     * @Parameters： table- 插入表
	 * columns- 插入列的列表
	 * values- 插入列对应值
     * @Return： 受影响行数
	 * @throws IllegalArgumentException if columns size is not equal to values size
	* */
	public int insert(String table, List<String> columns, List values){
		if(columns.size() != values.size()){
			throw new IllegalArgumentException("columns size is not equal to values size");
		}
		String sql = generateInsertSql(table, columns);
	    return upDate(sql, values);
    }


	/**
	 * @Description： 根据表名批量插入
	 * @Parameter： sql-insert into [table](column...) values(value...)
	 * itemsTable-待插入的数据表
	 * @Return： void
	 * */

	public void batchInsert(String table, List<String> columns, List<List> itemsTable){
		String insertSql = generateInsertSql(table, columns);
		batchUpdate(insertSql, itemsTable);
	}


	/**
     * @Description: 关闭所有数据库连接资源
	* */
	public void close(){
		try{
			if(null != rs)
				rs.close();
			if(null != stmt)
				stmt.close();
			if(null != conn)
				conn.close();
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	public static void main(String[] args){

		String table = "table";
		final StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append("insert into ");
		sqlBuffer.append(table);
		sqlBuffer.append("(");
		System.out.print(sqlBuffer.toString());

//		String sql = "update twitter_comment set like_num = ?, repost_num = ? where comment_id = ? and comment_uid = ?";
//		List parameters = new ArrayList();
//		int like_num = 100;
//		parameters.add(like_num);
//		parameters.add(100);
//		parameters.add("750552245956177920");
//		parameters.add("mfeigin");
//		DBUtil db = new DBUtil();
//		db.update(sql, parameters);

	}

}
