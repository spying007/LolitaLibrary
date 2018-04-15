package LOLIBRARY.DAO;

import LOLIBRARY.bean.Product;
import LOLIBRARY.bean.ProductImage;
import LOLIBRARY.util.DBUtil;
import com.sun.jndi.cosnaming.CNCtx;
import com.sun.prism.impl.PrismTrace;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

public class ProductImageDao {
    public static final String type_single = "type_singel";
    public static final String type_detail = "type_datail";

    public int getTotal(){
        int total =0;

        try(Connection c= DBUtil.getConnection(); Statement s= c.createStatement()){
            String sql = "select count(*) from ProductImage";

            ResultSet rs = s.executeQuery(sql);
            while (rs.next()){
                total = rs.getInt(1);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return total;
    }

    public void add(ProductImage bean){
        String sql = "insert into ProductImage values(null,?,?)";

        try(Connection c= DBUtil.getConnection(); PreparedStatement ps= c.prepareStatement(sql)){
            ps.setInt(1,bean.getProduct().getId());
            ps.setString(2,bean.getType());
            ps.execute();

            ResultSet rs = ps.getGeneratedKeys();
            while (rs.next()){
                int id = rs.getInt("id");
                bean.setId(id);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void delete(int id){

        try(Connection c = DBUtil.getConnection();Statement s = c.createStatement()){
            String sql = "delete from ProductImage where id =" + id;
            s.execute(sql);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public ProductImage get(int id){
        ProductImage bean = new ProductImage();

        try(Connection c =DBUtil.getConnection();Statement s=c.createStatement()){

            String sql = "select * from ProductImage where id =" +id;
            ResultSet rs = s.executeQuery(sql);

            if(rs.next()){
                String type =rs.getString("type");
                int pid =rs.getInt("pid");
                Product product = new ProductDao().get(pid);

                bean.setId(id);
                bean.setProduct(product);
                bean.setType(type);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return bean;
    }

    public void update(ProductImage bean){

    }

    public List<ProductImage> list(Product p, String type, int start ,int count){
        List<ProductImage> beans= new ArrayList<ProductImage>();
        String sql = "select * from ProductImage where pid= ? type = ? order by id desc limit ?,? ";

        try(Connection c= DBUtil.getConnection();PreparedStatement ps = c.prepareStatement(sql)){

            ps.setInt(1,p.getId());
            ps.setString(2,type);
            ps.setInt(3,start);
            ps.setInt(4,count);
            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                ProductImage bean = new ProductImage();
                int id =rs.getInt(1);

                bean.setType(type);
                bean.setId(id);
                bean.setProduct(p);
                beans.add(bean);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return beans;
    }

    public List<ProductImage> list(Product p, String type){
        return list(p,type,0,Short.MAX_VALUE);
    }

}
