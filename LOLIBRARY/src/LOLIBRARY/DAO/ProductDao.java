package LOLIBRARY.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;

import LOLIBRARY.bean.User;
import LOLIBRARY.util.DBUtil;
import LOLIBRARY.bean.Product;
import LOLIBRARY.bean.ProductImage;
import LOLIBRARY.bean.Category;
import LOLIBRARY.util.DateUtil;
import sun.security.pkcs11.Secmod;


public class ProductDao {

    public int getTotal(int cid){
        int total =0;
        try (Connection c = DBUtil.getConnection();Statement s =c.createStatement()){

            String sql = "select (count)* from Product where cid =" +cid;

            ResultSet rs = s.executeQuery(sql);
            while (rs.next()){
                total=rs.getInt(1);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return total;
    }

    public void add(Product bean){
        String sql = "insert into ProductValues(null,?,?,?,?,?,?,?)";

        try (Connection c= DBUtil.getConnection();PreparedStatement ps =c.prepareStatement(sql)){

            ps.setString(1,bean.getName());
            ps.setString(2,bean.getSubTitle());
            ps.setFloat(3,bean.getOriginalPrice());
            ps.setFloat(4,bean.getPromotionPrice());
            ps.setInt(5,bean.getStock());
            ps.setInt(6,bean.getCategory().getId());
            ps.setTimestamp(7, DateUtil.d2t(bean.getCreateDate()));
            ps.execute();

            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                int id =rs.getInt(1);
                bean.setId(id);
            }
            }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void delete(int id){
        try (Connection c = DBUtil.getConnection();Statement s =c.createStatement()){

            String sql ="delete from Product where id =" + id;
            s.execute(sql);

        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    public void update(Product bean){
        String sql = "update Product set name= ?, SubTitle= ?, OriginalPrice= ?, PromotionPrice= ?, Stock =? ,cid =?, CreateDate= ?, where id= ?";
        try(Connection c =DBUtil.getConnection(); PreparedStatement ps =c.prepareStatement(sql)){

            ps.setString(1,bean.getName());
            ps.setString(2,bean.getSubTitle());
            ps.setFloat(3,bean.getOriginalPrice());
            ps.setFloat(4,bean.getOriginalPrice());
            ps.setInt(5,bean.getStock());
            ps.setInt(6,bean.getCategory().getId());
            ps.setTimestamp(7,DateUtil.d2t(bean.getCreateDate()));
            ps.setInt(8,bean.getId());

        }catch (SQLException e){
            e.printStackTrace();
        }


    }

    public Product get(int id){
        Product bean = new Product();

        try(Connection c =DBUtil.getConnection(); Statement s=c.createStatement()){

            String sql = "select from Product where id =" + id;
            ResultSet rs = s.executeQuery(sql);
            if(rs.next()){
                String name = rs.getString("name");
                String subTitle = rs.getString("subTitle");
                Float OriginalPrice = rs.getFloat("OriginalPrice");
                Float PromotionPrice = rs.getFloat("PromotionPrice");
                int stock = rs.getInt("Stock");
                int cid = rs.getInt("cid");
                Date createDate = rs.getDate("createDate");

                bean.setName(name);
                bean.setSubTitle(subTitle);
                bean.setOriginalPrice(OriginalPrice);
                bean.setPromotionPrice(PromotionPrice);
                bean.setStock(stock);
                Category category = new CategoryDao().get(cid);
                bean.setCategory(category);
                bean.setCreateDate(createDate);
                bean.setId(id);

                setFirstProductImage(bean);
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        return bean;
    }

    public List<Product> list(){
        return list(0,Short.MAX_VALUE);
    }

    public List<Product> list(int start,int count){
        List<Product> beans = new ArrayList<Product>();

        String sql ="select * from Product limit ?,?";

        try(Connection c =DBUtil.getConnection();PreparedStatement ps = c.prepareStatement(sql)){
            ps.setInt(1,start);
            ps.setInt(2,count);
            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                Product bean = new Product();
                String name = rs.getString("name");
                String subTitle = rs.getString("subTitle");
                Float originalPrice = rs.getFloat("originalPrice");
                Float promotionPrice = rs.getFloat("promotionPrice");
                int stock = rs.getInt("stock");
                int cid = rs.getInt("cid");
                Date createdate = rs.getDate("createDate");

                bean.setName(name);
                bean.setSubTitle(subTitle);
                bean.setOriginalPrice(originalPrice);
                bean.setPromotionPrice(promotionPrice);
                bean.setStock(stock);
                Category category = new CategoryDao().get(cid);
                bean.setCategory(category);
                beans.add(bean);
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        return beans;
    }

    public List<Product> list(int cid){
        return list(cid,0,Short.MAX_VALUE);
    }
    public List<Product> list(int cid,int start,int count){
        List<Product> beans = new ArrayList<Product>();
        Category category = new CategoryDao().get(cid);

        String sql = "select * from Product where cid =? order by id desc limit ?,?";
        try(Connection c= DBUtil.getConnection();PreparedStatement ps = c.prepareStatement(sql)){

            ps.setInt(1,cid);
            ps.setInt(2,start);
            ps.setInt(3,count);
            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                Product bean =new Product();
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String subTitle = rs.getString("subTitle");
                Float originalPrice = rs.getFloat("originalPrice");
                Float promotionPrice = rs.getFloat("promotionPrice");
                int stock = rs.getInt("stock");
                Date createDate = rs.getDate("createDate");

                bean.setName(name);
                bean.setSubTitle(subTitle);
                bean.setOriginalPrice(originalPrice);
                bean.setPromotionPrice(promotionPrice);
                bean.setStock(stock);
                bean.setId(id);
                bean.setCategory(category);
                setFirstProductImage(bean);
                beans.add(bean);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return beans;
    }
    public void fill(Category c){
        List<Product> ps = this.list(c.getId());
        c.setProducts(ps);
    }
    public void fill(List<Category> cs){
        for(Category c : cs){
            fill(c);
        }
    }
    public void fillByRow(List<Category> cs){
        final int productNumberEachRow =8;
        for(Category c : cs){
            List<Product> products = c.getProducts();
            List<List<Product>> productsByRow = new ArrayList<>();
            for(int i=0;i<products.size();i+=productNumberEachRow){
                int size = i+productNumberEachRow;
                size= size>products.size()?products.size():size;
                List<Product> productsOfEachRow= products.subList(i,size);
                productsByRow.add(productsOfEachRow);
            }
            c.setProductsByRow(productsByRow);
        }

    }
    public List<Product> search(String keyword,int start,int count){
        List<Product> beans = new ArrayList<Product>();
        if(null==keyword||0==keyword.trim().length()){
            return beans;
        }

        String sql = "select * from Product where name like ? limit ?,? ";

        try (Connection c =DBUtil.getConnection();PreparedStatement ps =c.prepareStatement(sql)){
            ps.setString(1,"%" +keyword.trim()+ "%");
            ps.setInt(2,start);
            ps.setInt(3,count);

            ResultSet rs =ps.executeQuery();
            while (rs.next()){
                Product bean =new Product();
                int id =rs.getInt(1);
                int cid = rs.getInt("cid");
                String name = rs.getString("name");
                String subTitle =rs.getString("subTitle");
                float originalPrice =rs.getFloat("originalPrice");
                float promotionPrice = rs.getFloat("promotionPrice");
                int stock = rs.getInt("stock");
                Date createDate = DateUtil.t2d(rs.getTimestamp("createDate"));

                bean.setName(name);
                bean.setSubTitle(subTitle);
                bean.setOriginalPrice(originalPrice);
                bean.setPromotionPrice(promotionPrice);
                bean.setStock(stock);
                bean.setId(id);
                bean.setCreateDate(createDate);

                Category category = new CategoryDao().get(cid);
                bean.setCategory(category);
                setFirstProductImage(bean);
                beans.add(bean);
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        return beans;
    }
    public void setFirstProductImage(Product p){
        List<ProductImage> pis =new ProductImageDao().list(p,ProductImageDao.type_single);
        if(!pis.isEmpty()){
            p.setFirstProductImage(pis.get(0));
        }

    }

}
