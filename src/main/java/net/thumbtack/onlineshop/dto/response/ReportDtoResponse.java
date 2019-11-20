package net.thumbtack.onlineshop.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReportDtoResponse {

    private List<PurchaseReportDtoResponse> purchases;
    private String result;

    public List<PurchaseReportDtoResponse> getPurchases() {
        return purchases;
    }

    public void setPurchases(List<PurchaseReportDtoResponse> purchases) {
        this.purchases = purchases;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
