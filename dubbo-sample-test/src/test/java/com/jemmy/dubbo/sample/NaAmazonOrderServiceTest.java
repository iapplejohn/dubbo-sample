package com.jemmy.dubbo.sample;

import com.pingpongx.data.central.api.dto.request.SortType;
import com.pingpongx.data.central.api.dto.request.amazonna.NaAmazonOrderRequest;
import com.pingpongx.data.central.api.dto.response.amazonna.NaAmazonOrderResponse;
import com.pingpongx.data.central.api.service.NaAmazonOrderService;
import java.util.Date;
import org.apache.dubbo.config.annotation.Reference;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author zhujiang.cheng
 * @since 2020/7/9
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class NaAmazonOrderServiceTest {

    @Reference(version = "1.0.0",retries = 0,timeout = 90000, parameters = "payload=268435456")
    private NaAmazonOrderService service;

    @Test
    public void queryNaAmazonOrdersTest() {

        NaAmazonOrderRequest request = new NaAmazonOrderRequest();
//        request.setSellerId("A1WP38S7UV453T");
        request.setSellerId("A21LRCDFJ1GGE4");
        request.setPageNo(1);
        request.setPageSize(10000);
//        request.setCurrency("USD");
//        request.setAmazonOrderId("106-3884479-3174635");
//        request.setPurchaseDateStart(ToDate.toDate("20160523 20:00:00","yyyyMMdd HH:mm:ss"));
//        request.setPurchaseDateEnd(ToDate.toDate("20160524 02:19:46","yyyyMMdd HH:mm:ss"));
//        request.setOrderAmountStart(BigDecimal.valueOf(35));
//        request.setOrderAmountEnd(BigDecimal.valueOf(60));
        request.setSortField(NaAmazonOrderRequest.SortField.created);
        request.setSortType(SortType.DESC);
//        request.setPageNo(2);
//        request.setPageSize(5);
        long start = new Date().getTime();
        NaAmazonOrderResponse response = service.queryNaAmazonOrders(request);
        long end = new Date().getTime();
        System.out.println("花费时间："+ (end - start));
        System.out.println("total : "+ response.getTotal());
        System.out.println("pageSize : " + response.getNaAmazonOrderDTOS().size());
        System.out.println(response.getTbId());
//        for (NaAmazonOrderDTO naAmazonOrderDTO : response.getNaAmazonOrderDTOS()) {
////            if (naAmazonOrderDTO.getAmazonOrderId().equals("116-3295352-0437804")) {
//            if (naAmazonOrderDTO.getItems() != null && naAmazonOrderDTO.getItems().size() > 2){
//                System.out.println(JSONObject.toJSONString(naAmazonOrderDTO));
//                System.out.println(naAmazonOrderDTO.getOrderTotalAmount());
//            }
//
////            }
//        }
//        System.out.println(response.getTbId());

        //5bf3fc373d067239d8ce4d64
    }

}
