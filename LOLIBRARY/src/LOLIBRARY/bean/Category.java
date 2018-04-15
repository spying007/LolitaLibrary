package LOLIBRARY.bean;

import java.util.List;

public class Category {
    private String name;
    private  int id;
    List<Product> products;
    List<List<Product>> productsByRow;

    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }
    @Override
    public String toString(){
        return "Category [name=" +name + "]";
    }
    public List<Product> getProducts() {
        return products;
    }
    public List<List<Product>> getProductsByRow() {
        return productsByRow;
    }
    public void setProducts(List<Product> products) {
        this.products = products;
    }
    public void setProductsByRow(List<List<Product>> productsByRow) {
        this.productsByRow = productsByRow;
    }
}
