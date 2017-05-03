package com.alipay.model.result;

import com.alipay.api.response.AlipayTradeCancelResponse;
import com.alipay.model.TradeStatus;

/**
 * Created by liuyangkly on 15/8/26.
 */
public class AlipayF2FCancelResult implements Result {
    private TradeStatus tradeStatus;
    private AlipayTradeCancelResponse response;

    public AlipayF2FCancelResult(AlipayTradeCancelResponse response) {
        this.response = response;
    }

    public void setTradeStatus(TradeStatus tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    public void setResponse(AlipayTradeCancelResponse response) {
        this.response = response;
    }

    public TradeStatus getTradeStatus() {
        return tradeStatus;
    }

    public AlipayTradeCancelResponse getResponse() {
        return response;
    }

    @Override
    public boolean isTradeSuccess() {
        return response != null &&
                TradeStatus.SUCCESS.equals(tradeStatus);
    }
}
