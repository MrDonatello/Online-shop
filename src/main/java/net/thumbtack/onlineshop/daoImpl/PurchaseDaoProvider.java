package net.thumbtack.onlineshop.daoImpl;

import org.apache.ibatis.jdbc.SQL;

import java.util.Map;

public class PurchaseDaoProvider {

    public String getReport(Map<String, String> map) {
        return new SQL() {
            {
                SELECT("p.productid AS productId, p.name, p.count, p.clientid AS clientId, p.price, date_time AS dateTime");
                FROM("purchases p");

                String criterionCondition = map.get("criterionCondition");
                String additionallyCondition = map.get("additionallyCondition");
                String beginDateCondition = map.get("beginDateCondition");
                String endDateCondition = map.get("endDateCondition");
                String limitCondition = map.get("limitCondition");
                String offsetCondition = map.get("offsetCondition");
                String regularizeCondition = map.get("regularizeCondition");

                if (criterionCondition != null) {
                    switch (criterionCondition) {
                        case "category":
                            FROM(" product_category pc");
                            WHERE("p.productid = pc.productid");
                            if (additionallyCondition != null) {
                                WHERE(" pc.categoryid IN(" + additionallyCondition + ")");
                            }
                            break;
                        case "product":
                            FROM("product pr");
                            WHERE("p.productid = pr.id");
                            if (additionallyCondition != null) {
                                WHERE(" p.productid IN(" + additionallyCondition + ")");
                            }
                            break;
                        case "client":
                            FROM("client c");
                            WHERE("p.clientid = c.userid");
                            if (additionallyCondition != null) {
                                WHERE(" p.clientid IN(" + additionallyCondition + ")");
                            }
                            break;
                    }
                }
                if (beginDateCondition != null && endDateCondition != null) {
                    WHERE("  p.date_time >= " + beginDateCondition + " AND p.date_time  <= " + endDateCondition);
                }
                if (!limitCondition.equals("null") && !offsetCondition.equals("null")) {
                    ORDER_BY(regularizeCondition + " DESC LIMIT " + offsetCondition + ", " + limitCondition);
                } else if (!limitCondition.equals("null")) {
                    ORDER_BY(regularizeCondition + " DESC LIMIT " + limitCondition);
                } else {
                    ORDER_BY(regularizeCondition + " DESC");
                }
            }
        }.toString();

    }
}
