package LOLIBRARY.DAO;

import LOLIBRARY.bean.Category;
import LOLIBRARY.bean.Property;

import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import LOLIBRARY.util.DBUtil;
import javafx.scene.SnapshotParametersBuilder;


public class PropertyDao {

    public int getTotal(){
        int total =0;

        try(Connection c= DBUtil.getConnection();Statement s= c.createStatement()){
            String sql = "select count(*) from Property";
            ResultSet rs = s.executeQuery(sql);
            while (rs.next()){
                total = rs.getInt(1);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return total;
    }

    public void add(Property bean) {

        String sql = "insert into Property value(null,?,?)";
        try(Connection c= DBUtil.getConnection();PreparedStatement ps= c.prepareStatement(sql)){

            ps.setInt(1,bean.getCategory().getId());
            ps.setString(2,bean.getName());
            ps.execute();

            ResultSet  rs= ps.getGeneratedKeys();
            if(rs.next()){
                int id = rs.getInt(1);
                bean.setId(id);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void delete(int id){
        try(Connection c= DBUtil.getConnection();Statement s= c.createStatement()){
            String sql ="delete from Property where id =" + id;
            s.execute(sql);
        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    public void update(Property bean){
        String sql ="update from Property set cid= ? name= ? where id =? ";
        try (Connection c =DBUtil.getConnection();PreparedStatement ps= c.prepareStatement(sql)){
            ps.setInt(1,bean.getCategory().getId());
            ps.setString(2,bean.getName());
            ps.setInt(3,bean.getId());
            ps.execute();
        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    public Property get(int id){
        Property bean =new Property();
        String sql ="select * from Property where id =" + id;
        try (Connection c= DBUtil.getConnection();PreparedStatement ps= c.prepareStatement(sql)){
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                int cid = rs.getInt("cid");
                String name =rs.getString("name");
                Category category = new CategoryDao().get(cid);

                bean.setCategory(category);
                bean.setName(name);
                bean.setId(id);
            }


        }catch (SQLException e){
            e.printStackTrace();
        }
        return bean;
    }

    public List<Property> list(int cid){
        return list(cid,0,Short.MAX_VALUE);
    }

    public List<Property> list(int cid, int start, int count){
        List<Property> beans = new ArrayList<Property>();

        String sql = "select * from Property where cid = ? order by id desc limit ?,?";
        try(Connection c = DBUtil.getConnection();PreparedStatement ps =c.prepareStatement(sql)){

            ps.setInt(1,cid);
            ps.setInt(2,start);
            ps.setInt(3,count);

            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Property bean= new Property();
                String name = rs.getString("name");
                int id = rs.getInt("id");
                Category category = new CategoryDao().get(cid);

                bean.setId(id);
                bean.setName(name);
                bean.setCategory(category);
                beans.add(bean);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return beans;
    }
}
