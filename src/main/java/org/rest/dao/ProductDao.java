package org.rest.dao;

import org.rest.db.DBConnection;
import org.rest.entity.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductDao {
    public List<Product> getAllProducts(int offset, int limit) {
        List<Product> productList = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection()) {
            String sql = "SELECT * FROM Products LIMIT ? OFFSET ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1,limit);
            stmt.setInt(2,offset);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Product product = new Product(rs.getInt("id"), rs.getString("name"), rs.getDouble("price"), rs.getString("category"), rs.getInt("stockQuantity"));
                productList.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productList;
    }

    public Product getProduct(int id) {
        try (Connection connection = DBConnection.getConnection()) {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM Products WHERE id = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Product(rs.getInt("id"), rs.getString("name"), rs.getDouble("price"), rs.getString("category"), rs.getInt("stockQuantity"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Optional<Product> getProduct(Product product) {
        Optional<Product> foundProduct = Optional.empty();
        try (Connection connection = DBConnection.getConnection()) {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM Products WHERE name = ? AND category = ? AND price = ?");
            ps.setString(1, product.getName());
            ps.setString(2, product.getCategory());
            ps.setDouble(3, product.getPrice());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                foundProduct = Optional.of(new Product(rs.getInt("id"), rs.getString("name"), rs.getDouble("price"), rs.getString("category"), rs.getInt("stockQuantity")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return foundProduct;
    }

    public void addProduct(Product product) {
        try (Connection connection = DBConnection.getConnection()) {
            String sql = "INSERT INTO Products (name, category, price, stockQuantity) VALUES (?, ?, ?, ?)";
            Optional<Product> foundProduct = getProduct(product);
            if(foundProduct.isEmpty()){
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setString(1, product.getName());
                ps.setString(2, product.getCategory());
                ps.setDouble(3, product.getPrice());
                ps.setInt(4, product.getStockQuantity());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateProduct(Product product) {
        try (Connection connection = DBConnection.getConnection()) {
            PreparedStatement ps = connection.prepareStatement("UPDATE Products SET name = ?, category = ?, price = ?, stockQuantity = ? WHERE id = ?");
            ps.setString(1, product.getName());
            ps.setString(2, product.getCategory());
            ps.setDouble(3,product.getPrice());
            ps.setInt(4, product.getStockQuantity());
            ps.setInt(5, product.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteProduct(int id) {
        try (Connection connection = DBConnection.getConnection()) {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM Products WHERE id = ?");
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
