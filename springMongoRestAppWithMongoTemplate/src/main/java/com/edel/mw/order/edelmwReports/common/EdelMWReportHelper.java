package com.edel.mw.order.edelmwReports.common;

import com.edel.mw.order.edelmwReports.common.dao.PositionPriceObject;
import com.edel.mw.order.edelmwReports.v1.dao.PositionBookResponseObject;
import com.edel.mw.order.edelmwReports.v1.dao.TradeBookResponseObject;
import com.edel.mw.order.edelmwReports.v1.dao.TradeFillItem;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by jitheshrajan on 7/4/18.
 */
public class EdelMWReportHelper {

    public static String convertEpochToDateFormat(long epoch, SimpleDateFormat df) {
        Date date = new Date(epoch);
        String sDate = df.format(date);
        System.out.println("Date: "+sDate);
        return sDate;
    }

    public static String convertEpochToDate(long epoch, String sFormat) {
        try {
            SimpleDateFormat df = new SimpleDateFormat(sFormat);
            if(epoch == 0L) {
                throw new NullPointerException();
            } else {
                return convertEpochToDateFormat(epoch, df);
            }
        } catch (NullPointerException var4) {
            return convertEpochToDate(System.currentTimeMillis() / 1000L, sFormat);
        }
    }

    public static String formatDate(String sDate, String fromFormat, String toFormat) {

        long temp = convertDateToEpoch(sDate, fromFormat);
        String result = convertEpochToDate(temp, toFormat);

        return result;
    }

    public static long getDateDiffMillis(String fromDate, String toDate, String format) {
        try {
            Long fromDateMillis = convertDateToEpoch(fromDate, format);
            Long toDateMillis = convertDateToEpoch(toDate, format);
            return toDateMillis - fromDateMillis;
        } catch (Exception var5) {
            return 0L;
        }
    }

    public static long getDateDiff(String fromDate, String toDate, String format, TimeUnit timeUnit) {
        Long dateDiff = getDateDiffMillis(fromDate, toDate, format);
        return dateDiff < 0L ? TimeUnit.valueOf(timeUnit.name()).convert(dateDiff * -1L, TimeUnit.MILLISECONDS) : TimeUnit.valueOf(timeUnit.name()).convert(dateDiff, TimeUnit.MILLISECONDS);
    }

    public static boolean checkIfDateFormat(String sDate, String format) {
        if (sDate != null && !sDate.isEmpty()) {
            try {
                Long var2 = convertDateToEpoch(sDate, format);
                return true;
            } catch (Exception var3) {
                return false;
            }
        } else {
            return false;
        }
    }


    public static long convertDateToEpoch(String sDate, String sFormat) {
        SimpleDateFormat df = new SimpleDateFormat(sFormat);
        Date date = null;
        try {
            date = df.parse(sDate);
        } catch (ParseException e) {
            System.out.println(e.getLocalizedMessage());
        }
        long epoch = date.getTime();

        return epoch;
    }


    public static String calculateIndividualAveragePriceFromWeightedAverage(
            String finalWeightedAverage,
            String finalQuantity,
            String currentWeightedAverage,
            String currentQuantity
    ){

        try{
            Double finalWeightedAverageDouble = Double.parseDouble(finalWeightedAverage);
            Double finalQuantityDouble = Double.parseDouble(finalQuantity);
            Double currentQuantityDouble = Double.parseDouble(currentQuantity);
            Double currentWeightedAverageDouble  = Double.parseDouble(currentWeightedAverage);

            Double individualAgeragePrice = 0.00d;

            individualAgeragePrice =
                    ((finalWeightedAverageDouble * finalQuantityDouble) - (currentWeightedAverageDouble * currentQuantityDouble)) /
                            (finalQuantityDouble - currentQuantityDouble);

            return individualAgeragePrice.toString();
        }catch (Exception e){
            System.out.println("Error while parsing average price");

        }

        return "0.00";

    }


    public static List<PositionBookResponseObject> calculateAveragePriceForPositions(
            List<TradeBookResponseObject> tradeBookResponseObjects
    ){
        HashMap<String, PositionBookResponseObject> positionBookResponseObjectHashMap = new HashMap<>();

        for(TradeBookResponseObject tradeBookResponseObject : tradeBookResponseObjects){
            String uniquePositionKey = tradeBookResponseObject.getSym()+":"+tradeBookResponseObject.getPrdCode();

            System.out.println(uniquePositionKey);

            PositionBookResponseObject positionBookResponseObject = null;

            if(tradeBookResponseObject.getFlDtlLst() == null || tradeBookResponseObject.getFlDtlLst().isEmpty()){
                System.out.println("No fill items present!");
                continue;
            }else{

                // since the fills are arranged in a way that the highest fill id will contain the correct average price
//                of the entire transaction we dont have to do much here!


                PositionPriceObject positionPriceObject = new PositionPriceObject();

                positionPriceObject.setType(tradeBookResponseObject.getTrsTyp());

                Collections.sort(tradeBookResponseObject.getFlDtlLst(), new Comparator<TradeFillItem>() {
                    @Override
                    public int compare(TradeFillItem o1, TradeFillItem o2) {
                        return Integer.parseInt(o2.getFlQty()) - Integer.parseInt(o1.getFlQty());
                    }
                });

                System.out.println(tradeBookResponseObject.getFlDtlLst().get(0).getFlQty());
                System.out.println(tradeBookResponseObject.getFlDtlLst().get(0).getFlID());

                if("BUY".equalsIgnoreCase(tradeBookResponseObject.getTrsTyp())){
                    positionPriceObject.setAvgBuyPrice(Double.parseDouble(tradeBookResponseObject.getFlDtlLst().get(0).getFlPrc()));
                    positionPriceObject.setBuyQty(Long.parseLong(tradeBookResponseObject.getFlDtlLst().get(0).getFlQty()));
                } else if("SELL".equalsIgnoreCase(tradeBookResponseObject.getTrsTyp())){
                    positionPriceObject.setAvgSellPrice(Double.parseDouble(tradeBookResponseObject.getFlDtlLst().get(0).getFlPrc()));
                    positionPriceObject.setSellQty(Long.parseLong(tradeBookResponseObject.getFlDtlLst().get(0).getFlQty()));
                }



                positionBookResponseObject = positionBookResponseObjectHashMap.getOrDefault(
                        uniquePositionKey,
                        new PositionBookResponseObject()
                );

                addAndCalculateWeightToPositionBook(positionBookResponseObject, positionPriceObject);

                positionBookResponseObject.setSym(tradeBookResponseObject.getSym());
                positionBookResponseObject.setPrdCode(tradeBookResponseObject.getPrdCode());

                positionBookResponseObjectHashMap.put(uniquePositionKey, positionBookResponseObject);


            }
        }

        return new ArrayList<PositionBookResponseObject>(positionBookResponseObjectHashMap.values());

    }



    public static List<PositionBookResponseObject> calculateAveragePriceForTradeObjects(
            List<TradeBookResponseObject> tradeBookResponseObjects)
    {

        HashMap<String, PositionBookResponseObject> positionBookResponseObjectHashMap = new HashMap<>();


        for(TradeBookResponseObject tradeBookResponseObject : tradeBookResponseObjects){

            String uniquePositionKey = tradeBookResponseObject.getSym()+":"+tradeBookResponseObject.getPrdCode();

            System.out.println(uniquePositionKey);

            PositionBookResponseObject positionBookResponseObject = null;

            if(tradeBookResponseObject.getFlDtlLst() == null || tradeBookResponseObject.getFlDtlLst().isEmpty()){
                System.out.println("No fill items present!");
                continue;
            }else{

                PositionPriceObject positionPriceObject = new PositionPriceObject();

                positionPriceObject.setType(tradeBookResponseObject.getTrsTyp());

                for(TradeFillItem tradeFillItem : tradeBookResponseObject.getFlDtlLst()){

                    // taking all the items is not necessary since the price is already factored in!

                    if("BUY".equalsIgnoreCase(tradeBookResponseObject.getTrsTyp())){

                        System.out.println("processing buy items!");


                        if(positionPriceObject.getAvgBuyPrice() == 0.00d && positionPriceObject.getBuyQty() == 0L){
                            positionPriceObject.setAvgBuyPrice(Double.parseDouble(tradeFillItem.getFlPrc()));
                            positionPriceObject.setBuyQty(Long.parseLong(tradeFillItem.getFlQty()));
                        }else {

                            positionPriceObject.setAvgBuyPrice(addWeighedFieldToAverage(
                                    positionPriceObject.getAvgBuyPrice(),
                                    positionPriceObject.getBuyQty(),
                                    Double.parseDouble(tradeFillItem.getFlPrc()),
                                    Long.parseLong(tradeFillItem.getFlQty())));

                            positionPriceObject.setBuyQty(Long.parseLong(tradeFillItem.getFlQty()) +positionPriceObject.getBuyQty());


                        }

                    }else if("SELL".equalsIgnoreCase(tradeBookResponseObject.getTrsTyp())){

                        System.out.println("processing sell items!");

                        if(positionPriceObject.getAvgSellPrice() == 0.00d && positionPriceObject.getSellQty() == 0L){
                            positionPriceObject.setAvgSellPrice(Double.parseDouble(tradeFillItem.getFlPrc()));
                            positionPriceObject.setSellQty(Long.parseLong(tradeFillItem.getFlQty()));
                        }else {

                            positionPriceObject.setAvgSellPrice(addWeighedFieldToAverage(
                                    positionPriceObject.getAvgSellPrice(),
                                    positionPriceObject.getSellQty(),
                                    Double.parseDouble(tradeFillItem.getFlPrc()),
                                    Long.parseLong(tradeFillItem.getFlQty())));

                            positionPriceObject.setSellQty(Long.parseLong(tradeFillItem.getFlQty()) +positionPriceObject.getSellQty());

                        }
                    }

                    positionBookResponseObject = positionBookResponseObjectHashMap.getOrDefault(
                            uniquePositionKey,
                            new PositionBookResponseObject()
                    );

                    addAndCalculateWeightToPositionBook(positionBookResponseObject, positionPriceObject);


                    positionBookResponseObjectHashMap.put(uniquePositionKey, positionBookResponseObject);

                }
            }
        }

        return new ArrayList<PositionBookResponseObject>(positionBookResponseObjectHashMap.values());

    }

    private static void addAndCalculateWeightToPositionBook(
            PositionBookResponseObject positionBookResponseObject,
            PositionPriceObject positionPriceObject)
    {

        if(positionBookResponseObject == null || positionPriceObject == null){
            System.out.println("Error while parsing position book! : something was null!");
        }else{

            if("BUY".equalsIgnoreCase(positionPriceObject.getType())){


                positionBookResponseObject.setAvgBPrc(BigDecimal.valueOf(positionPriceObject.getAvgBuyPrice()).toPlainString());
                positionBookResponseObject.setNtBQty(positionPriceObject.getBuyQty()+"");


                if(positionBookResponseObject.getAvgBPrc() == null || positionBookResponseObject.getAvgBPrc().isEmpty()){
                    positionBookResponseObject.setAvgBPrc(BigDecimal.valueOf(positionPriceObject.getAvgBuyPrice()).toPlainString());
                    positionBookResponseObject.setNtBQty(positionPriceObject.getBuyQty()+"");
                }else{
                    positionBookResponseObject.setAvgBPrc(
                            BigDecimal.valueOf(addWeighedFieldToAverage(
                                    Double.parseDouble(positionBookResponseObject.getAvgBPrc()),
                                    Long.parseLong(positionBookResponseObject.getNtBQty()),
                                    positionPriceObject.getAvgBuyPrice(),
                                    positionPriceObject.getBuyQty())).toPlainString());

                    positionBookResponseObject.setNtBQty(
                            (Long.parseLong(positionBookResponseObject.getNtBQty()) + positionPriceObject.getBuyQty())+""
                    );


                }


                positionBookResponseObject.setNtBAmt(
                        BigDecimal.valueOf(
                                Double.parseDouble(positionBookResponseObject.getAvgBPrc()) * Long.parseLong(positionBookResponseObject.getNtBQty())
                        ).toPlainString()
                );

            }else if("SELL".equalsIgnoreCase(positionPriceObject.getType())){

                positionBookResponseObject.setAvgSPrc(BigDecimal.valueOf(positionPriceObject.getAvgSellPrice()).toPlainString());
                positionBookResponseObject.setNtSQty(positionPriceObject.getSellQty()+"");

                if(positionBookResponseObject.getAvgSPrc() == null || positionBookResponseObject.getAvgSPrc().isEmpty()){
                    positionBookResponseObject.setAvgSPrc(BigDecimal.valueOf(positionPriceObject.getAvgSellPrice()).toPlainString());
                    positionBookResponseObject.setNtSQty(positionPriceObject.getSellQty()+"");
                }else{
                    positionBookResponseObject.setAvgSPrc(
                            BigDecimal.valueOf(addWeighedFieldToAverage(
                                    Double.parseDouble(positionBookResponseObject.getAvgSPrc()),
                                    Long.parseLong(positionBookResponseObject.getNtSQty()),
                                    positionPriceObject.getAvgSellPrice(),
                                    positionPriceObject.getSellQty())).toPlainString());

                    positionBookResponseObject.setNtSQty(
                            (Long.parseLong(positionBookResponseObject.getNtSQty()) + positionPriceObject.getSellQty())+""
                    );


                }

                positionBookResponseObject.setNtSAmt(
                        BigDecimal.valueOf(
                                Double.parseDouble(positionBookResponseObject.getAvgSPrc()) * Long.parseLong(positionBookResponseObject.getNtSQty())
                        ).toPlainString()
                );

            }else{
                System.out.println("Invalid transaction type here!");
            }

        }


        if(positionBookResponseObject.getNtAmt() != null && !positionBookResponseObject.getNtAmt().isEmpty()){
            // need to recalculate net amount

            System.out.println(positionBookResponseObject.getNtBAmt() +" "+ positionBookResponseObject.getNtSAmt());

            System.out.println(positionBookResponseObject.getNtBQty() + " "+positionBookResponseObject.getNtSQty());

            if(positionBookResponseObject.getNtBAmt() == null){
                positionBookResponseObject.setNtBAmt("0.00");
                positionBookResponseObject.setNtBQty("0");
            }

            if(positionBookResponseObject.getNtSAmt() == null){
                positionBookResponseObject.setNtSAmt("0.00");
                positionBookResponseObject.setNtSQty("0");
            }

            positionBookResponseObject.setNtAmt(
                    BigDecimal.valueOf(Double.parseDouble(positionBookResponseObject.getNtBAmt()) -
                            Double.parseDouble(positionBookResponseObject.getNtSAmt())).toPlainString()
            );

            positionBookResponseObject.setNtQty(
                    (Long.parseLong(positionBookResponseObject.getNtBQty()) -
                            Long.parseLong(positionBookResponseObject.getNtSQty()))+""
            );


        }else{
            // fresh case!

            if("SELL".equalsIgnoreCase(positionPriceObject.getType())){
                positionBookResponseObject.setNtAmt(positionBookResponseObject.getNtSAmt());
                positionBookResponseObject.setNtQty(positionBookResponseObject.getNtSQty());
            } else if("BUY".equalsIgnoreCase(positionPriceObject.getType())){
                positionBookResponseObject.setNtAmt(positionBookResponseObject.getNtBAmt());
                positionBookResponseObject.setNtQty(positionBookResponseObject.getNtBQty());
            }



        }



    }

    private static Double addWeighedFieldToAverage(
            Double avgPrice,
            Long totalQuantity,
            Double newPrice,
            Long newQuantity)
    {

        return (avgPrice * totalQuantity + newPrice* newQuantity)/(totalQuantity + newQuantity);


    }
}
