package LOLIBRARY.DAO;

import LOLIBRARY.bean.Product;
import LOLIBRARY.bean.Property;
import LOLIBRARY.bean.PropertyValue;
import LOLIBRARY.util.DBUtil;

import java.util.List;
import java.util.ArrayList;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.Statement;

public class PropertyValueDAO {

    public int getTotal(){
        int total =0;
        try(Connection c = DBUtil.getConnection(); Statement s = c.createStatement()){

            String sql ="select count(*) from PropertyValue";
            ResultSet rs = s.executeQuery(sql);
            while (rs.next()){
                total =rs.getInt(1);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return total;
    }

    public void update(PropertyValue bean){

        String sql = "update PropertyValue set pid =? , ptid = ? ,value =? ,where id =?";
        try (Connection c =DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)){

            ps.setInt(1,bean.getProduct().getId());
            ps.setInt(2,bean.getProperty().getId());
            ps.setString(3,bean.getValue());
            ps.setInt(4,bean.getId());
            ps.execute();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void add(PropertyValue bean){

        String sql = "insert into PropertyValue values(null,?,?,?)";
        try(Connection c = DBUtil.getConnection();PreparedStatement ps=c.prepareStatement(sql)){
            ps.setInt(1,bean.getProduct().getId());
            ps.setInt(2,bean.getProperty().getId());
            ps.setString(3,bean.getValue());

            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()){
                int id =rs.getInt(1);
                bean.setId(id);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    public void delete(int id){

        try(Connection c =DBUtil.getConnection();Statement s = c.createStatement()){
            String sql = "delete from PropertyValue where id =" + id;
            s.execute(sql);
        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    public void init(Product p){
        List<Property> pts = new PropertyDao().list(p.getCategory().getId());

        for(Property pt : pts){
            PropertyValue pv = get(pt.getId(),p.getId());
            if(null == pv){
                pv = new PropertyValue();
                pv.setProduct(p);
                pv.setProperty(pt);
                this.add(pv);
            }
        }
    }

    public PropertyValue get(int pid,int ptid){
        PropertyValue bean = new PropertyValue();

        return bean;
    }

    public List<PropertyValue> list(){
        return list(0,Short.MAX_VALUE);
    }

    public List<PropertyValue> list(int start ,int count){
        List<PropertyValue> beans = new ArrayList<PropertyValue>();
        String sql = "select * from Propertyvalue order by id desc limit(?,?)";
        try (Connection c =DBUtil.getConnection();PreparedStatement ps =c.prepareStatement(sql)){
            ps.setInt(1,start);
            ps.setInt(2,count);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                PropertyValue bean =new PropertyValue();
                int id = rs.getInt(1);
                int pid =rs.getInt("pid");
                int ptid =rs.getInt("ptid");
                String value =rs.getString("value");
                Product product = new ProductDao().get(pid);
                Property property =new PropertyDao().get(ptid);

                bean.setId(id);
                bean.setValue(value);
                bean.setProduct(product);
                bean.setProperty(property);
                beans.add(bean);
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        return beans;
    }

    public List<PropertyValue> list(int pid){
        List<PropertyValue> beans = new ArrayList<PropertyValue>();
        String sql = "select from PropertyValue where id = ? order by ptid desc";

        try(Connection c =DBUtil.getConnection(); PreparedStatement ps =c.prepareStatement(sql)){
            ps.setInt(1,pid);
            ResultSet rs =ps.executeQuery();
            while (rs.next()){
                PropertyValue bean = new PropertyValue();
                int id =rs.getInt(1);
                int ptid =rs.getInt("ptid");
                String value =rs.getString("value");
                Property property = new PropertyDao().get(pid);
                Product product =new ProductDao().get(pid);

                bean.setProperty(property);
                bean.setProduct(product);
                bean.setId(id);
                bean.setValue(value);
                beans.add(bean);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        return beans;
    }

}
