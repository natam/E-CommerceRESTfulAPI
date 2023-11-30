package org.rest.dao;

import org.rest.db.DBConnection;
import org.rest.entity.Order;
import org.rest.entity.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderDao {
    public List<Order> getOrders(int offset, int limit) {
        List<Order> orderstList = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection()) {
            String sql = "SELECT * FROM Orders LIMIT ? OFFSET ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1,limit);
            stmt.setInt(2,offset);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Order order = new Order(rs.getInt("id"), rs.getInt("productId"), rs.getInt("quantity"), rs.getString("customer"));
                orderstList.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderstList;
    }

    public List<Order> getOrdersByCustomer(int offset, int limit, String customer) {
        List<Order> orderstList = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection()) {
            String sql = "SELECT * FROM Orders LIMIT ? OFFSET ? WHERE customer = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1,limit);
            stmt.setInt(2,offset);
            stmt.setString(3, customer);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Order order = new Order(rs.getInt("id"), rs.getInt("productId"), rs.getInt("quantity"), rs.getString("customer"));
                orderstList.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderstList;
    }

    public Order getOrder(int id) {
        try (Connection connection = DBConnection.getConnection()) {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM Orders WHERE id = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Order(rs.getInt("id"), rs.getInt("productId"), rs.getInt("quantity"), rs.getString("customer"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //Transaction for order creation
    public void addOrder(Order order) {
        try (Connection connection = DBConnection.getConnection()) {
            connection.setAutoCommit(false);
            Savepoint savepoint = connection.setSavepoint();
            String addOrderSql = "INSERT INTO Orders (productId, quantity, customer) VALUES (?, ?, ?)";
            String getProduct = "SELECT * FROM Products WHERE id = ? LIMIT 1";
            String editProduct = "UPDATE Products SET stockQuantity = ? WHERE id = ?";
            int quantityAvailable = 0;

            try (PreparedStatement selectProduct = connection.prepareStatement(getProduct)){
                selectProduct.setInt(1, order.getProductId());
                ResultSet rs = selectProduct.executeQuery();
                rs.next();
                quantityAvailable = rs.getInt("stockQuantity");
                if(quantityAvailable<order.getQuantity()){
                    throw new SQLException("Not enough quantity in stock");
                }
            }

            try(PreparedStatement insertOrder = connection.prepareStatement(addOrderSql)) {
                insertOrder.setInt(1, order.getProductId());
                insertOrder.setInt(2, order.getQuantity());
                insertOrder.setString(3, order.getCustomer());
                insertOrder.executeUpdate();
            }

            try(PreparedStatement updateProduct = connection.prepareStatement(editProduct)) {
                updateProduct.setInt(1, quantityAvailable - order.getQuantity());
                updateProduct.setInt(2, order.getProductId());
                updateProduct.executeUpdate();
            }

            catch (SQLException ex){
                connection.rollback();
                ex.printStackTrace();
            }
            finally {
                connection.setAutoCommit(true);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateOrder(Order order) {
        try (Connection connection = DBConnection.getConnection()) {
            PreparedStatement ps = connection.prepareStatement("UPDATE Orders SET productId = ?, quantity = ?, customer = ? WHERE id = ?");
            ps.setInt(1, order.getProductId());
            ps.setInt(2, order.getQuantity());
            ps.setString(3, order.getCustomer());
            ps.setInt(4, order.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteOrder(int id) {
        try (Connection connection = DBConnection.getConnection()) {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM Orders WHERE id = ?");
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
