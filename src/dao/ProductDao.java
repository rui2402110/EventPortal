package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bean.Product;


// このDAOはうっかりAI生成したものをエイヤと貼り付けただけのものなので正しく動くか知りません
public class ProductDao extends Dao {

    /**
     * 商品を作成するメソッド
     * @param product 商品Bean
     * @return true: 作成成功, false: 作成失敗（重複など）
     */
    public boolean productCreate(Product product) throws Exception {
        Connection connection = getConnection();
        PreparedStatement statement = null;
        boolean result = false;

        try {
            // 既に同じ商品IDが存在するかチェック
            Product existingProduct = get(product.getItemId());
            if (existingProduct == null) {
                // 商品が重複していない場合に処理を実行
                statement = connection.prepareStatement(
                    "INSERT INTO PRODUCTS (" +
                    "event_id, item_id, price, overview, stock, image, product_name" +
                    ") VALUES (?, ?, ?, ?, ?, ?, ?)"
                );

                statement.setString(1, product.getEventId());
                statement.setString(2, product.getItemId());
                statement.setInt(3, product.getPrice());
                statement.setString(4, product.getOverview());
                statement.setInt(5, product.getStock());
                statement.setString(6, product.getImage());
                statement.setString(7, product.getProductName());

                int affected = statement.executeUpdate();
                result = (affected > 0);

            } else {
                return false;
            }

        } catch (Exception e) {
            throw e;
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException sqle) {
                    throw sqle;
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException sqle) {
                    throw sqle;
                }
            }
        }
        return result;
    }

    /**
     * 商品IDから商品を取得するメソッド
     * @param itemId 商品ID
     * @return 商品Bean (存在しない場合はnull)
     */
    public Product get(String itemId) throws Exception {
        Connection connection = getConnection();
        PreparedStatement statement = null;
        Product product = null;

        try {
            statement = connection.prepareStatement(
                "SELECT * FROM PRODUCTS WHERE item_id = ?"
            );
            statement.setString(1, itemId);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                product = new Product();
                product.setEventId(resultSet.getString("event_id"));
                product.setItemId(resultSet.getString("item_id"));
                product.setPrice(resultSet.getInt("price"));
                product.setOverview(resultSet.getString("overview"));
                product.setStock(resultSet.getInt("stock"));
                product.setImage(resultSet.getString("image"));
                product.setProductName(resultSet.getString("product_name"));
            }

        } catch (Exception e) {
            throw e;
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException sqle) {
                    throw sqle;
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException sqle) {
                    throw sqle;
                }
            }
        }
        return product;
    }

    /**
     * イベントIDで商品一覧を取得するメソッド
     * @param eventId イベントID
     * @return 商品リスト
     */
    public List<Product> getByEventId(String eventId) throws Exception {
        Connection connection = getConnection();
        PreparedStatement statement = null;
        List<Product> list = new ArrayList<>();

        try {
            statement = connection.prepareStatement(
                "SELECT * FROM PRODUCTS WHERE event_id = ? ORDER BY item_id"
            );
            statement.setString(1, eventId);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Product product = new Product();
                product.setEventId(resultSet.getString("event_id"));
                product.setItemId(resultSet.getString("item_id"));
                product.setPrice(resultSet.getInt("price"));
                product.setOverview(resultSet.getString("overview"));
                product.setStock(resultSet.getInt("stock"));
                product.setImage(resultSet.getString("image"));
                product.setProductName(resultSet.getString("product_name"));

                list.add(product);
            }

        } catch (Exception e) {
            throw e;
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException sqle) {
                    throw sqle;
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException sqle) {
                    throw sqle;
                }
            }
        }
        return list;
    }

    /**
     * 全商品を取得するメソッド
     * @return 商品リスト
     */
    public List<Product> getAll() throws Exception {
        Connection connection = getConnection();
        PreparedStatement statement = null;
        List<Product> list = new ArrayList<>();

        try {
            statement = connection.prepareStatement(
                "SELECT * FROM PRODUCTS ORDER BY item_id"
            );

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Product product = new Product();
                product.setEventId(resultSet.getString("event_id"));
                product.setItemId(resultSet.getString("item_id"));
                product.setPrice(resultSet.getInt("price"));
                product.setOverview(resultSet.getString("overview"));
                product.setStock(resultSet.getInt("stock"));
                product.setImage(resultSet.getString("image"));
                product.setProductName(resultSet.getString("product_name"));

                list.add(product);
            }

        } catch (Exception e) {
            throw e;
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException sqle) {
                    throw sqle;
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException sqle) {
                    throw sqle;
                }
            }
        }
        return list;
    }

    /**
     * 商品IDを新規に取得するメソッド
     * @return 新しい商品ID (例: ITEM001, ITEM002, ...)
     */
    public String itemIdGet() throws Exception {
        Connection connection = getConnection();
        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement(
                "SELECT item_id FROM PRODUCTS ORDER BY item_id DESC LIMIT 1"
            );
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String lastItemId = resultSet.getString("item_id");
                return incrementItemId(lastItemId);
            } else {
                return "ITEM001";
            }

        } catch (Exception e) {
            throw e;
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException sqle) {
                    throw sqle;
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException sqle) {
                    throw sqle;
                }
            }
        }
    }

    /**
     * 商品IDをインクリメントするメソッド
     * @param currentId 現在の商品ID (例: ITEM001)
     * @return 新しい商品ID (例: ITEM002)
     */
    private static String incrementItemId(String currentId) {
        final String prefix = "ITEM";
        final int idLen = 3;

        // プレフィックスを除いた数値部分を取得
        String numberPart = currentId.substring(prefix.length());
        int number = Integer.parseInt(numberPart);

        // 1を加算
        number++;

        // 新しいIDを生成
        return prefix + String.format("%0" + idLen + "d", number);
    }

    /**
     * 商品を更新するメソッド
     * @param product 商品Bean
     * @return true: 更新成功, false: 更新失敗
     */
    public boolean update(Product product) throws Exception {
        Connection connection = getConnection();
        PreparedStatement statement = null;
        boolean result = false;

        try {
            statement = connection.prepareStatement(
                "UPDATE PRODUCTS SET " +
                "event_id = ?, price = ?, overview = ?, stock = ?, image = ?, product_name = ? " +
                "WHERE item_id = ?"
            );

            statement.setString(1, product.getEventId());
            statement.setInt(2, product.getPrice());
            statement.setString(3, product.getOverview());
            statement.setInt(4, product.getStock());
            statement.setString(5, product.getImage());
            statement.setString(6, product.getProductName());
            statement.setString(7, product.getItemId());

            int affected = statement.executeUpdate();
            result = (affected > 0);

        } catch (Exception e) {
            throw e;
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException sqle) {
                    throw sqle;
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException sqle) {
                    throw sqle;
                }
            }
        }
        return result;
    }

    /**
     * 商品を削除するメソッド
     * @param itemId 商品ID
     * @return true: 削除成功, false: 削除失敗
     */
    public boolean delete(String itemId) throws Exception {
        Connection connection = getConnection();
        PreparedStatement statement = null;
        boolean result = false;

        try {
            statement = connection.prepareStatement(
                "DELETE FROM PRODUCTS WHERE item_id = ?"
            );
            statement.setString(1, itemId);

            int affected = statement.executeUpdate();
            result = (affected > 0);

        } catch (Exception e) {
            throw e;
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException sqle) {
                    throw sqle;
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException sqle) {
                    throw sqle;
                }
            }
        }
        return result;
    }
}