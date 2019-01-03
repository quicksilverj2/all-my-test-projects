package com.edel.stream.streamtoqueue.util;

import com.edel.streamparse.model.Request;
import com.edel.streamparse.model.StreamingRequest;
import com.edel.streamparse.model.SubscriptionDetail;
import com.edel.streamparse.model.Symbol;
import com.edel.streamparse.util.StreamingTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


/**
 * Created by jitheshrajan on 9/27/18.
 */
public class ESHelper {

    private static final Logger log =
            LoggerFactory.getLogger(ESHelper.class);

    public static final String TIME_WINDOW_FORMAT = "dd MMM yyyy hh:mm:ss";

    public static void closeBufferReader(BufferedReader bufferedReader){
        try{
            if(bufferedReader!=null){
                bufferedReader.close();
            }
        }catch (Exception e){
            log.error("Exception while closing buffer reader ",e);
        }

    }

    public static void closeSocket(Socket socket){
        try{
            if(socket!=null){
                socket.close();
            }
        }catch (Exception e){
            log.error("Exception while closing socket ",e);
        }
    }

    public static String convertEpochToDate(long epoch, String sFormat) {
        try {
            SimpleDateFormat df = new SimpleDateFormat(sFormat);
            if (epoch == 0) {
                throw new NullPointerException();
            }
            return convertEpochToDateFormat(epoch, df);
        } catch (NullPointerException e) {
            return convertEpochToDate(System.currentTimeMillis() / 1000, sFormat);
        }
    }

    public static String convertEpochToDateFormat(long epoch, SimpleDateFormat df) {
        Date date = new Date(epoch);
        String sDate = df.format(date);
        return sDate;
    }

    public static StreamingRequest createQuoteStreamingRequest(){
        List<String> stringSymList = Arrays.asList("11536_NSE","1235_NSE","29124_NSE","-101","-29","-21","211281_MCX","13_NSE","209714_MCX");

        List<Symbol> symbolList = new ArrayList<>();
        for(String sym : stringSymList){
            if(sym!= null && !sym.isEmpty())
            {
                symbolList.add(Symbol.builder().symbol(sym).build());
            }
        }

        SubscriptionDetail subscriptionDetail = SubscriptionDetail.builder().symbols(symbolList).build();

        Request subscriptionDetailRequest = Request.builder()
                .requestType("subscribe")
                .appID("appid-to-be-provided-here")
                .responseFormat("json")
                .streamingType(StreamingTypeEnum.quote.name())
//                .sbscrLst(subscriptionDetail)
                .build();

        subscriptionDetailRequest.setSbscrLst(subscriptionDetail);

        return StreamingRequest.builder().request(subscriptionDetailRequest).build();
    }

}
