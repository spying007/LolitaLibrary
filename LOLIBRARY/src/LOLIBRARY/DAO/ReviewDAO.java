package LOLIBRARY.DAO;

import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;

import LOLIBRARY.bean.Product;
import LOLIBRARY.bean.User;
import LOLIBRARY.util.DBUtil;
import LOLIBRARY.util.DateUtil;
import LOLIBRARY.bean.Review;

public class ReviewDAO {

    public int getTotal(){
        int total =0;
        try(Connection c =DBUtil.getConnection();Statement s = c.createStatement()){
            String sql = "select count(*) from Review";

            ResultSet rs = s.executeQuery(sql);
            while (rs.next()){
                total = rs.getInt(1);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

    public void update(Review bean){
        String sql = "update Review set content= ? , uid= ?, pid=? , createDate = ? where id =?";
        try(Connection c = DBUtil.getConnection() ; PreparedStatement ps =c.prepareStatement(sql)){

            ps.setString(1,bean.getContent());
            ps.setInt(2,bean.getUser().getId());
            ps.setInt(3,bean.getProduct().getId());
            ps.setTimestamp(4,DateUtil.d2t(bean.getCreateDate()));
            ps.setInt(5,bean.getId());
            ps.execute();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void add(Review bean){
        String sql = "insert into Review values(null,?,?,?,?)";

        try(Connection c= DBUtil.getConnection(); PreparedStatement ps =c.prepareStatement(sql)){
            ps.setString(1,bean.getContent());
            ps.setInt(2,bean.getUser().getId());
            ps.setInt(3,bean.getProduct().getId());
            ps.setTimestamp(4,DateUtil.d2t(bean.getCreateDate()));
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();

            if(rs.next()){
                int id = rs.getInt(1);
                bean.setId(id);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    public void delete(int id){
        String sql = "delete from Review where id =" + id;
        try(Connection c = DBUtil.getConnection();Statement s = c.createStatement()){
            s.execute(sql);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public Review get(int id){
        Review bean = new Review();
        try(Connection c =DBUtil.getConnection();Statement s= c.createStatement()){
            String sql ="select from Review where id =" + id;

            ResultSet rs = s.executeQuery(sql);
            if(rs.next()){
                String content =rs.getString("content");
                Date createDate = DateUtil.t2d(rs.getTimestamp("createDate"));
                int pid = rs.getInt("pid");
                int uid =rs.getInt("uid");
                Product product = new ProductDao().get(pid);
                User user = new UserDAO().get(uid);

                bean.setId(id);
                bean.setContent(content);
                bean.setCreateDate(createDate);
                bean.setProduct(product);
                bean.setUser(user);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return bean;
    }

    public List<Review> list(int pid){
        return list(pid,0,Short.MAX_VALUE);
    }

    public List<Review> list(int pid ,int start,int count){
        List<Review> beans = new ArrayList<Review>();
        String sql = "select * from Review where pid = ? order by id desc limit(?,?)";

        try(Connection c= DBUtil.getConnection(); PreparedStatement ps =c.prepareStatement(sql)){
            ps.setInt(1,pid);
            ps.setInt(2,start);
            ps.setInt(3,count);
            ResultSet rs =ps.executeQuery();

            while (rs.next()){
                Review bean = new Review();
                int id =rs.getInt(1);
                String content = rs.getString("content");
                Date createDate = DateUtil.t2d(rs.getTimestamp("createDate"));
                int uid =rs.getInt("uid");
                User user = new UserDAO().get(uid);
                Product product =new ProductDao().get(pid);

                bean.setUser(user);
                bean.setProduct(product);
                bean.setCreateDate(createDate);
                bean.setContent(content);
                bean.setId(id);
                beans.add(bean);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return beans;
    }

    public int getCount(int pid){
        String sql ="select count(*) from Review where pid =?";

        try(Connection c= DBUtil.getConnection(); PreparedStatement ps =c.prepareStatement(sql)){
            ps.setInt(1,pid);
            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                return rs.getInt(1);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return 0;
    }

    public Boolean isExist(String content,int pid){
        String sql = "select * from Review where content =? and pid =?";

        try(Connection c= DBUtil.getConnection();PreparedStatement ps = c.prepareStatement(sql)){
            ps.setString(1,content);
            ps.setInt(2,pid);
            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                return true;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
